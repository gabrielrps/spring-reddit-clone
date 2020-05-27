package br.com.reddit.clone.springredditclone.service;

import br.com.reddit.clone.springredditclone.dto.VoteDto;
import br.com.reddit.clone.springredditclone.exceptions.SpringRedditException;
import br.com.reddit.clone.springredditclone.mapper.VoteMapper;
import br.com.reddit.clone.springredditclone.model.Post;
import br.com.reddit.clone.springredditclone.model.User;
import br.com.reddit.clone.springredditclone.model.Vote;
import br.com.reddit.clone.springredditclone.model.VoteType;
import br.com.reddit.clone.springredditclone.repository.PostRepository;
import br.com.reddit.clone.springredditclone.repository.VoteRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class VoteService {

    private final VoteRepository voteRepository;
    private final PostRepository postRepository;
    private final AuthService authService;
    private final VoteMapper voteMapper;

    public void vote(VoteDto voteDto) {
        Post post = postRepository.findById(voteDto.getPostId()).orElseThrow(() -> new SpringRedditException("Post not found"));
        User user = authService.getCurrentUser();

        Optional<Vote> votePostAndUser = voteRepository.findTopByPostAndUserOrderByVoteIdDesc(post, user);

        if(votePostAndUser.isPresent() && votePostAndUser.get().getVoteType().equals(voteDto.getVoteType())){
            throw new SpringRedditException("You have already " + voteDto.getVoteType() + "'d for this post");
        }
        if(VoteType.UPVOTE.equals(voteDto.getVoteType())){
            post.setVoteCount(post.getVoteCount() == null ? 0 + 1 : post.getVoteCount() + 1);
        }else{
            post.setVoteCount(post.getVoteCount() == null ? 0 - 1 : post.getVoteCount() - 1);
        }
        voteRepository.save(voteMapper.map(voteDto, post, user));
            postRepository.save(post);

    }

}
