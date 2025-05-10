package com.benjamin.Department_Service.controller;

import com.benjamin.Department_Service.exception.DepartmentNotFoundException;
import com.benjamin.Department_Service.model.Department;
import com.benjamin.Department_Service.Dto.Department.DepartmentRequest;
import com.benjamin.Department_Service.Dto.Department.DepartmentResponse;
import com.benjamin.Department_Service.service.DepartmentServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/department")
@Tag(name = "Department Management", description = "Endpoints for managing departments")
@Slf4j
public class DepartmentController {

    @Autowired
    private DepartmentServiceImpl departmentService;

    @PostMapping("/create")
    @Operation(summary = "Create department", description = "Adds a new department")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Department created successfully"),
            @ApiResponse(responseCode = "500", description = "Error creating department")
    })
    public ResponseEntity<DepartmentResponse> createDepartment(@RequestBody DepartmentRequest departmentRequest) {
        try {
            log.info("Creating Department with Request: {}", departmentRequest);
            DepartmentResponse departmentResponse = departmentService.createDepartment(departmentRequest);
            return ResponseEntity.ok(departmentResponse);
        } catch (Exception e) {
            log.error("Error Creating Department with Request: {}", departmentRequest, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{departmentName}")
    @Operation(summary = "Get department by name", description = "Retrieves department details by name")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Department found"),
            @ApiResponse(responseCode = "404", description = "Department not found")
    })
    public ResponseEntity<DepartmentResponse> getDepartmentByName(@PathVariable String departmentName) {
        try {
            log.info("Fetching Department By Name: {}", departmentName);
            DepartmentResponse departmentResponse = departmentService.getDepartmentByName(departmentName);
            return ResponseEntity.ok(departmentResponse);
        } catch (Exception e) {
            log.error("{} Department Not Found!", departmentName, e);
            throw new DepartmentNotFoundException(departmentName + " Department Not Found!");
        }
    }

    @GetMapping("/id/{departmentId}")
    @Operation(summary = "Get department by ID", description = "Retrieves department details by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Department found"),
            @ApiResponse(responseCode = "404", description = "Department not found")
    })
    public ResponseEntity<DepartmentResponse> getDepartmentById(@PathVariable Long departmentId) {
        try {
            log.info("Fetching Department By Id: {}", departmentId);
            DepartmentResponse departmentResponse = departmentService.getDepartmentById(departmentId);
            return ResponseEntity.ok(departmentResponse);
        } catch (Exception e) {
            log.error("{} Department Not Found", departmentId, e);
            throw new DepartmentNotFoundException(departmentId + " Department Id Not Found");
        }
    }

    @GetMapping
    @Operation(summary = "Get all departments", description = "Returns a list of all departments")
    @ApiResponse(responseCode = "200", description = "Departments retrieved successfully")
    public ResponseEntity<List<Department>> getAllDepartments() {
        try {
            log.info("Get All Departments");
            List<Department> departments = departmentService.getAllDepartments();
            return ResponseEntity.ok(departments);
        } catch (Exception e) {
            log.error("Error getting Departments!");
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/update/{departmentName}")
    @Operation(summary = "Update department", description = "Updates the details of a department by name")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Department updated successfully"),
            @ApiResponse(responseCode = "404", description = "Department not found"),
            @ApiResponse(responseCode = "500", description = "Error updating department")
    })
    public ResponseEntity<DepartmentResponse> updateDepartment(@PathVariable String departmentName,
                                                               @RequestBody DepartmentRequest departmentRequest) {
        try {
            log.info("Updating Department by {} with Request {}", departmentName, departmentRequest);
            DepartmentResponse departmentResponse = departmentService.updateDepartment(departmentName, departmentRequest);
            return ResponseEntity.ok(departmentResponse);
        } catch (RuntimeException e) {
            log.error("Error Updating Department {}", departmentName, e);
            return ResponseEntity.internalServerError().build();
        } catch (Exception e) {
            log.error("{} Department Not Found!", departmentName, e);
            throw new DepartmentNotFoundException(departmentName + " Department Not Found!");
        }
    }

    @DeleteMapping("/delete/{departmentId}")
    @Operation(summary = "Delete department", description = "Deletes a department by its ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Department deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Department not found"),
            @ApiResponse(responseCode = "500", description = "Error deleting department")
    })
    public ResponseEntity<DepartmentResponse> deleteDepartment(@PathVariable Long departmentId) {
        try {
            log.info("Deleting Department By Id: {}", departmentId);
            DepartmentResponse departmentResponse = departmentService.deleteDepartmentById(departmentId);
            return ResponseEntity.ok(departmentResponse);
        } catch (RuntimeException e) {
            log.error("Error Deleting Department {}", departmentId, e);
            return ResponseEntity.internalServerError().build();
        } catch (Exception e) {
            log.error("{} Department Not Found!", departmentId, e);
            throw new DepartmentNotFoundException(departmentId + " Department Not Found!");
        }
    }
}