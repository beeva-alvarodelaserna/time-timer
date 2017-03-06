package com.bbva.kst.uniqueid.instruments.utils;

import com.bbva.kst.uniqueid.instruments.inject.AppInjector;
import com.bbva.kst.uniqueid.instruments.logger.Logger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class StringUtils {

	public static String makeStringSingular(String plural) {
		String singular;
		if (plural.endsWith("'s") || plural.endsWith("ss")) {
			singular = plural;
		} else if (plural.endsWith("s")) {
			if (plural.endsWith("ies")) {
				singular = plural.replace("ies", "y");
			} else {
				singular = plural.substring(0, plural.length() - 1);
			}
		} else {
			singular = plural;
		}
		return singular;
	}

	public static String obtainTwoDigitString(int digit) {
		String result = "";
		if (digit >= 10 && digit <= 99) {
			result = String.valueOf(digit);
		} else if (digit < 10) {
			result += "0";
			result += String.valueOf(digit);
		} else {
			result += String.valueOf(digit)
				.substring(0, 1);
		}
		return result;
	}

	public static String bytesToHexString(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (byte b : bytes) {
			sb.append(String.format("%02x", b));
		}
		return sb.toString();
	}

	public static byte[] hexStringToByteArray(String s) {
		int len = s.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
		}
		return data;
	}

	public static boolean isNullOrEmpty(String str) {
		return (str == null || str.isEmpty());
	}

	public static String emptyIfNullString(String source) {
		return isNullOrEmpty(source) ? "" : source;
	}

	public static String capitalize(String s) {
		if (isNullOrEmpty(s)) {
			return null;
		} else if (s.length() == 1) {
			return s.toUpperCase();
		} else {
			if (s.toLowerCase()
					.equals("id") || s.toLowerCase()
					.equals("url") || s.toLowerCase()
					.equals("cvv") || s.toLowerCase()
					.equals("ssn")) {
				return s.toUpperCase();
			} else {
				String[] stringArray = s.split(" ");
				String result = "";
				for (int i = 0; i < stringArray.length; i++) {
					if (i == stringArray.length - 1) {
						result += stringArray[i].substring(0, 1)
									  .toUpperCase() + stringArray[i].substring(1)
									  .toLowerCase();
					} else {
						if (stringArray[i].toLowerCase()
							.equals("ssn")) {
							result += stringArray[i].toUpperCase();
						} else {
							result += stringArray[i].substring(0, 1)
										  .toUpperCase() + stringArray[i].substring(1)
										  .toLowerCase();
						}
						result += " ";
					}
				}
				return result;
			}
		}
	}

	public static String dateToString(String msg) {
		SimpleDateFormat localFormat = getSimpleDateFormatInLocalTimeZone();
		SimpleDateFormat resultFormat = new SimpleDateFormat("MMM dd,yyyy hh:mm a");
		SimpleDateFormat utcFormat = getSimpleDateFormatWithTimeZone(TimeZone.getTimeZone("UTC"));
		String resultStr = msg;
		try {
			Date utcDate = utcFormat.parse(msg);
			String msg2 = localFormat.format(utcDate);
			Date date = localFormat.parse(msg2);
			resultStr = resultFormat.format(date);
		} catch (ParseException e) {
			AppInjector.inject(Logger.class)
				.debugError(null, null, e);
		}
		return resultStr;
	}

	public static String getTodayDate() {
		SimpleDateFormat resultFormat = new SimpleDateFormat("MM/dd/yy");
		Date todayDate = new Date();
		return resultFormat.format(todayDate);
	}

	public static String dateToStringItemDetail(String msg) {
		SimpleDateFormat localFormat = getSimpleDateFormatInLocalTimeZone();
		SimpleDateFormat resultFormat = new SimpleDateFormat("MM/dd/yy");
		SimpleDateFormat utcFormat = getSimpleDateFormatWithTimeZone(TimeZone.getTimeZone("UTC"));
		String resultStr = msg;
		try {
			Date utcDate = utcFormat.parse(msg);
			String msg2 = localFormat.format(utcDate);
			Date date = localFormat.parse(msg2);
			resultStr = resultFormat.format(date);
		} catch (ParseException e) {
			AppInjector.inject(Logger.class)
				.debugError(null, null, e);
		}
		return resultStr;
	}

	public static String[] dateToStringArray(String msg) {
		SimpleDateFormat localFormat = getSimpleDateFormatInLocalTimeZone();
		SimpleDateFormat resultFormat = new SimpleDateFormat("MM/dd hh:mm a");
		SimpleDateFormat utcFormat = getSimpleDateFormatWithTimeZone(TimeZone.getTimeZone("UTC"));
		String resultStr = msg;
		try {
			Date utcDate = utcFormat.parse(msg);
			String msg2 = localFormat.format(utcDate);
			Date date = localFormat.parse(msg2);
			resultStr = resultFormat.format(date);
		} catch (ParseException e) {
			AppInjector.inject(Logger.class)
				.debugError(null, null, e);
		}
		String[] resultArray = new String[2];
		String[] split = resultStr.split(" ");
		resultArray[0] = split[0];
		resultArray[1] = split[1] + " " + split[2];
		return resultArray;
	}

	private static SimpleDateFormat getSimpleDateFormatWithTimeZone(TimeZone utc) {
		SimpleDateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		utcFormat.setTimeZone(utc);
		return utcFormat;
	}

	private static SimpleDateFormat getSimpleDateFormatInLocalTimeZone() {
		TimeZone tz = TimeZone.getDefault();
		return getSimpleDateFormatWithTimeZone(TimeZone.getTimeZone(tz.getID()));
	}

	public static String getTimeStamp() {
		SimpleDateFormat simpleDateFormat = getSimpleDateFormatInLocalTimeZone();
		return simpleDateFormat.format(Calendar.getInstance()
										   .getTime());
	}
}
