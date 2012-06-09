package com.leisuretime.ui;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.leisuretime.R;
import com.leisuretime.model.LRCObject;

public class LrcListItemView extends RelativeLayout {

	private Context mContext;
	private TextView tv_lrc_en;
	private TextView tv_lrc_cn;
	private Button btn_lrc_play;

	public LrcListItemView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		mContext = context;
	}

	public LrcListItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		mContext = context;
	}

	public LrcListItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		mContext = context;
	}

	@Override
	protected void onFinishInflate() {
		// TODO Auto-generated method stub
		super.onFinishInflate();

		tv_lrc_cn = (TextView) findViewById(R.id.tv_lrc_cn);
		tv_lrc_en = (TextView) findViewById(R.id.tv_lrc_en);
		btn_lrc_play = (Button) findViewById(R.id.btn_lrc_play);

	}

	public void setLrc(LRCObject lrc) {
		tv_lrc_en.setText(lrc.lrc);

	}

	public void setTextColor(int red) {
		// TODO Auto-generated method stub
		tv_lrc_cn.setTextColor(red);
		tv_lrc_en.setTextColor(red);
	}
	
	
	

}
