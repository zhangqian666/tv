package com.iptv.rocky.hwdata.json;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.http.NameValuePair;
import android.util.JsonReader;
import com.iptv.common.HttpJsonFactoryBase;
import com.iptv.common.data.HomePrompt;
import com.iptv.common.data.HomePromptType;
import com.iptv.rocky.hwdata.IPTVUriUtils;
import com.iptv.rocky.model.TvApplication;

public class HomePromptJsonFactory extends HttpJsonFactoryBase<List<HomePrompt>> {

	@Override
	protected List<HomePrompt> AnalysisData(JsonReader reader)throws IOException {
		List<HomePrompt> list = new ArrayList<HomePrompt>();
		reader.beginArray();
		
		while (reader.hasNext()) {
			reader.beginObject();
			HomePrompt homePrompt = new HomePrompt();
			while (reader.hasNext()) {
				String name = reader.nextName();
				if (name.equals("backgroundImage")) {
					homePrompt.setBackgroundImage(IPTVUriUtils.HotelHost+ nextString(reader));
				}else if(name.equals("duration")){
					homePrompt.setDuration(nextInt(reader));
				}else if(name.endsWith("beginDate")){
					Long begin = nextLong(reader);
					if(begin != null){
						homePrompt.setBeginDate(new Date(begin));
					}
				}else if(name.endsWith("endDate")){
					Long end = nextLong(reader);
					if(end != null){
						homePrompt.setBeginDate(new Date(end));	
					}else{
						homePrompt.setBeginDate(null);
					}
				}else if(name.endsWith("type")){
					homePrompt.setType(HomePromptType.createType(nextString(reader)));
				}else {
					reader.skipValue();
				}
			}
			list.add(homePrompt);
			reader.endObject();
		}
		reader.endArray();
		return list;
	}

	@Override
	protected String CreateUri(Object... args) {
		return String.format("%s?roomid=%s&lang=%s", IPTVUriUtils.homePromptHost, TvApplication.roomId,TvApplication.language);
	}

	@Override
	protected ArrayList<NameValuePair> getPostArgs() {
		return null;
	}

}
