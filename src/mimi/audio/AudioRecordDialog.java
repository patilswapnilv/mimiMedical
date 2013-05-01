/**   
 * @title AudioRecordDialog.java
 * @package mimi.sound
 * @description 
 * @author SunQingqing  
 * @update 2012-9-18 下午12:03:07
 * @version V1.0   
 */
package mimi.audio;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import mimi.medical.R;
import mimi.utils.FileUtils;
import mimi.viewcontroller.PublishWeiboController;
import mimi.viewcontroller.ViewController;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.media.MediaRecorder;
import android.media.MediaRecorder.AudioEncoder;
import android.media.MediaRecorder.AudioSource;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * @description 
 * @version 1.0
 * @author SunQingqing
 * @update 2012-9-18 下午12:03:07 
 */

public class AudioRecordDialog extends Dialog implements View.OnClickListener{
	private static final String TAG = "AudioRecordDialog";
	private static final boolean SHOW_LOG = true; 
	
	private ViewController mViewController;
	private Context mContext;
	
	private MediaRecorder mMediaRecorder;
	private Button startRecordBtn;
	private Button cancelRecordBtn;
	private TextView recordStateHintTextView;
	private ProgressBar prepareRecordProgressBar;
	
	private File recordFile;
	
	private int secondLen;
	private Timer mTimer;
	private MyTimerTask mTimerTask;
	
	public AudioRecordDialog(Context context,ViewController viewController) {
		//应用自定义的主题
		super(context,R.style.cyDialogTheme);
		mViewController = viewController;
		mContext = context;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_record);
	}
	
	@Override
	protected void onStart() {
		init();
		super.onStart();
	}

	private void init(){
		startRecordBtn = (Button)findViewById(R.id.btn_start_record);
		cancelRecordBtn = (Button)findViewById(R.id.btn_cancel_record);
		recordStateHintTextView = (TextView)findViewById(R.id.tv_record_state_hint);
		prepareRecordProgressBar = (ProgressBar)findViewById(R.id.loading_progress);
		startRecordBtn.setEnabled(false);
		startRecordBtn.setText("开始");
		recordStateHintTextView.setText("正在初始化录音设备");
		cancelRecordBtn.setEnabled(false);
		prepareRecordProgressBar.setVisibility(View.VISIBLE);
		startRecordBtn.setOnClickListener(this);
		cancelRecordBtn.setOnClickListener(this);
		mTimer = new Timer(true);
		secondLen = 60;
		new Thread(new StartPrepareRecordThread()).start();
	}
	
	final class StartPrepareRecordThread implements Runnable{

		@Override
		public void run() {
			new Handler(mContext.getMainLooper())
			.postDelayed(new PrepareRecordThread(AudioRecordDialog.this),500L);
		}
		
	}
	/**
	 * 录音初始化线程
	 */
	final class PrepareRecordThread implements Runnable{

		private AudioRecordDialog dialog;
		public PrepareRecordThread(AudioRecordDialog dialog){
			this.dialog = dialog;
		}
		@Override
		public void run() {
			try {
				Log.i("PrepareRecordThread","prepare record");
				Thread.sleep(500L);
				dialog.initReocrd();
				dialog.finishPrepareRecord();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
	/**
	 * 用于倒计时的task
	 */
	final class MyTimerTask extends TimerTask{

		@Override
		public void run() {
			((Activity)mContext).runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					recordStateHintTextView.setText("正在录音 - "+secondLen+"秒");
					secondLen--;
					if(secondLen <= 0){
						finishRecord();
						recordStateHintTextView.setText("录音完毕");
					}
				}
			});
		}
		
	}
	
	private void initReocrd() throws IOException,IllegalStateException{
		
		mMediaRecorder = new MediaRecorder();
		
		recordFile = FileUtils.createAudioFile();
		 ((PublishWeiboController)mViewController).setRecordFilePath(recordFile.getAbsolutePath());
		 
		if(SHOW_LOG) 	Log.i(TAG,"recordFile: " + recordFile.getAbsolutePath());
		
		mMediaRecorder.setAudioSource(AudioSource.MIC);
		mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
		mMediaRecorder.setAudioEncoder(AudioEncoder.DEFAULT);
		mMediaRecorder.setOutputFile(recordFile.getAbsolutePath());
		mMediaRecorder.prepare();
	}
	
	private void finishPrepareRecord(){
		if(SHOW_LOG)	Log.i(TAG,"finish prepare record");
		recordStateHintTextView.setText("录音设备初始化完毕");
		startRecordBtn.setEnabled(true);
		cancelRecordBtn.setEnabled(true);
		prepareRecordProgressBar.setVisibility(View.GONE);
	}
	
	private void finishRecord(){
		if(SHOW_LOG)	Log.i(TAG , "record finished");
		if(mTimer != null){
			mTimer.cancel();
			mTimer = null;
			if(mTimerTask!=null){
				mTimerTask.cancel();
				mTimerTask = null;
			}
		}
		if(mMediaRecorder!=null){
			mMediaRecorder.stop();
        	mMediaRecorder.release();
        	mMediaRecorder = null;
		}
        this.dismiss();
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_start_record:
			if(SHOW_LOG)	Log.i(TAG,"recording");
			
			//开启录音倒计时
			if(mTimer!=null){
				if(mTimerTask!=null){
					mTimerTask.cancel();
				}
			}
			mTimerTask = new MyTimerTask();
			mTimer.schedule(mTimerTask, 0, 1000);
			
			//开始录音
			mMediaRecorder.start();
			
			//设置完成的点击事件
			startRecordBtn.setText("完成");
			startRecordBtn.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					finishRecord();
				}
			});
			break;

		case R.id.btn_cancel_record:
			this.dismiss();
			break;
		}
	}

}
