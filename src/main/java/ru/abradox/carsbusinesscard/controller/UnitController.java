package ru.abradox.carsbusinesscard.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.abradox.carsbusinesscard.dto.UnitDto;
import ru.abradox.carsbusinesscard.dto.request.CreateUnitRequest;
import ru.abradox.carsbusinesscard.dto.request.UpdateUnitRequest;
import ru.abradox.carsbusinesscard.service.UnitService;

import java.util.List;
import java.util.UUID;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/unit")
public class UnitController {

    private final UnitService service;

    @GetMapping
    public ResponseEntity<List<UnitDto>> getAll() {
        log.info("Принят запрос на получение всех unit");
        var values = service.getAll();
        return ResponseEntity.ok(values);
    }

    @GetMapping("/{external_id}")
    public ResponseEntity<UnitDto> getByExternalId(
            @Valid @NotNull @PathVariable("external_id") UUID externalId) {
        log.info("Принят запрос на получение unit по externalId={}", externalId);
        var value = service.getByExternalId(externalId);
        return value.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }


    @PostMapping("/internal")
    public ResponseEntity<Void> create(@Valid @RequestBody CreateUnitRequest request) {
        log.info("Принят запрос на создание unit: {}", request);
        service.create(request);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/internal")
    public ResponseEntity<Void> patch(@Valid @RequestBody UpdateUnitRequest request) {
        log.info("Принят запрос на обновление unit: {}", request);
        service.update(request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{external_id}/internal")
    public ResponseEntity<Void> delete(
            @Valid @NotNull @PathVariable("external_id") UUID externalId) {
        log.info("Принят запрос на удаление unit: {}", externalId);
        service.delete(externalId);
        return ResponseEntity.ok().build();
    }
}
