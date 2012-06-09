package com.leisuretime.ui;

import java.util.ArrayList;
import java.util.List;

import com.leisuretime.R;
import com.leisuretime.model.Video;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class RecommendActivity extends Activity {

	private Button btn_play;
	private Context mContext;
	private ListView lv_vidoe;
	private VideoListAdapter mAdapter;
	private List<Video> mList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.recommend_activity);

		btn_play = (Button) findViewById(R.id.btn_play);
		lv_vidoe = (ListView) findViewById(R.id.lv_video);
		
		mList = new ArrayList<Video>();
		mList.add(new Video());
		
		mAdapter = new VideoListAdapter(mContext, mList);
		lv_vidoe.setAdapter(mAdapter);
		
		
		btn_play.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(mContext, VideoPlayerActivity.class);
				Uri uri = Uri.parse("/sdcard/gjbz.mp4");
				i.setData(uri);
				startActivity(i);
			}
		});
	}

}
