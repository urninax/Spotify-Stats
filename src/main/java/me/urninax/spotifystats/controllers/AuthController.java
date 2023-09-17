package me.urninax.spotifystats.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import me.urninax.spotifystats.dto.SigninDTO;
import me.urninax.spotifystats.dto.SignupDTO;
import me.urninax.spotifystats.models.User;
import me.urninax.spotifystats.security.services.RegistrationService;
import me.urninax.spotifystats.security.services.UserDetailsServiceImpl;
import me.urninax.spotifystats.services.UserService;
import me.urninax.spotifystats.utils.UserValidator;
import me.urninax.spotifystats.utils.responses.UserErrorResponse;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController{
    private final RegistrationService registrationService;
    private final UserValidator userValidator;
    private final ModelMapper modelMapper;
    private final AuthenticationManager authenticationManager;

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

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signinUser(@RequestBody SigninDTO signinDTO){
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                signinDTO.getUsername(), signinDTO.getPassword()
        );

        try{
            authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        }catch(AuthenticationException e){
            UserErrorResponse userErrorResponse = new UserErrorResponse("Invalid credentials", LocalDateTime.now());
            return new ResponseEntity<>(userErrorResponse, HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(HttpStatus.OK); // TODO: implement jwtToken
    }

    public User convertToUser(SignupDTO signupDTO){
        return modelMapper.map(signupDTO, User.class);
    }
}
