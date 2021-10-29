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
public class PriorityStatsDto {
    @JsonProperty("name")
    private String name;
    @JsonProperty("value")
    private int value;
}
