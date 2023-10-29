package com.thymeleaf.bootstrap.springboot.repository;

import com.thymeleaf.bootstrap.springboot.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>{

    Optional<Employee> findEmployeesByName(String name);

}
