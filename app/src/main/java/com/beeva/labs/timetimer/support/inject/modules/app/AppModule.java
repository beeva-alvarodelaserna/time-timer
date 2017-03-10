package com.beeva.labs.timetimer.support.inject.modules.app;

import android.content.Context;
import com.beeva.labs.timetimer.TimeTimerApp;
import com.wokdsem.kinject.annotations.Includes;
import com.wokdsem.kinject.annotations.Module;
import com.wokdsem.kinject.annotations.Provides;

@Module(completed = true)
public class AppModule {

	private final TimeTimerApp app;

	public AppModule(TimeTimerApp app) {
		this.app = app;
	}

	@Includes
	CoreModule includeCoreModule() {
		return new CoreModule();
	}
	
	@Provides
	Context provideAppContext() {
		return app;
	}

	@Provides
	TimeTimerApp provideUniqueIdApp() {
		return app;
	}

}
