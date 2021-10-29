package com.bugtracker.repository;

import com.bugtracker.backend.entity.User;
import com.bugtracker.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.*;
import java.util.ArrayList;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@Tag("integration-test")
@DataJpaTest
@ActiveProfiles("test")
public class UserTest {
    @Autowired private UserRepository userRepository;

    @BeforeEach
    void setUp(){userRepository.deleteAll();}

    @Test
    void canFindAll(){
        //given
        User user1 = new User("Geri","ok@gmail","077","123");
        User user2 = new User("Geri1","ok@gmail1","0771","123");
        List<User> list = new ArrayList<>();
        list.add(user1); list.add(user2);

        //when
        List<User> users= userRepository.findAll();

        //then
        assertThat(users).hasSize(2);

    }

}
