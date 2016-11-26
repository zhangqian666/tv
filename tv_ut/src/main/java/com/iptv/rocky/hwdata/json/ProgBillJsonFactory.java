package com.iptv.rocky.hwdata.json;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.NameValuePair;

import android.content.Context;
import android.util.JsonReader;

import com.iptv.common.HttpJsonFactoryBase;
import com.iptv.common.data.LiveChannelBill;
import com.iptv.common.data.ProgBill;
import com.iptv.common.utils.LogUtils;
import com.iptv.rocky.hwdata.IPTVUriUtils;
import com.iptv.rocky.hwdata.local.LiveChannelBillLocalFactory;

public class ProgBillJsonFactory extends HttpJsonFactoryBase<LiveChannelBill> {
	
	private Context mContext;
	
	public ProgBillJsonFactory(Context context)	{
		mContext = context;
	}

	@Override
	protected LiveChannelBill AnalysisData(JsonReader reader)
			throws IOException {
		LiveChannelBill info = new LiveChannelBill();
		
		reader.beginObject();
		while (reader.hasNext()) {
			if (reader.nextName().equals("CHANNELID")) {
				info.ChannelID = nextString(reader);
			}
			
			if (reader.nextName().equals("LIST")) {
				reader.beginArray();
				while (reader.hasNext()) {
					reader.beginObject();
					ProgBill bill = new ProgBill();
					while (reader.hasNext()) {
						String name = reader.nextName();
						if (name.equalsIgnoreCase("title")) {
							bill.title = nextString(reader);
						} else if (name.equalsIgnoreCase("progId")) {
							bill.programId = nextString(reader);
						} else if (name.equalsIgnoreCase("beginTime")) {
							bill.beginTime = nextString(reader);
						} else if (name.equalsIgnoreCase("endTime")) {
							bill.endTime = nextString(reader);
						} else if (name.equalsIgnoreCase("beginDate")) {
							bill.beginDate = nextString(reader);
						} else {
							reader.skipValue();
						}
					}
					info.lstProgBill.add(bill);
					reader.endObject();
				}
				reader.endArray();
			} else {
				reader.skipValue();
			}
		}
		reader.endObject();
		
		// 保存到数据库
		LiveChannelBillLocalFactory liveChannelBillLocalFactory = new LiveChannelBillLocalFactory(mContext);
		liveChannelBillLocalFactory.insertBillInfos(info);
		
		LogUtils.debug("ProgBillFactory channelid:" + info.ChannelID);
		
		return info;
	}

	@Override
	protected String CreateUri(Object... args) {
		return String.format("%s&progId=%d&date=%s",
				IPTVUriUtils.ProgBillHost, args[0], args[1]);
	}

	@Override
	protected ArrayList<NameValuePair> getPostArgs() {
		// TODO Auto-generated method stub
		return null;
	}

}
