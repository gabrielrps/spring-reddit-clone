package br.com.reddit.clone.springredditclone.repository;

import br.com.reddit.clone.springredditclone.model.Post;
import br.com.reddit.clone.springredditclone.model.User;
import br.com.reddit.clone.springredditclone.model.VerificationToken;
import br.com.reddit.clone.springredditclone.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {

    Optional<Vote> findTopByPostAndUserOrderByVoteIdDesc(Post post, User currentUser);
}
