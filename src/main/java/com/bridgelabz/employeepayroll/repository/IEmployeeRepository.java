package com.bridgelabz.employeepayroll.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bridgelabz.employeepayroll.model.EmployeePayroll;

@Repository
public interface IEmployeeRepository extends JpaRepository<EmployeePayroll, Long>{
	
	/**
	 * fetches the employee with the specified email
	 */
	Optional<EmployeePayroll> findByEmail(String email);

	/**
	 * @return returns the employees with salary greater than average salary
	 */
	@Query(value = "Select * from employees where salary > (select avg(salary) from employees)",nativeQuery = true)
	EmployeePayroll[] getAboveAverage();

	/**
	 * @return returns the employees sorted by id.
	 */
	@Query(value = "Select * from employees order by id",nativeQuery = true)
	Optional<List<EmployeePayroll>> getSorted();

	Integer deleteByEmail(String email);

}
