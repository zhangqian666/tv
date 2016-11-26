package com.iptv.rocky.hwdata.json;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.NameValuePair;

import android.util.JsonReader;

import com.iptv.common.HttpJsonFactoryBase;
import com.iptv.common.data.VodChannel;
import com.iptv.rocky.hwdata.IPTVUriUtils;

public class VodChannelListJsonFactory extends HttpJsonFactoryBase<ArrayList<VodChannel>> {

	@Override
	protected ArrayList<VodChannel> AnalysisData(JsonReader reader)
			throws IOException {
		ArrayList<VodChannel> list = new ArrayList<VodChannel>();
		int COUNTTOTAL = 0;
		int RETCODE = 0;
		
		reader.beginObject();
		while (reader.hasNext()) {
			if (reader.nextName().equals("COUNTTOTAL")) {
				COUNTTOTAL = nextInt(reader);
			}
			
			if (reader.nextName().equals("RETCODE")) {
				RETCODE = nextInt(reader);
			}
			
			if (reader.nextName().equals("LIST")) {
				reader.beginArray();
				while (reader.hasNext()) {
					reader.beginObject();
					VodChannel info = new VodChannel();
					while (reader.hasNext()) {
						String name = reader.nextName();
						
						if (name.equals("VODID")) {
							info.VODID = nextString(reader);
						} else if (name.equals("VODNAME")) {
							info.VODNAME = nextString(reader);
						} else if (name.equals("PICPATH")) {
							info.PICPATH = nextString(reader);
						} else if (name.equals("DEFINITION")) {
							info.DEFINITION = nextInt(reader);
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
		return IPTVUriUtils.VodChannelListHost;
	}

	@Override
	protected ArrayList<NameValuePair> getPostArgs() {
		// TODO Auto-generated method stub
		return null;
	}

}
