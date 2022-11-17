package com.example.rickandmortyapp.controller;

import com.example.rickandmortyapp.dto.CharacterResponseDto;
import com.example.rickandmortyapp.dto.mapper.MovieCharacterMapper;
import com.example.rickandmortyapp.service.MovieCharacterService;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/movie-characters")
public class MovieCharacterController {
    private final MovieCharacterService characterService;
    private final MovieCharacterMapper characterMapper;

    public MovieCharacterController(MovieCharacterService characterService,
                                    MovieCharacterMapper characterMapper) {
        this.characterService = characterService;
        this.characterMapper = characterMapper;
    }

    @GetMapping("/random")
    @ApiOperation("Gives random character")
    public CharacterResponseDto getRandom() {
        return characterMapper.toResponseDto(
                characterService.getRandomCharacter());
    }

    @GetMapping("/by-name")
    @ApiOperation("Search all character by name")
    public List<CharacterResponseDto> findAllByName(@RequestParam("name") String namePart) {
        return characterService.findAllByNameContains(namePart)
                .stream()
                .map(characterMapper::toResponseDto)
                .collect(Collectors.toList());
    }
}
