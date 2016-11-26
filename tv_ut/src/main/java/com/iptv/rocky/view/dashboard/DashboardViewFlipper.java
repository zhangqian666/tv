package com.iptv.rocky.view.dashboard;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterViewFlipper;
import android.widget.BaseAdapter;

import com.iptv.common.data.DashboardPicture;
import com.iptv.common.utils.LogUtils;
import com.iptv.rocky.model.TvApplication;

public class DashboardViewFlipper extends AdapterViewFlipper{
	
	private List<DashboardPicture> infos;
	
	public DashboardViewFlipper(Context context) {
		this(context, null);
	}

    public DashboardViewFlipper(Context context, AttributeSet attrs) {
	    super(context, attrs);
	}
    
    public void createView() {
		infos = new ArrayList<DashboardPicture>();
    }
    
    public void destroy() {
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
        	DashboardPictureView mDashboardPictureView = new DashboardPictureView(getContext());
			mDashboardPictureView.createView(infos.get(position));
			TvApplication.idsContent = infos.get(position).getTitle();
            return mDashboardPictureView;
        }
    };

	public List<DashboardPicture> getInfos() {
		return infos;
	}

	public void setInfos(List<DashboardPicture> infos) {
		this.infos = infos;
	}
}
