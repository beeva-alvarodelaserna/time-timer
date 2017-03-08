package com.beeva.labs.timetimer.splash.multipart.setup.duration_and_name_of_steps;

import android.content.Context;
import android.os.Bundle;
import com.bbva.kst.uniqueid.R;
import com.beeva.labs.timetimer.splash.multipart.session.MultipartSessionFragment;
import com.beeva.labs.timetimer.splash.multipart.session_concentric.MultipartSessionConcentricFragment;
import com.beeva.labs.timetimer.support.base.ActivityNavigator;
import com.beeva.labs.timetimer.support.base.BaseFragment;
import com.beeva.labs.timetimer.support.base.BaseInteractor;
import com.beeva.labs.timetimer.support.ui.SessionStep;
import com.beeva.labs.timetimer.support.ui.ToastUtils;
import java.util.ArrayList;

public class MultipartSetupDurationAndNameOfStepsFragment
	extends BaseFragment<MultipartSetupDurationAndNameOfStepsView, BaseInteractor> {
	
	private final static String NUMBER_OF_STEPS = "numberOfSteps";
	private final static String TOTAL_DURATION = "totalDuration";
	
	private int numberOfSteps, totalDuration;
	private ArrayList<SessionStep> steps;
	
	private static MultipartSetupDurationAndNameOfStepsFragment newInstance() {
		return new MultipartSetupDurationAndNameOfStepsFragment();
	}
	
	public static MultipartSetupDurationAndNameOfStepsFragment newInstance(int numberOfSteps) {
		MultipartSetupDurationAndNameOfStepsFragment fragment = MultipartSetupDurationAndNameOfStepsFragment.newInstance();
		Bundle arguments = new Bundle();
		arguments.putInt(NUMBER_OF_STEPS, numberOfSteps);
		fragment.setArguments(arguments);
		return fragment;
	}
	
	public static MultipartSetupDurationAndNameOfStepsFragment newInstance(
		int totalDuration, int numberOfSteps) {
		MultipartSetupDurationAndNameOfStepsFragment fragment = MultipartSetupDurationAndNameOfStepsFragment.newInstance();
		Bundle arguments = new Bundle();
		arguments.putInt(NUMBER_OF_STEPS, numberOfSteps);
		arguments.putInt(TOTAL_DURATION, totalDuration);
		fragment.setArguments(arguments);
		return fragment;
	}
	
	@Override
	protected void loadArguments(Bundle arguments) {
		if (arguments.containsKey(NUMBER_OF_STEPS)) {
			numberOfSteps = arguments.getInt(NUMBER_OF_STEPS);
		}
		if (arguments.containsKey(TOTAL_DURATION)) {
			totalDuration = arguments.getInt(TOTAL_DURATION);
		}
	}
	
	@Override
	protected BaseInteractor getInteractor() {
		return BaseInteractor.EMPTY_INTERACTOR;
	}
	
	@Override
	protected MultipartSetupDurationAndNameOfStepsView getFragmentView() {
		return new MultipartSetupDurationAndNameOfStepsView(
			new MultipartSetupDurationAndNameOfStepsView.ViewListener() {
				
				@Override
				public void onFinish() {
					//MultipartSessionFragment sessionFragment = MultipartSessionFragment.newInstance(steps);
					//viewContextInject(ActivityNavigator.class).navigate(sessionFragment);
					MultipartSessionConcentricFragment sessionFragment = MultipartSessionConcentricFragment.newInstance(
						steps);
					viewContextInject(ActivityNavigator.class).navigate(sessionFragment);
				}
				
				@Override
				public void onAddStepInfo(String name, int duration, float timerAngle) {
					if (totalDuration == 0) {
						totalDuration = 60;
					}
					totalDuration -= duration;
					if (totalDuration > 0) {
						SessionStep step = new SessionStep();
						step.name = name;
						step.duration = duration;
						step.timerAngle = timerAngle;
						step.isRunning = false;
						steps.add(step);
						ToastUtils.showShort(viewContextInject(Context.class),
											 String.format(getString(R.string.remaining_time),
														   String.valueOf(totalDuration)));
						if (fragmentView != null) {
							fragmentView.continueSettingUp();
							
						}
					} else {
						ToastUtils.showShort(viewContextInject(Context.class),
											 String.format(getString(R.string.time_excedded),
														   String.valueOf(totalDuration)));
					}
				}
			});
	}
	
	@Override
	protected void onViewCreated() {
		if (fragmentView != null && totalDuration > 0 && numberOfSteps > 0) {
			fragmentView.initSessionSetup(totalDuration, numberOfSteps);
			steps = new ArrayList<>();
		} else if (fragmentView != null && numberOfSteps > 0) {
			fragmentView.initSessionSetup(numberOfSteps);
			steps = new ArrayList<>();
		}
	}
}
