package com.iptv.common.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 点播分类
 */
public class MyHotelPictureList implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -499504994600293479L;
	
	/*
	 	VODID Integer 节目编号
		VODNAME String 节目名称
		PICPATH String 影片海报路径
		DEFINITION Integer 高清标识		1：高清；2：标清
	 */
	public String categoryId;
	public String contentId;
	//public EnumType.ContentType content_type;
	//public String VODNAME;
	public String TITLE;
	//public String PICPATH;
	public int DEFINITION;
	
	//public int nNumber;
	
	public List<MyHotelPicture> pictureList;
	
	public MyHotelPictureList()
	{
		pictureList = new ArrayList<MyHotelPicture>();
	}
	
	public MyHotelPictureList(int VODID)
	{
		//this.VODID = VODID;
	}
	
}
