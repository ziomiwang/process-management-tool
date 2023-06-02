package com.example.processmanagementtool.web;

import com.example.processmanagementtool.api.v1.TeamApi;
import com.example.processmanagementtool.dto.SuccessResponseDTO;
import com.example.processmanagementtool.dto.TeamRequestDTO;
import com.example.processmanagementtool.team.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class TeamController implements TeamApi {

    private final TeamService teamService;

    @Override
    public Mono<ResponseEntity<SuccessResponseDTO>> inviteUsersAndCreateTeam(Long id, Mono<TeamRequestDTO> teamRequestDTO, ServerWebExchange exchange) {
        return teamService.inviteUsersToTeam(id, teamRequestDTO)
                .map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<SuccessResponseDTO>> leaveCurrentTeam(Long id, ServerWebExchange exchange) {
        return teamService.leaveCurrentTeam(id)
                .map(ResponseEntity::ok);
    }
}
