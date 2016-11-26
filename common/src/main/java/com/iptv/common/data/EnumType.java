package com.iptv.common.data;

public class EnumType {
	public static enum LayoutType {
		ROW, 		// 大竖图
		COL, 		// 大横图
		NORMAL, 	// 小图
		UNKNOW;

		public static LayoutType createType(int type) {
			switch (type) {
			case 1:
				return LayoutType.ROW;
			case 2:
				return LayoutType.COL;
			case 3:
				return LayoutType.NORMAL;
			default:
				return LayoutType.UNKNOW;
			}
		}

		public static LayoutType convertStr(String str) {
			if ("ROW".equalsIgnoreCase(str)) {
				return LayoutType.ROW;
			} else if ("COL".equalsIgnoreCase(str)) {
				return LayoutType.COL;
			} else if ("NORMAL".equalsIgnoreCase(str)) {
				return LayoutType.NORMAL;
			} else if ("UNKNOW".equalsIgnoreCase(str)) {
				return LayoutType.UNKNOW;
			} else {
				return LayoutType.UNKNOW;
			}
		}
	};


	public static enum BackGroundType {
		COLOR, 			// 颜色背景
		PICTURE, 		// 图片背景
		COLOR_ICON, 	// 颜色背景+ICON
		PICTURE_ICON,	// 图片背景+ICON
		PICTURE_DYNAMIC,// 图片背景+动态图片
		UNKNOW;

		public static BackGroundType createType(int type) {
			switch (type) {
			case 1:
				return BackGroundType.COLOR;
			case 2:
				return BackGroundType.PICTURE;
			case 3:
				return BackGroundType.COLOR_ICON;
			case 4:
				return BackGroundType.PICTURE_ICON;
			case 5:
				return BackGroundType.PICTURE_DYNAMIC;
			default:
				return BackGroundType.UNKNOW;
			}
		}

		public static BackGroundType convertInt(int type) {
			if (type == 1) {
				return BackGroundType.COLOR;
			} else if (type == 2) {
				return BackGroundType.PICTURE;
			} else if (type == 3) {
				return BackGroundType.COLOR_ICON;
			} else if (type == 4) {
				return BackGroundType.PICTURE_ICON;
			} else if (type == 5) {
				return BackGroundType.PICTURE_DYNAMIC;	
			} else {
				return BackGroundType.UNKNOW;
			}
		}
	};

	public static String converLayoutType(LayoutType type) {
		if (type == LayoutType.ROW) {
			return "ROW";
		} else if (type == LayoutType.COL) {
			return "COL";
		} else if (type == LayoutType.NORMAL) {
			return "NORMAL";
		} else if (type == LayoutType.UNKNOW) {
			return "UNKNOW";
		} else {
			return "UNKNOW";
		}
	}

	public static int convertBackgroundType(BackGroundType type) {
		if (type == BackGroundType.COLOR) {
			return 1;
		} else if (type == BackGroundType.PICTURE) {
			return 2;
		} else if (type == BackGroundType.COLOR_ICON) {
			return 3;
		} else if (type == BackGroundType.PICTURE_ICON) {
			return 4;
		} else if (type == BackGroundType.PICTURE_DYNAMIC) {
			return 5;		
		} else {
			return 0;
		}
	}

//	public static int convertPlatform(Platform platform) {
//		if (platform == Platform.HOTEL) {
//			return 1;
//		} else if (platform == Platform.HUAWEI) {
//			return 2;
//		} else if (platform == Platform.ZTE) {
//			return 3;	
//		} else {
//			return 0;
//		}
//	}

	public static enum ContentType {
		HOME_MOVIE, 			// 电影
		HOME_SERIES, 			// 电视剧
		HOME_HOTEL_INTRODUCE,
		HOME_SPECIAL, 			// 专题
		VIDEO,
		HOTEL_INTRODUCE,		// 酒店介绍
		LIVE_CHANNEL,
		LIVE_TYPE,
		VOD_TOP_CATEGORY, 
		VOD_SEC_CATEGORY, 
		SPECIAL_CATEGORY,
		CATEGORY, 
		HOTEL_TOP_CATEGORY,
		HOTEL_SEC_CATEGORY,
		UNKNOW;

