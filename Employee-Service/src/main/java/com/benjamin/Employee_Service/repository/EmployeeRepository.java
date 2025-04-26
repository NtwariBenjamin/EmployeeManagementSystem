package com.benjamin.Employee_Service.repository;

import com.benjamin.Employee_Service.model.employee.Employee;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee,Long> {
    List<Employee> findByDepartmentId(Long departmentId);
    Page<Employee> findAll(Pageable pageable);

}
