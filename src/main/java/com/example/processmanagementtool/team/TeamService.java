package com.example.processmanagementtool.team;

import com.example.processmanagementtool.domain.team.Team;
import com.example.processmanagementtool.domain.team.repository.TeamRepository;
import com.example.processmanagementtool.domain.user.TeamMembershipType;
import com.example.processmanagementtool.domain.user.User;
import com.example.processmanagementtool.domain.user.repository.UserRepository;
import com.example.processmanagementtool.dto.SuccessResponseDTO;
import com.example.processmanagementtool.dto.TeamRequestDTO;
import com.example.processmanagementtool.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    //User nie powinien moc zaprosic samego siebie
    //Wyjscie ownera z teamu = usuniecie z teamu
    public Mono<SuccessResponseDTO> inviteUsersToTeam(Long id, Mono<TeamRequestDTO> requestData) {
        return requestData.flatMap(data -> userRepository.findById(id).flatMap(sender -> validateRequest(data.getUserIds(), sender)))
                .flatMap(validated -> findOwnerUserAndInvite(id, validated))
                .flatMap(this::mapInviteOutputToResponse);
    }

    public Mono<SuccessResponseDTO> leaveCurrentTeam(Long id) {
        return userRepository.findById(id)
                .flatMap(this::validateMembership)
                .switchIfEmpty(Mono.error(new UserNotFoundException("User not found")));
    }

    private Mono<SuccessResponseDTO> validateMembership(User user) {
        if (Objects.isNull(user.getMembershipType())){
            return Mono.error(new UserNotFoundException("User doesn't belong to any team"));
        }
        if (user.getMembershipType().equals(TeamMembershipType.OWNER)) {
            return handleOwnerTeamDisband(user)
                    .flatMap(this::processOutputToResponse);
        }
        return updateUserTeamInfo(user)
                .flatMap(this::processOutputToResponse);
    }

    private Mono<List<User>> handleOwnerTeamDisband(User teamOwner) {
        return userRepository.findAllByTeamId(teamOwner.getTeamId())
                .flatMap(this::updateUserTeamInfo)
                .collectList()
                .flatMap(result -> teamRepository.deleteById(teamOwner.getTeamId())
                        .then(Mono.just(result)));
    }

    private Mono<User> updateUserTeamInfo(User user) {
        user.setTeamId(null);
        user.setMembershipType(null);
        return userRepository.save(user);
    }

    private Mono<SuccessResponseDTO> processOutputToResponse(User data) {
        return Mono.just(SuccessResponseDTO.builder()
                .message("User successfully left current team")
                .build());
    }

    private Mono<SuccessResponseDTO> processOutputToResponse(List<User> data) {
        return Mono.just(SuccessResponseDTO.builder()
                .message("Team successfully disbanded")
                        .data(data)
                .build());
    }

    //TODO new exception
    private Mono<List<String>> validateRequest(List<String> request, User sender) {
        if (request.stream().distinct().count() != request.size()) {
            return Mono.error(new UserNotFoundException("Invalid input data"));
        }
        if (request.contains(sender.getLogin())){
            return Mono.error(new UserNotFoundException("User cannot invite himself"));
        }
        return Mono.just(request);
    }

    private Mono<List<User>> findOwnerUserAndInvite(Long id, List<String> input) {
        return findUser(id)
                .flatMap(userWithTeam -> inviteUsersToTeam(userWithTeam, input).collectList());
    }

    private Mono<User> findUser(Long id) {
        return userRepository.findById(id)
                .flatMap(this::checkIfUserAlreadyOwnsATeam);
    }

    private Mono<User> checkIfUserAlreadyOwnsATeam(User user) {
        if (user.getMembershipType() != null && user.getMembershipType().equals(TeamMembershipType.OWNER)) {
            return Mono.just(user);
        }
        Team newTeam = Team.builder()
                .ownerId(user.getId())
                .build();

        return teamRepository.save(newTeam)
                .flatMap(savedTeam -> assignOwnerToNewTeam(user, savedTeam));
    }

    private Flux<User> inviteUsersToTeam(User user, List<String> request) {
        return teamRepository.findTeamByOwnerId(user.getId())
                .flatMapMany(team -> findUsersByIds(team, request));
    }

    private Flux<User> findUsersByIds(Team team, List<String> logins) {
        return userRepository.findAllByLoginIn(logins)
                .flatMap(matchedUsers -> inviteUserToTeam(team, matchedUsers));
    }

    private Mono<SuccessResponseDTO> mapInviteOutputToResponse(List<User> output) {
        return Mono.just(SuccessResponseDTO.builder()
                .message("Successfully sent " + output.size() + " invitations to team")
                .data(output)
                .build());
    }

    private Mono<User> assignOwnerToNewTeam(User user, Team team) {
        user.setTeamId(team.getId());
        user.setMembershipType(TeamMembershipType.OWNER);
        return userRepository.save(user);
    }

    private Mono<User> inviteUserToTeam(Team team, User user) {
        user.setTeamId(team.getId());
        user.setMembershipType(TeamMembershipType.MEMBER);
        return userRepository.save(user);
    }
}
