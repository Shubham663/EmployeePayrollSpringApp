package com.bridgelabz.employeepayroll.service;

import com.bridgelabz.employeepayroll.dto.CredentialsDto;
import com.bridgelabz.employeepayroll.dto.EmployeeDto;
import com.bridgelabz.employeepayroll.utility.Response;

public interface IEmployeeService {
	public Response addEmployee(EmployeeDto employeePayrollDto);

	public Response deleteByEmail(String email);

	public Response getByEmail(String email);

	public Response getList();

	public Response updateByEmail(EmployeeDto employeeDto);

	public Response getSalaryAboveAverage();

	public Response check(com.bridgelabz.employeepayroll.dto.CredentialsDto credentials);
	
	public Response addCredentials(CredentialsDto credentials);

	public Response getEmployeeById(Long id);
}
