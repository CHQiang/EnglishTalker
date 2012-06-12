package com.leisuretime.ui;

import java.util.ArrayList;
import java.util.List;

import com.leisuretime.R;
import com.leisuretime.model.Video;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

public class PersonalActivity extends Activity implements OnClickListener {
	private Context mContext;
	private ListView lv_vidoe;
	private VideoListAdapter mAdapter;
	private List<Video> mList;

	private Button btn_downloads;
	private Button btn_bookmarks;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.personal_activity);
		mContext = this;
		lv_vidoe = (ListView) findViewById(R.id.lv_video);
		btn_bookmarks = (Button) findViewById(R.id.btn_bookmarks);
		btn_downloads = (Button) findViewById(R.id.btn_downloads);

		mList = new ArrayList<Video>();
		mList.add(new Video());

		mAdapter = new VideoListAdapter(mContext, mList);
		lv_vidoe.setAdapter(mAdapter);

		btn_bookmarks.setOnClickListener(this);
		btn_downloads.setOnClickListener(this);
	}

	private void changeDownloadsorBookmarks(boolean isDownloads) {
		if (isDownloads) {
			btn_downloads
					.setBackgroundResource(R.drawable.channel_toggle_redleft);
			btn_downloads.setTextColor(mContext.getResources().getColor(
					R.color.toggle_text_white));
			btn_bookmarks
					.setBackgroundResource(R.drawable.channel_toggle_pressedright);
			btn_bookmarks.setTextColor(mContext.getResources().getColor(
					R.color.toggle_text_black));
		} else {

			btn_downloads
					.setBackgroundResource(R.drawable.channel_toggle_pressedleft);
			btn_downloads.setTextColor(mContext.getResources().getColor(
					R.color.toggle_text_black));
			btn_bookmarks
					.setBackgroundResource(R.drawable.channel_toggle_redright);
			btn_bookmarks.setTextColor(mContext.getResources().getColor(
					R.color.toggle_text_white));
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();

		switch (id) {
		case R.id.btn_bookmarks:
			changeDownloadsorBookmarks(false);
			break;
		case R.id.btn_downloads:
			changeDownloadsorBookmarks(true);
			break;
		}

	}
}
