package com.bridgelabz.employeepayroll.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bridgelabz.employeepayroll.model.EmployeePayroll;

@Repository
public interface IEmployeeRepository extends JpaRepository<EmployeePayroll, Long>{

}
