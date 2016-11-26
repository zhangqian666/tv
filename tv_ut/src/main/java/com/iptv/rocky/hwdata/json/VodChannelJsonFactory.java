package com.iptv.rocky.hwdata.json;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import org.apache.http.NameValuePair;
import android.util.JsonReader;
import com.iptv.common.HttpJsonFactoryBase;
import com.iptv.common.data.EnumType;
import com.iptv.common.data.VodChannel;
import com.iptv.common.utils.CommonUtils;
import com.iptv.common.utils.LogUtils;
import com.iptv.rocky.hwdata.IPTVUriUtils;
import com.iptv.rocky.model.TvApplication;

public class VodChannelJsonFactory extends HttpJsonFactoryBase<ArrayList<VodChannel>>{

	private VodChannel info;
	public int counttotal;
	public EnumType.Platform platformEach;

	@Override
	protected ArrayList<VodChannel> AnalysisData(JsonReader reader)
			throws IOException {
		ArrayList<VodChannel> list = new ArrayList<VodChannel>();
		int COUNTTOTAL = 0;
		int RETCODE = 0;
		
		reader.beginObject();
		while (reader.hasNext()) {
			String rootName =reader.nextName();
			if (rootName.equals("COUNTTOTAL")) {
				COUNTTOTAL = nextInt(reader);
				counttotal=COUNTTOTAL;
				LogUtils.info("COUNTTOTAL----->"+COUNTTOTAL);
			}else if (rootName.equals("RETCODE")) {
				RETCODE = nextInt(reader);
			}else if (rootName.equals("ITEM")) {
				reader.beginArray();
				while (reader.hasNext()) {
					reader.beginObject();
					info = new VodChannel();
					while (reader.hasNext()) {
						String name = reader.nextName();
//						LogUtils.e("VodChannelJsonFactory", "name=  "+name);
						if (name.trim().equals("VODID")) {
							info.VODID = nextString(reader);
							LogUtils.e("VODID", "name=  "+info.VODID);	
						}else if (name.equals("VODNAME")) {
							info.VODNAME = nextString(reader).toString().replaceAll("&#37;", "%");
						}else if (name.equals("PICPATH")) {
							String picPath = nextString(reader);
							info.PICPATH=IPTVUriUtils.EpgHost+picPath.substring(picPath.indexOf("/images/"), picPath.length());
						}else if (name.equals("DEFINITION")) {
							info.DEFINITION = nextInt(reader);
						}else if(name.equals("CONTENTCODE")){
							info.CONTENTCODE = nextString(reader);
							LogUtils.info("info.CONTENTCODE  :"+info.CONTENTCODE);
						}else if(name.equals("columncode")){
							info.columncode = nextString(reader);
							LogUtils.info("info.columncode  :"+info.columncode);
						}else{
							reader.skipValue();
						}
					}
					info.platform=TvApplication.platform;
//					counttotal++;
					list.add(info);
//					LogUtils.info("VodChannel----->"+info.toString());
					reader.endObject();
				}
				
				reader.endArray();
			} else {
				reader.skipValue();
			}
		}
		reader.endObject();
		LogUtils.info("counttotal-->1:  "+counttotal+"  list   "+list.size());
		return list;
	}

	@Override
	protected ArrayList<NameValuePair> getPostArgs() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String CreateUri(Object... args) {
		String url = null;
		if (TvApplication.platform == EnumType.Platform.HUAWEI || TvApplication.platform == EnumType.Platform.UNKNOW) {
			url = String.format(Locale.CHINA, "%s?typeId=%s&length=%d&station=%d",IPTVUriUtils.VodChannelListHostJson, args[1], args[2], args[3]);
		} else if (TvApplication.platform == EnumType.Platform.HOTEL) {
			url = String.format(Locale.CHINA,"%s?typeId=%s&length=%d&station=%d", IPTVUriUtils.HotelProgramListHost, args[1], args[2], args[3]);
		}else if (TvApplication.platform == EnumType.Platform.ZTE) {
			url = String.format("%s?typeId=%s&curpagenum=%d&pagecount=%d", IPTVUriUtils.vodGetVodListByTypeIdZTE, args[1],args[2],args[3]);//&curpagenum=%d&pagecount=%d  ,args[2],args[3]
		}
		return url;
	}

}
