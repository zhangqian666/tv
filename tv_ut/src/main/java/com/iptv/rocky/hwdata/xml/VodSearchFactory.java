package com.iptv.rocky.hwdata.xml;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.iptv.common.HttpXmlFactoryBase;
import com.iptv.common.data.EnumType;
import com.iptv.common.data.VodChannel;
import com.iptv.common.utils.CommonUtils;
import com.iptv.common.utils.LogUtils;
import com.iptv.rocky.hwdata.IPTVUriUtils;

public class VodSearchFactory extends HttpXmlFactoryBase<ArrayList<VodChannel>> {

	private VodChannel mCurrentInfo;
	
	public int counttotal;
	
	@Override
	protected void xmlStartElement(String nodeName, ArrayList<VodChannel> content,
			Attributes attributes) throws SAXException {
		if ("SEARCHRESULT".equals(nodeName)) {
			counttotal = Integer.valueOf(attributes.getValue("COUNTTOTAL"));
		} else if ("ITEM".equals(nodeName)) {
			mCurrentInfo = new VodChannel();
		}
	}

	@Override
	protected void xmlEndElement(String nodeName, String value,
			ArrayList<VodChannel> content) throws SAXException {
		if ("VODID".equals(nodeName)) {
			mCurrentInfo.VODID = value;
		} else if ("VODNAME".equals(nodeName)) {
			mCurrentInfo.VODNAME = value;
		} else if ("PLATFORM".equals(nodeName)) {
			mCurrentInfo.platform = EnumType.Platform.createPlatform(value);		
		} else if ("PICPATH".equals(nodeName)) {
			mCurrentInfo.PICPATH = IPTVUriUtils.Host + value;
		} else if ("ITEM".equals(nodeName)) {
			
			if(mCurrentInfo.platform == null){
				mCurrentInfo.platform = EnumType.Platform.HUAWEI;
			}
			
			content.add(mCurrentInfo);
			mCurrentInfo = null;
		}
	}

	@Override
	protected ArrayList<VodChannel> createContent() {
		return new ArrayList<VodChannel>(10);
	}

	@Override
	protected String CreateUri(Object... args) {
		//LogUtils.error("搜索字符串:"+String.format("%s&code=%s&length=%d&station=%d",IPTVUriUtils.VodSearchHost, args[0], args[1], args[2]));
		return String.format("%s&code=%s&length=%d&station=%d",IPTVUriUtils.VodSearchHost, args[0], args[1], args[2]);
	}

	@Override
	protected ArrayList<NameValuePair> getPostArgs() {
		
		return null;
	}

}
