package com.iptv.rocky.base;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.iptv.common.utils.DataReloadUtil;
import com.iptv.rocky.HomeActivity;
import com.iptv.rocky.model.ActivityStack;
import com.iptv.rocky.utils.TvUtils;
import com.iptv.rocky.view.TVItemViewReloadable;

public abstract class BasePlayActivity extends Activity {
	
	//private StatusBarView mStatusView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		ActivityStack.pushActivity(this);
	}

	@Override
	protected void onStart() {
		super.onStart();
		
//		mStatusView = (StatusBarView) findViewById(R.id.tv_status_bar);
//		if (mStatusView != null) {
//			mStatusView.start();
//		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		
//		if (mStatusView != null) {
//			mStatusView.stop();
//		}
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}
	
	@Override
    public void onBackPressed() {
		Log.d("BasePlayActivity", "调取返回键操作");
		Log.d("BasePlayActivity","ActivityStack.size():"+ActivityStack.size());
 		if (ActivityStack.size() == 1 && 
			ActivityStack.getFront().getClass().equals(this.getClass()) && 
			HomeActivity.class.equals(this.getClass())) {
 			Log.d("BasePlayActivity", "1");
 			TvUtils.doubleClickQuitApp(this);
		} else {
			Log.d("BasePlayActivity", "弹出当前页");
			ActivityStack.popCurrent();
		}
    }
	
	public void handleMessage() {
		int size = DataReloadUtil.size();
		if (size > 0) {
			for (int i = 0, index = 0; i < size; i++, index++) {
				Integer value = DataReloadUtil.get(index);
				if (value != null) {
					View view = findViewById(value);
					if (view instanceof TVItemViewReloadable) {
						((TVItemViewReloadable)view).reloadData();
						if (DataReloadUtil.removeMessage(index)) {
							index--;
						}
	    			}
				}
    		}
    	}
    }
	
}
