package com.benjamin.Employee_Service.dto.performance;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PerformanceRequest {

    private Long employeeId;

    private String reviewPeriod;

    private Double score;

    private String comments;
}

