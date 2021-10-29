package com.bugtracker.backend.service;

import com.bugtracker.backend.dto.PriorityStatsDto;
import com.bugtracker.backend.dto.UserTicketStatsDto;
import com.bugtracker.backend.entity.*;
import com.bugtracker.backend.repository.TicketRepository;
import com.bugtracker.backend.util.GlobalUtil;
import lombok.NonNull;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Log
@Transactional //means we don't have to call save, transactions are persisted automatically
@Slf4j //just logs what the service is doing by calling log.info etc
//@RequiredArgsConstructor //manages injecting beans
public class TicketService {

    @Autowired
    private  TicketRepository ticketRepository;
    @Autowired
    private  CommentService commentService;
    @Autowired
    private  ProjectService projectService;
    @Autowired
    private  TagService tagService;
    @Autowired
    private  UserService userService;
    @Autowired
    private  PriorityService priorityService;
    @Autowired
    private  TicketHistoryService ticketHistoryService;



    public Ticket getTicketOfComment(@NonNull int commentId){
        Comment existingComment= commentService.getCommentById(commentId);

        return existingComment.getTicket();
    }


    public List<Ticket> getAllTickets(int pageNo){
        Pageable page = PageRequest.of(pageNo, GlobalUtil.itemsPerPage);
        return ticketRepository.getAllTickets(page);
    }
    public List<Ticket> getAllTickets(){
        return ticketRepository.findAll();
    }

    public Ticket getTicketById(@NonNull int ticketId){
        return ticketRepository.findById(ticketId).orElse(null);
    }


    public Optional<Ticket> addTagToTicket(int tagId, int ticketId){
        Tag tag = tagService.getTagById(tagId).orElse(null);
        Ticket ticket = ticketRepository.findById(ticketId).orElse(null);
        ticket.getTags().add(tag);
        tag.getTickets().add(ticket);

        Optional<Ticket> result = Optional.of(ticketRepository.save(ticket));
        tagService.saveTag(tag);

        return result;


    }

    public List<Ticket> getTicketByProject(int projectId, int pageNo){
        Pageable page = PageRequest.of(pageNo, GlobalUtil.itemsPerPage);
        return ticketRepository.getTicketByProject(projectId,page);
    }

    public void addAllTickets(List<Ticket> tickets){
        ticketRepository.saveAll(tickets);
    }

    public Ticket saveTicket(@NonNull Ticket ticket){
        //have to create a new object here otherwise jpa will pick up the null values from the JSON coming from the frontend and insert
        // new null rows to user, priority and project for some reason, not sure why when I'm doing the same thing here, and it works
        Ticket newTicket = new Ticket(null,null, ticket.getTicketTitle(), ticket.getTicketDesc(),
                ticket.getTicketCreated(),ticket.getDeadline(),ticket.getStatus(),null,null);
        return ticketRepository.save(newTicket);
    }

    public Ticket assignTicket(@NonNull int ticketId, Integer assignedUserId){
        Ticket ticket = ticketRepository.findById(ticketId).orElse(null);
        if(assignedUserId!=null){
            ticket.setAssignedUser(userService.getUserById(assignedUserId).orElse(null));
        }
        return ticketRepository.save(ticket);
    }
    public Ticket assignTicket(@NonNull Ticket ticket,int projectId,int userId,int priorityId){
        ticket.setProject(projectService.getProjectById(projectId).orElse(null));
        ticket.setTicketPriority(priorityService.getPriorityById(priorityId).orElse(null));
        ticket.setSubmitUser(userService.getUserById(userId).orElse(null));

        return ticketRepository.save(ticket);
    }

    /*public void addTagToTicket(int tagId, int ticketId){
        Tag tag = tagService.getTagById(tagId).orElse(null);


    }*/

    public Ticket updateTicket(Ticket ticket, int ticketId, int priorityId){
        Ticket existingTicket = ticketRepository.findById(ticketId).orElse(null);

        createTicketHistory(existingTicket);
        existingTicket.setTicketTitle(ticket.getTicketTitle());
        existingTicket.setTicketDesc(ticket.getTicketDesc());
        existingTicket.setTicketCreated(ticket.getTicketCreated());
        existingTicket.setDeadline(ticket.getDeadline());
        existingTicket.setStatus(ticket.getStatus());
        existingTicket.setTicketPriority(priorityService.getPriorityById(priorityId).orElse(null));



        return ticketRepository.save(existingTicket);


    }

