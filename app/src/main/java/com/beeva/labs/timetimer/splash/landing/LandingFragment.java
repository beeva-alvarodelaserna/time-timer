package com.beeva.labs.timetimer.splash.landing;

import android.content.Context;
import com.bbva.kst.uniqueid.R;
import com.beeva.labs.timetimer.splash.multipart.setup.number_of_steps.MultipartSetupNumberOfStepsFragment;
import com.beeva.labs.timetimer.splash.quick_start.QuickStartFragment;
import com.beeva.labs.timetimer.support.base.ActivityNavigator;
import com.beeva.labs.timetimer.support.base.BaseFragment;
import com.beeva.labs.timetimer.support.base.BaseInteractor;
import com.beeva.labs.timetimer.support.ui.ToastUtils;

public class LandingFragment extends BaseFragment<LandingView, BaseInteractor> {

	public static LandingFragment newInstance() {
		return new LandingFragment();
	}

	@Override
	protected BaseInteractor getInteractor() {
		return BaseInteractor.EMPTY_INTERACTOR;
	}

	@Override
	protected LandingView getFragmentView() {
		return new LandingView(new LandingView.ViewListener() {
			@Override
			public void onQuickStart() {
				QuickStartFragment quickStartFragment = QuickStartFragment.newInstance();
				viewContextInject(ActivityNavigator.class).navigate(quickStartFragment);
			}

			@Override
			public void onFavorites() {
				showNotImplementedMessage();
			}

			@Override
			public void onMultipart() {
				MultipartSetupNumberOfStepsFragment multipartSetupFragment = MultipartSetupNumberOfStepsFragment.newInstance();
				viewContextInject(ActivityNavigator.class).navigate(multipartSetupFragment);
			}
		});
	}

	private void showNotImplementedMessage() {
		ToastUtils.show(viewContextInject(Context.class), getString(R.string.not_implemented));
	}

	@Override
	protected void onViewCreated() {
	}
}
