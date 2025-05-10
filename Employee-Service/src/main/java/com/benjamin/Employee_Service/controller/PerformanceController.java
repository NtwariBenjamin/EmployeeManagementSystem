package com.benjamin.Employee_Service.controller;

import com.benjamin.Employee_Service.dto.performance.PerformanceRequest;
import com.benjamin.Employee_Service.dto.performance.PerformanceResponse;
import com.benjamin.Employee_Service.service.PerformanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/performance")
@Tag(name = "Performance Management", description = "Endpoints for managing employee performance reviews")
public class PerformanceController {

    @Autowired
    private PerformanceService performanceService;

    @PostMapping
    @Operation(summary = "Add performance review", description = "Adds a performance review for an employee")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Review added successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    public ResponseEntity<PerformanceResponse> addReview(@RequestBody PerformanceRequest request) {
        return ResponseEntity.ok(performanceService.addPerformanceReview(request));
    }

    @GetMapping("/employee/{employeeId}")
    @Operation(summary = "Get employee performance reviews", description = "Retrieves all performance reviews for a given employee")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Performance reviews retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Employee not found")
    })
    public ResponseEntity<List<PerformanceResponse>> getEmployeePerformance(@PathVariable Long employeeId) {
        return ResponseEntity.ok(performanceService.getPerformanceByEmployee(employeeId));
    }

    @GetMapping
    @Operation(summary = "Get all performance reviews", description = "Retrieves all employee performance reviews")
    @ApiResponse(responseCode = "200", description = "All performance reviews retrieved successfully")
    public ResponseEntity<List<PerformanceResponse>> getAllPerformance() {
        return ResponseEntity.ok(performanceService.getAllPerformances());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update performance review", description = "Updates an existing performance review by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Review updated successfully"),
            @ApiResponse(responseCode = "404", description = "Review not found")
    })
    public ResponseEntity<PerformanceResponse> updateReview(@PathVariable Long id, @RequestBody PerformanceRequest request) {
        return ResponseEntity.ok(performanceService.updatePerformanceReview(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete performance review", description = "Deletes a performance review by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Review deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Review not found")
    })
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        performanceService.deletePerformanceReview(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/department/{departmentId}/average-score")
    @Operation(summary = "Get average score by department", description = "Retrieves average performance score for a department")
    @ApiResponse(responseCode = "200", description = "Average score retrieved successfully")
    public ResponseEntity<Double> getDepartmentAverageScore(@PathVariable Long departmentId) {
        return ResponseEntity.ok(performanceService.getAverageScoreByDepartment(departmentId));
    }
}