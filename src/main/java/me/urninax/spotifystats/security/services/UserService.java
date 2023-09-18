package me.urninax.spotifystats.security.services;

import me.urninax.spotifystats.security.models.User;
import me.urninax.spotifystats.security.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UserService{
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public Optional<User> findByUsername(String username){
        Optional<User> optionalUser = userRepository.findByUsername(username);
        return optionalUser;
    }

    public Optional<User> findByEmail(String email){
        return userRepository.findByEmail(email);
    }
}
