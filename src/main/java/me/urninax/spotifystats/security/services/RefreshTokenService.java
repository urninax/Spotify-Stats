package me.urninax.spotifystats.security.services;

import lombok.AllArgsConstructor;
import me.urninax.spotifystats.security.models.RefreshToken;
import me.urninax.spotifystats.security.models.User;
import me.urninax.spotifystats.security.repositories.RefreshTokenRepository;
import me.urninax.spotifystats.security.repositories.UserRepository;
import me.urninax.spotifystats.security.utils.exceptions.RefreshTokenExpiredException;
import me.urninax.spotifystats.security.utils.exceptions.RefreshTokenNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class RefreshTokenService{
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    @Value("${jwtRefreshExpirationMs}")
    private Long refreshExpirationMs;

    @Autowired
    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, UserRepository userRepository){
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public RefreshToken createToken(User user){
        RefreshToken refreshToken = new RefreshToken();

        refreshToken = setRefreshTokenFields(refreshToken, user);

        refreshTokenRepository.save(refreshToken);

        return refreshToken;
    }

    @Transactional
    public RefreshToken updateToken(User user){
        RefreshToken currentUserRefreshToken = user.getRefreshToken();
        Optional<RefreshToken> refreshTokenOptional = refreshTokenRepository.findById(currentUserRefreshToken.getId());

        if(refreshTokenOptional.isPresent()){
            RefreshToken refreshToken = refreshTokenOptional.get();

            refreshToken.setId(currentUserRefreshToken.getId());

            currentUserRefreshToken = setRefreshTokenFields(refreshToken, user);
        }

        return currentUserRefreshToken;
    }

    @Transactional
    public RefreshToken setRefreshTokenFields(RefreshToken refreshToken, User user){ //return refresh token with new fields
        refreshToken.setUser(user);
        refreshToken.setExpiryTime(Instant.now().plusMillis(refreshExpirationMs));
        refreshToken.setToken(UUID.randomUUID().toString());

        return refreshToken;
    }

    public RefreshToken verifyExpiration(String token) throws RefreshTokenNotFoundException, RefreshTokenExpiredException{
        Optional<RefreshToken> refreshTokenOptional = refreshTokenRepository.findByToken(token);

        if(refreshTokenOptional.isPresent()){
            RefreshToken refreshToken = refreshTokenOptional.get();

            Instant tokenCurrentExpiryTime = refreshToken.getExpiryTime();

            if(Instant.now().isAfter(tokenCurrentExpiryTime)){
                throw new RefreshTokenExpiredException(refreshToken.getToken(), "Refresh token is expired. Sign in again.");
            }

            return refreshToken;
        }else{
            throw new RefreshTokenNotFoundException("Refresh token is not in database");
        }
    }

    public Optional<RefreshToken> findByToken(String token){
        return refreshTokenRepository.findByToken(token);
    }

    @Transactional
    public void deleteByUserId(Long id){
        Optional<User> userOptional = userRepository.findById(id);

        if(userOptional.isPresent()){
            User user = userOptional.get();

            RefreshToken refreshToken = user.getRefreshToken();

            if(refreshToken != null){
                refreshTokenRepository.delete(refreshToken);
            }
        }
    }
}
