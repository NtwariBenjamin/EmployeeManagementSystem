package com.benjamin.Employee_Service.service;

import com.benjamin.Employee_Service.dto.department.DepartmentAnalyticsResponse;
import com.benjamin.Employee_Service.model.department.Department;
import com.benjamin.Employee_Service.model.employee.EmployeeRequest;
import com.benjamin.Employee_Service.model.employee.EmployeeResponse;
import com.benjamin.Employee_Service.model.employee.EmployeesResponse;
import com.benjamin.Employee_Service.model.user.User;
import org.springframework.data.domain.Page;

import java.util.List;

public interface EmployeeService {
    EmployeeResponse registerEmployee(User user, Department department, EmployeeRequest employeeRequest);

    List<EmployeesResponse> allEmployees();

    List<EmployeesResponse> getEmployeesByDepartment(String departmentName);

    EmployeesResponse getEmployeeByUsername(String username);

    List<EmployeesResponse> getEmployeeByRole(String role);

    EmployeeResponse updateEmployee(Long id, EmployeeRequest employeeRequest);

    EmployeeResponse deleteEmployee(Long id);

    EmployeeResponse updateEmployeeStatus(Long id, EmployeeRequest employeeRequest);

    DepartmentAnalyticsResponse getDepartmentAnalytics(Long departmentId);

    Page<EmployeeResponse> getEmployees(int page, int size, String sortBy, String direction);

    EmployeeResponse getEmployeeById(Long id);

}
