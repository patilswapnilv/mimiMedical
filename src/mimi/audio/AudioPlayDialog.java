/**   
 * @title AudioPlayDialog.java
 * @package mimi.audio
 * @description 
 * @author SunQingqing  
 * @update 2012-9-18 下午02:43:25
 * @version V1.0   
 */
package mimi.audio;

import java.util.Timer;
import java.util.TimerTask;

import mimi.medical.R;
import mimi.viewcontroller.ViewController;

import android.app.Activity;
import android.app.Dialog;
import android.media.MediaPlayer;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @description 
 * @version 1.0
 * @author SunQingqing
 * @update 2012-9-18 下午02:43:25 
 */

public class AudioPlayDialog extends Dialog
	implements MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener, MediaPlayer.OnPreparedListener{

	private static final String TAG = "AudioPlayDialog";
	private final boolean SHOW_LOG = true;
	private Context mContext;
	private ViewController mViewController;
	private MediaPlayer mMediaPlayer;
	
	private TextView playStateHintTextView;
	private Button finishPlayButton;
	private String audioFilePath;
	
	private int secondLen;
	private Timer mTimer;
	private MyTimerTask mTimerTask;
	
	public AudioPlayDialog(Context context,ViewController viewController) {
		//应用自定义的主题
		super(context,R.style.cyDialogTheme);
		mContext = context;
		mViewController = viewController;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_player);
	}
	
	@Override
	protected void onStart() {
		init();
	}
	
	private void init(){
		playStateHintTextView = (TextView)findViewById(R.id.tv_player_state_hint);
		finishPlayButton = (Button)findViewById(R.id.btn_finish_play);
		finishPlayButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mMediaPlayer!=null){
					mMediaPlayer.stop();
					mMediaPlayer.reset();
					mMediaPlayer.release();
					mMediaPlayer = null;
				}
				dismiss();
			}
		});
		finishPlayButton.setEnabled(false);
		secondLen = 0;
		mTimer = new Timer(true);
		mMediaPlayer = new MediaPlayer();
		mMediaPlayer.setOnCompletionListener(this);
		mMediaPlayer.setOnErrorListener(this);
		mMediaPlayer.setOnPreparedListener(this);
		if(SHOW_LOG) 	Log.i(TAG,"audioFilePath: " + audioFilePath);
		try {
			mMediaPlayer.setDataSource(audioFilePath);
			mMediaPlayer.prepare();
		} catch (Exception e) {
			e.printStackTrace();
			finishPlayButton.setEnabled(true);
			playStateHintTextView.setText("无法播放文件");
		}
	}

	@Override
	protected void onStop() {
		if(mMediaPlayer!=null){
			mMediaPlayer.stop();
			mMediaPlayer.reset();
			mMediaPlayer.release();
			mMediaPlayer = null;
		}
	}

	public void setAudioFilePath(String path){
		this.audioFilePath = path;
	}
	
	public void play(String filePath){
		setAudioFilePath(filePath);
		this.show();
	}

	
	/**
	 * 用于播放计时的task
	 */
	final class MyTimerTask extends TimerTask{

		@Override
		public void run() {
			((Activity)mContext).runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					playStateHintTextView.setText("正在播放 - "+secondLen+"秒");
					secondLen++;
				}
			});
		}
		
	}
	
	@Override
	public void onCompletion(MediaPlayer arg0) {
		if(mTimer != null){
			mTimer.cancel();
			mTimer = null;
			if(mTimerTask!=null){
				mTimerTask.cancel();
				mTimerTask = null;
			}
		}
		dismiss();
	}

	@Override
	public boolean onError(MediaPlayer arg0, int arg1, int arg2) {
		dismiss();
	    Toast.makeText(getOwnerActivity(), "播放出错", 0);
	    return true;
	}

	@Override
	public void onPrepared(MediaPlayer arg0) {
		finishPlayButton.setEnabled(true);
		//开启播放计时
		if(mTimer!=null){
			if(mTimerTask!=null){
				mTimerTask.cancel();
			}
		}
		mTimerTask = new MyTimerTask();
		mTimer.schedule(mTimerTask,0, 1000);
		mMediaPlayer.start();
	}
	
}
