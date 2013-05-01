package mimi.viewcontroller;

import mimi.medical.R;
import android.content.Context;
import android.view.View;

public class ProfileController extends ViewController {

	private static final String TAG = "ProfileController";
	private static final boolean SHOW_LOGS = true;
	private Context mContext;
	private View mRootView;
	
	public ProfileController(Context context){
		this.mContext = context;
		initView();
	}
	
	@Override
	public void initView() {
		mRootView = View.inflate(mContext, R.layout.view_profile, null);
	}

	@Override
	public View getView() {
		return mRootView;
	}

	@Override
	public void setVisible(boolean visible) {
		if (visible) {
			mRootView.setVisibility(View.VISIBLE);
			mRootView.bringToFront();
		} else {
			mRootView.setVisibility(View.GONE);
		}
	}

	@Override
	public void refreshView() {
		// TODO Auto-generated method stub
		
	}

}
