package com.processmanagement.config;

import com.processmanagement.PBKDF2Encoder;
import com.processmanagement.domain.team.repository.TeamRepository;
import com.processmanagement.domain.user.repository.UserRepository;
import com.processmanagement.security.service.AuthenticationUserService;
import com.processmanagement.team.InviteTeamService;
import com.processmanagement.team.LeaveTeamService;
import com.processmanagement.user.UserAuthService;
import com.processmanagement.user.UserService;
import com.processmanagement.utlitiy.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;

@Configuration
@DataR2dbcTest
@ActiveProfiles({"test"})
public class TeamControllerTestConfiguration {

    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private UserRepository userRepository;

    @MockBean
    private UserService userService;

    @Bean
    public InviteTeamService inviteTeamService() {
        return new InviteTeamService(userRepository, teamRepository);
    }

    @Bean
    public LeaveTeamService leaveTeamService() {
        return new LeaveTeamService(userRepository, teamRepository);
    }

    @Bean
    public UserAuthService userAuthService() {
        return new UserAuthService(jwtUtil(), pbkdf2Encoder(), authenticationUserService(), userRepository);
    }

    @Bean
    public AuthenticationUserService authenticationUserService() {
        return new AuthenticationUserService(userRepository);
    }

    @Bean
    public JWTUtil jwtUtil() {
        return new JWTUtil();
    }

    @Bean
    public PBKDF2Encoder pbkdf2Encoder() {
        return new PBKDF2Encoder();
    }

}
