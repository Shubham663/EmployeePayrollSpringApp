package com.bridgelabz.employeepayroll.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.employeepayroll.model.EmployeePayroll;
import com.bridgelabz.employeepayroll.service.IEmployeeService;

@RestController
@RequestMapping("/hello")
public class EmployeeController {

	
	@Autowired
	private IEmployeeService employeeService;
		
	@PostMapping(value= "/post")
	public void addEmployee(@RequestBody EmployeePayroll employeePayroll) {
		System.out.println("Inside Controller");
		employeeService.addEmployee(employeePayroll);
	}
	

	@DeleteMapping("/delete")
	public void delete(@RequestParam(value = "id")Long id) {
		System.out.println("Deleting using id: " + id);
		employeeService.deleteById(id);
	}
	
	@PutMapping("/put/{name}")
	public void update(@RequestParam(value = "id")Long id,@PathVariable String name) {
		System.out.println("Updating using id: " + id);
		employeeService.updateById(id,name);
	}
	
	@GetMapping("/get")
	public String returnString(@RequestParam(value = "id",defaultValue = "1")Long id ){
		return employeeService.getByID(id);
	}
}
