package com.iptv.rocky.hwdata.json;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.NameValuePair;

import android.util.JsonReader;

import com.iptv.common.HttpJsonFactoryBase;
import com.iptv.common.data.LiveChannelInfo;
import com.iptv.common.data.RecChan;
import com.iptv.common.utils.CommonUtils;
import com.iptv.common.utils.LogUtils;
import com.iptv.rocky.hwdata.IPTVUriUtils;

public class RecChanJsonFactory extends HttpJsonFactoryBase<ArrayList<RecChan>> {

	@Override
	protected ArrayList<RecChan> AnalysisData(JsonReader reader)
			throws IOException {
		ArrayList<RecChan> list = new ArrayList<RecChan>();
		Map<String,RecChan>  map=new HashMap<String,RecChan>();
		
		reader.beginObject();
		while (reader.hasNext()) {
			String rootName=reader.nextName();
			 if (rootName.equals("ITEM")) {	
				reader.beginArray();
				while (reader.hasNext()) {
					reader.beginObject();
					RecChan info = new RecChan();
					while (reader.hasNext()) {
						String name = reader.nextName();
						 if (name.equals("CHANNELID")) {
							info.CHANNELID = nextInt(reader);
						} else if (name.equals("CHANNELCODEZTE")) {
							info.CHANNELIDZTE = nextString(reader);
			            }else if (name.equals("CHANNELINDEX")) {
							info.CHANNELINDEX = String.format("%03d", CommonUtils.parseInt(nextString(reader)));
			            } else if (name.equals("CHANNELNAME")) {
							info.CHANNELNAME = nextString(reader);
			            } else if (name.equals("RECORDLENGTH")) {
							info.RECORDLENGTH = CommonUtils.parseInt(nextString(reader));
			            } else if (name.equals("DEFINITION")) {
							info.DEFINITION =  CommonUtils.parseInt(nextString(reader));
			            } else if(name.equals("channeltype")){
			            	nextString(reader);
			            } else if(name.equals("columncode")){
			            	nextString(reader);
			            } else if(name.equals("columnname")){
			            	nextString(reader);
			            } else {
							reader.skipValue();
						}
					}
					if (!map.containsKey(info.CHANNELIDZTE)) {
						map.put(info.CHANNELIDZTE, info);
						list.add(info);
					}
					
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
//		return IPTVUriUtils.RecChanHost;
		return IPTVUriUtils.getChannelList;
	}

	@Override
	protected ArrayList<NameValuePair> getPostArgs() {
		// TODO Auto-generated method stub
		return null;
	}

}
