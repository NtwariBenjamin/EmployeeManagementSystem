package com.benjamin.Employee_Service.service;

import com.benjamin.Employee_Service.dto.performance.PerformanceRequest;
import com.benjamin.Employee_Service.dto.performance.PerformanceResponse;

import java.util.List;

public interface PerformanceService {

    PerformanceResponse addPerformanceReview(PerformanceRequest performanceRequest);

    List<PerformanceResponse> getPerformanceByEmployee(Long employeeId);

    List<PerformanceResponse> getAllPerformances();

    PerformanceResponse updatePerformanceReview(Long id, PerformanceRequest performanceRequest);

    void deletePerformanceReview(Long id);

    Double getAverageScoreByDepartment(Long departmentId);
}

