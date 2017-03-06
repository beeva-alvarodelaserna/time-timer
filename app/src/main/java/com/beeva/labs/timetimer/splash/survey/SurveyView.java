package com.beeva.labs.timetimer.splash.survey;

import android.view.View;
import android.widget.LinearLayout;
import com.bbva.kst.uniqueid.R;
import com.beeva.labs.timetimer.support.base.BaseFragmentView;

public class SurveyView extends BaseFragmentView {
	
	private final ViewListener viewListener;
	
	private LinearLayout buttonYes, buttonMeh, buttonNo;
	
	SurveyView(ViewListener viewListener) {
		super(R.layout.survey_layout);
		this.viewListener = viewListener;
	}
	
	@Override
	protected void setUpView(View view) {
		buttonYes = (LinearLayout) view.findViewById(R.id.survey_button_yes);
		buttonMeh = (LinearLayout) view.findViewById(R.id.survey_button_meh);
		buttonNo= (LinearLayout) view.findViewById(R.id.survey_button_no);
		buttonYes.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				viewListener.onYes();
			}
		});
		buttonMeh.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				viewListener.onMeh();
			}
		});
		buttonNo.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				viewListener.onNo();
			}
		});
	}
	
	public interface ViewListener {
		
		void onYes();
		
		void onMeh();
		
		void onNo();
	}
	
}
