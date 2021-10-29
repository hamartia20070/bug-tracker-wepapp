package com.bugtracker.backend.repository;

import com.bugtracker.backend.entity.Project;
import com.bugtracker.backend.entity.Tag;
import com.bugtracker.backend.entity.TicketHistory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface TicketHistoryRepository extends JpaRepository<TicketHistory,Integer>, PagingAndSortingRepository<TicketHistory,Integer> {

    @Query("select t from TicketHistory t " +
            "where t.ticket.id = ?1")
    List<TicketHistory> getHistoryOfTicket(int ticketId,Pageable pageable);

}
