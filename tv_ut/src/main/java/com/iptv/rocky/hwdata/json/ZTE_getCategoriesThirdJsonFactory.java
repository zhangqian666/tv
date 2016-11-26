package com.iptv.rocky.hwdata.json;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.NameValuePair;

import com.iptv.common.HttpJsonFactoryBase;
import com.iptv.common.data.VodDetailInfo;

import android.util.JsonReader;

public class ZTE_getCategoriesThirdJsonFactory extends HttpJsonFactoryBase<ArrayList<VodDetailInfo>>{

	@Override
	protected ArrayList<VodDetailInfo> AnalysisData(JsonReader reader) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected ArrayList<NameValuePair> getPostArgs() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String CreateUri(Object... args) {
		// TODO Auto-generated method stub
		return null;
	}



}
