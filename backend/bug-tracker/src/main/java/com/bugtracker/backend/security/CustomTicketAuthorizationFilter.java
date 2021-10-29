package com.bugtracker.backend.security;

import com.bugtracker.backend.service.TicketService;
import com.bugtracker.backend.service.UserService;
import com.bugtracker.backend.util.BeanUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@Service
public class CustomTicketAuthorizationFilter {


    //For getting tickets of project, user needs access level 1 or higher to project
    public void ticketAccessToProjectAuthorization(HttpServletRequest request, HttpServletResponse response, String username, String[] roles) throws IOException {
        String path = request.getRequestURI().substring(request.getContextPath().length());
        if (request.getMethod().equals("GET") && path.matches("/api/tickets/project/\\d{1,6}")) {
            String projectId = path.substring("/api/tickets/project/".length());
            log.info("Project ID: {}", projectId);

            if (checkIfUserIsAssignedToProjectWithAccesslevel(projectId, roles, 1)) {
                log.info("ACCESS ALLOWED TO: {} FOR USER: {} WITH METHOD: {}", path, username, request.getMethod());

            } else {//return a JSON telling the client what went wrong
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(),
                        writeErrorAsJson("User has no access to tickets of project with ID: " + projectId));
            }


        }
    }

    //Reading and Creating Ticket authorization method reading requires access level 1 to project of ticket, creating requires access level 2 to project of ticket
    public boolean ticketCreationAuthorization(HttpServletRequest request, HttpServletResponse response, String username, String[] roles) throws IOException {
        String path = request.getRequestURI().substring(request.getContextPath().length());

        if (path.matches("/api/tickets/project/\\d{1,6}")
        || path.matches("/api/tickets/project/\\d{1,6}/tag/\\d{1,6}")) {
            log.info("PATH RECOGNISED by ticketCreationAuthorization() method path: {}", path);
            int accessLevel = 1;
            if (request.getMethod().equals("POST") || request.getMethod().equals("PATCH")) {
                accessLevel = 2;
            }
            String projectId = "";
            try {//there are two possible paths to catch here: http://localhost:8080/api/tickets/project/3?pageNo=0
                projectId = path.substring("/api/tickets/project/".length(), "/api/tickets/project/".length() + 6).replaceAll("\\D+", "");
            } catch (Exception e) {
                projectId = path.substring("/api/tickets/project/".length());
            }
            log.info("Project ID: {}", projectId);

            if (checkIfUserIsAssignedToProjectWithAccesslevel(projectId, roles, accessLevel)) {
                log.info("ACCESS ALLOWED TO: {} FOR USER: {} WITH METHOD: {}", path, username, request.getMethod());

            } else {//return a JSON telling the client what went wrong
                response.setContentType(APPLICATION_JSON_VALUE);
                response.sendError(1);
                new ObjectMapper().writeValue(response.getOutputStream(),
                        writeErrorAsJson("User has no access to creating/reading tickets for project with ID: " + projectId));
                return false;
            }
        }
        return true;
    }

    //Ticket ownership verification method
    //Editing/Assigning user && Adding tag to ticket can only be done by the ticket creator
    public boolean ticketOwnershipAuthorization(HttpServletRequest request, HttpServletResponse response, String username) throws IOException {
        String path = request.getRequestURI().substring(request.getContextPath().length());

        if (request.getMethod().equals("PATCH") && path.matches("/api/tickets/\\d{1,6}\\\\*")
                || request.getMethod().equals("PATCH") && path.matches("/api/tickets/\\d{1,6}/assign/\\d{1,6}")
                || request.getMethod().equals("POST") && path.matches("/api/tickets/\\d{1,6}/tag/\\d{1,6}")) {
            log.info("PATH RECOGNISED by ticketOwnershipAuthorization() method path: {}", path);
            String ticketId = "";
            try {//there are two possible paths to catch here: http://localhost:8080/api/tickets/project/3?pageNo=0
                ticketId = path.substring("/api/tickets/".length(), "/api/tickets/".length() + 6).replaceAll("\\D+", "");
            } catch (Exception e) {
                ticketId = path.substring("/api/tickets/".length());
            }
            log.info("ticket ID : {}", ticketId);

            TicketService ticketService = BeanUtil.getBean(TicketService.class);
            UserService userService = BeanUtil.getBean(UserService.class);

            log.info("Ticket's owner name: {}",ticketService.getTicketById(1).getSubmitUser().getUserName());

            if (ticketService.getTicketById(Integer.parseInt(ticketId)).getSubmitUser().getUserName().equals(userService.getUserByName(username).getUserName())) {
                log.info("ACCESS ALLOWED TO: {} FOR USER: {} WITH METHOD: {}", path, username, request.getMethod());
            } else {
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(),
                        writeErrorAsJson("User is not the owner of ticket with ID: " + ticketId));
                return false;
            }

        }
        return true;
    }


    //REDUNDANT
    private boolean checkIfUserIsAssignedToProjectWithAccesslevel(String projectId, String[] userRoles, int accessLevel) {

        for (String role : userRoles) {


            String[] userAccessLevelAndProjectId = role.split(",");//the roles we set into the jwt are the user's access level as a digit
            // and the project's id separated by a ","

            //check if user is assigned to the requested project
            if (userAccessLevelAndProjectId[1].matches(projectId)
                    && Integer.parseInt(userAccessLevelAndProjectId[0]) >= accessLevel) {
                return true;
            }
        }
        return false;
    }

    private Map<String, String> writeErrorAsJson(String errorMessage) {
        Map<String, String> error = new HashMap<>();
        error.put("error_message", errorMessage);
        return error;
    }
}
