package com.benjamin.Employee_Service.service;


import com.benjamin.Employee_Service.dto.department.DepartmentAnalyticsResponse;
import com.benjamin.Employee_Service.model.department.Department;
import com.benjamin.Employee_Service.model.employee.Employee;
import com.benjamin.Employee_Service.model.employee.EmployeeRequest;
import com.benjamin.Employee_Service.model.employee.EmployeeResponse;
import com.benjamin.Employee_Service.model.employee.EmployeesResponse;
import com.benjamin.Employee_Service.model.user.User;
import com.benjamin.Employee_Service.repository.EmployeeRepository;
import com.benjamin.Employee_Service.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class EmployeeServiceImpl implements EmployeeService{

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private DepartmentClient departmentClient;

    @Autowired
    private AuthClient authClient;

    @Override
    public EmployeeResponse registerEmployee(User user, Department department, EmployeeRequest employeeRequest) {
        Employee employee = Employee.builder()
                .name(employeeRequest.getName())
                .email(employeeRequest.getEmail())
                .departmentId(department.getId())
                .userId(user.getId())
                .hireDate(LocalDate.now())
                .salary(employeeRequest.getSalary())
                .isActive(employeeRequest.getIsActive())
                .build();
        employeeRepository.save(employee);
        return EmployeeResponse.builder()
                .employee(employee)
                .message("Employee registered successfully!")
                .build();
    }

    @Override
    public List<EmployeesResponse> allEmployees() {
        return employeeRepository.findAll().stream()
                .map(this::buildEmployeesResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<EmployeesResponse> getEmployeesByDepartment(String departmentName) {
        return employeeRepository.findAll().stream()
                .filter(emp -> departmentName.equals(departmentClient.getDepartmentDetails(emp.getDepartmentId()).getName()))
                .map(this::buildEmployeesResponse)
                .collect(Collectors.toList());
    }

    @Override
    public EmployeesResponse getEmployeeByUsername(String username) {
        return employeeRepository.findAll().stream()
                .map(this::buildEmployeesResponse)
                .filter(resp -> username.equals(resp.getUsername()))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<EmployeesResponse> getEmployeeByRole(String role) {
        return employeeRepository.findAll().stream()
                .map(this::buildEmployeesResponse)
                .filter(resp -> role.equals(resp.getRole()))
                .collect(Collectors.toList());
    }

    @Override
    public EmployeeResponse updateEmployee(Long id, EmployeeRequest employeeRequest) {
        Employee employee = findEmployeeByIdOrThrow(id);

        employee.setName(employeeRequest.getName());
        employee.setEmail(employeeRequest.getEmail());
        employee.setDepartmentId(employeeRequest.getDepartmentId());
        employee.setUserId(employeeRequest.getUserId());
        employee.setSalary(employeeRequest.getSalary());
        employee.setHireDate(employeeRequest.getHireDate());
        employee.setIsActive(employeeRequest.getIsActive());

        employeeRepository.save(employee);

        return EmployeeResponse.builder()
                .employee(employee)
                .message("Employee Updated Successfully!")
                .build();
    }

    @Override
    public EmployeeResponse deleteEmployee(Long id) {
        Employee employee = findEmployeeByIdOrThrow(id);
        employeeRepository.delete(employee);

        return EmployeeResponse.builder()
                .employee(null)
                .message("Employee Deleted Successfully!")
                .build();
    }

    @Override
    public EmployeeResponse updateEmployeeStatus(Long id, EmployeeRequest employeeRequest) {
        Employee employee = findEmployeeByIdOrThrow(id);
        employee.setIsActive(employeeRequest.getIsActive());
        employeeRepository.save(employee);

        return EmployeeResponse.builder()
                .employee(employee)
                .message("Employee Status Updated Successfully!")
                .build();
    }

    @Override
    public DepartmentAnalyticsResponse getDepartmentAnalytics(Long departmentId) {
        List<Employee> employees = employeeRepository.findByDepartmentId(departmentId);

        if (employees.isEmpty()) {
            throw new RuntimeException("No employees found in department " + departmentId);
        }

        int headcount = employees.size();
        double avgSalary = employees.stream()
                .map(Employee::getSalary)
                .mapToDouble(Double::parseDouble)
                .average()
                .orElse(0.0);

        return DepartmentAnalyticsResponse.builder()
                .departmentName(departmentClient.getDepartmentDetails(departmentId).getName())
                .departmentId(departmentId)
                .headcount(headcount)
                .averageSalary(avgSalary)
                .build();
    }

    @Override
    public Page<EmployeeResponse> getEmployees(int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return employeeRepository.findAll(pageable)
                .map(this::mapToEmployeeResponse);
    }

    @Override
    public EmployeeResponse getEmployeeById(Long id) {
        Employee employee = findEmployeeByIdOrThrow(id);
        return EmployeeResponse.builder()
                .employee(employee)
                .message("Employee found successfully!")
                .build();
    }

    private Employee findEmployeeByIdOrThrow(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
    }

    private EmployeesResponse buildEmployeesResponse(Employee employee) {
        Department department = departmentClient.getDepartmentDetails(employee.getDepartmentId());
        User user = authClient.getUserDetails(employee.getUserId());

        return new EmployeesResponse(
                employee.getName(),
                user.getUsername(),
                employee.getEmail(),
                String.valueOf(user.getRole()),
                employee.getSalary(),
                department.getName(),
                employee.getHireDate(),
                employee.getIsActive()
        );
    }

    private EmployeeResponse mapToEmployeeResponse(Employee employee) {
        return EmployeeResponse.builder()
                .employee(employee)
                .message("")
                .build();
    }
}
