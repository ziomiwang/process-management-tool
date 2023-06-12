package com.example.processmanagementtool.team;

import com.example.processmanagementtool.domain.team.repository.TeamRepository;
import com.example.processmanagementtool.domain.user.TeamMembershipType;
import com.example.processmanagementtool.domain.user.User;
import com.example.processmanagementtool.domain.user.repository.UserRepository;
import com.example.processmanagementtool.dto.SuccessResponseDTO;
import com.example.processmanagementtool.exception.customexceptions.BadRequest;
import com.example.processmanagementtool.exception.customexceptions.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class LeaveTeamService {

    private final UserRepository userRepository;
    private final TeamRepository teamRepository;

    public Mono<SuccessResponseDTO> leaveCurrentTeam(Long id) {
        return userRepository.findById(id)
                .flatMap(this::handleRequestByMembership)
                .switchIfEmpty(Mono.error(new UserNotFoundException("User not found")));
    }

    private Mono<SuccessResponseDTO> handleRequestByMembership(User user) {
        if (Objects.isNull(user.getMembershipType())) {
            return Mono.error(new BadRequest("User doesn't belong to any team"));
        }
        if (user.getMembershipType().equals(TeamMembershipType.OWNER)) {
            return detachMembersFromOwnerTeamAndDeleteTeam(user.getTeamId())
                    .flatMap(res -> Mono.just(SuccessResponseDTO.builder()
                            .message("Team successfully disbanded")
                            .data(res)
                            .build()));
        }
        return setUserTeamInfoToNull(user)
                .flatMap(ignore -> Mono.just(SuccessResponseDTO.builder()
                        .message("User successfully left current team")
                        .build()));
    }

    private Mono<List<User>> detachMembersFromOwnerTeamAndDeleteTeam(final Long teamId) {
        return userRepository.findAllByTeamId(teamId)
                .flatMap(this::setUserTeamInfoToNull)
                .collectList()
                .flatMap(result -> teamRepository.deleteById(teamId)
                        .then(Mono.just(result)))
                .switchIfEmpty(Mono.error(new BadRequest("No users found within team")));
    }

    private Mono<User> setUserTeamInfoToNull(User user) {
        user.setTeamId(null);
        user.setMembershipType(null);
        return userRepository.save(user);
    }
}
