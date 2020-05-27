package br.com.reddit.clone.springredditclone.service;

import br.com.reddit.clone.springredditclone.dto.CommentsDto;
import br.com.reddit.clone.springredditclone.exceptions.SpringRedditException;
import br.com.reddit.clone.springredditclone.mapper.CommentsMapper;
import br.com.reddit.clone.springredditclone.model.Comment;
import br.com.reddit.clone.springredditclone.model.NotificationEmail;
import br.com.reddit.clone.springredditclone.model.Post;
import br.com.reddit.clone.springredditclone.model.User;
import br.com.reddit.clone.springredditclone.repository.CommentRepository;
import br.com.reddit.clone.springredditclone.repository.PostRepository;
import br.com.reddit.clone.springredditclone.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class CommentService {

    private static final String POST_URL = "";
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final CommentRepository commentRepository;
    private final CommentsMapper commentsMapper;
    private final MailContentBuilder mailContentBuilder;
    private final MailService mailService;

    public CommentsDto save(CommentsDto commentsDto){
        Post post = postRepository.findById(commentsDto.getPostId()).orElseThrow(() -> new SpringRedditException("Post not found"));
        User user = authService.getCurrentUser();

        Comment save = commentRepository.save(commentsMapper.map(commentsDto, post, user));

        String message = mailContentBuilder.build(post.getUser().getUsername() + " posted a comment on your post. " + POST_URL);
        sendCommentNotification(message, post.getUser());

        return commentsMapper.mapToDto(save);
    }

    public List<CommentsDto> getAllComentsForPost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new SpringRedditException("Post not found"));

        return commentRepository.findByPost(post).stream().map(commentsMapper::mapToDto).collect(Collectors.toList());
    }

    public List<CommentsDto> getAllComentsForUsername(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new SpringRedditException("User not found"));

        return commentRepository.findAllByUser(user).stream().map(commentsMapper::mapToDto).collect(Collectors.toList());
    }

    private void sendCommentNotification(String message, User user) {
        mailService.sendMail(new NotificationEmail(user.getUsername() + " Comment on your post", user.getEmail(), message));
    }
}
