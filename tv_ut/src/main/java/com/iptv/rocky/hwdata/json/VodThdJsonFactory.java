package com.iptv.rocky.hwdata.json;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.NameValuePair;

import android.util.JsonReader;

import com.iptv.common.HttpJsonFactoryBase;
import com.iptv.common.data.EnumType;
import com.iptv.common.data.VodSec;
import com.iptv.common.utils.LogUtils;
import com.iptv.rocky.hwdata.IPTVUriUtils;
import com.iptv.rocky.model.TvApplication;

/**
 *解析获取第三级分类的JSON
 * @author LDD
 *
 */
public class VodThdJsonFactory extends HttpJsonFactoryBase<ArrayList<VodSec>>{
private String columncode="";
private Map<String,Object> map=new HashMap<String,Object>();
	@Override
	protected ArrayList<VodSec> AnalysisData(JsonReader reader)
			throws IOException {

		ArrayList<VodSec> list = new ArrayList<VodSec>();
		int COUNTTOTAL = 0;
		int RETCODE = 0;
		reader.beginObject();
		while (reader.hasNext()) {
			String rootName =reader.nextName();
			if (rootName.equals("COUNTTOTAL")) {
				COUNTTOTAL = nextInt(reader);
			}else if (rootName.equals("RETCODE")) {
				RETCODE = nextInt(reader);
			}else if (rootName.equals("ITEM")) {
				reader.beginArray();
				while (reader.hasNext()) {
					reader.beginObject();
					VodSec info = new VodSec();
					while (reader.hasNext()) {
						String name = reader.nextName();
						if (name.equals("TYPE_ID")) {
							info.type_id = nextString(reader);
						}else if (name.equals("TYPE_NAME")) {
							info.title = nextString(reader).toString().replaceAll("&#37;", "%");
						}else if (name.equals("TYPE_PICPATH")) {
							if(TvApplication.platform!=EnumType.Platform.ZTE)
								info.bg = IPTVUriUtils.Host + nextString(reader);
							else
								nextString(reader);
						}else if (name.equals("SUBJECT_TYPE")) {
							info.subjectType = nextInt(reader);
						}else if (name.equals("LAYOUT_TYPE")) {
							info.layout_type =EnumType.LayoutType.createType(nextInt(reader));
						}else if (name.equals("SUB_CONTENT_TYPE")) {
							info.sub_content_type =EnumType.SubContentType.createType(nextInt(reader));
						}else if (name.equals("CONTENT_TYPE")) {
							info.content_type =EnumType.ContentType.createType(nextInt(reader));
						}else if (name.equals("iconposter")) {
							info.bg = IPTVUriUtils.Host + nextString(reader);
						}else{
							reader.skipValue();
						}
					}
					map.put(info.type_id, info);
					if (!map.containsKey(columncode)) {
						list.add(info);
					}
					reader.endObject();
				}
				reader.endArray();
			} else {
				reader.skipValue();
			}
		}
		reader.endObject();
		
		return list;
	
	}

	@Override
	protected ArrayList<NameValuePair> getPostArgs() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String CreateUri(Object... args) {
		String url="";
		LogUtils.info("TvApplication.platform---->"+TvApplication.platform);
		if (TvApplication.platform==EnumType.Platform.DEVHUAWEI || TvApplication.platform==EnumType.Platform.RUNHUAWEI) {
			url=String.format("%s?typeId=%s",IPTVUriUtils.VodThdHostJSON, args[0]);
		}else if(TvApplication.platform==EnumType.Platform.ZTE){
			columncode=args[0].toString();
			url=String.format("%s?columncode=%s",IPTVUriUtils.zte_getCategoriesThirdJson, args[0]);
		}
		return url;
		
	}

}
