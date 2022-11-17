package com.example.rickandmortyapp.controller;

import com.example.rickandmortyapp.model.Gender;
import com.example.rickandmortyapp.model.MovieCharacter;
import com.example.rickandmortyapp.model.Status;
import com.example.rickandmortyapp.service.MovieCharacterService;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import java.util.LinkedList;
import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class MovieCharacterControllerTest {
    @MockBean
    private MovieCharacterService characterService;
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(mockMvc);
    }

    @Test
    void shouldShowAllMovieCharacterWithNamePart() {
        List<MovieCharacter> mockCharacter = new LinkedList<>();
        MovieCharacter character = new MovieCharacter();
        character.setId(1L);
        character.setExternalId(1L);
        character.setName("Rick");
        character.setGender(Gender.MALE);
        character.setStatus(Status.ALIVE);
        mockCharacter.add(character);

        character = new MovieCharacter();
        character.setId(2L);
        character.setExternalId(2L);
        character.setName("Rick S");
        character.setGender(Gender.MALE);
        character.setStatus(Status.ALIVE);
        mockCharacter.add(character);

        Mockito.when(characterService.findAllByNameContains("Rick")).thenReturn(mockCharacter);
        RestAssuredMockMvc.when()
                .get("/movie-characters/by-name?name=Rick")
                .then()
                .statusCode(200)
                .body("size()", Matchers.equalTo(2))
                .body("[0].id", Matchers.equalTo(1))
                .body("[0].externalId", Matchers.equalTo(1))
                .body("[0].name", Matchers.equalTo("Rick"))
                .body("[0].gender", Matchers.equalTo("MALE"))
                .body("[0].status", Matchers.equalTo("ALIVE"))
                .body("[1].id", Matchers.equalTo(2))
                .body("[1].externalId", Matchers.equalTo(2))
                .body("[1].name", Matchers.equalTo("Rick S"))
                .body("[1].gender", Matchers.equalTo("MALE"))
                .body("[1].status", Matchers.equalTo("ALIVE"));

    }

    @Test
    void shouldShowRandomMovieCharacter() {
        MovieCharacter mockCharacter = new MovieCharacter();
        mockCharacter.setId(22L);
        mockCharacter.setExternalId(22L);
        mockCharacter.setName("Rick");
        mockCharacter.setGender(Gender.MALE);
        mockCharacter.setStatus(Status.ALIVE);

        Mockito.when(characterService.getRandomCharacter()).thenReturn(mockCharacter);
        RestAssuredMockMvc.when()
                .get("/movie-characters/random")
                .then()
                .statusCode(200)
                .body("id", Matchers.equalTo(22))
                .body("externalId", Matchers.equalTo(22))
                .body("name", Matchers.equalTo("Rick"))
                .body("gender", Matchers.equalTo("MALE"))
                .body("status", Matchers.equalTo("ALIVE"));
    }
}
