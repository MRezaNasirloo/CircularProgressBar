package com.fourmob.colorpicker;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;

public class ColorStateDrawable extends LayerDrawable {
	private final int mColor;

	public ColorStateDrawable(Drawable[] drawables, int color) {
		super(drawables);
		this.mColor = color;
	}

	private int getPressedColor(int color) {
		float[] hsv = new float[3];
		Color.colorToHSV(color, hsv);
		hsv[2] = (0.7F * hsv[2]);
		return Color.HSVToColor(hsv);
	}

	public boolean isStateful() {
		return true;
	}

	protected boolean onStateChange(int[] states) {
		boolean pressed = false;
		for (int state : states) {
			if (state == android.R.attr.state_pressed || state == android.R.attr.state_focused) {
				pressed = true;
				break;
			}
		}
		if (pressed) {
			super.setColorFilter(getPressedColor(this.mColor), PorterDuff.Mode.SRC_ATOP);
		} else {
			super.setColorFilter(this.mColor, PorterDuff.Mode.SRC_ATOP);
		}
		return super.onStateChange(states);
	}
}