package br.com.reddit.clone.springredditclone.mapper;

import br.com.reddit.clone.springredditclone.dto.CommentsDto;
import br.com.reddit.clone.springredditclone.model.Comment;
import br.com.reddit.clone.springredditclone.model.Post;
import br.com.reddit.clone.springredditclone.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentsMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "text", source = "commentsDto.text")
    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "post", source = "post")
    Comment map(CommentsDto commentsDto, Post post, User user);

    @Mapping(target = "postId", expression = "java(comment.getPost().getPostId())")
    @Mapping(target = "userName", expression = "java(comment.getUser().getUsername())")
    CommentsDto mapToDto(Comment comment);

}
