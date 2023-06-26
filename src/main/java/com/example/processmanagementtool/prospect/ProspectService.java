package com.example.processmanagementtool.prospect;

import com.example.processmanagementtool.domain.prospect.Prospect;
import com.example.processmanagementtool.domain.prospect.repository.ProspectRepository;
import com.example.processmanagementtool.domain.template.Template;
import com.example.processmanagementtool.domain.template.repository.TemplateRepository;
import com.example.processmanagementtool.domain.user.repository.UserRepository;
import com.example.processmanagementtool.dto.ProspectPropertyDTO;
import com.example.processmanagementtool.dto.ProspectRequestDTO;
import com.example.processmanagementtool.dto.SuccessResponseDTO;
import com.example.processmanagementtool.exception.customexceptions.BadRequest;
import com.example.processmanagementtool.exception.customexceptions.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ProspectService {

    private final ProspectRepository prospectRepository;
    private final TemplateRepository templateRepository;
    private final UserRepository userRepository;

    public Mono<SuccessResponseDTO> createNewProspect(Principal principal, Mono<ProspectRequestDTO> request) {
        return userRepository.findUserByLogin(principal.getName())
                .flatMap(foundUser -> findTemplateAndValidateData(request, foundUser.getId())
                .flatMap(validatedData -> mapValidatedRequestToProspectAndSave(validatedData, foundUser.getId())))
                .switchIfEmpty(Mono.error(new UserNotFoundException()));
    }

    private Mono<ProspectRequestDTO> findTemplateAndValidateData(Mono<ProspectRequestDTO> request, Long userId){
        return request.flatMap(data -> templateRepository.findTemplateByUserIdAndId(userId, data.getTemplateId())
                .flatMap(foundTemplate -> validateProspectProperties(foundTemplate, data)))
                .switchIfEmpty(Mono.error(new BadRequest("Template not found")));
    }

    private Mono<ProspectRequestDTO> validateProspectProperties(Template template, ProspectRequestDTO request) {
        List<String> wrongKeys = template.getFields().entrySet()
                .stream()
                .filter(entry -> entry.getValue().getRequired())
                .filter(property -> checkIfPropertyIsNull(property.getKey(), request.getFields()))
                .map(Map.Entry::getKey)
                .toList();

        if (!wrongKeys.isEmpty()) {
            return Mono.error(new BadRequest("Required fields are not filled"));
        }
        return Mono.just(request);
    }

    private boolean checkIfPropertyIsNull(String key, Map<String, ProspectPropertyDTO> requestFields) {
        ProspectPropertyDTO value = requestFields.get(key);
        return Objects.isNull(value.getData());
    }

    private Mono<SuccessResponseDTO> mapValidatedRequestToProspectAndSave(ProspectRequestDTO prospectRequestDTO, Long userId) {
        Prospect prospect = ProspectDTOMapper.mapDTOtoProspect(prospectRequestDTO);
        prospect.setUserId(userId);
        return prospectRepository.save(prospect)
                .flatMap(res -> Mono.just(SuccessResponseDTO.builder()
                        .message("Successfully created new prospect")
                        .build()));
    }
}
