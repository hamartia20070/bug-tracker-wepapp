package com.bugtracker.backend.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.bugtracker.backend.util.GlobalUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static java.util.Arrays.stream;
import static jdk.nashorn.internal.runtime.PropertyDescriptor.GET;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
//This filter will listen on to every request coming in to the api, this is where we will verify the token and if the user has authority for the request
public class CustomAuthorizationFilter extends OncePerRequestFilter {


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(request.getServletPath().equals("/api/login") || request.getServletPath().equals("/api/token/refresh")
             || request.getServletPath().equals("/api/register")  /* || request.getServletPath().equals("/api/users")*/){
            filterChain.doFilter(request,response);
        }//do nothing if it's the login path or refresh token path
        else {
            String authorizationHeader = request.getHeader(AUTHORIZATION);

            if(authorizationHeader!=null && authorizationHeader.startsWith("Bearer ")){
                try{
                    String token = authorizationHeader.substring("Bearer ".length());

                    String username = JwtUtil.getUsername(token);
                    String[] roles = JwtUtil.getRoles(token);


                    //I'm going to prevent users from accessing urls to projects they are not added to.
                    //What projects a user is added to is burned into their token so they need to get a new token
                    // every time they are added or updated to a project for the changes to get written onto the new token
                    CustomProjectAuthorization customProjectAuthenticationFilter = new CustomProjectAuthorization();

                    //For getting a project, user needs access level 1 or higher to project
                    //For updating a project, user needs access level 2 or higher

                    /*
                    * Paths Covered: /api/projects/\d{1,6} GET && PATCH
                    * */
                    if(!customProjectAuthenticationFilter.projectGettingAndUpdatingAuthorization(request,response,username,roles)){
                        return;
                    }


                    //If user wants to update their role or another user's role for a project, they need access level 3 or above to that project
                    // OR if user wants to remove a user from the project

                    /*
                     * Paths Covered: /api/projects/\d{1,6}/user*  PATCH && DELETE
                     * */
                    if(!customProjectAuthenticationFilter.projectUserAssignmentAuthorization(request,response,username,roles)){
                        return;
                    }

                    CustomTicketAuthorizationFilter customTicketAuthorizationFilter = new CustomTicketAuthorizationFilter();


                    //To view/create ticket, user needs to be assigned to project of ticket with access level 1 for reading and 2 for creating

                    /*
                     * Paths Covered: /api/tickets/project/\d{1,6}
                     *                /api/tickets/project/\d{1,6}/tag/\d{1,6} GET && POST && PATCH for both
                     * */
                    if(!customTicketAuthorizationFilter.ticketCreationAuthorization(request,response,username,roles)){
                        return;
                    }

                    //To edit/ add tag/ assign user to ticket, user needs to be the owner of the ticket

                    /*
                     * Paths Covered: /api/tickets/\d{1,6}\\*               PATCH
                     *                /api/tickets/\d{1,6}/assign/\d{1,6}   PATCH
                     *                /api/tickets/\\d{1,6}/tag/\\d{1,6}"   POST
                     * */
                    if(!customTicketAuthorizationFilter.ticketOwnershipAuthorization(request,response,username)){
                        return;
                    }

                    Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                    //we have to convert the roles into something that extends GrantedAuthority so we can handle authorization using it,
                    // SimpleGrantedAuthority is an easy option
                    stream(roles).forEach(role -> {
                        authorities.add(new SimpleGrantedAuthority(role));
                    });
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(username,null,authorities);
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    //basically just told spring that this is xy user and here are their xy authorities


                    filterChain.doFilter(request,response);

                }catch (Exception ex){
                    log.error("Error logging in: {}",ex.getMessage());
                    response.setHeader("error",ex.getMessage());

                    //send error message as JSON
                    response.setContentType(APPLICATION_JSON_VALUE);
                    new ObjectMapper().writeValue(response.getOutputStream(), GlobalUtil.writeErrorAsJson(ex.getMessage()));
                }
            }else{
                throw new RuntimeException("Token is missing");
            }
        }
    }
}


                    //CODE FROM THE BODY I MAY NEED LATER

                    /*Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                    //we have to convert the roles into something that extends GrantedAuthority so we can handle authorization using it,
                    // SimpleGrantedAuthority is an easy option
                    stream(roles).forEach(role -> {
                        authorities.add(new SimpleGrantedAuthority(role));
                    });
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(username,null,authorities);
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);*/
                    //basically just told spring that this is xy user and here are their xy authorities