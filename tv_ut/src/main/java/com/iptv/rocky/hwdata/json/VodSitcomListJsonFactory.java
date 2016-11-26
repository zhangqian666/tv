package com.iptv.rocky.hwdata.json;

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
import com.rabbitmq.client.AMQP.Basic.Return;

public class VodSitcomListJsonFactory extends HttpJsonFactoryBase<ArrayList<VodChannel>>{
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
			}else if (rootName.equals("RETCODE")) {
				RETCODE = nextInt(reader);
			}else if (rootName.equals("ITEM")) {
				reader.beginArray();
				while (reader.hasNext()) {
					reader.beginObject();
					info = new VodChannel();
					while (reader.hasNext()) {
						String name = reader.nextName();
						if (name.trim().equals("vodId")) {
							info.VODID = nextString(reader);
						}else if (name.equals("vodNum")) {
//							info.nNumber=CommonUtils.parseInt(nextString(reader));
							info.seriesnum=CommonUtils.parseInt(nextString(reader));
							info.nNumber=info.seriesnum;
						}else if (name.equals("contentcode")) {
							info.CONTENTCODE = nextString(reader);
						}else if (name.equals("proName")) {
							info.VODNAME=nextString(reader).toString().replaceAll("&#37;", "%");
						}else if (name.equals("seriesprogramcode")) {
							info.seriesprogramcode=nextString(reader);
						}else if (name.equals("programtype")) {
							info.programtype=nextString(reader);
						}else if (name.equals("programsearchkey")) {
							info.programsearchkey=nextString(reader);
						}else if (name.equals("scrollFlag")) {
//							info.scrollFlag=nextString(reader);
							String value=nextString(reader);
						}else if (name.equals("subProgramName")) {
//							info.VODNAME=nextString(reader);
							String value=nextString(reader);
						}else if (name.equals("price")) {
							info.price=CommonUtils.parseInt(nextString(reader));
						}else if (name.equals("picpath")) {
							info.VODNAME=nextString(reader);
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
		EnumType.Platform platform;
		if (args[0] != null) {
			platform = EnumType.Platform.createPlatform(args[0].toString());
		} else {
			platform = EnumType.Platform.HUAWEI;
		}
		String url = null;
		if (platform == EnumType.Platform.HUAWEI || platform == EnumType.Platform.UNKNOW) {
//			url = String.format(Locale.CHINA, "%s?typeId=%s&length=%d&station=%d",IPTVUriUtils.VodSitcomListHostJson, args[1], args[2], args[3]);
		} else if(platform == EnumType.Platform.ZTE ){
			url = String.format("%s?programCode=%s",IPTVUriUtils.getSitcomList, args[1]);
		}
		return url;
	}
	
}
