package com.iptv.rocky.hwdata.json;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import org.apache.http.NameValuePair;

import android.util.JsonReader;

import com.iptv.common.HttpJsonFactoryBase;
import com.iptv.common.data.VodOrLivePlayRecordReportResult;
import com.iptv.common.data.VodPlayRecord;
import com.iptv.rocky.hwdata.IPTVUriUtils;
import com.iptv.rocky.model.TvApplication;

/**
 * 
 * 
 *
 */
public class VodPlayRecordFactory extends HttpJsonFactoryBase<VodOrLivePlayRecordReportResult> {

	@Override
	protected VodOrLivePlayRecordReportResult AnalysisData(JsonReader reader)throws IOException {
		
		VodOrLivePlayRecordReportResult info = new VodOrLivePlayRecordReportResult();
		reader.beginObject();
		while (reader.hasNext()) {
			String name = reader.nextName();
			if (name.equals("result")) {
				info.setResult(nextInt(reader));
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
		
		VodPlayRecord playRecord = (VodPlayRecord) args[0];
		String progName = null;
		progName = URLEncoder.encode((String) args[1]);
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA);
		String beginTime = format.format(playRecord.getBeginPlayDateTime());
		String endTime = format.format(playRecord.getEndPlayDateTime());
		return String.format("%s?userid=%s&progid=%s&platform=%s&endposition=%d&beginposition=%d&begintime=%s&endtime=%s&progname=%s&ordered=%b", IPTVUriUtils.VodPlayRecordReportHost,TvApplication.account,  playRecord.getProgramId(),playRecord.getPlatform(),playRecord.getEndPosition(),playRecord.getBeginPosition(),beginTime,endTime,progName,playRecord.isOrdered());
	}

	@Override
	protected ArrayList<NameValuePair> getPostArgs() {
		
		return null;
	}
	
}
