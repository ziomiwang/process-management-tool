package com.example.processmanagementtool.template;

import com.example.processmanagementtool.domain.template.Template;
import com.example.processmanagementtool.domain.template.repository.TemplateRepository;
import com.example.processmanagementtool.domain.user.repository.UserRepository;
import com.example.processmanagementtool.dto.SuccessResponseDTO;
import com.example.processmanagementtool.dto.TemplateRequestDTO;
import com.example.processmanagementtool.dto.TemplateResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.security.Principal;

@Service
@RequiredArgsConstructor
public class TemplateService {

    private final TemplateRepository templateRepository;
    private final UserRepository userRepository;

    public Mono<SuccessResponseDTO> createTemplate(Principal principal, Mono<TemplateRequestDTO> request) {
        return userRepository.findUserByLogin(principal.getName())
                .flatMap(foundUser -> request
                        .flatMap(data -> mapRequestToTemplateAndSave(data, foundUser.getId())));
    }

    public Flux<TemplateResponseDTO> findAllTemplatesByUser(Principal principal) {
        return userRepository.findUserByLogin(principal.getName())
                .flatMapMany(foundUser -> findTemplates(foundUser.getId()));
    }

    private Flux<TemplateResponseDTO> findTemplates(Long userId) {
        return templateRepository.findAllByUserId(userId)
                .map(TemplateDTOMapper::mapTemplateToTemplateResponseDTO);
    }

    private Mono<SuccessResponseDTO> mapRequestToTemplateAndSave(TemplateRequestDTO data, Long userId) {
        Template template = TemplateDTOMapper.mapTemplateRequestDTOToTemplate(data);
        template.setUserId(userId);
        return templateRepository.save(template)
                .flatMap(savedTemplate -> Mono.just(SuccessResponseDTO.builder()
                                .message("success")
                        .build()));
    }
}
