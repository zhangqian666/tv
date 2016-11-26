package com.iptv.rocky.hwdata.xml;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.iptv.common.HttpXmlFactoryBase;
import com.iptv.common.data.RecUrl;
import com.iptv.rocky.hwdata.IPTVUriUtils;

public class RecUrlFactory extends HttpXmlFactoryBase<RecUrl> {
	
	private RecUrl mCurrentInfo;
	
	@Override
	protected void xmlStartElement(String nodeName, RecUrl content,
			Attributes attributes) throws SAXException {
	}
	
	@Override
	protected void xmlEndElement(String nodeName, String value,
			RecUrl content) throws SAXException {
		if ("RTSPURL".equals(nodeName)) {
			mCurrentInfo.RTSPURL = value;
		}
	}

	@Override
	protected RecUrl createContent() {
		if (mCurrentInfo == null) {
			mCurrentInfo = new RecUrl();
		}
		return mCurrentInfo;
	}

	@Override
	protected String CreateUri(Object... args) {
		return String.format("%s&progId=%d&playBillId=%d",
				IPTVUriUtils.RecTriggerPlayUrlHost, args[0], args[1]);
	}

	@Override
	protected ArrayList<NameValuePair> getPostArgs() {
		// TODO Auto-generated method stub
		return null;
	}

}
