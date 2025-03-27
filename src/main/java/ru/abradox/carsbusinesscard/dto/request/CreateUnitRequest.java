package ru.abradox.carsbusinesscard.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUnitRequest {

    @NotBlank
    @Size(max = 255)
    private String name;

    @Size(max = 2048)
    private String photoLink;

    private String description;
}
