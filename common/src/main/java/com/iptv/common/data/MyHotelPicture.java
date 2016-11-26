package com.iptv.common.data;

import java.io.Serializable;

/**
 * 点播分类
 */
public class MyHotelPicture implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -499504994600293479L;
	
	public int seq;
	
	public String title;
	public String picPath;
	
	// 背景图片
	public String bgimg;
	
	// 最上面一层的说明图片
	public String topimg;
	
	public String frontImagePosition;
	
	public MyHotelPicture()
	{
		
	}
	
}
