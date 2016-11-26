package com.iptv.rocky.hwdata.json;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.NameValuePair;

import android.util.JsonReader;

import com.iptv.common.HttpJsonFactoryBase;
import com.iptv.common.data.SpecialCategoryObj;
import com.iptv.rocky.hwdata.IPTVUriUtils;

public class SpecialCategoryFactory extends
		HttpJsonFactoryBase<ArrayList<SpecialCategoryObj>> {

	@Override
	protected ArrayList<SpecialCategoryObj> AnalysisData(JsonReader reader)
			throws IOException {
		ArrayList<SpecialCategoryObj> list = new ArrayList<SpecialCategoryObj>();
		reader.beginObject();
		while (reader.hasNext()) {
			if (reader.nextName().equals("data")) {
				reader.beginArray();
				while (reader.hasNext()) {
					SpecialCategoryObj scObj = SpecialCategoryObj.build(reader);
					list.add(scObj);
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
		
		/*if(args.length ==1){
			return String.format("%sspecial?version=3.4.4&channel_id=9997&less=videos,bgimg&count=30&page=%s",IPTVUriUtils.CmsHost, args[0]);
		}else if(args.length == 3){*/
			return String.format("%sspecial/list?version=3.4.4&channel_id=9997&less=videos,bgimg&count=30&page=%s&userid=%s&lang=%s",IPTVUriUtils.HotelHost, args[1],args[2],args[3]);
		/*}else{
			return String.format("%sspecial?version=3.4.4&channel_id=9997&less=videos,bgimg&count=30&page=%s",IPTVUriUtils.CmsHost, args[0]);
		}*/
		
	}

	@Override
	protected ArrayList<NameValuePair> getPostArgs() {
		// TODO Auto-generated method stub
		return null;
	}

}
