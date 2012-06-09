package com.leisuretime.ui;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.leisuretime.R;
import com.leisuretime.model.LRCObject;
import com.leisuretime.ui.MyVideoView.MySizeChangeLinstener;
import com.leisuretime.ui.SoundView.OnVolumeChangedListener;
import com.leisuretime.util.FileUtil;

public class VideoPlayerActivity extends Activity implements
		View.OnClickListener, View.OnLongClickListener {

	private final static String TAG = "VideoPlayerActivity";

	private int playedTime;

	private MyVideoView vv = null;
	private SeekBar seekBar = null;
	private TextView durationTextView = null;
	private TextView playedTextView = null;
	private GestureDetector mGestureDetector = null;
	private AudioManager mAudioManager = null;

	private int maxVolume = 0;
	private int currentVolume = 0;

	private ImageButton btn_show_text = null;
	private ImageButton btn_backward = null;
	private ImageButton btn_play_pause = null;
	private ImageButton btn_forward = null;
	private ImageButton btn_sound_view = null;

	private View controlView = null;

	private SoundView mSoundView = null;
	private PopupWindow mSoundWindow = null;

	private Button btn_hold_speak;
	private ImageView img_recorder;
	private MediaRecorder mMediaRecorder;

	// 字幕
	private TextView tv_lrc;
	private List<LRCObject> lrc_list;
	private ListView lv_lrc;
	private LrcListAdapter myAdapter;
	private boolean lv_scroll;

	private static int screenWidth = 0;
	private static int screenHeight = 0;

	private final static int TIME = 6868;

	private final static int PROGRESS_CHANGED = 0;
	private final static int HIDE_CONTROLER = 1;
	private static final int PAUSE = 2;

	// private boolean isControllerShow = true;
	private boolean isPaused = true;
	// private boolean isFullScreen = false;
	private boolean isSilent = false;
	// private boolean isSoundShow = false;
	protected boolean isCompletion = false;

	GestureDetector gestureRec;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.v(TAG, "onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.video_player_activity);

		initView();

		lrc_list = FileUtil.read("/sdcard/gjbz.txt");
		myAdapter = new LrcListAdapter(this, lrc_list);
		lv_lrc.setAdapter(myAdapter);
		lv_lrc.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				if (scrollState == SCROLL_STATE_TOUCH_SCROLL) {
					lv_scroll = true;
				} else if (scrollState == SCROLL_STATE_IDLE) {
					lv_scroll = false;
				}

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {

			}
		});

		mSoundView.setOnVolumeChangeListener(new OnVolumeChangedListener() {

			@Override
			public void setYourVolume(int index) {

				cancelDelayHide();
				updateVolume(index);
				hideControllerDelay();
			}
		});

		mSoundWindow = new PopupWindow(mSoundView);

		Uri uri = getIntent().getData();
		if (uri != null) {
			if (vv.getVideoHeight() == 0) {
				vv.setVideoURI(uri);
			}
		} 
		vv.setMySizeChangeLinstener(new MySizeChangeLinstener() {

			@Override
			public void doMyThings() {
				// TODO Auto-generated method stub
				setVideoScale(SCREEN_DEFAULT);
			}

		});

		mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		currentVolume = mAudioManager
				.getStreamVolume(AudioManager.STREAM_MUSIC);

		btn_show_text.setAlpha(0xBB);
		btn_backward.setAlpha(0xBB);
		btn_play_pause.setAlpha(0xBB);
		btn_forward.setAlpha(0xBB);
		btn_sound_view.setAlpha(findAlphaFromSound());

		btn_show_text.setOnClickListener(this);
		btn_backward.setOnClickListener(this);
		btn_play_pause.setOnClickListener(this);
		btn_forward.setOnClickListener(this);
		btn_sound_view.setOnClickListener(this);

		btn_sound_view.setOnLongClickListener(this);
		btn_hold_speak.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				int action = event.getAction();
				switch (action) {
				case MotionEvent.ACTION_DOWN:
					img_recorder.setVisibility(View.VISIBLE);
					btn_hold_speak
							.setBackgroundResource(R.drawable.voice_rcd_btn_pressed);
					if (mMediaRecorder == null) {
						mMediaRecorder = new MediaRecorder();
					} else {
						FileUtil.resetRec(mMediaRecorder);
					}
					FileUtil.startRec(mMediaRecorder);
					break;
				case MotionEvent.ACTION_UP:
					img_recorder.setVisibility(View.GONE);
					btn_hold_speak
							.setBackgroundResource(R.drawable.voice_rcd_btn_nor);
					FileUtil.stopRec(mMediaRecorder);
					break;
				case MotionEvent.ACTION_MOVE:
					break;
				default:
					break;
				}
				return false;
			}
		});

		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onProgressChanged(SeekBar seekbar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				if (fromUser) {
					if (!isPaused) {
						vv.seekTo(progress);
					} else {

					}
				}
			}

			@Override
			public void onStartTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub
				myHandler.removeMessages(HIDE_CONTROLER);
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				myHandler.sendEmptyMessageDelayed(HIDE_CONTROLER, TIME);
			}
		});

		getScreenSize();

		mGestureDetector = new GestureDetector(new SimpleOnGestureListener() {

			@Override
			public boolean onDoubleTap(MotionEvent e) {
				// TODO Auto-generated method stub
				// if (isFullScreen) {
				// setVideoScale(SCREEN_DEFAULT);
				// } else {
				// setVideoScale(SCREEN_FULL);
				// }
				// isFullScreen = !isFullScreen;
				// Log.d(TAG, "onDoubleTap");
				//
				// if (isControllerShow) {
				// showController();
				// }
				return true;
			}

			@Override
			public boolean onSingleTapConfirmed(MotionEvent e) {
				// TODO Auto-generated method stub
				if (!controlView.isShown()) {
					showController();
					hideControllerDelay();
				} else {
					cancelDelayHide();
					hideController();
				}
				// return super.onSingleTapConfirmed(e);
				return true;
			}

			@Override
			public void onLongPress(MotionEvent e) {
				// TODO Auto-generated method stub
				if (isPaused) {
					vv.start();
					btn_play_pause.setImageResource(R.drawable.pause);
					cancelDelayHide();
					hideControllerDelay();
				} else {
					vv.pause();
					btn_play_pause.setImageResource(R.drawable.play);
					cancelDelayHide();
					showController();
				}
				isPaused = !isPaused;
				// super.onLongPress(e);
			}
		});

		vv.setOnPreparedListener(new OnPreparedListener() {

			@Override
			public void onPrepared(MediaPlayer arg0) {
				// TODO Auto-generated method stub

				setVideoScale(SCREEN_DEFAULT);
				// isFullScreen = false;
				if (!controlView.isShown()) {
					showController();
				}

				int i = vv.getDuration();
				Log.d("onCompletion", "" + i);
				seekBar.setMax(i);
				i /= 1000;
				int minute = i / 60;
				int hour = minute / 60;
				int second = i % 60;
				minute %= 60;
				durationTextView.setText(String.format("%02d:%02d:%02d", hour,
						minute, second));

//				vv.start();
//				btn_play_pause.setImageResource(R.drawable.pause);
				hideControllerDelay();
				myHandler.sendEmptyMessage(PROGRESS_CHANGED);
			}
		});

		vv.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer arg0) {
				isPaused = true;
				btn_play_pause.setImageResource(R.drawable.play);
				isCompletion = true;
			}
		});

		// myHandler.sendEmptyMessageDelayed(PAUSE, 1000);
	}

	private void initView() {
		// TODO Auto-generated method stub
		vv = (MyVideoView) findViewById(R.id.vv);
		controlView = findViewById(R.id.controler);
		durationTextView = (TextView) findViewById(R.id.duration);
		playedTextView = (TextView) findViewById(R.id.has_played);
		seekBar = (SeekBar) findViewById(R.id.seekbar);
		btn_show_text = (ImageButton) findViewById(R.id.btn_show_text);
		btn_backward = (ImageButton) findViewById(R.id.btn_backward);
		btn_play_pause = (ImageButton) findViewById(R.id.btn_play_pause);
		btn_forward = (ImageButton) findViewById(R.id.btn_forward);
		btn_sound_view = (ImageButton) findViewById(R.id.btn_sound_view);
		img_recorder = (ImageView) findViewById(R.id.img_recorder);
		btn_hold_speak = (Button) findViewById(R.id.btn_hold_speak);
		tv_lrc = (TextView) findViewById(R.id.tv_lrc);
		lv_lrc = (ListView) findViewById(R.id.lv_lrc);
		mSoundView = new SoundView(this);

	}

	Handler myHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {

			case PROGRESS_CHANGED:

				int nowTime = vv.getCurrentPosition();
				// 查找字幕
				for (int i = 0; i < lrc_list.size(); i++) {
					LRCObject val = lrc_list.get(i);
					if (nowTime > val.begintime && nowTime < val.endtime) {
						tv_lrc.setText(val.lrc);
						if(!lv_scroll){
							myAdapter.setSelectItem(i);
							myAdapter.notifyDataSetChanged();
							lv_lrc.setSelection(i);
						}
						break;
					} else {
						tv_lrc.setText("");
					}
				}

				seekBar.setProgress(nowTime);

				nowTime /= 1000;
				int minute = nowTime / 60;
				int hour = minute / 60;
				int second = nowTime % 60;
				minute %= 60;
				playedTextView.setText(String.format("%02d:%02d:%02d", hour,
						minute, second));

				sendEmptyMessage(PROGRESS_CHANGED);
				break;

			case HIDE_CONTROLER:
				hideController();
				break;
			case PAUSE:
				onClick(btn_play_pause);
			}

			super.handleMessage(msg);
		}
	};

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub

		boolean result = mGestureDetector.onTouchEvent(event);

		if (!result) {
			if (event.getAction() == MotionEvent.ACTION_UP) {

			}
			result = super.onTouchEvent(event);
		}

		return result;
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		Log.v(TAG, "onConfigurationChanged");
		getScreenSize();
		setVideoScale(SCREEN_DEFAULT);
		super.onConfigurationChanged(newConfig);
	}

	@Override
	protected void onPause() {
		Log.v(TAG, "onPause");
		playedTime = vv.getCurrentPosition();
		vv.pause();
		btn_play_pause.setImageResource(R.drawable.play);
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		Log.v(TAG, "onResume");
		vv.seekTo(playedTime);
//		vv.start();
//		btn_play_pause.setImageResource(R.drawable.pause);
		hideControllerDelay();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		Log.v(TAG, "onDestroy");
		// if (controler.isShowing()) {
		// controler.dismiss();
		// }
		if (controlView.isShown()) {
			controlView.setVisibility(View.GONE);
		}
		if (mSoundWindow.isShowing()) {
			mSoundWindow.dismiss();
		}

		myHandler.removeMessages(PROGRESS_CHANGED);
		myHandler.removeMessages(HIDE_CONTROLER);

		FileUtil.releaseRec(mMediaRecorder);
		super.onDestroy();
	}

	private void getScreenSize() {
		Display display = getWindowManager().getDefaultDisplay();
		screenHeight = display.getHeight();
		screenWidth = display.getWidth();
	}

	private void hideController() {
		if (controlView.isShown()) {
			controlView.setVisibility(View.GONE);
		}
		if (mSoundWindow.isShowing()) {
			mSoundWindow.dismiss();
		}
	}

	private void hideControllerDelay() {
		// myHandler.sendEmptyMessageDelayed(HIDE_CONTROLER, TIME);
	}

	private void showController() {
		// controler.update(screenWidth, controlHeight);
		// controler.update(0, 0, screenWidth, controlHeight);

		controlView.setVisibility(View.VISIBLE);
		// isControllerShow = true;
	}

	private void cancelDelayHide() {
		myHandler.removeMessages(HIDE_CONTROLER);
	}

	// private final static int SCREEN_FULL = 0;
	private final static int SCREEN_DEFAULT = 1;

	private void setVideoScale(int flag) {

		LayoutParams lp = vv.getLayoutParams();

		switch (flag) {
		// case SCREEN_FULL:
		//
		// Log.d(TAG, "screenWidth: " + screenWidth + " screenHeight: "
		// + screenHeight);
		// vv.setVideoScale(screenWidth, screenHeight);
		// getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		//
		// break;

		case SCREEN_DEFAULT:

			int videoWidth = vv.getVideoWidth();
			int videoHeight = vv.getVideoHeight();
			int mWidth = screenWidth;
			int mHeight = screenHeight - 25;

			if (videoWidth > 0 && videoHeight > 0) {
				if (videoWidth * mHeight > mWidth * videoHeight) {
					// Log.i("@@@", "image too tall, correcting");
					mHeight = mWidth * videoHeight / videoWidth;
				} else if (videoWidth * mHeight < mWidth * videoHeight) {
					// Log.i("@@@", "image too wide, correcting");
					mWidth = mHeight * videoWidth / videoHeight;
				} else {

				}
			}

			vv.setVideoScale(mWidth, mHeight);

			getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

			break;
		}
	}

	private int findAlphaFromSound() {
		if (mAudioManager != null) {
			// int currentVolume =
			// mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
			int alpha = currentVolume * (0xCC - 0x55) / maxVolume + 0x55;
			return alpha;
		} else {
			return 0xCC;
		}
	}

	private void updateVolume(int index) {
		if (mAudioManager != null) {
			if (isSilent) {
				mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
			} else {
				mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, index,
						0);
			}
			currentVolume = index;
			btn_sound_view.setAlpha(findAlphaFromSound());
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		switch (id) {
		case R.id.btn_show_text:
			break;
		case R.id.btn_backward:
			break;
		case R.id.btn_play_pause:
			cancelDelayHide();
			if (isPaused) {
				if (isCompletion) {
					// vv.reset();
					isCompletion = false;
				}
				vv.start();
				btn_play_pause.setImageResource(R.drawable.pause);
				hideControllerDelay();
			} else {
				vv.pause();
				btn_play_pause.setImageResource(R.drawable.play);
			}
			isPaused = !isPaused;
			break;
		case R.id.btn_forward:

			break;
		case R.id.btn_sound_view:
			cancelDelayHide();
			if (mSoundWindow.isShowing()) {
				mSoundWindow.dismiss();
			} else {
				if (mSoundWindow.isShowing()) {
					mSoundWindow.update(5, -50, SoundView.MY_WIDTH,
							SoundView.MY_HEIGHT);
				} else {
					mSoundWindow.showAtLocation(vv, Gravity.RIGHT
							| Gravity.CENTER_VERTICAL, 5, -50);
					mSoundWindow.update(5, -50, SoundView.MY_WIDTH,
							SoundView.MY_HEIGHT);
				}
			}
			// isSoundShow = !isSoundShow;
			hideControllerDelay();
			break;

		default:
			break;
		}

	}

	@Override
	public boolean onLongClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		switch (id) {
		case R.id.btn_sound_view:
			if (isSilent) {
				btn_sound_view.setImageResource(R.drawable.soundenable);
			} else {
				btn_sound_view.setImageResource(R.drawable.sounddisable);
			}
			isSilent = !isSilent;
			updateVolume(currentVolume);
			cancelDelayHide();
			hideControllerDelay();
			return true;
		case R.id.btn_show_text:

		}

		return false;
	}

}