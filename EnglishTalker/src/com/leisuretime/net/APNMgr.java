package com.leisuretime.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 
 * @author chqiang
 *
 */
public class APNMgr {
	
	private static Context m_context = null;
	
	public static void setAPNMgrContext(Context context){
		m_context = context;
	}
	public static int getApnType(){
		final android.net.Uri PREFERAPN_URI = android.net.Uri.parse("content://telephony/carriers/preferapn");
		ConnectivityManager connMgr = null;
		if(m_context != null) {
			connMgr = (ConnectivityManager) m_context.getSystemService(Context.CONNECTIVITY_SERVICE);
		}
		if(connMgr == null)
			return NOT_EXIST;
		if(connMgr.getActiveNetworkInfo() == null)
			return NOT_EXIST;
		if(connMgr.getActiveNetworkInfo().isConnected()){
			NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();
			int type = activeInfo.getType();
			if(type == ConnectivityManager.TYPE_WIFI){
				//wifi
				return WIFI_APN;
			} else if(type == ConnectivityManager.TYPE_MOBILE){
				String extraInfo = activeInfo.getExtraInfo();
				
				if(extraInfo != null && (extraInfo.equalsIgnoreCase("cmwap") || extraInfo.equalsIgnoreCase("cmwap:gsm"))){
					return CMWAP_APN;
				}
				return CMNET_APN;
			}
		} else {
			return NOT_EXIST;
		}
		
		return DEFAULT_APN;
	}
	
	public static final int DEFAULT_APN = 0x0;
	
	public static final int CMWAP_APN = DEFAULT_APN + 1;
	
	public static final int CMNET_APN = DEFAULT_APN + 2;
	
	public static final int WIFI_APN = DEFAULT_APN + 3;
	
	public static final int NOT_EXIST = -1;
	
	public static final String CMCC_PROXY = "10.0.0.172";
}
