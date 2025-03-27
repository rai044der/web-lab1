package ru.abradox.carsbusinesscard.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.abradox.carsbusinesscard.dto.UnitDto;
import ru.abradox.carsbusinesscard.dto.request.CreateUnitRequest;
import ru.abradox.carsbusinesscard.dto.request.UpdateUnitRequest;
import ru.abradox.carsbusinesscard.entity.UnitEntity;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UnitMapper {

    UnitDto toDto(UnitEntity entity);

    List<UnitDto> toDto(List<UnitEntity> entity);

    @Mapping(target = "externalId", ignore = true)
    void update(@MappingTarget UnitEntity unit, UpdateUnitRequest request);

    @Mapping(target = "externalId", expression = "java(java.util.UUID.randomUUID())")
    UnitEntity toEntity(CreateUnitRequest dto);
}
