package com.beeva.labs.timetimer.splash.multipart.setup.number_of_steps;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import com.beeva.labs.timetimer.R;
import com.beeva.labs.timetimer.support.base.BaseFragmentView;
import com.beeva.labs.timetimer.support.base.ViewNavigator;
import com.beeva.labs.timetimer.support.ui.NumberPicker;

class MultipartSetupNumberOfStepsView extends BaseFragmentView {

	private final ViewListener viewListener;

	private int numberOfSteps = 2;

	MultipartSetupNumberOfStepsView(ViewListener viewListener) {
		super(R.layout.multipart_setup_number_of_steps_layout);
		this.viewListener = viewListener;
	}

	@Override
	protected void setUpView(View view) {
		Toolbar toolbar = (Toolbar) view.findViewById(R.id.multipart_setup_toolbar);
		viewContextInject(ViewNavigator.class).setUpNavigation(toolbar);
		NumberPicker numberPicker = (NumberPicker) view.findViewById(
			R.id.multipart_setup_number_picker);
		numberPicker.setOnValueChangedListener(
			new android.widget.NumberPicker.OnValueChangeListener() {
				@Override
				public void onValueChange(
					android.widget.NumberPicker picker, int oldVal, int newVal) {
					numberOfSteps = newVal;
				}
			});
		LinearLayout okButton = (LinearLayout) view.findViewById(R.id.multipart_setup_ok_button);
		okButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				viewListener.onContinue(numberOfSteps);
			}
		});
	}

	interface ViewListener {

		void onContinue(int numberOfSteps);

	}

}
