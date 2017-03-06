package com.beeva.labs.timetimer.support.inject;

import com.bbva.kst.uniqueid.instruments.inject.Injector;
import com.wokdsem.kinject.Kinject;

public class KinjectInjector implements Injector {

	private final Kinject injector;

	public KinjectInjector(Object module) {
		injector = Kinject.instantiate(module);
	}

	@Override
	public <T> T inject(Class<T> tClass) {
		return injector.get(tClass);
	}

}
