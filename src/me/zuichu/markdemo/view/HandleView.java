package me.zuichu.markdemo.view;

import me.zuichu.lib.SmartCore;
import me.zuichu.lib.mark.HandleViewParent;
import me.zuichu.markdemo.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

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
public class HandleView extends HandleViewParent {
	private MarkText textPage;
	private int tag;// left:0;right:1
	private View menuView;
	private float oldX, oldY, curX, curY = 0;

	public HandleView(Context context, int tag) {
		super(context);
		this.tag = tag;
		initialize();
	}

	public HandleView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize();
	}

	private void initialize() {
		if (tag == 0) {
			setHandlerImg(R.drawable.icon_left);
		} else {
			setHandlerImg(R.drawable.icon_left);
		}
		setBgColor(getResources().getColor(R.color.transparent));
	}

	public void setTextPage(MarkText tp) {
		this.textPage = tp;
	}

	public void setMenuView(View menuView) {
		this.menuView = menuView;
	}

	public HandleView(Context context, AttributeSet attrs, int defstyle) {
		super(context, attrs, defstyle);
		initialize();
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			textPage.curTag = tag;
			textPage.isLongClick = false;
			textPage.msv.setScrollingEnabled(false);
			oldX = event.getRawX();
			oldY = event.getRawY();
			break;
		case MotionEvent.ACTION_MOVE:
			curX = event.getRawX();
			curY = event.getRawY();
			float disX = curX - oldX;
			float disY = curY - oldY;
			this.setX((getX() + disX) - 20);
			this.setY((getY() + disY) - 20);
			SmartCore.getMotionEvent(textPage, action, getX() + disX, getY()
					+ disY);
			oldX = curX - 20;
			oldY = curY - 20;
			textPage.curTag = tag;
			if (tag == 0) {
				if ((getX() + disX) >= (textPage.width - menuView.getWidth())) {
					break;
				}
				menuView.setX(getX() + disX);
				if (getY() < 50) {
					menuView.setY(100);
				} else {
					menuView.setY(getY() + disY - 100);
				}

			}
			break;
		case MotionEvent.ACTION_UP:
			textPage.curTag = tag;
			textPage.msv.setScrollingEnabled(true);
			break;
		}
		return true;
	}

}
