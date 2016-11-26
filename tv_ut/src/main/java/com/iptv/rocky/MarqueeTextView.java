package com.iptv.rocky;

import com.iptv.common.utils.LogUtils;
import com.iptv.rocky.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

public class MarqueeTextView extends TextView{
	private String content;
	private float content_long;
	private int speed;
	private Paint mpaint = new Paint();
	private float mTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 20, getResources().getDisplayMetrics());
	private float cur_x = 0;
	private Handler handler;
	public MarqueeTextView(Context context) {
		this(context, null);
	}

	public MarqueeTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MarqueeTextView);
		content = ta.getString(R.styleable.MarqueeTextView_content);
		speed = ta.getInt(R.styleable.MarqueeTextView_marquee_speed, 20);
		ta.recycle();
		
		mpaint.setAntiAlias(true);
		mpaint.setDither(true);
		LogUtils.error("文字尺寸："+mTextSize);
		mpaint.setTextSize(mTextSize);
		mpaint.setColor(getResources().getColor(R.color.white));
		content_long = mpaint.measureText(content);
		cur_x = content_long+getWidth();
		handler = new Handler();
		handler.postDelayed(showRunnable, 10);
	}
	
	public void setScrollText(String text){
		this.content = text;
		handler.removeCallbacks(showRunnable);
		handler.postDelayed(showRunnable, 10);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
//		LogUtils.error("内容:"+content);
		canvas.drawText(content, cur_x, getHeight()/2, mpaint);
	}
	
	private Runnable showRunnable = new Runnable() {
		
		@Override
		public void run() {
			cur_x -= speed;
			if (cur_x<-content_long) {
				cur_x = content_long+getWidth();
			}
			handler.postDelayed(this, 10);
			invalidate();
		}
	};
	
}
