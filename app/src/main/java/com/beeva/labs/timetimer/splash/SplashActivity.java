package com.beeva.labs.timetimer.splash;

import android.os.Bundle;
import com.beeva.labs.timetimer.splash.landing.LandingFragment;
import com.beeva.labs.timetimer.support.base.ActivityNavigator;
import com.beeva.labs.timetimer.support.base.BaseActivity;
import com.beeva.labs.timetimer.support.base.BaseInteractor;

public class SplashActivity extends BaseActivity<SplashView, BaseInteractor> {

	private static final String FROM_TOKEN_EXPIRATION_BUNDLE_EXTRA = "FROM_TOKEN_EXPIRATION";

	@Override
	protected SplashView getView() {
		return new SplashView();
	}

	@Override
	protected BaseInteractor getInteractor() {
		return BaseInteractor.EMPTY_INTERACTOR;
	}

	@Override
	protected void onCreated(Bundle savedInstanceState) {
		if (savedInstanceState == null) {
			onBoarding();
		}
	}

	private void onBoarding() {
		navigate();
	}

	private void navigate() {
		LandingFragment landingFragment = LandingFragment.newInstance();
		viewContextInject(ActivityNavigator.class).navigate(landingFragment, ActivityNavigator.B_REF_SETTINGS);
	}

	@Override
	protected boolean onBackIntercept() {
		if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
			moveTaskToBack(true);
			return true;
		} else {
			return false;
		}
	}
}
