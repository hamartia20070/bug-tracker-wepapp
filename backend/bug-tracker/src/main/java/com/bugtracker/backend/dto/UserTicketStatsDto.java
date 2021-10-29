package com.bugtracker.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class UserTicketStatsDto {
    @JsonProperty("ticketsCreatedByUser")
    private int ticketsCreatedByUser;
    @JsonProperty("ticketsCompletedByUser")
    private int ticketsCompletedByUser;
    @JsonProperty("ticketsAssignedToUser")
    private int ticketsAssignedToUser;

}
