package com.bugtracker.backend.dto;

import com.bugtracker.backend.entity.Ticket;
import com.bugtracker.backend.entity.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CommentDto {

    @JsonProperty("id")
    private int id;

    @JsonProperty("ticketId")
    private int ticketId;

    @JsonProperty("message")
    private String message;

    @JsonProperty("userId")
    private int userId;
}
