package me.zuichu.markdemo.view;

import java.util.ArrayList;
import java.util.List;

import me.zuichu.lib.mark.MarkScrollView;
import me.zuichu.lib.mark.MarkTextParent;
import me.zuichu.markdemo.R;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
public class MarkText extends MarkTextParent implements OnClickListener {
	private int off;
	private Context c;
	private MarkText markText;
	private WindowManager window;
	public int width, height;
	private String text;
	private TextView tv_copy;
	public RelativeLayout rl_root;
	private RelativeLayout relative;
	private List<HandleView> handlerViews;
	private ArrayList<View> tipViews;
	private List<View> menuViews;
	private View menuView;
	public int curTag = -1;
	private boolean isSelect = false;
	private boolean isEdit = false;
	private boolean hasSelect = false;
	public boolean isLongClick = false;
	public MaskView tv_float;
	public MarkScrollView msv;
	private int curOff;
	private int memOff = -1;
	private HandleView iv_left, iv_right;
	private String content = "";

	public String getWord() {
		return text;
	}

	public MarkText(Context context, RelativeLayout rel) {
		super(context);
		this.c = context;
		this.relative = rel;
		initialize();
	}

	public void setMarkText(String content) {
		this.content = content;
	}

	public void setRootView(RelativeLayout rl) {
		this.rl_root = rl;
	}

	public void setCanEdit(boolean edit) {
		isEdit = edit;
	}

	public MarkText(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize();
	}

	public MarkText(Context context, AttributeSet attrs, int defstyle) {
		super(context, attrs, defstyle);
		initialize();
	}

	private void initialize() {
		markText = this;
		setBackgroundColor(Color.TRANSPARENT);
		handlerViews = new ArrayList<HandleView>();
		tipViews = new ArrayList<View>();
		menuViews = new ArrayList<View>();
		menuView = View.inflate(getContext(), R.layout.menu_mark, null);
		tv_copy = (TextView) menuView.findViewById(R.id.tv_copy);
		tv_copy.setOnClickListener(this);
		tv_float = new MaskView(getContext());
		android.view.ViewGroup.LayoutParams params = new LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.MATCH_PARENT);
		tv_float.setLayoutParams(params);
		tv_float.setBackgroundColor(getResources()
				.getColor(R.color.transparent));
		window = (WindowManager) getContext().getSystemService(
				Context.WINDOW_SERVICE);
		width = window.getDefaultDisplay().getWidth();
		height = window.getDefaultDisplay().getHeight();
	}

	@Override
	public boolean getDefaultEditable() {
		if (isEdit) {
			return true;
		} else {
			return false;
		}
	}

	public void setIsSelect(boolean is) {
		this.isSelect = is;
	}

	@Override
	public boolean performLongClick() {
		setIsSelect(true);
		rl_root.removeView(tv_float);
		rl_root.addView(tv_float);
		tv_float.setHeight(getHeight());
		isLongClick = true;
		tv_float.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				clearHandler();
				setSelection(0);
				isLongClick = false;
				setIsSelect(false);
				rl_root.removeView(tv_float);
				rl_root.removeView(menuView);
			}
		});
		Vibrator vibrator = (Vibrator) getContext().getSystemService(
				Service.VIBRATOR_SERVICE);
		vibrator.vibrate(100);
		return super.performLongClick();
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (!isSelect) {
			return super.onTouchEvent(event);
		}
		int action = event.getAction();
		int line = 0;
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			line = getVerticalLine(getScrollY() + (int) event.getY());
			off = getHorizontalOff(line, (int) event.getX());
			ToSelect(getSpannabletText(), off);
			break;
		case MotionEvent.ACTION_MOVE:
			line = getVerticalLine(getScrollY() + (int) event.getY());
			curOff = getHorizontalOff(line, (int) event.getX());
			if (curOff > off) {
				if (curTag == 0) {
					ToSelect(getSpannabletText(), memOff, curOff);
					off = curOff;
				} else {
					memOff = getHorizontalOff(line, (int) event.getX());
					ToSelect(getSpannabletText(), off, curOff);
				}
			} else {
				if (curTag == 0) {
					ToSelect(getSpannabletText(), curOff, memOff);
					off = curOff;
				} else {
					memOff = getHorizontalOff(line, (int) event.getX());
					ToSelect(getSpannabletText(), curOff, off);
				}
			}
			break;
		case MotionEvent.ACTION_UP:
			line = getVerticalLine(getScrollY() + (int) event.getY());
			curOff = getHorizontalOff(line, (int) event.getX());
			memOff = getHorizontalOff(line, (int) event.getX());
			if (isLongClick) {
				createHandler(0, event.getX(), event.getY());
				createHandler(1, event.getX() + 10, event.getY());
				off = getHorizontalOff(line, (int) event.getX() + 10);
				ToSelect(getSpannabletText(), curOff, off);
			} else {
				if (off <= curOff) {
					ToSelect(getSpannabletText(), curOff, off);
				} else {
					ToSelect(getSpannabletText(), off, curOff);
				}
			}
			break;
		}
		return true;
	}

	public void setScrollView(MarkScrollView msv) {
		this.msv = msv;
	}

	private void clearHandler() {
		if (handlerViews.size() > 0) {
			for (HandleView v : handlerViews) {
				rl_root.removeView(v);
			}
			for (View v : menuViews) {
				rl_root.removeView(v);
			}
			curTag = -1;
		}
	}

	private void createHandler(int tag, float x, float y) {
		HandleView iv = new HandleView(getContext(), tag);
		iv.setTextPage(markText);
		rl_root.addView(iv);
		handlerViews.add(iv);
		iv.setX(x - 20);
		iv.setY(y - 20);
		if (tag == 0) {
			iv_left = iv;
			iv.performClick();
			if (tipViews.size() > 0) {
				rl_root.removeView(menuView);
			}
			rl_root.addView(menuView);
			iv.setMenuView(menuView);
			menuView.setX(x);
			if (y < 100) {
				y = 200;
			}
			menuView.setY(y - 100);
			tipViews.clear();
			tipViews.add(menuView);
		} else if (tag == 1) {
			iv_right = iv;
		}
	}

	public RelativeLayout getRoot() {
		return rl_root;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.tv_copy:
			if (getEnd() > getStart()) {
				Toast.makeText(getContext(),
						getContent().subSequence(getStart(), getEnd()),
						Toast.LENGTH_SHORT).show();
				ClipboardManager cmb = (ClipboardManager) getContext()
						.getSystemService(Context.CLIPBOARD_SERVICE);
				cmb.setText(getContent().subSequence(getStart(), getEnd()));
			} else {
				Toast.makeText(getContext(),
						getContent().subSequence(getEnd(), getStart()),
						Toast.LENGTH_SHORT).show();
				ClipboardManager cmb = (ClipboardManager) getContext()
						.getSystemService(Context.CLIPBOARD_SERVICE);
				cmb.setText(getContent().subSequence(getEnd(), getStart()));
			}
			break;
		default:
			break;
		}
	}
}
