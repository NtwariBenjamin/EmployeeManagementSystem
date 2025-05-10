package com.benjamin.Department_Service.service;

import com.benjamin.Department_Service.model.Department;
import com.benjamin.Department_Service.Dto.Department.DepartmentRequest;
import com.benjamin.Department_Service.Dto.Department.DepartmentResponse;
import com.benjamin.Department_Service.repository.DepartmentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.benjamin.Department_Service.exception.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class DepartmentServiceImpl implements DepartmentService{

    @Autowired
    private DepartmentRepository departmentRepository;

    @Override
    public DepartmentResponse createDepartment(DepartmentRequest departmentRequest) {
        Optional<Department> departmentOptional = departmentRepository.findByName(departmentRequest.getName());
        if (departmentOptional.isPresent()) {
            return DepartmentResponse.builder()
                    .department(departmentOptional.get())
                    .message("Department is Already Created!")
                    .build();
        }
        Department department = Department.builder()
                .name(departmentRequest.getName())
                .managerId(departmentRequest.getManagerId())
                .location(departmentRequest.getLocation())
                .build();
        departmentRepository.save(department);
        return DepartmentResponse.builder()
                .department(department)
                .message(departmentRequest.getName() + " Department Created Successfully!")
                .build();
    }

    @Override
    public DepartmentResponse getDepartmentByName(String departmentName) {
        Department department = findDepartmentByNameOrThrow(departmentName);
        return DepartmentResponse.builder()
                .department(department)
                .message("Department found successfully")
                .build();
    }

    @Override
    public DepartmentResponse getDepartmentById(Long departmentId) {
        Department department = findDepartmentByIdOrThrow(departmentId);
        return DepartmentResponse.builder()
                .department(department)
                .message(department.getName() + " Department is Found!")
                .build();
    }

    @Override
    public List<Department> getAllDepartments() {
        log.info("Fetching All Departments!");
        return departmentRepository.findAll();
    }

    @Override
    public DepartmentResponse updateDepartment(String departmentName, DepartmentRequest departmentRequest) {
        Department updatedDepartment = findDepartmentByNameOrThrow(departmentName);
        updatedDepartment.setName(departmentRequest.getName());
        updatedDepartment.setManagerId(departmentRequest.getManagerId());
        updatedDepartment.setLocation(departmentRequest.getLocation());
        departmentRepository.save(updatedDepartment);
        return DepartmentResponse.builder()
                .department(updatedDepartment)
                .message("Department Updated Successfully!")
                .build();
    }

    @Override
    public DepartmentResponse deleteDepartmentById(Long departmentId) {
        Department department = findDepartmentByIdOrThrow(departmentId);
        departmentRepository.deleteById(departmentId);
        return DepartmentResponse.builder()
                .department(null)
                .message("Department with ID: " + departmentId + " Deleted Successfully!")
                .build();
    }

    private Department findDepartmentByNameOrThrow(String departmentName) {
        return departmentRepository.findByName(departmentName)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with name: " + departmentName));
    }

    private Department findDepartmentByIdOrThrow(Long departmentId) {
        return departmentRepository.findById(departmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with ID: " + departmentId));
    }
}
