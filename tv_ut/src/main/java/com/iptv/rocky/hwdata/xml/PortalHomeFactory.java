package com.iptv.rocky.hwdata.xml;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.iptv.common.HttpXmlFactoryBase;
import com.iptv.common.data.EnumType;
import com.iptv.common.data.PortalHome;
import com.iptv.common.utils.CommonUtils;
import com.iptv.common.utils.LogUtils;
import com.iptv.rocky.hwdata.IPTVUriUtils;
import com.iptv.rocky.model.TvApplication;

public class PortalHomeFactory extends HttpXmlFactoryBase<ArrayList<PortalHome>> {

	private PortalHome mCurrentInfo;
	private ArrayList<String> imgs;
	
	@Override
	protected void xmlStartElement(String nodeName, ArrayList<PortalHome> content,
			Attributes attributes) throws SAXException {
		if ("ITEM".equals(nodeName)) {
			mCurrentInfo = new PortalHome();
		} else if ("IMGS".equals(nodeName)) {
			imgs = new ArrayList<String>();
		}
	}

	@Override
	protected void xmlEndElement(String nodeName, String value,
			ArrayList<PortalHome> content) throws SAXException {
		if ("ID".equals(nodeName)) {
			mCurrentInfo.id = CommonUtils.parseInt(value);
		} else if ("LAYOUT_TYPE".equals(nodeName)) {
			mCurrentInfo.layout_type = EnumType.LayoutType.createType(CommonUtils.parseInt(value));
		} else if ("BACKGROUNDTYPE".equals(nodeName)) {
			mCurrentInfo.bgType = EnumType.BackGroundType.createType(CommonUtils.parseInt(value));	
		} else if ("CONTENT_TYPE".equals(nodeName)) {
			mCurrentInfo.content_type = EnumType.ContentType.createType(CommonUtils.parseInt(value));
		}else if ("subContentType".equals(nodeName)){
			mCurrentInfo.subContentType = EnumType.SubContentType.createType(CommonUtils.parseInt(value));
		}else if ("PLATFORM".equals(nodeName)) {
			mCurrentInfo.platform = EnumType.Platform.createPlatform(value);	
		} else if ("CONTENT_ID".equals(nodeName)) {
			if(value != null){
				mCurrentInfo.content_id =value;
				if(mCurrentInfo.platform != EnumType.Platform.ZTE){
					mCurrentInfo.programId =value;
				}
			}
		} else if ("TITLE".equals(nodeName)) {
			mCurrentInfo.title = value;
		} else if ("BGIMG".equals(nodeName)) {
			mCurrentInfo.bg = IPTVUriUtils.HotelHost + value;
		} else if ("IMG".equals(nodeName)) {
			imgs.add(IPTVUriUtils.Host + value);
		} else if ("COLOR".equals(nodeName)) {
			mCurrentInfo.color = CommonUtils.parseInt(value);
		} else if ("SEQ".equals(nodeName)) {	
			mCurrentInfo.seq = CommonUtils.parseInt(value);
		} else if ("IMGS".equals(nodeName)) {
			if (mCurrentInfo != null)
				mCurrentInfo.imgs = imgs;
		} else if("programId".equals(nodeName)){
			if(mCurrentInfo.programId != "null"){
				if(mCurrentInfo.platform != EnumType.Platform.ZTE){
					mCurrentInfo.programId=value;
				}
			}
		} else if("parentId".equals(nodeName)){
			mCurrentInfo.parentId=value;
		} else if ("ITEM".equals(nodeName)) {
			if (mCurrentInfo.content_type == null) {
				mCurrentInfo.content_type = EnumType.ContentType.UNKNOW;
			}
			if (mCurrentInfo.layout_type == null) {
				mCurrentInfo.layout_type = EnumType.LayoutType.UNKNOW;
			}
			content.add(mCurrentInfo);
			mCurrentInfo = null;
		}
	}

	@Override
	protected ArrayList<PortalHome> createContent() {
		return new ArrayList<PortalHome>(10);
	}

	@Override
	protected String CreateUri(Object... args) {
		String stbId = TvApplication.stbId;
		String groupId = TvApplication.groupId;
		return String.format("%s?userid=%s&lang=%s&stbid=%s&groupId=%s",IPTVUriUtils.PortalHomeHost, args[0], args[1],stbId,groupId);
	}

	@Override
	protected ArrayList<NameValuePair> getPostArgs() {
		// TODO Auto-generated method stub
		return null;
	}

}
