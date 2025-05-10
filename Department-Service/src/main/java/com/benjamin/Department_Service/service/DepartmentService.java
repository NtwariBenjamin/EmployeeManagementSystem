package com.benjamin.Department_Service.service;

import com.benjamin.Department_Service.Dto.Department.DepartmentRequest;
import com.benjamin.Department_Service.Dto.Department.DepartmentResponse;
import com.benjamin.Department_Service.model.Department;

import java.util.List;

public interface DepartmentService {

    DepartmentResponse createDepartment(DepartmentRequest departmentRequest);

    DepartmentResponse getDepartmentByName(String departmentName);

    DepartmentResponse getDepartmentById(Long departmentId);

    List<Department> getAllDepartments();

    DepartmentResponse updateDepartment(String departmentName, DepartmentRequest departmentRequest);

    DepartmentResponse deleteDepartmentById(Long departmentId);
}
