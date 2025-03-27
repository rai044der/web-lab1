package ru.abradox.carsbusinesscard.service;

import ru.abradox.carsbusinesscard.dto.UnitDto;
import ru.abradox.carsbusinesscard.dto.request.CreateUnitRequest;
import ru.abradox.carsbusinesscard.dto.request.UpdateUnitRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UnitService {

    Optional<UnitDto> getByExternalId(UUID externalId);

    List<UnitDto> getAll();

    void update(UpdateUnitRequest request);

    void create(CreateUnitRequest request);

    void delete(UUID externalId);
}
