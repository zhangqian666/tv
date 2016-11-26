package com.iptv.rocky.hwdata.json;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import org.apache.http.NameValuePair;
import android.util.JsonReader;
import com.iptv.common.HttpJsonFactoryBase;
import com.iptv.common.data.WelcomeInfo;
import com.iptv.common.utils.LogUtils;
import com.iptv.rocky.hwdata.IPTVUriUtils;
import com.iptv.rocky.model.TvApplication;

public class WelcomeInfoFactory extends HttpJsonFactoryBase<WelcomeInfo> {

	@Override
	protected WelcomeInfo AnalysisData(JsonReader reader)throws IOException {
		WelcomeInfo info = new WelcomeInfo();
		reader.beginObject();
		while (reader.hasNext()) {
			String name = reader.nextName();
			if (name.equals("backgroundImage")) {
				info.setBackgroundImage(IPTVUriUtils.HotelHost + nextString(reader));
			} else if (name.equals("topImageChinese")) {
				info.setTopImageChinese( IPTVUriUtils.HotelHost + nextString(reader));
			} else if (name.equals("topImageEnglish")) {
				info.setTopImageEnglish(IPTVUriUtils.HotelHost + nextString(reader));
			} else if (name.equals("welcomeChineseText")) {
				info.setWelcomeChineseText(nextString(reader));
			} else if (name.equals("welcomeEnglishText")){	
				info.setWelcomeEnglishText(nextString(reader));
			} else {
				reader.skipValue();
			}
		}
		reader.endObject();
		return info;
	}

	@Override
	protected String CreateUri(Object... args) {
		return String.format(Locale.CHINA,"%s?welcomeId=%s", IPTVUriUtils.welcomeInfo,TvApplication.welcomeId);
	}

	@Override
	protected ArrayList<NameValuePair> getPostArgs() {
	
		return null;
	}

}
