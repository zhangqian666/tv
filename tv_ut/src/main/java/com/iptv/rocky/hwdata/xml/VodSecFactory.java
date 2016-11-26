package com.iptv.rocky.hwdata.xml;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.iptv.common.HttpXmlFactoryBase;
import com.iptv.common.data.EnumType;
import com.iptv.common.data.VodSec;
import com.iptv.common.utils.CommonUtils;
import com.iptv.rocky.hwdata.IPTVUriUtils;

public class VodSecFactory extends HttpXmlFactoryBase<ArrayList<VodSec>> {

	private VodSec mCurrentInfo;
	
	@Override
	protected void xmlStartElement(String nodeName, ArrayList<VodSec> content,
			Attributes attributes) throws SAXException {
		if ("CATEGORY".equals(nodeName)) {
			mCurrentInfo = new VodSec();
		}
	}

	@Override
	protected void xmlEndElement(String nodeName, String value,
			ArrayList<VodSec> content) throws SAXException {
		if ("ID".equals(nodeName)) {
			mCurrentInfo.id = CommonUtils.parseInt(value);
		} else if ("SUB_CONTENT_TYPE".equals(nodeName)) {
			mCurrentInfo.sub_content_type = EnumType.SubContentType.createType(CommonUtils.parseInt(value));
		} else if ("CONTENT_TYPE".equals(nodeName)) {
			mCurrentInfo.content_type = EnumType.ContentType.createType(CommonUtils.parseInt(value));
		} else if ("LAYOUT_TYPE".equals(nodeName)) {
			mCurrentInfo.layout_type = EnumType.LayoutType.createType(CommonUtils.parseInt(value));
		} else if ("CATEGORYID".equals(nodeName)) {
			mCurrentInfo.type_id = value;
		} else if ("TITLE".equals(nodeName)) {
			mCurrentInfo.title = value;
		} else if ("BGIMG".equals(nodeName)) {
			mCurrentInfo.bg = IPTVUriUtils.HotelHost + value;
		} else if ("CATEGORY".equals(nodeName)) {
			if (mCurrentInfo.layout_type == null) {
				mCurrentInfo.layout_type = EnumType.LayoutType.UNKNOW;
			}
			if (mCurrentInfo.content_type == null) {
				mCurrentInfo.content_type = EnumType.ContentType.UNKNOW;
			}
			if (mCurrentInfo.sub_content_type == null) {
				mCurrentInfo.sub_content_type = EnumType.SubContentType.UNKNOW;
			}
			content.add(mCurrentInfo);
			mCurrentInfo = null;
		}
	}

	@Override
	protected ArrayList<VodSec> createContent() {
		return new ArrayList<VodSec>(10);
	}

	@Override
	protected String CreateUri(Object... args) {
		return String.format("%s?userid=%s&lang=%s&parentid=%s", IPTVUriUtils.VodSecHost, args[0], args[1], args[2]);
	}

	@Override
	protected ArrayList<NameValuePair> getPostArgs() {
		// TODO Auto-generated method stub
		return null;
	}

}
