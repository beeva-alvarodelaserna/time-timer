package com.beeva.labs.timetimer.support.instruments;

import android.util.Log;
import com.bbva.kst.uniqueid.instruments.logger.Logger;
import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;

public class AppLogger implements Logger {

	@Override
	public void debugMessage(String tag, String msg) {
		Log.d(tag, msg);
		CrashlyticsCore.getInstance()
			.log(tag + " --> " + msg);
	}

	@Override
	public void debugError(String tag, String msg, Throwable e) {
		Log.e(tag, msg, e);
	}

	@Override
	public void nonFatalError(String tag, String msg, Throwable e) {
		Log.e(tag, msg, e);
		e.printStackTrace();
		CrashlyticsCore.getInstance()
			.logException(e);
	}

	@Override
	public void setKey(String key, String value) {
		Crashlytics.setString(key, value);
	}

}
