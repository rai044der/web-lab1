package ru.abradox.carsbusinesscard.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MarkCallBackAsProcessedRequest {

    @NotNull
    private UUID externalId;
}
