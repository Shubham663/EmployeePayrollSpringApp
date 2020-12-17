package com.bridgelabz.employeepayroll.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bridgelabz.employeepayroll.model.Credentials;

@Repository
public interface CredentialsRepo extends JpaRepository<Credentials, Long>{
	Optional<Credentials> findByEmail(String email);
}
