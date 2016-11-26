package com.iptv.rocky.view.special;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.RelativeLayout;

import com.iptv.common.HttpEventHandler;
import com.iptv.common.data.EnumType;
import com.iptv.common.data.SpecialItemObj;
import com.iptv.common.data.SpecialObj;
import com.iptv.common.data.VodChannel;
import com.iptv.common.utils.LogUtils;
import com.iptv.common.view.AsyncImageView;
import com.iptv.common.view.AsyncImageView.AsyncImageLoadedListener;
import com.iptv.rocky.R;
import com.iptv.rocky.hwdata.IPTVUriUtils;
import com.iptv.rocky.hwdata.json.SpecialDetailFactory;
import com.iptv.rocky.hwdata.json.VodChannelJsonFactory;
import com.iptv.rocky.hwdata.xml.VodChannelFactory;
import com.iptv.rocky.model.TvApplication;
import com.iptv.rocky.model.special.SpecialListItemData;
import com.iptv.rocky.model.vodmovielist.VodMovieListLayoutItem;
import com.iptv.rocky.utils.TvUtils;
import com.iptv.rocky.view.StatusBarView;

public class SpecialDetailMasterView extends RelativeLayout 
	implements SpecialListItemData.onScrollListener, AsyncImageLoadedListener {
	
	private HorizontalScrollView scrollView;
	private AsyncImageView backgroundImage;
	private SpecialHListView hlistView;
	private SpecialDetailFactory factory;
	
	private VodChannelFactory mVodChannelFactory;
	
	private VodChannelJsonFactory mVodChannelJsonFactory;
	
	private View progressBar;
	
	private SpecialObj special;
	
	private int size = -1;
	private int moveLen;
	
	public SpecialDetailMasterView(Context context) {
		this(context, null, 0);
	}
	
	public SpecialDetailMasterView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SpecialDetailMasterView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		
		factory = new SpecialDetailFactory();
		factory.setHttpEventHandler(new HttpEventHandler<SpecialObj>() {
			
			@Override
			public void HttpSucessHandler(SpecialObj result) {
				special=result;
				if(result.getPlatform() == EnumType.Platform.HUAWEI){
					mVodChannelFactory = new VodChannelFactory();
					mVodChannelFactory.setHttpEventHandler(liveLayoutHandler);
					mVodChannelFactory.setHttpHeaderParams(IPTVUriUtils.getHeader());
					mVodChannelFactory.DownloadDatas(result.getPlatform().toString(), result.getCategoryId(), 60, 0);
				}else if(result.getPlatform() == EnumType.Platform.ZTE){
					mVodChannelJsonFactory = new VodChannelJsonFactory();
					mVodChannelJsonFactory.setHttpEventHandler(liveLayoutZteHandler);
					mVodChannelJsonFactory.setHttpHeaderParams(IPTVUriUtils.getHeader());
					mVodChannelJsonFactory.DownloadDatas(result.getPlatform().toString(), result.getCategoryId(), 1, 60);
				}else{
					progressBar.setVisibility(View.GONE);
					initView(result);
				}
			}
			
			@Override
			public void HttpFailHandler() {
				progressBar.setVisibility(View.GONE);
				TvUtils.processHttpFail(getContext());
			}
		});
	}
	
	public void createView(int id) {
		factory.DownloadDatas(id);
	}
	
	public void createView(EnumType.Platform platform,String id) {
		factory.DownloadDatas(id);
	}
	
	private void initView(SpecialObj sObj){
		this.size = sObj.getSpecialItemObjs().size();
		backgroundImage.setImageUrl(sObj.getBgimg());
		hlistView.initView(sObj.getSpecialItemObjs(), this);
	}
	
	public void destory(){
		hlistView.destroy();
		factory.cancel();
	}
	
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		
		progressBar = findViewById(R.id.tv_progressbar);
		scrollView = (HorizontalScrollView) findViewById(R.id.special_detail_scroll);
		scrollView.setSmoothScrollingEnabled(true);
		scrollView.setFocusable(false);
		scrollView.setFocusableInTouchMode(false);
		StatusBarView sbView = (StatusBarView) findViewById(R.id.tv_status_bar);
		sbView.setSearchVisibility(View.GONE);
		backgroundImage = (AsyncImageView) findViewById(R.id.special_detail_scroll_bg);
		backgroundImage.setImageLoadedListener(this);
		hlistView = (SpecialHListView) findViewById(R.id.special_detail_hlistview);
		hlistView.setOnKeyListener(onKeyListener);
	}
	
	private OnKeyListener onKeyListener = new OnKeyListener() {
		
		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			int action = event.getAction();
			
			if (action == KeyEvent.ACTION_DOWN) {
				if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
					onScroll(KeyEvent.KEYCODE_DPAD_LEFT);
				} else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
					onScroll(KeyEvent.KEYCODE_DPAD_RIGHT);
				}
			}			
			return false;
		}
	};
	
	public boolean dispatchKeyEvent(KeyEvent event) {
		int keycode = event.getKeyCode();
		if (keycode == KeyEvent.KEYCODE_DPAD_UP) {
			return true;
		}
		return super.dispatchKeyEvent(event);
	};
		
	@Override
	public void onScroll(int direction) {
		if (moveLen > 0) {
			int selectedPosition = hlistView.getSelectedItemPosition();
			if (direction == KeyEvent.KEYCODE_DPAD_RIGHT) {
				if (selectedPosition < size - 1) {
					scrollView.smoothScrollBy(moveLen, 0);
				}
			} else {
				if (selectedPosition != 0) {
					scrollView.smoothScrollBy(-moveLen, 0);
				}
			}
		}
	}

	@Override
	public void onLoadComplete(String imageUri, View view, Bitmap loadedImage) {
		if (loadedImage.getHeight() > TvApplication.pixelHeight) {
			backgroundImage.setAdjustViewBounds(true);
		}
		float offset = loadedImage.getWidth() * TvApplication.pixelHeight / loadedImage.getHeight() - TvApplication.pixelWidth;
		if (offset < 0) {
			offset = 0;
		}
		int width = (int)(TvApplication.pixelWidth + offset);
		backgroundImage.setMinimumWidth(width);
		if (size > 0) {
			moveLen = (int)(offset / size);
		}
	}
	
	
	private HttpEventHandler<ArrayList<VodChannel>> liveLayoutHandler = new HttpEventHandler<ArrayList<VodChannel>>() {
		
		@Override
		public void HttpSucessHandler(ArrayList<VodChannel> result) {

			List<SpecialItemObj> specialItemObjs = new ArrayList<SpecialItemObj>();
			for(VodChannel vodChannel:result){
				SpecialItemObj obj = new SpecialItemObj();
				obj.setImage(vodChannel.PICPATH);
				obj.setPlatform(EnumType.Platform.HUAWEI);
				obj.setTitle(vodChannel.VODNAME);
				obj.setId(vodChannel.VODID);
				specialItemObjs.add(obj);
//				LogUtils.error("id:"+vodChannel.VODID +"  name："+vodChannel.VODNAME);
			}
			special.setSpecialItemObjs(specialItemObjs);
			progressBar.setVisibility(View.GONE);
			initView(special);
		}
		
		@Override
		public void HttpFailHandler() {
			TvUtils.processHttpFail(getContext());
		}
	};
	
private HttpEventHandler<ArrayList<VodChannel>> liveLayoutZteHandler = new HttpEventHandler<ArrayList<VodChannel>>() {
		
		@Override
		public void HttpSucessHandler(ArrayList<VodChannel> result) {

			List<SpecialItemObj> specialItemObjs = new ArrayList<SpecialItemObj>();
			for(VodChannel vodChannel:result){
				SpecialItemObj obj = new SpecialItemObj();
				obj.setImage(vodChannel.PICPATH);
				obj.setPlatform(EnumType.Platform.ZTE);
				obj.setTitle(vodChannel.VODNAME);
				obj.setId(vodChannel.VODID);
				obj.setColumnCode(vodChannel.columncode);
				obj.setContentCode(vodChannel.CONTENTCODE);
				specialItemObjs.add(obj);
//				LogUtils.error("id:"+vodChannel.VODID +"  name："+vodChannel.VODNAME);
			}
			special.setSpecialItemObjs(specialItemObjs);
			progressBar.setVisibility(View.GONE);
			initView(special);
		}
		
		@Override
		public void HttpFailHandler() {
			TvUtils.processHttpFail(getContext());
		}
	};
}
