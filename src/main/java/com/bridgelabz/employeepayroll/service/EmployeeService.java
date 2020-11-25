package com.bridgelabz.employeepayroll.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bridgelabz.employeepayroll.model.EmployeePayroll;
import com.bridgelabz.employeepayroll.repository.IEmployeeRepository;

@Service
public class EmployeeService implements IEmployeeService{
	
	@Autowired
	private IEmployeeRepository empRepository;
	
	@Override
	public void addEmployee(EmployeePayroll employeePayroll) {
		empRepository.save(employeePayroll);
		System.out.println("Employee " + employeePayroll + " added to repository");
	}

	@Override
	public String getByID(Long id) {
		return empRepository.findById(id).get().toString();
	}

	@Override
	public void deleteById(Long id) {
		empRepository.deleteById(id);
	}

	@Override
	public void updateById(Long id,String name) {
		EmployeePayroll employeePayroll = empRepository.findById(id).get();
		employeePayroll.setEmployeeName(name);
		empRepository.save(employeePayroll);
		
	}
}
