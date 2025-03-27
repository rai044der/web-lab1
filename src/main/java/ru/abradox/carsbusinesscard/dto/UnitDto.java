package ru.abradox.carsbusinesscard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UnitDto {

    private UUID externalId;

    private String name;

    private String photoLink;

    private String description;
}
