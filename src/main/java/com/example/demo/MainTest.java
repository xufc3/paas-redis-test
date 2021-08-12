package com.example.demo;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import sun.misc.Launcher;

import java.util.Locale;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainTest {
    private static final Logger logger = LoggerFactory.getLogger(MainTest.class);
	
    public static String bytes2hex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        String tmp = null;
        for (byte b : bytes) {
            // 将每个字节与0xFF进行与运算，然后转化为10进制，然后借助于Integer再转化为16进制
            tmp = Integer.toHexString(0xFF & b);
            if (tmp.length() == 1) {     
                tmp = "0" + tmp;
            }
            sb.append(tmp).append(" ");
        }
        return sb.toString();

    }
    
    public static void main(String[] args) throws IOException {
//		String s= String.format("ss%s", "aaa");
//		System.out.println(s);
//		logger.info("{}", "abc");
		
//		String a = System.getProperty("java.ext.dirs");
//		StringTokenizer st = new StringTokenizer(a, File.pathSeparator);
//		while (st.hasMoreTokens()) {
//			System.out.println(st.nextToken());
//			
//		}
//		System.out.println(MainTest.class.getClassLoader());
//		System.out.println(MainTest.class.getClassLoader().getParent());
//		System.out.println(MainTest.class.getClassLoader().getParent().getParent());
//		
//		System.out.println(int.class.getClassLoader());
//		
//		Launcher a = Launcher.getLauncher();
//		System.out.println(a.getClassLoader());
		String cn = "中文";
		System.out.println(cn);
//		System.out.println(System.getProperty("file.encoding"));
//		System.out.println(Charset.defaultCharset());
//		System.setProperty("file.encoding", "UTF-8");
		System.out.println(Charset.defaultCharset());
		System.out.println(Locale.getDefault());
//		System.out.println(cn.toCharArray());
//		System.out.println(bytes2hex(cn.getBytes(Charset.defaultCharset())));
//		System.out.println(bytes2hex(cn.getBytes()));
		System.out.println(bytes2hex(cn.getBytes("GBK")));
		System.out.println(bytes2hex(cn.getBytes("UTF-8")));
		
		System.out.println(bytes2hex(cn.getBytes("iso-8859-1")));
		
		System.out.println(new String(cn.getBytes("iso-8859-1"),"GBK"));
		
		System.out.println(new String(cn.getBytes("iso-8859-1"),"UTF-8"));
		
		InputStream s = MainTest.class.getClassLoader().getResourceAsStream("chinese.properties");
		Properties p = new Properties();
		p.load(s);
		System.out.println(p.getProperty("abc"));
		System.out.println(p.getProperty("abc").length());
		System.out.println(new String(p.getProperty("abc").getBytes("ISO-8859-1"),"UTF-8"));
		
		InputStream s1 = MainTest.class.getClassLoader().getResourceAsStream("chinese-latin1.properties");
		Properties p1 = new Properties();
		p1.load(s1);
		System.out.println(p1.getProperty("abc"));
		System.out.println(p1.getProperty("abc").length());
		System.out.println(new String(p1.getProperty("abc").getBytes("ISO-8859-1"),"UTF-8"));

	}

}
