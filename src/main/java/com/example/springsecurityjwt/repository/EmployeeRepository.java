package com.example.springsecurityjwt.repository;




import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.springsecurityjwt.model.Employee;


@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

	List<Employee> findAll();
}
