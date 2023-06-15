package com.example.processmanagementtool.web;

import com.example.config.TeamControllerTestConfiguration;
import com.example.processmanagementtool.dto.SuccessResponseDTO;
import com.example.processmanagementtool.dto.TeamRequestDTO;
import com.example.processmanagementtool.dto.UserRequestDTO;
import com.example.processmanagementtool.team.InviteTeamService;
import com.example.processmanagementtool.user.dto.SimpleUser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@WebFluxTest
@Import(TeamControllerTestConfiguration.class)
class TeamControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Mock
    private Principal principal;

    @Autowired
    @InjectMocks
    private InviteTeamService inviteTeamService;


    private final UserRequestDTO userToInvite = UserRequestDTO.builder()
            .login("test")
            .password("password")
            .name("Adam")
            .build();

    private final TeamRequestDTO teamRequest = TeamRequestDTO.builder()
            .userIds(List.of("test"))
            .build();

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

        webTestClient
                .mutateWith(SecurityMockServerConfigurers.csrf())
                .post()
                .uri("/auth/register")
                .body(Mono.just(userToInvite), UserRequestDTO.class)
                .accept(MediaType.APPLICATION_JSON)
                .exchange();

        createTeamBefore();
    }


    private void createTeamBefore() {
        Mockito.when(principal.getName()).thenReturn("user");
        Mono<SuccessResponseDTO> successResponseDTOMono = inviteTeamService.inviteUsersToTeam(principal, Mono.just(teamRequest));
        successResponseDTOMono.block();
    }

    @Test
    @WithMockUser
    void shouldCreateNewTeamAndInviteUsers() {
        webTestClient
                .mutateWith(SecurityMockServerConfigurers.csrf())
                .post().uri("/team")
                .body(Mono.just(teamRequest), TeamRequestDTO.class)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(SuccessResponseDTO.class)
                .value(res -> {
                    ObjectMapper objectMapper = new ObjectMapper();
                    List<SimpleUser> simpleUsers = objectMapper.convertValue(res.getData(), new TypeReference<>() {
                    });
                    SimpleUser simpleUser = simpleUsers.get(0);
                    assertEquals(simpleUser.getLogin(), userToInvite.getLogin());
                });
    }

    @Test
    @WithMockUser(username = "test", password = "test")
    void shouldLeaveCurrentTeam() {
        createTeamBefore();
        webTestClient
                .mutateWith(SecurityMockServerConfigurers.csrf())
                .post().uri("/team/leave")
                .body(Mono.just(teamRequest), TeamRequestDTO.class)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(SuccessResponseDTO.class)
                .isEqualTo(SuccessResponseDTO.builder()
                        .message("User successfully left current team")
                        .build());
    }

    @Test
    @WithMockUser
    void shouldRemoveTeamMembersAndDeleteTeam() {
        createTeamBefore();
        webTestClient
                .mutateWith(SecurityMockServerConfigurers.csrf())
                .post().uri("/team/leave")
                .body(Mono.just(teamRequest), TeamRequestDTO.class)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(SuccessResponseDTO.class)
                .value(res -> assertEquals(res.getMessage(), "Team successfully disbanded"));
    }
}