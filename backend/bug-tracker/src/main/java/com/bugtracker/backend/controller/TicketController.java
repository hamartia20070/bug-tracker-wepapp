package com.bugtracker.backend.controller;

import com.bugtracker.backend.dto.PriorityStatsDto;
import com.bugtracker.backend.entity.Comment;
import com.bugtracker.backend.entity.Ticket;
import com.bugtracker.backend.entity.TicketHistory;
import com.bugtracker.backend.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("api/tickets")
public class TicketController {

    @Autowired
    private TagService tagService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private TicketHistoryService ticketHistoryService;
    @Autowired
    private PriorityService priorityService;
    @Autowired
    private TicketService ticketService;
    @Autowired
    private CommentService commentService;



    //tested
    @GetMapping("/{ticketId}")
    public Ticket getTicketById(@PathVariable int ticketId){
        return ticketService.getTicketById(ticketId);
    }

    //tested
    //user needs access to project [DONE]
    @GetMapping({"/project/{projectId}","/project/{projectId}/priority/{priorityId}"})
    public List<Ticket> getTicketsByPriorityAndProject(@PathVariable int projectId, @PathVariable(required = false) Integer priorityId,@RequestParam int pageNo){
        if(priorityId!=null){
            return ticketService.getTicketByProjectAndPriority(projectId,priorityId,pageNo);
        }
        return ticketService.getTicketByProject(projectId,pageNo);
    }

    //gets only the tickets for projects that have the given user assigned to them
    //TODO: should check if token username matches passed ID on auth filter, basically only allowing clients to pass their own ID
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Ticket>> getTicketsForProjectsWithUser(@PathVariable int userId, @RequestParam int pageNo,
                                                                      @RequestParam String sortBy,@RequestParam String getBy,
                                                                      @RequestParam(required = false) Integer tagId){
        switch (sortBy) {
            case "created":
                return ResponseEntity.ok().body(ticketService.getTicketsByProjectWithUser(userId,pageNo,"ticketCreated",getBy,tagId));
            case "priority":
                return ResponseEntity.ok().body(ticketService.getTicketsByProjectWithUser(userId,pageNo,"ticketPriority.importance",getBy,tagId));
            case "deadline":
                return ResponseEntity.ok().body(ticketService.getTicketsByProjectWithUser(userId,pageNo,"deadline",getBy,tagId));
            default:
                return ResponseEntity.badRequest().body(null);
        }


    }

    //tested sample input: {
    //    "ticketTitle": "This is bugged",
    //    "ticketDesc": "Sample desc",
    //    "ticketCreated": "2021-10-04T23:28:40.632+00:00",
    //    "deadline": "2021-10-04T23:28:40.632+00:00",
    //    "status": "CLOSED"
    //}
    //user needs level 2 to project [DONE]
    @PostMapping("/project/{projectId}")
    @ResponseStatus(HttpStatus.CREATED)
    public Ticket addTicket(@RequestBody Ticket ticket,@PathVariable int projectId, @RequestParam int userId,@RequestParam int priorityId){
        Ticket newTicket = ticketService.saveTicket(ticket);
        return ticketService.assignTicket(newTicket,projectId,userId,priorityId);
    }

    //tested
    //user needs to have created the ticket [DONE]
    //link as : http://localhost:8080/api/tickets/1?priorityId=2
    @PatchMapping("/{ticketId}")
    @ResponseStatus(HttpStatus.OK)
    public Ticket updateTicket(@RequestBody Ticket newTicket, @PathVariable int ticketId,@RequestParam int priorityId){
        return ticketService.updateTicket(newTicket,ticketId, priorityId);
    }

    //Tested
    //needs to be owner of ticket [DONE]
    @PatchMapping("/{ticketId}/assign/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public Ticket assignTicket(@PathVariable int ticketId, @PathVariable Integer userId){

        return ticketService.assignTicket(ticketId,userId);

    }



    //---------------------------TAGS--------------------------------------------------------

    //tested
    //needs to have access to project [DONE]
    @GetMapping("/project/{projectId}/tag/{tagId}")
    public List<Ticket> getTicketByTagAndProject(@PathVariable int tagId, @PathVariable int projectId, @RequestParam int pageNo){
        return ticketService.getTicketByTagAndProject(tagId, projectId, pageNo);
    }

    //tested
    //need to be owner of ticket [DONE]
    @PostMapping("/{ticketId}/tag/{tagId}")
    public Optional<Ticket> addTagToTicket(@PathVariable int ticketId,@PathVariable int tagId){
        return ticketService.addTagToTicket(tagId,ticketId);

        /*try{
            return ticketService.addTagToTicket(tagId,ticketId);
        }catch (SQLIntegrityConstraintViolationException e){
            throw new ResponseStatusException(HttpStatus.ALREADY_REPORTED,String.format("Tag with ID: %d already added to ticket",tagId),e);
        } TODO: Error handling for all methods   */
    }

    //---------------------------COMMENTS--------------------------------------------------------
    //TODO: Edit/Delete Comment
    //tested
    @GetMapping("/{ticketId}/comments")
    public List<Comment> getCommentsForTicket(@PathVariable int ticketId,@RequestParam int pageNo){
        return commentService.getCommentsByTicket(ticketId,pageNo);
    }
    //tested link like: http://localhost:8080/api/tickets/2/comments?userId=3
    @PostMapping("/{ticketId}/comments")
    public Optional<Comment> addComment(@RequestBody Comment comment,@PathVariable int ticketId,@RequestParam int userId){
        return commentService.addComment(comment,ticketId,userId);
    }

    //update comment method here

    //---------------------------History--------------------------------------------------------



    @GetMapping("/{ticketId}/history")
    public ResponseEntity<List<TicketHistory>> getHistoryOfTicket(@PathVariable int ticketId, @RequestParam int pageNo){
        return ResponseEntity.ok().body(ticketHistoryService.getHistoryOfTicket(ticketId,pageNo));
    }

    //---------------------------Stats--------------------------------------------------------


    @GetMapping("/{ticketId}/count")
    public ResponseEntity<Integer> getNoOfCommentsForTicket(@PathVariable int ticketId){
        return ResponseEntity.ok().body(ticketService.getNoOfCommentsForTicket(ticketId));
    }
    @GetMapping("/priority/count")
    public ResponseEntity<List<PriorityStatsDto>> getTicketsCountByPriority(){
        return ResponseEntity.ok().body(ticketService.getTicketsCountByPriority());
    }

    @GetMapping("/stats/allAndFinished")
    public ResponseEntity<Integer[]> getAllAndFinishedTicketsCount(){
        return ResponseEntity.ok().body(ticketService.getAllTicketsAndFinishedTicketsCount());
    }

    @GetMapping("/{userId}/stats")
    public ResponseEntity<Integer[]> getUserAssignedAndCreatedTicketsCount(@PathVariable int userId){
        return ResponseEntity.ok().body(ticketService.getUserAssignedAndCreatedTickets(userId));
    }




}
