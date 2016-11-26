package com.iptv.rocky.hwdata.json;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.NameValuePair;

import android.content.Context;
import android.util.JsonReader;

import com.iptv.common.HttpJsonFactoryBase;
import com.iptv.common.data.EnumType;
import com.iptv.common.data.PortalChannels;
import com.iptv.common.data.PortalLiveChannel;
import com.iptv.common.data.PortalLiveType;
import com.iptv.rocky.hwdata.IPTVUriUtils;
import com.iptv.rocky.hwdata.local.LiveTypeLocalFactory;

public class PortalLiveChannelsJsonFactory extends HttpJsonFactoryBase<PortalChannels> {
	
	private Context mContext;
	
	public PortalLiveChannelsJsonFactory(Context context)	{
		mContext = context;
	}

	@Override
	protected PortalChannels AnalysisData(JsonReader reader)
			throws IOException {
		PortalChannels obj = new PortalChannels();
		
		int COUNTTOTAL = 0;
		int RETCODE = 0;
		
		reader.beginObject();
		while (reader.hasNext()) {
			if (reader.nextName().equals("RETCODE")) {
				RETCODE = nextInt(reader);
			}
			
			if (reader.nextName().equals("CHANNELCOUNTTOTAL")) {
				COUNTTOTAL = nextInt(reader);
			}
			
			if (reader.nextName().equalsIgnoreCase("channellist")) {	// ChannelList
				reader.beginArray();
				while (reader.hasNext()) {
					reader.beginObject();
					PortalLiveChannel info = new PortalLiveChannel();
					while (reader.hasNext()) {
						String name = reader.nextName();
						if (name.equals("id")) {
							info.id = nextInt(reader);
						} else if (name.equals("bgimg")) {
							info.bg = nextString(reader);
						} else if (name.equals("title")) {
							info.ChannelName = nextString(reader);
						} else if (name.equals("content_type")) {
							info.content_type = EnumType.ContentType.createType(nextInt(reader));
						} else if (name.equals("layout_type")) {
							info.layout_type = EnumType.LayoutType.createType(nextInt(reader));
						} else if (name.equals("channel_id")) {
							info.ChannelID = nextString(reader);
						} else {
							reader.skipValue();
						}
					}
					
					if (info.content_type == null) {
						info.content_type = EnumType.ContentType.UNKNOW;
					}
					if (info.layout_type == null) {
						info.layout_type = EnumType.LayoutType.UNKNOW;
					}
					
					obj.lstPortalLiveChannel.add(info);
					reader.endObject();
				}
				reader.endArray();
			} 
			
			if (reader.nextName().equalsIgnoreCase("categorylist")) {	// CategoryList
				reader.beginArray();
				while (reader.hasNext()) {
					reader.beginObject();
					PortalLiveType info = new PortalLiveType();
					while (reader.hasNext()) {
						String name = reader.nextName();
						if (name.equals("id")) {
							info.id = nextInt(reader);
						} else if (name.equals("icon")) {
							info.icon = nextString(reader);
						} else if (name.equals("bgimg")) {
							info.bg = nextString(reader);
						} else if (name.equals("title")) {
							info.title = nextString(reader);
						} else if (name.equals("content_type")) {
							info.content_type = EnumType.ContentType.createType(nextInt(reader));
						} else if (name.equals("layout_type")) {
							info.layout_type = EnumType.LayoutType.createType(nextInt(reader));
						} else if (name.equals("typeId")) {
							info.typeId = nextInt(reader);
						} else if (name.equalsIgnoreCase("channelList")) {
							reader.beginArray();
							while (reader.hasNext()) {
								info.lstChannelIds.add(nextString(reader));
							}
							reader.endArray();
						} else {
							reader.skipValue();
						}
					}
					
					if (info.content_type == null) {
						info.content_type = EnumType.ContentType.UNKNOW;
					}
					if (info.layout_type == null) {
						info.layout_type = EnumType.LayoutType.UNKNOW;
					}
					
					obj.lstPortalLiveType.add(info);
					reader.endObject();
				}
				reader.endArray();
			} else {
				reader.skipValue();
			}
		}
		reader.endObject();
		
		// ���浽��ݿ�
		LiveTypeLocalFactory liveTypeLocalFactory = new LiveTypeLocalFactory(mContext);
		liveTypeLocalFactory.insertLiveTypeInfos(obj.lstPortalLiveType);
		
		return obj;
	}

	@Override
	protected String CreateUri(Object... args) {
		return IPTVUriUtils.PortalChannelsHost;
	}

	@Override
	protected ArrayList<NameValuePair> getPostArgs() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
