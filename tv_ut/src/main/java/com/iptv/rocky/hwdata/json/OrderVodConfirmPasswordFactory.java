package com.iptv.rocky.hwdata.json;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Locale;

import org.apache.http.NameValuePair;

import android.util.JsonReader;

import com.iptv.common.HttpJsonFactoryBase;
import com.iptv.common.data.ConfirmPasswordResult;
import com.iptv.rocky.hwdata.IPTVUriUtils;
import com.iptv.rocky.model.TvApplication;

public class OrderVodConfirmPasswordFactory extends HttpJsonFactoryBase<ConfirmPasswordResult> {

	@Override
	protected ConfirmPasswordResult AnalysisData(JsonReader reader)
			throws IOException {
		ConfirmPasswordResult info = new ConfirmPasswordResult();
		reader.beginObject();
		while (reader.hasNext()) {
			String name = reader.nextName();
			if (name.equals("passwordResult")) {
				info.setPasswordResult(nextBoolean(reader));
			} else if (name.equals("orderResult")) {
				info.setOrderResult(nextInt(reader));
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
		
		String password= (String) args[0];
		int days = (Integer) args[1];
		double price =(Double)args[2];
		String  progid = (String) args[3];
		String progName= URLEncoder.encode((String) args[4]);
		String packageSelected = (String) args[5];
		boolean isSeries = (Boolean)args[6];
		return String.format(Locale.CHINA,"%s?userid=%s&password=%s&days=%d&price=%f&progid=%s&progname=%s&ordertype=%s&series=%b", 
		IPTVUriUtils.orderVodConfirmPasswordHost, TvApplication.account,password, days, price,  progid,progName,packageSelected,isSeries);
	}

	@Override
	protected ArrayList<NameValuePair> getPostArgs() {
	
		return null;
	}

}
