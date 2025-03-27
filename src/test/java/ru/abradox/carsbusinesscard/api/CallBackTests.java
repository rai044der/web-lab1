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
import ru.abradox.carsbusinesscard.dto.CallBackDto;
import ru.abradox.carsbusinesscard.dto.request.CreateCallBackRequest;
import ru.abradox.carsbusinesscard.dto.request.MarkCallBackAsProcessedRequest;
import ru.abradox.carsbusinesscard.entity.CallBackEntity;
import ru.abradox.carsbusinesscard.repository.CallBackRepository;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class CallBackTests {

    @Autowired
    CallBackRepository repository;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mvc;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
        repository.save(getTestEntity(false));
        repository.save(getTestEntity(true));
    }

    @Test
    @DisplayName("Получение всех call_back")
    void get_all_call_back_success() throws Exception {

        var response = mvc.perform(get("/api/v1/call_back/internal"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        var result = objectMapper.readValue(response, new TypeReference<List<CallBackDto>>() {
        });
        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("Получение всех call_back c удаленными")
    void get_all_call_back_with_processed_success() throws Exception {

        var response = mvc.perform(get("/api/v1/call_back/internal").param("includeProcessed", "true"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        var result = objectMapper.readValue(response, new TypeReference<List<CallBackDto>>() {
        });
        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("Получение всех call_back без значений")
    void get_all_call_back_empty_list() throws Exception {

        repository.deleteAll();

        var response = mvc.perform(get("/api/v1/call_back/internal"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        var result = objectMapper.readValue(response, new TypeReference<List<CallBackDto>>() {
        });
        assertTrue(result.isEmpty());
    }

    private CallBackEntity getTestEntity(boolean includeProcessed) {
        return CallBackEntity.builder()
                .name("name")
                .description("description")
                .phone("phone")
                .processed(includeProcessed)
                .build();
    }

    @Test
    @DisplayName("Создание call_back")
    void create_call_back_success() throws Exception {

        repository.deleteAll();

        var request = CreateCallBackRequest.builder()
                .name("new_name")
                .description("new_description")
                .phone("new_phone")
                .build();

        mvc.perform(post("/api/v1/call_back")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        get_all_call_back_success();
    }

    @Test
    @DisplayName("Отметка call_back как обработанного")
    void patch_call_back_success() throws Exception {

        var entity = repository.findAll().stream()
                .filter(c -> !c.getProcessed())
                .findFirst()
                .orElseThrow();
        var externalId = entity.getExternalId();

        var request = new MarkCallBackAsProcessedRequest(externalId);

        mvc.perform(patch("/api/v1/call_back/internal")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        entity = repository.findByExternalId(externalId).orElseThrow();
        assertTrue(entity.getProcessed());
    }

    @Test
    @DisplayName("Отметка несуществующего call_back как обработанного")
    void patch_call_back_not_exist() throws Exception {
        var externalId = UUID.randomUUID();

        var request = new MarkCallBackAsProcessedRequest(externalId);

        mvc.perform(patch("/api/v1/call_back/internal")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        var entity = repository.findByExternalId(externalId);
        assertTrue(entity.isEmpty());
    }
}
