package com.beeva.labs.timetimer.splash.multipart.setup.duration_and_name_of_steps;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Point;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.beeva.labs.timetimer.R;
import com.beeva.labs.timetimer.instruments.utils.StringUtils;
import com.beeva.labs.timetimer.support.base.BaseFragmentView;
import com.beeva.labs.timetimer.support.base.ViewNavigator;
import com.beeva.labs.timetimer.support.ui.TextWatcher;
import com.beeva.labs.timetimer.support.ui.TimerView;
import com.beeva.labs.timetimer.support.ui.ToastUtils;
import com.rey.material.widget.Slider;

class MultipartSetupDurationAndNameOfStepsView extends BaseFragmentView {
	
	private final ViewListener viewListener;
	
	private int numberOfSteps, currentStep, timerValue, width, height, TIMER_LENGTH, totalDuration;
	private float timerAngle;
	private float MAX_VALUE_IN_MINUTES_FLOAT = 3600F;
	private int MAX_VALUE_IN_MINUTES = 3600;
	private float MAX_VALUE_IN_SECONDS_FLOAT = 60F;
	private int MAX_VALUE_IN_SECONDS = 60;
	private final float CIRCLE = 360F; // 360 degrees
	
	private Button duration;
	private TextInputEditText partNameInput;
	private String partName;
	private Button okButton;
	private TimerView timerView;
	private Slider slider;
	private Toolbar toolbar;
	
	MultipartSetupDurationAndNameOfStepsView(ViewListener viewListener) {
		super(R.layout.multipart_setup_duration_and_name_layout);
		this.viewListener = viewListener;
	}
	
	@Override
	protected void setUpView(View view) {
		toolbar = (Toolbar) view.findViewById(R.id.multipart_setup_duration_and_name_toolbar);
		viewContextInject(ViewNavigator.class).setUpNavigation(toolbar);
		Display display = ((Activity) viewContextInject(Context.class)).getWindowManager()
			.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		width = size.x;
		height = size.y;
		duration = (Button) view.findViewById(R.id.multipart_setup_duration_and_name_duration);
		okButton = (Button) view.findViewById(
			R.id.multipart_setup_duration_and_name_btn_timer_start);
		partNameInput = (TextInputEditText) view.findViewById(
			R.id.multipart_setup_duration_and_name_part_name);
		partNameInput.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
				partName = s.toString();
			}
		});
		okButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (currentStep < numberOfSteps - 1) {
					if (!StringUtils.isNullOrEmpty(partName)) {
						addStep();
					} else {
						ToastUtils.showShort(viewContextInject(Context.class),
											 "Name cannot be empty");
					}
				} else {
					addStep();
					viewListener.onFinish();
				}
			}
		});
		timerView = (TimerView) view.findViewById(R.id.multipart_setup_duration_and_name_timer);
		view.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				Point point = new Point((int) event.getX(), (int) event.getY());
				float angle = timerView.getAngle(point, height, width);
				timerView.drawProgressWithAngle(angle);
				TIMER_LENGTH = (int) (angle * 60F / CIRCLE);
				timerValue = TIMER_LENGTH;
				timerAngle = angle;
				float percentValue = (TIMER_LENGTH / MAX_VALUE_IN_SECONDS_FLOAT) * 100;
				slider.setValue(percentValue, true);
				duration.setText(String.valueOf(TIMER_LENGTH));
				return false;
			}
		});
		slider = (Slider) view.findViewById(R.id.multipart_setup_duration_and_name_slider);
		slider.setOnPositionChangeListener(new Slider.OnPositionChangeListener() {
			@Override
			public void onPositionChanged(
				Slider view, boolean fromUser, float oldPos, float newPos, int oldValue,
				int newValue) {
				float percentPosition = newValue / 100F;
				TIMER_LENGTH = (int) (percentPosition * MAX_VALUE_IN_SECONDS);
				timerValue = TIMER_LENGTH;
				timerAngle = CIRCLE * percentPosition;
				float progress = (MAX_VALUE_IN_SECONDS - TIMER_LENGTH) / MAX_VALUE_IN_SECONDS_FLOAT;
				timerView.drawProgress(progress, false);
				duration.setText(String.valueOf(TIMER_LENGTH));
			}
		});
	}
	
	private void addStep() {
		if (timerValue > 0) {
			viewListener.onAddStepInfo(partName, timerValue, timerAngle);
		} else {
			ToastUtils.showShort(viewContextInject(Context.class), "Duration cannot be 0");
		}
	}
	
	void initSessionSetup(int numberOfSteps) {
		currentStep = 0;
		this.numberOfSteps = numberOfSteps;
		setTitle(currentStep);
	}
	
	private void setTitle(int currentStep) {
		toolbar.setTitle(String.format(viewContextInject(Context.class).getString(
			R.string.set_the_duration_of_part_with_format), String.valueOf(currentStep + 1)));
	}
	
	void continueSettingUp() {
		currentStep++;
		setTitle(currentStep);
		partNameInput.setText("");
		if (currentStep == numberOfSteps - 1) {
			okButton.setText(viewContextInject(Context.class).getString(R.string.done));
			okButton.setBackgroundTintList(ColorStateList.valueOf(
				viewContextInject(Context.class).getResources()
					.getColor(R.color.errorColor)));
		}
		timerView.stop();
		timerValue = 0;
		duration.setText("0");
		slider.setValue(0f, false);
	}
	
	void initSessionSetup(int totalDurationInMinutes, int numberOfSteps) {
		initSessionSetup(numberOfSteps);
		this.totalDuration = totalDurationInMinutes;
		timerView.setMaxSeconds(totalDurationInMinutes);
		MAX_VALUE_IN_SECONDS = totalDurationInMinutes * 60;
		MAX_VALUE_IN_MINUTES = totalDurationInMinutes * 3600;
		MAX_VALUE_IN_SECONDS_FLOAT = totalDurationInMinutes * 60F;
		MAX_VALUE_IN_MINUTES_FLOAT = totalDurationInMinutes * 3600F;
	}
	
	interface ViewListener {
		
		void onFinish();
		
		void onAddStepInfo(String name, int duration, float timerAngle);
		
	}
	
}
