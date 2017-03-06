package com.beeva.labs.timetimer.support.ui;

import android.content.Context;
import android.util.AttributeSet;

public class NumberPicker extends android.widget.NumberPicker {

	public NumberPicker(Context context) {
		super(context);
	}

	public NumberPicker(Context context, AttributeSet attrs) {
		super(context, attrs);
		processAttributeSet(attrs);
	}

	public NumberPicker(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		processAttributeSet(attrs);
	}

	private void processAttributeSet(AttributeSet attrs) {
		this.setMinValue(attrs.getAttributeIntValue(null, "min", 0));
		this.setMaxValue(attrs.getAttributeIntValue(null, "max", 0));
	}
}
