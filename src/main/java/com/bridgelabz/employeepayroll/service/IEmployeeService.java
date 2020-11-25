package com.bridgelabz.employeepayroll.service;

import com.bridgelabz.employeepayroll.model.EmployeePayroll;

public interface IEmployeeService {
	public void addEmployee(EmployeePayroll employeePayroll);

	public String getByID(Long id);

	public void deleteById(Long id);

	public void updateById(Long id, String name);
}
