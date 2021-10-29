package com.bugtracker.backend.repository;

import com.bugtracker.backend.entity.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket,Integer>, PagingAndSortingRepository<Ticket,Integer> {

    @Query("select t from Ticket t " +
            "where t.project.id = ?1")
    List<Ticket> getTicketByProject(int projectId, Pageable pageable);

    @Query("select t from Ticket t " +
            "LEFT JOIN t.project.projectUserRoleLinks link " +
            "where link.user.id = ?1")
    List<Ticket> getTicketByProjectsWithUser(int userId, Pageable pageable);

    @Query("select t from Ticket t " +
            "where t.project.id = ?1 " +
            "and t.ticketPriority.id = ?2")
    List<Ticket> getTicketByProjectAndPriority(int projectId, int priorityId, Pageable pageable);

    @Query("select t from Ticket t " +
            "LEFT JOIN t.tags tag " +
                  "where tag.id = ?1 " +
                    "and t.project.id = ?2")
    List<Ticket> getTicketByTagAndProject(int tagId, int projectId, Pageable pageable);

    @Query("select t from Ticket t")
    List<Ticket> getAllTickets(Pageable pageable);

    @Query("select t from Ticket t " +
            "where t.submitUser.id = ?1")
    List<Ticket> getTicketsByUser(int userId, Pageable pageable);

    @Query("select count(t) from Ticket t " +
            "where t.submitUser.id = ?1 and t.project.id = ?2")
    int getNoOfTicketsMadeByUserForProject(int userId, int projectId);

    @Query("select count(t) from Ticket t " +
            "where t.assignedUser.id = ?1 and status= com.bugtracker.backend.entity.TicketStatus.CLOSED and t.project.id = ?2")
    int getNoOfTicketsCompletedByUserForProject(int userId, int projectId);

    @Query("select count(t) from Ticket t " +
            "where t.assignedUser.id = ?1 and status= com.bugtracker.backend.entity.TicketStatus.OPEN and t.project.id = ?2")
    int getNoOfTicketsPendingForUserForProject(int userId, int projectId);

    @Query("select t from Ticket t " +
            "where t.assignedUser.id = ?1")
    List<Ticket> getTicketsByAssignedUser(int userId, Pageable pageable);

    @Query("select count(c) from Comment c " +
            "where c.ticket.id = ?1")
    int getNoOfComments(int ticketId);

    @Query("select count(t) from Ticket t " +
            "where t.ticketPriority.importance = ?1")
    int getTicketsCountByPriority(int priorityImportance);

    @Query("select count(t) from Ticket t " +
            "where t.status = com.bugtracker.backend.entity.TicketStatus.CLOSED")
    int getCountOfTicketsCompleted();
    @Query("select count(t) from Ticket t ")
    int getCountOfTickets();

    @Query("select count(t) from Ticket t " +
            "where t.assignedUser.id = ?1")
    int getTicketsCountByAssignedUser(int userId);
    @Query("select count(t) from Ticket t " +
            "where t.submitUser.id = ?1")
    int getTicketsCountByUser(int userId);

    @Query("select t from Ticket t " +
            "LEFT JOIN t.project.projectUserRoleLinks link " +
            "where link.user.id = ?1 and t.status = ?2")
    List<Ticket> getTicketsByStatusForProjectsWithUser(int userId, TicketStatus status, Pageable pageable);

    @Query("select t from Ticket t " +
            "LEFT JOIN t.project.projectUserRoleLinks link " +
            "LEFT JOIN t.tags tags " +
            "where link.user.id = ?1 and tags.id = ?2")
    List<Ticket> getTicketsByTagForProjectsWithUser(int userId, int tagId, Pageable pageable);

}
