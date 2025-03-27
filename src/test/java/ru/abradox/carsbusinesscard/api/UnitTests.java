package ru.abradox.carsbusinesscard.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.abradox.carsbusinesscard.dto.UnitDto;
import ru.abradox.carsbusinesscard.dto.request.CreateUnitRequest;
import ru.abradox.carsbusinesscard.dto.request.UpdateUnitRequest;
import ru.abradox.carsbusinesscard.entity.UnitEntity;
import ru.abradox.carsbusinesscard.repository.UnitRepository;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class UnitTests {

    private static UnitEntity UNIT_1;

    private static UnitEntity UNIT_2;

    @Autowired
    UnitRepository repository;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mvc;

    @BeforeEach
    void setUp() {
        UNIT_1 = UnitEntity.builder()
                .name("unit1")
                .description("description1")
                .photoLink("photoLink1")
                .build();
        UNIT_2 = UnitEntity.builder()
                .name("unit2")
                .description("description2")
                .photoLink("photoLink2")
                .build();

        repository.deleteAll();
        repository.saveAllAndFlush(List.of(UNIT_1, UNIT_2));
    }

    @Test
    @DisplayName("Получение всех unit")
    void get_all_unit_success() throws Exception {

        var response = mvc.perform(get("/api/v1/unit"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        var result = objectMapper.readValue(response, new TypeReference<List<UnitDto>>() {
        });
        var resultNames = result.stream().map(UnitDto::getName).toList();
        assertTrue(resultNames.containsAll(List.of(UNIT_1.getName(), UNIT_2.getName())));
    }

    @Test
    @DisplayName("Получение всех unit без значений")
    void get_all_unit_empty_list() throws Exception {

        repository.deleteAll();

        var response = mvc.perform(get("/api/v1/unit"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        var result = objectMapper.readValue(response, new TypeReference<List<UnitDto>>() {
        });
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Получение конкретного unit по external_id")
    void get_unit_by_external_id_success() throws Exception {

        mvc.perform(get("/api/v1/unit/" + UNIT_1.getExternalId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.externalId").value(UNIT_1.getExternalId().toString()))
                .andExpect(jsonPath("$.name").value(UNIT_1.getName()))
                .andExpect(jsonPath("$.photoLink").value(UNIT_1.getPhotoLink()))
                .andExpect(jsonPath("$.description").value(UNIT_1.getDescription()));
    }

    @Test
    @DisplayName("Получение конкретного unit по external_id - не найдено")
    void get_unit_by_external_id_not_found() throws Exception {

        mvc.perform(get("/api/v1/unit/" + UUID.randomUUID()))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Получение конкретного unit по external_id - валидация")
    void get_unit_by_external_id_validation() throws Exception {

        mvc.perform(get("/api/v1/unit/"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Изменение unit")
    void patch_unit_success() throws Exception {

        var request = UpdateUnitRequest.builder()
                .externalId(UNIT_1.getExternalId())
                .name(UNIT_1.getName() + "_new")
                .description(UNIT_1.getDescription() + "_new")
                .photoLink(UNIT_1.getPhotoLink() + "_new")
                .build();

        mvc.perform(patch("/api/v1/unit/internal")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        var entity = repository.findByExternalId(UNIT_1.getExternalId()).orElseThrow();
        assertEquals(request.getName(), entity.getName());
        assertEquals(request.getDescription(), entity.getDescription());
        assertEquals(request.getPhotoLink(), entity.getPhotoLink());
    }

    @Test
    @DisplayName("Изменение unit - валидация")
    void patch_unit_validation() throws Exception {

        var request = UpdateUnitRequest.builder()
                .externalId(null)
                .build();

        mvc.perform(patch("/api/v1/unit/internal")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(400));
    }

    @Test
    @DisplayName("Создание unit")
    void create_unit_success() throws Exception {

        var request = CreateUnitRequest.builder()
                .name(UNIT_1.getName() + "_new")
                .description(UNIT_1.getDescription() + "_new")
                .photoLink(UNIT_1.getPhotoLink() + "_new")
                .build();

        mvc.perform(post("/api/v1/unit/internal")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        var entity = repository.findByName(request.getName()).orElseThrow();
        assertEquals(request.getName(), entity.getName());
        assertEquals(request.getDescription(), entity.getDescription());
        assertEquals(request.getPhotoLink(), entity.getPhotoLink());
    }

    @Test
    @DisplayName("Создание unit - валидация")
    void create_unit_validation() throws Exception {

        var request = CreateUnitRequest.builder()
                .build();

        mvc.perform(post("/api/v1/unit/internal")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(400));
    }

    @Test
    @DisplayName("Удаление unit")
    void delete_unit_success() throws Exception {

        mvc.perform(delete("/api/v1/unit/" + UNIT_1.getExternalId() + "/internal"))
                .andDo(print())
                .andExpect(status().isOk());

        assertTrue(repository.findByExternalId(UNIT_1.getExternalId()).isEmpty());
    }

    @Test
    @DisplayName("Удаление не существующего unit")
    void delete_not_exist_unit() throws Exception {

        mvc.perform(delete("/api/v1/unit/" + UUID.randomUUID() + "/internal"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
