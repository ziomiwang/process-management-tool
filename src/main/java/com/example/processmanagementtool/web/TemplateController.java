package com.example.processmanagementtool.web;

import com.example.processmanagementtool.api.v1.TemplateApi;
import com.example.processmanagementtool.dto.SuccessResponseDTO;
import com.example.processmanagementtool.dto.TemplateRequestDTO;
import com.example.processmanagementtool.dto.TemplateResponseDTO;
import com.example.processmanagementtool.template.TemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class TemplateController implements TemplateApi {

    private final TemplateService templateService;

    @Override
    public Mono<ResponseEntity<SuccessResponseDTO>> createTemplate(Mono<TemplateRequestDTO> templateRequestDTO, ServerWebExchange exchange) {
        return exchange.getPrincipal()
                .flatMap(principal -> templateService.createTemplate(principal, templateRequestDTO))
                .map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<Flux<TemplateResponseDTO>>> getUserTemplates(ServerWebExchange exchange) {
        return exchange.getPrincipal()
                .map(templateService::findAllTemplatesByUser)
                .map(ResponseEntity::ok);
    }
}
