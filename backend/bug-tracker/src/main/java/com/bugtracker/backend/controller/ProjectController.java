package com.bugtracker.backend.controller;

import com.bugtracker.backend.dto.PriorityStatsDto;
import com.bugtracker.backend.dto.UserTicketStatsDto;
import com.bugtracker.backend.entity.Project;
import com.bugtracker.backend.entity.ProjectUserRoleLink;
import com.bugtracker.backend.entity.User;
import com.bugtracker.backend.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("api/projects")
@Slf4j
public class ProjectController {

    private static final String PROJECT_NOT_FOUND_ERROR_MESSAGE  = "Could not find Project with ID %d";
    private static final String PRIORITY_NOT_FOUND_ERROR_MESSAGE  = "Could not find Priority with ID %d";
    @Autowired
    private ProjectService projectService;
    @Autowired
    private ProjectUserRoleLinkService projectUserRoleLinkService;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private TicketService ticketService;

    @GetMapping()
    public ResponseEntity<List<Project>> getAllProjects(@RequestParam int pageNo,@RequestParam String sortBy,
                                                        @RequestParam(required = false) Integer userId,@RequestParam(required = false) String status,
                                                        @RequestParam(required = false) Integer priority){
        switch (sortBy) {
            case "created":
                if(userId!=null){
                    return ResponseEntity.ok().body(projectService.getProjectsByUser(userId,pageNo, "project.dateStarted",priority));
                }else if(!Objects.equals(status, "")){
                    boolean state = status.equals("open");
                    return ResponseEntity.ok().body(projectService.getProjectsByStatus(state,pageNo, "dateStarted",priority));
                }else{
                    return ResponseEntity.ok().body(projectService.getProjects(pageNo, "dateStarted",priority));
                }
            case "priority":
                if(userId!=null){
                    return ResponseEntity.ok().body(projectService.getProjectsByUser(userId,pageNo, "project.projectPriority.importance",priority));
                }else if(!Objects.equals(status, "")){
                    boolean state = status.equals("open");
                    return ResponseEntity.ok().body(projectService.getProjectsByStatus(state,pageNo, "projectPriority.importance",priority));
                }else{
                    return ResponseEntity.ok().body(projectService.getProjects(pageNo, "projectPriority.importance",priority));
                }
            case "activity":
                //return projects in order of latest created ticket
                return ResponseEntity.ok().body(projectService.getProjectsByLastActive(pageNo));
            default:
                return ResponseEntity.badRequest().body(null);
        }

    }

    //TODO: ResponseEntity is a cleaner way of returning data, should replace everything with it
    //TODO URI BUILDING LOOK IT UP
    //TODO: DTOs might be better
    //TODO: Log.info everything
    //TODO: A map of how to input data for posting above each post method would be nice
    //TODO: [DONE] User who creates a project should be assigned as owner to the project
    //TODO: [DONE] redo user populate with fix passwords (its 123 for all)
    //TODO: [DONE] User register path
    //TODO: [DONE] finish authorization filter for tickets
    //TODO: [DONE] get user assigned tickets, get user made tickets
    //TODO: [DONE] make gets return pages, not all
    //TODO: [DONE] User controller finish
    //TODO: Service all should return optional with Option.ofNullable()
    //TODO: models : custom equals method with Objects.equals(this object, that.object)
    //TODO: empty and other logic checks in service


    public ResponseEntity<List<Project>> getAllProject(){
        return ResponseEntity.ok().body(projectService.getProjects());
    }

