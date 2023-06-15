package com.example.config;

import com.example.processmanagementtool.PBKDF2Encoder;
import com.example.processmanagementtool.domain.team.repository.TeamRepository;
import com.example.processmanagementtool.domain.user.repository.UserRepository;
import com.example.processmanagementtool.security.service.AuthenticationUserService;
import com.example.processmanagementtool.team.InviteTeamService;
import com.example.processmanagementtool.team.LeaveTeamService;
import com.example.processmanagementtool.user.UserAuthService;
import com.example.processmanagementtool.user.UserService;
import com.example.processmanagementtool.utlitiy.JWTUtil;
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
