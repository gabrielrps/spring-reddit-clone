package br.com.reddit.clone.springredditclone.repository;

import br.com.reddit.clone.springredditclone.model.User;
import br.com.reddit.clone.springredditclone.model.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    Optional<VerificationToken> findByToken(String token);
}
