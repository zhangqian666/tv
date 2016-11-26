package com.iptv.rocky;

import java.util.ArrayList;
import java.util.List;

import com.iptv.common.HttpEventHandler;
import com.iptv.common.data.MyHotelPicture;
import com.iptv.common.utils.Constants;
import com.iptv.common.utils.LogUtils;
import com.iptv.common.view.AsyncImageView;
import com.iptv.rocky.hwdata.IPTVUriUtils;
import com.iptv.rocky.hwdata.xml.MyHotelPictureFactory;
import com.iptv.rocky.model.TvApplication;
import com.iptv.rocky.model.hotel.MyHotelPictureListLayoutItem;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;


public class MyHotelShowPicturesActivity extends FragmentActivity{

	private String id;
	private String name;
	
	//private MyHotelPictureShowMasterLayout mMasterLayout;
	private List<AsyncImageView> bottomImageList;
	
	// 顶端图片列表
	private List<AsyncImageView> topImageList;
	private List<Integer> topAlignList;
	private ViewPager mPager; 
    private ArrayList<Fragment> fragmentList;  
	
	private MyHotelPictureFactory mMyHotelPictureFactory;
	

//	private ScreenShot mScreenShot;
	
	
	 // 图片数组  
	//private int[] arrayPictures = { R.drawable.business_centre_boardroom, R.drawable.business_centre,  R.drawable.kunlun_ballroom_chinese_wedding, R.drawable.kunlun_ballroom_classroom };  
	// 要显示的图片在图片数组中的Index  
	private int pictureIndex; 
	
	//private Integer [] selectId = new Integer[]{ R.drawable.business_centre_boardroom, R.drawable.business_centre,  R.drawable.kunlun_ballroom_chinese_wedding, R.drawable.kunlun_ballroom_classroom};  

    private int selectedTag = 0;  
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_hotel_show_pictures);
	
//		mMasterLayout = (MyHotelPictureShowMasterLayout)findViewById(R.id.my_hotel_picture_show_layout);
		mPager = (ViewPager) findViewById(R.id.my_hotel_picture_show_view_pager);
		Intent intent = getIntent();
		if (intent != null) {
			LogUtils.info("Constants.cListIdExtra:"+intent.getStringExtra(Constants.cListIdExtra));
			id = intent.getStringExtra(Constants.cListIdExtra);
			name = intent.getStringExtra(Constants.cListNameExtra);
			//mMasterLayout.createView(id, name);
		}
		bottomImageList = new ArrayList<AsyncImageView>();
		topImageList = new ArrayList<AsyncImageView>();
		topAlignList= new ArrayList<Integer>();
		mMyHotelPictureFactory = new MyHotelPictureFactory();
		mMyHotelPictureFactory.setHttpEventHandler(liveLayoutHandler);
		mMyHotelPictureFactory.setHttpHeaderParams(IPTVUriUtils.getHeader());
		mMyHotelPictureFactory.DownloadDatas(TvApplication.account,TvApplication.language,id,name);
	}

