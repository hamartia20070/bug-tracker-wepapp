package com.bugtracker.backend.repository;

import com.bugtracker.backend.entity.Comment;
import com.bugtracker.backend.entity.Project;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Integer>, PagingAndSortingRepository<Comment,Integer> {


    @Query("select c from Comment c "+
            "where c.ticket.id = ?1")
    List<Comment> getCommentsByTicket(int ticketId, Pageable pageable);
}
