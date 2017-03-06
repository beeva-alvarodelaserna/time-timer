package com.beeva.labs.timetimer.support.inject.modules.activity;

import com.beeva.labs.timetimer.support.base.BaseActivity;
import com.beeva.labs.timetimer.support.delegates.SharingUsers;
import com.beeva.labs.timetimer.support.delegates.SharingUsersDelegate;
import com.wokdsem.kinject.annotations.Includes;
import com.wokdsem.kinject.annotations.Module;
import com.wokdsem.kinject.annotations.Provides;

@Module(completed = true)
public class HomeActivityModule {

	private final BaseActivity baseActivity;

	public HomeActivityModule(BaseActivity baseActivity) {
		this.baseActivity = baseActivity;
	}

	@Includes
	public BaseActivityModule includeBaseActivityModule() {
		return new BaseActivityModule(baseActivity);
	}

	@Provides(singleton = true)
	public SharingUsers provideShareUsers() {
		return new SharingUsersDelegate();
	}
}
