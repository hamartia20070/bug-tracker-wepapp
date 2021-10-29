package com.bugtracker.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.util.Set;


@Entity
@JsonIgnoreProperties(value = {"projectUserRoleLinks"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Setter
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    @Column(name = "idRole", nullable = false)
    private Integer id;

    @Column(name = "RoleType", length = 50)
    private String roleType;

    @Column(name = "RoleAccessLevel")
    private int accessLevel;//1 = read, 2 = read & write 3 = Owner

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            mappedBy = "role")
    private Set<ProjectUserRoleLink> projectUserRoleLinks;

    public Role(String roleType, int accessLevel) {
        this.roleType = roleType;
        this.accessLevel = accessLevel;
    }
}
