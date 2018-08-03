package org.common.helper;

import java.io.Closeable;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;
import org.common.exception.ReportableException;

public class Helper {

	private static Logger logger = Logger.getLogger(Helper.class);

	public static void closeMultiple(Closeable... cs) {
		for (Closeable c : cs) {
			close(c);
		}
	}

	public static void close(Closeable c) {
		if (c == null)
			return;
		try {
			c.close();
		} catch (IOException e) {
			logger.error("Error when closing", e);
		}
	}

	public static boolean isAlpha(String word) {
		char[] chars = word.toCharArray();

		for (char c : chars) {
			if (!Character.isLetter(c)) {
				return false;
			}
		}
		return true;
	}

	public static boolean isPositiveNumeric(String word) {
		for (char c : word.toCharArray()) {
			if (!Character.isDigit(c))
				return false;
		}
		return true;
	}

	public static String getLeadingNumeric(String word) {
		Matcher matcher = Pattern.compile("\\d+").matcher(word);
		if (matcher.find()) {
			return matcher.group();
		}
		return "";
	}
	
	public static int longToInt(long l) {
	    if (l < Integer.MIN_VALUE || l > Integer.MAX_VALUE) {
	        throw new ReportableException("long value" + l + " overflow when casting to int value");
	    }
	    return (int)l;
	}
	
	public static int toInt(Object obj){
	    if (obj instanceof String) {
	        return Integer.parseInt((String) obj);
	    } else if (obj instanceof Integer) {
	        return ((Integer) obj).intValue();
	    } else if (obj instanceof Long) {
	    	return Helper.longToInt((Long)obj);
	    } else {
			String toString = obj.toString();
			if (toString.matches("-?d+")) {
			     return Integer.parseInt(toString);
			}
			throw new ReportableException(obj + "cannot be cast into int value");
	    }
	}

}
