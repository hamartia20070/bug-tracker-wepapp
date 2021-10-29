package com.bugtracker.backend.service;

import com.bugtracker.backend.entity.Ticket;
import com.bugtracker.backend.entity.TicketHistory;
import com.bugtracker.backend.repository.TicketHistoryRepository;
import com.bugtracker.backend.util.GlobalUtil;
import lombok.NonNull;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Log
@Transactional
public class TicketHistoryService {
    @Autowired
    private TicketHistoryRepository ticketHistoryRepository;

    @Autowired
    private TicketService ticketService;

    public Optional<TicketHistory> addHistory(TicketHistory ticketHistory, int ticketId){
        addTicketToHistory(ticketId,ticketHistory);
        ticketService.saveTicket(ticketHistory.getTicket());
        return Optional.of(ticketHistoryRepository.save(ticketHistory));
    }
    public  void addTicketToHistory(int ticketId, TicketHistory ticketHistory){
        //check if ticket exists
        Ticket toAdd= ticketService.getTicketById(ticketId);
        ticketHistory.setTicket(toAdd);
    }

    public TicketHistory saveTicketHistory(TicketHistory ticketHistory){
        return ticketHistoryRepository.save(ticketHistory);
    }

    public void saveTicketHistories(List<TicketHistory> ticketHistories){
        ticketHistoryRepository.saveAll(ticketHistories);
    }

    public List<TicketHistory> getHistoryOfTicket(@NonNull int ticketId, int pageNo){
        Pageable page = PageRequest.of(pageNo, GlobalUtil.itemsPerPage);
        return ticketHistoryRepository.getHistoryOfTicket(ticketId,page);
    }

}
