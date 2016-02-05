package com.test.minafile.client;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class TestFileInput {
	// 测试保存文件以后，是否能够正确读出来之后
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		FileInputStream ios = new FileInputStream("D:\\Serverfile\\1454662462150gagag.txt");
		ObjectInputStream iis = new ObjectInputStream(ios);
		String date = (String) iis.readObject();
		System.out.println(date);
	}
}
