package com.bridgelabz.employeepayroll.service;

import java.util.List;
import java.util.stream.Stream;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.bridgelabz.employeepayroll.dto.CredentialsDto;
import com.bridgelabz.employeepayroll.dto.EmployeeDto;
import com.bridgelabz.employeepayroll.exception.CredentialsException;
import com.bridgelabz.employeepayroll.exception.EmployeeException;
import com.bridgelabz.employeepayroll.model.Credentials;
import com.bridgelabz.employeepayroll.model.EmployeePayroll;
import com.bridgelabz.employeepayroll.repository.CredentialsRepo;
import com.bridgelabz.employeepayroll.repository.IEmployeeRepository;
import com.bridgelabz.employeepayroll.utility.Response;
import com.bridgelabz.employeepayroll.utility.TokenHelper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmployeeService implements IEmployeeService{
	
	@Autowired
	private IEmployeeRepository empRepository;
	
	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private Environment environment;
	
	@Autowired
	private TokenHelper tokenHelper;
	
	@Autowired
	private CredentialsRepo credentialsRepo;
	
	/**
	 *Method for adding the employee to the employee database
	 */
	@Override
	public Response addEmployee(EmployeeDto employeePayrollDto) {
		empRepository.findByEmail(employeePayrollDto.getEmail()).ifPresent((emp) -> {throw new EmployeeException("The employee with this email already exists in the database");});
		Stream.of(employeePayrollDto).map((emp) -> mapper.map(emp, EmployeePayroll.class)).forEach(emp -> empRepository.save(emp));
		log.info("Employee " + employeePayrollDto + " added to repository");
		Response response = new Response(200, "Employee Data added susscesfully",employeePayrollDto);
		return response;
	}

	/**
	 *Deletes the employee with specified id
	 */
	@Override
	public Response deleteByEmail(String email) {
		Integer k = empRepository.deleteByEmail(email);
		if(k.intValue() == 0)
			throw new EmployeeException("Employee with specified email is not present and therefor cannot be deleted");
		log.info("Employee deleted successfully");
		return new Response(200,"Employee deleted successfully",null);
	}

	/**
	 *Fetches the employee with specified email.
	 */
	@Override
	public Response getByEmail(String email) {
		EmployeePayroll employeePayroll = empRepository.findByEmail(email).orElseThrow(() -> new EmployeeException("Employee Object having email " + email +"not found"));
		log.info("Employee with email " + email + " fetched successfully");
		return new Response(200, "Employee fetched successfully", mapper.map(employeePayroll,EmployeeDto.class));
	}

	/**
	 *Fetches all the employees.
	 */
	@Override
	public Response getList() {
		List<EmployeePayroll> employeePayrolls = empRepository.getSorted()
												.orElseThrow(() -> new EmployeeException("The list of emloyees was not fetched correctly"));
		log.info("The list of employees fetched correctly");
		return new Response(200, "List of Employees fetched successfully", employeePayrolls);
	}

	/**
	 *Updates the employee with the specified email
	 */
	@Override
	public Response updateByEmail(EmployeeDto employeeDto) {
		EmployeePayroll employeePayroll = empRepository.findByEmail(employeeDto.getEmail()).orElseThrow(() -> new EmployeeException("The employee with the given Email does not exist and therefore cannot be updated"));
		employeePayroll.setEmployeeName(employeeDto.getEmployeeName());
		employeePayroll.setSalary(employeeDto.getSalary());
		employeePayroll.setEmail(employeeDto.getEmail());
		empRepository.save(employeePayroll);
		log.info("The employee with email " + employeeDto.getEmail() + " updated successfully");
		Response response = new Response(200, "Employee Data updated susscesfully",employeeDto);
		return response;
	}

	/**
	 *Fetches all the employees whose salary is greater than average
	 */
	@Override
	public Response getSalaryAboveAverage() {
		EmployeePayroll[] employeesWithSalaryGreaterThanAverage = empRepository.getAboveAverage();
		if(employeesWithSalaryGreaterThanAverage.length == 0)
			return new Response(200, "No employees with such salaries present", employeesWithSalaryGreaterThanAverage);
		log.info("The employees with salary above average fetched correctly");
		return new Response(200,"The employees with salary above average fetched correctly",employeesWithSalaryGreaterThanAverage);
		
	}
	
	public Response check(CredentialsDto credentials) {
		Credentials credentials2 = credentialsRepo.findByEmail(credentials.getEmail()).orElseThrow(() -> new CredentialsException("Invalid Credentials. Password or Username invalid"));
		log.info(credentials.toString());
		String token = tokenHelper.createJWT(credentials2.getId().toString(), environment.getProperty("token.issuer"), environment.getProperty("token.subject"), Long.parseLong(environment.getProperty("token.expirationTime"))); 
		CredentialsDto dto = mapper.map(credentials2, CredentialsDto.class);
		if(credentials.equals(dto))	
			return new Response(200, "Successfully Logged in", token);
		else 
			return new Response(400, "Invalid Credentials. Password or Username invalid", "Please provide valid credentials");
	}
	
	public Response addCredentials(CredentialsDto credentials) {
		credentialsRepo.findByEmail(credentials.getEmail()).ifPresent((emp) -> {throw new CredentialsException("Invalid Credentials. Password or Username invalid");});
		Credentials credentials3 = mapper.map(credentials, Credentials.class);
		credentialsRepo.save(credentials3);
		log.info("Successfully signed up");
		return new Response(200, "The credentials were added successfully", credentials);
	}

	@Override
	public Response getEmployeeById(Long id) {
		EmployeePayroll employeePayrolls = empRepository.findById(id).orElseThrow(() -> new EmployeeException("The list of emloyees was not fetched correctly"));
		EmployeeDto employeeDto = mapper.map(employeePayrolls, EmployeeDto.class);
		log.info("The employee fetched correctly");
		return new Response(200, "Employee fetched successfully", employeeDto);
		}
}