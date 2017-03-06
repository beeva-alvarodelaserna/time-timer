package com.beeva.labs.timetimer.splash.multipart.setup.number_of_steps;

import com.beeva.labs.timetimer.splash.multipart.setup.duration_and_name_of_steps.MultipartSetupDurationAndNameOfStepsFragment;
import com.beeva.labs.timetimer.support.base.ActivityNavigator;
import com.beeva.labs.timetimer.support.base.BaseFragment;
import com.beeva.labs.timetimer.support.base.BaseInteractor;

public class MultipartSetupNumberOfStepsFragment
	extends BaseFragment<MultipartSetupNumberOfStepsView, BaseInteractor> {

	public static MultipartSetupNumberOfStepsFragment newInstance() {
		return new MultipartSetupNumberOfStepsFragment();
	}

	@Override
	protected BaseInteractor getInteractor() {
		return BaseInteractor.EMPTY_INTERACTOR;
	}

	@Override
	protected MultipartSetupNumberOfStepsView getFragmentView() {
		return new MultipartSetupNumberOfStepsView(
			new MultipartSetupNumberOfStepsView.ViewListener() {

				@Override
				public void onContinue(int numberOfSteps) {
					MultipartSetupDurationAndNameOfStepsFragment fragment = MultipartSetupDurationAndNameOfStepsFragment.newInstance(
						numberOfSteps);
					viewContextInject(ActivityNavigator.class).navigate(
						fragment);
				}
			});
	}
}
