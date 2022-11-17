package com.example.rickandmortyapp.service;

import com.example.rickandmortyapp.dto.external.ApiCharacterDto;
import com.example.rickandmortyapp.dto.external.ApiResponseDto;
import com.example.rickandmortyapp.dto.mapper.MovieCharacterMapper;
import com.example.rickandmortyapp.model.MovieCharacter;
import com.example.rickandmortyapp.repository.MovieCharacterRepository;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class MovieCharacterServiceImpl implements MovieCharacterService {
    private final HttpClient httpClient;
    private final MovieCharacterRepository movieCharacterRepository;
    private final MovieCharacterMapper movieCharacterMapper;

    public MovieCharacterServiceImpl(HttpClient httpClient,
                                     MovieCharacterRepository movieCharacterRepository,
                                     MovieCharacterMapper movieCharacterMapper) {
        this.httpClient = httpClient;
        this.movieCharacterRepository = movieCharacterRepository;
        this.movieCharacterMapper = movieCharacterMapper;
    }

    @PostConstruct
    @Scheduled(cron = "0 8 * * * *")
    @Override
    public void syncExternalCharacters() {
        log.info("syncExternalCharacters method was invoked at " + LocalTime.now());
        ApiResponseDto responseDto = httpClient.get("https://rickandmortyapi.com/api/character",
                ApiResponseDto.class);
        saveDtosToDB(responseDto);
        while (responseDto.getInfo().getNext() != null) {
            responseDto = httpClient.get(responseDto.getInfo().getNext(),
                    ApiResponseDto.class);
            saveDtosToDB(responseDto);
        }
    }

    @Override
    public MovieCharacter getRandomCharacter() {
        return movieCharacterRepository.getReferenceById(getRandomId());
    }

    @Override
    public List<MovieCharacter> findAllByNameContains(String namePart) {
        return movieCharacterRepository.findAllByNameContains(namePart);
    }

    long getRandomId() {
        return (long) (Math.random() * movieCharacterRepository.count());
    }

    void saveDtosToDB(ApiResponseDto responseDto) {
        Map<Long, ApiCharacterDto> externalDtos = Arrays.stream(responseDto.getResults())
                .collect(Collectors.toMap(ApiCharacterDto::getId, Function.identity()));

        Set<Long> externalIds = externalDtos.keySet();

        List<MovieCharacter> existingCharacters = movieCharacterRepository
                .findAllByExternalIdIn(externalIds);

        Map<Long, MovieCharacter> existingCharactersWithIds = existingCharacters.stream()
                .collect(Collectors.toMap(MovieCharacter::getExternalId, Function.identity()));

        Set<Long> existingIds = existingCharactersWithIds.keySet();

        externalIds.removeAll(existingIds);

        List<MovieCharacter> charactersToSave = externalIds.stream()
                .map(i -> movieCharacterMapper.parseApiCharacterResponseDto(externalDtos.get(i)))
                .collect(Collectors.toList());
        movieCharacterRepository.saveAll(charactersToSave);
    }
}
