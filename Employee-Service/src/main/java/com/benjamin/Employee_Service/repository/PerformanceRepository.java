package com.benjamin.Employee_Service.repository;

import com.benjamin.Employee_Service.model.performance.Performance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PerformanceRepository extends JpaRepository<Performance, Long> {

    List<Performance> findByEmployeeId(Long employeeId);

    List<Performance> findByReviewPeriod(String reviewPeriod);
}

