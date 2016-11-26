package com.iptv.rocky.base;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.iptv.common.utils.DataReloadUtil;
import com.iptv.rocky.model.ActivityStack;
import com.iptv.rocky.utils.TvUtils;
import com.iptv.rocky.view.StatusBarView;
import com.iptv.rocky.view.TVItemViewReloadable;
import com.iptv.rocky.R;

public abstract class BaseActivity extends Activity {
	
	private StatusBarView mStatusView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		ActivityStack.pushActivity(this);
	}

	@Override
	protected void onStart() {
		super.onStart();
		
		mStatusView = (StatusBarView) findViewById(R.id.tv_status_bar);
		if (mStatusView != null) {
			mStatusView.start();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		if (mStatusView != null) {
			mStatusView.stop();
		}
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
		//Log.d("BaseActivity","ActivityStack.size():"+ActivityStack.size());
 		if (ActivityStack.size() == 1 && 
			ActivityStack.getFront().getClass().equals(this.getClass())) {
 			TvUtils.doubleClickQuitApp(this);
		} else {
			ActivityStack.popCurrent();
		}
    }
	
	public void handleMessage() {
		int size = DataReloadUtil.size();
		//Log.d("BaseActivity", "DataReloadUtil的大小"+ size);
		if (size > 0) {
			for (int i = 0, index = 0; i < size; i++, index++) {
				Integer value = DataReloadUtil.get(index);
				if (value != null) {
					View view = findViewById(value);
					//Log.d("BaseActivity",view.toString());
					if (view instanceof TVItemViewReloadable) {
						((TVItemViewReloadable) view).reloadData();
						if (DataReloadUtil.removeMessage(index)) {
							index--;
						}
	    			}
				}
    		}
    	}
    }
	
}
