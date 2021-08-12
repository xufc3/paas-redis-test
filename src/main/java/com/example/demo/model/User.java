package com.example.demo.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class User {
	private String name;
	private String pwd;
	public static void main(String[] args) {
		User u = new User();
		u.setName("aa");
		System.out.println(u);
	}

}
