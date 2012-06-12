package com.leisuretime.ui;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabWidget;
import android.widget.TextView;

import com.leisuretime.R;

public class EnglishTalkerActivity extends TabActivity implements
		OnTabChangeListener {

	private Context mContext;

	private GestureDetector gestureDetector;
	private FrameLayout frameLayout;

	private AnimationTabHost mTabHost;
	private TabWidget mTabWidget;

	/** 记录当前分页ID */
	private int currentTabID = 0;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		mContext = this;

		initView();

		gestureDetector = new GestureDetector(new TabHostTouch());
		new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (gestureDetector.onTouchEvent(event)) {
					return true;
				}
				return false;
			}
		};

		frameLayout = mTabHost.getTabContentView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		mTabHost = (AnimationTabHost) findViewById(android.R.id.tabhost);
		mTabWidget = (TabWidget) findViewById(android.R.id.tabs);
		mTabHost.setOnTabChangedListener(this);

		setIndicator(R.drawable.tab_recommend, 0, new Intent(this,
				RecommendActivity.class), R.string.recommend);
		setIndicator(R.drawable.tab_channel, 1, new Intent(this,
				ChannelActivity.class), R.string.channel);
//		setIndicator(R.drawable.tab_search, 2, new Intent(this,
//				SearchActivity.class), R.string.search);
		setIndicator(R.drawable.tab_personal, 3, new Intent(this,
				PersonalActivity.class), R.string.peraonal);

		mTabHost.setOpenAnimation(true);


	}

	@Override
	public void onTabChanged(String tabId) {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {

		if (gestureDetector.onTouchEvent(event)) {
			event.setAction(MotionEvent.ACTION_CANCEL);
		}

		return super.dispatchTouchEvent(event);

	}

	private void setIndicator(int icon, int tabId, Intent intent, int name) {

		View localView = LayoutInflater.from(this.mTabHost.getContext())
				.inflate(R.layout.tab_widget_view, null);
		ImageView iv_icon = (ImageView) localView
				.findViewById(R.id.main_activity_tab_image);
		TextView tv_name = (TextView) localView
				.findViewById(R.id.main_activity_tab_text);

		iv_icon.setBackgroundResource(icon);
		tv_name.setText(name);

		String str = String.valueOf(tabId);

		TabHost.TabSpec localTabSpec = mTabHost.newTabSpec(str)
				.setIndicator(localView).setContent(intent);
		mTabHost.addTab(localTabSpec);
	}

	private class TabHostTouch extends SimpleOnGestureListener {
		/** 滑动翻页所需距离 */
		private static final int ON_TOUCH_DISTANCE = 80;

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			if (e1.getX() - e2.getX() <= (-ON_TOUCH_DISTANCE)) {
				currentTabID = mTabHost.getCurrentTab() - 1;

				if (currentTabID < 0) {
					currentTabID = mTabHost.getTabCount() - 1;
				}
			} else if (e1.getX() - e2.getX() >= ON_TOUCH_DISTANCE) {
				currentTabID = mTabHost.getCurrentTab() + 1;

				if (currentTabID >= mTabHost.getTabCount()) {
					currentTabID = 0;
				}
			}
			mTabHost.setCurrentTab(currentTabID);
			return false;
		}
	}
}