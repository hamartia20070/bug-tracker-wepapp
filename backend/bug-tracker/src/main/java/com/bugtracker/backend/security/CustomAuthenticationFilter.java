package com.bugtracker.backend.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    public CustomAuthenticationFilter(AuthenticationManager authenticationManager){
        this.authenticationManager = authenticationManager;
    }

    @Override//We are basically grabbing an incoming http request from the controller and getting two parameter values from the request
    // which should have the username and password in it
    // just pass in user credentials and the rest is done by spring sec
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        log.info("Username is: {}",username);log.info("Password is : {}",password);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,password);
        return authenticationManager.authenticate(authenticationToken);
    }

    @Override//this method is called when a login is successful, we will use this to pass the jwt token to the client
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        User user = (User)authentication.getPrincipal();

        //Auth0 algorithm for generating the token "secret" is basically the key, this would usually be saved somewhere secure, encrypted and only passed in at runtime
        String access_token = JwtUtil.createToken(user.getUsername(),request.getRequestURL().toString(),
                user.getAuthorities(),new Date(System.currentTimeMillis() + 1 * 60 * 1000));


        //create refresh token to get new token instead of having to log in
        String refresh_token = JwtUtil.createToken(user.getUsername(),request.getRequestURL().toString(),
                null,new Date(System.currentTimeMillis() + 30L * 24L * 60L * 60L * 1000L)/*30 days*/);


        //output new tokens as json
        //redundant, use Global util
        String pattern = "MM/dd/yyyy HH:mm:ss";
        DateFormat df = new SimpleDateFormat(pattern);
        Map<String,String> tokens = new HashMap<>();
        tokens.put("access_token",access_token);
        tokens.put("access_token_expires",df.format(JwtUtil.getExpiresAt(access_token)));
        tokens.put("refresh_token",refresh_token);
        tokens.put("refresh_token_expires",df.format(JwtUtil.getExpiresAt(refresh_token)));
        tokens.put("username",user.getUsername());
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(),tokens);

    }

}

