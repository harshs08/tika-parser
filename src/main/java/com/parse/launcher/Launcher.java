package com.parse.launcher;

import com.parse.util.DateParser;

public class Launcher {

	public static void main(String[] args) {
		DateParser dp = new DateParser();
		String date = "2012-10-23";
		String date1 = "9999-25-13";
		String date2 = "120-12-04";
		boolean a = dp.isValidDate(date);
		boolean b = dp.isValidDate(date1);
		boolean c = dp.isValidDate(date2);
		
		System.out.println("Date:"+a+ " Date1:"+b+" Date2:"+c);
		
	}

}
