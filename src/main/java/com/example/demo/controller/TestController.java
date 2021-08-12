package com.example.demo.controller;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.GenTestData;
import com.example.demo.RedisConsistency;
import com.example.demo.model.Test;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(value = "测试类")
@RestController
public class TestController {

	private static final String template = "Hello, %s!";
	private final AtomicLong counter = new AtomicLong();
	@Autowired
	RedisConsistency rs;

	@ApiOperation(value = "获取欢迎信息", notes = "根据url的name来获取用户详细信息")
	@ApiImplicitParam(name = "name", value = "用户名称", required = true, dataType = "String")
	@GetMapping("/test")
	public Test dealTest(@RequestParam(value = "name", defaultValue = "World") String name) {
		return new Test(counter.incrementAndGet(), String.format(template, name));
	}

	@GetMapping("/test1")
	public Test dealTest1(@RequestParam(value = "name", defaultValue = "World") String name) {
		return new Test(counter.incrementAndGet(), String.format(template, name));
	}

	@ApiOperation(value = "启动redis一致性测试", notes = "数据一致性测试", tags = "redis api")
	@GetMapping("/redisstart")
	public String redisStart() {
		rs.start();
		return "started";
	}

	@ApiOperation(value = "停止redis一致性测试", notes = "数据一致性测试", tags = "redis api")
	@GetMapping("/redisstop")
	public String redisStop() {
		rs.stop();
		return "stoped";
	}

	@ApiOperation(value = "普通String数据导入", notes = "单条方式导入测试", tags = "redis api")
	@ApiImplicitParam(name = "count", value = "数量", required = false, dataType = "String")
	@GetMapping("/importdata")
	public String importData(@RequestParam(value = "count", defaultValue = "10000") String count) {
		long start = System.currentTimeMillis();
		rs.importData(Integer.parseInt(count));
		long costs = System.currentTimeMillis() - start;
		return "import " + count + " data finished costs=" + costs + "ms";
	}

	@ApiOperation(value = "普通String数据批量导入", notes = "批量方式导入测试", tags = "redis api")
	@ApiImplicitParams({ @ApiImplicitParam(name = "count", value = "数量", required = false, dataType = "String"),
			@ApiImplicitParam(name = "size", value = "批量数", required = false, dataType = "int") })
	@GetMapping("/importstringdatabatch")
	public String importStringDataBatch(@RequestParam(value = "count", defaultValue = "10000") String count,
			@RequestParam(value = "size", defaultValue = "5000") int size) {
		long start = System.currentTimeMillis();
		rs.importDataBatch(Integer.parseInt(count), size);
		long costs = System.currentTimeMillis() - start;
		return "import " + count + " data,batch size=" + size + " finished costs=" + costs + "ms";
	}

	@ApiOperation(value = "普通Hash数据批量导入", notes = "批量方式导入测试", tags = "redis api")
	@ApiImplicitParams({ @ApiImplicitParam(name = "count", value = "数量", required = false, dataType = "int"),
			@ApiImplicitParam(name = "size", value = "key对应的map大小", required = false, dataType = "int") })
	@GetMapping("/importhashdatabatch")
	public String importHashDataBatch(@RequestParam(value = "count", defaultValue = "10000") int count,
			@RequestParam(value = "size", defaultValue = "30") int size) {
		long start = System.currentTimeMillis();
		rs.importHashDataBatch(count, size);
		long costs = System.currentTimeMillis() - start;
		return "import " + count +  "hash data,map size=" + size + " finished costs="
				+ costs + "ms";
	}
	
	@ApiOperation(value = "普通Set数据批量导入", notes = "批量方式导入测试", tags = "redis api")
	@ApiImplicitParams({ @ApiImplicitParam(name = "count", value = "数量", required = false, dataType = "int"),
			@ApiImplicitParam(name = "size", value = "key对应的member大小", required = false, dataType = "int") })
	@GetMapping("/importsetdatabatch")
	public String importSetDataBatch(@RequestParam(value = "count", defaultValue = "10000") int count,
			@RequestParam(value = "size", defaultValue = "30") int size) {
		long start = System.currentTimeMillis();
		rs.importSetDataBatch(count, size);
		long costs = System.currentTimeMillis() - start;
		return "import " + count + " set data,set size=" + size + " finished costs="
				+ costs + "ms";
	}
	
	@ApiOperation(value = "普通Set数据批量导入", notes = "批量方式导入测试", tags = "redis api")
	@ApiImplicitParams({ @ApiImplicitParam(name = "count", value = "数量", required = false, dataType = "int"),
			@ApiImplicitParam(name = "size", value = "key对应的list大小", required = false, dataType = "int") })
	@GetMapping("/importlistdatabatch")
	public String importListDataBatch(@RequestParam(value = "count", defaultValue = "10000") int count,
			@RequestParam(value = "size", defaultValue = "30") int size) {
		long start = System.currentTimeMillis();
		rs.importListDataBatch(count, size);
		long costs = System.currentTimeMillis() - start;
		return "import " + count + " list data,list size=" + size + " finished costs="
				+ costs + "ms";
	}

}
