package com.example.demo;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class Server{
	private String port;
	private String name;
	private List<Service> serviceList = new ArrayList<Service>();
	public void addService(Service service) {
		serviceList.add(service);
	}
	
	public Server() {
		super();
	}

	@Override
	public String toString() {
		return "Server [port=" + port + ", name=" + name + ", serviceList=" + serviceList + "]";
	}

	public void play() {
		this.toString();
	}
}