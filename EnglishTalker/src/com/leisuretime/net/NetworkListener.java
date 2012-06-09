package com.leisuretime.net;

public interface NetworkListener {

	public void action(int actionCode, Object object);

	static final int BASE = 0x00;
	/**
	 * 服务端错误，网络连接的状态码事件-getResponseCode()
	 */
	public static final int RESPONSE_CODE_ERROR = BASE + 1;
	/**
	 * 联网返回数据处理
	 */
	public static final int RECEIVE_STREAM = BASE + 2;
	/**
	 * 手机是否已经联网
	 */
	public static final int APN_NOTEXIST = BASE + 3;
	/**
	 * 网络连接超时
	 */
	public static final int REQUEST_TIMEOUT = BASE + 4;

	/**
	 * 网络连接流异常
	 * */
	public static final int STREAM_EXCEPTION = BASE + 5;

	/**
	 * session 超时
	 */
//	static final int SESSION_TIMEOUT = BASE + 6;
}
