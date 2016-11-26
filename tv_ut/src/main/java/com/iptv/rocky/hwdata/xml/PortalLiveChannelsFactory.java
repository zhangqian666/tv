package com.iptv.rocky.hwdata.xml;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.iptv.common.HttpXmlFactoryBase;
import com.iptv.common.data.EnumType;
import com.iptv.common.data.PortalChannels;
import com.iptv.common.data.PortalLiveChannel;
import com.iptv.common.data.PortalLiveType;
import com.iptv.common.utils.CommonUtils;
import com.iptv.common.utils.LogUtils;
import com.iptv.rocky.hwdata.IPTVUriUtils;

public class PortalLiveChannelsFactory extends HttpXmlFactoryBase<PortalChannels> {
	
	private PortalChannels mCurrentInfo;
	private PortalLiveChannel portalLiveChannel;
	private PortalLiveType portalLiveType;
	private ArrayList<String> lstChannelIds;
	
	private String type;
	
	@Override
	protected void xmlStartElement(String nodeName, PortalChannels content,
			Attributes attributes) throws SAXException {
		if ("LIVECHANNEL".equals(nodeName)) {
			type = "LIVECHANNEL";
			portalLiveChannel = new PortalLiveChannel();
		} else if ("CATEGORY".equals(nodeName)) {
			type = "LIVECLASSIFICATION";
			portalLiveType = new PortalLiveType();
			lstChannelIds = new ArrayList<String>();
		}
	}
	
	@Override
	protected void xmlEndElement(String nodeName, String value,
			PortalChannels content) throws SAXException {
		
		if ("LIVECHANNELID".equals(nodeName)) {
			portalLiveChannel.id = CommonUtils.parseInt(value);
			LogUtils.info("LIVECHANNELID--->"+portalLiveChannel.id);
		} else if ("LIVECHANNELLAYOUT_TYPE".equals(nodeName)) {
			portalLiveChannel.layout_type = EnumType.LayoutType.createType(CommonUtils.parseInt(value));
		} else if ("LIVECHANNELCONTENT_TYPE".equals(nodeName)) {
			portalLiveChannel.content_type = EnumType.ContentType.createType(CommonUtils.parseInt(value));
		} else if ("LIVECHANNELCHANNELID".equals(nodeName)) {
			portalLiveChannel.ChannelID = value;
		} else if ("LIVECHANNEL_BG_TYPE".equals(nodeName)) {	
			portalLiveChannel.backGroundType = EnumType.BackGroundType.createType(CommonUtils.parseInt(value));
		} else if ("LIVECHANNEL_BG_IMG".equals(nodeName)) {
			portalLiveChannel.bg = IPTVUriUtils.HotelHost + value;
		} else if ("LIVECHANNEL".equals(nodeName)) {
			mCurrentInfo.lstPortalLiveChannel.add(portalLiveChannel);
		} else if ("LIVECHANNEL_SEQ".equals(nodeName)) {	
			portalLiveChannel.seq = CommonUtils.parseInt(value);
		} else if ("SEQ".equals(nodeName)) {		
			portalLiveType.seq = CommonUtils.parseInt(value);
		} else if ("ID".equals(nodeName)) {
			portalLiveType.id = CommonUtils.parseInt(value);
		} else if ("LAYOUT_TYPE".equals(nodeName)) {
			portalLiveType.layout_type = EnumType.LayoutType.createType(CommonUtils.parseInt(value));
		} else if ("CONTENT_TYPE".equals(nodeName)) {
			portalLiveType.content_type = EnumType.ContentType.createType(CommonUtils.parseInt(value));
		} else if ("ICON".equals(nodeName)) {
			portalLiveType.icon = IPTVUriUtils.HotelHost + value;
		} else if ("BGTYPE".equals(nodeName)) {	
			portalLiveType.backGroundType = EnumType.BackGroundType.createType(CommonUtils.parseInt(value));	
		} else if ("BGIMG".equals(nodeName)) {
			portalLiveType.bg = IPTVUriUtils.HotelHost + value;
		} else if ("TITLE".equals(nodeName)) {
			if (type.equals("LIVECHANNEL")) {
				portalLiveChannel.ChannelName = value;
			} else if (type.equals("LIVECLASSIFICATION")) {
				portalLiveType.title = value;
			}
		} else if ("CATEGORYID".equals(nodeName)) {
			portalLiveType.typeId = CommonUtils.parseInt(value);
		} else if ("CHNID".equals(nodeName)) {
			lstChannelIds.add(value);		
		} else if ("CATEGORY".equals(nodeName)) {
			if (portalLiveType.content_type == null) {
				portalLiveType.content_type = EnumType.ContentType.UNKNOW;
			}
			if (portalLiveType.layout_type == null) {
				portalLiveType.layout_type = EnumType.LayoutType.UNKNOW;
			}
			portalLiveType.lstChannelIds.addAll(lstChannelIds);
			lstChannelIds.clear();
			mCurrentInfo.lstPortalLiveType.add(portalLiveType);
		}
	}

	@Override
	protected PortalChannels createContent() {
		if (mCurrentInfo == null) {
			mCurrentInfo = new PortalChannels();
		}
		return mCurrentInfo;
	}

	@Override
	protected String CreateUri(Object... args) {
		return String.format("%s?userid=%s&lang=%s", IPTVUriUtils.PortalChannelsHost, args[0], args[1]);
	}

	@Override
	protected ArrayList<NameValuePair> getPostArgs() {
		// TODO Auto-generated method stub
		return null;
	}

}
