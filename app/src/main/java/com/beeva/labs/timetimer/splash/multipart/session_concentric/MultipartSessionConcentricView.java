package com.beeva.labs.timetimer.splash.multipart.session_concentric;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bbva.kst.uniqueid.R;
import com.beeva.labs.timetimer.splash.multipart.session.MultipartSessionView;
import com.beeva.labs.timetimer.support.base.BaseFragmentView;
import com.beeva.labs.timetimer.support.ui.SessionStep;
import com.beeva.labs.timetimer.support.ui.TimerView;
import java.util.ArrayList;

public class MultipartSessionConcentricView extends BaseFragmentView {
	
	private final ViewListener viewListener;
	private RelativeLayout container;
	private ArrayList<TimerView> timerList;
	private TextView title;
	private boolean isRunning, isPaused;
	private String sessionString;
	private SpannableString styledString;
	
	MultipartSessionConcentricView(ViewListener viewListener) {
		super(R.layout.multipart_session_concentric_layout);
		this.viewListener = viewListener;
	}
	
	@Override
	protected void setUpView(View view) {
		isRunning = false;
		isPaused = false;
		container = (RelativeLayout) view.findViewById(R.id.multipart_session_concentric_container);
	}
	
	void paintElementsInView(ArrayList<SessionStep> items) {
		timerList = new ArrayList<>();
		container.removeAllViews();
		ArrayList<Integer> timerSizes = getTimerSizes(items);
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
			ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		lp.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
		lp.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
		title = new TextView(viewContextInject(Context.class));
		title.setTextSize(20f);
		int titleId = View.generateViewId();
		title.setId(titleId);
		title.setText(R.string.center_to_start);
		title.setLayoutParams(lp);
		container.addView(title);
		ImageView backgroundImage = new ImageView(viewContextInject(Context.class));
		lp = new RelativeLayout.LayoutParams(timerSizes.get(timerSizes.size()-1), timerSizes.get(timerSizes.size()-1));
		lp.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
		backgroundImage.setBackground(viewContextInject(Context.class).getDrawable(R.drawable.stopwatch));
		backgroundImage.setLayoutParams(lp);
		container.addView(backgroundImage);
		for (int i = 0; i < timerSizes.size(); i++) {
			TimerView timer = getTimer(timerSizes.get(i), items, i);
			timerList.add(timer);
			container.addView(timer);
		}
		container.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!isRunning && !isPaused) {
					viewListener.onStartSession();
				} else {
					viewListener.onPauseSession();
				}
			}
		});
	}
	
	private TimerView getTimer(int size, ArrayList<SessionStep> items, int index) {
		TimerView timerView = new TimerView(viewContextInject(Context.class));
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(size, size);
		lp.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
		timerView.setLayoutParams(lp);
		float rotation = 0f;
		for (int i = index + 1; i < items.size(); i++) {
			rotation += items.get(i).timerAngle;
		}
		timerView.setRotation(rotation);
		timerView.setCircleColor(Color.GRAY);
		timerView.drawProgressWithAngle(items.get(index).timerAngle);
		return timerView;
		
	}
	
	private ArrayList<Integer> getTimerSizes(ArrayList<SessionStep> items) {
		int minSize = 800;
		ArrayList<Integer> sizes = new ArrayList<>();
		sizes.add(minSize);
		for (int i = 1; i < items.size(); i++) {
			//minSize += 80;
			sizes.add(minSize);
		}
		return sizes;
	}
	
	void startTimer(int position, SessionStep step) {
		TimerView timerView = timerList.get(position);
		int value = step.duration;
		timerView.setCircleColor(Color.GREEN);
		//timerView.start(position, value * 60, viewListener);
		timerView.startInSeconds(position, value, viewListener);
		isRunning = true;
		sessionString = String.format(viewContextInject(Context.class).getString(R.string.session_running),
									  step.name);
		styledString = new SpannableString(sessionString);
		styledString.setSpan(new StyleSpan(Typeface.BOLD), 8, 8 + step.name.length(), 0);
		title.setText(styledString);
	}
	
	void stopTimer(int position) {
		TimerView timerView = timerList.get(position);
		timerView.stop();
	}
	
	void pauseTimer(int position) {
		TimerView timerView = timerList.get(position);
		timerView.pause();
		isRunning = false;
		isPaused = true;
		title.setText(R.string.paused);
	}
	
	void resumeTimer(int position) {
		TimerView timerView = timerList.get(position);
		timerView.resume();
		title.setText(styledString);
		isRunning = false;
		isPaused = false;
	}
	
	public interface ViewListener extends MultipartSessionView.ViewListener {
		
		void onFinishPart(int partIndex);
		
		void onStartSession();
		
		void onPauseSession();
	}
	
}
