package br.com.reddit.clone.springredditclone.repository;

import br.com.reddit.clone.springredditclone.model.Post;
import br.com.reddit.clone.springredditclone.model.Subreddit;
import br.com.reddit.clone.springredditclone.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllBySubreddit(Subreddit subreddit);

    List<Post> findByUser(User user);
}
