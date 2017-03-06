package com.beeva.labs.timetimer.support.inject.modules.app;

import android.os.Handler;
import android.os.Looper;
import com.bbva.kst.uniqueid.instruments.logger.Logger;
import com.beeva.labs.timetimer.support.instruments.AppLogger;
import com.wokdsem.kinject.annotations.Module;
import com.wokdsem.kinject.annotations.Provides;
import com.wokdsem.kommander.CentralKommander;
import com.wokdsem.kommander.Deliverer;
import com.wokdsem.kommander.Kommander;

@Module
public class CoreModule {

	@Provides(singleton = true)
	Kommander provideKommander() {
		CentralKommander centralKommander = CentralKommander.getInstance();
		return centralKommander.buildKommander(new Deliverer() {
			private final Handler handler = new Handler(Looper.getMainLooper());

			@Override
			public void deliver(Runnable runnable) {
				handler.post(runnable);
			}
		});
	}

	@Provides(singleton = true)
	Logger provideLogger() {
		return new AppLogger();
	}

}
