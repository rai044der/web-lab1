package ru.abradox.carsbusinesscard.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.abradox.carsbusinesscard.dto.CallBackDto;
import ru.abradox.carsbusinesscard.dto.request.CreateCallBackRequest;
import ru.abradox.carsbusinesscard.entity.CallBackEntity;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CallBackMapper {

    List<CallBackDto> toDto(List<CallBackEntity> entities);

    @Mapping(target = "externalId", expression = "java(java.util.UUID.randomUUID())")
    CallBackEntity toEntity(CreateCallBackRequest dto);
}
