package com.beeva.labs.timetimer.support.base;

import com.beeva.labs.timetimer.instruments.inject.AppInjector;
import com.beeva.labs.timetimer.instruments.inject.Injector;
import com.wokdsem.kommander.Kommander;
import java.util.concurrent.atomic.AtomicLong;

public abstract class BaseInteractor {

	public static final BaseInteractor EMPTY_INTERACTOR;
	private static final AtomicLong idGen;

	static {
		idGen = new AtomicLong();
		EMPTY_INTERACTOR = new BaseInteractor() {
		};
	}

	protected final String tag;
	private final Injector injector;
	protected final Kommander kommander;

	protected BaseInteractor() {
		tag = getClass().getCanonicalName() + idGen.incrementAndGet();
		injector = AppInjector.getInjector();
		kommander = injector.inject(Kommander.class);
	}

	public final void cancelPendingOperations() {
		cancelPendingOperations(tag);
	}

	public final void cancelPendingOperations(String tag) {
		kommander.cancelKommands(tag);
	}

	protected final <T> T inject(Class<T> tClass) {
		return injector.inject(tClass);
	}

}
