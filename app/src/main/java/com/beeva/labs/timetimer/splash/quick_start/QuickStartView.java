package com.beeva.labs.timetimer.splash.quick_start;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Point;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import com.bbva.kst.uniqueid.R;
import com.beeva.labs.timetimer.support.base.BaseFragmentView;
import com.beeva.labs.timetimer.support.ui.TimerView;
import com.rey.material.widget.Slider;

public class QuickStartView extends BaseFragmentView {
	
	private final ViewListener viewListener;
	
	private int TIMER_LENGTH = 15;
	private final float MAX_VALUE_IN_MINUTES_FLOAT = 3600F;
	private final float MAX_VALUE_IN_SECONDS_FLOAT = 60F;
	private final int MAX_VALUE_IN_MINUTES = 3600;
	private final int MAX_VALUE_IN_SECONDS = 60;
	private final float CIRCLE = 360F; // 360 degrees
	
	private TimerView mTimerView;
	private Button timerStartButton;
	private Slider slider;
	private Button duration;
	private ImageButton btnSpeak;
	private int width;
	private int height;
	private boolean timerIsRunning = false, timerIsPaused = false;
	
	QuickStartView(ViewListener viewListener) {
		super(R.layout.quick_start_layout);
		this.viewListener = viewListener;
	}
	
	@Override
	protected void setUpView(View view) {
		Display display = ((Activity) viewContextInject(Context.class)).getWindowManager()
			.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		width = size.x;
		height = size.y;
		mTimerView = (TimerView) view.findViewById(R.id.timer);
		duration = (Button) view.findViewById(R.id.quick_start_duration);
		slider = (Slider) view.findViewById(R.id.quick_start_slider);
		slider.setOnPositionChangeListener(new Slider.OnPositionChangeListener() {
			@Override
			public void onPositionChanged(
				Slider view, boolean fromUser, float oldPos, float newPos, int oldValue,
				int newValue) {
				float percentPosition = newValue / 100F;
				TIMER_LENGTH = (int) (percentPosition * MAX_VALUE_IN_SECONDS);
				float progress = (MAX_VALUE_IN_SECONDS - TIMER_LENGTH) / MAX_VALUE_IN_SECONDS_FLOAT;
				mTimerView.drawProgress(progress, false);
				duration.setText(String.valueOf(TIMER_LENGTH));
			}
		});
		timerStartButton = (Button) view.findViewById(R.id.quick_start_btn_timer_start);
		timerStartButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startOrStop();
			}
		});
		view.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				Point point = new Point((int) event.getX(), (int) event.getY());
				float angle = mTimerView.getAngle(point, height, width);
				mTimerView.drawProgressWithAngle(angle);
				TIMER_LENGTH = (int) (angle * 60F / CIRCLE);
				float percentValue = (TIMER_LENGTH / MAX_VALUE_IN_SECONDS_FLOAT) * 100;
				slider.setValue(percentValue, true);
				duration.setText(String.valueOf(TIMER_LENGTH));
				return false;
			}
		});
		btnSpeak = (ImageButton) view.findViewById(R.id.quick_start_btnSpeak);
		btnSpeak.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				viewListener.onPromptVoiceInput();
			}
		});
	}
	
	private void startOrStop() {
		if (timerIsRunning) {
			viewListener.onTimerStop();
		} else {
			viewListener.onTimerStart();
		}
	}
	
	void startTimer() {
		if (timerIsPaused) {
			timerIsRunning = true;
			timerIsPaused = false;
			paintButtonYellow();
			mTimerView.setCircleColor(viewContextInject(Context.class).getResources()
										  .getColor(R.color.errorColor));
			mTimerView.resume();
		} else {
			timerIsRunning = true;
			timerIsPaused = false;
			paintButtonYellow();
			mTimerView.setCircleColor(viewContextInject(Context.class).getResources()
										  .getColor(R.color.errorColor));
			//mTimerView.start(TIMER_LENGTH * 60, viewListener);
			mTimerView.startInSeconds(TIMER_LENGTH, viewListener);
		}
	}
	
	void stopTimer() {
		timerIsRunning = false;
		timerIsPaused = true;
		mTimerView.pause();
		paintButtonBlue();
	}
	
	void paintButtonBlue() {
		timerStartButton.setText(R.string.resume);
		timerStartButton.setBackgroundTintList(ColorStateList.valueOf(
			viewContextInject(Context.class).getResources()
				.getColor(R.color.colorAccent)));
	}
	
	void paintButtonYellow() {
		timerStartButton.setText(R.string.stop);
		timerStartButton.setBackgroundTintList(ColorStateList.valueOf(
			viewContextInject(Context.class).getResources()
				.getColor(R.color.errorColor)));
	}
	
	public interface ViewListener {
		
		void onTimerStart();
		
		void onTimerStop();
		
		void onTimerFinished();
		
		void onPromptVoiceInput();
	}
	
}
