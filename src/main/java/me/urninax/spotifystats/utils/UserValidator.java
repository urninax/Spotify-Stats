package me.urninax.spotifystats.utils;

import me.urninax.spotifystats.models.User;
import me.urninax.spotifystats.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

@Component
public class UserValidator implements Validator{
    private final UserService userService;

    @Autowired
    public UserValidator(UserService userService){
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> clazz){
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors){
        User user = (User) target;
        Optional<User> optionalUserForUsernameErrorCheck = userService.findByUsername(user.getUsername());

        if(optionalUserForUsernameErrorCheck.isPresent()){
            errors.rejectValue("username", "409", "Username already exists");
        }

        Optional<User> optionalUserForEmailErrorCheck = userService.findByEmail(user.getEmail());

        if(optionalUserForEmailErrorCheck.isPresent()){
            errors.rejectValue("email", "409", "Email already exists");
        }
    }
}
