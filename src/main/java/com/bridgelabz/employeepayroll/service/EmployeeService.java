package com.bridgelabz.employeepayroll.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.transaction.Transactional;

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
		empRepository.findByEmail(employeePayrollDto.getEmail()).ifPresent((emp) -> {throw new EmployeeException(environment.getProperty("EMPLOYEE_ALREADY_PRESENT"));});
		Stream.of(employeePayrollDto).map((emp) -> mapper.map(emp, EmployeePayroll.class)).forEach(emp -> empRepository.save(emp));
		log.info(environment.getProperty("ADDITION_SUCCESSFULL"));
		Response response = new Response(200, environment.getProperty("ADDITION_SUCCESSFULL"),employeePayrollDto);
		return response;
	}

	/**
	 *Deletes the employee with specified id
	 */
	@Override
	@Transactional
	public Response deleteByEmail(String email) {
		Integer k = empRepository.deleteByEmail(email);
		if(k.intValue() == 0)
			throw new EmployeeException(environment.getProperty("EMPLOYEE_FETCH_UNSUCCESSFULL"));
		log.info(environment.getProperty("DELETION_SUCCESSFULL"));
		return new Response(200,environment.getProperty("DELETION_SUCCESSFULL"),null);
	}

	/**
	 *Fetches the employee with specified email.
	 */
	@Override
	public Response getByEmail(String email) {
		EmployeePayroll employeePayroll = empRepository.findByEmail(email).orElseThrow(() -> new EmployeeException(environment.getProperty("EMPLOYEE_FETCH_UNSUCCESSFULL")));
		log.info(environment.getProperty("SUCCESS"));
		return new Response(200, environment.getProperty("SUCCESS"), mapper.map(employeePayroll,EmployeeDto.class));
	}

	/**
	 *Fetches all the employees.
	 */
	@Override
	public Response getList() {
		List<EmployeePayroll> employeePayrolls = empRepository.getSorted()
												.orElseThrow(() -> new EmployeeException(environment.getProperty("LIST_LOAD_UNSUCCESSFULL")));
		log.info(environment.getProperty("LIST_LOADED_SUCCESSFULLY"));
		return new Response(200, environment.getProperty("LIST_LOADED_SUCCESSFULLY"), employeePayrolls);
	}

	/**
	 *Updates the employee with the specified email
	 */
	@Override
	public Response updateByEmail(EmployeeDto employeeDto) {
		EmployeePayroll employeePayroll = empRepository.findByEmail(employeeDto.getEmail()).orElseThrow(() -> new EmployeeException(environment.getProperty("EMPLOYEE_FETCH_UNSUCCESSFULL")));
		employeePayroll.setEmployeeName(employeeDto.getEmployeeName());
		employeePayroll.setSalary(employeeDto.getSalary());
		employeePayroll.setEmail(employeeDto.getEmail());
		empRepository.save(employeePayroll);
		log.info(environment.getProperty("UPDATE_SUCCESSFULL"));
		Response response = new Response(200, environment.getProperty("UPDATE_SUCCESSFULL"),employeeDto);
		return response;
	}

	/**
	 *Fetches all the employees whose salary is greater than average
	 */
	@Override
	public Response getSalaryAboveAverage() {
		EmployeePayroll[] employeesWithSalaryGreaterThanAverage = empRepository.getAboveAverage();
		log.info(environment.getProperty("LIST_LOADED_SUCCESSFULLY"));
		return new Response(200,environment.getProperty("LIST_LOADED_SUCCESSFULLY"),employeesWithSalaryGreaterThanAverage);
		
	}
	
	public Response check(CredentialsDto credentials) {
		Credentials credentials2 = credentialsRepo.findByEmail(credentials.getEmail()).orElseThrow(() -> new CredentialsException(environment.getProperty("INVALID_CREDENTIALS")));
		log.info(credentials.toString());
		String token = tokenHelper.createJWT(credentials2.getId().toString(), environment.getProperty("token.issuer"), environment.getProperty("token.subject"), Long.parseLong(environment.getProperty("token.expirationTime"))); 
		CredentialsDto dto = mapper.map(credentials2, CredentialsDto.class);
		if(credentials.equals(dto))	
			return new Response(200, environment.getProperty("LOGIN_SUCCESSFULL"), token);
		else 
			return new Response(400, environment.getProperty("INVALID_CREDENTIALS"),null);
	}
	
	public Response addCredentials(CredentialsDto credentials) {
		credentialsRepo.findByEmail(credentials.getEmail()).ifPresent((emp) -> {throw new CredentialsException(environment.getProperty("EMPLOYEE_ALREADY_PRESENT"));});
		Credentials credentials3 = mapper.map(credentials, Credentials.class);
		credentialsRepo.save(credentials3);
		log.info(environment.getProperty("SIGNUP_SUCCESSFULL"));
		return new Response(200, environment.getProperty("SIGNUP_SUCCESSFULL"), credentials);
	}

	@Override
	public Response getEmployeeById(Long id) {
		EmployeePayroll employeePayrolls = empRepository.findById(id).orElseThrow(() -> new EmployeeException(environment.getProperty("EMPLOYEE_FETCHed_UNSUCCESSFULL")));
		EmployeeDto employeeDto = mapper.map(employeePayrolls, EmployeeDto.class);
		log.info(environment.getProperty("SUCCESS"));
		return new Response(200, environment.getProperty("SUCCESS"), employeeDto);
		}
}