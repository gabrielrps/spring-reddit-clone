package br.com.reddit.clone.springredditclone.mapper;

import br.com.reddit.clone.springredditclone.dto.PostRequest;
import br.com.reddit.clone.springredditclone.dto.PostResponse;
import br.com.reddit.clone.springredditclone.model.Post;
import br.com.reddit.clone.springredditclone.model.Subreddit;
import br.com.reddit.clone.springredditclone.model.User;
import br.com.reddit.clone.springredditclone.repository.CommentRepository;
import br.com.reddit.clone.springredditclone.repository.VoteRepository;
import br.com.reddit.clone.springredditclone.service.AuthService;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class PostMapper {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private VoteRepository voteRepository;
    @Autowired
    private AuthService authService;

    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "subreddit", source = "subreddit")
    @Mapping(target = "user", source = "user")
    @Mapping(target = "description", source = "postRequest.description")
    @Mapping(target = "voteCount", constant = "0")
    public abstract Post map(PostRequest postRequest, Subreddit subreddit, User user);

    @Mapping(target = "id", source = "postId")
    @Mapping(target = "postName", source = "postName")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "url", source = "url")
    @Mapping(target = "subredditName", source = "subreddit.name")
    @Mapping(target = "userName", source = "user.username")
    @Mapping(target = "commentCount", expression = "java(commentCount(post))")
    @Mapping(target = "duration", expression = "java(getDuration(post))")
    public abstract PostResponse mapToDto(Post post);

    Integer commentCount(Post post){ return commentRepository.findByPost(post).size(); }

    String getDuration(Post post){ return TimeAgo.using(post.getCreatedDate().toEpochMilli()); }

}
