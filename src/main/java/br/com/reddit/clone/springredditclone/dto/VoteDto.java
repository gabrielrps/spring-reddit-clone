package br.com.reddit.clone.springredditclone.dto;

import br.com.reddit.clone.springredditclone.model.VoteType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VoteDto {

    private VoteType voteType;
    private Long postId;

}