    //Tested
    //Only users assigned to the project can see
    @GetMapping("/{projectId}")
    public Project getProjectById(@PathVariable int projectId){
        return projectService.getProjectById(projectId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        String.format(PROJECT_NOT_FOUND_ERROR_MESSAGE, projectId))
        );
    }
    @GetMapping("/{projectId}/stats/{userId}")
    public int[] getProjectStats(@PathVariable int projectId,@PathVariable int userId){
        return projectService.getNoOfActiveAndClosedAndAssignedTicketsOfProject(projectId,userId);
    }

    //Tested
    //TODO: delete this
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Project>> getProjectsByUser(@PathVariable int userId, @RequestParam int pageNo,@RequestParam String sortBy){
        //return projectService.getProjectsByUser(userId,pageNo);

        return ResponseEntity.badRequest().body(null);

    }

    //Tested
    @GetMapping("/user/{userId}/role/{roleId}")
    public List<Project> getProjectsByUserRole(@PathVariable int userId,@PathVariable int roleId,@RequestParam int pageNo){
        return projectService.getByUserAndRole(userId,roleId,pageNo);
    }

    //Get project by priority or by priority and user
    //Tested
    @GetMapping({"/priority/{priorityId}","/priority/{priorityId}/user/{userId}"})
    public List<Project> getProjectsByPriority(@PathVariable int priorityId,@PathVariable(required = false) Integer userId,@RequestParam int pageNo){
        if(userId!=null){
            return projectService.getByUserAndPriority(userId,priorityId,pageNo);
        }
        return projectService.getByPriority(priorityId,pageNo);
    }

    //Tested
    //TODO: This needs restriction, allowed for now
    @PostMapping("/{projectId}/user")
    @ResponseStatus(HttpStatus.CREATED)
    public ProjectUserRoleLink assignUserToProject(@PathVariable int projectId , @RequestParam int userId, @RequestParam int roleId){
        //will have to check if user is already assigned
        try {
            if(projectService.getProjectByUser(userId,projectId)==null){
                return projectUserRoleLinkService.addUserToProject(userId,projectId,roleId).orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                String.format(PROJECT_NOT_FOUND_ERROR_MESSAGE, projectId))
                );
            }
        }catch(Exception e){
            System.out.println("Exception : "+e.toString());
        }
        return updateUserRoleToProject(projectId,userId,roleId);
    }

    //Tested
    //Needs access level 3 to project DONE
    @PatchMapping("/{projectId}/user")
    @ResponseStatus(HttpStatus.OK)
    public ProjectUserRoleLink updateUserRoleToProject(@PathVariable int projectId ,@RequestParam int userId,@RequestParam int roleId){
        return projectUserRoleLinkService.updateUserRoleInProject(projectId,userId,roleId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        String.format(PROJECT_NOT_FOUND_ERROR_MESSAGE, projectId))
        );
    }

    //Tested
    //Needs access level 3 DONE
    @DeleteMapping("/{projectId}/user")
    public void removeUserFromProject(@PathVariable int projectId ,@RequestParam int userId){
        projectUserRoleLinkService.removeUserFromProject(projectId,userId);
    }

    //Tested
    //Instantly assign creating user as well DONE
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public Project addProject(@RequestBody Project project,@RequestParam int priorityId, @RequestParam int userId,@RequestParam int roleId){
        Project newProject = projectService.saveProject(project,priorityId).orElse(null);

        assignUserToProject(newProject.getId(),userId,roleId);

        return newProject;
    }

    //Tested
    //Needs access level 2 DONE
    @PatchMapping("/{projectId}")
    @ResponseStatus(HttpStatus.OK)
    public Project updateProject(@RequestBody Project newProject, @RequestParam int priorityId, @PathVariable int projectId, HttpServletRequest request){

       return projectService.updateProject(newProject,projectId,priorityId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        String.format(PROJECT_NOT_FOUND_ERROR_MESSAGE, projectId))
        );
    }

    //TODO: No deletes for now
    //FAILS if project has tickets or users assigned to it
    @DeleteMapping("/{projectId}")
    public void deleteProject(@PathVariable int projectId){
        projectService.deleteProject(projectId);
    }

    //Tested
    @GetMapping("/{projectId}/users")
    public List<User> getUsersOfProject(@PathVariable int projectId, @RequestParam int pageNo){
        return projectUserRoleLinkService.findAllUserByProject(projectId,pageNo);
    }

    @GetMapping("/{projectId}/users/count")
    public int getNoOfUsersOfProject(@PathVariable int projectId){
        return projectService.getNoOfUsersOfProject(projectId);
    }

    @GetMapping("/{projectId}/users/{userId}/stats")
    public UserTicketStatsDto getUserStatsOfProject(@PathVariable int projectId,@PathVariable int userId){
        return ticketService.getUserStatsForProject(projectId,userId);
    }

    @GetMapping("/{projectId}/users/{userId}/link")
    public ProjectUserRoleLink getUserProjectLink(@PathVariable int projectId,@PathVariable int userId){
        return projectUserRoleLinkService.getLinkProjectAndUser(projectId,userId);
    }

    @GetMapping("/priority/count")
    public ResponseEntity<List<PriorityStatsDto>> getTicketsCountByPriority(){
        return ResponseEntity.ok().body(projectService.getProjectsCountByPriority());
    }
    @GetMapping("/tickets/count")
    public ResponseEntity<List<PriorityStatsDto>> getActiveTicketsCountForAllProjects(){
        return ResponseEntity.ok().body(projectService.getActiveTicketsCountForAllProjects());
    }

    @GetMapping("/count")
    public ResponseEntity<Integer> getProjectsCount(){
        return ResponseEntity.ok().body(projectService.getProjectsCount());
    }








}
