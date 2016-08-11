package com.bfsi.egalite.util;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Utility class for all string manipulations / formatting / querying / pattern
 * matching
 * 
 * @author vijay
 * 
 */
public class StringUtil {
	private static final Pattern EMAIL_ADDRESS_PATTERN = Pattern
			.compile("([\\w\\-]([\\.\\w])+[\\w]+@([\\w\\-]+\\.)+[A-Za-z]{2,4})");

	public static boolean checkEmailFormat(String email) {
		return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
	}

	/**
	 * Splits string with separator and return array of string
	 * 
	 * @param original
	 * @param delimiter
	 * @return
	 */
	public static String[] splitStringWithSeparator(String original,
			String delimiter) {
		if (original == null)
			return null;
		return original.split(delimiter);
	}

	
	
	public static String join(List<String> list, String delim) {
	    StringBuilder sb = new StringBuilder();
	    String loopDelim = "";
	    for(String s : list) {
	        sb.append(loopDelim);
	        sb.append(s);            
	        loopDelim = delim;
	    }

	    return sb.toString();
	}
}
