package com.iptv.rocky.hwdata.json;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.NameValuePair;

import com.iptv.common.HttpJsonFactoryBase;
import com.iptv.common.data.RecUrl;
import com.iptv.common.utils.LogUtils;
import com.iptv.rocky.hwdata.IPTVUriUtils;

import android.util.JsonReader;

public class RecUrlJsonFactory extends HttpJsonFactoryBase<RecUrl>{
	private RecUrl mCurrentInfo=new RecUrl();
	@Override
	protected RecUrl AnalysisData(JsonReader reader) throws IOException {
		reader.beginObject();
		while (reader.hasNext()) {
			String nodeName=reader.nextName();
			if ("PLAYURL".equals(nodeName)) {
				mCurrentInfo.RTSPURL = nextString(reader);
				LogUtils.info("---->"+mCurrentInfo.RTSPURL);
			}else{
				reader.skipValue();
			}
		}
		reader.endObject();
		return mCurrentInfo;
	}

	@Override
	protected ArrayList<NameValuePair> getPostArgs() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String CreateUri(Object... args) {

		return String.format("%s?channelcode=%s&vodId=%s&authidsession=%s",IPTVUriUtils.getReBillUrl,args[0],args[1],args[2]);
	}

}
