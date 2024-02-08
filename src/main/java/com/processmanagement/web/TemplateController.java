package com.processmanagement.web;

import com.example.processmanagementtool.api.v1.TemplateApi;
import com.example.processmanagementtool.dto.SuccessResponseDTO;
import com.example.processmanagementtool.dto.TemplatePageDTO;
import com.example.processmanagementtool.dto.TemplateRequestDTO;
import com.processmanagement.template.TemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
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
    public Mono<ResponseEntity<TemplatePageDTO>> getUserTemplates(Integer page, Integer size, ServerWebExchange exchange) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return exchange.getPrincipal()
                .flatMap(principal -> templateService.findAllTemplatesByUser(principal, pageRequest))
                .map(ResponseEntity::ok);
    }
}
