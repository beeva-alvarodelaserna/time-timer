package com.beeva.labs.timetimer.support.ui;

import android.graphics.Color;
import java.util.Random;

public class SessionStep {

	public String name;
	public int duration;
	public boolean isRunning;
	public float timerAngle;
	public int circleColor;

	public SessionStep() {
		Random rnd = new Random();
		int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
		circleColor = color;
	}
}
