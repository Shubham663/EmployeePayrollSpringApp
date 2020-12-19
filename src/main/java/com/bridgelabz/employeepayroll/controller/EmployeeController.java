package com.bridgelabz.employeepayroll.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.employeepayroll.dto.CredentialsDto;
import com.bridgelabz.employeepayroll.dto.EmployeeDto;
import com.bridgelabz.employeepayroll.service.IEmployeeService;
import com.bridgelabz.employeepayroll.utility.Response;
import com.bridgelabz.employeepayroll.utility.TokenHelper;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/employee")
@CrossOrigin
@Slf4j

@PropertySource("classpath:message.properties")
public class EmployeeController {

	
	@Autowired
	Environment environment;
	
	@Autowired
	private IEmployeeService employeeService;
	
	@Autowired
	private TokenHelper tokenHelper;
	
	
	/**
	 * Method for checking whether the credentials provided are valid or not
	 */
	@PostMapping("/signin")
	public ResponseEntity<Response> checkCredentials(@Valid @RequestBody CredentialsDto credentials) {
		Response response = employeeService.check(credentials);	
		return new ResponseEntity<Response>(response,HttpStatus.OK);
	}
	
	@PostMapping("/signup")
	public ResponseEntity<Response> addCredentials(@Valid @RequestBody CredentialsDto credentials) {
		Response response = employeeService.addCredentials(credentials);
		return new ResponseEntity<>(response,HttpStatus.OK);
	}
	
	/**
	 * @return returns the list of employees from the database
	 */
	@GetMapping
	public ResponseEntity<Response> getEmployeeList(@RequestHeader(value = "Authorisation")String token) {
		Claims claims = tokenHelper.decodeJWT(token);
		log.info(claims.get("token", String.class));
		return new ResponseEntity<>(employeeService.getList(),HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Response> getById(@PathVariable(name = "id")Long id,@RequestHeader(value = "Authorisation")String token) {
		Claims claims = tokenHelper.decodeJWT(token);
		log.info(claims.get("token", String.class));
		return new ResponseEntity<>(employeeService.getEmployeeById(id),HttpStatus.OK);
	}
	
	/**
	 * Posting the employeePayroll to database
	 */
	@PostMapping
	public ResponseEntity<Response> addEmployee(@Valid @RequestBody EmployeeDto employeePayrollDto,@RequestHeader(value = "Authorisation")String token) {
		Claims claims = tokenHelper.decodeJWT(token);
		log.info(claims.get("token", String.class));
		return new ResponseEntity<>(employeeService.addEmployee(employeePayrollDto),HttpStatus.OK);
	}
	

	/**
	 * Deleting the employee using the email.
	 */
	@DeleteMapping
//	@Transactional
	public ResponseEntity<Response> delete(@Valid @RequestBody EmployeeDto employeeDto,@RequestHeader(value = "Authorisation")String token) {
		Claims claims = tokenHelper.decodeJWT(token);
		log.info(claims.get("token", String.class));
		Response response = employeeService.deleteByEmail(employeeDto.getEmail());
		return new ResponseEntity<Response>(response,HttpStatus.OK);
	}
	
	/**
	 * Updating the employee based on the email.
	 */
	@PutMapping
	public ResponseEntity<Response> updateByEmail(@Valid @RequestBody EmployeeDto employeeDto,@RequestHeader(value = "Authorisation")String token) {
		Claims claims = tokenHelper.decodeJWT(token);
		log.info(claims.get("token", String.class));
		Response response = employeeService.updateByEmail(employeeDto);
		return new ResponseEntity<>(response,HttpStatus.OK);
	}
	
	
	
	/**
	 * @return Fetching the employee by email.
	 */
	@CrossOrigin
	@PostMapping("/getbyemail")
	public ResponseEntity<Response> returnString(@RequestBody String email,@RequestHeader(value = "Authorisation")String token) {
		Claims claims = tokenHelper.decodeJWT(token);
		log.info(claims.get("token", String.class));
		Response response = employeeService.getByEmail(email);
		return new ResponseEntity<Response>(response,HttpStatus.OK);
	}
	
	/**
	 * @return Returns the employees with salary greater than average salaries.
	 */
	@GetMapping("/salaryAboveAverage")
	public ResponseEntity<Response> getSalaryGreaterThanAverage(@RequestHeader(value = "Authorisation")String token) {
		Claims claims = tokenHelper.decodeJWT(token);
		log.info(claims.get("token", String.class));
		return  new ResponseEntity<>(employeeService.getSalaryAboveAverage(),HttpStatus.OK);
	}
}