		public static ContentType createType(int type) {
			switch (type) {
			case 100:
				return ContentType.HOME_MOVIE;
			case 101:
				return ContentType.HOME_SERIES;
			case 102:
				return ContentType.HOME_HOTEL_INTRODUCE;
			case 103:	
				return ContentType.HOME_SPECIAL;
			case 3:
				return ContentType.VIDEO;
			case 4:
				return ContentType.LIVE_CHANNEL;
			case 5:
				return ContentType.LIVE_TYPE;
			case 200:
				return ContentType.HOTEL_TOP_CATEGORY;
			case 201:
				return ContentType.HOTEL_SEC_CATEGORY;	
			case 300:
				return ContentType.VOD_TOP_CATEGORY;
			case 301:
				return ContentType.VOD_SEC_CATEGORY;
			case 303:
				return ContentType.SPECIAL_CATEGORY;	
			case 400:
				return ContentType.SPECIAL_CATEGORY;
			case 8:
				return ContentType.CATEGORY;
			default:
				return ContentType.UNKNOW;
			}
		}

		public static ContentType convertStr(String str) {
			if ("HOME_MOVIE".equalsIgnoreCase(str)) {
				return ContentType.HOME_MOVIE;
			} else if ("HOME_SERIES".equalsIgnoreCase(str)) {
				return ContentType.HOME_SERIES;	
			} else if ("HOME_HOTEL_INTRODUCE".equalsIgnoreCase(str)) {
				return ContentType.HOME_HOTEL_INTRODUCE;	
			} else if ("HOME_SPECIAL".equalsIgnoreCase(str)) {
				return ContentType.HOME_SPECIAL;	
			} else if ("VIDEO".equalsIgnoreCase(str)) {
				return ContentType.VIDEO;
			} else if ("LIVE_CHANNEL".equalsIgnoreCase(str)) {
				return ContentType.LIVE_CHANNEL;
			} else if ("LIVE_TYPE".equalsIgnoreCase(str)) {
				return ContentType.LIVE_TYPE;
			} else if ("VOD_TOP_CATEGORY".equalsIgnoreCase(str)) {
				return ContentType.VOD_TOP_CATEGORY;
			} else if ("VOD_SEC_CATEGORY".equalsIgnoreCase(str)) {
				return ContentType.VOD_SEC_CATEGORY;
			} else if ("CATEGORY".equalsIgnoreCase(str)) {
				return ContentType.CATEGORY;
			} else if ("HOTEL_TOP_CATEGORY".equalsIgnoreCase(str)) {
				return ContentType.HOTEL_TOP_CATEGORY;
			} else if ("HOTEL_SEC_CATEGORY".equalsIgnoreCase(str)) {
				return ContentType.HOTEL_SEC_CATEGORY;
			} else if ("SPECIAL_CATEGORY".equalsIgnoreCase(str)) {
				return ContentType.SPECIAL_CATEGORY;	
			} else if ("CATEGORY".equalsIgnoreCase(str)) {
				return ContentType.CATEGORY;	
			} else if ("UNKNOW".equalsIgnoreCase(str)) {
				return ContentType.UNKNOW;
			} else {
				return ContentType.UNKNOW;
			}
		}
	};

	public static enum Platform {
		HOTEL, 			// 电影
		HUAWEI, 		
		RUNHUAWEI,
		DEVHUAWEI,
		ZTE,
		UNKNOW;

		/*public static Platform createPlatform(String type) {
			if(type != null){
				switch (type) {
				case "HOTEL":
					return Platform.HOTEL;
				case "HUAWEI":
					return Platform.HUAWEI;
				case "ZTE":
					return Platform.ZTE;
				default:
					return Platform.UNKNOW;
				}
			}else {
				return Platform.HUAWEI;
			}			
		}*/

		public static Platform createPlatform(String str) {
			if ("HOTEL".equalsIgnoreCase(str)) {
				return Platform.HOTEL;
			} else if ("HUAWEI".equalsIgnoreCase(str)) {
				return Platform.HUAWEI;	
			} else if ("RUNHUAWEI".equalsIgnoreCase(str)) {
				return Platform.RUNHUAWEI;		
			} else if ("DEVHUAWEI".equalsIgnoreCase(str)) {
				return Platform.DEVHUAWEI;			
			} else if ("ZTE".equalsIgnoreCase(str)) {
				return Platform.ZTE;	
			} else {
				return Platform.UNKNOW;
			}
		}
		
