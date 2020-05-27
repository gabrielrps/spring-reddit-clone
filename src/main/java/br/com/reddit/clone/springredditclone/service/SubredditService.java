package br.com.reddit.clone.springredditclone.service;


import br.com.reddit.clone.springredditclone.dto.SubredditDto;
import br.com.reddit.clone.springredditclone.exceptions.SpringRedditException;
import br.com.reddit.clone.springredditclone.mapper.SubredditMapper;
import br.com.reddit.clone.springredditclone.model.Subreddit;
import br.com.reddit.clone.springredditclone.repository.SubredditRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class SubredditService {

    private final SubredditRepository subredditRepository;
    private final SubredditMapper subredditMapper;

    @Transactional
    public SubredditDto save(SubredditDto subredditDto){
        Subreddit subreddit = subredditMapper.mapDtoToSubreddit(subredditDto);
        Subreddit save = subredditRepository.save(subreddit);

        subredditDto.setId(save.getId());

        return subredditDto;
    }

    public List<SubredditDto> getAllSubreddits() {
        return subredditRepository.findAll().stream().map(subredditMapper::mapSubredditToDto).collect(Collectors.toList());
    }

    public SubredditDto getSubreddit(Long id) {
        Subreddit subreddit = subredditRepository.findById(id).orElseThrow(() -> new SpringRedditException("Subreddit not found"));

        return subredditMapper.mapSubredditToDto(subreddit);
    }
}
