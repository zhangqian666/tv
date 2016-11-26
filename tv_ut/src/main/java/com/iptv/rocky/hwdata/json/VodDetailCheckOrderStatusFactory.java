package com.iptv.rocky.hwdata.json;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import org.apache.http.NameValuePair;
import android.util.JsonReader;
import com.iptv.common.HttpJsonFactoryBase;
import com.iptv.common.data.BillingTypeOfHotelGuest;
import com.iptv.common.data.VodOrderStatusResult;
import com.iptv.rocky.hwdata.IPTVUriUtils;
import com.iptv.rocky.model.TvApplication;

public class VodDetailCheckOrderStatusFactory extends HttpJsonFactoryBase<VodOrderStatusResult> {

	@Override
	protected VodOrderStatusResult AnalysisData(JsonReader reader)
			throws IOException {
		VodOrderStatusResult info = new VodOrderStatusResult();
		reader.beginObject();
		while (reader.hasNext()) {
			String name = reader.nextName();
			if (name.equals("result")) {
				info.setResult(nextInt(reader));
			}else if(name.equals("price")){ 
				info.setPrice(nextDouble(reader));
			}else if(name.equals("guestPayedProgramPrice")){ 
				info.setGuestPayedProgramPrice(nextDouble(reader));
			}else if(name.equals("billingTypeOfGuest")){
				TvApplication.billingType=BillingTypeOfHotelGuest.createType(nextString(reader));
			}
			else {
				reader.skipValue();
			}
		}
		reader.endObject();
		return info;
	}

	@Override
	protected String CreateUri(Object... args) {
		String userId = TvApplication.account;
		String progid =  (String) args[0];
		double price = (Double)args[1];
		String columnid= (String) args[2];//;
		return String.format(Locale.CHINA ,"%s?userid=%s&roomid=%s&progid=%s&price=%f&columnid=%s", IPTVUriUtils.vodDetailCheckOrderStatusHost, userId,TvApplication.roomId ,progid, price,columnid);
	}

	@Override
	protected ArrayList<NameValuePair> getPostArgs() {
		return null;
	}

}
