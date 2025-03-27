package ru.abradox.carsbusinesscard.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCallBackRequest {

    @NotBlank
    @Size(max = 255)
    private String name;

    @Size(max = 2048)
    private String description;

    @NotBlank
    @Size(max = 32)
    private String phone;
}
