package com.bugtracker.backend.service;

import com.bugtracker.backend.dto.UserStatsDto;
import com.bugtracker.backend.entity.User;
import com.bugtracker.backend.repository.UserRepository;
import com.bugtracker.backend.util.GlobalUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Slf4j //just logs what the service is doing by calling log.info etc
@RequiredArgsConstructor //manages injecting beans
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUserName(username);
        if(user == null){
            log.error("User Not Found In Database");
            throw new UsernameNotFoundException("User not found in database");
        }else {
            log.info("User found in database: {}",username);
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();

        //instead of the role name we are passing the role access level and project id since that's what will determine user access rights to projects
        user.getProjectUserRoleLinks().forEach(link -> {
            authorities.add(new SimpleGrantedAuthority(
                    link.getRole().getAccessLevel()+","+link.getProject().getId()
            ));
        });

        return new org.springframework.security.core.userdetails.User(user.getUserName(),user.getPassword(),authorities);
    }

    public User saveUser(User user) {
        log.info("Saving new user: {} to database",user.getUserName());
        log.info("New user password: {}", user.getPassword());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public List<User> saveUsers(List<User> users) {
        return userRepository.saveAll(users);
    }

    public List<User> getUsers(int pageNo) {
        Pageable page = PageRequest.of(pageNo, GlobalUtil.itemsPerPage);
        return userRepository.getAllUsers(page);
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(int id) {
        return userRepository.findById(id);
    }

    public User getUserByName(String name) {
        return userRepository.findByUserName(name);
    }

    public String deleteUser(int id) {
        userRepository.deleteById(id);
        return "User " + id + " deleted";
    }

    public User updateUser(User newUser) {
        User existingUser = userRepository.findById(newUser.getId()).orElse(null);
        existingUser.setUserName(newUser.getUserName());
        existingUser.setUserEmail(newUser.getUserEmail());
        existingUser.setUserPhoneNo(newUser.getUserPhoneNo());

        return userRepository.save(existingUser);
    }

    public User updateUserPassword(String username, String password) {
        User existingUser = userRepository.findByUserName(username);
        existingUser.setPassword(passwordEncoder.encode(password));
        return userRepository.save(existingUser);
    }


    public UserStatsDto getUserStats(int userId){
        return new UserStatsDto(
                userRepository.getUserCreatedTicketsCount(userId),
                userRepository.getUserAssignedTicketsCount(userId),
                userRepository.getUserProjectsCount(userId),
                userRepository.getUserCommentsCount(userId)
        );
    }



}
