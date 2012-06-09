package com.leisuretime.ui;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.leisuretime.R;
import com.leisuretime.model.LRCObject;

public class LrcListAdapter extends BaseAdapter {

	private Context mContext;
	private List<LRCObject> lrc_list;
	private LayoutInflater inflater;

	public LrcListAdapter(Context context, List<LRCObject> list) {
		mContext = context;
		lrc_list = list;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return lrc_list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return lrc_list.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.lrc_list_item_view, null);
		}
		LrcListItemView view = (LrcListItemView) convertView;
		view.setLrc(lrc_list.get(position));
		
		if (position == mItemIndex) {
			view.setTextColor(Color.RED);
		}else{
			view.setTextColor(Color.BLACK);
		}
//		lv_lrc.setSelection(position);
		return view;
	}

	private int mItemIndex = -1;

	public void setSelectItem(int index) {
		mItemIndex = index;
	}
	
	
}
