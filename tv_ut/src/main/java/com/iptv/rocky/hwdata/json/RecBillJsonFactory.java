package com.iptv.rocky.hwdata.json;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.NameValuePair;

import com.iptv.common.HttpJsonFactoryBase;
import com.iptv.common.data.RecBill;
import com.iptv.rocky.hwdata.IPTVUriUtils;

import android.util.JsonReader;

public class RecBillJsonFactory extends HttpJsonFactoryBase<ArrayList<RecBill>> {

	@Override
	protected ArrayList<RecBill> AnalysisData(JsonReader reader)
			throws IOException {
		ArrayList<RecBill> list = new ArrayList<RecBill>();
		
		reader.beginObject();
		while(reader.hasNext()){
			String rootName=reader.nextName();
			if ("lists".equals(rootName)) {
				reader.beginArray();
				while (reader.hasNext()) {
					reader.beginObject();
					RecBill info = new RecBill();
					while (reader.hasNext()) {
						String name = reader.nextName();
//						if ("TITLE".equals(name)) {
						if ("name".equals(name)) {
							info.title = nextString(reader).toString().replaceAll("&#37;", "%");
//						} else if ("PROGID".equals(name)) {
						} else if ("progId".equals(name)) {
							info.programIdZte = nextString(reader);
//						} else if ("BEGINTIME".equals(name)) {
						} else if ("startTime".equals(name)) {
							info.beginTime = nextString(reader);
//						} else if ("ENDTIME".equals(name)) {
						} else if ("endTime".equals(name)) {
							info.endTime = nextString(reader);
//						} else if ("BEGINDATE".equals(name)) {
						} else if ("beginDate".equals(name)) {
							info.beginDate = nextString(reader);
						} else if ("STATUS".equals(name)) {
							info.recStatus = nextInt(reader);
						} else {
							reader.skipValue();
						}
					}
					list.add(info);
					reader.endObject();
				}
				reader.endArray();
			}else{
				reader.skipValue();
			}
		}
		reader.endObject();
		return list;
	}

	@Override
	protected String CreateUri(Object... args) {
		return String.format("%s?CHANNELID=%s&date=%s",IPTVUriUtils.getChanBill,args[0],args[1]);
	}

	@Override
	protected ArrayList<NameValuePair> getPostArgs() {
		// TODO Auto-generated method stub
		return null;
	}

}
