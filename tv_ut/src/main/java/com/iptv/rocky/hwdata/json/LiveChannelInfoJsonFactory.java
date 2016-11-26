package com.iptv.rocky.hwdata.json;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.NameValuePair;

import android.util.JsonReader;

import com.iptv.common.HttpJsonFactoryBase;
import com.iptv.common.data.LiveChannelInfo;
import com.iptv.common.utils.CommonUtils;
import com.iptv.rocky.hwdata.IPTVUriUtils;

public class LiveChannelInfoJsonFactory extends HttpJsonFactoryBase<LiveChannelInfo> {

	@Override
	protected LiveChannelInfo AnalysisData(JsonReader reader)
			throws IOException {
		LiveChannelInfo info = new LiveChannelInfo();
		
		reader.beginObject();
		while (reader.hasNext()) {
			String name = reader.nextName();
			
			if (name.equals("TVODSTATUS")) {
				info.TVODSTATUS = nextInt(reader);
			} else if (name.equals("PAUSELENGTH")) {
				info.PAUSELENGTH = nextInt(reader);
			} else if (name.equals("ISSUBSCRIBED")) {
				info.ISSUBSCRIBED = nextInt(reader);
			} else if (name.equals("ISTVOD")) {
				info.ISTVOD = nextInt(reader);
			} else if (name.equals("ISPLTV")) {
				info.ISPLTV = nextInt(reader);
			} else if (name.equals("LIVESTATUS")) {
				info.LIVESTATUS = nextInt(reader);
			} else if (name.equals("isSubscribe")) {
				info.isSubscribe = nextInt(reader);
			} else if (name.equals("VIDEOTYPE")) {
				info.VIDEOTYPE = nextString(reader);
			} else if (name.equals("STORELENGTH")) {
				info.STORELENGTH = nextInt(reader);
			} else if (name.equals("INTRODUCTION")) {
				info.INTRODUCTION = nextString(reader);
			} else if (name.equals("PLTVSTATUS")) {
				info.PLTVSTATUS = nextInt(reader);
			} else if (name.equals("CHANNELINDEX")) {
				info.CHANNELINDEX = nextInt(reader);
			} else if (name.equals("AREAID")) {
				reader.beginArray();
				while (reader.hasNext()) {
					info.AREAID.add(nextInt(reader)+"");
				}
				reader.endArray();
			} else if (name.equals("CODE")) {
				info.CODE = nextString(reader);
			} else if (name.equals("SERVICEPORT")) {
				info.SERVICEPORT = nextInt(reader);
			} else if (name.equals("PICTURE")) {
				info.PICTURE = nextString(reader);
            } else if (name.equals("ChannelSDP")) {
				info.ChannelSDP = nextString(reader);
            } else if (name.equals("CHANNELNAME")) {
				info.ChannelName = nextString(reader);
            } else if (name.equals("SERVICEID")) {
				reader.beginArray();
				while (reader.hasNext()) {
					info.SERVICEID.add(nextString(reader));
				}
				reader.endArray();
            } else if (name.equals("PICS_PATH")) {
				info.ChannelLogoURL = nextString(reader);
            } else if (name.equals("SERVICEIP")) {
				info.SERVICEIP = nextString(reader);
            } else if (name.equals("POSTERPATHS")) {
            	reader.beginObject();
				while (reader.hasNext()) {
					String key = reader.nextName();
					ArrayList<String> value = new ArrayList<String>();
					reader.beginArray();
					while (reader.hasNext()) {
						value.add(nextString(reader));
					}
					reader.endArray();
					if (value.size() > 0)
						info.POSTERPATHS.put(CommonUtils.parseInt(key, 0), value);
				 }
				 reader.endObject();
            } else if (name.equals("INTRODUCE")) {
				info.INTRODUCE = nextString(reader);
            } else if (name.equals("ISCPVR")) {
				info.ISCPVR = nextInt(reader);
            } else if (name.equals("NPVRSTATUS")) {
				info.NPVRSTATUS = nextInt(reader);
            } else if (name.equals("ISNPVR")) {
				info.ISNPVR = nextInt(reader);
            } else if (name.equals("DEFINITION")) {
				info.DEFINITION = nextInt(reader);
            } else if (name.equals("MEDIAID")) {
				info.MEDIAID = nextString(reader);
            } else if (name.equals("RECORDLENGTH")) {
				info.RECORDLENGTH = nextInt(reader);
            } else if (name.equals("RATINGID")) {
				info.RATINGID = nextInt(reader);
            } else if (name.equals("ISNVOD")) {
				info.ISNVOD = nextInt(reader);
            } else if (name.equals("NVODSTATUS")) {
				info.NVODSTATUS = nextInt(reader);
			} else {
				reader.skipValue();
			}
		}
		reader.endObject();
		
		return info;
	}

	@Override
	protected String CreateUri(Object... args) {
		return IPTVUriUtils.ChannelInfoHost;
	}

	@Override
	protected ArrayList<NameValuePair> getPostArgs() {
		// TODO Auto-generated method stub
		return null;
	}

}
