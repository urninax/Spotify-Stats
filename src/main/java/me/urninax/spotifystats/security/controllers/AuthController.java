package me.urninax.spotifystats.security.controllers;

import jakarta.validation.Valid;
import me.urninax.spotifystats.security.dto.*;
import me.urninax.spotifystats.security.models.RefreshToken;
import me.urninax.spotifystats.security.models.User;
import me.urninax.spotifystats.security.services.*;
import me.urninax.spotifystats.security.utils.JWTUtil;
import me.urninax.spotifystats.security.utils.UserValidator;
import me.urninax.spotifystats.security.utils.exceptions.RefreshTokenExpiredException;
import me.urninax.spotifystats.security.utils.exceptions.RefreshTokenNotFoundException;
import me.urninax.spotifystats.security.utils.responses.RefreshTokenErrorResponse;
import me.urninax.spotifystats.security.utils.responses.UserErrorResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController{
    private final RegistrationService registrationService;
    private final UserValidator userValidator;
    private final ModelMapper modelMapper;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;
    private final JWTUtil jwtUtil;
    private final UserService userService;

    @Value("${jwtExpirationMs}")
    private Long expirationMs;

    public AuthController(RegistrationService registrationService,
                          UserValidator userValidator,
                          ModelMapper modelMapper,
                          AuthenticationManager authenticationManager,
                          RefreshTokenService refreshTokenService,
                          JWTUtil jwtUtil, UserService userService){

        this.registrationService = registrationService;
        this.userValidator = userValidator;
        this.modelMapper = modelMapper;
        this.authenticationManager = authenticationManager;
        this.refreshTokenService = refreshTokenService;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody @Valid SignupDTO signupDTO,
                                          BindingResult bindingResult){
        User user = convertToUser(signupDTO);

        userValidator.validate(user, bindingResult);

        if(bindingResult.hasErrors()){ //validate body
            List<FieldError> errors = bindingResult.getFieldErrors();

            StringBuilder stringBuilder = new StringBuilder();

            for(FieldError error : errors){
                stringBuilder.append(error.getField())
                        .append(" - ")
                        .append(error.getDefaultMessage())
                        .append("; ");
            }

            UserErrorResponse userErrorResponse = new UserErrorResponse( //generate error response if something's wrong
                    stringBuilder.toString(),
                    LocalDateTime.now());

            return new ResponseEntity<>(userErrorResponse, HttpStatus.CONFLICT);
        }

        User registeredUser = registrationService.register(user);

        RefreshToken refreshToken = refreshTokenService.createToken(registeredUser); //create new refresh token with freshly registered user's id

        SignupResponseDTO signupResponseDTO = new SignupResponseDTO(
                "User created",
                refreshToken.getToken(),
                Instant.now()
        );

        return new ResponseEntity<>(signupResponseDTO, HttpStatus.CREATED);
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signinUser(@RequestBody SigninDTO signinDTO) throws RefreshTokenNotFoundException{
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                signinDTO.getUsername(), signinDTO.getPassword()
        );

        Authentication authentication;

        try{
            authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken); //authenticate user with username and password
        }catch(AuthenticationException e){
            UserErrorResponse userErrorResponse = new UserErrorResponse("Invalid credentials", LocalDateTime.now());
            return new ResponseEntity<>(userErrorResponse, HttpStatus.BAD_REQUEST);
        }

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        User userFromPrincipal = userDetails.getUser();
        RefreshToken currentRefreshToken = userFromPrincipal.getRefreshToken();

        try{
            refreshTokenService.verifyExpiration(currentRefreshToken.getToken()); //verify current user's refresh token expiry time
        }catch(RefreshTokenExpiredException exc){
            currentRefreshToken = refreshTokenService.updateToken(userFromPrincipal); //create new token if current is expired
        }

        String accessToken = jwtUtil.generateToken(userFromPrincipal.getUsername());

        SigninResponseDTO signinResponseDTO = new SigninResponseDTO( //generate response with data
                userDetails.getUsername(),
                currentRefreshToken.getToken(), //if token was expired -> shows new token
                accessToken,
                "Successfully signed in",
                Instant.now()
        );
        return new ResponseEntity<>(signinResponseDTO, HttpStatus.OK);
    }

    @PostMapping("/refresh-access-token")
    public ResponseEntity<?> refreshAccessToken(@RequestBody @Valid RefreshTokenRequestDTO refreshTokenRequestDTO,
                                                BindingResult bindingResult, WebRequest request) throws RefreshTokenExpiredException, RefreshTokenNotFoundException{

        if(bindingResult.hasErrors()){ //validate body
            RefreshTokenErrorResponse response = new RefreshTokenErrorResponse(
                    HttpStatus.NO_CONTENT.value(),
                    Instant.now(),
                    bindingResult.getFieldErrors().get(0).getDefaultMessage(),
                    request.getDescription(false).substring(4)
            );

            return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
        }

        String requestRefreshToken = refreshTokenRequestDTO.getRefreshToken();

        RefreshToken refreshToken = refreshTokenService.verifyExpiration(requestRefreshToken); //verify refresh token's expiry time from body
        String accessToken = jwtUtil.generateToken(refreshToken.getUser().getUsername()); //generate new jwt-token from refresh token owner username

        Instant timestamp = Instant.now();
        RefreshAccessTokenResponse response = new RefreshAccessTokenResponse(  //generate response with data
                accessToken,
                timestamp,
                timestamp.plusMillis(expirationMs)
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public User convertToUser(SignupDTO signupDTO){
        return modelMapper.map(signupDTO, User.class);
    }
}
