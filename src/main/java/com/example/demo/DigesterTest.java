package com.example.demo;

import java.io.File;
import java.io.IOException;

import org.apache.commons.digester3.Digester;
import org.xml.sax.SAXException;

public class DigesterTest {

	public static void main(String[] args) {
		Digester digester = new Digester();
//		digester.addObjectCreate("Server", Server.class);
		digester.addObjectCreate("Server", "com.example.demo.Server1", "name");
		digester.addSetProperties("Server");
		digester.addCallMethod("Server", "play");
//		digester.addSetNext("Server", "addService");
		
		//同理 读取Service节点，现在我是栈顶
        digester.addObjectCreate("Server/Service", Service.class);
        digester.addSetProperties("Server/Service");
        // 调用addService方法 将Service加入Server对象
        digester.addSetNext("Server/Service", "addService");
 
        //windows中根目录代表项目所在的盘符
        File file = new File("/Users/xfc/server.xml");
        
        Server server = null;
        try {
        	server = (Server)digester.parse(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        System.out.println(server);
		
		

	}

}
