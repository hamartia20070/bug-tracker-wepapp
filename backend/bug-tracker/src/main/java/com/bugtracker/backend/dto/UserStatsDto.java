package com.bugtracker.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
public class UserStatsDto {
    @JsonProperty("createdTickets")
    private int createdTickets;
    @JsonProperty("assignedTickets")
    private int assignedTickets;
    @JsonProperty("noOfProjects")
    private int noOfProjects;
    @JsonProperty("noOfComments")
    private int noOfComments;
}
