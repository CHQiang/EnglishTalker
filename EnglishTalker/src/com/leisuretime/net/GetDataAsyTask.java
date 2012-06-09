package com.leisuretime.net;

import android.os.AsyncTask;

public class GetDataAsyTask extends AsyncTask<Object, Integer, Object> {
	ConnectorImpl conn;

	public GetDataAsyTask(ConnectorImpl conn) {
		// TODO Auto-generated constructor stub
		this.conn = conn;
	}

	@Override
	protected Object doInBackground(Object... params) {
		// TODO Auto-generated method stub
		conn.run();
		return null;
	}

}
