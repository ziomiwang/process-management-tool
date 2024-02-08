package com.processmanagement.template;

import com.example.processmanagementtool.dto.SuccessResponseDTO;
import com.example.processmanagementtool.dto.TemplatePageDTO;
import com.example.processmanagementtool.dto.TemplateRequestDTO;
import com.processmanagement.commons.ResponseHelper;
import com.processmanagement.domain.template.Template;
import com.processmanagement.domain.template.repository.TemplateRepository;
import com.processmanagement.domain.user.repository.UserRepository;
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
                .flatMap(foundUser -> getAndZipTemplates(foundUser.getId(), pageRequest))
                .flatMap(tuple2 -> mapTemplatePageToTemplatePageDTO(new PageImpl<>(tuple2.getT1(), pageRequest, tuple2.getT2())));
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

    private Mono<Tuple2<List<Template>, Long>> getAndZipTemplates(Long userId, PageRequest pageRequest) {
        return templateRepository.findAllByUserId(userId, pageRequest).collectList()
                .zipWith(templateRepository.countByUserId(userId));
    }

    private Mono<SuccessResponseDTO> mapRequestToTemplateAndSave(TemplateRequestDTO data, Long userId) {
        Template template = TemplateDTOMapper.mapTemplateRequestDTOToTemplate(data);
        template.setUserId(userId);
        return templateRepository.save(template)
                .flatMap(savedTemplate -> ResponseHelper.buildSuccessResponse("Successfully created new template"));
    }
}
