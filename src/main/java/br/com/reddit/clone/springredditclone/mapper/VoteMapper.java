package br.com.reddit.clone.springredditclone.mapper;

import br.com.reddit.clone.springredditclone.dto.VoteDto;
import br.com.reddit.clone.springredditclone.model.Post;
import br.com.reddit.clone.springredditclone.model.User;
import br.com.reddit.clone.springredditclone.model.Vote;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VoteMapper {

    @Mapping(target = "voteId", ignore = true)
    @Mapping(target = "voteType", source = "voteDto.voteType")
    @Mapping(target = "user", source = "user")
    @Mapping(target = "post", source = "post")
    Vote map(VoteDto voteDto, Post post, User user);

}
