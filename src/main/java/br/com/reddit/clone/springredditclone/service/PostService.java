package br.com.reddit.clone.springredditclone.service;

import br.com.reddit.clone.springredditclone.dto.PostRequest;
import br.com.reddit.clone.springredditclone.dto.PostResponse;
import br.com.reddit.clone.springredditclone.exceptions.SpringRedditException;
import br.com.reddit.clone.springredditclone.mapper.PostMapper;
import br.com.reddit.clone.springredditclone.model.Post;
import br.com.reddit.clone.springredditclone.model.Subreddit;
import br.com.reddit.clone.springredditclone.model.User;
import br.com.reddit.clone.springredditclone.repository.PostRepository;
import br.com.reddit.clone.springredditclone.repository.SubredditRepository;
import br.com.reddit.clone.springredditclone.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class PostService {

    private final SubredditRepository subredditRepository;
    private final AuthService authService;
    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final UserRepository userRepository;

    public PostResponse save(PostRequest postRequest) {
        Subreddit subreddit = subredditRepository.findByName(postRequest.getSubredditName())
                .orElseThrow(() -> new SpringRedditException(postRequest.getSubredditName() + " not found"));

        User user = authService.getCurrentUser();

        Post save = postRepository.save(postMapper.map(postRequest, subreddit, user));

        return postMapper.mapToDto(save);
    }

    public PostResponse getPost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new SpringRedditException("Post not found"));

        return postMapper.mapToDto(post);
    }

    public List<PostResponse> getAllPosts() {
        return postRepository.findAll().stream().map(postMapper::mapToDto).collect(Collectors.toList());
    }

    public List<PostResponse> getPostsBySubreddit(Long id) {
        Subreddit subreddit = subredditRepository.findById(id)
                .orElseThrow(() -> new SpringRedditException("Subreddit not found"));

        return postRepository.findAllBySubreddit(subreddit).stream().map(postMapper::mapToDto).collect(Collectors.toList());
    }

    public List<PostResponse> getPostsByUsername(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new SpringRedditException("User not found"));

        return postRepository.findByUser(user).stream().map(postMapper::mapToDto).collect(Collectors.toList());
    }
}
