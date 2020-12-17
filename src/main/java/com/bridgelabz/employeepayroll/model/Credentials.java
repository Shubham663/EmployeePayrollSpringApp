package com.bridgelabz.employeepayroll.model;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "LoginCredentials")
@Data
public class Credentials {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String email;
	private String password;
	
	@Override
	public boolean equals(Object obj) {
		Credentials credentials = (Credentials)obj;
		if(credentials.getEmail().equals(this.getEmail()) && credentials.getPassword().equals(this.getPassword()))
			return true;
		else
			return false;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(email,password,id);
	}
}
