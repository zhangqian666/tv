package com.iptv.rocky.hwdata.xml;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.iptv.common.HttpXmlFactoryBase;
import com.iptv.common.data.EnumType;
import com.iptv.common.data.MyHotelTop;
import com.iptv.common.utils.AtvUtils;
import com.iptv.common.utils.CommonUtils;
import com.iptv.rocky.hwdata.IPTVUriUtils;

public class MyHotelTopFactory extends HttpXmlFactoryBase<ArrayList<MyHotelTop>> {

	private MyHotelTop mCurrentInfo;
	private ArrayList<String> imgs;
	
	@Override
	protected void xmlStartElement(String nodeName, ArrayList<MyHotelTop> content,	Attributes attributes) throws SAXException {
		if ("ITEM".equals(nodeName)) {
			mCurrentInfo = new MyHotelTop();
		}else if ("IMGS".equals(nodeName)) {
			imgs = new ArrayList<String>();
		}
	}

	@Override
	protected void xmlEndElement(String nodeName, String value,ArrayList<MyHotelTop> content) throws SAXException {
		if ("ID".equals(nodeName)) {
			mCurrentInfo.id = CommonUtils.parseInt(value);
		} else if ("LAYOUT_TYPE".equals(nodeName)) {
			mCurrentInfo.layout_type = EnumType.LayoutType.createType(CommonUtils.parseInt(value));
		} else if ("CONTENT_TYPE".equals(nodeName)) {
			mCurrentInfo.content_type = EnumType.ContentType.createType(CommonUtils.parseInt(value));
		} else if ("MY_HOTEL_SUB_CONTENT_TYPE".equals(nodeName)) {
			mCurrentInfo.subContentType = EnumType.MyHotelSubContentType.createType(CommonUtils.parseInt(value));	
		} else if ("ICON".equals(nodeName)) {
			mCurrentInfo.icon = IPTVUriUtils.Host + value;
		} else if ("TITLE".equals(nodeName)) {
			mCurrentInfo.title = value;
		} else if ("CATEGORYID".equals(nodeName)) {
			mCurrentInfo.type_id = value;	
		} else if ("backGroudType".equals(nodeName)) {
			mCurrentInfo.bgType =  EnumType.BackGroundType.createType(CommonUtils.parseInt(value));	
		} else if ("BGIMG".equals(nodeName)) {
			mCurrentInfo.bg = IPTVUriUtils.HotelHost + value;
		} else if ("IMG".equals(nodeName)) {
			imgs.add(IPTVUriUtils.HotelHost + value);
		} else if ("IMGS".equals(nodeName)) {
			if (mCurrentInfo != null) mCurrentInfo.imgs = imgs;
		} else if ("COLOR".equals(nodeName)) {
			int color = CommonUtils.parseInt(value);
			if(color >0 ){
				mCurrentInfo.bg = AtvUtils.getMetroItemBackground(color);
			}
		} else if ("ITEM".equals(nodeName)) {
			if (mCurrentInfo.content_type == null) {
				mCurrentInfo.content_type = EnumType.ContentType.UNKNOW;
			}
			if (mCurrentInfo.layout_type == null) {
				mCurrentInfo.layout_type = EnumType.LayoutType.UNKNOW;
			}
			content.add(mCurrentInfo);
			mCurrentInfo = null;
		}
		
	}

	@Override
	protected ArrayList<MyHotelTop> createContent() {
		return new ArrayList<MyHotelTop>(10);
	}

	@Override
	protected String CreateUri(Object... args) {
		return String.format("%s?userid=%s&lang=%s&id=%s",IPTVUriUtils.myHotelTopLevelHost, args[0], args[1],args[2]);
	}

	@Override
	protected ArrayList<NameValuePair> getPostArgs() {
		// TODO Auto-generated method stub
		return null;
	}
}
