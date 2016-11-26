package com.iptv.rocky.hwdata.xml;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.iptv.common.HttpXmlFactoryBase;
import com.iptv.common.data.EnumType;
import com.iptv.common.data.VodTop;
import com.iptv.common.utils.AtvUtils;
import com.iptv.common.utils.CommonUtils;
import com.iptv.common.utils.LogUtils;
import com.iptv.rocky.hwdata.IPTVUriUtils;

public class VodTopFactory extends HttpXmlFactoryBase<ArrayList<VodTop>> {

	private VodTop mCurrentInfo;
	
	@Override
	protected void xmlStartElement(String nodeName, ArrayList<VodTop> content,
			Attributes attributes) throws SAXException {
		if ("CATEGORY".equals(nodeName)) {
			mCurrentInfo = new VodTop();
		}
	}

	@Override
	protected void xmlEndElement(String nodeName, String value,
			ArrayList<VodTop> content) throws SAXException {
		
		if ("ID".equals(nodeName)) {
			mCurrentInfo.id = CommonUtils.parseInt(value);
		} else if ("LAYOUT_TYPE".equals(nodeName)) {
			mCurrentInfo.layout_type = EnumType.LayoutType.createType(CommonUtils.parseInt(value));
		} else if ("CONTENT_TYPE".equals(nodeName)) {
			mCurrentInfo.content_type = EnumType.ContentType.createType(CommonUtils.parseInt(value));
		} else if ("SEQ".equals(nodeName)) {
			mCurrentInfo.seq = CommonUtils.parseInt(value);	
		} else if ("SUB_CONTENT_TYPE".equals(nodeName)) {
			mCurrentInfo.subContentType = EnumType.SubContentType.createType(CommonUtils.parseInt(value));		
		} else if ("ICON".equals(nodeName)) {
			mCurrentInfo.icon = IPTVUriUtils.HotelHost+ value;
		} else if ("TITLE".equals(nodeName)) {
			mCurrentInfo.title = value;
		} else if ("BGIMG".equals(nodeName)) {
			mCurrentInfo.bg = IPTVUriUtils.HotelHost+value;	
		} else if ("CATEGORYID".equals(nodeName)) {
			mCurrentInfo.type_id = value;
		} else if ("BGTYPE".equals(nodeName)) {
			mCurrentInfo.bgType =  EnumType.BackGroundType.createType(CommonUtils.parseInt(value));
		} else if ("COLOR".equals(nodeName)) {
			mCurrentInfo.color = AtvUtils.getMetroItemBackground(CommonUtils.parseInt(value));
			LogUtils.debug("设置后的颜色信息:"+mCurrentInfo.color);
		} else if ("CATEGORY".equals(nodeName)) {
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
	protected ArrayList<VodTop> createContent() {
		return new ArrayList<VodTop>(10);
	}

	@Override
	protected String CreateUri(Object... args) {
		//return IPTVUriUtils.VodTopHost;
		return String.format("%s?userid=%s&lang=%s",IPTVUriUtils.VodTopHost, args[0], args[1]);
	}

	@Override
	protected ArrayList<NameValuePair> getPostArgs() {
		// TODO Auto-generated method stub
		return null;
	}

}
