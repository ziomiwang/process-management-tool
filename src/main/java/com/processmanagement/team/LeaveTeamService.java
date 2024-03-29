package com.processmanagement.team;

import com.example.processmanagementtool.dto.SuccessResponseDTO;
import com.processmanagement.commons.ResponseHelper;
import com.processmanagement.domain.team.repository.TeamRepository;
import com.processmanagement.domain.user.TeamMembershipType;
import com.processmanagement.domain.user.User;
import com.processmanagement.domain.user.repository.UserRepository;
import com.processmanagement.exception.customexceptions.BadRequest;
import com.processmanagement.exception.customexceptions.UserNotFoundException;
import com.processmanagement.user.UserDTOMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class LeaveTeamService {

    private final UserRepository userRepository;
    private final TeamRepository teamRepository;

    public Mono<SuccessResponseDTO> leaveCurrentTeam(Principal principal) {
        return userRepository.findUserByLogin(principal.getName())
                .flatMap(this::handleRequestByMembership)
                .switchIfEmpty(Mono.error(new UserNotFoundException()));
    }

    private Mono<SuccessResponseDTO> handleRequestByMembership(User user) {
        if (Objects.isNull(user.getMembershipType())) {
            return Mono.error(new BadRequest("User doesn't belong to any team"));
        }
        if (user.getMembershipType().equals(TeamMembershipType.OWNER)) {
            return detachMembersFromOwnerTeamAndDeleteTeam(user.getTeamId())
                    .flatMap(res -> ResponseHelper.buildSuccessResponse("Team successfully disbanded", UserDTOMapper.mapUserToSimpleUser(res)));
        }
        return setUserTeamInfoToNull(user)
                .flatMap(ignore -> ResponseHelper.buildSuccessResponse("User successfully left current team"));
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
