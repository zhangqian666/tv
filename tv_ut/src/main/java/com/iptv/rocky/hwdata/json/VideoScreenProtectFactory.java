package com.iptv.rocky.hwdata.json;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import org.apache.http.NameValuePair;

import android.util.JsonReader;

import com.iptv.common.HttpJsonFactoryBase;
import com.iptv.common.data.VideoScreenProtect;
import com.iptv.common.data.VideoScreenProtectInfo;
import com.iptv.rocky.hwdata.IPTVUriUtils;
import com.iptv.rocky.model.TvApplication;

public class VideoScreenProtectFactory extends HttpJsonFactoryBase<VideoScreenProtect> {

	@Override
	protected VideoScreenProtect AnalysisData(JsonReader reader)throws IOException {
		VideoScreenProtect info = new VideoScreenProtect();
		info.setInfos(new ArrayList<VideoScreenProtectInfo>());
		reader.beginObject();
		while (reader.hasNext()) {
			String name = reader.nextName();
			if (name.equals("videoInfos")) {
				reader.beginArray();
				while (reader.hasNext()) {
					reader.beginObject();
					VideoScreenProtectInfo imageInfo = new VideoScreenProtectInfo();
					while (reader.hasNext()) {
						String imageName = reader.nextName();
						if (imageName.equals("seq")) {
							imageInfo.setSeq(nextInt(reader));
						} else if (imageName.equals("url")) {
							imageInfo.setUrl(nextString(reader));
						} else if (imageName.equals("duration")) {
							imageInfo.setDuration(nextInt(reader));
						} else {
							reader.skipValue();
						}
					}
					info.getInfos().add(imageInfo);
					reader.endObject();
				 }
				 reader.endArray();
			} else {
				reader.skipValue();
			}
		}
		reader.endObject();
		return info;
	}

	@Override
	protected String CreateUri(Object... args) {
		return String.format(Locale.CHINA,"%s?id=%s", IPTVUriUtils.videoScreenProtect,TvApplication.screenProtectId);
	}

	@Override
	protected ArrayList<NameValuePair> getPostArgs() {
	
		return null;
	}

}
