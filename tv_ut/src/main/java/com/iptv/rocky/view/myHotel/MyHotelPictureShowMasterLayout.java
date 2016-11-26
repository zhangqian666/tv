package com.iptv.rocky.view.myHotel;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ViewSwitcher.ViewFactory;

import com.iptv.common.HttpEventHandler;
import com.iptv.common.data.MyHotelPicture;
import com.iptv.common.utils.LogUtils;
import com.iptv.common.view.AsyncImageView;
import com.iptv.rocky.R;
import com.iptv.rocky.hwdata.xml.MyHotelPictureFactory;
import com.iptv.rocky.model.TvApplication;
import com.iptv.rocky.model.hotel.MyHotelPictureListLayoutItem;
import com.nostra13.universalimageloader.core.assist.FailReason.FailType;

import java.util.ArrayList;
import java.util.List;

public class MyHotelPictureShowMasterLayout extends RelativeLayout implements ViewFactory,
        AsyncImageView.AsyncImageLoadedListener,
        AsyncImageView.AsyncImageFailListener,
        OnPageChangeListener {

    private AsyncImageView imageBottom;
    private AsyncImageView imageTopLeft;
    private AsyncImageView imageTopRight;

    private LinearLayout layoutTopLeft;
    private LinearLayout layoutTopRight;

    private boolean isViewLoaded;

    private int position;
    private List<AsyncImageView> bottomImageList;

    // 顶端图片列表
    private List<AsyncImageView> topImageList;
    private List<Integer> topAlignList;

    //数字频道切换的处理
    private Handler handlerUserChannelNumber = new Handler();

    private ArrayList<MyHotelPicture> datas = new ArrayList<MyHotelPicture>();


    private MyHotelPictureFactory mMyHotelPictureFactory;
    private Animation left_in_animation;
    private String mTypeId;
    private String mName;

    // 图片的列表
    private ViewPager mPager;
    private Context mContext;

    public MyHotelPictureShowMasterLayout(Context context) {

        this(context, null, 0);
    }

    public MyHotelPictureShowMasterLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyHotelPictureShowMasterLayout(Context context, AttributeSet attrs, int defStyle) {

        super(context, attrs, defStyle);
        this.mContext = context;

    }

    public void destroy() {
        //mPageView.destroy();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        mPager = (ViewPager) findViewById(R.id.my_hotel_picture_show_view_pager);

        position = 0;

        final Animation left = new TranslateAnimation(0, -170, 0, 0);
        left.setDuration(20000);
        left.setFillAfter(true);

        final Animation right = new TranslateAnimation(-170, 0, 0, 0);
        right.setDuration(20000);
        right.setFillAfter(true);

        left_in_animation = AnimationUtils.loadAnimation(this.getContext(), R.anim.fade_in_10);

        bottomImageList = new ArrayList<AsyncImageView>();
        topImageList = new ArrayList<AsyncImageView>();
        topAlignList = new ArrayList<Integer>();

        //layoutTopLeft = (LinearLayout) findViewById(R.id.topLeft);
        //layoutTopRight =(LinearLayout) findViewById(R.id.topRight);
        //imageBottom = (AsyncImageView) findViewById(R.id.imageBottom);
        //imageBottom.startAnimation(left);

		 /*left.setAnimationListener(new AnimationListener() {
				@Override
				public void onAnimationStart(Animation animation) {
				}
				@Override
				public void onAnimationRepeat(Animation animation) {
				}
				@Override
				public void onAnimationEnd(Animation animation) {
					imageBottom.startAnimation(right);
				}
			});
	        
	        right.setAnimationListener(new AnimationListener() {
				@Override
				public void onAnimationStart(Animation animation) {
				}
				@Override
				public void onAnimationRepeat(Animation animation) {
				}
				@Override
				public void onAnimationEnd(Animation animation) {
					imageBottom.startAnimation(left);
				}
			});*/


        //imageTopLeft = (AsyncImageView) findViewById(R.id.imageTopLeft);
        //imageTopRight = (AsyncImageView) findViewById(R.id.imageTopRight);
        //imageSwitcher = (ImageSwitcher) findViewById(R.id.imageSwitcherBottom);

        //topImageSwither = (ImageSwitcher) findViewById(R.id.imageSwitcherTop);

        //imageInfo = (RelativeLayout) findViewById(R.id.picture_title);

        //imageDescribe = (TextView) findViewById(R.id.image_describe);
        //mViewPager =(ViewPager) findViewById(R.id.viewPager);
		
/*		fragment1 = new Fragment();
		fragment2 = new Fragment();
		fragment3 = new Fragment();
		
		fragmentList = new ArrayList<Fragment>();
		fragmentList.add(fragment1);
		fragmentList.add(fragment2);
		fragmentList.add(fragment3);*/

        //mViewPager.setAdapter(new MyViewPagerAdapter(getSupportFragmentManager()));

        //mTitleView = (TitleView) findViewById(R.id.my_hotel_picture_list_title);
        //mPageView = (MyHotelPictureShowViewPager) findViewById(R.id.my_hotel_picture_show_viewpager);
        //mPageView.setOnPageChangeListener(this);
        //mViewPager.setAdapter(new MyViewPagerAdapter());
        //mViewPager.setOnPageChangeListener(this);
    }

    public void createView(String id, String type) {
        mTypeId = id;
        mName = type;
        //mTitleView.setText(name);
        mMyHotelPictureFactory.DownloadDatas(TvApplication.account, TvApplication.language, id, type);
    }

	/*@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}
	
	@Override
	public void onPageSelected(int arg0) {
	}*/

    //@Override
/*	protected boolean onRequestFocusInDescendants(int direction,
			Rect previouslyFocusedRect) {
		return false;//mPageView.requestFocus(direction, previouslyFocusedRect);
	}*/


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        LogUtils.info("keyCode:" + keyCode);
        if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) { //向后退
            LogUtils.error("向左 Position:" + position + bottomImageList.size());
			
			/*position--;
			if (position < 0){
				position = bottomImageList.size() - 1;
			}
			imageBottom.setImageDrawable(bottomImageList.get(position).getDrawable());*/
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) { //向前进
            LogUtils.error("向右 Position:" + position + bottomImageList.size());
			
			/*position++;
			if (position >= bottomImageList.size()){
				position = 0;
			}
			imageBottom.setImageDrawable(bottomImageList.get(position).getDrawable());*/
        }
        return super.onKeyDown(keyCode, event);
    }


    private HttpEventHandler<ArrayList<MyHotelPicture>> liveLayoutHandler = new HttpEventHandler<ArrayList<MyHotelPicture>>() {

        @Override
        public void HttpSucessHandler(ArrayList<MyHotelPicture> result) {
            MyHotelPictureListLayoutItem item = new MyHotelPictureListLayoutItem();
            item.id = mTypeId;
            item.title = mName;
            ArrayList<MyHotelPictureListLayoutItem> items = new ArrayList<MyHotelPictureListLayoutItem>();
            items.add(item);

            if (result != null) {

                for (MyHotelPicture info : result) {
                    if (info != null) {
                        datas.add(info);
                        AsyncImageView im = new AsyncImageView(MyHotelPictureShowMasterLayout.this.getContext());
                        im.setImageUrl(info.bgimg);
                        bottomImageList.add(im);

                        AsyncImageView imTop = new AsyncImageView(MyHotelPictureShowMasterLayout.this.getContext());

                        imTop.setImageUrl(info.topimg);
                        LogUtils.error("下发的对齐方式：" + info.frontImagePosition);
                        if (info.frontImagePosition.equals("LEFT")) {
                            //layoutTop.setGravity(START_OF);
                            topAlignList.add(START_OF);
                        } else if (info.frontImagePosition.equals("RIGHT")) {
                            //layoutTop.setGravity(END_OF);
                            topAlignList.add(END_OF);
                        } else if (info.frontImagePosition.equals("TOP")) {
                            //layoutTop.setGravity(START_OF);
                            topAlignList.add(START_OF);
                        } else if (info.frontImagePosition.equals("DOWN")) {
                            //layoutTop.setGravity(END_OF);
                            topAlignList.add(END_OF);
                        }
                        topImageList.add(imTop);


                        //设置Adapter
                        LogUtils.info("mPager 是否空:" + (mPager == null));

//							mPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentList));  
                        //设置监听，主要是设置点点的背景
                        mPager.setOnPageChangeListener(MyHotelPictureShowMasterLayout.this);
                        //设置ViewPager的默认项, 设置为长度的100倍，这样子开始就能往左滑动
                        mPager.setCurrentItem((bottomImageList.size()) * 100);

                    }
                }
            }
        }


        @Override
        public void HttpFailHandler() {
            //TvUtils.processHttpFail(getContext());
        }
    };


    @Override
    public View makeView() {
        AsyncImageView im = new AsyncImageView(MyHotelPictureShowMasterLayout.this.getContext());
        //im.setImageDrawable(imageList.get(position).getDrawable());
        return im;
    }


    //
    Runnable runnableChannelLeftList = new Runnable() {
        @Override
        public void run() {
            if (datas.size() > 0) {
                //imageDescribe.setText(datas.get(position).title);
                LogUtils.debug("底图数组大小:" + bottomImageList.size() + "  position:" + position + "顶图数组大小：" + topImageList.size());
                if (bottomImageList.size() > 0) {

                    imageBottom.setImageDrawable(bottomImageList.get(position).getDrawable());

                    int topAlign = topAlignList.get(position);

                    if (topAlign == START_OF) {
                        layoutTopRight.setVisibility(INVISIBLE);
                        layoutTopLeft.setVisibility(VISIBLE);
                        imageTopLeft.setImageDrawable(topImageList.get(position).getDrawable());
                        Animation left_in_animation = AnimationUtils.loadAnimation(MyHotelPictureShowMasterLayout.this.mContext, R.anim.left_in);
                        imageTopLeft.startAnimation(left_in_animation);//使用view的startAnimation方法开始执行动画
                    } else if (topAlign == END_OF) {
                        layoutTopLeft.setVisibility(INVISIBLE);
                        layoutTopRight.setVisibility(VISIBLE);
                        imageTopRight.setImageDrawable(topImageList.get(position).getDrawable());
                        Animation right_in_animation = AnimationUtils.loadAnimation(MyHotelPictureShowMasterLayout.this.mContext, R.anim.right_in);
                        imageTopRight.startAnimation(right_in_animation);//使用view的startAnimation方法开始执行动画
                    }
                }
            }
        }
    };

    public void displayPictureTitle() {
        //imageDescribe.setText(datas.get(position).title);
        //imageDescribe.setVisibility(View.VISIBLE);
    }

    public void hidePictureTitle() {
        //imageDescribe.setVisibility(View.GONE);
    }

    @Override
    public void onLoadFailed(String imageUri, View view, FailType failType) {
        LogUtils.debug("图片异步加载：加载的URL：" + imageUri);


        isViewLoaded = true;
    }

    @Override
    public void onLoadComplete(String imageUri, View view, Bitmap loadedImage) {
        LogUtils.debug("图片异步加载：加载的URL：" + imageUri + "  ; 加地址对比，便于将图片设置为指定的");

        if (imageUri != null) {
            if (imageUri.equals(bottomImageList.get(position))) {
                //imageBottom.setImageDrawable(bottomImageList.get(position).getDrawable());
                imageBottom.setImageBitmap(loadedImage);
            }
        }
        isViewLoaded = true;
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


    public class PictureAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            LogUtils.debug("bottomImageList.size():" + bottomImageList.size());
            return bottomImageList.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            // TODO Auto-generated method stub
//			LogUtils.d
            return arg0 == arg1;
        }

        @Override
        public Object instantiateItem(View container, int position) {
            ((ViewPager) container).addView(bottomImageList.get(position % bottomImageList.size()));
            return bottomImageList.get(position % bottomImageList.size());
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // TODO Auto-generated method stub
//			super.destroyItem(container, position, object);
            ((ViewPager) container).removeView(bottomImageList.get(position));
        }

    }


}
