package com.iptv.rocky.hwdata.xml;

import java.text.DecimalFormat;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.iptv.common.HttpXmlFactoryBase;
import com.iptv.common.data.EnumType;
import com.iptv.common.data.VodChannel;
import com.iptv.common.data.VodDetailInfo;
import com.iptv.common.utils.CommonUtils;
import com.iptv.rocky.hwdata.IPTVUriUtils;

public class VodDetailInfoFactory extends HttpXmlFactoryBase<VodDetailInfo> {
	
	private VodDetailInfo mCurrentInfo;
	
	private int POSTERPATHS_key = -1;
	private ArrayList<String> POSTER_PATHS;
	private int CASTMAP_key = -1;
	private ArrayList<String> CAST_NAMES;
	
	private EnumType.Platform platform;
	
	@Override
	protected void xmlStartElement(String nodeName, VodDetailInfo content,
			Attributes attributes) throws SAXException {
		if ("POSTER_ID".equals(nodeName)) {
			POSTERPATHS_key = Integer.valueOf(attributes.getValue("KEY"));
			POSTER_PATHS = new ArrayList<String>();
		} else if ("CAST_ID".equals(nodeName)) {
			CASTMAP_key = Integer.valueOf(attributes.getValue("KEY"));
			CAST_NAMES = new ArrayList<String>();
		}
	}
	
	@Override
	protected void xmlEndElement(String nodeName, String value,
			VodDetailInfo content) throws SAXException {
		if ("VODID".equals(nodeName)) {
			mCurrentInfo.VODID =value;
		} else if ("VODNAME".equals(nodeName)) {
			mCurrentInfo.VODNAME = value;
		} else if ("DIRECTOR".equals(nodeName)) {
			mCurrentInfo.DIRECTOR = value;
		} else if ("DEFINITION".equals(nodeName)) {
			mCurrentInfo.DEFINITION = CommonUtils.parseInt(value);
		} else if ("PICPATH".equals(nodeName)) {
			//mCurrentInfo.PICPATH = IPTVUriUtils.Host + value;
			mCurrentInfo.PICPATH =  value;
		} else if ("INTRODUCE".equals(nodeName)) {
			mCurrentInfo.INTRODUCE = value;
		} else if ("PLATFORM".equals(nodeName)) {
			mCurrentInfo.platform = EnumType.Platform.createPlatform(value);	
		} else if ("ISASSESS".equals(nodeName)) {
			mCurrentInfo.ISASSESS = CommonUtils.parseInt(value);
		} else if ("ISSITCOM".equals(nodeName)) {
			mCurrentInfo.ISSITCOM = CommonUtils.parseInt(value);
		} else if ("SITCOMNUM".equals(nodeName)) {
			mCurrentInfo.SITCOMNUM = CommonUtils.parseInt(value);
		} else if ("ID".equals(nodeName)) {
			mCurrentInfo.SERVICEID.add(value);
		} else if ("ELAPSETIME".equals(nodeName)) {
			mCurrentInfo.ELAPSETIME = CommonUtils.parseInt(value);
		} else if ("VODPRICE".equals(nodeName)) {
			double price = Double.parseDouble(value);
			DecimalFormat vf= new DecimalFormat("#0.00");
			mCurrentInfo.VODPRICE = vf.format(price);
			mCurrentInfo.price = price;
		} else if ("POSTER_PATH".equals(nodeName)) {
			POSTER_PATHS.add(IPTVUriUtils.Host + value);
		} else if ("POSTER_ID".equals(nodeName)) {
			if (POSTERPATHS_key != -1 && POSTER_PATHS != null && POSTER_PATHS.size() > 0)
			{
				mCurrentInfo.POSTERPATHS.put(POSTERPATHS_key, POSTER_PATHS);
			}
		} else if ("NAME".equals(nodeName)) {
			CAST_NAMES.add(value);
		} else if ("CAST_ID".equals(nodeName)) {
			if (CASTMAP_key != -1 && CAST_NAMES != null && CAST_NAMES.size() > 0)
			{
				mCurrentInfo.CASTMAP.put(CASTMAP_key, CAST_NAMES);
			}
		} else if ("SUBVODID".equals(nodeName)) {
			mCurrentInfo.SUBVODIDLIST.add(new VodChannel(value));
		} else if ("SUBVODNUM".equals(nodeName)) {
			mCurrentInfo.SUBVODNUMLIST.add(CommonUtils.parseInt(value));
		} else if ("VOD".equals(nodeName)) {
			
			mCurrentInfo.platform = platform;
			if(mCurrentInfo.platform == EnumType.Platform.HUAWEI){
				mCurrentInfo.PICPATH = IPTVUriUtils.Host + mCurrentInfo.PICPATH;
			}else if(mCurrentInfo.platform == EnumType.Platform.HOTEL){
				mCurrentInfo.PICPATH = IPTVUriUtils.HotelHost + mCurrentInfo.PICPATH;
			}else{
				mCurrentInfo.PICPATH = IPTVUriUtils.Host + mCurrentInfo.PICPATH;
			}
			if (mCurrentInfo.SUBVODIDLIST.size() == mCurrentInfo.SUBVODNUMLIST.size() && mCurrentInfo.ISSITCOM == 1)
			{
				for (int i = 0; i < mCurrentInfo.SUBVODIDLIST.size(); i++)
				{
					mCurrentInfo.SUBVODIDLIST.get(i).nNumber = mCurrentInfo.SUBVODNUMLIST.get(i);
				}
			}
		}
	}

	@Override
	protected VodDetailInfo createContent() {
		if (mCurrentInfo == null) {
			mCurrentInfo = new VodDetailInfo();
		}
		return mCurrentInfo;
	}

	@Override
	protected String CreateUri(Object... args) {
		if(args[0] == null){
			platform =EnumType.Platform.HUAWEI;
		}else{
			platform = EnumType.Platform.createPlatform(args[0].toString());
		}
		String url =null;
		if(platform == EnumType.Platform.HUAWEI){
			url =String.format("%s&vodId=%s",IPTVUriUtils.VodDetailInfoHost, args[1]);
		}else if(platform == EnumType.Platform.HOTEL){
			url =String.format("%s?vodId=%s",IPTVUriUtils.HotelProgramDetailHost, args[1]);
		}
		return url;
	}

	@Override
	protected ArrayList<NameValuePair> getPostArgs() {
		return null;
	}
}
