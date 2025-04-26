package com.benjamin.Employee_Service.service;

import com.benjamin.Employee_Service.model.department.Department;
import com.benjamin.Employee_Service.model.department.DepartmentAnalyticsResponse;
import com.benjamin.Employee_Service.model.employee.Employee;
import com.benjamin.Employee_Service.model.employee.EmployeeRequest;
import com.benjamin.Employee_Service.model.employee.EmployeeResponse;
import com.benjamin.Employee_Service.model.employee.EmployeesResponse;
import com.benjamin.Employee_Service.model.user.User;
import com.benjamin.Employee_Service.repository.EmployeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private DepartmentClient departmentClient;
    @Autowired
    private AuthClient authClient;

    public EmployeeResponse registerEmployee(User user, Department department, EmployeeRequest employeeRequest) {
        Employee employee=Employee.builder()
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

    public List<EmployeesResponse> allEmployees() {
        List<Employee> employees=employeeRepository.findAll();
        List<EmployeesResponse> responseList=new ArrayList<>();

        for(Employee employee:employees){
            Department department=departmentClient.getDepartmentDetails(employee.getDepartmentId());
            User user=authClient.getUserDetails(employee.getUserId());
            EmployeesResponse response=new EmployeesResponse(employee.getName(),user.getUsername(),
                    employee.getEmail(),String.valueOf(user.getRole()),employee.getSalary(),department.getName(),
                    employee.getHireDate(),employee.getIsActive());
            responseList.add(response);
        }
        return responseList;



    }

    public List<EmployeesResponse> getEmployeesByDepartment(String departmentName) {
        List<Employee> employees=employeeRepository.findAll();
        List<EmployeesResponse> responseList=new ArrayList<>();
        for (Employee employee:employees){
            Department department=departmentClient.getDepartmentDetails(employee.getDepartmentId());
            User user=authClient.getUserDetails(employee.getUserId());
            if (!departmentName.isEmpty() && departmentName.equals(department.getName())){
                EmployeesResponse response=new EmployeesResponse(employee.getName(),user.getUsername(),
                        employee.getEmail(),String.valueOf(user.getRole()),employee.getSalary(),department.getName(),
                        employee.getHireDate(),employee.getIsActive());
                responseList.add(response);
            }
        }
        return responseList;

    }

    public EmployeesResponse getEmployeeByUsername(String username) {
        List<Employee> employees=employeeRepository.findAll();
        EmployeesResponse response = null;
        for (Employee employee:employees){
            Department department=departmentClient.getDepartmentDetails(employee.getDepartmentId());
            User user=authClient.getUserDetails(employee.getUserId());
            if (username.equals(user.getUsername())){
                response=new EmployeesResponse(employee.getName(),user.getUsername(),
                        employee.getEmail(),String.valueOf(user.getRole()),employee.getSalary(),department.getName(),
                        employee.getHireDate(),employee.getIsActive());
            }
        }
        return response;
    }

    public List<EmployeesResponse> getEmployeeByRole(String role) {
        List<Employee> employees=employeeRepository.findAll();
        List<EmployeesResponse> responseList=new ArrayList<>();

        for (Employee employee:employees){
            Department department=departmentClient.getDepartmentDetails(employee.getDepartmentId());
            User user=authClient.getUserDetails(employee.getUserId());
            if (role.equals(String.valueOf(user.getRole()))){
                EmployeesResponse response=new EmployeesResponse(employee.getName(),user.getUsername(),
                        employee.getEmail(),String.valueOf(user.getRole()),employee.getSalary(),department.getName(),
                        employee.getHireDate(),employee.getIsActive());
                responseList.add(response);
            }
        }
        return responseList;
    }

    public EmployeeResponse updateEmployee(Long id, EmployeeRequest employeeRequest) {
        Optional<Employee> employeeOptional=employeeRepository.findById(id);
        if (employeeOptional.isEmpty()){
            return EmployeeResponse.builder()
                    .employee(null)
                    .message("Employee Not Found!")
                    .build();
        }
        Employee updatedEmployee=employeeOptional.get();
        updatedEmployee.setName(employeeRequest.getName());
        updatedEmployee.setEmail(employeeRequest.getEmail());
        updatedEmployee.setDepartmentId(employeeRequest.getDepartmentId());
        updatedEmployee.setUserId(employeeRequest.getUserId());
        updatedEmployee.setSalary(employeeRequest.getSalary());
        updatedEmployee.setHireDate(employeeRequest.getHireDate());
        updatedEmployee.setIsActive(employeeRequest.getIsActive());
        employeeRepository.save(updatedEmployee);
        return EmployeeResponse.builder()
                .employee(updatedEmployee)
                .message("Employee Updated Successfully!")
                .build();
    }

    public EmployeeResponse deleteEmployee(Long id) {
        Optional<Employee> employeeOptional=employeeRepository.findById(id);
        if (employeeOptional.isEmpty()){
            return EmployeeResponse.builder()
                    .employee(null)
                    .message("Employee Not Found!")
                    .build();
        }
        employeeRepository.deleteById(id);
        return EmployeeResponse.builder()
                .employee(null)
                .message("Employee Deleted Successfully!")
                .build();
    }

    public EmployeeResponse updateEmployeeStatus(Long id,EmployeeRequest employeeRequest) {
        Optional<Employee> employeeOptional=employeeRepository.findById(id);
        if (employeeOptional.isEmpty()){
            return EmployeeResponse.builder()
                    .employee(null)
                    .message("Employee Not Found!")
                    .build();
        }
        Employee updateEmployee=employeeOptional.get();
        updateEmployee.setIsActive(employeeRequest.getIsActive());
        return EmployeeResponse.builder()
                .employee(updateEmployee)
                .message("Employee Status Activated Successfully!")
                .build();
    }

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
                .departmentId(employees.get(0).getId())
                .headcount(headcount)
                .averageSalary(avgSalary)
                .build();
    }

    public Page<EmployeeResponse> getEmployees(int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Employee> employeesPage = employeeRepository.findAll(pageable);

        return employeesPage.map(this::mapToResponse);
    }

    private EmployeeResponse mapToResponse(Employee employee) {
        return EmployeeResponse.builder()
                .employee(employee)
                .message("")
                .build();
    }


}
