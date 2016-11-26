package com.iptv.common.view;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

public class AsyncImageView extends ImageView {

	private AsyncImageLoadedListener mLoadedListener;
	private AsyncImageFailListener mFailedListener;
	
	private static DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder()
//		.showImageOnLoading(R.drawable.ic_stub)
//		.showImageForEmptyUri(R.drawable.ic_empty)
//		.showImageOnFail(R.drawable.ic_error)
		.cacheInMemory(true)
		.cacheOnDisk(true)
		.considerExifParams(true)
		.bitmapConfig(Bitmap.Config.RGB_565);

	public AsyncImageView(Context context) {
		super(context);
	}

	public AsyncImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public AsyncImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	public void setImageUrl(String url) {
		setImageUrl(url, 0);
	}
	
	public void setImageUrl(String imageUri, int defaultImage) {	
        if (defaultImage <= 0) {
        	setImageBitmap(null);
        }
        
        if (TextUtils.isEmpty(imageUri)) {
        	setImageBitmap(null);
        	if (mFailedListener != null) {
				mFailedListener.onLoadFailed(imageUri, this, FailReason.FailType.UNKNOWN);
			}
        	return;
        }
        
    	builder
        .showImageOnLoading(defaultImage)
        .showImageForEmptyUri(defaultImage)
        .showImageOnFail(defaultImage)
        .imageScaleType(ImageScaleType.EXACTLY) ;
		ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader.displayImage(imageUri, this, builder.build(), listener);
	}
	
	private ImageLoadingListener listener = new ImageLoadingListener() {
		
		@Override
		public void onLoadingStarted(String imageUri, View view) {
			
		}
		
		@Override
		public void onLoadingFailed(String imageUri, View view,
				FailReason failReason) {
			if (mFailedListener != null) {
				mFailedListener.onLoadFailed(imageUri, view, failReason != null ? failReason.getType() : FailReason.FailType.UNKNOWN);
			}
		}
		
		@Override
		public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
			if (mLoadedListener != null) {
				mLoadedListener.onLoadComplete(imageUri, view, loadedImage);
			}
		}
		
		@Override
		public void onLoadingCancelled(String imageUri, View view) {
			
		}
	};
	
	public void setImageLoadedListener(AsyncImageLoadedListener listener) {
		mLoadedListener = listener;
	}
	
	public void setImageFailedListener(AsyncImageFailListener listener) {
		mFailedListener = listener;
	} 
	
	public interface AsyncImageLoadedListener {
		public void onLoadComplete(String imageUri, View view, Bitmap loadedImage);
	}
	
	public interface AsyncImageFailListener {
		public void onLoadFailed(String imageUri, View view, FailReason.FailType failType);
	}
}
