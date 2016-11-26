package com.iptv.rocky;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.RelativeLayout;
import android.widget.ViewSwitcher.ViewFactory;

import com.iptv.common.HttpEventHandler;
import com.iptv.common.data.MyHotelPicture;
import com.iptv.common.data.ScreenProtect;
import com.iptv.common.data.ScreenProtectImageInfo;
import com.iptv.common.utils.LogUtils;
import com.iptv.common.view.AsyncImageView;
import com.iptv.rocky.hwdata.json.ScreenProtectFactory;
import com.iptv.rocky.model.ActivityStack;
import com.iptv.rocky.model.TvApplication;
import com.iptv.rocky.utils.BackgroundMusic;
import com.nostra13.universalimageloader.core.assist.FailReason.FailType;

import java.util.ArrayList;
import java.util.List;

/**
 * 待机屏保部分，此部分是图片的
 * 
 *
 */
public class ScreenProtectActivity extends FragmentActivity implements ViewFactory, AsyncImageView.AsyncImageLoadedListener, AsyncImageView.AsyncImageFailListener,OnPageChangeListener{

	private int position;
	private List<AsyncImageView> bottomImageList;		// 底部图片列表
	private List<AsyncImageView> topImageList;			// 顶端图片列表
	private List<Integer> topAlignList;					// 对齐方式
	private List<ScreenProtectImageInfo> infoList; 
	
	//数字频道切换的处理
	private Handler handlerChangeImage = new Handler();
	private ArrayList<MyHotelPicture> datas = new ArrayList<MyHotelPicture>();
	private ScreenProtectFactory screenProtectFactory;

	private Animation left_in_animation;
	
	// 图片的列表
	private ViewPager mPager;
	private int currentItem = 0;
    private ArrayList<FragmentScreenProtectImageInfo> fragmentList;
    private String preStatus;
    
