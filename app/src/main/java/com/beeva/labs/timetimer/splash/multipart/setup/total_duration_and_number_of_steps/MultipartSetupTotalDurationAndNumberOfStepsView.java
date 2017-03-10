package com.beeva.labs.timetimer.splash.multipart.setup.total_duration_and_number_of_steps;

import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.view.View;
import android.widget.LinearLayout;
import com.beeva.labs.timetimer.R;
import com.beeva.labs.timetimer.instruments.utils.StringUtils;
import com.beeva.labs.timetimer.support.base.BaseFragmentView;
import com.beeva.labs.timetimer.support.base.ViewNavigator;
import com.beeva.labs.timetimer.support.ui.NumberPicker;
import com.beeva.labs.timetimer.support.ui.TextWatcher;

class MultipartSetupTotalDurationAndNumberOfStepsView extends BaseFragmentView {
	
	private final ViewListener viewListener;
	
	private int numberOfSteps = 2, totalDuration = 60;
	
	MultipartSetupTotalDurationAndNumberOfStepsView(ViewListener viewListener) {
		super(R.layout.multipart_setup_total_duration_and_number_of_steps_layout);
		this.viewListener = viewListener;
	}
	
	@Override
	protected void setUpView(View view) {
		Toolbar toolbar = (Toolbar) view.findViewById(
			R.id.multipart_setup_total_duration_and_number_toolbar);
		viewContextInject(ViewNavigator.class).setUpNavigation(toolbar);
		TextInputEditText editText = (TextInputEditText) view.findViewById(
			R.id.multipart_setup_total_duration_and_number_duration);
		editText.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
				String value = s.toString();
				if (!StringUtils.isNullOrEmpty(value)) {
					totalDuration = Integer.parseInt(value);
				}
			}
		});
		NumberPicker numberPicker = (NumberPicker) view.findViewById(
			R.id.multipart_setup_total_duration_and_number_number_picker);
		numberPicker.setOnValueChangedListener(
			new android.widget.NumberPicker.OnValueChangeListener() {
				@Override
				public void onValueChange(
					android.widget.NumberPicker picker, int oldVal, int newVal) {
					numberOfSteps = newVal;
				}
			});
		LinearLayout okButton = (LinearLayout) view.findViewById(
			R.id.multipart_setup_total_duration_and_number_ok_button);
		okButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				viewListener.onContinue(totalDuration, numberOfSteps);
			}
		});
	}
	
	interface ViewListener {
		
		void onContinue(int totalDuration, int numberOfSteps);
		
	}
	
}
