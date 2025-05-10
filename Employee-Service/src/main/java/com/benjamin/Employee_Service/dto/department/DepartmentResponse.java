package com.benjamin.Employee_Service.dto.department;

import com.benjamin.Employee_Service.model.department.Department;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentResponse {
    private Department department;
    private String message;
}
