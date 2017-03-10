package com.beeva.labs.timetimer.instruments.inject;

public interface Injector {

	<T> T inject(Class<T> tClass);

}
