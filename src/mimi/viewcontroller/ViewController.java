package mimi.viewcontroller;

import android.view.KeyEvent;
import android.view.View;

public abstract class ViewController {
	public abstract void initView();

	public abstract View getView();

	public abstract void setVisible(boolean visible);

	public abstract void refreshView();
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 没有显示，则忽略按键事件
		if (!getView().isShown()) {
			return false;
		}
		// 处理返回按键
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (mOnBackListener != null) {
				mOnBackListener.onBack(this);
				return true;
			}
		}
		return false;
	}

	/**
	 * 设置退出事件的监听器
	 * 
	 * @param listener
	 */
	public void setOnBackListener(OnBackListener listener) {
		mOnBackListener = listener;
	}

	/**
	 * 控件退出事件的监听器
	 */
	public interface OnBackListener {
		public void onBack(ViewController controller);
	}

	protected OnBackListener mOnBackListener;
}
