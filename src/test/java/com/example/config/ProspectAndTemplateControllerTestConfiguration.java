package com.example.config;

import com.example.processmanagementtool.PBKDF2Encoder;
import com.example.processmanagementtool.configuration.ReactivePostgresConfig;
import com.example.processmanagementtool.domain.prospect.repository.ProspectRepository;
import com.example.processmanagementtool.domain.template.repository.TemplateRepository;
import com.example.processmanagementtool.domain.user.repository.UserRepository;
import com.example.processmanagementtool.prospect.ProspectService;
import com.example.processmanagementtool.security.service.AuthenticationUserService;
import com.example.processmanagementtool.template.TemplateService;
import com.example.processmanagementtool.user.UserAuthService;
import com.example.processmanagementtool.user.UserService;
import com.example.processmanagementtool.utlitiy.JWTUtil;
import com.example.processmanagementtool.web.TeamController;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.r2dbc.connection.init.CompositeDatabasePopulator;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;
import org.springframework.test.context.ActiveProfiles;

@Configuration
@DataR2dbcTest
@ActiveProfiles({"test"})
@Import(ReactivePostgresConfig.class)
public class ProspectAndTemplateControllerTestConfiguration {

    @Autowired
    private ProspectRepository prospectRepository;
    @Autowired
    private TemplateRepository templateRepository;
    @Autowired
    private UserRepository userRepository;

    @MockBean
    private UserService userService;

    @MockBean
    private TeamController teamController;

    @Bean
    public ProspectService prospectService() {
        return new ProspectService(prospectRepository, templateRepository, userRepository);
    }

    @Bean
    public TemplateService templateService() {
        return new TemplateService(templateRepository, userRepository);
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

    @Bean
    public ConnectionFactoryInitializer initializer(ConnectionFactory connectionFactory) {
        ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
        initializer.setConnectionFactory(connectionFactory);

        CompositeDatabasePopulator populator = new CompositeDatabasePopulator();
        populator.addPopulators(new ResourceDatabasePopulator(new ClassPathResource("init-sql/init.sql")));
        initializer.setDatabasePopulator(populator);

        return initializer;
    }


}
