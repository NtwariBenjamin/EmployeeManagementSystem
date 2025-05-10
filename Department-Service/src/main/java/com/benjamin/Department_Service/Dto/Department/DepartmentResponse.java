package com.benjamin.Department_Service.Dto.Department;

import com.benjamin.Department_Service.model.Department;
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
