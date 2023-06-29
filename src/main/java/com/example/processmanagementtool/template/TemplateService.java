package com.example.processmanagementtool.template;

import com.example.processmanagementtool.domain.template.Template;
import com.example.processmanagementtool.domain.template.repository.TemplateRepository;
import com.example.processmanagementtool.domain.user.repository.UserRepository;
import com.example.processmanagementtool.dto.SuccessResponseDTO;
import com.example.processmanagementtool.dto.TemplatePageDTO;
import com.example.processmanagementtool.dto.TemplateRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.security.Principal;
import java.util.List;

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

    public Mono<TemplatePageDTO> findAllTemplatesByUser(Principal principal, PageRequest pageRequest) {
        return userRepository.findUserByLogin(principal.getName())
                .flatMap(foundUser -> getMapAndZipTemplates(foundUser.getId(), pageRequest))
                .map(res -> new PageImpl<>(res.getT1(), pageRequest, res.getT2()))
                .flatMap(this::mapTemplatePageToTemplatePageDTO);
    }

    private Mono<TemplatePageDTO> mapTemplatePageToTemplatePageDTO(Page<Template> page) {
        return Mono.just(TemplatePageDTO.builder()
                .totalPages(page.getTotalPages())
                .totalElements(page.getTotalElements())
                .pageElements(page.getNumberOfElements())
                .content(TemplateDTOMapper.mapTemplateToTemplateResponseDTO(page.getContent()))
                .responseType("template")
                .build());
    }

    private Mono<Tuple2<List<Template>, Long>> getMapAndZipTemplates(Long userId, PageRequest pageRequest) {
        return templateRepository.findAllByUserId(userId, pageRequest).collectList()
                .zipWith(templateRepository.findAllByUserId(userId).count());
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
