package br.com.reddit.clone.springredditclone.controller;

import br.com.reddit.clone.springredditclone.dto.PostRequest;
import br.com.reddit.clone.springredditclone.dto.PostResponse;
import br.com.reddit.clone.springredditclone.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@AllArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostResponse> createPost(@RequestBody PostRequest postRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(postService.save(postRequest));
    }

    @GetMapping
    public ResponseEntity<List<PostResponse>> getAllPosts() {
        return ResponseEntity.status(HttpStatus.OK).body(postService.getAllPosts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPost(@PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(postService.getPost(id));
    }

    @GetMapping("by-subreddit/{id}")
    public ResponseEntity<List<PostResponse>> getPostsBySubreddit(@PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(postService.getPostsBySubreddit(id));
    }

    @GetMapping("by-user/{username}")
    public ResponseEntity<List<PostResponse>> getPostsByUsername(@PathVariable("username") String username) {
        return ResponseEntity.status(HttpStatus.OK).body(postService.getPostsByUsername(username));
    }
}
