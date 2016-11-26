package com.iptv.common.view;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Please use AsyncImageView
 *
 */
@Deprecated
public class AssertImageView extends ImageView {

	private Context mContext;
	
	public AssertImageView(Context context) {
		this(context, null, 0);
	}
	
	public AssertImageView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public AssertImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		this.mContext = context;
	}
	
	public void setImageUrl(String url) {
		Bitmap bitmap = null;
		try {
			bitmap = getImageFromAssetsFile(url);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		if (bitmap != null) {
			super.setImageBitmap(bitmap);
		}
	}
	
	private Bitmap getImageFromAssetsFile(String url) throws IOException {
		InputStream stream = mContext.getResources().getAssets().open(url);
		try {
			return BitmapFactory.decodeStream(stream);
		} finally {
			stream.close();
		}
    }

}
