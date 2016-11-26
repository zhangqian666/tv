package com.iptv.rocky.hwdata.xml;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.iptv.common.HttpXmlFactoryBase;
import com.iptv.common.data.EnumType;
import com.iptv.common.data.LiveChannelBill;
import com.iptv.common.utils.CommonUtils;
import com.iptv.rocky.hwdata.IPTVUriUtils;

public class PublicLiveChannelsFactory extends HttpXmlFactoryBase<ArrayList<LiveChannelBill>> {

	private LiveChannelBill mCurrentInfo;
	
	@Override
	protected void xmlStartElement(String nodeName, ArrayList<LiveChannelBill> content, Attributes attributes) throws SAXException {
		if ("CHANNEL".equals(nodeName)) {
			mCurrentInfo = new LiveChannelBill();
		}
	}

	@Override
	protected void xmlEndElement(String nodeName, String value, 
			ArrayList<LiveChannelBill> content) throws SAXException {
		
		if (nodeName.equals("Platform")) {
			mCurrentInfo.platform = EnumType.Platform.createPlatform(value);
		} else if ("ChannelID".equals(nodeName)) {
			mCurrentInfo.ChannelID =value;
		} else if (nodeName.equals("ChannelType")) {
			mCurrentInfo.ChannelType = CommonUtils.parseInt(value);	
		} else if ("UserChannelID".equals(nodeName)) {
			mCurrentInfo.UserChannelID = CommonUtils.parseInt(value);
		} else if ("ChannelName".equals(nodeName)) {
			mCurrentInfo.ChannelName = value;
		} else if (nodeName.equals("ChannelURL")) {
			mCurrentInfo.ChannelURL = value;
		} else if (nodeName.equals("TimeShift")) {
			mCurrentInfo.TimeShift = (value.equals("1"));
		} else if (nodeName.equals("ChannelPurchased")) {
			mCurrentInfo.ChannelPurchased = value.endsWith("1");	
		} else if ("CHANNEL".equals(nodeName)) {
			content.add(mCurrentInfo);
			//mCurrentInfo = null;
		}
	}

	@Override
	protected ArrayList<LiveChannelBill> createContent() {
		return new ArrayList<LiveChannelBill>(10);
	}

	@Override
	protected String CreateUri(Object... args) {
		return String.format("%s?userid=%s&lang=%s",IPTVUriUtils.ChannelListPublic, args[0], args[1]);
	}

	@Override
	protected ArrayList<NameValuePair> getPostArgs() {
		return null;
	}

}
