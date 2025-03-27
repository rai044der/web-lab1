package ru.abradox.carsbusinesscard.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.abradox.carsbusinesscard.dto.CallBackDto;
import ru.abradox.carsbusinesscard.dto.request.CreateCallBackRequest;
import ru.abradox.carsbusinesscard.dto.request.MarkCallBackAsProcessedRequest;
import ru.abradox.carsbusinesscard.repository.CallBackRepository;
import ru.abradox.carsbusinesscard.service.CallBackService;
import ru.abradox.carsbusinesscard.service.mapper.CallBackMapper;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class JpaCallBackService implements CallBackService {

    private final CallBackRepository repository;
    private final CallBackMapper mapper;

    @Override
    public List<CallBackDto> getAll(boolean includeProcessed) {
        log.info("Запрашиваю все записи call_back");
        var entities = repository.findAllWithIncludeDeletedAndSortByCreated(includeProcessed);
        var dtoList = mapper.toDto(entities);
        log.info("Получено {} call_back: {}", dtoList.size(), dtoList);
        return dtoList;
    }

    @Override
    public void create(CreateCallBackRequest request) {
        log.info("Создаю call_back с параметрами {}", request);
        var callBack = mapper.toEntity(request);
        callBack = repository.save(callBack);
        log.info("Создание успешно: {}", callBack);
    }

    @Override
    @Retryable(retryFor = Exception.class)
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void markAsProcessed(MarkCallBackAsProcessedRequest request) {
        var externalId = request.getExternalId();
        log.info("Отмечаю call_back {} как завершенный", externalId);
        var count = repository.updateProcessed(externalId);
        log.info("Успешное завершение! Количество измененных записей: {}", count);
    }
}
