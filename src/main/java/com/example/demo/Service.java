package com.example.demo;

import java.util.List;

import lombok.Data;

@Data
public class Service{
	private String name;

	@Override
	public String toString() {
		return "Service [name=" + name + "]";
	}
	
}