package com.iptv.rocky.hwdata.xml;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import android.util.Log;

import com.iptv.common.HttpXmlFactoryBase;
import com.iptv.common.data.MyHotelPicture;
import com.iptv.common.data.MyHotelPictureList;
import com.iptv.common.utils.CommonUtils;
import com.iptv.common.utils.LogUtils;
import com.iptv.rocky.hwdata.IPTVUriUtils;
import com.iptv.rocky.model.TvApplication;

public class MyHotelPictureFactory extends HttpXmlFactoryBase<ArrayList<MyHotelPicture>> {

	private MyHotelPictureList mCurrentInfo;
	private MyHotelPicture myHotelPicture;
	public int counttotal;
	
	private boolean analyzImage = false;
	
	@Override
	protected void xmlStartElement(String nodeName, ArrayList<MyHotelPicture> content,Attributes attributes) throws SAXException {
		if ("HOTEL".equals(nodeName)) {
			mCurrentInfo = new MyHotelPictureList();
			/*String countTotal =attributes.getValue("COUNTTOTAL");
			if(countTotal != null){
				counttotal = Integer.valueOf(countTotal);
			}*/
			//mCurrentInfo.pictureList = new ArrayList<MyHotelPicture>();
		} else if ("IMG".equals(nodeName)) {
			myHotelPicture = new MyHotelPicture();
		} else if ("ITEM".equals(nodeName)){
			
			
		}
	}

	@Override
	protected void xmlEndElement(String nodeName, String value,ArrayList<MyHotelPicture> content) throws SAXException {
		Log.d("MyHotelPictureFactory","value:"+value +"; nodename: " + nodeName);
		if("TITLE".equals(nodeName)) {
			myHotelPicture.title = value;
		} else if ("IMG".equals(nodeName)) {
			//mCurrentInfo.pictureList.add(myHotelPicture);
			content.add(myHotelPicture);
		}else if("HOTEL".equals(nodeName)){
			
		}else if("COUNTTOTAL".equals(nodeName)){
		
		}else if("ITEM".equals(nodeName)){
			//content.add(myHotelPicture);
		}else if("BGIMG".equals(nodeName)){
			myHotelPicture.bgimg = IPTVUriUtils.HotelHost + value;
		}else if("FRIMG".equals(nodeName)){
			myHotelPicture.topimg = IPTVUriUtils.HotelHost + value;
		}else if("FRPOSITION".equals(nodeName)){
			myHotelPicture.frontImagePosition = value;
		}else if("seq".equals(nodeName)){
			myHotelPicture.seq = CommonUtils.parseInt(value);
		}
	}

	@Override
	protected ArrayList<MyHotelPicture> createContent() {
		return new ArrayList<MyHotelPicture>(10);
	}

	@Override
	protected String CreateUri(Object... args) {
		//LogUtils.info("URl "+String.format("%s?userid=%s&lang=%s&categoryid=%s",IPTVUriUtils.myHotelPictureListHost, args[0], args[1], args[2],args[3]));
		return String.format("%s?userid=%s&lang=%s&id=%s&type=%s&stbid=%s",IPTVUriUtils.myHotelPictureListHost, args[0], args[1], args[2],args[3],TvApplication.stbId);
	}
	
	private void  analyzImageInfo(){
		
	}

	@Override
	protected ArrayList<NameValuePair> getPostArgs() {
		// TODO Auto-generated method stub
		return null;
	}

}
