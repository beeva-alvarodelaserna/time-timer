package com.beeva.labs.timetimer.splash.landing;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.beeva.labs.timetimer.R;
import com.beeva.labs.timetimer.support.base.BaseFragmentView;

class LandingView extends BaseFragmentView {

	private final ViewListener viewListener;

	private LinearLayout tv_QuickStart;
	private TextView tv_Favorites, tv_MultiPart;


	LandingView(ViewListener viewListener) {
		super(R.layout.landing_layout);
		this.viewListener = viewListener;
	}

	@Override
	protected void setUpView(View view) {
		tv_QuickStart = (LinearLayout) view.findViewById(R.id.landing_layout_button_quick_start);
		//tv_Favorites = (TextView) view.findViewById(R.id.landing_layout_button_saved_sessions);
		tv_MultiPart = (TextView) view.findViewById(R.id.landing_layout_button_multipart);
		tv_QuickStart.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				viewListener.onQuickStart();
			}
		});
		//tv_Favorites.setOnClickListener(new View.OnClickListener() {
		//	@Override
		//	public void onClick(View v) {
		//		viewListener.onFavorites();
		//	}
		//});
		tv_MultiPart.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				viewListener.onMultipart();
			}
		});
	}

	interface ViewListener {

		void onQuickStart();

		void onFavorites();

		void onMultipart();

	}

}
