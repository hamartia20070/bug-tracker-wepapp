package com.bugtracker.backend.controller;

import com.bugtracker.backend.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/populate")
public class PopulateController {
    @Autowired
    private UserService userService;

    @Autowired
    private TicketService ticketService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private DataPopulationService dataPopulationService;

    @Autowired
    private ProjectUserRoleLinkService projectUserRoleLinkService;

    @GetMapping
    public void test() {
        dataPopulationService.populateComment();
        dataPopulationService.populateUser();
        dataPopulationService.populatePriority();
        dataPopulationService.populateRoles();
        dataPopulationService.populateTags();
        dataPopulationService.populateProject();
        dataPopulationService.populateTickets();
        dataPopulationService.populateTicketHistory();
    }

    @GetMapping("/tag")
    public void test2() {
        ticketService.addTagToTicket(1, 1);
    }

    @GetMapping("/link")
    public void test3() {
        projectUserRoleLinkService.addUserToProject(1, 1, 1);
    }

    @GetMapping("/link2")
    public void test4() {
        projectUserRoleLinkService.updateUserRoleInProject(1, 1, 5);
    }
}
