package com.bridgelabz.employeepayroll.configure;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;

@org.springframework.context.annotation.Configuration
public class Configuration {
	
	/**
	 * @return returns a model mapper object.
	 */
	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

}
