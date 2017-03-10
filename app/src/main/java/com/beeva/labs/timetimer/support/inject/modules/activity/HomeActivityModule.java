package com.beeva.labs.timetimer.support.inject.modules.activity;

import com.beeva.labs.timetimer.support.base.BaseActivity;
import com.wokdsem.kinject.annotations.Includes;
import com.wokdsem.kinject.annotations.Module;

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
}
