package com.beeva.labs.timetimer.splash.multipart.session;

import android.content.Context;
import android.content.res.ColorStateList;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import com.bbva.kst.uniqueid.R;
import com.beeva.labs.timetimer.splash.multipart.session_concentric.MultipartSessionConcentricView;
import com.beeva.labs.timetimer.support.base.BaseFragmentView;
import com.beeva.labs.timetimer.support.ui.SessionStep;
import com.beeva.labs.timetimer.support.ui.TimerView;
import java.util.ArrayList;

public class MultipartSessionView extends BaseFragmentView {

	private final ViewListener viewListener;
	private TimerRenderer renderer;
	private Button startButton;
	private RecyclerView recyclerView;

	MultipartSessionView(ViewListener viewListener) {
		super(R.layout.multipart_session_layout);
		this.viewListener = viewListener;
	}

	@Override
	protected void setUpView(View view) {
		recyclerView = (RecyclerView) view.findViewById(R.id.multipart_session_recycler_view);
		renderer = new TimerRenderer(recyclerView, viewListener);
		startButton = (Button) view.findViewById(R.id.multipart_session_button_ok);
		startButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (startButton.getText()
					.equals(viewContextInject(Context.class).getString(R.string.start))) {
					viewListener.onStartSession();
				} else {
					viewListener.onPauseSession();
				}
			}
		});
	}

	void paintElementsInView(ArrayList<SessionStep> items) {
		renderer.render(items);
	}

	void startTimer(int position, SessionStep step) {
		RelativeLayout layout = (RelativeLayout) recyclerView.getChildAt(position);
		TimerView timerView = (TimerView) layout.getChildAt(0);
		int value = step.duration;
		//timerView.start(position, value * 60, viewListener);
		timerView.startInSeconds(position, value, viewListener);
	}

	void paintButtonBlue() {
		startButton.setText(R.string.resume);
		startButton.setBackgroundTintList(ColorStateList.valueOf(
			viewContextInject(Context.class).getResources()
				.getColor(R.color.colorAccent)));
	}

	void paintButtonYellow() {
		startButton.setText(R.string.stop);
		startButton.setBackgroundTintList(ColorStateList.valueOf(
			viewContextInject(Context.class).getResources()
				.getColor(R.color.errorColor)));
	}

	void stopTimer(int position) {
		RelativeLayout layout = (RelativeLayout) recyclerView.getChildAt(position);
		TimerView timerView = (TimerView) layout.getChildAt(0);
		timerView.stop();
	}

	void pauseTimer(int position) {
		RelativeLayout layout = (RelativeLayout) recyclerView.getChildAt(position);
		TimerView timerView = (TimerView) layout.getChildAt(0);
		timerView.pause();
	}

	void resumeTimer(int position) {
		RelativeLayout layout = (RelativeLayout) recyclerView.getChildAt(position);
		TimerView timerView = (TimerView) layout.getChildAt(0);
		timerView.resume();
	}

	void updateSteps(ArrayList<SessionStep> steps) {
		renderer.updateSteps(steps);
	}

	public interface ViewListener {

		void onFinishPart(int partIndex);

		void onStartSession();

		void onPauseSession();
	}

}