		public static String createPlatform(Platform str) {
			if (Platform.HOTEL==str) {
				return "HOTEL";
			} else if (Platform.HUAWEI==str) {
				return "HUAWEI";	
			} else if (Platform.RUNHUAWEI==str) {
				return "RUNHUAWEI";		
			} else if (Platform.DEVHUAWEI==str) {
				return "DEVHUAWEI";			
			} else if (Platform.ZTE==str) {
				return "ZTE";	
			} else {
				return "UNKNOW";
			}
		}
	};


	

	public static String converContentType(ContentType type) {
		if (type == ContentType.HOME_MOVIE) {
			return "HOME_MOVIE";
		} else if (type == ContentType.HOME_SERIES) {
			return "HOME_SERIES";	
		} else if (type == ContentType.HOME_HOTEL_INTRODUCE) {
			return "HOME_HOTEL_INTRODUCE";		
		} else if (type == ContentType.HOME_SPECIAL) {
			return "HOME_SPECIAL";
		} else if (type == ContentType.VIDEO) {
			return "VIDEO";
		} else if (type == ContentType.LIVE_CHANNEL) {
			return "LIVE_CHANNEL";
		} else if (type == ContentType.LIVE_TYPE) {
			return "LIVE_TYPE";
		} else if (type == ContentType.VOD_TOP_CATEGORY) {
			return "VOD_TOP_CATEGORY";
		} else if (type == ContentType.VOD_SEC_CATEGORY) {
			return "VOD_SEC_CATEGORY";
		} else if (type == ContentType.HOTEL_TOP_CATEGORY) {
			return "HOTEL_TOP_CATEGORY";
		} else if (type == ContentType.HOTEL_SEC_CATEGORY) {
			return "HOTEL_SEC_CATEGORY";	
		} else if (type == ContentType.SPECIAL_CATEGORY) {
			return "SPECIAL_CATEGORY";	
		} else if (type == ContentType.CATEGORY) {
			return "CATEGORY";
		} else if (type == ContentType.UNKNOW) {
			return "UNKNOW";
		} else {
			return "UNKNOW";
		}
	}

	public static enum ItemType {
		COMMON,
		BACKIMAGE,
		ANIM,
		TOP,
		TOPVER,
		TOPHOR,
		SPECIAL;

		public static ItemType createType(String type) {
			if ("special".equals(type)) {
				return ItemType.SPECIAL;
			} else if ("backimage".equals(type)) {
				return ItemType.BACKIMAGE;
			} else if ("anim".equals(type)) {
				return ItemType.ANIM;
			} else if ("top".equals(type)) {
				return ItemType.TOP;
			} else if ("topver".equals(type)) {
				return ItemType.TOPVER;
			} else if ("tophor".equals(type)) {
				return ItemType.TOPHOR;
			}
			return ItemType.COMMON;
		}
	}

	/*
	// category_type int 	0：VOD；1：电视剧；2：专题；3：直播分类；4：直播频道；5：回看；6：收视记录；
	public static enum CategoryType {
		VOD,
		SERIES,
		SPECIAL,
		LIVETYPE,
		LIVECHANNEL,
		REC,
		VIEWED,
		UNKNOW;

		public static CategoryType createType(int type) {
			switch (type) {
			case 0:
				return CategoryType.VOD;
			case 1:
				return CategoryType.SERIES;
			case 2:
				return CategoryType.SPECIAL;
			case 3:
				return CategoryType.LIVETYPE;
			case 4:
				return CategoryType.LIVECHANNEL;
			case 5:
				return CategoryType.REC;
			case 6:
				return CategoryType.VIEWED;
			default:
				return CategoryType.UNKNOW;
			}
		}

		public static CategoryType convertStr(String str) {
			if ("VOD".equalsIgnoreCase(str)) {
				return CategoryType.VOD;
			} else if ("SERIES".equalsIgnoreCase(str)) {
				return CategoryType.SERIES;
			} else if ("SPECIAL".equalsIgnoreCase(str)) {
				return CategoryType.SPECIAL;
			} else if ("LIVETYPE".equalsIgnoreCase(str)) {
				return CategoryType.LIVETYPE;
			} else if ("LIVECHANNEL".equalsIgnoreCase(str)) {
				return CategoryType.LIVECHANNEL;
			} else if ("REC".equalsIgnoreCase(str)) {
				return CategoryType.REC;
			} else if ("VIEWED".equalsIgnoreCase(str)) {
				return CategoryType.VIEWED;
			} else if ("UNKNOW".equalsIgnoreCase(str)) {
				return CategoryType.UNKNOW;
			} else {
				return CategoryType.UNKNOW;
			}
		}
	};

	public static String converCategoryType(CategoryType type) {
		if (type == CategoryType.VOD) {
			return "VOD";
		} else if (type == CategoryType.SERIES) {
			return "SERIES";
		} else if (type == CategoryType.SPECIAL) {
			return "SPECIAL";
		} else if (type == CategoryType.LIVETYPE) {
			return "LIVETYPE";
		} else if (type == CategoryType.LIVECHANNEL) {
			return "LIVECHANNEL";
		} else if (type == CategoryType.REC) {
			return "REC";
		} else if (type == CategoryType.VIEWED) {
			return "VIEWED";
		} else if (type == CategoryType.UNKNOW) {
			return "UNKNOW";
		} else {
			return "UNKNOW";
		}
	}
	 */

