package com.iptv.rocky.hwdata.json;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.apache.http.NameValuePair;

import com.iptv.common.HttpJsonFactoryBase;
import com.iptv.common.data.LiveChannelBill;
import com.iptv.common.data.ProgBill;
import com.iptv.common.utils.LogUtils;
import com.iptv.rocky.HomeActivity;
import com.iptv.rocky.hwdata.IPTVUriUtils;
import com.iptv.rocky.model.TvApplication;

import android.util.JsonReader;

public class AllChannelProgBilJsonlFactory extends HttpJsonFactoryBase<ArrayList<LiveChannelBill>>{
	private LiveChannelBill mCurrentInfo;
	public ProgBill progBill;
	
	public static ArrayList<LiveChannelBill> lstLiveChannelBill;
	
	public static HashMap<String, LiveChannelBill> mapLiveChannelBillZte;
	@Override
	protected ArrayList<LiveChannelBill> AnalysisData(JsonReader reader) throws IOException {
		ArrayList<LiveChannelBill> list = new ArrayList<LiveChannelBill>();
		
		reader.beginObject();
		while (reader.hasNext()) {
			String nodeName=reader.nextName();
		if ("CHANNELS".equals(nodeName)) {
			reader.beginArray();
			while(reader.hasNext()){
				reader.beginObject();
				while(reader.hasNext()){
					String ChannelID="";
					String columnName=reader.nextName();
				if ("CHANNELCODE".equals(columnName)) {
				    ChannelID = nextString(reader);
					if (HomeActivity.mapLiveChannel.containsKey(ChannelID)) {
						mCurrentInfo = new LiveChannelBill(HomeActivity.mapLiveChannel.get(ChannelID));
					}
					
				}else if ("PROGLIST".equals(columnName)) {
					reader.beginArray();
					while(reader.hasNext()){
						reader.beginObject();
						progBill=new ProgBill();
						while(reader.hasNext()){
							String objName=reader.nextName();
							if ("PROGCODE".equals(objName)) {
								progBill.programId = nextString(reader);
							}else if ("BEGINDATE".equals(objName)) {
								progBill.beginDate = nextString(reader);
							}else if ("BEGINTIME".equals(objName)) {
								progBill.beginTime = nextString(reader);
							}else if ("ENDTIME".equals(objName)) {
								progBill.endTime = nextString(reader);
							}else if ("TITLE".equals(objName)) {
								progBill.title = nextString(reader).toString().replaceAll("&#37;", "%");
							}else {
								reader.skipValue();
							}
						}
						if (mCurrentInfo != null){
							mCurrentInfo.lstProgBill.add(progBill);
						}
						reader.endObject();
					}
					reader.endArray();
					if (mCurrentInfo != null)
						list.add(mCurrentInfo);
					mCurrentInfo = null;
				}else{
					reader.skipValue();
				}
				
				}
				reader.endObject();
			}
			reader.endArray();
		} else{
			reader.skipValue();
		}
		
		}
		LogUtils.error("mapLiveChannelBillZte 是否为空?"+(mapLiveChannelBillZte == null));
		if(mapLiveChannelBillZte == null){
			mapLiveChannelBillZte = new LinkedHashMap<String, LiveChannelBill>();
		}
		for (LiveChannelBill bill : list) {
			mapLiveChannelBillZte.put(bill.ChannelID, bill);
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
		
		return String.format("%s?lang="+TvApplication.language+"&date=%s",IPTVUriUtils.AllChannelProgBillHostZTE,args[0]);
	}

}
