package com.example.demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RedisConsistency {
	@Autowired
	RedisClusterOper redisOp;
	long reads = 0;
	long writes = 0;
	long failed_reads = 0;
	long failed_writes = 0;
	int delay = 0;
	int lost_writes = 0;
	int not_ack_writes = 0;
	Map<String, Long> cached = new HashMap<String, Long>();
	boolean stop = false;

	public void stop() {
		this.stop = true;
	}

	public void start() {
		this.stop = false;
		new Thread(new Runnable() {

			@Override
			public void run() {
				long last_report = System.currentTimeMillis();
				while (!stop) {
					// Read
					String key = genkey();
					try {
						String val = redisOp.get(key);
						if (val != null) {
							check_consistency(key, Long.parseLong(val));
						}
						reads += 1;
					} catch (Exception e) {
						System.out.println("Reading:" + e.getMessage());
						failed_reads += 1;
					}
//		            # Write
					try {
						Long val = redisOp.increment(key);
						cached.put(key, val);
						writes += 1;
					} catch (Exception e) {
						System.out.println("Writing:" + e.getMessage());
						failed_writes += 1;
					}
					//
//		            # Report
					try {
						Thread.sleep(delay);
					} catch (InterruptedException e) {
					}
					if (System.currentTimeMillis() != last_report) {
						StringBuilder sb = new StringBuilder();
						sb.append(reads + " R (" + failed_reads + ") err |");
						sb.append(writes + " W (" + failed_writes + ") err |");
						if (lost_writes > 0) {
							sb.append(lost_writes + " lost |");
						}
						if (not_ack_writes > 0) {
							sb.append(not_ack_writes + " noack |");
						}
						System.out.println(sb.toString());
					}
				}
			}
		}).start();

	}

	private String genkey() {
//		# Write more often to a small subset of keys
		int keyspace = 1000000;
		int working_set = 10000000;
		Random r = new Random();
		int ks = r.nextFloat() > 0.5 ? keyspace : working_set;
		return "key_" + r.nextInt(ks);
	}

	private void check_consistency(String key, long value) {
		Long expected = cached.get(key);
		if (expected == null) {
			return;
		}
		if (expected != value) {
			System.out.println(key+" expect:" + expected + ", reality:" + value+ ", diff:" + Math.abs(expected-value));
		}
		if (expected > value) {
			lost_writes += 1;
		} else if (expected < value) {
			not_ack_writes += 1;
		}
	}

	public void importData(int count) {

		int i = 0;
		Random rand = new Random();		
		while (i++ < count) {
			String key = RandomStringUtils.randomAlphabetic(rand.nextInt(5) + 5);
			String val = RandomStringUtils.randomAlphanumeric(rand.nextInt(30) + 1);
			try {
				redisOp.set(key, val);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public void importDataBatch(int count, int batchCount) {

		int i = 0;
		int times = count / batchCount;

		while (i++ < times) {
			Map<String, String> map = genMapData(batchCount);
			try {
				redisOp.batchset(map);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//剩余数据量写入
		int left= count - times * batchCount;
		Map<String, String> map = genMapData(left);
		try {
			redisOp.batchset(map);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private Map<String, String> genMapData(int count) {
		int i = 0;
		Map<String, String> map = new HashMap<String, String>();
		Random rand = new Random();		
		while (i++ < count) {
			String key = RandomStringUtils.randomAlphabetic(rand.nextInt(5) + 5)+ System.nanoTime();
			String val = RandomStringUtils.randomAlphanumeric(rand.nextInt(30) + 1);
			map.put(key, val);
		}
		return map;
	}

	public void importHashDataBatch(int count, int mapsize) {

		int i = 0;
//		int times = count / keyCount;
		Random rand = new Random();
		while (i++ < count) {
			String key = RandomStringUtils.randomAlphabetic(rand.nextInt(5) + 11)+ System.nanoTime();
			Map<String, String> map = genMapData(mapsize);
			try {
				redisOp.hmset(key, map);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private List<String> genListData(int count) {
		int i = 0;
		List<String> lst = new ArrayList<String>();
		Random rand = new Random();		
		while (i++ < count) {
			String val = RandomStringUtils.randomAlphanumeric(rand.nextInt(30) + 1);
			lst.add(val);
		}
		return lst;
	}

	public void importSetDataBatch(int count, int size) {

		int i = 0;
//		int times = count / keyCount;
		Random rand = new Random();
		while (i++ < count) {
			String key = RandomStringUtils.randomAlphabetic(rand.nextInt(5) + 11)+ System.nanoTime();
			List<String> lst = genListData(size);
			try {
				redisOp.sadd(key, lst.toArray(new String[0]));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void importListDataBatch(int count, int size) {

		int i = 0;
//		int times = count / keyCount;
		Random rand = new Random();
		while (i++ < count) {
			String key = RandomStringUtils.randomAlphabetic(rand.nextInt(5) + 11)+ System.nanoTime();
			List<String> lst = genListData(size);
			try {
				redisOp.leftPush(key, lst);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
