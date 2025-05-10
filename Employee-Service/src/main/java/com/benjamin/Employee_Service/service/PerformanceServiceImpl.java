package com.benjamin.Employee_Service.service;

import com.benjamin.Employee_Service.dto.performance.PerformanceRequest;
import com.benjamin.Employee_Service.dto.performance.PerformanceResponse;
import com.benjamin.Employee_Service.exception.ResourceNotFoundException;
import com.benjamin.Employee_Service.model.employee.Employee;
import com.benjamin.Employee_Service.model.performance.Performance;
import com.benjamin.Employee_Service.repository.EmployeeRepository;
import com.benjamin.Employee_Service.repository.PerformanceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PerformanceServiceImpl implements PerformanceService {

    @Autowired
    private PerformanceRepository performanceRepository;

    @Autowired
    private EmployeeRepository employeeRepository; // to fetch employee details

    @Autowired
    private DepartmentClient departmentClient; // for department operations

    @Override
    public PerformanceResponse addPerformanceReview(PerformanceRequest request) {
        Employee employee = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + request.getEmployeeId()));

        Performance performance = Performance.builder()
                .employeeId(request.getEmployeeId())
                .reviewPeriod(request.getReviewPeriod())
                .score(request.getScore())
                .comments(request.getComments())
                .reviewDate(LocalDate.now())
                .build();

        performanceRepository.save(performance);

        return mapToPerformanceResponse(performance, employee.getName(), "Performance Review Added Successfully!");
    }

    @Override
    public List<PerformanceResponse> getPerformanceByEmployee(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + employeeId));

        return performanceRepository.findByEmployeeId(employeeId).stream()
                .map(performance -> mapToPerformanceResponse(performance, employee.getName(), ""))
                .collect(Collectors.toList());
    }

    @Override
    public List<PerformanceResponse> getAllPerformances() {
        List<Employee> employees = employeeRepository.findAll();
        Map<Long, String> employeeNameMap = employees.stream()
                .collect(Collectors.toMap(Employee::getId, Employee::getName));

        return performanceRepository.findAll().stream()
                .map(performance -> mapToPerformanceResponse(performance, employeeNameMap.get(performance.getEmployeeId()), ""))
                .collect(Collectors.toList());
    }

    @Override
    public PerformanceResponse updatePerformanceReview(Long id, PerformanceRequest request) {
        Performance performance = performanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Performance review not found with id: " + id));

        performance.setReviewPeriod(request.getReviewPeriod());
        performance.setScore(request.getScore());
        performance.setComments(request.getComments());
        performance.setReviewDate(LocalDate.now());

        performanceRepository.save(performance);

        Employee employee = employeeRepository.findById(performance.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + performance.getEmployeeId()));

        return mapToPerformanceResponse(performance, employee.getName(), "Performance Review Updated Successfully!");
    }

    @Override
    public void deletePerformanceReview(Long id) {
        Performance performance = performanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Performance review not found with id: " + id));
        performanceRepository.delete(performance);
    }

    @Override
    public Double getAverageScoreByDepartment(Long departmentId) {
        List<Employee> employees = employeeRepository.findByDepartmentId(departmentId);

        if (employees.isEmpty()) {
            throw new RuntimeException("No employees found in department " + departmentId);
        }

        List<Long> employeeIds = employees.stream()
                .map(Employee::getId)
                .collect(Collectors.toList());

        List<Performance> performances = performanceRepository.findAll().stream()
                .filter(perf -> employeeIds.contains(perf.getEmployeeId()))
                .collect(Collectors.toList());

        return performances.stream()
                .mapToDouble(Performance::getScore)
                .average()
                .orElse(0.0);
    }

    private PerformanceResponse mapToPerformanceResponse(Performance performance, String employeeName, String message) {
        return PerformanceResponse.builder()
                .id(performance.getId())
                .employeeId(performance.getEmployeeId())
                .employeeName(employeeName)
                .reviewPeriod(performance.getReviewPeriod())
                .score(performance.getScore())
                .comments(performance.getComments())
                .reviewDate(performance.getReviewDate())
                .message(message)
                .build();
    }
}

