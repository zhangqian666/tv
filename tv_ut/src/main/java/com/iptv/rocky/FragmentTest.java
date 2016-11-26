package com.iptv.rocky;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iptv.common.data.MyHotelPicture;
import com.iptv.common.utils.LogUtils;
import com.iptv.common.view.AsyncImageView;

public class FragmentTest extends Fragment{

	MyHotelPicture info;
	private int size;
	private int index;
	
	private AsyncImageView imageBottom;
	private AsyncImageView imageTopLeft;
	private AsyncImageView imageTopRight;
	
	private LinearLayout layoutTopLeft;
	private LinearLayout layoutTopRight;
	
	private TextView pageIndicator;

	public FragmentTest(){

	}

	@SuppressLint("ValidFragment")
	public FragmentTest(MyHotelPicture info,int size,int index) {
		
		this.size=size;
		this.index = index;
		this.info = info;
	}
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.fragmenttest, container, false);//关联布局文件 
		 
		imageBottom= (AsyncImageView) rootView.findViewById(R.id.imageBottom);
		imageTopLeft = (AsyncImageView) rootView.findViewById(R.id.imageTopLeft);
		imageTopRight = (AsyncImageView) rootView.findViewById(R.id.imageTopRight);
		imageBottom.setImageUrl(info.bgimg);
        
		if(info.frontImagePosition.equals("LEFT")){
			imageTopLeft.setImageUrl(info.topimg);
		}else if(info.frontImagePosition.equals("RIGHT")){
			imageTopRight.setImageUrl(info.topimg);
		}else if(info.frontImagePosition.equals("TOP")){
			imageTopLeft.setImageUrl(info.topimg);
		}else if(info.frontImagePosition.equals("DOWN")){
			imageTopRight.setImageUrl(info.topimg);
		}
	
		
		layoutTopLeft = (LinearLayout) rootView.findViewById(R.id.topLeft);
		layoutTopRight =(LinearLayout) rootView.findViewById(R.id.topRight);
		
		pageIndicator = (TextView) rootView.findViewById(R.id.pageIndicator);
        //设置按键监听事件  
		pageIndicator.setText(String.valueOf(index+1)+"/"+String.valueOf(size));
		
		return rootView;
	}
	
	public void doAnimation(){
		LogUtils.error("Do Animation");
		
	
		if(info.frontImagePosition.equals("LEFT")){
			LogUtils.debug("左对齐");
			layoutTopRight.setVisibility(View.INVISIBLE);
			layoutTopLeft.setVisibility(View.VISIBLE);
			Animation left_in_animation = AnimationUtils.loadAnimation(this.getActivity(), R.anim.left_in);
			imageTopLeft.startAnimation(left_in_animation);//使用view的startAnimation方法开始执行动画   

		}else if(info.frontImagePosition.equals("RIGHT")){
			LogUtils.debug("右对齐");
			layoutTopLeft.setVisibility(View.INVISIBLE);
			layoutTopRight.setVisibility(View.VISIBLE);
			
			Animation right_in_animation = AnimationUtils.loadAnimation(this.getActivity(), R.anim.right_in);
			imageTopRight.startAnimation(right_in_animation);//使用view的startAnimation方法开始执行动画   
		}
		
		
	}
}
