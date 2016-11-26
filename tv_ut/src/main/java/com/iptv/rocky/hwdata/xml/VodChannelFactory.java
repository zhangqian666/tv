package com.iptv.rocky.hwdata.xml;

import java.util.ArrayList;
import java.util.Locale;

import org.apache.http.NameValuePair;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.iptv.common.HttpXmlFactoryBase;
import com.iptv.common.data.EnumType;
import com.iptv.common.data.VodChannel;
import com.iptv.common.utils.CommonUtils;
import com.iptv.common.utils.LogUtils;
import com.iptv.rocky.hwdata.IPTVUriUtils;

public class VodChannelFactory extends HttpXmlFactoryBase<ArrayList<VodChannel>> {

	private VodChannel mCurrentInfo;
	public int counttotal;
	public EnumType.Platform platformEach;
	
	@Override
	protected void xmlStartElement(String nodeName, ArrayList<VodChannel> content,
			Attributes attributes) throws SAXException {
		if ("VODS".equals(nodeName)) {
			counttotal = Integer.valueOf(attributes.getValue("COUNTTOTAL"));
		} else if ("ITEM".equals(nodeName)) {
			mCurrentInfo = new VodChannel();
			platformEach = EnumType.Platform.HUAWEI;
		}
	}

	@Override
	protected void xmlEndElement(String nodeName, String value, 
			ArrayList<VodChannel> content) throws SAXException {
		
		if ("VODID".equals(nodeName)) {
			mCurrentInfo.VODID = value;
			
		} else if ("CONTENT_TYPE".equals(nodeName)) {
			mCurrentInfo.content_type = EnumType.ContentType.createType(CommonUtils.parseInt(value));
		} else if ("VODNAME".equals(nodeName)) {
			mCurrentInfo.VODNAME = value;
			
		} else if ("PICPATH".equals(nodeName)) {
			mCurrentInfo.PICPATH = value;
		} else if ("PLATFORM".equals(nodeName)) {
			platformEach = EnumType.Platform.createPlatform(value);	
		} else if ("DEFINITION".equals(nodeName)) {
			mCurrentInfo.DEFINITION = CommonUtils.parseInt(value);
		} else if ("ITEM".equals(nodeName)) {
			if (platformEach == EnumType.Platform.HUAWEI || platformEach == EnumType.Platform.DEVHUAWEI || platformEach == EnumType.Platform.RUNHUAWEI) {
				mCurrentInfo.PICPATH = IPTVUriUtils.Host + mCurrentInfo.PICPATH;
			} else if (platformEach == EnumType.Platform.HOTEL) {
				mCurrentInfo.PICPATH = IPTVUriUtils.HotelHost +  mCurrentInfo.PICPATH;
			} else {
				mCurrentInfo.PICPATH = IPTVUriUtils.Host + mCurrentInfo.PICPATH;
			}
			mCurrentInfo.platform = platformEach;
			if (mCurrentInfo.content_type == null) {
				mCurrentInfo.content_type = EnumType.ContentType.UNKNOW;
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
		EnumType.Platform platform;
		if (args[0] != null) {
			platform = EnumType.Platform.createPlatform(args[0].toString());
		} else {
			platform = EnumType.Platform.HUAWEI;
		}
		String url = null;
		LogUtils.debug("CreateUrl:"+platform);
		if (platform == EnumType.Platform.HUAWEI || platform == EnumType.Platform.UNKNOW || platform == EnumType.Platform.DEVHUAWEI || platform == EnumType.Platform.RUNHUAWEI) {
			url = String.format(Locale.CHINA,"%s?Action=getVodListByTypeId&typeId=%s&length=%d&station=%d", IPTVUriUtils.VodChannelListHost, args[1], args[2], args[3]);
		} else if (platform == EnumType.Platform.HOTEL) {
			url = String.format(Locale.CHINA,"%s?typeId=%s&length=%d&station=%d", IPTVUriUtils.HotelProgramListHost, args[1], args[2], args[3]);
		} else if (platform == EnumType.Platform.ZTE){
			
		}
		return url;
	}

	@Override
	protected ArrayList<NameValuePair> getPostArgs() {
		// TODO Auto-generated method stub
		return null;
	}

}
