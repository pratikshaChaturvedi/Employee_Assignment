package com.cognizant.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cognizant.model.Employee;
import com.cognizant.utility.Utility;

@RestController
@RequestMapping(path = "/employee")
public class EmployeeController {

	@GetMapping(path = "/all")
	public List<Employee> getAllEmployees() {

		List<Employee> employeeList = null;
		try {
			File file = ResourceUtils.getFile("classpath:abc.csv");
			employeeList = Utility.getEmployeeList(file);
		} catch (IOException e) {
			System.out.println("File not found");
			e.printStackTrace();
		}
		System.out.println("<---------Completed Successfully--------->");
		return employeeList;
	}

	@GetMapping(path = "firstname/{fname}")
	public List<Employee> getEmployee(@PathVariable String fname) {

		List<Employee> employeeList = null;
		try {
			File file = ResourceUtils.getFile("classpath:abc.csv");
			employeeList = Utility.getEmployeeList(file);
		} catch (IOException e) {
			System.out.println("File not found");
			e.printStackTrace();
		}
		List<Employee> filteredEmployee = new ArrayList<Employee>();
		employeeList.stream().forEach(emp -> {
			if (emp.getFirstName().equals(fname))
				filteredEmployee.add(emp);
		});
		System.out.println("<---------Completed Successfully--------->");
		return filteredEmployee;
	}

}
