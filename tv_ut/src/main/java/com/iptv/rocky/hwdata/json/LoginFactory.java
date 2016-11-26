package com.iptv.rocky.hwdata.json;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.NameValuePair;

import android.util.JsonReader;

import com.iptv.common.HttpJsonFactoryBase;
import com.iptv.common.data.LoginResult;
import com.iptv.rocky.hwdata.IPTVUriUtils;

public class LoginFactory extends HttpJsonFactoryBase<LoginResult> {

	@Override
	protected LoginResult AnalysisData(JsonReader reader)
			throws IOException {
		LoginResult info = new LoginResult();
		
		reader.beginObject();
		while (reader.hasNext()) {
			String name = reader.nextName();
			
			if (name.equals("result")) {
				info.setResult( nextInt(reader));
			} else if (name.equals("reason")) {
				info.setReason( nextString(reader));
			} else if(name.equals("hotelId")){
				info.setHotelId(nextString(reader));
			}else if(name.equals("hotelColumnEnabledList")){
				reader.beginArray();
			
			}else{
				reader.skipValue();
			}
		}
		reader.endObject();
		
		return info;
	}

	@Override
	protected String CreateUri(Object... args) {
		return String.format("%s?userid=%s", IPTVUriUtils.hotelLoginHost, args[0]);
	}

	@Override
	protected ArrayList<NameValuePair> getPostArgs() {
		return null;
	}

}
