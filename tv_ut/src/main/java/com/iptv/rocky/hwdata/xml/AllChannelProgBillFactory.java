package com.iptv.rocky.hwdata.xml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.apache.http.NameValuePair;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.iptv.common.HttpXmlFactoryBase;
import com.iptv.common.data.EnumType;
import com.iptv.common.data.LiveChannelBill;
import com.iptv.common.data.ProgBill;
import com.iptv.common.utils.LogUtils;
import com.iptv.rocky.HomeActivity;
import com.iptv.rocky.hwdata.IPTVUriUtils;
import com.iptv.rocky.model.TvApplication;

public class AllChannelProgBillFactory extends HttpXmlFactoryBase<ArrayList<LiveChannelBill>> {

	private LiveChannelBill mCurrentInfo;
	public ProgBill progBill;
	
	public static ArrayList<LiveChannelBill> lstLiveChannelBill;
	
	public static HashMap<String, LiveChannelBill> mapLiveChannelBill;
	
	@Override
	protected void xmlStartElement(String nodeName, ArrayList<LiveChannelBill> content,
			Attributes attributes) throws SAXException {
		if ("CHANNEL".equals(nodeName)) {
			if(TvApplication.platform.equals(EnumType.Platform.HUAWEI) || TvApplication.platform.equals(EnumType.Platform.HOTEL)){
				String ChannelID = attributes.getValue("CHANNELID");
				if (HomeActivity.mapLiveChannel.containsKey(ChannelID)) {
					mCurrentInfo = new LiveChannelBill(HomeActivity.mapLiveChannel.get(ChannelID));
				}
			}else{
				String ChannelID = attributes.getValue("CHANNELID");
				if (HomeActivity.mapLiveChannel.containsKey(ChannelID)) {
					mCurrentInfo = new LiveChannelBill(HomeActivity.mapLiveChannel.get(ChannelID));
				}
			}
		} else if ("ITEM".equals(nodeName)) {
			progBill = new ProgBill();
		}
	}
	
	@Override
	protected void xmlEndElement(String nodeName, String value,
			ArrayList<LiveChannelBill> content) throws SAXException {
		if ("PROGID".equals(nodeName)) {
			progBill.programId = value;
		} else if ("BEGINDATE".equals(nodeName)) {
			progBill.beginDate = value;
		} else if ("BEGINTIME".equals(nodeName)) {
			progBill.beginTime = value;
		} else if ("ENDTIME".equals(nodeName)) {
			progBill.endTime = value;
		} else if ("TITLE".equals(nodeName)) {
			progBill.title = value;
		} else if ("ITEM".equals(nodeName)) {
			if (mCurrentInfo != null)
				mCurrentInfo.lstProgBill.add(progBill);
		} else if ("CHANNEL".equals(nodeName)) {
			if (mCurrentInfo != null)
				content.add(mCurrentInfo);
			mCurrentInfo = null;
		} else if ("CHANNELS".equals(nodeName)) {
			LogUtils.error("mapLiveChannelBill 是否为空?"+(mapLiveChannelBill == null));
			if(mapLiveChannelBill == null){
				mapLiveChannelBill = new LinkedHashMap<String, LiveChannelBill>();
			}
			
			for (LiveChannelBill bill : content) {
				mapLiveChannelBill.put(bill.ChannelID, bill);
			}
		}
	}

	@Override
	protected ArrayList<LiveChannelBill> createContent() {
		return new ArrayList<LiveChannelBill>(10);
	}

	@Override
	protected String CreateUri(Object... args) {
		return String.format("%s&date=%s",IPTVUriUtils.AllChannelProgBillHost, args[0]);
	}

	@Override
	protected ArrayList<NameValuePair> getPostArgs() {
		// TODO Auto-generated method stub
		return null;
	}

}
