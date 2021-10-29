package com.bugtracker.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.util.*;


@Entity
@JsonIgnoreProperties(value = {"comments","ticketHistory"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Setter
@NamedEntityGraph(name = "Ticket.comments",
        attributeNodes = {@NamedAttributeNode("comments")}
)
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    @Column(name = "idTicket", nullable = false)
    private int id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "project_id",referencedColumnName = "idProject")
    private Project project;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "submit_user_id",referencedColumnName = "idUser")
    private User submitUser;

    @Column(name = "ticketTitle", length = 50)
    private String ticketTitle;

    @Column(name = "TicketDesc", length = 1000)
    private String ticketDesc;

    @Column(name = "ticketCreated", length = 50)
    private Date ticketCreated;

    @Column(name = "ticketDeadline", length = 50)
    private Date deadline;

    @Column(name = "ticketStatus", length = 50)
    private TicketStatus status;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "assigned_user_id",referencedColumnName = "idUser")
    private User assignedUser;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "priority_id",referencedColumnName = "idPriority")
    private Priority ticketPriority;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ticket", fetch = FetchType.LAZY)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ticket", fetch = FetchType.LAZY)
    private Set<TicketHistory> ticketHistory = new HashSet<>();

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
           name="ticket_tags",
            joinColumns = {@JoinColumn(name="ticket_id")},
            inverseJoinColumns = {@JoinColumn(name="tag_id")}
    )
    private Set<Tag> tags = new HashSet<>();

    public Ticket(Project project, User submitUser, String ticketTitle, String ticketDesc, Date ticketCreated, Date deadline, TicketStatus status, User assignedUser, Priority ticketPriority) {
        this.project = project;
        this.submitUser = submitUser;
        this.ticketTitle = ticketTitle;
        this.ticketDesc = ticketDesc;
        this.ticketCreated = ticketCreated;
        this.deadline = deadline;
        this.status = status;
        this.assignedUser = assignedUser;
        this.ticketPriority = ticketPriority;
    }
}
