package com.parse.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class DateParser {

//	private String errorMessage;

	public boolean isValidDate(String date) {
//		errorMessage = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date testDate = null;

		try {
			testDate = sdf.parse(date);
		}

		catch (ParseException e) {
//			errorMessage = "the date you provided is in an invalid date"
//					+ " format.";
			return false;
		}

		if (!sdf.format(testDate).equals(date)) {
//			errorMessage = "The date that you provided is invalid.";
			return false;
		}

		return true;

	}

}
