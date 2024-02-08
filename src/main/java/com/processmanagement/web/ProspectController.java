package com.processmanagement.web;

import com.example.processmanagementtool.api.v1.ProspectApi;
import com.example.processmanagementtool.dto.ProspectPageDTO;
import com.example.processmanagementtool.dto.ProspectRequestDTO;
import com.example.processmanagementtool.dto.SuccessResponseDTO;
import com.processmanagement.prospect.ProspectService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class ProspectController implements ProspectApi {

    private final ProspectService prospectService;

    @Override
    public Mono<ResponseEntity<SuccessResponseDTO>> createNewProspect(Mono<ProspectRequestDTO> prospectRequestDTO, ServerWebExchange exchange) {
        return exchange.getPrincipal()
                .flatMap(principal -> prospectService.createNewProspect(principal, prospectRequestDTO))
                .map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<ProspectPageDTO>> getUserProspects(Integer page, Integer size, ServerWebExchange exchange) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return exchange.getPrincipal()
                .flatMap(principal -> prospectService.findAllProspectsByUser(principal, pageRequest))
                .map(ResponseEntity::ok);
    }
}
