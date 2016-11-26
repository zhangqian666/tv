package com.iptv.rocky.hwdata.json;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import org.apache.http.NameValuePair;
import android.util.JsonReader;
import com.iptv.common.HttpJsonFactoryBase;
import com.iptv.common.data.OrderVodResult;
import com.iptv.rocky.hwdata.IPTVUriUtils;
import com.iptv.rocky.model.TvApplication;

public class OrderDayPackageFactory extends HttpJsonFactoryBase<OrderVodResult> {

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
		int days = (Integer) args[0];
		double price = (Double) args[1];
		return String.format(Locale.CHINA,"%s?userid=%s&days=%d&price=%f&roomid=%s", IPTVUriUtils.orderOneDayPackageHost, userId,days,price,TvApplication.roomId);
	}

	@Override
	protected ArrayList<NameValuePair> getPostArgs() {
		return null;
	}

}
