package com.example.processmanagementtool.web;

import com.example.config.ProspectAndTemplateControllerTestConfiguration;
import com.example.processmanagementtool.dto.*;
import com.example.processmanagementtool.exception.customexceptions.BadRequest;
import com.example.processmanagementtool.prospect.ProspectService;
import com.example.processmanagementtool.template.TemplateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.util.Map;

@WebFluxTest
@AutoConfigureWebTestClient(timeout = "36000")
@Import({ProspectAndTemplateControllerTestConfiguration.class})
class ProspectAndTemplateControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Mock
    private Principal principal;

    @Autowired
    @InjectMocks
    private ProspectService prospectService;

    @Autowired
    @InjectMocks
    private TemplateService templateService;

    @BeforeEach
    void saveUsersToDb() {
        UserRequestDTO requestDTO = UserRequestDTO.builder()
                .login("user")
                .password("password")
                .name("test")
                .build();

        webTestClient
                .mutateWith(SecurityMockServerConfigurers.csrf())
                .post()
                .uri("/auth/register")
                .body(Mono.just(requestDTO), UserRequestDTO.class)
                .accept(MediaType.APPLICATION_JSON)
                .exchange();
    }

    @Test
    @WithMockUser
    void shouldCreateNewTemplateAndProspect() {

        TemplateRequestDTO templateRequestDTO = TemplateRequestDTO.builder()
                .name("test template")
                .fields(Map.of("name", TemplatePropertyDTO.builder()
                                .required(true)
                                .data("")
                                .build(),
                        "linkedin_url", TemplatePropertyDTO.builder()
                                .required(false)
                                .data("")
                                .build()))
                .build();

        ProspectRequestDTO prospectRequestDTO = ProspectRequestDTO.builder()
                .templateId(1L)
                .templateName("test template")
                .fields(Map.of("name", ProspectPropertyDTO.builder()
                                .data("karol")
                                .build(),
                        "linkedin_url", ProspectPropertyDTO.builder()
                                .data("linkedin.com/karol")
                                .build()))
                .build();

        webTestClient
                .mutateWith(SecurityMockServerConfigurers.csrf())
                .post().uri("/template")
                .body(Mono.just(templateRequestDTO), TemplateRequestDTO.class)
                .accept(MediaType.APPLICATION_JSON)
                .exchange();

        webTestClient.mutateWith(SecurityMockServerConfigurers.csrf())
                .post().uri("/prospect")
                .body(Mono.just(prospectRequestDTO), ProspectRequestDTO.class)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(SuccessResponseDTO.class)
                .isEqualTo(SuccessResponseDTO.builder()
                        .message("Successfully created new prospect")
                        .build());
    }

    @Test
    @WithMockUser
    void shouldThrowExceptionWhenRequiredFieldIsNotFilled() {

        TemplateRequestDTO templateRequestDTO = TemplateRequestDTO.builder()
                .name("test template")
                .fields(Map.of("name", TemplatePropertyDTO.builder()
                                .required(true)
                                .data("")
                                .build(),
                        "linkedin_url", TemplatePropertyDTO.builder()
                                .required(false)
                                .data("")
                                .build()))
                .build();

        ProspectRequestDTO prospectRequestDTO = ProspectRequestDTO.builder()
                .templateId(1L)
                .templateName("test template")
                .fields(Map.of("name", ProspectPropertyDTO.builder()
                                .data(null)
                                .build(),
                        "linkedin_url", ProspectPropertyDTO.builder()
                                .data("linkedin.com/karol")
                                .build()))
                .build();

        webTestClient
                .mutateWith(SecurityMockServerConfigurers.csrf())
                .post().uri("/template")
                .body(Mono.just(templateRequestDTO), TemplateRequestDTO.class)
                .accept(MediaType.APPLICATION_JSON)
                .exchange();

        webTestClient.mutateWith(SecurityMockServerConfigurers.csrf())
                .post().uri("/prospect")
                .body(Mono.just(prospectRequestDTO), ProspectRequestDTO.class)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody(BadRequest.class)
                .isEqualTo(new BadRequest("Required fields are not filled"));

    }
}