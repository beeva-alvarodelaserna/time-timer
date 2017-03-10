package com.beeva.labs.timetimer.splash.multipart.session;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.beeva.labs.timetimer.R;
import com.beeva.labs.timetimer.support.ui.SessionStep;
import com.beeva.labs.timetimer.support.ui.TimerView;
import java.util.ArrayList;
import java.util.List;

public class TimerRenderer {

	private final ItemSubtypesRecyclerViewAdapter adapter;
	private final MultipartSessionView.ViewListener viewListener;

	TimerRenderer(
		RecyclerView recyclerView, MultipartSessionView.ViewListener viewListener) {
		adapter = new ItemSubtypesRecyclerViewAdapter();
		this.viewListener = viewListener;
		recyclerView.setAdapter(adapter);
		recyclerView.setLayoutManager(new GridLayoutManager(recyclerView.getContext(), 2));
	}

	void render(ArrayList<SessionStep> items) {
		adapter.setData(items);
	}

	void updateSteps(ArrayList<SessionStep> steps) {
		adapter.setData(steps);
		adapter.notifyDataSetChanged();
	}

	private class ItemSubtypesRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

		private final List<SessionStep> subtypes;

		ItemSubtypesRecyclerViewAdapter() {
			this.subtypes = new ArrayList<>();
		}

		@Override
		public int getItemViewType(int position) {
			return position;
		}

		@Override
		public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			RecyclerView.ViewHolder viewHolder;
			LayoutInflater inflater = LayoutInflater.from(parent.getContext());
			View view = inflater.inflate(R.layout.timer_with_text_layout, parent, false);
			viewHolder = new TimerViewHolder(view);
			return viewHolder;
		}

		@Override
		public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
			configureViewHolder((TimerViewHolder) holder, position);
		}

		@Override
		public int getItemCount() {
			return subtypes.size();
		}

		private void configureViewHolder(TimerViewHolder holder, int position) {
			final SessionStep step = subtypes.get(position);
			if (step != null) {
				holder.paintName(step.name);
				holder.setTimer(step.duration);
				holder.setCircleColor(step.circleColor);
				holder.resize(step.isRunning);
			}
		}

		void setData(List<SessionStep> items) {
			subtypes.clear();
			subtypes.addAll(items);
			notifyDataSetChanged();
		}
	}

	private class TimerViewHolder extends RecyclerView.ViewHolder {

		private final TextView name;
		private final TimerView timer;

		TimerViewHolder(View itemView) {
			super(itemView);
			timer = (TimerView) itemView.findViewById(R.id.timer);
			name = (TextView) itemView.findViewById(R.id.name);
		}

		void paintName(String msg) {
			name.setText(msg);
		}

		void setTimer(int duration) {
			float angle = (3600 - duration*60)/3600f;
			timer.drawProgress(angle, false);
		}

		void setCircleColor(int color) {
			timer.setCircleColor(color);
		}

		void resize(boolean isRunning) {
			ViewGroup.LayoutParams params = timer.getLayoutParams();
			if (isRunning) {
				params.width = 400;
				timer.setLayoutParams(params);
			} else {
				params.width = 150;
				timer.setLayoutParams(params);
			}
		}
	}
}
