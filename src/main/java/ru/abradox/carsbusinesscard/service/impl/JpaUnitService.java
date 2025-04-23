package ru.abradox.carsbusinesscard.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.abradox.carsbusinesscard.dto.UnitDto;
import ru.abradox.carsbusinesscard.dto.request.CreateUnitRequest;
import ru.abradox.carsbusinesscard.dto.request.UpdateUnitRequest;
import ru.abradox.carsbusinesscard.repository.UnitRepository;
import ru.abradox.carsbusinesscard.service.UnitService;
import ru.abradox.carsbusinesscard.service.mapper.UnitMapper;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class JpaUnitService implements UnitService {

    private final UnitRepository repository;
    private final UnitMapper mapper;

    @Override
    @Cacheable("units")
    public Optional<UnitDto> getByExternalId(UUID externalId) {
        log.info("Запрашиваю unit с externalId={}", externalId);
        var entity = repository.findByExternalId(externalId);
        var dto = entity.map(mapper::toDto);
        log.info("Получено common_value: {}", dto.orElse(null));
        return dto;
    }

    @Override
    @Cacheable("units")
    public List<UnitDto> getAll() {
        log.info("Запрашиваю все записи unit");
        var entities = repository.findAll();
        var dtoList = mapper.toDto(entities);
        log.info("Получено {} unit: {}", dtoList.size(), dtoList);
        return dtoList;
    }

    @Override
    @CacheEvict(value = "units", allEntries = true)
    @Retryable(retryFor = Exception.class)
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void update(UpdateUnitRequest request) {
        var externalId = request.getExternalId();
        log.info("Изменяю unit с externalId {} с полями {}", externalId, request);
        var entity = repository.findByExternalId(externalId);
        entity.ifPresentOrElse(unit -> {
            log.info("Успешно получил запись из БД: {}", unit);
            mapper.update(unit, request);
            unit = repository.save(unit);
            log.info("Изменение успешно: {}", unit);
        }, () -> log.warn("Не найдено unit с externalId {}", externalId));
    }

    @Override
    @CacheEvict(value = "units", allEntries = true)
    @Retryable(retryFor = Exception.class)
    public void create(CreateUnitRequest request) {
        var isAlreadyExist = repository.existsByName(request.getName());
        if (isAlreadyExist) {
            log.warn("Unit с name={} уже существует", request.getName());
            return;
        }
        log.info("Создаю unit с параметрами {}", request);
        var unit = mapper.toEntity(request);
        unit = repository.save(unit);
        log.info("Создание успешно: {}", unit);
    }

    @Override
    @CacheEvict(value = "units", allEntries = true)
    @Transactional
    public void delete(UUID externalId) {
        log.info("Удаляю unit с externalId={}", externalId);
        var deletedRecords = repository.deleteByExternalId(externalId);
        if (deletedRecords == 0) {
            log.warn("Запись не найдена");
        } else {
            log.info("Успешное удаление");
        }
    }
}
