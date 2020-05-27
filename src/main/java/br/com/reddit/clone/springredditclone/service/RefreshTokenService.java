package br.com.reddit.clone.springredditclone.service;

import br.com.reddit.clone.springredditclone.dto.RefreshTokenRequest;
import br.com.reddit.clone.springredditclone.exceptions.SpringRedditException;
import br.com.reddit.clone.springredditclone.model.RefreshToken;
import br.com.reddit.clone.springredditclone.repository.RefreshTokenRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken genereteRefreshToken(){
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setCreatedDate(Instant.now());

        return refreshTokenRepository.save(refreshToken);
    }

    void validateRefreshToken(String token){
        refreshTokenRepository.findByToken(token).orElseThrow(() -> new SpringRedditException("Invalid Refresh Token"));
    }

    public void deleteRefreshToken(String token) {
        refreshTokenRepository.deleteByToken(token);
    }
}
