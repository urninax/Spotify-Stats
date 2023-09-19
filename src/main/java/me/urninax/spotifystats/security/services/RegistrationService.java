package me.urninax.spotifystats.security.services;

import lombok.AllArgsConstructor;
import me.urninax.spotifystats.security.enums.ERole;
import me.urninax.spotifystats.security.models.User;
import me.urninax.spotifystats.security.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class RegistrationService{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User register(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(ERole.ROLE_USER);

        userRepository.save(user);

        return user;
    }
}
