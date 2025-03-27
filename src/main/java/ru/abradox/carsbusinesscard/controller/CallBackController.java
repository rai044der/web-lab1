package ru.abradox.carsbusinesscard.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.abradox.carsbusinesscard.dto.CallBackDto;
import ru.abradox.carsbusinesscard.dto.request.CreateCallBackRequest;
import ru.abradox.carsbusinesscard.dto.request.MarkCallBackAsProcessedRequest;
import ru.abradox.carsbusinesscard.service.CallBackService;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/call_back")
public class CallBackController {

    private final CallBackService service;

    @GetMapping("/internal")
    public ResponseEntity<List<CallBackDto>> getAll(
            @RequestParam(required = false, defaultValue = "false") Boolean includeProcessed) {
        log.info("Принят запрос на получение всех call_back: includeProcessed={}", includeProcessed);
        var values = service.getAll(includeProcessed);
        return ResponseEntity.ok(values);
    }

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody CreateCallBackRequest request) {
        log.info("Принят запрос на создание call_back: {}", request);
        service.create(request);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/internal")
    public ResponseEntity<Void> markCallBackAsProcessed(@Valid @RequestBody MarkCallBackAsProcessedRequest request) {
        log.info("Принят запрос на обновление call_back: {}", request);
        service.markAsProcessed(request);
        return ResponseEntity.ok().build();
    }
}
