package com.iptv.rocky.hwdata.xml;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.protocol.HTTP;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.iptv.common.HttpXmlFactoryBase;
import com.iptv.common.update.VersionInfo;
import com.iptv.common.utils.CommonUtils;
import com.iptv.rocky.hwdata.IPTVUriUtils;

public class CheckUpdateFactory extends HttpXmlFactoryBase<VersionInfo> {
	
	private VersionInfo mCurrentInfo;
	
	@Override
	protected void xmlStartElement(String nodeName, VersionInfo content,
			Attributes attributes) throws SAXException {
	}
	
	@Override
	protected void xmlEndElement(String nodeName, String value,
			VersionInfo content) throws SAXException {
		if ("isUpdate".equals(nodeName)) {
			mCurrentInfo.setIsUpdate(value.equals("1"));
		} else if ("versionCode".equals(nodeName)) {
			mCurrentInfo.setVersionCode(CommonUtils.parseInt(value));
		} else if ("versionName".equals(nodeName)) {
			mCurrentInfo.setVersionName(value);
		} else if ("size".equals(nodeName)) {
			mCurrentInfo.setSize(value);
		} else if ("updateLog".equals(nodeName)) {
			mCurrentInfo.setUpdateLog(value);
		} else if ("updateUrl".equals(nodeName)) {
			mCurrentInfo.setUpdateUrl(value);
		} else if ("mode".equals(nodeName)) {
			mCurrentInfo.setMode(CommonUtils.parseInt(value));
		}
	}

	@Override
	protected VersionInfo createContent() {
		if (mCurrentInfo == null) {
			mCurrentInfo = new VersionInfo();
		}
		return mCurrentInfo;
	}

	/**
	 * 0 : channel
	 * 1 : deviceid
	 * 2 : devicetype
	 * 3 : osv
	 * 4 : sv
	 * 
	 * */
	@Override
	protected String CreateUri(Object... args) {
		String deviceType = "";
		try {
			deviceType = URLEncoder.encode((String)args[2], HTTP.UTF_8);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return String.format("%s?channel=%s&deviceid=%s&devicetype=%s&osv=%s&sv=%s&platform=%s&hotelid=%s&maker=%s",
				IPTVUriUtils.CheckIptvUpDateHost, args[0], args[1], deviceType, args[3], args[4],args[5],args[6],args[7]);
	}

	@Override
	protected ArrayList<NameValuePair> getPostArgs() {
		// TODO Auto-generated method stub
		return null;
	}

}
