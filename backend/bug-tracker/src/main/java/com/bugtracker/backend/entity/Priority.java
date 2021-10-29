package com.bugtracker.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@JsonIgnoreProperties(value = {"projects","tickets"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Setter
public class Priority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    @Column(name = "idPriority", nullable = false)
    private Integer id;

    @Column(name = "priorityType", length = 50)
    private String priorityType;

    @Column(name = "priorityImportance")
    private int importance;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "projectPriority")
    private Set<Project> projects = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ticketPriority")
    private Set<Ticket> tickets = new HashSet<>();

    public Priority(String priorityType, int importance) {
        this.priorityType = priorityType;
        this.importance = importance;
    }
}
