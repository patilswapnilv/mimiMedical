package mimi.medical;

import mimi.utils.UserUtils;
import mimi.viewcontroller.CommentController;
import mimi.viewcontroller.HomeController;
import mimi.viewcontroller.LoginController;
import mimi.viewcontroller.ProfileController;
import mimi.viewcontroller.PublishWeiboController;
import mimi.viewcontroller.RegisterController;
import mimi.viewcontroller.ViewController;
import mimi.viewcontroller.ViewController.OnBackListener;
import mimi.viewcontroller.WriteController;
import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.RelativeLayout.LayoutParams;

public class MainActivity extends Activity {
	private static final String TAG = "MainActivity";
	private static final boolean SHOW_LOGS = true;
	private ViewGroup mRootView;
	private ProgressBar mProgressBar;
	private LoginController mLoginController;
	private RegisterController mRegisterController;
	private HomeController mHomeController;
	private CommentController mCommentController;
	private WriteController mWriteController;
	private ProfileController mUserConsoleController;
	private PublishWeiboController mPublishWeiboController;
	
	private ViewController mLastViewController;
	private ViewController mCurrViewController;
	private SharedPreferences preferences;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		init();
	}
	
	private void init(){
		mRootView = (ViewGroup) View.inflate(this, R.layout.main, null);
		mProgressBar = (ProgressBar) mRootView.findViewById(R.id.progressbar);
		
		mLoginController = new LoginController(this);
		mLoginController.setOnBackListener(mOnBackListener);
		mRootView.addView(mLoginController.getView(),new ViewGroup.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		mLoginController.setVisible(false);
		
		mRegisterController = new RegisterController(this);
		mRegisterController.setOnBackListener(mOnBackListener);
		mRootView.addView(mRegisterController.getView(),new ViewGroup.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		mRegisterController.setVisible(false);
		
		mHomeController = new HomeController(this);
		mHomeController.setOnBackListener(mOnBackListener);
		mRootView.addView(mHomeController.getView(), new ViewGroup.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		mHomeController.setVisible(false);
		
		mCommentController = new CommentController(this);
		mCommentController.setOnBackListener(mOnBackListener);
		mRootView.addView(mCommentController.getView(), new ViewGroup.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		mCommentController.setVisible(false);
		
		mWriteController = new WriteController(this);
		mWriteController.setOnBackListener(mOnBackListener);
		mRootView.addView(mWriteController.getView(),new ViewGroup.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		mWriteController.setVisible(false);
		
		mPublishWeiboController = new PublishWeiboController(this);
		mPublishWeiboController.setOnBackListener(mOnBackListener);
		mRootView.addView(mPublishWeiboController.getView(),new ViewGroup.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		mPublishWeiboController.setVisible(false);
		
		mUserConsoleController = new ProfileController(this);
		mUserConsoleController.setOnBackListener(mOnBackListener);
		mRootView.addView(mUserConsoleController.getView(),new ViewGroup.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		mUserConsoleController.setVisible(false);
		preferences = getSharedPreferences(UserUtils.LOGIN_METHOD, 0);
		String usernameStr = preferences.getString(UserUtils.KEY_USERNAME, null);
		String passwordStr = preferences.getString(UserUtils.KEY_PASSWORD, null);
		if(usernameStr != null && passwordStr != null){
			mLoginController.initLogin(usernameStr, passwordStr);
		} else {
			switchView(VIEW_LOGIN);
		}
		
		setContentView(mRootView);
	}
	
	public void switchView(int type){
		if(SHOW_LOGS) Log.d(TAG, "switchView.type = " + type);
		mLastViewController = mCurrViewController;
		switch (type) {
		case VIEW_HOME:
			mCurrViewController = mHomeController;
			break;
		case VIEW_COMMENT:
			mCurrViewController = mCommentController;
			break;
		case VIEW_WRITE:
			mCurrViewController = mWriteController;
			break;
		case VIEW_LOGIN:
			mCurrViewController = mLoginController;
			break;
		case VIEW_REGISTER:
			mCurrViewController = mRegisterController;
			break;
		case VIEW_PROFILE:
			mCurrViewController = mUserConsoleController;
			break;
		case VIEW_PUBLISH_WEIBO:
			mCurrViewController = mPublishWeiboController;
			break;
		default:
			break;
		}
		mCurrViewController.setVisible(true);
		mCurrViewController.refreshView();
	}
	
	public void showProgressBar(boolean visible){
		if(visible) {
			mProgressBar.setVisibility(View.VISIBLE);
			mProgressBar.bringToFront();
		} else {
			mProgressBar.setVisibility(View.GONE);
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(mCurrViewController != null && mCurrViewController.onKeyDown(keyCode, event)){
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	
	private OnBackListener mOnBackListener = new OnBackListener() {
		@Override
		public void onBack(ViewController controller) {
			// TODO Auto-generated method stub
			if(controller == mHomeController){
				finish();
			} else {
				mCurrViewController = mLastViewController;
				controller.setVisible(false);
			}
		}
	};
	
	@Override
	public void finish() {
		new AlertDialog.Builder(this).setTitle(R.string.publisher_back_dialog_title)
		.setMessage(R.string.DesktopActivity_java_2)
		.setPositiveButton(R.string.publisher_back_dialog_ok_btn, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				MainActivity.super.finish();
			}
		}).setNegativeButton(R.string.publisher_back_dialog_cancel_btn, null).show();
	}
	
	public static final int VIEW_HOME = 1;
	public static final int VIEW_COMMENT = 2;
	public static final int VIEW_WRITE = 3;
	public static final int VIEW_LOGIN = 4;
	public static final int VIEW_REGISTER = 5;
	public static final int VIEW_PROFILE=6;
	public static final int VIEW_PUBLISH_WEIBO = 7;
}
