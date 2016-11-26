package com.iptv.rocky.hwdata.json;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.protocol.HTTP;

import com.iptv.common.HttpJsonFactoryBase;
import com.iptv.common.update.VersionInfo;
import com.iptv.common.utils.LogUtils;
import com.iptv.rocky.hwdata.IPTVUriUtils;

import android.util.JsonReader;

public class CheckUpdateFactory extends HttpJsonFactoryBase<VersionInfo> {
	
	private VersionInfo mCurrentInfo;
	

	
	@Override
	protected VersionInfo AnalysisData(JsonReader reader) throws IOException {
		
	
		VersionInfo info = new VersionInfo();
		
		reader.beginObject();
		while (reader.hasNext()) {
		
			String name = reader.nextName();
			
			if (name.equals("canUpdate")) {
				
				int canUpdate = nextInt(reader);
				LogUtils.debug("canUpdate:"+canUpdate);
				info.setIsUpdate(canUpdate==1?true:false);
			} else if (name.equals("versionCode")) {
				info.setVersionCode(nextInt(reader));
			} else if (name.equals("versionName")) {
				info.setVersionName(nextString(reader));
			} else if (name.equals("size")) {
				info.setSize(nextString(reader));
			} else if (name.equals("updateLog")) {
				info.setUpdateLog(nextString(reader));
			} else if (name.equals("updateUrl")) {
				info.setUpdateUrl(nextString(reader));
			} else if (name.equals("mode")) {
				info.setMode(nextInt(reader));
			} else {
				reader.skipValue();
			}
		};
		reader.endObject();
		return info;
	}


	/**
	 * 0 : channel
	 * 1 : deviceid
	 * 2 : devicetype
	 * 3 : osv
	 * 4 : sv
	 * 
	 */
	@Override
	protected String CreateUri(Object... args) {
		String deviceType = "";
		try {
			deviceType = URLEncoder.encode((String)args[2], HTTP.UTF_8);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return String.format("%s?channel=%s&deviceid=%s&devicetype=%s&osv=%s&sv=%s&platform=%s&stbid=%s&maker=%s",IPTVUriUtils.CheckIptvUpDateHost, args[0], args[1], deviceType, args[3], args[4],args[5],args[6],args[7]);
	}

	@Override
	protected ArrayList<NameValuePair> getPostArgs() {
		return null;
	}

}
