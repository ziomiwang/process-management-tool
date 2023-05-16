package com.example.processmanagementtool.repository;

import com.example.processmanagementtool.dto.UserDTO;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;

@Component
public class UserRepository {

    Map<Integer, UserDTO> database = new HashMap<>();

    public Mono<UserDTO> saveUser(UserDTO userDTO) {
        database.put(userDTO.getId(), userDTO);
        System.out.println("db output: " + database);
        return Mono.just(userDTO);
    }

    public Mono<UserDTO> getUser(Integer id) {
        return Mono.just(database.get(id));
    }

    public Flux<UserDTO> getAllUser() {
        return Flux.fromIterable(database.values());
    }

    public Flux<UserDTO> getAllUsersWithFilteredAge(Integer minAge, Integer maxAge) {
        List<UserDTO> users = new ArrayList<>(database.values());
        List<UserDTO> filteredUsers = users.stream()
                .filter(user -> user.getAge() >= minAge && user.getAge() <= maxAge)
                .toList();

        return Flux.fromIterable(filteredUsers);
    }

    public Mono<UserDTO> deleteUser(Integer id) {
        return Mono.just(database.remove(id));
    }

    public Mono<UserDTO> updateUser(Integer id, UserDTO updateData) {
        UserDTO updatedUser = database.computeIfPresent(id, (k, v) -> {
            v.setAge(updateData.getAge());
            v.setName(updateData.getName());
            return v;
        });

        return Objects.nonNull(updatedUser) ? Mono.just(updatedUser) : Mono.error(new RuntimeException("not found"));
    }
}
