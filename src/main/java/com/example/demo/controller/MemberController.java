package com.example.demo.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
@Api(value="member")
@RestController
public class MemberController {
	
	Map<String, String> members = new HashMap<String, String>();
	@PostMapping("/addMember")
	public String addMember(@RequestParam(value="name") String name,@RequestParam(value="age")String age)
	{
		members.put(name, age);
		return String.format("添加成员name={},age={}", name, age);
	}
	
	@GetMapping("/getMember")
	public String getMember(@RequestParam(value="name")String name)
	{
		return String.format("获取成员name={},age={}", name, members.get(name));
	}
	
	@GetMapping("/getMembers")
	public String getMembers()
	{
		return String.format("获取所有成员：", members);
	}
	
	@GetMapping("/deleteMember")
	public String deleteMember(@RequestParam(value="name")String name)
	{
		return String.format("删除成员name={},age={}", name, members.get(name));
	}
	
	@GetMapping("/updateMember")
	public String updateMember(@RequestParam(value="name")String name,@RequestParam(value="age")String age)
	{
		return String.format("更新成员name={},age={}", name, members.get(name));
	}

}
