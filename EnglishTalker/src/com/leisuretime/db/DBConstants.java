package com.leisuretime.db;

import android.provider.BaseColumns;

public class DBConstants {

	public static final String DBNAME = "et.sqlite";
	public static final String TAB_Video = "video";

	public static final class Tab_video implements BaseColumns {
		public static final String VIDEO_NAME = "video_name";
		public static final String SER_ID = "ser_id";
		public static final String LOCAL_URL = "local_url";

		public static final int INDEX_ID = 0;
		public static final int INDEX_VIDEO_NAME = 1;
		public static final int INDEX_SER_ID = 2;
		public static final int INDEX_LOCAL_URL = 3;

		public static final String[] COLUMNS = { _ID, VIDEO_NAME, SER_ID,
				LOCAL_URL };
	}

	public static final class Tab_practice implements BaseColumns {
		public static final String VIDEO_ID = "video_id";
		public static final String LOCAL_AUDIO_URL = "local_url";

		public static final int INDEX_ID = 0;
		public static final int INDEX_VIDEO_NAME = 1;
		public static final int INDEX_LOCAL_AUDIO_URL = 2;

		public static final String[] COLUMNS = { _ID, VIDEO_ID, LOCAL_AUDIO_URL };
	}
}
