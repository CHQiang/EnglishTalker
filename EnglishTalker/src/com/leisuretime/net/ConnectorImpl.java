package com.leisuretime.net;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.leisuretime.EnglishTalkerApp;

import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

public class ConnectorImpl implements Runnable {

	private String url = null;

	private Thread connectThread = null;

	private NetworkListener listener = null;

	private String params = null;

	private int connectTimeout = 15 * 1000;

	private int readTimeout = 15 * 1000;

	/**
	 * If need native thread to perform View invalidate, we should using handler
	 * */
	private Handler mHandler = null;

	/**
	 * If need to identify callback in parent thread, should set mCommand .
	 * */
	private int mCommand = -1;

	/**
	 * reconnection to remote server when necessary
	 * */
	private boolean bRetry = false;

	private int method = GET;

	public static String sessionId;
	public static final int POST = 1;
	public static final int GET = 0;

	private static final String TAG = "ConnectorImpl";

	public ConnectorImpl(String url, String aParams, NetworkListener aListener,
			int method) {
		this.url = url;
		params = aParams;
		listener = aListener;
		this.method = method;
	}

	public void setNativeHandlerCallBack(Handler handler, int cmd) {
		mHandler = handler;
		mCommand = cmd;
	}

	public void setNetworkListener(NetworkListener aListener) {
		listener = aListener;
	}

	public void connect() {
		if (connectThread == null) {
			connectThread = new Thread(this);
			connectThread.start();
		}
	}

	public void run() {
		if (TextUtils.isEmpty(url)) {
			Log.e(TAG, "url is null or length is 0");
			return;
		}

		if (listener == null) {
			Log.e(TAG, "listener == null");
			return;
		}

		try {
			int apn = APNMgr.getApnType();
			if (apn == APNMgr.NOT_EXIST) {
				listener.action(NetworkListener.APN_NOTEXIST, null);
			} else {

				connectTimeout = 30 * 1000;
				readTimeout = 50 * 1000;
				if (method == GET) {
					// doget2();
					doGet();
				} else if (method == POST) {
					doPost();
				} else {
					Log.e(TAG, "No such method " + method + " excute!");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
	}

	private void doPost() throws IOException {
		StringBuilder builder = new StringBuilder(url);
		String desturl = builder.toString();
		InputStream stream = null;
		HttpURLConnection conn = null;
		try {
			Log.d(TAG, "POST: " + params);
			byte[] data = null;
			if (params == null) {
				data = new byte[0];
			} else {
				data = params.getBytes();
			}

			URL url = new URL(desturl);
			Log.v(TAG, "URL:" + desturl);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");

			conn.setConnectTimeout(connectTimeout);
			conn.setReadTimeout(readTimeout);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setInstanceFollowRedirects(false);
			conn.setRequestProperty("Accept", "text/plain");
			conn.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			conn.setRequestProperty("Connection", "Close");
			conn.setRequestProperty("Content-Length",
					String.valueOf(data.length));
			if (sessionId != null) {
				conn.setRequestProperty("Cookie", sessionId);
			}
			conn.connect();

			OutputStream outputStream = conn.getOutputStream();
			outputStream.write(data);
			outputStream.flush();
			outputStream.close();

			// Get Session ID
			if (desturl.equals(EnglishTalkerApp.url_login)) {
				String key = "";
				if (conn != null) {
					for (int i = 1; (key = conn.getHeaderFieldKey(i)) != null; i++) {
						if (key.equalsIgnoreCase("set-cookie")) {
							sessionId = conn.getHeaderField(key);
							sessionId = sessionId.substring(0,
									sessionId.indexOf(";"));
							Log.v(TAG, sessionId);
						}
					}
				}
			}

			int resp_code = conn.getResponseCode();

			if (resp_code == HttpURLConnection.HTTP_OK) {
				stream = conn.getInputStream();
				String response = null;
				if (stream != null) {
					Log.d(TAG, "GETRESPONSE:" + resp_code);
					response = readStream(stream);
				}
				if (mCommand != -1) {
					listener.action(NetworkListener.RECEIVE_STREAM,
							new Object[] { new Integer(mCommand), response });
				} else {
					listener.action(NetworkListener.RECEIVE_STREAM, response);
				}
			}  else {
				listener.action(NetworkListener.RESPONSE_CODE_ERROR,
						new Integer(resp_code));
			}

		} catch (IOException e) {
			e.printStackTrace();
			listener.action(NetworkListener.REQUEST_TIMEOUT, null);
		} finally {
			if (stream != null)
				stream.close();
			if (conn != null)
				conn.disconnect();
		}
	}

	private void doGet() throws Exception {
		StringBuffer buffer = new StringBuffer(url);
		if (params != null && params.length() > 0) {
			if (!url.endsWith("?")) {
				buffer.append("?");
			}
			buffer.append(params);
		}
		String desturl = buffer.toString().trim();
		Log.d(TAG, "GET URL: " + desturl);
		InputStream stream = null;
		HttpURLConnection conn = null;
		try {
			URL url = new URL(desturl);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setDoInput(true);
			conn.setConnectTimeout(connectTimeout);
			if (sessionId != null) {
				conn.setRequestProperty("Cookie", sessionId);
			}
			conn.connect();

			int resp_code = conn.getResponseCode();

			if (resp_code == HttpURLConnection.HTTP_OK) {
				stream = conn.getInputStream();
				String response = null;
				if (stream != null) {
					Log.d(TAG, "GETRESPONSE:" + resp_code);
					try {
						response = readStream(stream);
					} catch (IOException e) {
						// TODO: handle exception
						listener.action(NetworkListener.STREAM_EXCEPTION,
								stream);
					}

				}


				if (mCommand != -1) {
					listener.action(NetworkListener.RECEIVE_STREAM,
							new Object[] { new Integer(mCommand), response });
				} else {
					listener.action(NetworkListener.RECEIVE_STREAM, response);
				}

			} else {
				listener.action(NetworkListener.RESPONSE_CODE_ERROR,
						new Integer(resp_code));
			}

		} catch (IOException e) {
			e.printStackTrace();
			listener.action(NetworkListener.REQUEST_TIMEOUT, null);
		} finally {
			if (stream != null)
				stream.close();

			if (conn != null)
				conn.disconnect();
		}
	}

	/**
	 * 读取数据
	 * 
	 * @param inStream
	 *            输入流
	 * @return
	 * @throws IOException
	 */
	public static String readStream(InputStream inStream) throws IOException {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = -1;
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		outStream.close();
		inStream.close();
		String str = new String(outStream.toByteArray(), "UTF-8");

		Log.d(TAG, "result:\n" + str);

		return str;
	}

}
