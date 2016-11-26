package com.iptv.rocky.hwdata.xml;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.iptv.common.HttpXmlFactoryBase;
import com.iptv.common.data.RecChan;
import com.iptv.common.utils.CommonUtils;
import com.iptv.rocky.hwdata.IPTVUriUtils;

public class RecChanFactory extends HttpXmlFactoryBase<ArrayList<RecChan>> {

	private RecChan mCurrentInfo;
	
	@Override
	protected void xmlStartElement(String nodeName, ArrayList<RecChan> content,
			Attributes attributes) throws SAXException {
		if ("ITEM".equals(nodeName)) {
			mCurrentInfo = new RecChan();
		}
	}

	@Override
	protected void xmlEndElement(String nodeName, String value,ArrayList<RecChan> content) throws SAXException {
		if ("CHANNELID".equals(nodeName)) {
			mCurrentInfo.CHANNELID = CommonUtils.parseInt(value);
		} else if ("CHANNELINDEX".equals(nodeName)) {
			mCurrentInfo.CHANNELINDEX = String.format("%03d", CommonUtils.parseInt(value));
		} else if ("CHANNELNAME".equals(nodeName)) {
			mCurrentInfo.CHANNELNAME = value;
		} else if ("RECORDLENGTH".equals(nodeName)) {
			mCurrentInfo.RECORDLENGTH = CommonUtils.parseInt(value);
		} else if ("DEFINITION".equals(nodeName)) {
			mCurrentInfo.DEFINITION = CommonUtils.parseInt(value);
		} else if ("ITEM".equals(nodeName)) {
			content.add(mCurrentInfo);
			mCurrentInfo = null;
		}
	}

	@Override
	protected ArrayList<RecChan> createContent() {
		return new ArrayList<RecChan>(10);
	}

	@Override
	protected String CreateUri(Object... args) {
		return IPTVUriUtils.RecChanHost;
	}

	@Override
	protected ArrayList<NameValuePair> getPostArgs() {
		// TODO Auto-generated method stub
		return null;
	}

}
