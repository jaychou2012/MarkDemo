package me.zuichu.markdemo.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

/**
 * 谭东jay
 * 
 * zuichutech
 * 
 * zuichu.me
 * 
 * QQ852041173技术支持
 * 
 * @author Tandong
 * 
 */
public class MaskView extends TextView {

	public MaskView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public MaskView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public MaskView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return super.onTouchEvent(event);
	}

	@Override
	public boolean performClick() {
		// TODO Auto-generated method stub
		return super.performClick();
	}
}
