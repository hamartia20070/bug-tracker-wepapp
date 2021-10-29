package com.bugtracker.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonIgnoreProperties(value = {"tickets"})
@AllArgsConstructor
@Getter
@Setter
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    @Column(name = "idTag", nullable = false)
    private int id;

    @Column(name = "TagName", length = 30)
    private String tag;

    @ManyToMany(mappedBy = "tags")
    private Set<Ticket> tickets = new HashSet<>();


    public Tag(String tag) {
        this.tag = tag;
    }
}
