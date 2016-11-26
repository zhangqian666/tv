package com.iptv.rocky.hwdata.xml;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import android.util.Log;

import com.iptv.common.HttpXmlFactoryBase;
import com.iptv.common.data.EnumType;
import com.iptv.common.data.MyHotelSec;
import com.iptv.common.utils.CommonUtils;
import com.iptv.rocky.hwdata.IPTVUriUtils;

public class MyHotelSecFactory extends HttpXmlFactoryBase<ArrayList<MyHotelSec>> {

	private MyHotelSec mCurrentInfo;
	
	@Override
	protected void xmlStartElement(String nodeName, ArrayList<MyHotelSec> content,
			Attributes attributes) throws SAXException {
		if ("ITEM".equals(nodeName)) {
			mCurrentInfo = new MyHotelSec();
		}
	}

	@Override
	protected void xmlEndElement(String nodeName, String value,
			ArrayList<MyHotelSec> content) throws SAXException {
		if ("ID".equals(nodeName)) {
			mCurrentInfo.id = CommonUtils.parseInt(value);
		} else if ("SUB_CONTENT_TYPE".equals(nodeName)) {
			mCurrentInfo.sub_content_type = EnumType.SubContentType.createType(CommonUtils.parseInt(value));
		} else if ("MY_HOTEL_SUB_CONTENT_TYPE".equals(nodeName)) {
			mCurrentInfo.my_hotel_sub_content_type = EnumType.MyHotelSubContentType.createType(CommonUtils.parseInt(value));
			Log.d("MyHotelSecFactory", "mCurrentInfo.my_hotel_sub_content_type:"+mCurrentInfo.my_hotel_sub_content_type);
		} else if ("CONTENT_TYPE".equals(nodeName)) {
			mCurrentInfo.content_type= EnumType.ContentType.createType(CommonUtils.parseInt(value));
		} else if ("LAYOUT_TYPE".equals(nodeName)) {
			mCurrentInfo.layout_type = EnumType.LayoutType.createType(CommonUtils.parseInt(value));
		} else if ("CATEGORYID".equals(nodeName)) {
			mCurrentInfo.type_id = value;
		} else if ("TITLE".equals(nodeName)) {
			mCurrentInfo.title = value;
		} else if ("BGIMG".equals(nodeName)) {
			mCurrentInfo.bg = IPTVUriUtils.HotelHost + value;
		} else if ("ITEM".equals(nodeName)) {
			if (mCurrentInfo.layout_type == null) {
				mCurrentInfo.layout_type = EnumType.LayoutType.UNKNOW;
			}
			if (mCurrentInfo.content_type == null) {
				mCurrentInfo.content_type = EnumType.ContentType.UNKNOW;
			}
			if (mCurrentInfo.sub_content_type == null) {
				mCurrentInfo.sub_content_type = EnumType.SubContentType.UNKNOW;
			}
			if (mCurrentInfo.my_hotel_sub_content_type == null) {
				mCurrentInfo.my_hotel_sub_content_type = EnumType.MyHotelSubContentType.UNKNOW;
			}
			content.add(mCurrentInfo);
			mCurrentInfo = null;
		}
	}

	@Override
	protected ArrayList<MyHotelSec> createContent() {
		return new ArrayList<MyHotelSec>(10);
	}

	@Override
	protected String CreateUri(Object... args) {
		return String.format("%s?userid=%s&lang=%s&id=%s",IPTVUriUtils.myHotelSecHost, args[0], args[1],args[2]);
	}

	@Override
	protected ArrayList<NameValuePair> getPostArgs() {
		// TODO Auto-generated method stub
		return null;
	}

}
