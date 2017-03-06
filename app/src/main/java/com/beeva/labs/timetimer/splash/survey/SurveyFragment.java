package com.beeva.labs.timetimer.splash.survey;

import com.beeva.labs.timetimer.splash.end_session.EndSessionFragment;
import com.beeva.labs.timetimer.support.base.ActivityNavigator;
import com.beeva.labs.timetimer.support.base.BaseFragment;
import com.beeva.labs.timetimer.support.base.BaseInteractor;

public class SurveyFragment extends BaseFragment<SurveyView, BaseInteractor> {

	public static SurveyFragment newInstance() {
		return new SurveyFragment();
	}

	@Override
	protected BaseInteractor getInteractor() {
		return BaseInteractor.EMPTY_INTERACTOR;
	}

	@Override
	protected SurveyView getFragmentView() {
		return new SurveyView(new SurveyView.ViewListener() {
			
			@Override
			public void onYes() {
				goToEndSession(0);
			}
			
			@Override
			public void onMeh() {
				goToEndSession(1);
			}
			
			@Override
			public void onNo() {
				goToEndSession(2);
			}
		});
	}
	
	private void goToEndSession(int result) {
		EndSessionFragment endSessionFragment = EndSessionFragment.newInstance(result);
		viewContextInject(ActivityNavigator.class).navigate(endSessionFragment);
	}
}
