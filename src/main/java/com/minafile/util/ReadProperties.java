package com.minafile.util;
 
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.minafile.exception.MyRuntimeException;
import com.minafile.model.PropertiesModel;

public class ReadProperties {
	// xml文件名。
	private static ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
		      "applicationContext.xml");
	public static void main(String[] args) {
		// 读取bean。
		PropertiesModel d = (PropertiesModel) context.getBean("propertiesModel");
	}
	
	public static PropertiesModel getModel(){
		PropertiesModel d =(PropertiesModel) context.getBean("propertiesModel");
		return d;
	}
	
	/**
	 * 这个方法暂时未用到。不过这里实现的是通过类加载器进行方法的调用。
	 * @param fieldName
	 * @return
	 */
	public static String getPropertyByFieldName(String fieldName){
		if(fieldName != null && !fieldName.equals("")){
			
			char[]  chars= fieldName.toCharArray();
			chars[0] = Character.toUpperCase(chars[0]); // 把第一个转为大写
			
			fieldName = String.valueOf(chars);
			
		}else{
			throw new MyRuntimeException("请输入有效的字段名");
		}
		
		Class classObject = PropertiesModel.class;
		Object invokeTester;
		Object result = "";
		try {
			invokeTester = classObject.getConstructor().newInstance();
			Method getMethod = classObject.getMethod("get" + fieldName);
			result = getMethod.invoke(invokeTester);
			System.out.println((String) result);
			
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		return (String) result;
	}
}
