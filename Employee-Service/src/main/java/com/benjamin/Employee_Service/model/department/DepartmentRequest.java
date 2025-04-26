package com.benjamin.Employee_Service.model.department;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentRequest {
    @JsonProperty
    private Long id;
    @JsonProperty
    private String name;
    @JsonProperty
    private Long managerId;
    @JsonProperty
    private String location;
}
