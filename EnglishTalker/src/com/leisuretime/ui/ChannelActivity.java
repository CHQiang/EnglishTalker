package com.leisuretime.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.leisuretime.R;
import com.leisuretime.model.Video;

public class ChannelActivity extends Activity implements OnClickListener {
	
	private Context mContext;
	private ListView lv_vidoe;
	private VideoListAdapter mAdapter;
	private List<Video> mList;

	private Button btn_movie;
	private Button btn_drama;
	private Button btn_speech;
	private Button btn_new;
	private Button btn_popular;
	
	private ImageView img_movie;
	private ImageView img_drama;
	private ImageView img_speech;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.channel_activity);
		mContext = this;

		lv_vidoe = (ListView) findViewById(R.id.lv_video);
		btn_movie = (Button) findViewById(R.id.btn_movie);
		btn_drama = (Button) findViewById(R.id.btn_drama);
		btn_speech = (Button) findViewById(R.id.btn_speech);
		btn_new = (Button) findViewById(R.id.btn_new);
		btn_popular = (Button) findViewById(R.id.btn_popular);
		img_movie = (ImageView) findViewById(R.id.img_active_movie);
		img_drama = (ImageView) findViewById(R.id.img_active_drama);
		img_speech = (ImageView) findViewById(R.id.img_active_speech);
		
		mList = new ArrayList<Video>();
		mList.add(new Video());

		mAdapter = new VideoListAdapter(mContext, mList);
		lv_vidoe.setAdapter(mAdapter);
		
		
		btn_movie.setOnClickListener(this);
		btn_drama.setOnClickListener(this);
		btn_speech.setOnClickListener(this);
		btn_new.setOnClickListener(this);
		btn_popular.setOnClickListener(this);
		
		setInactive();
		img_movie.setVisibility(View.VISIBLE);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		
		switch(id){
		case R.id.btn_movie:
			setInactive();
			img_movie.setVisibility(View.VISIBLE);
			break;
		case R.id.btn_drama:
			setInactive();
			img_drama.setVisibility(View.VISIBLE);
			break;
		case R.id.btn_speech:
			setInactive();
			img_speech.setVisibility(View.VISIBLE);
			break;
		case R.id.btn_new:
			changeNeworPopular(true);
			break;
		case R.id.btn_popular:
			changeNeworPopular(false);
			break;
		}
		
	}
	
	
	private void setInactive(){
		img_movie.setVisibility(View.GONE);
		img_drama.setVisibility(View.GONE);
		img_speech.setVisibility(View.GONE);
	}
	
	private void changeNeworPopular(boolean isNew){
		if(isNew){
			btn_new.setBackgroundResource(R.drawable.channel_toggle_redleft);
			btn_new.setTextColor(mContext.getResources().getColor(R.color.toggle_text_white));
			btn_popular.setBackgroundResource(R.drawable.channel_toggle_pressedright);
			btn_popular.setTextColor(mContext.getResources().getColor(R.color.toggle_text_black));
		}else{

			btn_new.setBackgroundResource(R.drawable.channel_toggle_pressedleft);
			btn_new.setTextColor(mContext.getResources().getColor(R.color.toggle_text_black));
			btn_popular.setBackgroundResource(R.drawable.channel_toggle_redright);
			btn_popular.setTextColor(mContext.getResources().getColor(R.color.toggle_text_white));
		}
		
	}

}
