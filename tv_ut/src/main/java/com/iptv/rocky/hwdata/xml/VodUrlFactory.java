package com.iptv.rocky.hwdata.xml;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.iptv.common.HttpXmlFactoryBase;
import com.iptv.common.data.EnumType;
import com.iptv.common.data.VodUrl;
import com.iptv.common.utils.LogUtils;
import com.iptv.rocky.hwdata.IPTVUriUtils;

public class VodUrlFactory extends HttpXmlFactoryBase<VodUrl> {
	
	private VodUrl mCurrentInfo;
	
	private EnumType.Platform platform;
	
	@Override
	protected void xmlStartElement(String nodeName, VodUrl content,
			Attributes attributes) throws SAXException {
	}
	
	@Override
	protected void xmlEndElement(String nodeName, String value,
			VodUrl content) throws SAXException {
		LogUtils.debug("获取到的地址信息："+nodeName+" "+value);
		if ("RTSPURL".equals(nodeName)) {
			mCurrentInfo.RTSPURL = value;
		} else if ("PLAYURL".equals(nodeName)) { 
			mCurrentInfo.RTSPURL = value;
		}
	}

	@Override
	protected VodUrl createContent() {
		if (mCurrentInfo == null) {
			mCurrentInfo = new VodUrl();
		}

		return mCurrentInfo;
	}

	@Override
	protected String CreateUri(Object... args) {
		platform = null;
		if (args[0] != null) {
			platform = EnumType.Platform.createPlatform(args[0].toString());
		} else {
			platform = EnumType.Platform.HUAWEI;
		}
		
		String url = null;
		if (platform == EnumType.Platform.HUAWEI || platform == EnumType.Platform.UNKNOW || platform == EnumType.Platform.DEVHUAWEI || platform == EnumType.Platform.RUNHUAWEI) {
			url = String.format("%s&progId=%s", IPTVUriUtils.VodTriggerPlayUrlHost, args[1]);
		} else if (platform == EnumType.Platform.HOTEL) {
			url = String.format("%s?progId=%s", IPTVUriUtils.HotelPlayUrlHost, args[1]);
		} else if(platform == EnumType.Platform.ZTE){
			url = String.format("%s?progId=%s", IPTVUriUtils.VodTriggerPlayUrlHost, args[1]);
		}
		
		return url;
	}

	@Override
	protected ArrayList<NameValuePair> getPostArgs() {
		return null;
	}

}
