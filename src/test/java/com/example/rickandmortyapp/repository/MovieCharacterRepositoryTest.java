package com.example.rickandmortyapp.repository;

import com.example.rickandmortyapp.model.Gender;
import com.example.rickandmortyapp.model.MovieCharacter;
import com.example.rickandmortyapp.model.Status;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MovieCharacterRepositoryTest {
    @Container
    static PostgreSQLContainer<?> database = new PostgreSQLContainer<>("postgres:13.1")
            .withDatabaseName("test")
            .withPassword("root")
            .withUsername("root");

    @DynamicPropertySource
    static void setDatasourceProperties(DynamicPropertyRegistry propertyRegistry) {
        propertyRegistry.add("spring.datasource.url", database::getJdbcUrl);
        propertyRegistry.add("spring.datasource.password", database::getPassword);
        propertyRegistry.add("spring.datasource.username", database::getUsername);
    }

    @Autowired
    private MovieCharacterRepository characterRepository;

    @Test
    @Sql("/scripts/init_ten_characters.sql")
    void shouldReturnAllMovieCharacterWithNamePart() {
        List<MovieCharacter> expected = new ArrayList<>();
        MovieCharacter character = new MovieCharacter();
        character.setId(1L);
        character.setExternalId(1L);
        character.setName("Rick");
        character.setGender(Gender.MALE);
        character.setStatus(Status.ALIVE);
        expected.add(character);

        character = new MovieCharacter();
        character.setId(2L);
        character.setExternalId(2L);
        character.setName("Rick Sanchez");
        character.setGender(Gender.MALE);
        character.setStatus(Status.ALIVE);
        expected.add(character);

        character = new MovieCharacter();
        character.setId(3L);
        character.setExternalId(3L);
        character.setName("Rick NOTSanchez");
        character.setGender(Gender.MALE);
        character.setStatus(Status.ALIVE);
        expected.add(character);

        character = new MovieCharacter();
        character.setId(10L);
        character.setExternalId(10L);
        character.setName("Pickle Rick");
        character.setGender(Gender.MALE);
        character.setStatus(Status.ALIVE);
        expected.add(character);

        List<MovieCharacter> actual = characterRepository.findAllByNameContains("Rick");
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @Sql("/scripts/init_ten_characters.sql")
    void shouldReturnMovieCharacterByExternalIds() {
        List<MovieCharacter> expected = new ArrayList<>();
        MovieCharacter character = new MovieCharacter();
        character.setId(1L);
        character.setExternalId(1L);
        character.setName("Rick");
        character.setGender(Gender.MALE);
        character.setStatus(Status.ALIVE);
        expected.add(character);

        character = new MovieCharacter();
        character.setId(2L);
        character.setExternalId(2L);
        character.setName("Rick Sanchez");
        character.setGender(Gender.MALE);
        character.setStatus(Status.ALIVE);
        expected.add(character);

        Set<Long> externalIdsSet = new HashSet<>();
        externalIdsSet.add(1L);
        externalIdsSet.add(2L);
        List<MovieCharacter> actual = characterRepository
                .findAllByExternalIdIn(externalIdsSet);
        Assertions.assertEquals(expected, actual);
    }
}
