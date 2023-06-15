package com.example.processmanagementtool.team;

import com.example.processmanagementtool.domain.team.Team;
import com.example.processmanagementtool.domain.team.repository.TeamRepository;
import com.example.processmanagementtool.domain.user.TeamMembershipType;
import com.example.processmanagementtool.domain.user.User;
import com.example.processmanagementtool.domain.user.repository.UserRepository;
import com.example.processmanagementtool.dto.SuccessResponseDTO;
import com.example.processmanagementtool.dto.TeamRequestDTO;
import com.example.processmanagementtool.exception.customexceptions.BadRequest;
import com.example.processmanagementtool.exception.customexceptions.UserNotFoundException;
import com.example.processmanagementtool.user.UserDTOMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class InviteTeamService {

    private final UserRepository userRepository;
    private final TeamRepository teamRepository;

    public Mono<SuccessResponseDTO> inviteUsersToTeam(Principal principal, Mono<TeamRequestDTO> requestData) {
        return requestData.flatMap(data -> processRequest(principal.getName(), data))
                .flatMap(this::invitationsResponse);
    }

    private Mono<List<User>> processRequest(String currentUser, TeamRequestDTO request) {
        return userRepository.findUserByLogin(currentUser)
                .flatMap(foundSender -> checkIfRequestContainsOwner(new HashSet<>(request.getUserIds()), foundSender)
                        .flatMap(validated -> findOwnerUserAndInvite(foundSender, validated))
                        .switchIfEmpty(Mono.error(new UserNotFoundException())));
    }

    private Mono<Set<String>> checkIfRequestContainsOwner(Set<String> request, User sender) {
        if (request.contains(sender.getLogin())) {
            return Mono.error(new BadRequest("User cannot invite himself"));
        }
        return Mono.just(request);
    }

    private Mono<List<User>> findOwnerUserAndInvite(User user, Set<String> input) {
        return createNewTeamIfNotPresent(user)
                .flatMap(userWithTeam -> findUsersAndSetToTeam(userWithTeam, input).collectList());
    }

    private Mono<User> createNewTeamIfNotPresent(User user) {
        if (user.getMembershipType() != null && user.getMembershipType().equals(TeamMembershipType.OWNER)) {
            return Mono.just(user);
        }
        Team newTeam = Team.builder()
                .ownerId(user.getId())
                .build();

        return teamRepository.save(newTeam)
                .flatMap(savedTeam -> assignOwnerToNewTeam(user, savedTeam));
    }

    private Flux<User> findUsersAndSetToTeam(User owner, Set<String> userEmails) {
        return userRepository.findAllByLoginIn(userEmails)
                .flatMap(matchedUsers -> setSingleUserToTeam(owner.getTeamId(), matchedUsers));
    }

    private Mono<User> assignOwnerToNewTeam(User user, Team team) {
        user.setTeamId(team.getId());
        user.setMembershipType(TeamMembershipType.OWNER);
        return userRepository.save(user);
    }

    private Mono<User> setSingleUserToTeam(Long teamId, User user) {
        user.setTeamId(teamId);
        user.setMembershipType(TeamMembershipType.MEMBER);
        return userRepository.save(user);
    }

    private Mono<SuccessResponseDTO> invitationsResponse(List<User> output) {
        return Mono.just(SuccessResponseDTO.builder()
                .message("Successfully sent " + output.size() + " invitations to team")
                .data(UserDTOMapper.mapUserToSimpleUser(output))
                .build());
    }
}
