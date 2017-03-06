package com.beeva.labs.timetimer.splash.multipart.session_concentric;

import android.content.Context;
import android.content.DialogInterface;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import com.bbva.kst.uniqueid.R;
import com.beeva.labs.timetimer.splash.survey.SurveyFragment;
import com.beeva.labs.timetimer.support.base.ActivityNavigator;
import com.beeva.labs.timetimer.support.base.BaseFragment;
import com.beeva.labs.timetimer.support.base.BaseInteractor;
import com.beeva.labs.timetimer.support.ui.SessionStep;
import java.util.ArrayList;

public class MultipartSessionConcentricFragment extends BaseFragment<MultipartSessionConcentricView, BaseInteractor> {

	private static ArrayList<SessionStep> steps;
	private int currentTimer;

	private static MultipartSessionConcentricFragment newInstance() {
		return new MultipartSessionConcentricFragment();
	}

	public static MultipartSessionConcentricFragment newInstance(ArrayList<SessionStep> stepList) {
		MultipartSessionConcentricFragment fragment = MultipartSessionConcentricFragment.newInstance();
		steps = stepList;
		return fragment;
	}

	@Override
	protected BaseInteractor getInteractor() {
		return BaseInteractor.EMPTY_INTERACTOR;
	}

	@Override
	protected MultipartSessionConcentricView getFragmentView() {
		return new MultipartSessionConcentricView(new MultipartSessionConcentricView.ViewListener() {
			@Override
			public void onFinishPart(int partIndex) {
				showFinishStepAlert(partIndex);
			}

			@Override
			public void onStartSession() {
				if (fragmentView != null) {
					currentTimer = 0;
					steps.get(0).isRunning = true;
					fragmentView.startTimer(currentTimer, steps.get(currentTimer));
				}
			}

			@Override
			public void onPauseSession() {
				if (fragmentView != null) {
					if (steps.get(currentTimer).isRunning) {
						fragmentView.pauseTimer(currentTimer);
						steps.get(currentTimer).isRunning = false;
					} else {
						fragmentView.resumeTimer(currentTimer);
						steps.get(currentTimer).isRunning = true;
					}
				}
			}
		});
	}

	private void playSound() {
		try {
			Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
			Ringtone r = RingtoneManager.getRingtone(viewContextInject(Context.class),
													 notification);
			r.play();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		if (fragmentView != null) {
			fragmentView.pauseTimer(currentTimer);
		}
	}

	private void showFinishStepAlert(final int step) {
		playSound();
		String key = steps.get(step).name;
		steps.get(step).duration = 0;
		steps.get(step).isRunning = false;
		new android.support.v7.app.AlertDialog.Builder(viewContextInject(Context.class)).setIcon(
			R.drawable.logo)
			.setTitle(String.format(
				viewContextInject(Context.class).getString(R.string.finished_with_format), key))
			.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					int nextStep = step + 1;
					if (fragmentView != null) {
						if (nextStep < steps.size()) {
							currentTimer = nextStep;
							steps.get(nextStep).isRunning = true;
							fragmentView.startTimer(nextStep, steps.get(nextStep));
						} else {
							finishSession();
						}
					}
				}

			})
			.show();
	}

	private void showFinisAlert() {
		new android.support.v7.app.AlertDialog.Builder(viewContextInject(Context.class)).setIcon(
			R.drawable.logo)
			.setTitle(R.string.done)
			.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					goToSurvey();
				}

			})
			.show();
	}

	private void goToSurvey() {
		SurveyFragment surveyFragment = SurveyFragment.newInstance();
		viewContextInject(ActivityNavigator.class).navigate(surveyFragment);
	}

	private void finishSession() {
		showFinisAlert();
	}

	@Override
	protected void onViewCreated() {
		if (fragmentView != null && !steps.isEmpty()) {
			fragmentView.paintElementsInView(steps);
		}
	}
}
