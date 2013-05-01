/**   
 * @title WriteCommentController.java
 * @package mimi.viewcontroller
 * @description 
 * @author SunQingqing  
 * @update 2012-9-9 下午05:08:43
 * @version V1.0   
 */
package mimi.viewcontroller;

import java.util.HashMap;
import java.util.Map;

import mimi.audio.AudioPlayDialog;
import mimi.audio.AudioRecordDialog;
import mimi.medical.MainActivity;
import mimi.medical.R;
import mimi.utils.SubmitDataByHttpClientAndOrdinaryWay;
import mimi.utils.UserUtils;
import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

/**
 * @description 发布微博
 * @version 1.0
 * @author SunQingqing
 * @update 2012-9-9 下午05:08:43 
 */

public class PublishWeiboController extends ViewController{
	private static final String TAG = "PublishController";
	private static final boolean SHOW_LOGS = true;
	private View mRootView;
	private Context mContext;
	private EditText mEditText;
	
	private String actionUrl = "http://42.121.237.116/medical/index.php?app=weibo&mod=operate&act=publish";
	
	/* 发布自己的录音*/
	private AudioRecordDialog audioRecordDialog;
	private AudioPlayDialog audioPlayDialog;
	private String audioFilePath;
	
	public PublishWeiboController(Context context){
		this.mContext = context;
		initView();
	}
	@Override
	public void initView() {
		mRootView = View.inflate(mContext, R.layout.view_publish_weibo, null);
		mEditText = (EditText) mRootView.findViewById(R.id.edittext);
		//获取publish weibo top位置上的back按钮，并注册点击事件
		RelativeLayout backCommentLayout = (RelativeLayout)mRootView.findViewById(R.id.layout_back_comment);
		backCommentLayout.setOnClickListener(mOnClickListener);
		
		RelativeLayout publishWeiboLayout = (RelativeLayout)mRootView.findViewById(R.id.layout_publish_weibo);
		publishWeiboLayout.setOnClickListener(mOnClickListener);
		
		ImageView publishAudioImageView = (ImageView)mRootView.findViewById(R.id.image_view_publish_audio);
		publishAudioImageView.setOnClickListener(mOnClickListener);
		
		audioRecordDialog = new AudioRecordDialog(mContext,this);
        audioPlayDialog = new AudioPlayDialog(mContext,this);
	}

	@Override
	public View getView() {
		return mRootView;
	}

	@Override
	public void refreshView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setVisible(boolean visible) {
		if (visible) {
			mRootView.setVisibility(View.VISIBLE);
			Animation anim = AnimationUtils.loadAnimation(mContext, R.anim.roll_up);
			anim.setAnimationListener(new AnimationListener() {
				@Override
				public void onAnimationStart(Animation animation) {
					// TODO Auto-generated method stub
				}
				@Override
				public void onAnimationRepeat(Animation animation) {
					// TODO Auto-generated method stub
				}
				@Override
				public void onAnimationEnd(Animation animation) {
					// TODO Auto-generated method stub
					mEditText.requestFocus();
					InputMethodManager imm = (InputMethodManager)mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
				}
			});
			mRootView.setAnimation(anim);
			mRootView.bringToFront();
		} else {
			InputMethodManager imm = (InputMethodManager)mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
			mRootView.setVisibility(View.GONE);
			mRootView.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.roll_down));
		}
	}
	
	public void setRecordFilePath(String path){
		audioFilePath = path;
	}
	
	public Context getContext(){
		return mContext;
	}
	
	private class PublishWeiboTask extends AsyncTask<String, Void, String>{
		private String weibo;
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			weibo = params[0];
			if(weibo == null) return null;
			try {
				Map<String, String> map = new HashMap<String, String>();
				map.put("content", weibo);
				map.put("publish_type","0");
				return SubmitDataByHttpClientAndOrdinaryWay.submintDataByHttpClientDoPost(map, actionUrl);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			((MainActivity)mContext).showProgressBar(false);
			if(SHOW_LOGS) Log.i(TAG, "publish result = "+ result);
			if(result != null){
				Toast.makeText(mContext, "publish success", Toast.LENGTH_SHORT).show();
				setVisible(false);
				((MainActivity)mContext).switchView(MainActivity.VIEW_HOME);
			}
		}
	}
	
	private void publishWeibo(){
		if(SHOW_LOGS)	Log.i(TAG, "click publish");
		String weibo = mEditText.getText().toString();
		if(weibo != null && !weibo.trim().equals("")){
			((MainActivity)mContext).showProgressBar(true);
			new PublishWeiboTask().execute(weibo);
		}else{
			Toast.makeText(mContext, "输入为空,请确认输入",Toast.LENGTH_SHORT).show();
		}
	}

	private OnClickListener mOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.layout_back_comment:
				if(mOnBackListener != null){
					mOnBackListener.onBack(PublishWeiboController.this);
				}
				break;
			case R.id.layout_publish_weibo:
				publishWeibo();
				break;
			case R.id.image_view_publish_audio:
				audioRecordDialog.show();
				break;
			default:
				break;
			}
		}
	};
}
