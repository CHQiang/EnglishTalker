package com.leisuretime.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

import com.leisuretime.model.LRCObject;

public class FileUtil {

	private static final String TAG = "FileUtil";

	/**
	 * 读取字幕文件
	 */
	public static List<LRCObject> read(String file) {
		List<LRCObject> lrc_read = new ArrayList<LRCObject>();
		String data = "";
		try {
			File saveFile = new File(file);
			FileInputStream stream = new FileInputStream(saveFile);// context.openFileInput(file);

			BufferedReader br = new BufferedReader(new InputStreamReader(
					stream, "GB2312"));
			int i = 0;
			LRCObject item1 = null;
			while ((data = br.readLine()) != null) {
				i++;
				switch (i % 4) {
				case 0:

					break;
				case 1:
					item1 = new LRCObject();
					item1.id = Integer.parseInt(data);
					break;
				case 2:
					String splitdata[] = data.split("-->");
					String beginStr = splitdata[0];
					String endStr = splitdata[1];

					item1.begintime = praseTimetoInt(beginStr);
					item1.endtime = praseTimetoInt(endStr);
					item1.timeline = item1.endtime - item1.begintime;
					break;
				case 3:
					item1.lrc = data;
					lrc_read.add(item1);
					break;
				default:
					break;
				}

			}
			stream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return lrc_read;
	}

	private static int praseTimetoInt(String tmpstr) {
		tmpstr = tmpstr.replace(":", ",");
		tmpstr = tmpstr.replace(",", "@");
		String timedata[] = tmpstr.split("@");

		int h = Integer.parseInt(timedata[0].trim());// 时
		int m = Integer.parseInt(timedata[1].trim()); // 分
		int s = Integer.parseInt(timedata[2].trim()); // 秒
		int ms = Integer.parseInt(timedata[3].trim()); // 毫秒
		// int currTime = (h * 60 * 60 + m * 60 + s) * 1000 + ms * 10;
		int currTime = (h * 60 * 60 + (m - 37) * 60 + (s - 33)) * 1000 + ms;// 字幕延时,网络下载的字幕

		return currTime;
	}

	public static void startRec(MediaRecorder mediaRecorder) {
		Log.v(TAG, "startRec");
		try {
			if (Environment.getExternalStorageState().equals(
					android.os.Environment.MEDIA_MOUNTED)) {
				/* 取得开始执行的时间 */
				long startRecTime = System.currentTimeMillis();
				/* 取得SD Card路径做为录音的文件位置 */
				File myRecAudioDir = Environment.getExternalStorageDirectory();
				String strTempFile = "iRecorder_";
				/* 建立录音档 */
				File myRecAudioFile = File.createTempFile(strTempFile, ".amr",
						myRecAudioDir);

				// mediaRecorder = new MediaRecorder();
				/* 设定录音来源为麦克风 */
				mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
				/* 设定输出格式 */
				mediaRecorder
						.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
				/* 设定音频格式为Encoder */
				mediaRecorder
						.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
				/* 设定音频保存路径 */
				mediaRecorder.setOutputFile(myRecAudioFile.getAbsolutePath());
				/* 准备开始录音 */
				mediaRecorder.prepare();

				mediaRecorder.start();
				// isStartRec = true;
			} else {
				// SleepSec = 1;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void stopRec(MediaRecorder mediaRecorder) {
		if(mediaRecorder!=null){
			mediaRecorder.stop();
		}
	}
	
	public static void resetRec(MediaRecorder mediaRecorder) {
		if(mediaRecorder!=null){
			mediaRecorder.reset();
		}
	}
	
	public static void releaseRec(MediaRecorder mediaRecorder) {
		if(mediaRecorder!=null){
			mediaRecorder.release();
		}
	}
	
	
}
