package com.leisuretime.ui;

import java.util.List;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.leisuretime.R;
import com.leisuretime.model.Video;

public class VideoListAdapter extends BaseAdapter {

	private Context mContext;
	private List<Video> mList;
	private LayoutInflater inflater;

	public VideoListAdapter(Context context, List<Video> list) {
		this.mContext = context;
		this.mList = list;
		inflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return mList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.video_item_view, null);
		}
		VideoItemView view = (VideoItemView) convertView;
		Video v = mList.get(position);
		view.bindData(v);

		return view;
	}

}
