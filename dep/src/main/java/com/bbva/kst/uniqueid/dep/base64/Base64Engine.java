package com.bbva.kst.uniqueid.dep.base64;

import android.util.Base64;
import java.io.UnsupportedEncodingException;

public class Base64Engine implements Base64Tools {
	
	@Override
	public String encodeToBase64(String input) {
		try {
			return Base64.encodeToString(input.getBytes("UTF-8"), Base64.NO_WRAP);
		} catch (UnsupportedEncodingException e) {
			//TODO log
			return null;
		}
	}

	@Override
	public String decodeFromBase64(String input) {
		try {
			byte[] decodedByte = Base64.decode(input, Base64.NO_WRAP);
			return new String(decodedByte, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			//TODO log
			return null;
		}
	}
}
