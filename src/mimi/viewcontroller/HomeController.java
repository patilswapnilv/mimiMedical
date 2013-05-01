package mimi.viewcontroller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import mimi.medical.MainActivity;
import mimi.medical.R;
import mimi.type.Weibo;
import mimi.utils.SubmitDataByHttpClientAndOrdinaryWay;
import mimi.utils.UserUtils;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class HomeController extends ViewController {
	private static final String TAG = "HomeController";
	private static final boolean SHOW_LOGS = true;
	private Context mContext;
	private View mRootView;
	private ListView mListView;
	private MyAdapter mAdapter;
	private ArrayList<Weibo> mWeibos;
	public HomeController(Context context) {
		mContext = context;
		initView();
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		mRootView = View.inflate(mContext, R.layout.view_home, null);
		mListView = (ListView) mRootView.findViewById(R.id.list_view_main);
		//获取home top位置上的post按钮，并注册点击事件
		RelativeLayout topPostLayout = (RelativeLayout)mRootView.findViewById(R.id.layout_post);
		topPostLayout.setOnClickListener(mOnClickListener);
		//获取home top位置上的open user console按钮，并注册点击事件
		RelativeLayout topOpenLayout = (RelativeLayout)mRootView.findViewById(R.id.layout_open_user_console);
		topOpenLayout.setOnClickListener(mOnClickListener);
		//获取home top位置上的按钮，并注册点击事件
		RelativeLayout consoleLayout = (RelativeLayout)mRootView.findViewById(R.id.layout_open_profile);
		consoleLayout.setOnClickListener(mOnClickListener);
		mWeibos = new ArrayList<Weibo>();
		mAdapter = new MyAdapter(mContext, mWeibos);
		mListView.setAdapter(mAdapter);
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
			mRootView.bringToFront();
		} else {
			mRootView.setVisibility(View.GONE);
		}
	}

	@Override
	public void refreshView() {
		// TODO Auto-generated method stub
		((MainActivity)mContext).showProgressBar(true);
		new GetDataTask().execute();
	}
	
	private class MyAdapter extends BaseAdapter {

		private Context context;
		private ArrayList<Weibo> mWeibos;
		public MyAdapter(Context context, ArrayList<Weibo> list){
			this.context = context;
			mWeibos = list;
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if (mWeibos != null) {
				return mWeibos.size();
			}
			return 0;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			Weibo weibo = mWeibos.get(position);
			LinearLayout layout_comment;
			TextView info_text, text_view_time_content,
			text_view_comment_content_1, text_view_comment_time_1, text_view_comment_content_2, text_view_comment_time_2, button_comment_count;
			ImageView image_view_head, image_view_menu;
			if(convertView == null){
				convertView = View.inflate(context, R.layout.item_content, null);
				info_text = (TextView) convertView.findViewById(R.id.info_text);
				text_view_time_content = (TextView) convertView.findViewById(R.id.text_view_time_content);
				text_view_comment_content_1 = (TextView) convertView.findViewById(R.id.text_view_comment_content_1);
				text_view_comment_time_1 = (TextView) convertView.findViewById(R.id.text_view_comment_time_1);
				text_view_comment_content_2 = (TextView) convertView.findViewById(R.id.text_view_comment_content_2);
				text_view_comment_time_2 = (TextView) convertView.findViewById(R.id.text_view_comment_time_2);
				button_comment_count = (TextView) convertView.findViewById(R.id.button_comment_count);
				image_view_head = (ImageView) convertView.findViewById(R.id.image_view_head);
				image_view_menu = (ImageView) convertView.findViewById(R.id.image_view_menu);
				layout_comment = (LinearLayout) convertView.findViewById(R.id.layout_comment);
			} else {
				info_text = (TextView) convertView.findViewById(R.id.info_text);
				text_view_time_content = (TextView) convertView.findViewById(R.id.text_view_time_content);
				text_view_comment_content_1 = (TextView) convertView.findViewById(R.id.text_view_comment_content_1);
				text_view_comment_time_1 = (TextView) convertView.findViewById(R.id.text_view_comment_time_1);
				text_view_comment_content_2 = (TextView) convertView.findViewById(R.id.text_view_comment_content_2);
				text_view_comment_time_2 = (TextView) convertView.findViewById(R.id.text_view_comment_time_2);
				button_comment_count = (TextView) convertView.findViewById(R.id.button_comment_count);
				image_view_head = (ImageView) convertView.findViewById(R.id.image_view_head);
				image_view_menu = (ImageView) convertView.findViewById(R.id.image_view_menu);
				layout_comment = (LinearLayout) convertView.findViewById(R.id.layout_comment);
			}
			info_text.setText(weibo.content);
			text_view_time_content.setText(weibo.ctime);
			text_view_comment_content_1.setText(weibo.comment);
			layout_comment.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(SHOW_LOGS) Log.i(TAG, "layout_comment.onClick");
					((MainActivity)context).switchView(MainActivity.VIEW_COMMENT);
				}
			});
			return convertView;
		}
	}
	
	private OnClickListener mOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.layout_post:
				if(SHOW_LOGS)	Log.i(TAG,"top->publish weibo");
				((MainActivity)mContext).switchView(MainActivity.VIEW_PUBLISH_WEIBO);
				break;
			case R.id.layout_open_user_console:
				break;
			case R.id.layout_open_profile:
				if(SHOW_LOGS) Log.i(TAG, "top->open user console.onClick");
				((MainActivity)mContext).switchView(MainActivity.VIEW_PROFILE);
				break;
			default:
				break;
			}
		}
	};
	
	private class GetDataTask extends AsyncTask<Void, Void, ArrayList<Weibo>>{
		@Override
		protected ArrayList<Weibo> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				Map<String, String> map = new HashMap<String, String>();
				map.put("app", "wap");
				map.put("mod", "Index");
				map.put(UserUtils.KET_ACT, "publicsquare");
				String jsonStr = SubmitDataByHttpClientAndOrdinaryWay.submintDataByHttpClientDoGet(map, mContext.getString(R.string.login_register_url));
				return UserUtils.parserWeibo(jsonStr);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(ArrayList<Weibo> result) {
			// TODO Auto-generated method stub
			((MainActivity)mContext).showProgressBar(false);
			if(result != null && result.size() > 0) {
				mWeibos.clear();
				mWeibos.addAll(result);
				mAdapter.notifyDataSetChanged();
			}
		}
	}
}
