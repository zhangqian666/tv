package com.iptv.rocky.hwdata.json;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.NameValuePair;

import android.util.JsonReader;

import com.iptv.common.HttpJsonFactoryBase;
import com.iptv.common.data.ChannelDetail;
import com.iptv.rocky.hwdata.IPTVUriUtils;

public class ChannelListJsonFactory extends HttpJsonFactoryBase<ArrayList<ChannelDetail>> {

	@Override
	protected ArrayList<ChannelDetail> AnalysisData(JsonReader reader)
			throws IOException {
		ArrayList<ChannelDetail> list = new ArrayList<ChannelDetail>();
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
					ChannelDetail info = new ChannelDetail();
					while (reader.hasNext()) {
						String name = reader.nextName();
						
						if (name.equals("CHANNELID")) {
							info.CHANNELID = nextInt(reader);
						} else if (name.equals("CHANNELNAME")) {
							info.CHANNELNAME = nextString(reader);
						} else if (name.equals("PICS_PATH")) {
							info.PICS_PATH = nextString(reader);
						} else if (name.equals("ISNVOD")) {
							info.ISNVOD = nextInt(reader);
						} else if (name.equals("ISPLTV")) {
							info.ISPLTV = nextInt(reader);
						} else if (name.equals("PLTVSTATUS")) {
							info.PLTVSTATUS = nextInt(reader);
						} else if (name.equals("ISTVOD")) {
							info.ISTVOD = nextInt(reader);
						} else if (name.equals("TVODSTATUS")) {
							info.TVODSTATUS = nextInt(reader);
						} else if (name.equals("CHANNELINDEX")) {
							info.CHANNELINDEX = nextInt(reader);
						} else if (name.equals("CHANNELTYPE")) {
							info.CHANNELTYPE = nextInt(reader);
                        } else if (name.equals("ISSUBSCRIBED")) {
							info.ISSUBSCRIBED = nextInt(reader);
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
		return IPTVUriUtils.ChannelListByTypeIdHost;
	}

	@Override
	protected ArrayList<NameValuePair> getPostArgs() {
		// TODO Auto-generated method stub
		return null;
	}

}
