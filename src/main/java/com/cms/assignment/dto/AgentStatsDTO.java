package com.cms.assignment.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AgentStatsDTO {
    private String agentName;
    private String department;
    private Long solvedCount;
    private Long activeCount;

}
