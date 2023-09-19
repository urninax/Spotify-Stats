package me.urninax.spotifystats.security.controllers;

import jakarta.validation.Valid;
import me.urninax.spotifystats.security.dto.*;
import me.urninax.spotifystats.security.models.RefreshToken;
import me.urninax.spotifystats.security.models.User;
import me.urninax.spotifystats.security.services.RefreshTokenService;
import me.urninax.spotifystats.security.services.RegistrationService;
import me.urninax.spotifystats.security.services.UserDetailsImpl;
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

@RestController
@RequestMapping("/api/auth")
public class AuthController{
    private final RegistrationService registrationService;
    private final UserValidator userValidator;
    private final ModelMapper modelMapper;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;
    private final JWTUtil jwtUtil;

    @Value("${jwtExpirationMs}")
    private Long expirationMs;

    public AuthController(RegistrationService registrationService,
                          UserValidator userValidator,
                          ModelMapper modelMapper,
                          AuthenticationManager authenticationManager,
                          RefreshTokenService refreshTokenService,
                          JWTUtil jwtUtil){

        this.registrationService = registrationService;
        this.userValidator = userValidator;
        this.modelMapper = modelMapper;
        this.authenticationManager = authenticationManager;
        this.refreshTokenService = refreshTokenService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody @Valid SignupDTO signupDTO,
                                          BindingResult bindingResult){
        User user = convertToUser(signupDTO);

        userValidator.validate(user, bindingResult);

        if(bindingResult.hasErrors()){
            List<FieldError> errors = bindingResult.getFieldErrors();

            StringBuilder stringBuilder = new StringBuilder();

            for(FieldError error : errors){
                stringBuilder.append(error.getField())
                        .append(" - ")
                        .append(error.getDefaultMessage())
                        .append("; ");
            }

            UserErrorResponse userErrorResponse = new UserErrorResponse(
                    stringBuilder.toString(),
                    LocalDateTime.now());

            return new ResponseEntity<>(userErrorResponse, HttpStatus.CONFLICT);
        }

        registrationService.register(user);

        SignupResponseDTO signupResponseDTO = new SignupResponseDTO(
                "User created",
                Instant.now()
        );

        return new ResponseEntity<>(signupResponseDTO, HttpStatus.CREATED);
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signinUser(@RequestBody SigninDTO signinDTO){
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                signinDTO.getUsername(), signinDTO.getPassword()
        );

        Authentication authentication;

        try{
            authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        }catch(AuthenticationException e){
            UserErrorResponse userErrorResponse = new UserErrorResponse("Invalid credentials", LocalDateTime.now());
            return new ResponseEntity<>(userErrorResponse, HttpStatus.BAD_REQUEST);
        }

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        RefreshToken refreshToken = refreshTokenService.createToken(userDetails.getUser().getId()); //if returns null -> implement user service


        SigninResponseDTO signinResponseDTO = new SigninResponseDTO(
                userDetails.getUsername(),
                refreshToken.getToken(),
                jwtUtil.generateToken(userDetails.getUsername())
        );
        return new ResponseEntity<>(signinResponseDTO, HttpStatus.OK);
    }

    @PostMapping("/refresh-access-token")
    public ResponseEntity<?> refreshAccessToken(@RequestBody @Valid RefreshTokenRequestDTO refreshTokenRequestDTO,
                                                BindingResult bindingResult, WebRequest request) throws RefreshTokenExpiredException, RefreshTokenNotFoundException{
        if(bindingResult.hasErrors()){
            RefreshTokenErrorResponse response = new RefreshTokenErrorResponse(
                    HttpStatus.NO_CONTENT.value(),
                    Instant.now(),
                    bindingResult.getFieldErrors().get(0).getDefaultMessage(),
                    request.getContextPath() // substring output if appears not so as intended
            );

            return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
        }

        String requestRefreshToken = refreshTokenRequestDTO.getRefreshToken();

        RefreshToken refreshToken = refreshTokenService.verifyExpiration(requestRefreshToken);
        String accessToken = jwtUtil.generateToken(refreshToken.getUser().getUsername());

        Instant timestamp = Instant.now();

        RefreshAccessTokenResponse response = new RefreshAccessTokenResponse(
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
