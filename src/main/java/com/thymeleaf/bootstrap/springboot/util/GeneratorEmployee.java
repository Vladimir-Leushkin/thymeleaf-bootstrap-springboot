package com.thymeleaf.bootstrap.springboot.util;

import com.thymeleaf.bootstrap.springboot.model.Employee;
import com.thymeleaf.bootstrap.springboot.repository.EmployeeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GeneratorEmployee {

    private final EmployeeRepository repository;

    @Transactional
    @EventListener
    public void generate(ContextRefreshedEvent event){
	final List<Employee> employees = new ArrayList<>();
	for (int i = 1; i < 101; i++){
	    final Employee employee = new Employee();
	    employee.setId(i);
	    employee.setDepartment("IT");
	    employee.setName("Bob" + i);
	    employee.setEmail("bob-" + i + "@com");
	    employees.add(employee);
	}
	repository.saveAll(employees);
    }
}
