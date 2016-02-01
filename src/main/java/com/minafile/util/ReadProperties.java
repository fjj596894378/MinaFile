package com.minafile.util;
 
import org.springframework.context.support.ClassPathXmlApplicationContext; 
import com.minafile.model.PropertiesModel;

public class ReadProperties {
	// xml文件名。
	private static ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
		      "applicationContext.xml");
	public static void main(String[] args) {
		// 读取bean。
		PropertiesModel d = (PropertiesModel) context.getBean("propertiesModel");
		System.out.println(d.getBjsy());
		System.out.println(d.getBjsyPath());
	}
}
