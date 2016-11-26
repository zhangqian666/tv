package com.iptv.rocky.hwdata.json;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;

import android.util.JsonReader;
import com.iptv.common.HttpJsonFactoryBase;
import com.iptv.common.data.EnumType;
import com.iptv.common.data.SpecialItemObj;
import com.iptv.common.data.SpecialObj;
import com.iptv.common.utils.LogUtils;
import com.iptv.rocky.hwdata.IPTVUriUtils;

public class SpecialDetailFactory extends HttpJsonFactoryBase<SpecialObj> {

	@Override
	protected SpecialObj AnalysisData(JsonReader reader) throws IOException {
		SpecialObj sObj = new SpecialObj();
		reader.beginObject();
		while (reader.hasNext()) {
			if (reader.nextName().equals("data")) {
				reader.beginObject();
				while (reader.hasNext()) {
					String name = reader.nextName();
					if (name.equals("bgimg")) {
						sObj.setBgimg(IPTVUriUtils.HotelHost+reader.nextString());
					} else if (name.equals("categoryId")) {
						sObj.setCategoryId(reader.nextString());	
					} else if (name.equals("title")) {
						sObj.setTitle(reader.nextString());
					} else if (name.equals("platform")) {
						sObj.setPlatform(EnumType.Platform.createPlatform(reader.nextString()));
					} else if (name.equals("cover_img")) {
						sObj.setCover_image(reader.nextString());
					} else if (name.equals("id")) {
						sObj.setId(reader.nextString());
					} else if (name.equals("videos")) {
						List<SpecialItemObj> list = new ArrayList<SpecialItemObj>();
						reader.beginArray();
						while (reader.hasNext()) {
							SpecialItemObj sItemObj = SpecialItemObj.build(reader);
							list.add(sItemObj);
						}
						sObj.setSpecialItemObjs(list);
						reader.endArray();
					} else {
						reader.skipValue();
					}
				}
				reader.endObject();
			} else {
				reader.skipValue();
			}
		}
		reader.endObject();
		return sObj;
	}

	@Override
	protected String CreateUri(Object... args) {
//		return String.format("%sspecial/%s?version=%s&channel_id=%s",
//				IPTVUriUtils.CmsHost, args[0], IPTVUriUtils.Version, IPTVUriUtils.Channel);
		
		return String.format("%sspecial/special/%s",IPTVUriUtils.HotelHost, args[0]);
		
		//return "http://tv.api.pptv.com/special/171?version=3.4.4&channel_id=9997";
	}

	@Override
	protected ArrayList<NameValuePair> getPostArgs() {
		// TODO Auto-generated method stub
		return null;
	}

}
