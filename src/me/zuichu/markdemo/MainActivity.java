package me.zuichu.markdemo;

import me.zuichu.lib.mark.MarkScrollView;
import me.zuichu.markdemo.view.MarkText;
import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.RelativeLayout;

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

public class MainActivity extends Activity {
	private MarkText mt;
	private RelativeLayout rl_root;
	private MarkScrollView msv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		initView();
	}

	private void initView() {
		mt = (MarkText) this.findViewById(R.id.mt);
		rl_root = (RelativeLayout) this.findViewById(R.id.rl_root);
		msv = (MarkScrollView) this.findViewById(R.id.msv);
		mt.setRootView(rl_root);
		mt.setScrollView(msv);
		mt.setCanEdit(false);
	}
}
