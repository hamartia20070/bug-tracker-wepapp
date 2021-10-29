package com.bugtracker.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import java.util.*;

@Entity
@JsonIgnoreProperties(value = {"tickets","projectUserRoleLinks"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Setter
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    @Column(name = "idProject", nullable = false)
    @JsonProperty("id")
    private Integer id;

    @Column(name = "projectName", length = 50)
    @JsonProperty("projectName")
    private String projectName;

    @Column(name = "projectDesc", length = 2000)
    @JsonProperty("projectDescription")
    private String projectDescription;

    @Column(name = "projectOpen")
    @JsonProperty("projectOpen")
    private boolean projectOpen;

    @Column(name = "projectStarted", length = 50)
    @JsonProperty("dateStarted")
    private Date dateStarted;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "priority_id",referencedColumnName = "idPriority")
    private Priority projectPriority ;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "project")
    private List<Ticket> tickets = new ArrayList<>();

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            mappedBy = "project")
    private List<ProjectUserRoleLink> projectUserRoleLinks;

    public Project(String projectName, String projectDescription, boolean projectOpen, Date dateStarted, Priority projectPriority) {
        this.projectName = projectName;
        this.projectDescription = projectDescription;
        this.projectOpen = projectOpen;
        this.dateStarted = dateStarted;
        this.projectPriority = projectPriority;
    }
}
