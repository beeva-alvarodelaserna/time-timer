package com.beeva.labs.timetimer.support.instruments;

import com.google.gson.Gson;

public class SerializerUi {

	private static final Gson gson = new Gson();

	public static <T> String serialize(T t) {
		return gson.toJson(t);

	}

	public static <T> T deserialize(String item, Class<T> t) {
		return gson.fromJson(item, t);

	}

}