	public static enum SubContentType {
		IPTV_SECOND_CATEGORY_LIST, 				// IPTV的二级分类列表
		IPTV_THIRD_CATEGORY_LIST, 				// IPTV的三级分类列表
		IPTV_HORIZONTAL_POSTER_CONTENT_lIST, 	// IPTV的横版海报内容列表
		IPTV_VIRTICAL_POSTER_CONTENT_lIST, 		// IPTV的纵版海报内容列表
		IPTV_PURE_TEXT_CONTENT_LIST, 			// IPTV的纯文字标题内容列表
		HOTEL_SECOND_CATEGORY_LIST, 			// IPTV的二级分类列表
		HOTEL_THIRD_CATEGORY_LIST, 
		HOTEL_HORIZONTAL_POSTER_CONTENT_lIST, 	// 酒店的横版内容列表
		HOTEL_VIRTICAL_POSTER_CONTENT_lIST, 
		HOTEL_PURE_TEXT_CONTENT_LIST, 

		IPTV_MOVIE,
		IPTV_SERIES,
		IPTV_SPECIAL,
		HOTEL_MOVIE,
		HOTEL_SERIES,
		HOTEL_SPECIAL,
		HOTEL_CLEAN,
		HOTEL_CHECKOUT,
		HOTEL_CAR,
		UNKNOW;

		public static SubContentType createType(int type) {
			switch (type) {
			case 300:
				return SubContentType.IPTV_SECOND_CATEGORY_LIST;
			case 301:
				return SubContentType.IPTV_THIRD_CATEGORY_LIST;
			case 302:
				return SubContentType.IPTV_HORIZONTAL_POSTER_CONTENT_lIST;
			case 303:
				return SubContentType.IPTV_VIRTICAL_POSTER_CONTENT_lIST;
			case 304:
				return SubContentType.IPTV_PURE_TEXT_CONTENT_LIST;
			case 310:
				return SubContentType.HOTEL_SECOND_CATEGORY_LIST;
			case 311:
				return SubContentType.HOTEL_THIRD_CATEGORY_LIST;
			case 312:
				return SubContentType.HOTEL_HORIZONTAL_POSTER_CONTENT_lIST;	
			case 313:
				return SubContentType.HOTEL_VIRTICAL_POSTER_CONTENT_lIST;	
			case 314:
				return SubContentType.HOTEL_PURE_TEXT_CONTENT_LIST;	
			case 320:
				return SubContentType.IPTV_MOVIE;
			case 321:
				return SubContentType.IPTV_SERIES;
			case 322:
				return SubContentType.IPTV_SPECIAL;
			case 330:
				return SubContentType.HOTEL_MOVIE;
			case 331:
				return SubContentType.HOTEL_SERIES;
			case 332:
				return SubContentType.HOTEL_SPECIAL;	
			case 340:
				return SubContentType.HOTEL_CLEAN;
			case 341:
				return SubContentType.HOTEL_CHECKOUT;
			case 342:
				return SubContentType.HOTEL_CAR;		
			default:
				return SubContentType.UNKNOW;
			}
		}
	};

