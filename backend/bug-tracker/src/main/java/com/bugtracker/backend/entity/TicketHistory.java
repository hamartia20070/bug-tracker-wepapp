package com.bugtracker.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.util.Date;


@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Setter
public class TicketHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    @Column(name = "idTicketHistory", nullable = false)
    private int id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ticket_id",referencedColumnName = "idTicket")
    private Ticket ticket;

    @Column(name = "OldTitle", length = 50)
    private String oldTitle;

    @Column(name = "OldDesc", length = 1000)
    private String oldDesc;

    @Column(name = "DateChanged", length = 50)
    private Date dateChanged;

    public TicketHistory(Ticket ticket, String oldTitle, String oldDesc, Date dateChanged) {
        this.ticket = ticket;
        this.oldTitle = oldTitle;
        this.oldDesc = oldDesc;
        this.dateChanged = dateChanged;
    }
}
