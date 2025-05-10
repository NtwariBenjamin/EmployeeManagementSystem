package com.benjamin.Employee_Service.dto.performance;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PerformanceResponse {

    private Long id;

    private Long employeeId;

    private String employeeName;

    private String reviewPeriod;

    private Double score;

    private String comments;

    private LocalDate reviewDate;

    private String message;
}
