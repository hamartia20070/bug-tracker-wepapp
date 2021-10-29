package com.bugtracker.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;

@Entity
@JsonIgnoreProperties(value = {"ticket"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Setter
@NamedEntityGraph(name = "Comment.ticket",
        attributeNodes = {@NamedAttributeNode("ticket")}
)
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    @Column(name = "idComment", nullable = false)
    private int id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ticket_id",referencedColumnName = "idTicket")
    private Ticket ticket;

    @Column(name = "CommentMessage", length = 800)
    private String message;



    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id",referencedColumnName = "idUser")
    private User user;

    public Comment(String message) {
        this.message = message;
    }

    public Comment(Ticket ticket, String message, User user) {
        this.ticket = ticket;
        this.message = message;
        this.user = user;
    }
}
