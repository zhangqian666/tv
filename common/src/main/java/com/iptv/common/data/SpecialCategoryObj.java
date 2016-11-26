package com.iptv.common.data;

import java.io.IOException;

import android.util.JsonReader;
import android.util.JsonToken;

public class SpecialCategoryObj {
	private String id;
	private String bgimg;
	private String cover_img;
	private String title;
	private String categoryId;
	private EnumType.Platform platform;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBgimg() {
		return bgimg;
	}

	public void setBgimg(String bgimg) {
		this.bgimg = bgimg;
	}


	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCover_img() {
		return cover_img;
	}

	public void setCover_img(String cover_img) {
		this.cover_img = cover_img;
	}
	
	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public EnumType.Platform getPlatform() {
		return platform;
	}

	public void setPlatform(EnumType.Platform platform) {
		this.platform = platform;
	}
	
	public static SpecialCategoryObj build(JsonReader reader) throws IOException{
		SpecialCategoryObj scObj = new SpecialCategoryObj();
		if (reader.peek().equals(JsonToken.NULL)) {
			reader.nextNull();
			return null;
		}
		reader.beginObject();
		while (reader.hasNext()) {
			String name = reader.nextName();
			if("id".equals(name)){
				//scObj.setId(reader.nextInt());
				scObj.setId(reader.nextString());
			}else if("bgimg".equals(name)){
				String bgImage =reader.nextString();
				if(bgImage != null){
					scObj.setBgimg(bgImage);
				}
			}else if("platform".equals(name)){
				String platform =reader.nextString();
				if(platform != null){
					scObj.setPlatform(EnumType.Platform.createPlatform(platform));
				}else{
					
				}
			}else if("categoryId".equals(name)){
				scObj.setCategoryId(reader.nextString());
			}else if("cover_img".equals(name)){
				String coverImage =reader.nextString();
				if(coverImage != null){
					scObj.setCover_img(coverImage);
				}
				
			}else if("title".equals(name)){
				scObj.setTitle(reader.nextString());
			}else{
				reader.skipValue();
			}
		}
		reader.endObject();
		return scObj;
	}



}
