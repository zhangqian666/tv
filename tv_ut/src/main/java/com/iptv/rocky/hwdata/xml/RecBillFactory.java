package com.iptv.rocky.hwdata.xml;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.iptv.common.HttpXmlFactoryBase;
import com.iptv.common.data.RecBill;
import com.iptv.common.utils.CommonUtils;
import com.iptv.rocky.hwdata.IPTVUriUtils;

public class RecBillFactory extends HttpXmlFactoryBase<ArrayList<RecBill>> {

	private RecBill mCurrentInfo;
	
	@Override
	protected void xmlStartElement(String nodeName, ArrayList<RecBill> content,
			Attributes attributes) throws SAXException {
		if ("ITEM".equals(nodeName)) {
			mCurrentInfo = new RecBill();
		}
	}
	
	@Override
	protected void xmlEndElement(String nodeName, String value,
			ArrayList<RecBill> content) throws SAXException {
		if ("PROGID".equals(nodeName)) {
			mCurrentInfo.programId = CommonUtils.parseInt(value);
		} else if ("BEGINDATE".equals(nodeName)) {
			mCurrentInfo.beginDate = value;
		} else if ("BEGINTIME".equals(nodeName)) {
			mCurrentInfo.beginTime = value;
		} else if ("ENDTIME".equals(nodeName)) {
			mCurrentInfo.endTime = value;
		} else if ("TITLE".equals(nodeName)) {
			mCurrentInfo.title = value;
		} else if ("STATUS".equals(nodeName)) {
			mCurrentInfo.recStatus = CommonUtils.parseInt(value);
		} else if ("ITEM".equals(nodeName)) {
			content.add(mCurrentInfo);
			mCurrentInfo = null;
		}
	}

	@Override
	protected ArrayList<RecBill> createContent() {
		return new ArrayList<RecBill>(10);
	}

	@Override
	protected String CreateUri(Object... args) {
		return String.format("%s?progId=%d&date=%s&statusFlag=%d",IPTVUriUtils.RecBillHost, args[0], args[1],args[2]);
	}

	@Override
	protected ArrayList<NameValuePair> getPostArgs() {
		return null;
	}

}
