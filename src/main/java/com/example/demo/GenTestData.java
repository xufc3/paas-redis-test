package com.example.demo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import org.apache.commons.lang3.RandomStringUtils;

public class GenTestData {

	public void genData(int count) {
		
		FileWriter fw = null;

		try {
			fw = new FileWriter(new File("data.txt"));

			int i = 0;
			while (i++ < count) {
				Random rand = new Random();
				String key = RandomStringUtils.randomAlphanumeric(rand.nextInt(10)+1);
				String val = RandomStringUtils.randomAlphanumeric(rand.nextInt(30)+1);
				fw.append("set " + key + " " + val +"\n");
				
				fw.flush();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
