package com.iptv.rocky.hwdata.json;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.iptv.common.HttpJsonFactoryBase;
import com.iptv.common.HttpXmlFactoryBase;
import com.iptv.common.data.EnumType;
import com.iptv.common.data.VodChannel;
import com.iptv.common.utils.CommonUtils;
import com.iptv.common.utils.LogUtils;
import com.iptv.rocky.hwdata.IPTVUriUtils;
import com.iptv.rocky.model.TvApplication;

import android.util.JsonReader;

public class VodSearchJsonFactory extends HttpJsonFactoryBase<ArrayList<VodChannel>> {

	public EnumType.Platform platformEach;
	private VodChannel mCurrentInfo;
	
	public int COUNTTOTAL;
	public int RETCODE = 0;

	@Override
	protected ArrayList<VodChannel> AnalysisData(JsonReader reader) throws IOException {
		ArrayList<VodChannel> list = new ArrayList<VodChannel>();
		
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
					mCurrentInfo = new VodChannel();
					while (reader.hasNext()) {
						String name = reader.nextName();
//						LogUtils.e("VodChannelJsonFactory", "name=  "+name);
						if (name.trim().equals("VODID")) {
							mCurrentInfo.VODID = nextString(reader);
						}else if (name.equals("VODNAME")) {
							mCurrentInfo.VODNAME = nextString(reader).toString().replaceAll("&#37;", "%");
						}else if (name.equals("PICPATH")) {
							String picPath = nextString(reader);
								mCurrentInfo.PICPATH=IPTVUriUtils.Host +picPath;
						}else if (name.equals("DEFINITION")) {
							mCurrentInfo.DEFINITION = nextInt(reader);
						}else if(name.equals("CONTENTCODE")){
							mCurrentInfo.CONTENTCODE = nextString(reader);
//							LogUtils.info("mCurrentInfo.CONTENTCODE  :"+mCurrentInfo.CONTENTCODE);
						}else if(name.equals("columncode")){
							mCurrentInfo.columncode = nextString(reader);
//							LogUtils.info("mCurrentInfo.columncode  :"+mCurrentInfo.columncode);
						}else{
							reader.skipValue();
						}
					}
					mCurrentInfo.platform=TvApplication.platform;
					list.add(mCurrentInfo);
//					LogUtils.info("VodChannel----->"+mCurrentInfo.toString());
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
		return String.format("%s?code=%s&pagecount=%d&curpagenum=%d", IPTVUriUtils.VodSearchByCode, args[1].toString().toLowerCase(), args[2], args[3]);
	}
}
