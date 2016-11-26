package com.iptv.rocky.model.home;

import java.io.IOException;
import java.util.ArrayList;

import com.iptv.common.AssertJsonFactoryBase;
import com.iptv.common.data.EnumType;
import com.iptv.common.data.MyHotelTop;
import com.iptv.common.data.PortalHome;
import com.iptv.common.data.PortalLiveChannel;
import com.iptv.common.data.PortalLiveType;
import com.iptv.common.data.VodTop;
import com.iptv.common.utils.AtvUtils;
import com.iptv.common.utils.LogUtils;
import com.iptv.rocky.hwdata.IPTVUriUtils;

import android.content.Context;
import android.content.res.Configuration;
import android.text.TextUtils;
import android.util.JsonReader;

public class HomeLayoutFactory extends AssertJsonFactoryBase<ArrayList<HomeLayoutItem>> {
	private static Context mContext;
	
	public HomeLayoutFactory(Context context) {
		super(context);
		mContext = context;
	}

	@Override
	protected String getFileName() {
		Configuration config = mContext.getResources().getConfiguration();
		String languageCode = config.locale.getLanguage();
		if (languageCode.equals(IPTVUriUtils.SIMPLIFIED_CHINESE_CODE)) {
			return "home_layout.json";
		} else if (languageCode.equals(IPTVUriUtils.ENGLISH_CODE)) {
			return "home_layout_eng.json";
		}
		LogUtils.debug("not choose");
		return null;
	}

	@Override
	protected ArrayList<HomeLayoutItem> AnalysisData(JsonReader reader) throws IOException {
		ArrayList<HomeLayoutItem> result = new ArrayList<HomeLayoutItem>();
		reader.beginArray();
		while (reader.hasNext()) {
			reader.beginObject();
			HomeLayoutItem layout = new HomeLayoutItem();
			while (reader.hasNext()) {
				String name = reader.nextName();
                if ("id".equals(name)) {
                    layout.id = reader.nextInt();
                } else if ("title".equals(name)) {
					layout.title = reader.nextString();
				} else if ("items".equals(name)) {
					reader.beginArray();
					layout.items = new ArrayList<HomePageItem>();
					while (reader.hasNext()) {
						reader.beginObject();
						HomePageItem item = new HomePageItem();
						while (reader.hasNext()) {
							String pname = reader.nextName();
                            if ("id".equals(pname)) {
                                item.id = reader.nextInt();
                            } else if ("title".equals(pname)) {
								item.title = reader.nextString();
							} else if ("type".equals(pname)){
								item.type = EnumType.ItemType.createType(reader.nextString());
							} else if ("icon".equals(pname)){
								item.icon = reader.nextString();
							} else if ("background".equals(pname)){
								item.background = reader.nextString();
							} else if ("wspan".equals(pname)){
								item.widthSpan = reader.nextDouble();
							} else if ("hspan".equals(pname)){
								item.heightSpan = reader.nextDouble();
							} else if ("images".equals(pname)) {
								reader.beginArray();
								item.images = new ArrayList<String>(4);
								while (reader.hasNext()) {
									item.images.add(reader.nextString());
								}
								reader.endArray();
							} else {
								reader.skipValue();
							}
						}
						layout.items.add(item);
						reader.endObject();
					}
					reader.endArray();
				} else {
					reader.skipValue();
				}
			}
			result.add(layout);
			reader.endObject();
		}
		reader.endArray();
		
		return result;
	}
	
	public static ArrayList<PortalHome> insertInfoToProtal(ArrayList<PortalHome> infos) {
		if (infos.size() > 1) {
			PortalHome info;
			int i = 2;
			//if (TvApplication.hotelColumnEnabled.getPlayRecord().equals("1")) {
				/*info = new PortalHome();
				info.icon = "assets://icon/view.png";
				info.title = mContext.getString(R.string.home_history);
				info.content_type = EnumType.ContentType.UNKNOW;
				info.layout_type = EnumType.LayoutType.UNKNOW;
	            info.id = DataReloadUtil.HomeHistoryViewID;
	            infos.add(i, info);*/
			//}
		}
		return infos;
	}
	
