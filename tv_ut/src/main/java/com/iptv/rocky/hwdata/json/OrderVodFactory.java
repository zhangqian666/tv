package com.iptv.rocky.hwdata.json;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Locale;

import org.apache.http.NameValuePair;

import android.util.JsonReader;

import com.iptv.common.HttpJsonFactoryBase;
import com.iptv.common.data.OrderVodResult;
import com.iptv.rocky.hwdata.IPTVUriUtils;
import com.iptv.rocky.model.TvApplication;

public class OrderVodFactory extends HttpJsonFactoryBase<OrderVodResult> {

	@Override
	protected OrderVodResult AnalysisData(JsonReader reader)
			throws IOException {
		OrderVodResult info = new OrderVodResult();
		reader.beginObject();
		while (reader.hasNext()) {
			String name = reader.nextName();
			if (name.equals("result")) {
				info.setResult(nextInt(reader));
			} else if (name.equals("programId")) {
				info.setProgramId(nextInt(reader));
			} else if (name.equals("price")) {
				info.setPrice(reader.nextDouble());
			
			} else if (name.equals("reason")) {
				info.setReason(nextString(reader));
			} else {
				reader.skipValue();
			}
		}
		reader.endObject();
		return info;
	}

	@Override
	protected String CreateUri(Object... args) {
		String userId = TvApplication.account;
		int progid = (Integer) args[0];
		String progName= URLEncoder.encode((String) args[1]);
		double price =(Double)args[2];
		String code = (String) args[3];
		String pack = (String) args[4];
		boolean isSeries = (Boolean)args[5];
		return String.format(Locale.CHINA,"%s?userid=%s&roomid=%s&progid=%d&progname=%s&price=%f&code=%s&package=%s&series=%b", IPTVUriUtils.orderVodHost, userId,TvApplication.roomId,progid,progName,price,code,pack,isSeries);
	}

	@Override
	protected ArrayList<NameValuePair> getPostArgs() {
	
		return null;
	}

}
