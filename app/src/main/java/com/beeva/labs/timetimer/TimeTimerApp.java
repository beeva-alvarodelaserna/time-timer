package com.beeva.labs.timetimer;

import android.app.Application;
import com.beeva.labs.timetimer.support.inject.KinjectInjector;
import com.beeva.labs.timetimer.support.inject.modules.app.AppModule;

import static com.beeva.labs.timetimer.instruments.inject.AppInjector.initInjector;

public class TimeTimerApp extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		setUp();

	}

	private void setUp() {
		setUpInjector();
	}

	private void setUpInjector() {
		AppModule appModule = new AppModule(this);
		initInjector(new KinjectInjector(appModule));
	}

}
