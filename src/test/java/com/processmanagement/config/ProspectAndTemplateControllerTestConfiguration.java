package com.processmanagement.config;

import com.processmanagement.PBKDF2Encoder;
import com.processmanagement.configuration.ReactivePostgresConfig;
import com.processmanagement.domain.prospect.repository.ProspectRepository;
import com.processmanagement.domain.template.repository.TemplateRepository;
import com.processmanagement.domain.user.repository.UserRepository;
import com.processmanagement.prospect.ProspectService;
import com.processmanagement.security.service.AuthenticationUserService;
import com.processmanagement.template.TemplateService;
import com.processmanagement.user.UserAuthService;
import com.processmanagement.user.UserService;
import com.processmanagement.utlitiy.JWTUtil;
import com.processmanagement.web.TeamController;
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
