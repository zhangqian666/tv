package com.iptv.rocky.hwdata.json;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import org.apache.http.NameValuePair;
import android.util.JsonReader;
import com.iptv.common.HttpJsonFactoryBase;
import com.iptv.common.data.Advertisement;
import com.iptv.common.data.AdvertisementImageInfo;
import com.iptv.common.data.EnumType;
import com.iptv.common.data.ScreenProtect;
import com.iptv.common.data.ScreenProtectImageInfo;
import com.iptv.common.utils.CommonUtils;
import com.iptv.rocky.hwdata.IPTVUriUtils;
import com.iptv.rocky.model.TvApplication;

public class AdvertisementFactory extends HttpJsonFactoryBase<Advertisement> {

	@Override
	protected Advertisement AnalysisData(JsonReader reader)throws IOException {
		Advertisement info = new Advertisement();
		info.setImages(new ArrayList<AdvertisementImageInfo>());
		reader.beginObject();
		while (reader.hasNext()) {
			String name = reader.nextName();
			if (name.equals("imageInfos")) {
				reader.beginArray();
				while (reader.hasNext()) {
					reader.beginObject();
					AdvertisementImageInfo imageInfo = new AdvertisementImageInfo();
					while (reader.hasNext()) {
						String imageName = reader.nextName();
						if (imageName.equals("title")) {
							imageInfo.title = nextString(reader);
						} else if (imageName.equals("seq")) {
							imageInfo.seq = nextInt(reader);
						} else if (imageName.equals("backgroundPicture")) {
							imageInfo.bgimg = IPTVUriUtils.HotelHost + nextString(reader);
						} else if (imageName.equals("frontPicture")) {
							imageInfo.topimg = IPTVUriUtils.HotelHost + nextString(reader);
						} else if (imageName.equals("frontPosition")) {
							imageInfo.frontImagePosition = nextString(reader);
						} else if (imageName.equals("duration")) {
							imageInfo.duration = nextInt(reader);
						} else {
							reader.skipValue();
						}
					}
					info.getImages().add(imageInfo);
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
		return String.format(Locale.CHINA,"%s?roomid=%s", IPTVUriUtils.advertisement,TvApplication.roomId);
	}

	@Override
	protected ArrayList<NameValuePair> getPostArgs() {
	
		return null;
	}

}
