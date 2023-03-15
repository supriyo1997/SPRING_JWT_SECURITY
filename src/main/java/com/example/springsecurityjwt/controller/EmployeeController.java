package com.example.springsecurityjwt.controller;





import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.springsecurityjwt.model.Employee;
import com.example.springsecurityjwt.repository.EmployeeRepository;
import com.example.springsecurityjwt.security.CurrentUser;
import com.example.springsecurityjwt.security.UserPrincipal;





@CrossOrigin("*")
@RestController
@RequestMapping("/api/crud")
public class EmployeeController {

	@Autowired
	EmployeeRepository empRep;
	
	@GetMapping("/employees")
	public List<Employee> findAll()
	{
		System.out.println("in emoloyee controller");
		return empRep.findAll();
	}
	
	@PostMapping("/employees")
	public Employee createEmployee(@RequestBody Employee emp)
	{
		return empRep.save(emp);
	}
	
	
}