	public static String converSubContentType(SubContentType type) {
		if (type == SubContentType.IPTV_SECOND_CATEGORY_LIST) {
			return "IPTV_SECOND_CATEGORY_LIST";
		} else if (type == SubContentType.IPTV_THIRD_CATEGORY_LIST) {
			return "IPTV_THIRD_CATEGORY_LIST";
		} else if (type == SubContentType.IPTV_HORIZONTAL_POSTER_CONTENT_lIST) {
			return "IPTV_HORIZONTAL_POSTER_CONTENT_lIST";
		} else if (type == SubContentType.IPTV_VIRTICAL_POSTER_CONTENT_lIST) {
			return "IPTV_VIRTICAL_POSTER_CONTENT_lIST";	
		} else if (type == SubContentType.IPTV_PURE_TEXT_CONTENT_LIST) {
			return "IPTV_PURE_TEXT_CONTENT_LIST";
		} else if (type == SubContentType.HOTEL_SECOND_CATEGORY_LIST) {
			return "HOTEL_SECOND_CATEGORY_LIST";	
		} else if (type == SubContentType.HOTEL_THIRD_CATEGORY_LIST) {
			return "HOTEL_THIRD_CATEGORY_LIST";		
		} else if (type == SubContentType.HOTEL_HORIZONTAL_POSTER_CONTENT_lIST) {
			return "HOTEL_HORIZONTAL_POSTER_CONTENT_lIST";	
		} else if (type == SubContentType.HOTEL_VIRTICAL_POSTER_CONTENT_lIST) {
			return "HOTEL_VIRTICAL_POSTER_CONTENT_lIST";
		} else if (type == SubContentType.HOTEL_PURE_TEXT_CONTENT_LIST) {
			return "HOTEL_PURE_TEXT_CONTENT_LIST";	
		} else if (type == SubContentType.IPTV_MOVIE) {
			return "IPTV_MOVIE";	
		} else if (type == SubContentType.IPTV_SERIES) {
			return "IPTV_SERIES";	
		} else if (type == SubContentType.IPTV_SPECIAL) {
			return "IPTV_SPECIAL";	
		} else if (type == SubContentType.HOTEL_MOVIE) {
			return "HOTEL_MOVIE";	
		} else if (type == SubContentType.HOTEL_SERIES) {
			return "HOTEL_SERIES";	
		} else if (type == SubContentType.HOTEL_SPECIAL) {
			return "HOTEL_SPECIAL";		
		} else if (type == SubContentType.HOTEL_CLEAN) {
			return "HOTEL_CLEAN";	
		} else if (type == SubContentType.HOTEL_CHECKOUT) {
			return "HOTEL_CHECKOUT";	
		} else if (type == SubContentType.HOTEL_CAR) {
			return "HOTEL_CAR";			
		} else if (type == SubContentType.UNKNOW) {
			return "UNKNOW";
		} else {
			return "UNKNOW";
		}
	}

	// 我的酒店的第三层类型
	public static enum MyHotelSubContentType {
		MYHOTEL_SECOND_CATEGORY_LIST,
		MYHOTEL_PICTURE_LIST,					// 详细内容图片列表形式
		MYHOTEL_SPECIAL, 						// 酒店介绍专题风格
		MYHOTEL_MOVIE,							// 酒店介绍视频
		HOTEL_SECOND_CATEGORY_LIST, 			// IPTV的二级分类列表
		HOTEL_THIRD_CATEGORY_LIST, 
		HOTEL_HORIZONTAL_POSTER_CONTENT_lIST, 	// 酒店的横版内容列表
		HOTEL_VIRTICAL_POSTER_CONTENT_lIST, 
		UNKNOW;

		public static MyHotelSubContentType createType(int type) {
			switch (type) {
			case 200:
				return MyHotelSubContentType.MYHOTEL_SECOND_CATEGORY_LIST;
			case 201:
				return MyHotelSubContentType.MYHOTEL_PICTURE_LIST;
			case 202:
				return MyHotelSubContentType.MYHOTEL_SPECIAL;
			case 203:
				return MyHotelSubContentType.MYHOTEL_MOVIE;
			case 310:
				return MyHotelSubContentType.HOTEL_SECOND_CATEGORY_LIST;
			case 311:
				return MyHotelSubContentType.HOTEL_THIRD_CATEGORY_LIST;
			case 312:
				return MyHotelSubContentType.HOTEL_HORIZONTAL_POSTER_CONTENT_lIST;	
			case 313:
				return MyHotelSubContentType.HOTEL_VIRTICAL_POSTER_CONTENT_lIST;		
			default:
				return MyHotelSubContentType.UNKNOW;
			}
		}

