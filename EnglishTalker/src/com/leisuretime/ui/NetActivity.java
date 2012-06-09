package com.leisuretime.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.leisuretime.R;
import com.leisuretime.net.ConnectorImpl;
import com.leisuretime.net.ErrorCode;
import com.leisuretime.net.GetDataAsyTask;
import com.leisuretime.net.NetworkListener;
import com.leisuretime.net.UIEventCode;

public class NetActivity extends Activity implements NetworkListener {
	private ProgressDialog m_Dialog;
	private Handler handler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			dissmissDialog();
			switch (msg.what) {
			case UIEventCode.VERSION_UPDATE:
				handleChackVersion();
				break;
			case UIEventCode.LOGIN_SUCESS:
				Toast.makeText(NetActivity.this, "登录成功", Toast.LENGTH_SHORT)
						.show();
				handleLoginSuccess();
				break;
			case UIEventCode.LOGIN_FAILED:
				Toast.makeText(NetActivity.this, "用户名密码错误", Toast.LENGTH_SHORT)
						.show();
				handleLoginFailed();
				break;
			case UIEventCode.RECEIVE_DATA_SUCESS:
				if (msg.arg1 != -1) {
					Toast.makeText(NetActivity.this,
							"数据接收成功,共接收数据" + msg.arg1 + "条", Toast.LENGTH_SHORT)
							.show();
				}
				reciveSeccessAndChangeUI();
				break;
			case UIEventCode.NETWORK_INVALIBLE:
				Toast.makeText(NetActivity.this, "未连接网络", Toast.LENGTH_SHORT)
						.show();
				break;
			case UIEventCode.REQ_TIMEOUT:
				Toast.makeText(NetActivity.this, "请求超时", Toast.LENGTH_SHORT)
						.show();
				break;
			case UIEventCode.SERVER_RESTART:
				Toast.makeText(NetActivity.this, "服务端重启", Toast.LENGTH_SHORT)
						.show();
				break;
			case UIEventCode.SESSION_OUT:
				Toast.makeText(NetActivity.this, "Session 过期",
						Toast.LENGTH_SHORT).show();
				NetActivity.this.finish();
				Intent i = new Intent(NetActivity.this, EnglishTalkerActivity.class);
				startActivity(i);
				break;
			case UIEventCode.STREAM_EXP:
				Toast.makeText(NetActivity.this, "返回数据异常", Toast.LENGTH_SHORT)
						.show();
				break;
			case UIEventCode.RECEIVE_ERROR_CODE:
				Toast.makeText(NetActivity.this, "请求失败，错误码为：" + msg.arg1,
						Toast.LENGTH_SHORT).show();
				break;
			}

		};
	};

	/**
	 * 数据接收成功，更新UI
	 */
	public void reciveSeccessAndChangeUI() {

	}

	public void handleLoginFailed() {
		// TODO Auto-generated method stub

	}

	public void handleChackVersion() {

	}

	public void handleLoginSuccess() {

	}

	@Override
	public void action(int actionCode, Object object) {
		// TODO Auto-generated method stub

		switch (actionCode) {
		case NetworkListener.STREAM_EXCEPTION:
			sendMsg(UIEventCode.STREAM_EXP, 0);
			break;
		case NetworkListener.REQUEST_TIMEOUT:
			sendMsg(UIEventCode.REQ_TIMEOUT, 0);
			break;
		case NetworkListener.RESPONSE_CODE_ERROR:
			int code = Integer.parseInt(object.toString());
			if (code == ErrorCode.ERROR_CODE_600) {
				sendMsg(UIEventCode.LOGIN_FAILED, code);
			} else if (code == ErrorCode.ERROR_CODE_601) {
				sendMsg(UIEventCode.SESSION_OUT, code);
			} else {
				sendMsg(UIEventCode.RECEIVE_ERROR_CODE, code);
			}
			break;

		case NetworkListener.APN_NOTEXIST:
			sendMsg(UIEventCode.NETWORK_INVALIBLE, 0);
			break;
		}

	}

	public void sendMsg(int what, int arg1) {
		Message msg = new Message();
		msg.what = what;
		msg.arg1 = arg1;
		handler.sendMessage(msg);
	}

	public void showProcessDialog(int titleid, int msgid) {
		if (m_Dialog == null) {
			m_Dialog = ProgressDialog.show(this,
					getResources().getString(titleid), getResources()
							.getString(msgid), true);
		} else if (m_Dialog.isShowing() == false) {
			m_Dialog.setMessage(getResources().getString(msgid));
			m_Dialog.show();
		}
	}

	private void dissmissDialog() {
		if (m_Dialog != null && m_Dialog.isShowing()) {
			m_Dialog.dismiss();
		}
	}

	/**
	 * 发送http请求
	 * 
	 * @param url
	 * @param aParams
	 * @param method
	 */
	public void connectNetwork(String url, String aParams, int method) {
		showProcessDialog(R.string.wait_dialog_title, R.string.getting_data);
		ConnectorImpl conn = new ConnectorImpl(url, aParams, this, method);
		GetDataAsyTask task = new GetDataAsyTask(conn);
		task.execute();
		// ThreadPoolWrap.getThreadPool().executeTask(conn);
	}

}
