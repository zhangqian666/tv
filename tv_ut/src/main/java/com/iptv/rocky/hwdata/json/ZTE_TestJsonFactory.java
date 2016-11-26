package com.iptv.rocky.hwdata.json;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.NameValuePair;

import android.util.JsonReader;

import com.iptv.common.HttpJsonFactoryBase;
import com.iptv.common.data.EnumType;
import com.iptv.common.data.VodSec;
import com.iptv.common.utils.LogUtils;
import com.iptv.rocky.hwdata.IPTVUriUtils;

public class ZTE_TestJsonFactory extends HttpJsonFactoryBase<ArrayList<VodSec>>{

	@Override
	protected ArrayList<VodSec> AnalysisData(JsonReader reader)
			throws IOException {
		ArrayList<VodSec> list = new ArrayList<VodSec>();
		int COUNTTOTAL = 0;
		int RETCODE = 0;
		reader.beginObject();
		while (reader.hasNext()) {
			String rootName =reader.nextName();
			if (rootName.equals("COUNTTOTAL")) {
				COUNTTOTAL = nextInt(reader);
			}else if (rootName.equals("RETCODE")) {
				RETCODE = nextInt(reader);
			}else if (rootName.equals("ITEM")) {
				reader.beginArray();
				while (reader.hasNext()) {
					reader.beginObject();
					VodSec info = new VodSec();
					while (reader.hasNext()) {
						String name = reader.nextName();
//						LogUtils.e("VodThdJsonFactory", "name=  "+name);
						if (name.equals("TYPE_ID")) {
							info.type_id = nextString(reader);
							LogUtils.e("VodThdJsonFactory", "name=  "+name);
							LogUtils.e("info.type_id", "info.type_id=  "+info.type_id);
						}else if (name.equals("TYPE_NAME")) {
							info.title = nextString(reader);
							LogUtils.e("VodThdJsonFactory", "name=  "+name);
							LogUtils.e("info.title", "info.title=  "+info.title);
//						}else if (name.equals("TYPE_PICPATH")) {
//							info.bg = nextString(reader);
//							LogUtils.e("info.bg", "info.bg=  "+info.bg);
//						}else if (name.equals("SUBJECT_TYPE")) {
//							info.subjectType = nextInt(reader);
//							LogUtils.e("info.subjectType", "info.subjectType=  "+info.subjectType);
//						}else if (name.equals("LAYOUT_TYPE")) {
//							info.layout_type =EnumType.LayoutType.createType(nextInt(reader));
//							LogUtils.e("info.layout_type", "info.layout_type=  "+info.layout_type);
//						}else if (name.equals("SUB_CONTENT_TYPE")) {
//							info.sub_content_type =EnumType.SubContentType.createType(nextInt(reader));
//							LogUtils.e("info.sub_content_type", "info.sub_content_type=  "+info.sub_content_type);
//						}else if (name.equals("CONTENT_TYPE")) {
//							info.content_type =EnumType.ContentType.createType(nextInt(reader));
//							LogUtils.e("info.content_type", "info.content_type=  "+info.content_type);
						}else{
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
	protected ArrayList<NameValuePair> getPostArgs() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String CreateUri(Object... args) {
		// TODO Auto-generated method stub
		return "http://10.0.5.220:8080/iptvepg/frame1021/ut/zte_GetAllChannelProgBill.jsp";
	}
}
