package com.bugtracker.backend.repository;

import com.bugtracker.backend.entity.Project;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project,Integer>, PagingAndSortingRepository<Project,Integer> {

    @Query("select p from Project p ")
    List<Project> getAll(Pageable pageable);

    @Query("select p from Project p where p.projectPriority.importance = ?1")
    List<Project> getAllByPriority(Integer priority,Pageable pageable);

    @Query("select count(p) from Project p ")
    Integer getAllCount();

    @Nullable
    @Query("select p.project from ProjectUserRoleLink p "+
            "where p.user.id = ?1")
    List<Project> findByUser(int userId,Pageable pageable);

    @Query("select p from Project p "+
            "where p.projectOpen = ?1")
    List<Project> findByStatus(boolean status,Pageable pageable);

    @Query("select p from Project p "+
            "where p.projectOpen = ?1 and p.projectPriority.importance = ?2")
    List<Project> findByStatusAndPriority(boolean status, Integer priority,Pageable pageable);

    @Query("select DISTINCT t.project, t.ticketCreated from Ticket t " +
            "order by t.ticketCreated")
    List<Project> getByLastActive(Pageable pageable);

    @Nullable
    @Query("select p.project from ProjectUserRoleLink p "+
            "where p.user.id = ?1 and p.project.id = ?2")
    Project findProjectByUser(int userId, int projectId);


    @Query("select p.project from ProjectUserRoleLink p " +
            "LEFT JOIN p.project " +
            "LEFT JOIN p.user " +
            "LEFT JOIN p.project.projectPriority " +
            "where p.user.id = ?1 " +
            "and p.project.projectPriority.importance = ?2")
    List<Project> findByUserAndPriority(int userId, Integer priority, Pageable pageable);

    @Query("select p.project from ProjectUserRoleLink p "+
            "where p.user.id = ?1 " +
            "and p.role.id = ?2")
    List<Project> findByUserAndRole(int userId, int roleId, Pageable pageable);

    @Query("select p from Project p " +
            "where p.projectPriority.id = ?1")
    List<Project> findByPriority(int priorityId);

    @Query("select count(t) from Ticket t where t.status = com.bugtracker.backend.entity.TicketStatus.OPEN and t.project.id = ?1")
    int getNumberOfActiveTicketsForProject(int projectId);
    @Query("select count(t) from Ticket t where t.status = com.bugtracker.backend.entity.TicketStatus.CLOSED and t.project.id = ?1")
    int getNumberOfClosedTicketsForProject(int projectId);
    @Query("select count(t) from Ticket t where t.status = com.bugtracker.backend.entity.TicketStatus.ONHOLD and t.project.id = ?1")
    int getNumberOfOnHoldTicketsForProject(int projectId);
    @Query("select count(t) from Ticket t where t.status = com.bugtracker.backend.entity.TicketStatus.OPEN and t.project.id = ?1 and " +
            "t.assignedUser.id = ?2")
    int getNumberOfAssignedTicketsOfProjectToUser(int projectId, int userId);

    @Query("select count(p) from ProjectUserRoleLink p where p.project.id = ?1")
    int getNumberOfUsersOfProject(int projectId);

    @Query("select count(p) from Project p " +
            "where p.projectPriority.importance = ?1")
    int getProjectsCountByPriority(int priorityImportance);
}
