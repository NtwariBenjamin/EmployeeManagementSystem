package com.benjamin.Employee_Service.controller;

import com.benjamin.Employee_Service.dto.department.DepartmentAnalyticsResponse;
import com.benjamin.Employee_Service.exception.UserNotFoundException;
import com.benjamin.Employee_Service.model.department.Department;

import com.benjamin.Employee_Service.model.employee.EmployeeRequest;
import com.benjamin.Employee_Service.model.employee.EmployeeResponse;
import com.benjamin.Employee_Service.model.employee.EmployeesResponse;
import com.benjamin.Employee_Service.model.user.Role;
import com.benjamin.Employee_Service.model.user.User;
import com.benjamin.Employee_Service.service.AuthClient;
import com.benjamin.Employee_Service.service.DepartmentClient;
import com.benjamin.Employee_Service.service.EmployeeServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employee")
@Tag(name = "Employee Management", description = "Endpoints for managing employees")
@Slf4j
public class EmployeeController {
    @Autowired
    private EmployeeServiceImpl employeeService;

    @Autowired
    private AuthClient authClient;

    @Autowired
    private DepartmentClient departmentClient;

    @PostMapping("/register")
    @Operation(summary = "Register an employee", description = "Registers a new employee with linked user and department. Only managers can register employees.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Employee registered successfully"),
            @ApiResponse(responseCode = "403", description = "Only managers can register employees"),
            @ApiResponse(responseCode = "404", description = "User or Department not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<EmployeeResponse> registerEmployee(
            @RequestHeader("Authorization") String token,
            @RequestBody EmployeeRequest employeeRequest) {
        
        try {

            User requestingUser = authClient.validateTokenAndGetUser(token);
            if (requestingUser.getRole() != Role.MANAGER) {
                log.warn("Unauthorized attempt to register employee by user: {}", requestingUser.getUsername());
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(EmployeeResponse.builder()
                        .message("Only managers can register employees")
                        .build());
            }

            Long departmentId = employeeRequest.getDepartmentId();
            Department department = departmentClient.getDepartmentDetails(departmentId);
            if (department == null || department.getId() == null) {
                log.warn("Department Not Found with departmentId {}", departmentId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(EmployeeResponse.builder()
                        .message("Department Not Found!")
                        .build());
            }

            Long userId = employeeRequest.getUserId();
            User user = authClient.getUserDetails(userId);
            if (user == null || user.getId() == null) {
                log.warn("Employee Not Found with userId {}", userId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(EmployeeResponse.builder()
                        .message("Employee Not Found!")
                        .build());
            }

            log.info("Registering employee with request {}", employeeRequest);
            EmployeeResponse employeeResponse = employeeService.registerEmployee(user, department, employeeRequest);
            return ResponseEntity.ok(employeeResponse);
        } catch (IllegalArgumentException e) {
            log.error("Invalid token or unauthorized access", e);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(EmployeeResponse.builder()
                    .message("Invalid token or unauthorized access")
                    .build());
        } catch (Exception e) {
            log.error("Error registering Employee with request {}", employeeRequest, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping
    @Operation(summary = "Get all employees", description = "Returns a list of all employees")
    @ApiResponse(responseCode = "200", description = "Employees retrieved successfully")
    public ResponseEntity<List<EmployeesResponse>> allEmployees() {
        try {
            log.info("Getting List of All Employees");
            List<EmployeesResponse> employees = employeeService.allEmployees();
            return ResponseEntity.ok(employees);
        } catch (Exception e) {
            log.error("Error retrieving Employees!", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("department/{departmentName}")
    @Operation(summary = "Get employees by department", description = "Returns employees belonging to the specified department")
    public ResponseEntity<List<EmployeesResponse>> getEmployeeByDepartment(@PathVariable String departmentName) {
        try {
            log.info("Getting Employee by departmentName: {}", departmentName);
            List<EmployeesResponse> response = employeeService.getEmployeesByDepartment(departmentName);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error getting Employee by Department: {}", departmentName, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("user/{username}")
    @Operation(summary = "Get employee by username", description = "Returns a specific employee by their username")
    public ResponseEntity<EmployeesResponse> getEmployeeByUsername(@PathVariable String username) {
        try {
            log.info("Getting Employee By Username: {}", username);
            EmployeesResponse response = employeeService.getEmployeeByUsername(username);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error("Error retrieving employee by username: {}", username, e);
            return ResponseEntity.internalServerError().build();
        } catch (Exception e) {
            log.error("Employee {} not Found!", username, e);
            throw new UserNotFoundException("User " + username + " Not Found in the System!");
        }
    }

    @GetMapping("user/role/{role}")
    @Operation(summary = "Get employees by role", description = "Returns a list of employees who have the specified role")
    public ResponseEntity<List<EmployeesResponse>> getEmployeesByRole(@PathVariable String role) {
        try {
            log.info("Get employees by their roles: {}", role);
            List<EmployeesResponse> response = employeeService.getEmployeeByRole(role);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error retrieving Employee by role: {}", role, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("update/{id}")
    @Operation(summary = "Update employee details", description = "Updates employee details by employee ID")
    public ResponseEntity<EmployeeResponse> updateEmployee(@PathVariable Long id, @RequestBody EmployeeRequest employeeRequest) {
        try {
            log.info("Updating Employee with request: {}", employeeRequest);
            EmployeeResponse response = employeeService.updateEmployee(id, employeeRequest);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error updating Employee with Request: {}", employeeRequest);
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("delete/{id}")
    @Operation(summary = "Delete an employee", description = "Deletes the employee with the given ID")
    public ResponseEntity<EmployeeResponse> deleteEmployee(@PathVariable Long id) {
        try {
            log.info("Deleting Employee with ID: {}", id);
            EmployeeResponse response = employeeService.deleteEmployee(id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error Deleting Employee: {}", id, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("status/{id}")
    @Operation(summary = "Update employee active status", description = "Updates the active status (enabled/disabled) of an employee")
    public ResponseEntity<EmployeeResponse> updateEmployeeStatus(@PathVariable Long id, @RequestBody EmployeeRequest employeeRequest) {
        try {
            log.info("Update Active Status with Employee id:{}", id);
            EmployeeResponse response = employeeService.updateEmployeeStatus(id, employeeRequest);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error Updating Active Status with Employee id:{}", id, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/analytics/{departmentId}")
    @Operation(summary = "Get department analytics", description = "Headcount and average salary by department name")
    public ResponseEntity<DepartmentAnalyticsResponse> getAnalytics(@PathVariable Long departmentId) {
        try {
            DepartmentAnalyticsResponse response = employeeService.getDepartmentAnalytics(departmentId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error getting analytics for department: {}", departmentId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/paginated")
    @Operation(summary = "Get paginated and sorted list of employees")
    public ResponseEntity<Page<EmployeeResponse>> getPaginatedEmployees(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        Page<EmployeeResponse> result = employeeService.getEmployees(page, size, sortBy, direction);
        return ResponseEntity.ok(result);
    }


}