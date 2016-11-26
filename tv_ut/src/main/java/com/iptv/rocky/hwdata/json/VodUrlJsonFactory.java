package com.iptv.rocky.hwdata.json;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.NameValuePair;

import android.util.JsonReader;

import com.iptv.common.HttpJsonFactoryBase;
import com.iptv.common.data.EnumType;
import com.iptv.common.data.VodUrl;
import com.iptv.common.utils.LogUtils;
import com.iptv.rocky.hwdata.IPTVUriUtils;

public class VodUrlJsonFactory extends HttpJsonFactoryBase<VodUrl>{
	private EnumType.Platform platform;
	@Override
	protected VodUrl AnalysisData(JsonReader reader) throws IOException {
		VodUrl info = new VodUrl();
		reader.beginObject();
		while (reader.hasNext()) {
			String name = reader.nextName();
			if("RTSPURL".equals(name)){
				info.RTSPURL=nextString(reader);
			}else if ("PLAYURL".equals(name)) {
				info.RTSPURL=nextString(reader);
			}else{
				reader.skipValue();
			}
		}
		return info;
	}

	@Override
	protected ArrayList<NameValuePair> getPostArgs() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String CreateUri(Object... args) {
		platform = null;
		if (args[0] != null) {
			platform = EnumType.Platform.createPlatform(args[0].toString());
		} else {
			platform = EnumType.Platform.HUAWEI;
		}
		
		String url = null;
		if (platform == EnumType.Platform.HUAWEI) {
			url = String.format("%s?beginTime=0&endTime=20000&progId=%s",IPTVUriUtils.VodTriggerPlayUrlHostJson, args[1]);
		} else if (platform == EnumType.Platform.HOTEL) {
			url = String.format("%s?progId=%d", IPTVUriUtils.HotelPlayUrlHost, args[1]);
		} else {
			url = String.format("%s?PROGID=%s&mediaservice=%s&authid=%s", IPTVUriUtils.vodGetPlayUrlJson, args[1],args[2],args[3]);
		}
		
		return url;
	}

}
