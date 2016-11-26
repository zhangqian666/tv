package com.iptv.rocky.view.dashboard;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.iptv.common.data.DashboardPicture;
import com.iptv.common.utils.LogUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;


/**
 * 公告信息的具体显示页，本页为图片类的
 *
 */
public class DashboardPictureView extends RelativeLayout implements ImageLoadingListener{

	private ImageView image;
    private ImageLoader bgImageLoader;
	public DashboardPictureView(Context context) {
		super(context);
	}

	public void createView(DashboardPicture pictureData) {
		
        bgImageLoader = ImageLoader.getInstance();
		bgImageLoader.loadImage(pictureData.getBackgroundImage(), DashboardPictureView.this);
		
		
		
/*		 AsyncImageView imageView=new AsyncImageView(getContext());
         
         imageView.setImageUrl(infos.get(position).getBackgroundImage());
         
         //imageView.setImageBitmap(YiImageUtil.loadImageFromUrl(infos.get(position).getBackgroundImage()));
         //设置ImageView的缩放类型
         imageView.setScaleType(ImageView.ScaleType.FIT_XY);
         //为ImageView设置布局参数
         imageView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));*/
		
	}
	
	
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
	}	

	@Override
	public void onLoadingCancelled(String arg0, View arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
		LogUtils.error("图片下载完毕");
		this.setBackground(new BitmapDrawable(this.getResources(),arg2));
		
		/*image = new ImageView(getContext());
        //设置ImageView的缩放类型
		image.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        //为ImageView设置布局参数
		image.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
		image.setImageBitmap(arg2);*/
	}

	@Override
	public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLoadingStarted(String arg0, View arg1) {
		// TODO Auto-generated method stub
		
	}
}