	@Override
	protected void onDestroy() {
		//if(handlerChangeImage.hasCallbacks(runnableChangePage)){
			handlerChangeImage.removeCallbacks(runnableChangePage);
		//}
		TvApplication.status = preStatus;
		
		//quiteBackgroundMusic();
		
		super.onDestroy();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		ActivityStack.pushActivity(this);
		setContentView(R.layout.activity_screen_protect);
		mPager = (ViewPager) this.findViewById(R.id.screen_protect_view_pager);
		
		bottomImageList = new ArrayList<AsyncImageView>();
		topImageList = new ArrayList<AsyncImageView>();
		topAlignList= new ArrayList<Integer>();
		
		screenProtectFactory = new ScreenProtectFactory();
		screenProtectFactory.setHttpEventHandler(screenProtectHandler);
		screenProtectFactory.DownloadDatas();
		
		preStatus = TvApplication.status;
		TvApplication.status = "SCREENPROTECT";
		
		//add 播放背景音乐
		//BackgroundMusic.getInstance(this).playBackgroundMusic("music/background_ex.mp3", true);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		LogUtils.info("keyCode:"+keyCode);
		
		switch(keyCode) {
			case KeyEvent.KEYCODE_0:
				//if(handlerChangeImage.hasCallbacks(runnableChangePage)){
					handlerChangeImage.removeCallbacks(runnableChangePage);
				//}
				onBackPressed();
				break;
			case KeyEvent.KEYCODE_1:
				//if(handlerChangeImage.hasCallbacks(runnableChangePage)){
					handlerChangeImage.removeCallbacks(runnableChangePage);
				//}
				onBackPressed();
				break;
			case KeyEvent.KEYCODE_2:
				//if(handlerChangeImage.hasCallbacks(runnableChangePage)){
					handlerChangeImage.removeCallbacks(runnableChangePage);
				//}
				onBackPressed();
				break;
			case KeyEvent.KEYCODE_3:
				//if(handlerChangeImage.hasCallbacks(runnableChangePage)){
					handlerChangeImage.removeCallbacks(runnableChangePage);
				//}
				onBackPressed();
				break;
			case KeyEvent.KEYCODE_4:
				//if(handlerChangeImage.hasCallbacks(runnableChangePage)){
					handlerChangeImage.removeCallbacks(runnableChangePage);
				//}
				onBackPressed();
				break;
			case KeyEvent.KEYCODE_5:
				//if(handlerChangeImage.hasCallbacks(runnableChangePage)){
					handlerChangeImage.removeCallbacks(runnableChangePage);
				//}
				onBackPressed();
				break;
			case KeyEvent.KEYCODE_6:
				//if(handlerChangeImage.hasCallbacks(runnableChangePage)){
					handlerChangeImage.removeCallbacks(runnableChangePage);
				//}
				onBackPressed();
				break;
			case KeyEvent.KEYCODE_7:
				//if(handlerChangeImage.hasCallbacks(runnableChangePage)){
					handlerChangeImage.removeCallbacks(runnableChangePage);
				//}
				onBackPressed();
				break;
			case KeyEvent.KEYCODE_8:
				//if(handlerChangeImage.hasCallbacks(runnableChangePage)){
					handlerChangeImage.removeCallbacks(runnableChangePage);
				//}
				onBackPressed();
				break;	
			case KeyEvent.KEYCODE_9:
				//if(handlerChangeImage.hasCallbacks(runnableChangePage)){
					handlerChangeImage.removeCallbacks(runnableChangePage);
				//}
				onBackPressed();
			break;		
			case KeyEvent.KEYCODE_DPAD_UP:
				//if(handlerChangeImage.hasCallbacks(runnableChangePage)){
					handlerChangeImage.removeCallbacks(runnableChangePage);
				//}
				onBackPressed();
				return true;
				//break;
			case KeyEvent.KEYCODE_DPAD_DOWN:
				//if(handlerChangeImage.hasCallbacks(runnableChangePage)){
					handlerChangeImage.removeCallbacks(runnableChangePage);
				//}
				onBackPressed();
				return true;
				//break;
			case KeyEvent.KEYCODE_DPAD_LEFT:
				if(bottomImageList.size()>1){
					if(currentItem >0){
						currentItem =currentItem-1;
					}else{
						currentItem =bottomImageList.size()-1;
					}
					mPager.setCurrentItem(currentItem);
					//if(handlerChangeImage.hasCallbacks(runnableChangePage)){
						handlerChangeImage.removeCallbacks(runnableChangePage);
					//}
					handlerChangeImage.postDelayed(runnableChangePage, infoList.get(currentItem).duration);
				}
				break;
			case KeyEvent.KEYCODE_DPAD_RIGHT:
				LogUtils.error("向右 Position:" + position +";"+bottomImageList.size());
				if(bottomImageList.size()>1){
					if(currentItem <bottomImageList.size()-1){
						currentItem +=1;
					}else{
						currentItem =0;
					}
					mPager.setCurrentItem(currentItem);
					//if(handlerChangeImage.hasCallbacks(runnableChangePage)){
						handlerChangeImage.removeCallbacks(runnableChangePage);
					//}
					handlerChangeImage.postDelayed(runnableChangePage, infoList.get(currentItem).duration);
				}
			case KeyEvent.KEYCODE_DPAD_CENTER:
				//if(handlerChangeImage.hasCallbacks(runnableChangePage)){
					handlerChangeImage.removeCallbacks(runnableChangePage);
				//}
				onBackPressed();
				return true;
				
			case KeyEvent.KEYCODE_BACK:	
				//if(handlerChangeImage.hasCallbacks(runnableChangePage)){
					handlerChangeImage.removeCallbacks(runnableChangePage);
				//}
				break;
			case KeyEvent.KEYCODE_PAGE_UP:
				//if(handlerChangeImage.hasCallbacks(runnableChangePage)){
					handlerChangeImage.removeCallbacks(runnableChangePage);
				//}
				onBackPressed();
				break;
			case KeyEvent.KEYCODE_PAGE_DOWN:
				//if(handlerChangeImage.hasCallbacks(runnableChangePage)){
					handlerChangeImage.removeCallbacks(runnableChangePage);
				//}
				onBackPressed();
				break;	
			default:
				//if(handlerChangeImage.hasCallbacks(runnableChangePage)){
					handlerChangeImage.removeCallbacks(runnableChangePage);
				//}
				onBackPressed();
				break;
		}	
		return super.onKeyDown(keyCode, event);
	}
	

	private void quiteBackgroundMusic() {
		if (BackgroundMusic.getInstance(this).isBackgroundMusicPlaying()) {
			BackgroundMusic.getInstance(this).stopBackgroundMusic();
		}
	}
	
	public void displayPictureTitle(){
	}
	
	public void hidePictureTitle(){
	}

	@Override
	public void onLoadFailed(String imageUri, View view, FailType failType) {
		LogUtils.debug("图片异步加载：加载的URL："+imageUri);
	}

	@Override
	public void onLoadComplete(String imageUri, View view, Bitmap loadedImage) {
		LogUtils.debug("图片异步加载：加载的URL："+imageUri+"  ; 加地址对比，便于将图片设置为指定的");
		
		if(imageUri != null){
			if(imageUri.equals(bottomImageList.get(position))){
				//imageBottom.setImageDrawable(bottomImageList.get(position).getDrawable());
				
			}
		}
		
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub
		
	}
	
	
	public class PictureAdapter extends PagerAdapter{

