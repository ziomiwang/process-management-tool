package com.example.config;

import com.example.processmanagementtool.repository.UserRepository;
import com.example.processmanagementtool.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;

@Configuration
@DataR2dbcTest
@ActiveProfiles({"test"})
public class WebfluxConfigurationTest {

    @Autowired
    private UserRepository userRepository;

    @Bean
    public UserService userService(){
        return new UserService(userRepository);
    }
}