/*	public class ImageAdapter extends BaseAdapter  
	{  
	       private Context context;  
	       int galleryItemBackground;  
	       public ImageAdapter (Context c)  
	       {  
	            context = c;  
	            TypedArray typeArray = obtainStyledAttributes(R.styleable.Gallery);  
	            galleryItemBackground = typeArray.getResourceId(R.styleable.Gallery_android_galleryItemBackground, 0);  
	            typeArray.recycle();  
	        }  
	        @Override  
	        public int getCount()   
	        {  
	            //返回selectId[]的长度  
	            return selectId.length;  
       }  
	  
	       @Override  
	        public Object getItem(int position)  
	        {  
	            return position;  
	       }  
	  
	        @Override  
	        public long getItemId(int position)   
	        {  
	           return position;  
	        }  
	 
	       @Override  
	        public View getView(int position, View convertView, ViewGroup parent)   
	       {  
	           ImageView imageView = new ImageView(context);  
	          //设置资源图片  
	          imageView.setImageResource(selectId[position]);  
	          imageView.setAdjustViewBounds(true);  //允许调整边框  
	          //设定底部画廊，自适应大小  
	          imageView.setLayoutParams(new Gallery.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));  
	          //设置画廊背景  
	          imageView.setBackgroundResource(galleryItemBackground);  
	          return imageView;  
	      }  
	}  

*/

	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		View v = getCurrentFocus();
		switch (keyCode) {
		case KeyEvent.KEYCODE_0:
			
			break;
		case KeyEvent.KEYCODE_1:
			
			break;
		case KeyEvent.KEYCODE_2:
			
			break;
		case KeyEvent.KEYCODE_3:
			
			break;
		case KeyEvent.KEYCODE_4:
			
			break;
		case KeyEvent.KEYCODE_5:
			
			break;
		case KeyEvent.KEYCODE_6:
			
			break;
		case KeyEvent.KEYCODE_7:
			
			break;
		case KeyEvent.KEYCODE_8:
			
			break;	
		case KeyEvent.KEYCODE_9:
			
			break;		
		
		case KeyEvent.KEYCODE_ENTER:
			
			break;
		case KeyEvent.KEYCODE_BACK:
			
			
			break;
		case KeyEvent.KEYCODE_HOME:
			break;
		case 17:
			break;
		case KeyEvent.KEYCODE_DPAD_UP:
			///this.mMasterLayout.displayPictureTitle();
			break;
		case KeyEvent.KEYCODE_DPAD_DOWN:
			//this.mMasterLayout.hidePictureTitle();
			break;
		case KeyEvent.KEYCODE_DPAD_LEFT:
			
//			this.mMasterLayout.preImage();
			break;
		case KeyEvent.KEYCODE_DPAD_RIGHT:
//			this.mMasterLayout.nextImage();
			break;
		case KeyEvent.KEYCODE_DPAD_CENTER:
			
			 //gallery.setVisibility(View.VISIBLE);  
			

		
			//Animation left_in_animation = AnimationUtils.loadAnimation(this, R.anim.fade_in_10);
			//Animation left_out_animation = AnimationUtils.loadAnimation(this, R.anim.fade_out_10);
			//this.mImageSwicher.setInAnimation(left_in_animation);
			//this.mImageSwicher.setInAnimation(left_out_animation);
		
			
			break;
		case KeyEvent.KEYCODE_VOLUME_UP:
			/*mScreenShot = new ScreenShot();
			Bitmap bmp = mScreenShot.myShot(this);
			try {
				mScreenShot.saveToSD(bmp, "/mnt/sdcard/pictures/", "myhotel.png");
			} catch (IOException e) {
				e.printStackTrace();
			}*/
			break;	
		case 82:
			break;
		case 253:
			break;
		case 303:
    		break;
		case 328: 	// channel +
			
			break;
		case 331:	// channel -
			
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}


	private HttpEventHandler<ArrayList<MyHotelPicture>> liveLayoutHandler = new HttpEventHandler<ArrayList<MyHotelPicture>>() {
		
		@Override
		public void HttpSucessHandler(ArrayList<MyHotelPicture> result) {
			MyHotelPictureListLayoutItem item = new MyHotelPictureListLayoutItem();
			//item.id = mTypeId;
			//item.title = mName;
			ArrayList<MyHotelPictureListLayoutItem> items = new ArrayList<MyHotelPictureListLayoutItem>();
			items.add(item);
			
			if(result != null){
				fragmentList = new ArrayList<Fragment>();
				fragmentList.clear();
				LogUtils.error("size================="+result.size());
				int i=0;
				for(MyHotelPicture info: result){
						if(info != null){
							//LogUtils.error( "背景图片路径:"+info.bgimg+"; 前脸图片:"+info.topimg);
							//datas.add(info);
							AsyncImageView im = new AsyncImageView(MyHotelShowPicturesActivity.this);
							im.setImageUrl(info.bgimg);
							bottomImageList.add(im);
							
							AsyncImageView imTop = new AsyncImageView(MyHotelShowPicturesActivity.this);
							
							imTop.setImageUrl(info.topimg);
							//LogUtils.error("下发的对齐方式："+info.frontImagePosition);
							if(info.frontImagePosition.equals("LEFT")){
								//layoutTop.setGravity(START_OF);
								topAlignList.add(RelativeLayout.START_OF);
							}else if(info.frontImagePosition.equals("RIGHT")){
								//layoutTop.setGravity(END_OF);
								topAlignList.add(RelativeLayout.END_OF);
							}else if(info.frontImagePosition.equals("TOP")){
								//layoutTop.setGravity(START_OF);
								topAlignList.add(RelativeLayout.START_OF);
							}else if(info.frontImagePosition.equals("DOWN")){
								//layoutTop.setGravity(END_OF);
								topAlignList.add(RelativeLayout.END_OF);
							}
							topImageList.add(imTop);
							
							Fragment fragmentTest= new FragmentTest(info,result.size(),i);
							fragmentList.add(fragmentTest);
							
						}
						i++;
					} 
			        
			        //给ViewPager设置适配器  
			        mPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentList));  
			        mPager.setCurrentItem(0);//设置当前显示标签页为第一页  
			        mPager.setOnPageChangeListener(new MyOnPageChangeListener());//页面变化时的监听器  				
				}
		}

		

		@Override
		public void HttpFailHandler() {
			//TvUtils.processHttpFail(getContext());
		}
	};

	
	public class MyFragmentPagerAdapter extends FragmentPagerAdapter{
		private ArrayList<Fragment> list;
		public MyFragmentPagerAdapter(FragmentManager fm, ArrayList<Fragment> list) {
			super(fm);
			this.list = list;
		}

		@Override
		public Fragment getItem(int arg0) {
			// TODO Auto-generated method stub
			return list.get(arg0);
		}

		@Override
		public int getCount() {
			
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
        	
        	((FragmentTest)fragmentList.get(arg0)).doAnimation();
        }  
    }  
}
