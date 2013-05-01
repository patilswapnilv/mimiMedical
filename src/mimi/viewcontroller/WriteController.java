/**   
 * @title WriteCommentController.java
 * @package mimi.viewcontroller
 * @description 
 * @author SunQingqing  
 * @update 2012-9-9 下午05:08:43
 * @version V1.0   
 */
package mimi.viewcontroller;

import mimi.medical.MainActivity;
import mimi.medical.R;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;

/**
 * @description 
 * @version 1.0
 * @author SunQingqing
 * @update 2012-9-9 下午05:08:43 
 */

public class WriteController extends ViewController{
	private static final String TAG = "WriteCommentController";
	private static final boolean SHOW_LOGS = true;
	private View mRootView;
	private Context mContext;
	private EditText mEditText;
	public WriteController(Context context){
		this.mContext = context;
		initView();
	}
	@Override
	public void initView() {
		mRootView = View.inflate(mContext, R.layout.view_write, null);
		mEditText = (EditText) mRootView.findViewById(R.id.edittext);
		//获取writecomment top位置上的back按钮，并注册点击事件
		RelativeLayout backCommentLayout = (RelativeLayout)mRootView.findViewById(R.id.layout_back_comment);
		backCommentLayout.setOnClickListener(mOnClickListener);
		
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

	private OnClickListener mOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.layout_back_comment:
				if(mOnBackListener != null){
					mOnBackListener.onBack(WriteController.this);
				}
				break;
			default:
				break;
			}
		}
	};
}
