package com.iptv.rocky.hwdata.json;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.NameValuePair;

import android.text.TextUtils;
import android.util.JsonReader;

import com.iptv.common.HttpJsonFactoryBase;
import com.iptv.common.data.EnumType;
import com.iptv.common.data.VodTop;
import com.iptv.common.utils.AtvUtils;
import com.iptv.rocky.hwdata.IPTVUriUtils;

public class VodTopJsonFactory extends HttpJsonFactoryBase<ArrayList<VodTop>> {

	@Override
	protected ArrayList<VodTop> AnalysisData(JsonReader reader)
			throws IOException {
		ArrayList<VodTop> list = new ArrayList<VodTop>();
		
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
			
			if (reader.nextName().toLowerCase().equals("vodtypelist")) {	// VodTypeList
				reader.beginArray();
				while (reader.hasNext()) {
					reader.beginObject();
					VodTop info = new VodTop();
					while (reader.hasNext()) {
						String name = reader.nextName();
						if (name.equals("id")) {
							info.id = nextInt(reader);
						} else if (name.equals("bgimg")) {
							info.bg = nextString(reader);
						} else if (name.equals("color")) {
							int color = nextInt(reader);
							if (TextUtils.isEmpty(info.bg)) {
								info.bg = AtvUtils.getMetroItemBackground(color);
							}
						} else if (name.equals("type_id")) {
							info.type_id = nextString(reader);
						} else if (name.equals("title")) {
							info.title = nextString(reader);
						} else if (name.equals("content_type")) {
							info.content_type = EnumType.ContentType.createType(nextInt(reader));
						} else if (name.equals("layout_type")) {
							info.layout_type = EnumType.LayoutType.createType(nextInt(reader));
						} else if (name.equals("icon")) {
							info.icon = nextString(reader);
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
		return IPTVUriUtils.VodTopHost;
	}

	@Override
	protected ArrayList<NameValuePair> getPostArgs() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