    public List<Ticket> getTicketByProjectAndPriority(int projectId, int priorityId, int pageNo){
        Pageable page = PageRequest.of(pageNo, GlobalUtil.itemsPerPage);
        return ticketRepository.getTicketByProjectAndPriority(projectId,priorityId,page);
    }

    public List<Ticket> getTicketByTagAndProject(int tagId, int projectId, int pageNo){
        Pageable page = PageRequest.of(pageNo, GlobalUtil.itemsPerPage);
        return ticketRepository.getTicketByTagAndProject(tagId,projectId,page);
    }

    public List<Ticket> getTicketsByUser(int userId, int pageNo){
        Pageable page = PageRequest.of(pageNo, GlobalUtil.itemsPerPage);
        return ticketRepository.getTicketsByUser(userId,page);

    }

    public List<Ticket> getTicketsByAssignedUser(int userId, int pageNo){
        Pageable page = PageRequest.of(pageNo, GlobalUtil.itemsPerPage);
        return ticketRepository.getTicketsByAssignedUser(userId,page);

    }

    public UserTicketStatsDto getUserStatsForProject(int userId, int projectId){
        UserTicketStatsDto result = new UserTicketStatsDto();
        result.setTicketsCreatedByUser(ticketRepository.getNoOfTicketsMadeByUserForProject(userId,projectId));
        result.setTicketsAssignedToUser(ticketRepository.getNoOfTicketsPendingForUserForProject(userId,projectId));
        result.setTicketsCompletedByUser(ticketRepository.getNoOfTicketsCompletedByUserForProject(userId,projectId));
        return result;
    }

    private void createTicketHistory(Ticket existingTicket){
        Date today = new Date();
        TicketHistory ticketHistory = new TicketHistory(
                existingTicket,
                existingTicket.getTicketTitle(),
                existingTicket.getTicketDesc(),
                today
        );

        ticketHistoryService.saveTicketHistory(ticketHistory);
    }

    public List<Ticket> getTicketsByProjectWithUser(int userId, int pageNo,String sortBy,String getBy,Integer tagId){
        Pageable page = PageRequest.of(pageNo, GlobalUtil.itemsPerPage, Sort.by(Sort.Direction.DESC,sortBy));

        if(tagId != 0){

            return ticketRepository.getTicketsByTagForProjectsWithUser(userId,tagId.intValue(),page);
        }
            switch (getBy) {
                case "all":
                    return ticketRepository.getTicketByProjectsWithUser(userId,page);
                case "owned by":
                    return ticketRepository.getTicketsByUser(userId,page);
                case "assigned to":
                    return ticketRepository.getTicketsByAssignedUser(userId,page);
                case "active":
                    return ticketRepository.getTicketsByStatusForProjectsWithUser(userId, TicketStatus.OPEN,page);
                case "onhold":
                    return ticketRepository.getTicketsByStatusForProjectsWithUser(userId, TicketStatus.ONHOLD,page);
                case "closed":
                    return ticketRepository.getTicketsByStatusForProjectsWithUser(userId, TicketStatus.CLOSED,page);
                default:
                    return null;
            }




    }

    public int getNoOfCommentsForTicket(int ticketId){
        return ticketRepository.getNoOfComments(ticketId);
    }

    public List<PriorityStatsDto> getTicketsCountByPriority(){
        List<PriorityStatsDto> results = new ArrayList<>();

        for(int i=1;i<=10;i++){
            results.add(new PriorityStatsDto("Ticket priority level: "+i,ticketRepository.getTicketsCountByPriority(i)));
        }
        return results;
    }

    public Integer[] getAllTicketsAndFinishedTicketsCount(){
        Integer [] result= new Integer[2];
        result[0]= ticketRepository.getCountOfTickets();
        result[1]= ticketRepository.getCountOfTicketsCompleted();
        return result;
    }
    public Integer[] getUserAssignedAndCreatedTickets(int userId){
        Integer [] result= new Integer[2];
        result[0]= ticketRepository.getTicketsCountByAssignedUser(userId);
        result[1]= ticketRepository.getTicketsCountByUser(userId);
        return result;
    }

}
