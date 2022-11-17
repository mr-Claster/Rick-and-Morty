package com.example.rickandmortyapp.dto.mapper;

import com.example.rickandmortyapp.dto.CharacterResponseDto;
import com.example.rickandmortyapp.dto.external.ApiCharacterDto;
import com.example.rickandmortyapp.model.Gender;
import com.example.rickandmortyapp.model.MovieCharacter;
import com.example.rickandmortyapp.model.Status;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MovieCharacterMapperTest {
    @Autowired
    private MovieCharacterMapper characterMapper;

    @Test
    void shouldReturnCharacterResponseDto() {
        MovieCharacter character = new MovieCharacter();
        character.setId(1L);
        character.setExternalId(1L);
        character.setName("Bob");
        character.setGender(Gender.MALE);
        character.setStatus(Status.ALIVE);

        CharacterResponseDto expected = new CharacterResponseDto();
        expected.setId(1L);
        expected.setExternalId(1L);
        expected.setName("Bob");
        expected.setGender(Gender.MALE);
        expected.setStatus(Status.ALIVE);

        CharacterResponseDto actual = characterMapper.toResponseDto(character);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void shouldReturnCharacterResponseDtoWithoutFields() {
        CharacterResponseDto expected = new CharacterResponseDto();
        MovieCharacter character = new MovieCharacter();
        CharacterResponseDto actual = characterMapper.toResponseDto(character);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void ShouldNotReturnCharacterResponseDtoWithoutMovieCharacter() {
        Assertions.assertThrows(NullPointerException.class,
                () -> characterMapper.toResponseDto(null));
    }

    @Test
    void ShouldReturnMovieCharacter() {
        MovieCharacter expected = new MovieCharacter();
        expected.setId(null);
        expected.setExternalId(1L);
        expected.setName("Rick");
        expected.setGender(Gender.MALE);
        expected.setStatus(Status.ALIVE);

        ApiCharacterDto dto = new ApiCharacterDto();
        dto.setId(1L);
        dto.setName("Rick");
        dto.setGender("Male");
        dto.setStatus("Alive");
        MovieCharacter actual = characterMapper.parseApiCharacterResponseDto(dto);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void shouldNotReturnMovieCharacterWithoutFields() {
        ApiCharacterDto dto = new ApiCharacterDto();
        Assertions.assertThrows(NullPointerException.class,
                () -> characterMapper.parseApiCharacterResponseDto(dto));
    }

    @Test
    void ShouldNotReturnMovieCharacterWithoutMovieCharacter() {
        Assertions.assertThrows(NullPointerException.class, () -> characterMapper.parseApiCharacterResponseDto(null));
    }
}
