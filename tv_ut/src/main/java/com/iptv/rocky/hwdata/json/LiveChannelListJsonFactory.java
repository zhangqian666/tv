package com.iptv.rocky.hwdata.json;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.NameValuePair;

import android.util.JsonReader;

import com.iptv.common.HttpJsonFactoryBase;
import com.iptv.common.data.LiveChannel;
import com.iptv.rocky.hwdata.IPTVUriUtils;

public class LiveChannelListJsonFactory extends HttpJsonFactoryBase<ArrayList<LiveChannel>> {

	@Override
	protected ArrayList<LiveChannel> AnalysisData(JsonReader reader)
			throws IOException {
		ArrayList<LiveChannel> list = new ArrayList<LiveChannel>();
		int totalcount = 0;
		
		reader.beginObject();
		while (reader.hasNext()) {
			if (reader.nextName().equals("totalcount")) {
				totalcount = nextInt(reader);
			}
			
			if (reader.nextName().equals("channels")) {
				reader.beginArray();
				while (reader.hasNext()) {
					reader.beginObject();
					LiveChannel info = new LiveChannel();
					while (reader.hasNext()) {
						String name = reader.nextName();
						
						if (name.equals("PositionY")) {
							info.PositionY = nextInt(reader);
						} else if (name.equals("PositionX")) {
							info.PositionX = nextInt(reader);
						} else if (name.equals("Interval")) {
							info.Interval = nextInt(reader);
						} else if (name.equals("ChannelPurchased")) {
							info.ChannelPurchased = nextInt(reader) == 1 ? true : false;
						} else if (name.equals("Lasting")) {
							info.Lasting = nextInt(reader);
						} else if (name.equals("UserChannelID")) {
							info.UserChannelID = nextInt(reader);
						} else if (name.equals("TimeShift")) {
							info.TimeShift = nextInt(reader) == 1 ? true : false;
						} else if (name.equals("BeginTime")) {
							info.BeginTime = nextInt(reader);
						} else if (name.equals("ChannelID")) {
							info.ChannelID = nextString(reader);
						} else if (name.equals("ChannelType")) {
							info.ChannelType = nextInt(reader);
                        } else if (name.equals("ChannelSDP")) {
							info.ChannelSDP = nextString(reader);
                        } else if (name.equals("ChannelName")) {
							info.ChannelName = nextString(reader);
                        } else if (name.equals("ChannelURL")) {
							info.ChannelURL = nextString(reader);
                        } else if (name.equals("ChannelLogoURL")) {
							info.ChannelLogoURL = nextString(reader);
                        } else if (name.equals("TimeShiftURL")) {
							info.TimeShiftURL = nextString(reader);
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
		return IPTVUriUtils.ChannelListHost;
	}

	@Override
	protected ArrayList<NameValuePair> getPostArgs() {
		// TODO Auto-generated method stub
		return null;
	}

}
