package com.benjamin.Employee_Service.model.employee;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeRequest {
    @JsonProperty
    private Long id;
    @JsonProperty
    private String name;
    @JsonProperty
    private String email;
    @JsonProperty
    private Long departmentId;
    @JsonProperty
    private Long userId; //references User in Authentication service
    @JsonProperty
    private String salary;
    @JsonProperty
    private LocalDate hireDate;
    private Boolean isActive;

}
