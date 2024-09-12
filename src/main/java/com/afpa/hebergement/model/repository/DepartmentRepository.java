package com.afpa.hebergement.model.repository;

import com.afpa.hebergement.model.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, Integer> {
    Optional<Department> findByDepartmentCode(String departmentCode);
}
