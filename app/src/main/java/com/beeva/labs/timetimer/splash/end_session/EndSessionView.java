package com.beeva.labs.timetimer.splash.end_session;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.bbva.kst.uniqueid.R;
import com.beeva.labs.timetimer.support.base.BaseFragmentView;

public class EndSessionView extends BaseFragmentView {
	
	private final ViewListener viewListener;
	
	private Button buttonNew, buttonQuit;
	private TextView message;
	
	EndSessionView(ViewListener viewListener) {
		super(R.layout.end_session_layout);
		this.viewListener = viewListener;
	}
	
	@Override
	protected void setUpView(View view) {
		buttonNew = (Button) view.findViewById(R.id.end_session_button_new);
		buttonQuit = (Button) view.findViewById(R.id.end_session_button_quit);
		buttonNew.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				viewListener.onNew();
			}
		});
		buttonQuit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				viewListener.onQuit();
			}
		});
		message = (TextView) view.findViewById(R.id.end_session_message);
	}
	
	void setupViewWithResult(int result) {
		switch (result) {
			case 1:
				message.setText(R.string.nice_job);
				break;
			case 2:
				message.setText(R.string.next_time_better);
				break;
			default:
				message.setText(R.string.job_well_done);
				break;
		}
	}
	
	public interface ViewListener {
		
		void onNew();
		
		void onQuit();
		
	}
	
}
