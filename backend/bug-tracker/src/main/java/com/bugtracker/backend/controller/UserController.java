package com.bugtracker.backend.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.bugtracker.backend.dto.CommentDto;
import com.bugtracker.backend.dto.UserStatsDto;
import com.bugtracker.backend.dto.UserToDeleteDto;
import com.bugtracker.backend.entity.Comment;
import com.bugtracker.backend.entity.Ticket;
import com.bugtracker.backend.entity.User;
import com.bugtracker.backend.security.JwtUtil;
import com.bugtracker.backend.service.*;
import com.bugtracker.backend.util.GlobalUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.jws.soap.SOAPBinding;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
@Slf4j
@RestController
@RequestMapping("api")
public class UserController {

    private static final String USER_NOT_FOUND_ERROR_MESSAGE = "Could not find the user with ID %d";

    @Autowired
//just basically means we don't have to initialize when calling other classes because spring will manage it for us (bean injection)
    private UserService userService;

    @Autowired
    private TicketService ticketService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private DataPopulationService dataPopulationService;

    @Autowired
    private ProjectUserRoleLinkService projectUserRoleLinkService;




    //we are basically creating a new token for the user using their refresh token which should be passed in the same way in the header
    @GetMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                String refresh_token = authorizationHeader.substring("Bearer ".length());//get refresh_token
                String username = JwtUtil.getUsername(refresh_token);//get refresh_token username
                User user = userService.getUserByName(username);//find user
                Date refreshExpires = JwtUtil.getExpiresAt(refresh_token);


                Collection<GrantedAuthority> authorities = new ArrayList<>();

                //instead of the role name we are passing the role access level and project id since that's what will determine user access rights to projects
                user.getProjectUserRoleLinks().forEach(link -> {
                    authorities.add(new SimpleGrantedAuthority(
                            link.getRole().getAccessLevel()+","+link.getProject().getId()
                    ));
                });


                String access_token = JwtUtil.createToken(user.getUserName(),request.getRequestURL().toString(),
                        authorities,new Date(System.currentTimeMillis() + 40 * 60 * 1000));


                response.setContentType(APPLICATION_JSON_VALUE);//set return media
                new ObjectMapper().writeValue(response.getOutputStream(), GlobalUtil.writeTokenAsJson(access_token,refresh_token,
                        JwtUtil.getExpiresAt(access_token),JwtUtil.getExpiresAt(refresh_token)));//output refresh token and new access token


            } catch (Exception ex) {
                log.error("Error logging in: {}", ex.getMessage());
                response.setHeader("error", ex.getMessage());
                new ObjectMapper().writeValue(response.getOutputStream(), GlobalUtil.writeErrorAsJson(ex.getMessage()));
            }
        } else {
            throw new RuntimeException("Refresh token is missing");
        }
    }

    /*
    * {
    "userName": "Demo",
    "userEmail": "Demo@gmail.com",
    "userPhoneNo": "0777777",
    "password": "123"
      }
    * */
    @PostMapping("/register")
    public ResponseEntity< User> registerUser(@RequestBody User user){
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/register").toUriString());
        return ResponseEntity.created(uri).body(userService.saveUser(user));
    }

    @GetMapping("/user/{username}")
    public ResponseEntity< User> getUser(@PathVariable String username){
        return ResponseEntity.ok().body(userService.getUserByName(username));
    }

    @PatchMapping("/user")
    public ResponseEntity< User> updateUser(@RequestBody User user){
        return ResponseEntity.accepted().body(userService.updateUser(user));
    }

    @PatchMapping("/user/resetPass")
    public ResponseEntity< User> resetPasswordUser(@RequestBody User user){
        return ResponseEntity.ok().body(userService.updateUserPassword(user.getUserName(),user.getPassword()));
    }

    //needs to be changed to pageable
    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers(@RequestParam int pageNo){
        return ResponseEntity.ok().body(userService.getUsers(pageNo));
    }

    @GetMapping("/user/{userId}/tickets")
    public ResponseEntity<List<Ticket>> getUserTickets(@PathVariable int userId, @RequestParam int pageNo){
        return ResponseEntity.ok().body(ticketService.getTicketsByUser(userId,pageNo));
    }
    @GetMapping("/user/{userId}/tickets/assigned")
    public ResponseEntity<List<Ticket>> getUserAssignedTickets(@PathVariable int userId, @RequestParam int pageNo){
        return ResponseEntity.ok().body(ticketService.getTicketsByAssignedUser(userId,pageNo));
    }

    @GetMapping("/user/{userId}/stats")
    public ResponseEntity<UserStatsDto> getUserStats(@PathVariable int userId){
        return ResponseEntity.ok().body(userService.getUserStats(userId));
    }


}
