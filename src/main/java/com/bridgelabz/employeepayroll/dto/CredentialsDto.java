package com.bridgelabz.employeepayroll.dto;

import javax.validation.constraints.NotNull;

import lombok.Data;
@Data
public class CredentialsDto {

	@NotNull
	private String email;
	@NotNull
	private String password;
	
}
