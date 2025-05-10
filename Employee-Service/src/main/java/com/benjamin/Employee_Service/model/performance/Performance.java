package com.benjamin.Employee_Service.model.performance;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "performance")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Performance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long employeeId; // Foreign key to Employee

    private String reviewPeriod; // e.g., "Q1 2025", "2025", "Monthly"

    private Double score; // Performance score (e.g., 0.0 - 100.0)

    private String comments; // Optional manager comments

    private LocalDate reviewDate;
}

