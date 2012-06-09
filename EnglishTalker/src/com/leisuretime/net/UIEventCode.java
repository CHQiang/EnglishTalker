package com.leisuretime.net;

/**
 * handler msg.what 收到消息后处理UI事件
 * 
 * @author chqiang
 * 
 */
public class UIEventCode {

	/**
	 * 登录成功
	 */
	public static final int LOGIN_SUCESS = 0;
	/**
	 * 登录失败
	 */
	public static final int LOGIN_FAILED = 1;
	/**
	 * session 超时
	 */
	public static final int SESSION_OUT = 2;

	/**
	 * 支撑侧服务器重启
	 */
	public static final int SERVER_RESTART = 3;

	/**
	 * 请求超时
	 */
	public static final int REQ_TIMEOUT = 4;

	/**
	 * 返回数据异常
	 */
	public static final int STREAM_EXP = 5;
	/**
	 * 网络未打开
	 */
	public static final int NETWORK_INVALIBLE = 6;
	/**
	 * 数据接收成功
	 */
	public static final int RECEIVE_DATA_SUCESS = 7;
	/**
	 * 服务端错误，http返回错误码
	 */
	public static final int RECEIVE_ERROR_CODE = 8;
	/**
	 * 版本更新
	 */
	public static final int VERSION_UPDATE = 9;
}
