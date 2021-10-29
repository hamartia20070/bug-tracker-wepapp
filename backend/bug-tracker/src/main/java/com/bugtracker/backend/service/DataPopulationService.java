package com.bugtracker.backend.service;


import com.bugtracker.backend.util.TestData;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Log
public class DataPopulationService {


    @Autowired
    private CommentService commentService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private PriorityService priorityService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private TagService tagService;
    @Autowired
    private TicketService ticketService;
    @Autowired
    private UserService userService;
    @Autowired
    private TicketHistoryService ticketHistoryService;

    public DataPopulationService() {
    }


    public void populateComment(){
        commentService.addAllComment(TestData.generateComment());
    }

    public void populatePriority(){
        priorityService.addAllPriorities(TestData.generatePriority());
    }

    public void populateProject(){
        projectService.addAllProjects(TestData.generateProjects(priorityService.getAllPriorities()));
    }

    public void populateRoles(){
        roleService.addAllRoles(TestData.generateRole());
    }

    public void populateTags(){
        tagService.addAllTags(TestData.generateTag());
    }

    public void populateTickets(){
        ticketService.addAllTickets(TestData.generateTickets(userService.getUsers(),projectService.getProjects(),priorityService.getAllPriorities()));
    }

    public void populateUser(){
        userService.saveUsers(TestData.generateUser());
    }

    public void populateTicketHistory(){
        ticketHistoryService.saveTicketHistories(TestData.generateTicketHistory(ticketService.getAllTickets()));
    }

}
