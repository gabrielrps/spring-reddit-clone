package br.com.reddit.clone.springredditclone.service;

import br.com.reddit.clone.springredditclone.dto.AuthenticationResponse;
import br.com.reddit.clone.springredditclone.dto.LoginRequest;
import br.com.reddit.clone.springredditclone.dto.RefreshTokenRequest;
import br.com.reddit.clone.springredditclone.dto.RegisterRequest;
import br.com.reddit.clone.springredditclone.exceptions.SpringRedditException;
import br.com.reddit.clone.springredditclone.model.NotificationEmail;
import br.com.reddit.clone.springredditclone.model.User;
import br.com.reddit.clone.springredditclone.model.VerificationToken;
import br.com.reddit.clone.springredditclone.repository.UserRepository;
import br.com.reddit.clone.springredditclone.repository.VerificationTokenRepository;
import br.com.reddit.clone.springredditclone.security.JwtProvider;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;
    private AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;

    @Transactional
    public void signup(RegisterRequest registerRequest){
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setCreated(Instant.now());
        user.setEnabled(false);

        userRepository.save(user);

        String token = genereteVerificationToken(user);

        mailService.sendMail(new NotificationEmail("Please Activate your Account"
                , user.getEmail()
                , "Thank you for signing up to Spring Reddit, " +
                "please click on the below url to activate your account : " +
                "http://localhost:8080/api/auth/accountVerification/" + token));
    }

    private String genereteVerificationToken(User user) {
        String token = UUID.randomUUID().toString();

        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);

        verificationTokenRepository.save(verificationToken);

        return token;
    }

    public void verifyAccount(String token) {
        Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken(token);
        verificationToken.orElseThrow(() -> new SpringRedditException("Invalid Token"));

        fetchUserAndEnable(verificationToken.get());

    }

    @Transactional
    public void fetchUserAndEnable(VerificationToken verificationToken) {
        User user = userRepository.findByUsername(verificationToken.getUser().getUsername())
                .orElseThrow(() -> new SpringRedditException("User not found with name " + verificationToken.getUser().getUsername()));
        user.setEnabled(true);

        userRepository.save(user);
    }

    public AuthenticationResponse login(LoginRequest loginRequest) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String token = jwtProvider.genereteToken(authenticate);

        return AuthenticationResponse.builder()
                .authenticationToken(token)
                .refreshToken(refreshTokenService.genereteRefreshToken().getToken())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationTime()))
                .username(loginRequest.getUsername())
                .build();

    }

    public User getCurrentUser() {
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();
        return userRepository.findByUsername(principal.getUsername())
                .orElseThrow(() -> new SpringRedditException("User name not found - " + principal.getUsername()));
    }

    public AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());
        String token = jwtProvider.genereteTokenWithUsername(refreshTokenRequest.getUsername());

        return AuthenticationResponse.builder()
                .authenticationToken(token)
                .refreshToken(refreshTokenRequest.getRefreshToken())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationTime()))
                .username(refreshTokenRequest.getUsername())
                .build();
    }
}