		public static MyHotelSubContentType convertStr(String str) {
			if ("MYHOTEL_SECOND_CATEGORY_LIST".equalsIgnoreCase(str)) {
				return MyHotelSubContentType.MYHOTEL_SECOND_CATEGORY_LIST;
			} else if ("MYHOTEL_PICTURE_LIST".equalsIgnoreCase(str)) {
				return MyHotelSubContentType.MYHOTEL_PICTURE_LIST;
			} else if ("MYHOTEL_SPECIAL".equalsIgnoreCase("str")) {
				return MyHotelSubContentType.MYHOTEL_SPECIAL;
			} else if ("MYHOTEL_MOVIE".equalsIgnoreCase("str")) {
				return MyHotelSubContentType.MYHOTEL_MOVIE;
			} else if ("HOTEL_SECOND_CATEGORY_LIST".equalsIgnoreCase("str")) {
				return MyHotelSubContentType.HOTEL_SECOND_CATEGORY_LIST;	
			} else if ("HOTEL_THIRD_CATEGORY_LIST".equalsIgnoreCase("str")) {
				return MyHotelSubContentType.HOTEL_THIRD_CATEGORY_LIST;		
			} else if ("HOTEL_HORIZONTAL_POSTER_CONTENT_lIST".equalsIgnoreCase("str")) {
				return MyHotelSubContentType.HOTEL_HORIZONTAL_POSTER_CONTENT_lIST;	
			} else if ("HOTEL_VIRTICAL_POSTER_CONTENT_lIST".equalsIgnoreCase("str")) {
				return MyHotelSubContentType.HOTEL_VIRTICAL_POSTER_CONTENT_lIST;	
			} else {
				return MyHotelSubContentType.UNKNOW;
			}
		}
	};


	public static String converMyHotelSubContentType(MyHotelSubContentType type) {
		if (type == MyHotelSubContentType.MYHOTEL_SECOND_CATEGORY_LIST) {
			return "MYHOTEL_SECOND_CATEGORY_LIST";
		} else if (type == MyHotelSubContentType.MYHOTEL_PICTURE_LIST) {
			return "MYHOTEL_PICTURE_LIST";
		} else if (type == MyHotelSubContentType.MYHOTEL_SPECIAL) {
			return "MYHOTEL_SPECIAL";
		} else if (type == MyHotelSubContentType.MYHOTEL_MOVIE) {
			return "MYHOTEL_MOVIE";
		} else if (type == MyHotelSubContentType.HOTEL_SECOND_CATEGORY_LIST) {
			return "HOTEL_SECOND_CATEGORY_LIST";	
		} else if (type == MyHotelSubContentType.HOTEL_THIRD_CATEGORY_LIST) {
			return "HOTEL_THIRD_CATEGORY_LIST";		
		} else if (type == MyHotelSubContentType.HOTEL_HORIZONTAL_POSTER_CONTENT_lIST) {
			return "HOTEL_HORIZONTAL_POSTER_CONTENT_lIST";	
		} else if (type == MyHotelSubContentType.HOTEL_VIRTICAL_POSTER_CONTENT_lIST) {
			return "HOTEL_VIRTICAL_POSTER_CONTENT_lIST";	
		} else {
			return "UNKNOW";
		}
	}

	public static enum StbOrientation {
		Horizonal,
		Virtical270,
		Virtical90;

		public static StbOrientation create(String orientation){
			switch (orientation) {
			case "Virtical90":
				return StbOrientation.Virtical90;
			case "Virtical270":
				return StbOrientation.Virtical270;
			case "Horizonal":
				return StbOrientation.Horizonal;
			default:
				return StbOrientation.Horizonal;
			}
		}
	}
	
	/**
	 * rebbit MQ 消息类型
	 * @author LDD
	 *
	 */
	public static enum NoticeType{
		IDS,
		ROOMNOTICE,
		WECHATPAY,
		UNKNOW;

		public static NoticeType createNoticeType(String str){
			if ("IDS".equalsIgnoreCase(str)) {
				return NoticeType.IDS;
			} else if ("ROOMNOTICE".equalsIgnoreCase(str)) {
				return NoticeType.ROOMNOTICE;	
			} else if ("WECHATPAY".equalsIgnoreCase(str)) {
				return NoticeType.WECHATPAY;		
			}else {
				return NoticeType.UNKNOW;
			}
		}
	}

}
