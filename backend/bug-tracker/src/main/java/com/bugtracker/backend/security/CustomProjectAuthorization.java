package com.bugtracker.backend.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
public final class CustomProjectAuthorization {


    //If user wants to update their role or another user's role for a project, they need access level 3 or above to that project
    // OR if user wants to remove a user from the project
    public boolean projectUserAssignmentAuthorization(HttpServletRequest request, HttpServletResponse response, String username, String[] roles) throws IOException{
        String path = request.getRequestURI().substring(request.getContextPath().length());
        if(request.getMethod().equals("PATCH") && path.matches("/api/projects/\\d{1,6}/user*")
                || request.getMethod().equals("DELETE") && path.matches("/api/projects/\\d{1,6}/user*")){

            String projectId = path.substring("/api/projects/".length(),"/api/projects/".length()+6).replaceAll("\\D+","");//extract only the projectId from url
            log.info("Request from url: {} with method: {} for project: {}",path,request.getMethod(),projectId);
            if(checkIfUserIsAssignedToProjectWithAccesslevel(projectId,roles,3)){
                log.info("UPDATE ALLOWED TO: {} FOR USER: {} WITH METHOD: {}",path,username,request.getMethod());
            }else {
                new ObjectMapper().writeValue(response.getOutputStream(),
                        writeErrorAsJson("User has no access or high enough access level to project with ID: "+projectId
                                +" Access level needed: "+3));
                return false;
            }

        }
        return true;
    }


    //For getting a project, user needs access level 1 or higher to project
    //For updating a project, user needs access level 2 or higher
    public boolean projectGettingAndUpdatingAuthorization(HttpServletRequest request, HttpServletResponse response, String username, String[] roles) throws IOException {
        String path = request.getRequestURI().substring(request.getContextPath().length());

        if(path.matches("/api/projects/\\d{1,6}")){

            log.info("Request from url: {} with method: {}",path,request.getMethod());
            String projectId = path.substring("/api/projects/".length());
            log.info("Project ID: {}",projectId);
            if(request.getMethod().equals("GET")){
                if(checkIfUserIsAssignedToProjectWithAccesslevel(projectId,roles,1)){
                    log.info("ACCESS ALLOWED TO: {} FOR USER: {} WITH METHOD: {}",path,username,request.getMethod());

                }else{//return a JSON telling the client what went wrong
                    response.setContentType(APPLICATION_JSON_VALUE);
                    new ObjectMapper().writeValue(response.getOutputStream(),
                            writeErrorAsJson("User has no access to project with ID: "+projectId));
                    return false;
                }

            }else if(request.getMethod().equals("PATCH")){
                if(checkIfUserIsAssignedToProjectWithAccesslevel(projectId,roles,2)){
                    log.info("ACCESS ALLOWED TO: {} FOR USER: {} WITH METHOD: {}",path,username,request.getMethod());

                }else{//return a JSON telling the client what went wrong
                    response.setContentType(APPLICATION_JSON_VALUE);
                    new ObjectMapper().writeValue(response.getOutputStream(),
                            writeErrorAsJson("User has no right to update project with ID: "+projectId));
                    return false;
                }
            }
        }
        return true;
    }

    public boolean checkIfUserIsAssignedToProjectWithAccesslevel(String projectId, String[] userRoles, int accessLevel){

        for(String role:userRoles){


            String[] userAccessLevelAndProjectId = role.split(",");//the roles we set into the jwt are the user's access level as a digit
            // and the project's id separated by a ","

            //check if user is assigned to the requested project
            if(userAccessLevelAndProjectId[1].matches(projectId)
                    && Integer.parseInt(userAccessLevelAndProjectId[0])>=accessLevel){
                return true;
            }
        }
        return false;
    }

    public Map<String,String> writeErrorAsJson(String errorMessage){
        Map<String,String> error = new HashMap<>();
        error.put("error_message",errorMessage);
        return error;
    }
}
