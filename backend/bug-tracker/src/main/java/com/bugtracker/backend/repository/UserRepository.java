package com.bugtracker.backend.repository;

import com.bugtracker.backend.entity.Priority;
import com.bugtracker.backend.entity.Project;
import com.bugtracker.backend.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User,Integer>, PagingAndSortingRepository<User,Integer> {

    @Query("select u from User u where u.userName = ?1")
    User findByUserName(String userName);

    @Query("select u from User u")
    List<User> getAllUsers(Pageable pageable);


    @Query("select count(t) from Ticket t where t.submitUser.id = ?1")
    int getUserCreatedTicketsCount(int userId);
    @Query("select count(t) from Ticket t where t.assignedUser.id = ?1")
    int getUserAssignedTicketsCount(int userId);
    @Query("select count(l) from ProjectUserRoleLink l where l.user.id = ?1")
    int getUserProjectsCount(int userId);
    @Query("select count(c) from Comment c where c.user.id = ?1")
    int getUserCommentsCount(int userId);
}
