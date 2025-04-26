package com.benjamin.Employee_Service.model.department;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentAnalyticsResponse {
    private String departmentName;
    private Long departmentId;
    private int headcount;
    private double averageSalary;
}
