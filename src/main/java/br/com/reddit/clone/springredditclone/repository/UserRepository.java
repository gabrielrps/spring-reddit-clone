package br.com.reddit.clone.springredditclone.repository;

import br.com.reddit.clone.springredditclone.model.Subreddit;
import br.com.reddit.clone.springredditclone.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotBlank;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
