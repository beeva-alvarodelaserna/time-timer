package com.beeva.labs.timetimer.splash.multipart.setup.total_duration_and_number_of_steps;

import com.beeva.labs.timetimer.splash.multipart.setup.duration_and_name_of_steps.MultipartSetupDurationAndNameOfStepsFragment;
import com.beeva.labs.timetimer.support.base.ActivityNavigator;
import com.beeva.labs.timetimer.support.base.BaseFragment;
import com.beeva.labs.timetimer.support.base.BaseInteractor;

public class MultipartSetupTotalDurationAndNumberOfStepsFragment
	extends BaseFragment<MultipartSetupTotalDurationAndNumberOfStepsView, BaseInteractor> {

	public static MultipartSetupTotalDurationAndNumberOfStepsFragment newInstance() {
		return new MultipartSetupTotalDurationAndNumberOfStepsFragment();
	}

	@Override
	protected BaseInteractor getInteractor() {
		return BaseInteractor.EMPTY_INTERACTOR;
	}

	@Override
	protected MultipartSetupTotalDurationAndNumberOfStepsView getFragmentView() {
		return new MultipartSetupTotalDurationAndNumberOfStepsView(
			new MultipartSetupTotalDurationAndNumberOfStepsView.ViewListener() {

				@Override
				public void onContinue(int totalDuration, int numberOfSteps) {
					MultipartSetupDurationAndNameOfStepsFragment fragment =
						MultipartSetupDurationAndNameOfStepsFragment.newInstance(totalDuration,
						numberOfSteps);
					viewContextInject(ActivityNavigator.class).navigate(
						fragment);
				}
			});
	}
}
