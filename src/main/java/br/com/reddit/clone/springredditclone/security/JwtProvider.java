package br.com.reddit.clone.springredditclone.security;

import br.com.reddit.clone.springredditclone.exceptions.SpringRedditException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;
import java.time.Instant;
import java.util.Date;

@Service

public class JwtProvider {

    private KeyStore keyStore;

    @Value("${jwt.expiration.time}")
    private Long jwtExpirationTime;

    @PostConstruct
    public void init(){
        try {
            keyStore = KeyStore.getInstance("JKS");
            InputStream resourceAsStream = getClass().getResourceAsStream("/springblog.jks");
            keyStore.load(resourceAsStream, "secret".toCharArray());
        } catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException e){
            throw new SpringRedditException("Exception occurred while loading keystore");
        }
    }


    public String genereteToken(Authentication authentication){
        User principal = (User) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject(principal.getUsername())
                .signWith(getPrincipalKey())
                .setExpiration(Date.from(Instant.now().plusMillis(jwtExpirationTime)))
                .compact();
    }

    public String genereteTokenWithUsername(String username){
        return Jwts.builder()
                .setSubject(username)
                .signWith(getPrincipalKey())
                .setExpiration(Date.from(Instant.now().plusMillis(jwtExpirationTime)))
                .compact();
    }

    private Key getPrincipalKey() {
        try {
            return (PrivateKey) keyStore.getKey("springblog", "secret".toCharArray());
        } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException e) {
            throw new SpringRedditException("Exception occurred while reatriving public key from keystore");
        }
    }

    public boolean validateToken(String jwt){
        Jwts.parser().setSigningKey(getPublicKey()).parseClaimsJws(jwt);
        return true;
    }

    private PublicKey getPublicKey() {
        try {
            return keyStore.getCertificate("springblog").getPublicKey();
        } catch (KeyStoreException e) {
            throw new SpringRedditException("Exception occurred while reatriving public key from keystore");
        }
    }

    public String getUsernameFromJwt(String token){
        Claims claims = Jwts.parser().setSigningKey(getPublicKey()).parseClaimsJws(token).getBody();

        return claims.getSubject();

    }

    public Long getJwtExpirationTime() {
        return jwtExpirationTime;
    }
}
