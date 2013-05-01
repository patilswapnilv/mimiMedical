package mimi.viewcontroller;

import mimi.medical.MainActivity;
import mimi.medical.R;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class CommentController extends ViewController {
	private static final String TAG = "CommentController";
	private static final boolean SHOW_LOGS = true;
	
	private Context mContext;
	private View mRootView;
	private MyListView mListView;
	private MyAdapter mAdapter;
	
	public CommentController(Context context){
		mContext = context;
		initView();
	}
	@Override
	public void initView() {
		// TODO Auto-generated method stub
		mRootView = View.inflate(mContext, R.layout.view_comment, null);
		mListView = (MyListView)mRootView.findViewById(R.id.listview_view_content_comment);
		//获取comment top位置上的back按钮，并注册点击事件
		RelativeLayout backHomeLayout = (RelativeLayout)mRootView.findViewById(R.id.layout_back_home);
		backHomeLayout.setOnClickListener(mOnClickListener);
		//获取comment top位置上的share按钮，并注册点击事件
		RelativeLayout shareLayout = (RelativeLayout)mRootView.findViewById(R.id.layout_comment_share);
		shareLayout.setOnClickListener(mOnClickListener);
		//获取comment top位置上的menu按钮，并注册点击事件
		RelativeLayout menuLayout = (RelativeLayout)mRootView.findViewById(R.id.layout_comment_menu);
		menuLayout.setOnClickListener(mOnClickListener);
		//注册写评论按钮点击事件
		((Button)mRootView.findViewById(R.id.add_comment_button)).setOnClickListener(mOnClickListener);
		mAdapter = new MyAdapter(mContext);
		mListView.setAdapter(mAdapter);
	}
	
	private class MyAdapter extends BaseAdapter {

		private Context context;
		public MyAdapter(Context context){
			this.context = context;
		}
		
		@Override
		public int getCount() {
			return 10;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView == null){
				convertView = View.inflate(context, R.layout.item_comment, null);
			} 
			return convertView;
		}
	}

	@Override
	public View getView() {
		// TODO Auto-generated method stub
		return mRootView;
	}

	@Override
	public void setVisible(boolean visible) {
		// TODO Auto-generated method stub
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
			case R.id.layout_back_home:
				if(mOnBackListener != null){
					mOnBackListener.onBack(CommentController.this);
				}
				break;
			case R.id.layout_comment_share:
				if(SHOW_LOGS) Log.i(TAG, "comment top->share.onClick");
				Toast.makeText(mContext, "comment share", Toast.LENGTH_SHORT).show();
				break;
			case R.id.layout_comment_menu:
				if(mOnBackListener != null){
					mOnBackListener.onBack(CommentController.this);
				}
				break;
			case R.id.add_comment_button:
				((MainActivity)mContext).switchView(MainActivity.VIEW_WRITE);
				break;
			default:
				break;
			}
		}
	};
}
