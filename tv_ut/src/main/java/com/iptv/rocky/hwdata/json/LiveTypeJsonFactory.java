package com.iptv.rocky.hwdata.json;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.NameValuePair;

import android.util.JsonReader;

import com.iptv.common.HttpJsonFactoryBase;
import com.iptv.common.data.LiveType;
import com.iptv.rocky.hwdata.IPTVUriUtils;

public class LiveTypeJsonFactory extends HttpJsonFactoryBase<ArrayList<LiveType>> {

	@Override
	protected ArrayList<LiveType> AnalysisData(JsonReader reader)
			throws IOException {
		ArrayList<LiveType> list = new ArrayList<LiveType>();
		
		int COUNTTOTAL = 0;
		int RETCODE = 0;
		
		reader.beginObject();
		while (reader.hasNext()) {
			if (reader.nextName().equals("RETCODE")) {
				RETCODE = nextInt(reader);
			}
			
			if (reader.nextName().equals("COUNTTOTAL")) {
				COUNTTOTAL = nextInt(reader);
			}
			
			if (reader.nextName().toLowerCase().equals("list")) {	// LIST
				reader.beginArray();
				while (reader.hasNext()) {
					reader.beginObject();
					LiveType info = new LiveType();
					while (reader.hasNext()) {
						String name = reader.nextName();
						if (name.equals("typeId")) {
							info.typeId = nextInt(reader);
						} else if (name.equals("channelType")) {
							info.title = nextString(reader);
						} else if (name.equals("channelList")) {
							ArrayList<String> lstIds = new ArrayList<String>();
							reader.beginArray();
							while (reader.hasNext()) {
								lstIds.add(nextString(reader));
							}
							info.lstChannelIds = lstIds;
							reader.endArray();
						} else {
							reader.skipValue();
						}
					}
					
					list.add(info);
					reader.endObject();
				}
				reader.endArray();
			} else {
				reader.skipValue();
			}
		}
		reader.endObject();
		
		return list;
	}

	@Override
	protected String CreateUri(Object... args) {
		return IPTVUriUtils.ChannelTypeHost;
	}

	@Override
	protected ArrayList<NameValuePair> getPostArgs() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