		@Override
		public int getCount() {
			LogUtils.debug("bottomImageList.size():"+bottomImageList.size());
			return bottomImageList.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
		
		@Override
		 public Object instantiateItem(View container, int position) {  
	        ((ViewPager)container).addView(bottomImageList.get(position % bottomImageList.size()));  
	        return bottomImageList.get(position % bottomImageList.size());  
	    }
		
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// super.destroyItem(container, position, object);
			((ViewPager) container).removeView(bottomImageList.get(position));
		}

	}


	@Override
	public View makeView() {
		return null;
	}
	
	/**
	 * 接收屏保数据
	 */
	private HttpEventHandler<ScreenProtect> screenProtectHandler = new HttpEventHandler<ScreenProtect>() {

		@Override
		public void HttpSucessHandler(ScreenProtect result) {
			fragmentList = new ArrayList<FragmentScreenProtectImageInfo>();
			fragmentList.clear();
		
			int i=0;
			infoList = new ArrayList<ScreenProtectImageInfo>();
			for(ScreenProtectImageInfo info: result.getImages()){
				if(info != null){
					AsyncImageView im = new AsyncImageView(ScreenProtectActivity.this);
					im.setImageUrl(info.bgimg);
					bottomImageList.add(im);
					
					AsyncImageView imTop = new AsyncImageView(ScreenProtectActivity.this);
					
					imTop.setImageUrl(info.topimg);
					LogUtils.error("下发的对齐方式："+info.frontImagePosition);
					if(info.frontImagePosition.equals("LEFT")){
						topAlignList.add(RelativeLayout.START_OF);
					}else if(info.frontImagePosition.equals("RIGHT")){
						topAlignList.add(RelativeLayout.END_OF);
					}else if(info.frontImagePosition.equals("TOP")){
						topAlignList.add(RelativeLayout.START_OF);
					}else if(info.frontImagePosition.equals("DOWN")){
						topAlignList.add(RelativeLayout.END_OF);
					}
					topImageList.add(imTop);
					
					infoList.add(info);
					FragmentScreenProtectImageInfo fragmentTest= new FragmentScreenProtectImageInfo(info,result.getImages().size(),i);
					fragmentList.add(fragmentTest);
				}
				i++;
			} 
	        //给ViewPager设置适配器  
			mPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentList));  
	        mPager.setCurrentItem(0);										//设置当前显示标签页为第一页  
	        mPager.setOnPageChangeListener(new MyOnPageChangeListener());	//页面变化时的监听器
	        //if(handlerChangeImage.hasCallbacks(runnableChangePage)){
	        	handlerChangeImage.removeCallbacks(runnableChangePage);
	        //}
	        if(infoList.size()>0){
	        	handlerChangeImage.postDelayed(runnableChangePage, infoList.get(0).duration*1000);
	        }
		}

		@Override
		public void HttpFailHandler() {
			LogUtils.error("下载屏保数据错误");
		}
	};
	
	public class MyFragmentPagerAdapter extends FragmentPagerAdapter{
		private ArrayList<FragmentScreenProtectImageInfo> list;
		public MyFragmentPagerAdapter(FragmentManager fm, ArrayList<FragmentScreenProtectImageInfo> list) {
			super(fm);
			this.list = list;
		}

		@Override
		public FragmentScreenProtectImageInfo getItem(int arg0) {
			return list.get(arg0);
		}

		@Override
		public int getCount(){
			return list.size();
		}
	}
	
	public class MyOnPageChangeListener implements OnPageChangeListener{  
        
        @Override  
        public void onPageScrolled(int arg0, float arg1, int arg2) {  
            // TODO Auto-generated method stub  
        }  
          
        @Override  
        public void onPageScrollStateChanged(int arg0) {  
            // TODO Auto-generated method stub  

        }  
          
        @Override  
        public void onPageSelected(int arg0) {  
        	((FragmentScreenProtectImageInfo)fragmentList.get(arg0)).doAnimation();
        }  
    }  
	
	
	private Runnable runnableChangePage = new Runnable() {
		@Override
		public void run() {
			if(bottomImageList.size()>0){
				if(bottomImageList.size()>1){
					if(currentItem <(bottomImageList.size()-1)){
						currentItem +=1;
					}else{
						currentItem =0;
					}
					mPager.setCurrentItem(currentItem);
					handlerChangeImage.removeCallbacks(runnableChangePage);
					if(infoList.get(currentItem).duration == 0){
						handlerChangeImage.postDelayed(runnableChangePage, 10000);
					}else{
						handlerChangeImage.postDelayed(runnableChangePage, infoList.get(currentItem).duration*1000);
					}
				}
			}else{
				
			}
		}
	};
	
	
	@Override
    public void onBackPressed() {
		ActivityStack.popCurrent();
    }
	
}
