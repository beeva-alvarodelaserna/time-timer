package com.beeva.labs.timetimer.splash.end_session;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import com.beeva.labs.timetimer.support.base.ActivityNavigator;
import com.beeva.labs.timetimer.support.base.BaseFragment;
import com.beeva.labs.timetimer.support.base.BaseInteractor;

public class EndSessionFragment extends BaseFragment<EndSessionView, BaseInteractor> {
	
	private static final String TAG_RESULT = "result";
	private int result;

	public static EndSessionFragment newInstance() {
		return new EndSessionFragment();
	}
	
	public static EndSessionFragment newInstance(int result) {
		EndSessionFragment endSessionFragment = EndSessionFragment.newInstance();
		Bundle args = new Bundle();
		args.putInt(TAG_RESULT, result);
		endSessionFragment.setArguments(args);
		return endSessionFragment;
	}
	
	@Override
	protected void loadArguments(Bundle arguments) {
		if (arguments.containsKey(TAG_RESULT)) {
			result = arguments.getInt(TAG_RESULT);
		}
	}
	
	@Override
	protected BaseInteractor getInteractor() {
		return BaseInteractor.EMPTY_INTERACTOR;
	}

	@Override
	protected EndSessionView getFragmentView() {
		return new EndSessionView(new EndSessionView.ViewListener() {
			
			@Override
			public void onNew() {
				goBack();
			}
			
			@Override
			public void onQuit() {
				quit();
			}
		});
	}
	
	@Override
	protected void onViewCreated() {
		super.onViewCreated();
		if (fragmentView != null) {
			fragmentView.setupViewWithResult(result);
		}
	}

	private void goBack() {
		viewContextInject(ActivityNavigator.class).navigateUp();
	}

	private void quit() {
		Activity activity = (Activity) viewContextInject(Context.class);
		activity.finish();
	}
	
}