	/**
	 * 本地数据（个人中心 & 设置）
	*/
	public static HomeItemData createHomeData(HomePageItem page) {
		if (page.type.equals(EnumType.ItemType.COMMON)) {
			return initData(new HomeCommonData(), page);
		} else if (page.type.equals(EnumType.ItemType.BACKIMAGE)) {
			return initData(new HomeBackImageData(), page);
		} else if (page.type.equals(EnumType.ItemType.ANIM)) {
			return initData(new HomeAnimData(), page);
		} else if (page.type.equals(EnumType.ItemType.TOP)) {
			return initData(new HomeTopData(), page);
		} else if (page.type.equals(EnumType.ItemType.TOPVER)) {
			return initData(new HomeTopVerData(), page);
		} else if (page.type.equals(EnumType.ItemType.TOPHOR)) {
			return initData(new HomeTopHorData(), page);
		}
		return null;
	}
	
	/**
	 * 热门推荐
	*/
	public static HomeItemData createHomeData(PortalHome info) {
		HomeItemData itemData = createItemData(info);
		double height = createItemHeight(info.layout_type);
		double width = createItemWidth(info.layout_type);
		return initData(itemData, info, width, height);
	}
	
	/**
	 * 直播推荐
	*/
	public static HomeItemData createHomeData(PortalLiveChannel info) {
		HomeItemData itemData = createItemData(info);
		double height = createItemHeight(info.layout_type);
		double width = createItemWidth(info.layout_type);
		return initData(itemData, info, width, height);
	}
	
	/**
	 * 直播分类
	*/
	public static HomeItemData createHomeData(PortalLiveType info) {
		HomeItemData itemData = createItemData(info);
		double height = createItemHeight(info.layout_type);
		double width = createItemWidth(info.layout_type);
		return initData(itemData, info, width, height);
	}
	
	/**
	 * 点播一级分类
	*/
	public static HomeItemData createHomeData(VodTop info) {
		HomeItemData itemData = createItemData(info);
		double height = createItemHeight(info.layout_type);
		double width = createItemWidth(info.layout_type);
		return initData(itemData, info, width, height);
	}
	
	/**
	 * 酒店一级分类
	*/
	public static HomeItemData createMyHotelData(MyHotelTop info) {
		HomeItemData itemData = createItemData(info);
		double height = createItemHeight(info.layout_type);
		double width = createItemWidth(info.layout_type);
		return initData(itemData, info, width, height);
	}
	
	private static HomeItemData createItemData(PortalHome info) {
		if (info.bgType == EnumType.BackGroundType.PICTURE) {
			if (info.bg.startsWith("http")) {
				//return new HomeCategoryData();
				return new HomeBackImageData();
			}
		} else if (info.bgType == EnumType.BackGroundType.PICTURE_DYNAMIC) {
			return new HomeAnimData();
		} else if (info.bgType == EnumType.BackGroundType.PICTURE_ICON) {
			
		} else if (info.bgType == EnumType.BackGroundType.COLOR) {
			
		} else if (info.bgType == EnumType.BackGroundType.COLOR_ICON) {
			
		} else if (info.bgType == EnumType.BackGroundType.UNKNOW) {
			return new HomeBackImageData();
		}
		if (TextUtils.isEmpty(info.bg)) {
			info.bg = AtvUtils.getMetroItemBackground(0);
		}
		return new HomeCommonData();
	}
	
	private static HomeItemData createItemData(PortalLiveChannel info) {
		if (info.content_type.equals(EnumType.ContentType.LIVE_CHANNEL)) {
			if (info.bg.startsWith("http")) {
				//return new HomeCategoryData();
				return new HomeBackImageData();
			}
		} else if (!TextUtils.isEmpty(info.bg) && info.bg.startsWith("http")) {
			return new HomeBackImageData();
		}
		if (TextUtils.isEmpty(info.bg)) {
			info.bg = AtvUtils.getMetroItemBackground(0);
		}
		return new HomeCommonData();
	}
	
