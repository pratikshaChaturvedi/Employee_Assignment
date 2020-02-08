package com.cognizant.utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

import com.cognizant.model.Employee;

public class Utility {

	// Checks the country domain

	public static boolean checkCountryDomain(String email, Employee employee) {
		boolean flag = false;
		String[] empEmail = email.split("\\.");
		String last = empEmail[empEmail.length - 1];
		if (!last.equals("com")) {
			flag = true;
		}
		return flag;
	}

	// Return the employee list from the document

	public static List<Employee> getEmployeeList(File file) throws IOException {
		List<Employee> employeeList = new ArrayList<>();
		BufferedReader br;
		try {
			Employee employee = null;
			br = new BufferedReader(new FileReader(file));
			String st;
			String[] employeeDetails = null;
			br.readLine();
			while ((st = br.readLine()) != null) {
				employeeDetails = st.split(",");
				employee = new Employee(employeeDetails[0], employeeDetails[1], employeeDetails[2], employeeDetails[3],
						employeeDetails[4], employeeDetails[5],
						Float.parseFloat((employeeDetails[6] + employeeDetails[7]).substring(2).replace(" \"", "")),
						employeeDetails[8], employeeDetails[9], employeeDetails[10], employeeDetails[11],
						employeeDetails[12]);
				employee.setCountryDomain(Utility.checkCountryDomain(employeeDetails[3], employee));
				employeeList.add(employee);

			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return employeeList;
	}

	// To print every employee details in a tabular form into a text file

	public static void printData(Collection<Employee> employeeList) throws IOException {

		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter limit: ");
		int limit = scanner.nextInt();
		scanner.close();
		System.out.println(String.format("%1s %30s %30s %30s %30s %80s \r\n", "Employee Id", "Employee Name", "Gender",
				"Department Name", "Salary", "Address"));
		employeeList.stream().limit(limit).forEach(employee -> {
			System.out.println(String.format("%1s %30s %30s %30s %30s %80s", employee.getEmployeeId(),
					employee.getFirstName() + " " + employee.getLastName(), employee.getGender(),
					employee.getDepartmentName(), employee.getSalary(),
					employee.getStreetAddress() + ", " + employee.getCity() + ", " + employee.getState()));
		});
		
	}

	public static void getEmployeesByDepartment(List<Employee> employeeList) {
		Map<String, Long> departmentCountingMap = employeeList.stream()
				.collect(Collectors.groupingBy(Employee::getDepartmentName, Collectors.counting()));
		System.out.println("Number of employees by department \n");

		departmentCountingMap.entrySet().stream().forEach(entry -> System.out
				.println("Department = " + entry.getKey() + " , No of Employees = " + entry.getValue()));

	}

	public static void getEmployeesByMaxAndMin(List<Employee> employeeList) {

		System.out.println();

		Map<String, Optional<Employee>> topEmployee = employeeList.stream().collect(Collectors.groupingBy(
				emp -> emp.getDepartmentName(), Collectors.maxBy(Comparator.comparingDouble(emp -> emp.getSalary()))));

		System.out.println("Department wise maximum salary \n");

		topEmployee.entrySet().stream().forEach(entry -> System.out
				.println("Department = " + entry.getKey() + " , Employee Details = " + entry.getValue().get()));
		System.out.println();

		Map<String, Optional<Employee>> bottomEmployee = employeeList.stream().collect(Collectors.groupingBy(
				emp -> emp.getDepartmentName(), Collectors.minBy(Comparator.comparingDouble(emp -> emp.getSalary()))));

		System.out.println("Department wise minimum salary \n");
		bottomEmployee.entrySet().stream().forEach(entry -> System.out
				.println("Department = " + entry.getKey() + " , Employee Details = " + entry.getValue().get()));
		
		System.out.println();
		
	}

	public static void getAverageSalary(List<Employee> employeeList) {
		Map<String, Double> averageSalary = employeeList.stream().collect(Collectors
				.groupingBy(emp -> emp.getDepartmentName(), Collectors.averagingDouble(emp -> emp.getSalary())));

		Map<String, Double> result = averageSalary.entrySet().stream().sorted(Map.Entry.comparingByValue())
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue,
						LinkedHashMap::new));

		System.out.println("Department wise average salary \n");
		result.entrySet().stream().forEach(entry -> {
			System.out.println("Department = " + entry.getKey() + " , Average Salary = " + entry.getValue());
		});
	}
}
