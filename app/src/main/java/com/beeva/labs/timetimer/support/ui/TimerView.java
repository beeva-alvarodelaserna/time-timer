package com.beeva.labs.timetimer.support.ui;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;
import com.bbva.kst.uniqueid.R;
import com.beeva.labs.timetimer.splash.multipart.session.MultipartSessionView;
import com.beeva.labs.timetimer.splash.multipart.session_concentric.MultipartSessionConcentricView;
import com.beeva.labs.timetimer.splash.quick_start.QuickStartView;
import java.util.concurrent.TimeUnit;

public class TimerView extends View {
	
	private static final int ARC_START_ANGLE = 270; // 12 o'clock
	private static final float THICKNESS_SCALE = 0.5f;
	private int MAX_SECONDS_ONE_HOUR = 3600;
	private int MAX_SECONDS_ONE_MINUTE = 60;
	private float MAX_SECONDS_ONE_HOUR_FLOAT = 3600F;
	private float MAX_SECONDS_ONE_MINUTE_FLOAT = 60F;
	
	private int REMAINING_TIME_IN_SECONDS;
	
	private Bitmap mBitmap;
	private Canvas mCanvas;
	
	private RectF mCircleOuterBounds;
	private RectF mCircleInnerBounds;
	
	private Paint mCirclePaint, mOuterCirclePaint;
	private Paint mEraserPaint;
	
	private float mCircleSweepAngle;
	
	private ValueAnimator mTimerAnimator;
	
	public TimerView(Context context) {
		this(context, null);
	}
	
