package com.benjamin.Employee_Service.model.employee;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Locale;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table
@Entity
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