	private static HomeItemData createItemData(PortalLiveType info) {
		if (info.content_type.equals(EnumType.ContentType.CATEGORY)) {
			if (info.bg.startsWith("http")) {
				return new HomeCategoryData();
			}
		} else if (!TextUtils.isEmpty(info.bg) && info.bg.startsWith("http")) {
			return new HomeLiveData();
		}
		if (TextUtils.isEmpty(info.bg)) {
			info.bg = AtvUtils.getMetroItemBackground(0);
		}
		return new HomeCommonData();
	}
	
	private static HomeItemData createItemData(VodTop info) {
		if (info.content_type.equals(EnumType.ContentType.VOD_TOP_CATEGORY)) {
			
			if (info.bgType.equals(EnumType.BackGroundType.COLOR)) {
				info.bg = AtvUtils.getMetroItemBackground(0);
				return new HomeBackImageData();
			} else if (info.bgType.equals(EnumType.BackGroundType.PICTURE)) {
				if (info.bg.startsWith("http")) {
					return new HomeCategoryData();
				}
			} else if(info.bgType.equals(EnumType.BackGroundType.COLOR_ICON)) {
				
			} else if(info.bgType.equals(EnumType.BackGroundType.PICTURE_ICON)) {
				return new HomeBackImageData();
			} else if(info.bgType.equals(EnumType.BackGroundType.PICTURE_DYNAMIC)) {
				/*if (info.imgs != null && info.imgs.size() > 0) {
					return new HomeAnimData();
				}*/
			}
			
			/*if (info.bg.startsWith("http")) {
				return new HomeCategoryData();
			}*/
		}
		return new HomeCommonData();
	}
	
	// 创建酒店的顶层分类
	private static HomeItemData createItemData(MyHotelTop info) {
		if (info.content_type.equals(EnumType.ContentType.HOTEL_TOP_CATEGORY)) {
			if (info.bgType.equals(EnumType.BackGroundType.PICTURE)) {
				if (info.bg.startsWith("http")) {
					return new HomeCategoryData();
				}
			} else if (info.bgType.equals(EnumType.BackGroundType.PICTURE_DYNAMIC)) {
				if (info.imgs != null && info.imgs.size() > 0) {
					return new HomeAnimData();
				}
			} else if (info.bgType.equals(EnumType.BackGroundType.COLOR_ICON)) {
				return new HomeBackImageData();
			}
		}
		if (TextUtils.isEmpty(info.bg)) {
			info.bg = AtvUtils.getMetroItemBackground(0);
		}
		return new HomeCommonData();
	}
	
	private static double createItemWidth(EnumType.LayoutType layout_type) {
		if (layout_type.equals(EnumType.LayoutType.ROW)) {
			return 1.3;
		} else if (layout_type.equals(EnumType.LayoutType.COL)) {
			return 2.0;
		} else if (layout_type.equals(EnumType.LayoutType.NORMAL)) {
			return 1.0;
		} else if (layout_type.equals(EnumType.LayoutType.UNKNOW)) {
			return 1.0;
		}
		return 1.0;
	}
	
	private static double createItemHeight(EnumType.LayoutType layout_type) {
		if (layout_type.equals(EnumType.LayoutType.ROW)) {
			return 2.0;
		} else if(layout_type.equals(EnumType.LayoutType.COL)) {
			return 1.0;
		} else if(layout_type.equals(EnumType.LayoutType.NORMAL)) {
			return 1.0;
		} else if(layout_type.equals(EnumType.LayoutType.UNKNOW)) {
			return 1.0;
		}
		return 1.0;
	}
	
	private static HomeItemData initData(HomeItemData data, HomePageItem page) {
        data.id = page.id;
        data.pageItem = page;
        data.widthSpan = page.widthSpan;
        data.heightSpan = page.heightSpan;
        data.initSpecialData();
        return data;
    }
	
