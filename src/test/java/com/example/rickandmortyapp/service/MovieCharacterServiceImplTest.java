package com.example.rickandmortyapp.service;

import com.example.rickandmortyapp.dto.external.ApiCharacterDto;
import com.example.rickandmortyapp.dto.external.ApiInfoDto;
import com.example.rickandmortyapp.dto.external.ApiResponseDto;
import com.example.rickandmortyapp.model.Gender;
import com.example.rickandmortyapp.model.MovieCharacter;
import com.example.rickandmortyapp.model.Status;
import com.example.rickandmortyapp.repository.MovieCharacterRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class MovieCharacterServiceImplTest {
    @Autowired
    private MovieCharacterServiceImpl characterService;
    @MockBean
    private MovieCharacterRepository characterRepository;

    @Test
    void shouldSaveAllNewMovieCharacter() {
        List<MovieCharacter> mockCharacter = new ArrayList<>();
        MovieCharacter movieCharacter;
        for (int i = 0; i < 4; i++) {
            movieCharacter = new MovieCharacter();
            movieCharacter.setId((long) i + 1);
            movieCharacter.setExternalId((long) i + 1);
            movieCharacter.setName(String.valueOf(i + 1));
            movieCharacter.setGender(Gender.MALE);
            movieCharacter.setStatus(Status.ALIVE);
            mockCharacter.add(movieCharacter);
        }
        Mockito.when(characterRepository.findAllByExternalIdIn(Set.of(1L,2L,3L))).thenReturn(mockCharacter);

        ApiCharacterDto[] apiCharacterDtos = new ApiCharacterDto[3];
        for (int i = 0; i < 3; i++) {
            apiCharacterDtos[i] = new ApiCharacterDto();
            apiCharacterDtos[i].setId((long) i + 1);
            apiCharacterDtos[i].setName(String.valueOf(i + 1));
            apiCharacterDtos[i].setGender("Male");
            apiCharacterDtos[i].setStatus("Alive");
        }
        ApiInfoDto apiInfoDto = new ApiInfoDto();
        apiInfoDto.setNext(null);
        apiInfoDto.setNext(null);
        apiInfoDto.setCount(3);
        apiInfoDto.setPages(1);

        ApiResponseDto apiResponseDto = new ApiResponseDto();
        apiResponseDto.setInfo(apiInfoDto);
        apiResponseDto.setResults(apiCharacterDtos);

        List<MovieCharacter> expected = new ArrayList<>();
        MovieCharacter character = new MovieCharacter();
        character.setId(4L);
        character.setExternalId(4L);
        character.setName("4");
        character.setGender(Gender.MALE);
        character.setStatus(Status.ALIVE);

        characterService.saveDtosToDB(apiResponseDto);
        Mockito.verify(characterRepository).saveAll(expected);
    }

    @Test
    void shouldReturnRandomMovieCharacter() {
        MovieCharacterServiceImpl characterServiceSpy = Mockito.spy(characterService);
        Mockito.doReturn(25L).when(characterServiceSpy).getRandomId();

        MovieCharacter mockCharacter = new MovieCharacter();
        mockCharacter.setId(25L);
        mockCharacter.setExternalId(25L);
        mockCharacter.setName("25");
        mockCharacter.setGender(Gender.MALE);
        mockCharacter.setStatus(Status.ALIVE);

        Mockito.when(characterRepository.getReferenceById(25L)).thenReturn(mockCharacter);

        MovieCharacter expected = new MovieCharacter();
        expected.setId(25L);
        expected.setExternalId(25L);
        expected.setName("25");
        expected.setGender(Gender.MALE);
        expected.setStatus(Status.ALIVE);
        MovieCharacter actual = characterServiceSpy.getRandomCharacter();

        Assertions.assertEquals(expected, actual);
    }
}
