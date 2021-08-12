package com.example.demo;

import java.net.URI;
import java.net.URISyntaxException;

public class Test {

	public static void main(String[] args) throws URISyntaxException {
		// TODO Auto-generated method stub
		String str="https://abc.com?a=1&b=[a, b]";
		str = str.replaceAll(" ", "%20");
		new URI(str);
		System.out.println("aa");

	}

}
