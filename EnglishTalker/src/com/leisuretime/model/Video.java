package com.leisuretime.model;

import android.graphics.Bitmap;

public class Video {

	public String videoName;//视频名称
	public int localID;//下载后本地数据库id
	public int serID;//服务端数据库id
	public String downloadURL;//下载前的下载路径
	public String localURL;//下载后的本地路径
	public int downloadTimes;//下载次数，不存入本地数据库
	public String videoCharacters;//包含角色
	public Bitmap pic;//不保存本地，可缓存
}
