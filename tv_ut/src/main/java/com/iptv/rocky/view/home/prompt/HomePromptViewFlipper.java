package com.iptv.rocky.view.home.prompt;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterViewFlipper;
import android.widget.BaseAdapter;

import com.iptv.common.data.HomePrompt;
import com.iptv.common.utils.LogUtils;

public class HomePromptViewFlipper extends AdapterViewFlipper{
	
	private List<HomePrompt> infos;
	private int position;
	
	// 定时退出开机提示
	private Handler handlerExitPage = new Handler();
	
	public HomePromptViewFlipper(Context context) {
		this(context, null);
	}

    public HomePromptViewFlipper(Context context, AttributeSet attrs) {
	    super(context, attrs);
	}
    
/*    public void createView(ArrayList<DashboardPicture> items) {
    	int size = items.size();
    	infos = items;
    	this.setAdapter(adapter);
    	
		mDashboardJsonFactory = new DashboardJsonFactory();
		mDashboardJsonFactory.setHttpEventHandler(dashBoardHandler);
		//mDashboardJsonFactory.DownloadDatas();
    	//adapter.registerDataSetObserver(observer)
    	
    	startFlipping();
 
    }*/
    
    public void createView() {
		//mDashboardJsonFactory = new DashboardJsonFactory();
		//mDashboardJsonFactory.setHttpEventHandler(dashBoardHandler);
		
		infos = new ArrayList<HomePrompt>();
		//mDashboardJsonFactory.DownloadDatas();
    }
    
    
    public void destroy() {
    	/*if (mPages != null) {
    		for (int i = 0, j = mPages.size(); i < j; i++) {
    			mPages.get(i).destroy();
    		}
    	}*/
    }
    
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
    	if (super.dispatchKeyEvent(event)) {
    		if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT 
				|| event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT) {
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					return true;
				}
    		}
    	}
    	return false;
    }
    
    
    
  	public BaseAdapter adapter=new BaseAdapter()
    {

        @Override
        public int getCount() {
            return infos.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
        //该方法返回的View代表了每个列表项
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            
        	LogUtils.debug("开始创建ImageView: position:"+position);
           
        	HomePromptPictureView mHomePromptPictureView = new HomePromptPictureView(getContext());
        	mHomePromptPictureView.createView(infos.get(position));
        	
        	handlerExitPage.removeCallbacks(runnableChangePage);
        	handlerExitPage.postDelayed(runnableChangePage,infos.get(position).getDuration()*1000);
            return mHomePromptPictureView;
        }
    };

	public List<HomePrompt> getInfos() {
		return infos;
	}

	public void setInfos(List<HomePrompt> infos) {
		this.infos = infos;
	}
    
	/**
	 *  定时更换信息
	 */
/*	Runnable runnableRefreshDashboardInfo = new Runnable() {
		@Override
		public void run() {
			LogUtils.debug("定时更新数据");
			//stopFlipping();
			mDashboardJsonFactory.DownloadDatas();
		}
	};*/
	
	
	/**
	 *  定时更换信息
	 */
	Runnable runnableChangePage = new Runnable() {
		@Override
		public void run() {
			if(infos.size()>1){
				
			}else{
				
			}
			
		}
	};

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}


}
