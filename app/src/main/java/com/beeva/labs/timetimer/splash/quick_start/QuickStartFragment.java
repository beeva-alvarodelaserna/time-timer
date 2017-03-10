package com.beeva.labs.timetimer.splash.quick_start;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.speech.RecognizerIntent;
import com.beeva.labs.timetimer.R;
import com.beeva.labs.timetimer.splash.survey.SurveyFragment;
import com.beeva.labs.timetimer.support.base.ActivityNavigator;
import com.beeva.labs.timetimer.support.base.BaseFragment;
import com.beeva.labs.timetimer.support.base.BaseInteractor;
import com.beeva.labs.timetimer.support.ui.ToastUtils;
import java.util.ArrayList;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

public class QuickStartFragment extends BaseFragment<QuickStartView, BaseInteractor> {

	private final int REQ_CODE_SPEECH_INPUT = 100;

	public static QuickStartFragment newInstance() {
		return new QuickStartFragment();
	}

	@Override
	protected BaseInteractor getInteractor() {
		return BaseInteractor.EMPTY_INTERACTOR;
	}

	@Override
	protected QuickStartView getFragmentView() {
		return new QuickStartView(new QuickStartView.ViewListener() {

			@Override
			public void onTimerStart() {
				if (fragmentView != null) {
					fragmentView.startTimer();
				}
			}

			@Override
			public void onTimerStop() {
				if (fragmentView != null) {
					fragmentView.stopTimer();
				}
			}

			@Override
			public void onTimerFinished() {
				if (fragmentView != null) {
					fragmentView.stopTimer();
				}
				showFinishAlert();
			}

			@Override
			public void onPromptVoiceInput() {
				promptSpeechInput();
			}
			
		});
	}

	private void showFinishAlert() {
		new android.support.v7.app.AlertDialog.Builder(viewContextInject(Context.class)).setIcon(
			R.drawable.logo)
			.setTitle(R.string.finished)
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

	private void promptSpeechInput() {
		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
						RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
		intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
						"Say a number");
		try {
			startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
		} catch (ActivityNotFoundException a) {
			ToastUtils.showShort(viewContextInject(Context.class), "Voice input unsupported");
		}
	}

	/**
	 * Receiving speech input
	 * */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
			case REQ_CODE_SPEECH_INPUT: {
				if (resultCode == RESULT_OK && null != data) {

					ArrayList<String> result = data
						.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
					ToastUtils.show(viewContextInject(Context.class), "You said: " + result.get(0));
				}
				break;
			}

		}
	}
}
