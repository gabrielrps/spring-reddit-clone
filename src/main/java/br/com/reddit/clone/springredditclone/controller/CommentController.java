package br.com.reddit.clone.springredditclone.controller;

import br.com.reddit.clone.springredditclone.dto.CommentsDto;
import br.com.reddit.clone.springredditclone.service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments/")
@AllArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<?> createComment(@RequestBody CommentsDto commentsDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(commentService.save(commentsDto));
    }

    @GetMapping("/by-post/{postId}")
    public ResponseEntity<List<CommentsDto>> getAllComentsForPost(@PathVariable("postId") Long postId){
        return ResponseEntity.status(HttpStatus.OK).body(commentService.getAllComentsForPost(postId));
    }

    @GetMapping("/by-user/{username}")
    public ResponseEntity<List<CommentsDto>> getAllComentsForPost(@PathVariable("username") String username){
        return ResponseEntity.status(HttpStatus.OK).body(commentService.getAllComentsForUsername(username));
    }

}
