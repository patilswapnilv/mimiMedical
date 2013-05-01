package mimi.viewcontroller;

import java.util.HashMap;
import java.util.Map;

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
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class LoginController extends ViewController {

	private static final String TAG = "LoginController";
	private static final boolean SHOW_LOGS = true;
	private Context mContext;
	private View mRootView;
	private EditText mEdit_UserName, mEdit_PassWord;
	
	public LoginController(Context context){
		this.mContext = context;
		initView();
	}
	
	@Override
	public void initView() {
		mRootView = View.inflate(mContext, R.layout.view_login, null);
		mEdit_UserName = (EditText) mRootView.findViewById(R.id.edit_username);
		mEdit_PassWord = (EditText) mRootView.findViewById(R.id.edit_password);
		RelativeLayout registerLayout = (RelativeLayout)mRootView.findViewById(R.id.layout_register);
		registerLayout.setOnClickListener(mOnClickListener);
		
		RelativeLayout loginBackLayout = (RelativeLayout)mRootView.findViewById(R.id.layout_login_back);
		loginBackLayout.setOnClickListener(mOnClickListener);
		
		//登录按钮点击事件注册
		Button loginButton = (Button)mRootView.findViewById(R.id.button_login);
		loginButton.setOnClickListener(mOnClickListener);
	}

	@Override
	public View getView() {
		return mRootView;
	}

	@Override
	public void setVisible(boolean visible) {
		if (visible) {
			mRootView.setVisibility(View.VISIBLE);
			mRootView.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.right_enter));
			mRootView.bringToFront();
		} else {
			mRootView.setVisibility(View.GONE);
			mRootView.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.right_exit));
		}
	}

	@Override
	public void refreshView() {
		// TODO Auto-generated method stub
		
	}

	public void initLogin(String userName, String passWord) {
		((MainActivity)mContext).showProgressBar(true);
		new LoginTask().execute(new LoginObj(userName, passWord));
	}
	
	private class LoginTask extends AsyncTask<LoginObj, Void, String>{
		private LoginObj obj;
		@Override
		protected String doInBackground(LoginObj... params) {
			// TODO Auto-generated method stub
			obj = params[0];
			if(obj == null) return null;
			try {
				Map<String, String> map = new HashMap<String, String>();
				map.put("app", "wap");
				map.put("mod", "Public");
				map.put(UserUtils.KET_ACT, UserUtils.LOGIN_METHOD);
				map.put(UserUtils.KEY_USERNAME, obj.userName);
				map.put(UserUtils.KEY_PASSWORD, obj.passWord);
				return SubmitDataByHttpClientAndOrdinaryWay.submintDataByHttpClientDoGet(map, mContext.getString(R.string.login_register_url));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			((MainActivity)mContext).showProgressBar(false);
			if (SHOW_LOGS) Log.d(TAG, "login_result = " + result);
			if(result != null && result.equalsIgnoreCase(UserUtils.SUCCED)){
				Editor sp = mContext.getSharedPreferences(UserUtils.LOGIN_METHOD, 0).edit();
				sp.putString(UserUtils.KEY_USERNAME, obj.userName);
				sp.putString(UserUtils.KEY_PASSWORD, obj.passWord);
				sp.commit();
				((MainActivity)mContext).switchView(MainActivity.VIEW_HOME);
			} else {
				Toast.makeText(mContext, R.string.login_failed, Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	private class LoginObj {
		public LoginObj(String userName, String passWord) {
			// TODO Auto-generated constructor stub
			this.userName = userName;
			this.passWord = passWord;
		}
		private String userName = "wangqian0601@gmail.com";
		private String passWord = "123456";
	}
	
	private OnClickListener mOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.layout_login_back:
				if(mOnBackListener != null){
					mOnBackListener.onBack(LoginController.this);
				}
				break;
			case R.id.button_login:
				String userName = mEdit_UserName.getText().toString();
				String passWord = mEdit_PassWord.getText().toString();
				if(UserUtils.replaceBlank(userName).length() <= 0) {
					Toast.makeText(mContext, R.string.username_not_null, Toast.LENGTH_SHORT).show();
					mEdit_UserName.setText("");
					return;
				}
				if(UserUtils.replaceBlank(passWord).length() <= 0){
					Toast.makeText(mContext, R.string.password_not_null, Toast.LENGTH_SHORT).show();
					mEdit_PassWord.setText("");
					return;
				}
				InputMethodManager imm = (InputMethodManager)mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(mEdit_PassWord.getWindowToken(), 0);
				initLogin("wangqian0601@gmail.com", "123456");
				break;
			case R.id.layout_register:
				((MainActivity)mContext).switchView(MainActivity.VIEW_REGISTER);
				break;
			default:
				break;
			}
		}
	};
}
