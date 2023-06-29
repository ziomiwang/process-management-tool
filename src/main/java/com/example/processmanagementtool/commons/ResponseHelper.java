package com.example.processmanagementtool.commons;

import com.example.processmanagementtool.dto.SuccessResponseDTO;
import reactor.core.publisher.Mono;

public class ResponseHelper {

    public static Mono<SuccessResponseDTO> buildSuccessResponse(String message, Object data) {
        return Mono.just(SuccessResponseDTO.builder()
                .message(message)
                .data(data)
                .build());
    }

    public static Mono<SuccessResponseDTO> buildSuccessResponse(String message) {
        return Mono.just(SuccessResponseDTO.builder()
                .message(message)
                .data(null)
                .build());
    }
}
