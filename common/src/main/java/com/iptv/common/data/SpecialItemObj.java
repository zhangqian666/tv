package com.iptv.common.data;

import java.io.IOException;

import android.util.JsonReader;
import android.util.JsonToken;

public class SpecialItemObj {
	private String epg_id;
	private String title;
	private String image;
	private String id;
	private String columnCode;
	private String contentCode;
	
	private EnumType.Platform platform;
	
	private boolean isVip;

	public String getEpg_id() {
		return epg_id;
	}

	public void setEpg_id(String epg_id) {
		this.epg_id = epg_id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public boolean isVip() {
		return isVip;
	}

	public void setVip(boolean isVip) {
		this.isVip = isVip;
	}

	public String getColumnCode() {
		return columnCode;
	}

	public void setColumnCode(String columnCode) {
		this.columnCode = columnCode;
	}

	public String getContentCode() {
		return contentCode;
	}

	public void setContentCode(String contentCode) {
		this.contentCode = contentCode;
	}

	public static SpecialItemObj build(JsonReader reader) throws IOException{
		if (reader.peek().equals(JsonToken.NULL)) {
			reader.nextNull();
			return null;
		}
		
		SpecialItemObj sItemObj = new SpecialItemObj();
		reader.beginObject();
		while (reader.hasNext()) {
			String name = reader.nextName();
			if(name.equals("epg_id")){
				sItemObj.setEpg_id(reader.nextString());
			}else if(name.equals("title")){
				sItemObj.setTitle(reader.nextString());
			}else if(name.equals("img")){
				sItemObj.setImage(reader.nextString());
			}else if(name.equals("platform")){
				sItemObj.setPlatform(EnumType.Platform.createPlatform(reader.nextString()));
			}else if(name.equals("id")){
				sItemObj.setId(reader.nextString());
			}else if(name.equals("vip")){
				sItemObj.setVip(reader.nextInt() == 0 ? false : true);
			}
			else{
				reader.skipValue();
			}
		}
		reader.endObject();
		
		return sItemObj;
	}

	public EnumType.Platform getPlatform() {
		return platform;
	}

	public void setPlatform(EnumType.Platform platform) {
		this.platform = platform;
	}
	
	
}
