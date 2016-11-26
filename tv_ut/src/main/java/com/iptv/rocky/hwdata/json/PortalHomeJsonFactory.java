package com.iptv.rocky.hwdata.json;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.NameValuePair;

import android.util.JsonReader;

import com.iptv.common.HttpJsonFactoryBase;
import com.iptv.common.data.EnumType;
import com.iptv.common.data.PortalHome;
import com.iptv.rocky.hwdata.IPTVUriUtils;

public class PortalHomeJsonFactory extends HttpJsonFactoryBase<ArrayList<PortalHome>> {

	@Override
	protected ArrayList<PortalHome> AnalysisData(JsonReader reader)
			throws IOException {
		ArrayList<PortalHome> list = new ArrayList<PortalHome>();
		
		reader.beginObject();
		while (reader.hasNext()) {
			if (reader.nextName().equals("data")) {
				reader.beginArray();
				while (reader.hasNext()) {
					reader.beginObject();
					PortalHome info = new PortalHome();
					while (reader.hasNext()) {
						String name = reader.nextName();
						if (name.equals("id")) {
							info.id = nextInt(reader);
						} else if (name.equals("bgimg")) {
							info.bg = nextString(reader);
						} else if (name.equals("content_id")) {
							info.content_id = nextString(reader);
						} else if (name.equals("title")) {
							info.title = nextString(reader);
						} else if (name.equals("content_type")) {
							info.content_type = EnumType.ContentType.createType(nextInt(reader));
						} else if (name.equals("layout_type")) {
							info.layout_type = EnumType.LayoutType.createType(nextInt(reader));
						} else if (name.equals("imgs")) {
							ArrayList<String> imgs = new ArrayList<String>(2);
							reader.beginArray();
							while (reader.hasNext()) {
								imgs.add(nextString(reader));
							}
							info.imgs = imgs;
							reader.endArray();
						} else {
							reader.skipValue();
						}
					}
					
					if (info.content_type == null) {
						if (info.imgs != null) {
							info.content_type = EnumType.ContentType.HOME_SPECIAL;
						} else {
							info.content_type = EnumType.ContentType.VIDEO;
						}
					}
					if (info.layout_type == null) {
						info.layout_type = EnumType.LayoutType.UNKNOW;
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
		return IPTVUriUtils.PortalHomeHost;
	}

	@Override
	protected ArrayList<NameValuePair> getPostArgs() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
