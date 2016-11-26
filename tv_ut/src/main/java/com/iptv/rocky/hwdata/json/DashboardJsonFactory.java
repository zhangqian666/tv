package com.iptv.rocky.hwdata.json;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import org.apache.http.NameValuePair;

import android.util.JsonReader;

import com.iptv.common.HttpJsonFactoryBase;
import com.iptv.common.data.DashboardInfo;
import com.iptv.common.data.DashboardPicture;
import com.iptv.common.utils.LogUtils;
import com.iptv.rocky.hwdata.IPTVUriUtils;
import com.iptv.rocky.model.TvApplication;

public class DashboardJsonFactory extends HttpJsonFactoryBase<ArrayList<DashboardInfo>> {

	@Override
	protected ArrayList<DashboardInfo> AnalysisData(JsonReader reader)
			throws IOException {
		ArrayList<DashboardInfo> list = new ArrayList<DashboardInfo>();
		reader.beginArray();
		while (reader.hasNext()) {
			reader.beginObject();
			
			DashboardInfo mDashboardInfo = new DashboardInfo();
			while (reader.hasNext()){
				
				String name = reader.nextName();
				if(name.endsWith("beginDate")){
					Long begin = nextLong(reader);
					if(begin != null){
						mDashboardInfo.setBeginDate(new Date(begin));
					}
				}else if(name.endsWith("endDate")){
					Long end = nextLong(reader);
					if(end != null){
						mDashboardInfo.setBeginDate(new Date(end));	
					}else{
						mDashboardInfo.setBeginDate(null);
					}
				}else if(name.endsWith("type")){
					mDashboardInfo.setType(nextString(reader));
				}else if(name.endsWith("title")){
					mDashboardInfo.setTitle(nextString(reader));	
				}else if(name.endsWith("contentType")){
					mDashboardInfo.setContentType(nextString(reader));	
				}else if(name.endsWith("playUrl")){
					mDashboardInfo.setPlayUrl(nextString(reader));	
				}else if(name.endsWith("image")){
					mDashboardInfo.setImage(new ArrayList<DashboardPicture>());
					reader.beginArray();
					while (reader.hasNext()) {
						reader.beginObject();
						DashboardPicture dashboardPicture = new DashboardPicture();
						while(reader.hasNext()){
							name = reader.nextName();
							if (name.equals("seq")) {
								dashboardPicture.setSeq(nextInt(reader));
							}else if (name.equals("backgroundImage")) {
//								dashboardPicture.setBackgroundImage(IPTVUriUtils.HotelHost+ nextString(reader));
								dashboardPicture.setBackgroundImage(IPTVUriUtils.HotelHost+ nextString(reader));
								//LogUtils.info("BackgroundImage----->>>"+dashboardPicture.getBackgroundImage());
							}else if(name.equals("duration")){
								dashboardPicture.setDuration(nextInt(reader));
							}else if(name.endsWith("beginDate")){
								Long begin = nextLong(reader);
								if(begin != null){
									dashboardPicture.setBeginDate(new Date(begin));
								}
							}else if(name.endsWith("endDate")){
								Long end = nextLong(reader);
								if(end != null){
									dashboardPicture.setBeginDate(new Date(end));	
								}else{
									dashboardPicture.setBeginDate(null);
								}
							}else if(name.endsWith("type")){
								dashboardPicture.setType(nextString(reader));
							}else if(name.endsWith("title")){
								dashboardPicture.setTitle(nextString(reader));	
							}else {
								reader.skipValue();
							}
						}
						reader.endObject();
						mDashboardInfo.getImage().add(dashboardPicture);
					}
					reader.endArray();
				}else {
					reader.skipValue();
				}
			}
			reader.endObject();
			list.add(mDashboardInfo);
		}
		
		reader.endArray();
		return list;
	}

	@Override
	protected String CreateUri(Object... args) {
		return String.format("%s?stbid=%s", IPTVUriUtils.dashboardHost, TvApplication.stbId);
	}

	@Override
	protected ArrayList<NameValuePair> getPostArgs() {
		return null;
	}

}
