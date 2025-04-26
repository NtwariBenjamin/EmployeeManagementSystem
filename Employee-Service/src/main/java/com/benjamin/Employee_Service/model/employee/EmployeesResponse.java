package com.benjamin.Employee_Service.model.employee;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeesResponse {
    private String name;
    private String username;
    private String email;
    private String role;
    private String salary;
    private String departmentName;
    private LocalDate hireDate;
    private Boolean isActive;
}
