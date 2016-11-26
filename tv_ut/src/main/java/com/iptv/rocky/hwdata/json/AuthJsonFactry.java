package com.iptv.rocky.hwdata.json;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;

import org.apache.http.NameValuePair;

import com.iptv.common.HttpJsonFactoryBase;
import com.iptv.common.data.Auth;
import com.iptv.common.utils.LogUtils;
import com.iptv.rocky.hwdata.IPTVUriUtils;

import android.util.JsonReader;

public class AuthJsonFactry extends HttpJsonFactoryBase<Auth>{

	@Override
	protected Auth AnalysisData(JsonReader reader) throws IOException {
		
		Auth info = new Auth();
		reader.beginObject();
		while (reader.hasNext()) {
			String name = reader.nextName();
			if (name.equals("_error_code")) {
				info._error_code=nextString(reader);
			} else if (name.equals("_return_message")) {
				info._return_message = nextString(reader);
			} else if (name.equals("AuthorizationID")) {
				info.AuthorizationID = nextString(reader);
			} else {
				reader.skipValue();
			}
		}
		reader.endObject();

		return info;
	}

	@Override
	protected ArrayList<NameValuePair> getPostArgs() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String CreateUri(Object... args) {
		// TODO Auto-generated method stub
		return String.format("%s?columncode=%s&programcode=%s&contentcode=%s&definition=%s&contenttype=%s&seriesProgramcode=%s",IPTVUriUtils.VodAuthJson,args[0],args[1],args[2],args[3],args[4],args[5]);
	}

}