	public TimerView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public TimerView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		int circleColor = Color.RED;
		if (attrs != null) {
			TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TimerView);
			if (ta != null) {
				circleColor = ta.getColor(R.styleable.TimerView_circleColor, circleColor);
				ta.recycle();
			}
		}
		mCirclePaint = new Paint();
		mCirclePaint.setAntiAlias(true);
		mCirclePaint.setColor(circleColor);
		mEraserPaint = new Paint();
		mEraserPaint.setAntiAlias(true);
		mEraserPaint.setColor(Color.TRANSPARENT);
		mEraserPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
		mOuterCirclePaint = new Paint();
		mOuterCirclePaint.setStyle(Paint.Style.STROKE);
	}
	
	public void setMaxSeconds(int maxMinutes) {
		MAX_SECONDS_ONE_MINUTE = maxMinutes * 60;
		MAX_SECONDS_ONE_HOUR = maxMinutes * 3600;
		MAX_SECONDS_ONE_MINUTE_FLOAT = maxMinutes * 60F;
		MAX_SECONDS_ONE_HOUR_FLOAT = maxMinutes * 3600F;
		REMAINING_TIME_IN_SECONDS = MAX_SECONDS_ONE_MINUTE; // TODO: cambiar cuando sea necesario
	}
	
	public void setCircleColor(int color) {
		mCirclePaint.setColor(color);
	}
	
	@SuppressWarnings("SuspiciousNameCombination")
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, widthMeasureSpec); // Trick to make the view square
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		if (w != oldw || h != oldh) {
			mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
			mBitmap.eraseColor(Color.TRANSPARENT);
			mCanvas = new Canvas(mBitmap);
		}
		super.onSizeChanged(w, h, oldw, oldh);
		updateBounds();
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawCircle(mCircleOuterBounds.centerX(), mCircleOuterBounds.centerY(),
						  mCircleOuterBounds.height() / 2, mOuterCirclePaint);
		mCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
		if (mCircleSweepAngle > 0f) {
			mCanvas.drawArc(mCircleOuterBounds, ARC_START_ANGLE, mCircleSweepAngle, true,
							mCirclePaint);
			mCanvas.drawOval(mCircleInnerBounds, mEraserPaint);
		}
		canvas.drawBitmap(mBitmap, 0, 0, null);
	}
	
	public void start(int secs, final QuickStartView.ViewListener viewListener) {
		stop();
		float progress = (MAX_SECONDS_ONE_HOUR - secs) / MAX_SECONDS_ONE_HOUR_FLOAT;
		drawProgress(progress, false);
		mTimerAnimator = ValueAnimator.ofFloat(progress, 1f);
		mTimerAnimator.setDuration(TimeUnit.SECONDS.toMillis(secs));
		mTimerAnimator.setInterpolator(new LinearInterpolator());
		mTimerAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				float value = (float) animation.getAnimatedValue();
				drawProgress(value, false);
				if (value == 1f) {
					viewListener.onTimerFinished();
				}
			}
		});
		mTimerAnimator.start();
	}
	
	public void startInSeconds(int secs, final QuickStartView.ViewListener viewListener) {
		stop();
		float progress = (MAX_SECONDS_ONE_MINUTE - secs) / MAX_SECONDS_ONE_MINUTE_FLOAT;
		drawProgress(progress, false);
		mTimerAnimator = ValueAnimator.ofFloat(progress, 1f);
		mTimerAnimator.setDuration(TimeUnit.SECONDS.toMillis(secs));
		mTimerAnimator.setInterpolator(new LinearInterpolator());
		mTimerAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				float value = (float) animation.getAnimatedValue();
				drawProgress(value, false);
				if (value == 1f) {
					viewListener.onTimerFinished();
				}
			}
		});
		mTimerAnimator.start();
	}
	
	public ValueAnimator getTimerAnimator() {
		return mTimerAnimator;
	}
	
	public void start(
		final int position, int secs, final MultipartSessionView.ViewListener viewListener) {
		stop();
		float progress = (MAX_SECONDS_ONE_HOUR - secs) / MAX_SECONDS_ONE_HOUR_FLOAT;
		drawProgress(progress, false);
		mTimerAnimator = ValueAnimator.ofFloat(progress, 1f);
		mTimerAnimator.setDuration(TimeUnit.SECONDS.toMillis(secs));
		mTimerAnimator.setInterpolator(new LinearInterpolator());
		mTimerAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				float value = (float) animation.getAnimatedValue();
				drawProgress(value, false);
				if (value == 1f) {
					viewListener.onFinishPart(position);
				}
			}
		});
		mTimerAnimator.start();
	}
	
	public void stop() {
		if (mTimerAnimator != null && mTimerAnimator.isRunning()) {
			mTimerAnimator.cancel();
			mTimerAnimator = null;
		}
		drawProgress(0f, true);
	}
	
	public void drawProgress(float progress, boolean isFinished) {
		mCircleSweepAngle = 360 - (360 * progress);
		if (mCircleSweepAngle == 360 && isFinished) {
			mCircleSweepAngle = 0;
		}
		invalidate();
	}
	
	public void pause() {
		if (mTimerAnimator != null && mTimerAnimator.isRunning()) {
			mTimerAnimator.pause();
		}
	}
	
	public void resume() {
		if (mTimerAnimator != null && mTimerAnimator.isPaused()) {
			mTimerAnimator.resume();
		}
	}
	
	public void drawProgressWithAngle(float angle) {
		mCircleSweepAngle = angle;
		invalidate();
	}
	
	private void updateBounds() {
		final float thickness = getWidth() * THICKNESS_SCALE;
		mCircleOuterBounds = new RectF(0, 0, getWidth(), getHeight());
		mCircleInnerBounds = new RectF(mCircleOuterBounds.left + thickness,
									   mCircleOuterBounds.top + thickness,
									   mCircleOuterBounds.right - thickness,
									   mCircleOuterBounds.bottom - thickness);
		invalidate();
	}
	
	public float getAngle(Point target, int height, int width) {
		float angle = (float) Math.toDegrees(
			Math.atan2(target.y - (height / 2), target.x - (width / 2)));
		angle += 90;
		if (angle < 0) {
			angle += 360;
		}
		return angle;
	}
	
	public void startInSeconds(
		final int position, int secs,
		final MultipartSessionConcentricView.ViewListener viewListener) {
		stop();
		float progress = (MAX_SECONDS_ONE_MINUTE - secs) / MAX_SECONDS_ONE_MINUTE_FLOAT;
		drawProgress(progress, false);
		mTimerAnimator = ValueAnimator.ofFloat(progress, 1f);
		mTimerAnimator.setDuration(TimeUnit.SECONDS.toMillis(secs));
		mTimerAnimator.setInterpolator(new LinearInterpolator());
		mTimerAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				float value = (float) animation.getAnimatedValue();
				drawProgress(value, false);
				if (value == 1f) {
					viewListener.onFinishPart(position);
				}
			}
		});
		mTimerAnimator.start();
	}
}