	private static HomeItemData initData(HomeItemData data, PortalLiveChannel info, double widthSpan, double heightSpan) {
        data.id = info.id;
        data.pageItem = new HomePageItem();
        data.pageItem.id = info.id;
        data.pageItem.contentid = info.ChannelID ;
        data.pageItem.contenttype = info.content_type;
        data.pageItem.layouttype = info.layout_type;
        data.pageItem.background = info.bg;
        data.pageItem.heightSpan = heightSpan;
        data.pageItem.widthSpan = widthSpan;
        data.widthSpan = widthSpan;
        data.heightSpan = heightSpan;
        data.initSpecialData();
        return data;
    }
	
	private static HomeItemData initData(HomeItemData data, PortalLiveType info, double widthSpan, double heightSpan) {
        data.id = info.id;
        data.pageItem = new HomePageItem();
        data.pageItem.id = info.id;
        data.pageItem.contentid = info.typeId + "";
        data.pageItem.contenttype = info.content_type;
        data.pageItem.layouttype = info.layout_type;
        data.pageItem.background = info.bg;
        data.pageItem.icon = info.icon;
        data.pageItem.title = info.title;
        data.pageItem.heightSpan = heightSpan;
        data.pageItem.widthSpan = widthSpan;
        data.widthSpan = widthSpan;
        data.heightSpan = heightSpan;
        data.initSpecialData();
        return data;
    }

    private static HomeItemData initData(HomeItemData data, VodTop info, double widthSpan, double heightSpan) {
        data.id = info.id;
        data.pageItem = new HomePageItem();
        data.pageItem.id = info.id;
        data.pageItem.contentid = info.type_id;
        data.pageItem.contenttype = info.content_type;
        data.pageItem.layouttype = info.layout_type;
        data.pageItem.background = info.bg;
        data.pageItem.icon = info.icon;
        data.pageItem.title = info.title;
        data.pageItem.heightSpan = heightSpan;
        data.pageItem.widthSpan = widthSpan;
        data.pageItem.subcontenttype = info.subContentType;
        data.pageItem.backgroundType = info.bgType;
        data.widthSpan = widthSpan;
        data.heightSpan = heightSpan;
        data.initSpecialData();
        return data;
    }
    
    private static HomeItemData initData(HomeItemData data, MyHotelTop info, double widthSpan, double heightSpan) {
        data.id = info.id;
        data.pageItem = new HomePageItem();
        data.pageItem.id = info.id;
        data.pageItem.contentid = info.type_id;
        data.pageItem.contenttype = info.content_type;
        data.pageItem.layouttype = info.layout_type;
        data.pageItem.myHotelSubContentType = info.subContentType;
        data.pageItem.backgroundType = info.bgType;
        data.pageItem.background = info.bg;
        data.pageItem.images = info.imgs;
        data.pageItem.icon = info.icon;
        data.pageItem.title = info.title;
        data.pageItem.heightSpan = heightSpan;
        data.pageItem.widthSpan = widthSpan;
        data.widthSpan = widthSpan;
        data.heightSpan = heightSpan;
        data.initSpecialData();
        return data;
    }
    
    private static HomeItemData initData(HomeItemData data, PortalHome info, double widthSpan, double heightSpan) {
        data.id = info.id;
        data.pageItem = new HomePageItem();
        data.pageItem.id = info.id;
        data.pageItem.contentid = info.programId + "";//修改原来是  info.content_id
        data.pageItem.columnCode = info.parentId;//父级ID；栏目id
        data.pageItem.contentCode = info.content_id;
        data.pageItem.contenttype = info.content_type;
        data.pageItem.subcontenttype = info.subContentType;
        data.pageItem.layouttype = info.layout_type;
        data.pageItem.backgroundType = info.bgType;
        data.pageItem.background = info.bg;
        data.pageItem.icon = info.icon;
        data.pageItem.images = info.imgs;
        data.pageItem.title = info.title;
        data.pageItem.heightSpan = heightSpan;
        data.pageItem.widthSpan = widthSpan;
        data.pageItem.platform = info.platform;
        data.widthSpan = widthSpan;
        data.heightSpan = heightSpan;
        data.initSpecialData();
        return data;
    }

}
