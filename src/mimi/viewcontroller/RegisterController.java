package mimi.viewcontroller;

import java.util.HashMap;
import java.util.Map;

import mimi.medical.MainActivity;
import mimi.medical.R;
import mimi.utils.SubmitDataByHttpClientAndOrdinaryWay;
import mimi.utils.UserUtils;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class RegisterController extends ViewController {
	private static final String TAG = "RegisterController";
	private static final boolean SHOW_LOGS = true;
	private Context mContext;
	private View mRootView;
	
	public RegisterController(Context context){
		this.mContext = context;
		initView();
	}
	
	@Override
	public void initView() {
		mRootView = View.inflate(mContext, R.layout.view_register, null);
		RelativeLayout layoutRegisterBack = (RelativeLayout)mRootView.findViewById(R.id.layout_register_back);
		layoutRegisterBack.setOnClickListener(mOnClickListener);
		//注册按钮点击事件注册
		Button registerButton = (Button)mRootView.findViewById(R.id.button_register);
		registerButton.setOnClickListener(mOnClickListener);
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

	private OnClickListener mOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.button_register:
				EditText edit_user = (EditText) mRootView.findViewById(R.id.edit_account);
				EditText edit_password = (EditText) mRootView.findViewById(R.id.edit_password);
				EditText edit_re_password = (EditText) mRootView.findViewById(R.id.edit_re_password);
				EditText edit_email = (EditText) mRootView.findViewById(R.id.edit_email);
				String[] str_edit = {edit_user.getText().toString(), edit_password.getText().toString(),
						edit_re_password.getText().toString(), edit_email.getText().toString()};
				
				for (int i = 0; i < str_edit.length; i++) {
					if(UserUtils.replaceBlank(str_edit[i]).length() == 0){
						switch (i) {
						case 0:
							Toast.makeText(mContext, R.string.username_not_null, Toast.LENGTH_SHORT).show();
							edit_user.setText("");
							break;
						case 1:
							Toast.makeText(mContext, R.string.password_not_null, Toast.LENGTH_SHORT).show();
							edit_password.setText("");
							break;
						case 2:
							Toast.makeText(mContext, R.string.password_notsame, Toast.LENGTH_SHORT).show();
							edit_re_password.setText("");
							break;
						case 3:
							Toast.makeText(mContext, R.string.email_not_empty, Toast.LENGTH_SHORT).show();
							edit_email.setText("");
							break;
						default:
							break;
						}
						return;
					}
				}
				RegisterObj obj = new RegisterObj();
//				obj.userName = str_edit[0];
//				obj.passWord = str_edit[1];
//				obj.re_passWord = str_edit[2];
//				obj.email = str_edit[3];
				new RegisterTask().execute(obj);
				break;
			case R.id.layout_register_back:
				if(mOnBackListener != null){
					mOnBackListener.onBack(RegisterController.this);
				}
				break;
			default:
				break;
			}
		}
	};
	
	private class RegisterTask extends AsyncTask<RegisterObj, Void, String>{
		private RegisterObj obj;
		@Override
		protected String doInBackground(RegisterObj... params) {
			// TODO Auto-generated method stub
			obj = params[0];
			if(obj == null) return null;
			try {
				Map<String, String> map = new HashMap<String, String>();
				map.put("app", "wap");
				map.put("mod", "Public");
				map.put(UserUtils.KET_REGISTER, UserUtils.REGISTER_METHOD);
				map.put(UserUtils.KEY_REGISTER_USERNAME, obj.userName);
				map.put(UserUtils.KEY_REGISTER_PASSWORD, obj.passWord);
				map.put(UserUtils.KEY_RE_PASSWORD, obj.re_passWord);
				map.put(UserUtils.KEY_REGISTER_EMAIL, obj.email);
				return SubmitDataByHttpClientAndOrdinaryWay.submintDataByHttpClientDoPost(map, mContext.getString(R.string.login_register_url));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			if(result == null) return;
			if (SHOW_LOGS) Log.d(TAG, "register_result = " + result);
			if(result.equals(UserUtils.SUCCED)){
				Toast.makeText(mContext, R.string.register_success + obj.userName + obj.passWord, Toast.LENGTH_SHORT).show();
				((MainActivity)mContext).switchView(MainActivity.VIEW_HOME);
			} else {
				Toast.makeText(mContext, result, Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	private class RegisterObj {
		public String userName = "test3";
		public String passWord = "123456";
		public String re_passWord = "123456";
		public String email = "test@test.com";
	}
}
