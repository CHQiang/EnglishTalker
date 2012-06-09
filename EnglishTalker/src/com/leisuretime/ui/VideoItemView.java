package com.leisuretime.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.leisuretime.R;
import com.leisuretime.model.Video;

public class VideoItemView extends RelativeLayout {

	private Context mContext;
	private TextView tv_video_name;
	private TextView tv_video_downloadTimes;
	private TextView tv_characters;
	private ImageView img_video_pic;
	private Button btn_download;
	private Button btn_play;

	public VideoItemView(Context context) {
		super(context);
		mContext = context;
	}

	public VideoItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
	}

	public VideoItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		tv_video_name = (TextView) findViewById(R.id.tv_video_name);
		tv_video_downloadTimes = (TextView) findViewById(R.id.tv_video_download_times);
		tv_characters = (TextView) findViewById(R.id.tv_video_characters);
		btn_download = (Button) findViewById(R.id.btn_video_download);
		btn_play = (Button) findViewById(R.id.btn_video_play);
	}

	public void bindData(Video v){
		
	}
	
}
