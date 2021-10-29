package com.bugtracker.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@JsonIgnoreProperties(value = {"ticketsMade","ticketsAssigned","comments","projectUserRoleLinks"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
//User creates conflict in postgreSql
@Table(name = "bugtrack_users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    @Column(name = "idUser", nullable = false)
    private Integer id;


    @Column(name = "UserName", length = 45, unique = true)
    private String userName;

    @Column(name = "UserEmail", length = 45)
    private String userEmail;

    @Column(name = "UserPhoneNo", length = 45)
    private String userPhoneNo;

    @Column(name = "UserPass", length = 1000)
    private String password;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "submitUser")
    private Set<Ticket> ticketsMade = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "assignedUser")
    private Set<Ticket> ticketsAssigned = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private Set<Comment> comments = new HashSet<>();

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            mappedBy = "user")
    private Set<ProjectUserRoleLink> projectUserRoleLinks;

    public User(String userName, String userEmail, String userPhoneNo, String password) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPhoneNo = userPhoneNo;
        this.password = password;
    }
}
