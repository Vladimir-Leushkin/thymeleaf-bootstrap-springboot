package com.thymeleaf.bootstrap.springboot.controller;

import com.thymeleaf.bootstrap.springboot.model.Employee;
import com.thymeleaf.bootstrap.springboot.repository.EmployeeRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Validated
@Controller
@RequiredArgsConstructor
//@Tag(name = "Пользователи системы", description = "Валидация на уровне контроллера")
public class EmployeeController {

    private final EmployeeRepository eRepo;

    @GetMapping({ "/list" })
    public ModelAndView getAllEmployees(
		    @RequestParam(defaultValue = "1")
		    int page,
		    @RequestParam(defaultValue = "10")
		    int size) {
	Pageable pageable = PageRequest.of(page - 1, size);
	Page<Employee> employees = eRepo.findAll(pageable);
	ModelAndView mav = new ModelAndView("list-employees");
	mav.addObject("employees", employees.getContent());
	mav.addObject("currentPage", page);
	mav.addObject("totalItems", employees.getTotalElements());
	mav.addObject("totalPages", employees.getTotalPages());
	mav.addObject("pageSize", size);
	return mav;
    }

    @GetMapping("/addEmployeeForm")
    public ModelAndView addEmployeeForm() {
	ModelAndView mav = new ModelAndView("add-employee-form");
	Employee newEmployee = new Employee();
	mav.addObject("employee", newEmployee);
	return mav;
    }

    @PostMapping("/saveEmployee")
    public String saveEmployee(
		    @ModelAttribute
		    Employee employee) {
	eRepo.save(employee);
	return "redirect:/list";
    }

    @GetMapping("/showUpdateForm")
    public ModelAndView showUpdateForm(
		    @RequestParam
		    Long employeeId) {
	ModelAndView mav = new ModelAndView("add-employee-form");
	Employee employee = eRepo.findById(employeeId).get();
	mav.addObject("employee", employee);
	return mav;
    }

    @GetMapping("/deleteEmployee")
    public String deleteEmployee(
		    @RequestParam
		    Long employeeId) {
	eRepo.deleteById(employeeId);
	return "redirect:/list";
    }

    @GetMapping(value = "/search")
    public String search(@Valid @Pattern(regexp = "^\\d{4}-\\d{7}-\\d{8}-\\d{8}/\\d{2}$", message = "Неверный формат запроса") @NotBlank
    @RequestParam
    String name, Model model) {
	if (name != null && !name.isEmpty()) {
	    Optional<Employee> employeeOptional = eRepo.findEmployeesByName(name);
	    if (employeeOptional.isPresent()) {
		model.addAttribute("employee", employeeOptional.get());
		return "redirect:/showUpdateForm/" + employeeOptional.get().getId();
	    }
	}
	model.addAttribute("emptyResponse", "К сожалению по вашему запросу ничего не найдено");
	return "index";
    }
}