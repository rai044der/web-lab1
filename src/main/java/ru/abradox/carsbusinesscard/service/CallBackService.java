package ru.abradox.carsbusinesscard.service;

import ru.abradox.carsbusinesscard.dto.CallBackDto;
import ru.abradox.carsbusinesscard.dto.request.CreateCallBackRequest;
import ru.abradox.carsbusinesscard.dto.request.MarkCallBackAsProcessedRequest;

import java.util.List;

public interface CallBackService {

    List<CallBackDto> getAll(boolean includeProcessed);

    void create(CreateCallBackRequest request);

    void markAsProcessed(MarkCallBackAsProcessedRequest request);
}
