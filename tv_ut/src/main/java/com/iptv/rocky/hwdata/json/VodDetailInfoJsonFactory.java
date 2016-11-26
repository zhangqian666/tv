package com.iptv.rocky.hwdata.json;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import org.apache.http.NameValuePair;

import android.util.JsonReader;

import com.iptv.common.HttpJsonFactoryBase;
import com.iptv.common.data.EnumType;
import com.iptv.common.data.VodChannel;
import com.iptv.common.data.VodDetailInfo;
import com.iptv.common.utils.CommonUtils;
import com.iptv.common.utils.LogUtils;
import com.iptv.common.utils.TimeUtil;
import com.iptv.rocky.hwdata.IPTVUriUtils;

public class VodDetailInfoJsonFactory extends HttpJsonFactoryBase<VodDetailInfo> {
	private EnumType.Platform platform;
	private int POSTERPATHS_key = -1;
	private ArrayList<String> POSTER_PATHS;
	private int CASTMAP_key = -1;
	private ArrayList<String> CAST_NAMES;
	
	@Override
	protected VodDetailInfo AnalysisData(JsonReader reader)
			throws IOException {
		VodDetailInfo info = new VodDetailInfo();
		reader.beginObject();
		while (reader.hasNext()) {
			String name = reader.nextName();
			if (name.equalsIgnoreCase("VODID")) {
					info.VODID =nextString(reader);
			} else if (name.equals("VODNAME")) {
				info.VODNAME = nextString(reader);
			} else if (name.equalsIgnoreCase("PICPATH")) {
				info.PICPATH = nextString(reader);
				if(platform == EnumType.Platform.HUAWEI || platform == EnumType.Platform.RUNHUAWEI || platform == EnumType.Platform.DEVHUAWEI){
					info.PICPATH = IPTVUriUtils.Host + info.PICPATH;
				}else if(platform == EnumType.Platform.HOTEL){
					info.PICPATH = IPTVUriUtils.HotelHost + info.PICPATH;
				}else if(platform == EnumType.Platform.ZTE){
					info.PICPATH = IPTVUriUtils.Host + info.PICPATH;
				}else if(platform == EnumType.Platform.UNKNOW){
					info.PICPATH = IPTVUriUtils.Host + info.PICPATH;
				}
			} else if (name.equals("DEFINITION")) {
				info.DEFINITION =  CommonUtils.parseInt(nextString(reader));
			} else if (name.equalsIgnoreCase("director")) {
				String value = nextString(reader);
				if (value.contains(";")) {
					value=value.replace(";", ",");
				}
				info.DIRECTOR=value;
			} else if (name.equals("ASSESSID")) {
				info.ASSESSID = CommonUtils.parseInt(nextString(reader).toString());
			} else if (name.equals("ELAPSETIME")) {
				info.ELAPSETIME =  CommonUtils.parseInt(nextString(reader).toString());
			} else if (name.equalsIgnoreCase("INTRODUCE")) {
				info.INTRODUCE = nextString(reader);
			} else if(name.equals("shortdesc")){
				info.shortdesc = nextString(reader);
			}else if (name.equals("VODPRICE")) {
				info.VODPRICE = nextString(reader);
			} else if (name.equals("SITCOMNUM")) {
				info.SITCOMNUM =  CommonUtils.parseInt(nextString(reader).toString());
			} else if (name.equals("ISASSESS")) {
				info.ISASSESS =  CommonUtils.parseInt(nextString(reader).toString());
			} else if (name.equals("ISSITCOM")) {
				info.ISSITCOM =  CommonUtils.parseInt(nextString(reader));
			} else if(name.equals("programtype")){
				info.programtype=nextString(reader);
			}else if (name.equals("contentcode")) {
				info.CONTENTCODE=nextString(reader);
			}else if(name.equals("seriesprogramcode")){
				info.seriesprogramcode=nextString(reader);
			}else if(name.equals("mediaservices")){
				info.mediaservices=CommonUtils.parseInt(nextString(reader).toString());
			}else if(name.equals("issimpletrailer")){
				info.issimpletrailer=nextString(reader);
			}else if(name.equals("posterfilelist")){
				info.posterfilelist=nextString(reader);
			}else if(name.equals("description")){
				info.INTRODUCE = nextString(reader);
			}else if(name.equals("actor")){
				String value = nextString(reader);
				ArrayList<String> actorList=new ArrayList<String>();
				String[] actor=value.split(";");
				for (int i = 0; i < actor.length; i++) {
					actorList.add(actor[i]);
				}
				info.CASTMAP.put(6, actorList);
			}else if(name.equals("columncode")){
				info.columncode=nextString(reader);
				LogUtils.error("columncode:     "+info.columncode);
			}else if(name.equals("channelcode")){
				info.channelcode=nextString(reader);
				LogUtils.error("channelcode:     "+info.channelcode);
			}else if(name.equals("elapsedtime")){
//				info.elapsedtime=nextString(reader);
				info.ELAPSETIME=(TimeUtil.getSeconds(nextString(reader).toString()))/60;
			}else if(name.equals("platform")){
//				LogUtils.debug("平台:"+platform);
				if(platform == EnumType.Platform.DEVHUAWEI || platform == EnumType.Platform.RUNHUAWEI || platform == EnumType.Platform.HUAWEI){
					info.platform=platform;
				}else{
					info.platform=EnumType.Platform.createPlatform(nextString(reader));
				}
			}else if (name.equals("CASTMAP")) {
				reader.beginObject();
				while (reader.hasNext()) {
					String key = reader.nextName();
					ArrayList<String> value = new ArrayList<String>();
					reader.beginArray();
					while (reader.hasNext()) {
						value.add(nextString(reader));
					}
					reader.endArray();
					if (value.size() > 0)
						info.CASTMAP.put(CommonUtils.parseInt(key, 0), value);
				 }
				 reader.endObject();
			} else if (name.equals("SUBVODIDLIST")) {
				reader.beginArray();
				while (reader.hasNext()) {
					String vodId = nextString(reader);
					info.SUBVODIDLIST.add(new VodChannel(vodId));
				}
				reader.endArray();
			} else if (name.equals("SUBVODNUMLIST")) {
				reader.beginArray();
				while (reader.hasNext()) {
					String vodNum = nextString(reader);
					info.SUBVODNUMLIST.add( CommonUtils.parseInt(vodNum));
				}
				reader.endArray();
			} else if (name.equals("CODE")) {
				info.CODE = nextString(reader);
			} else if (name.equals("FATHERVODID")) {
				info.FATHERVODID =  CommonUtils.parseInt(nextString(reader).toString());
            } else if (name.equals("SERVICEID")) {
				reader.beginArray();
				while (reader.hasNext()) {
					info.SERVICEID.add(nextString(reader));
				}
				reader.endArray();
            } else if (name.equals("allTypeId")) {
				reader.beginArray();
				while (reader.hasNext()) {
					info.allTypeId.add(nextString(reader));
				}
				reader.endArray();
            } else if (name.equals("POSTERPATHS")) {
				reader.beginObject();
				while (reader.hasNext()) {
					String key = reader.nextName();
					ArrayList<String> value = new ArrayList<String>();
					reader.beginArray();
					while (reader.hasNext()) {
						value.add(nextString(reader));
					}
					reader.endArray();
					if (value.size() > 0)
						info.POSTERPATHS.put(CommonUtils.parseInt(key, 0), value);
				 }
				 reader.endObject();
			} else {
				reader.skipValue();
			}
		}
		
		if (info.SUBVODIDLIST.size() == info.SUBVODNUMLIST.size() && info.ISSITCOM == 1)
		{
			for (int i = 0; i < info.SUBVODIDLIST.size(); i++)
			{
				info.SUBVODIDLIST.get(i).nNumber = info.SUBVODNUMLIST.get(i);
			}
		}
		
		reader.endObject();
		info.platform =platform;
		return info;
	}

	@Override
	protected String CreateUri(Object... args) {
		if(args[0] == null){
			platform =EnumType.Platform.HUAWEI;
		}else{
			platform = EnumType.Platform.createPlatform(args[0].toString());
		}
		String url =null;
		if(platform == EnumType.Platform.HUAWEI || platform == EnumType.Platform.RUNHUAWEI || platform == EnumType.Platform.DEVHUAWEI){
			url =String.format(Locale.CHINA,"%s?vodId=%s",IPTVUriUtils.VodDetailInfoHostJson, args[1]);
		}else if(platform == EnumType.Platform.HOTEL){
			url =String.format(Locale.CHINA,"%s?vodId=%s",IPTVUriUtils.HotelProgramDetaiJsonlHost, args[1]);
		}else if(platform==EnumType.Platform.ZTE){
			url=String.format(Locale.CHINA,"%s?columncode=%s&contentcode=%s",IPTVUriUtils.vodGetVodTailinfo, args[1],args[2]);
		}
		return url;
	}

	@Override
	protected ArrayList<NameValuePair> getPostArgs() {
		return null;
	}

}
