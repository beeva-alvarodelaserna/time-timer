package com.beeva.labs.timetimer.support.instruments;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.util.Patterns.EMAIL_ADDRESS;
import static android.util.Patterns.PHONE;

public class Validators {

	private static final int MIN_LENGTH = 6;

	public static boolean isValidPasswordMinLength(String sequence) {
		return sequence.length() >= MIN_LENGTH;
	}

	public static boolean isValidPasswordNumber(String sequence) {
		Pattern containsValidNumber = Pattern.compile(".*[0-9].*+$");
		Matcher containsNumberMatcher = containsValidNumber.matcher(sequence);
		return containsNumberMatcher.matches();
	}

	public static boolean isValidPasswordSpecial(String sequence) {
		Pattern containsValidSpecial = Pattern.compile(".*[!@#$%^&*\\-].*$");
		Matcher containsSpecialMatcher = containsValidSpecial.matcher(sequence);
		return containsSpecialMatcher.matches();
	}

	public static boolean isValidEmail(String email) {
		Matcher emailMatcher = EMAIL_ADDRESS.matcher(email);
		boolean validatedEmail = emailMatcher.matches();
		if (email.contains("@")) {
			String[] array = email.split("@");
			if (array.length > 1 && array[1].contains(".")) {
				String[] finalArray = array[1].split("\\.");
				Pattern isValidEmail = Pattern.compile("[A-Z0-9a-z\\._%+-]+@([A-Za-z0-9-]+\\.)+[A-Za-z]{2,4}", Pattern.CASE_INSENSITIVE);
				Matcher matcher = isValidEmail.matcher(email);
				boolean isValid = matcher.matches();
				boolean customChecks = true;
				if (array.length <= 1 || finalArray.length <= 1 || array[0].length() < 2 || finalArray[0].length() < 2 || finalArray[1].length() < 2) {
					customChecks = false;
				}
				return validatedEmail && isValid && customChecks;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	public static boolean isValidPhone(String phone) {
		Matcher phoneMatcher = PHONE.matcher(phone);
		return phoneMatcher.matches() && (phone.length() <= 12) && (phone.length() >= 9);
	}

	public static boolean isValidPhoneForUserSearch(String phone) {
		Matcher phoneMatcher = PHONE.matcher(phone);
		return phoneMatcher.matches();
	}
	
}
