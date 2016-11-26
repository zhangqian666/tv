package com.iptv.rocky.hwdata;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;

import android.text.TextUtils;

import com.iptv.common.data.EnumType;
import com.iptv.common.utils.LogUtils;
import com.iptv.rocky.auth.AuthService.AuthServiceBinder;
import com.iptv.rocky.model.TvApplication;

public final class IPTVUriUtils {
	
	public static void clear() {
		if(TvApplication.platform == EnumType.Platform.ZTE){
			zteHost = "/iptvepg/";
		}else if(TvApplication.platform == EnumType.Platform.DEVHUAWEI || TvApplication.platform == EnumType.Platform.RUNHUAWEI){
			huaweiHost = "/EPG/jsp/gdgaoqing/en/";
		}
		HotelHost="/hotel/";
		hotelLoginHost="login/first";
		IptvAccountInitHost = "login/initbystb";
		CheckIptvUpDateHost ="update/checkupdate";
		ChannelListHost = "ut/hw_UT_channel.jsp?Action=get3aChannelList";
		ChannelListByTypeIdHost = "ut/hw_UT.jsp?Action=getChanListByTypeId&typeId=-1&length=1000&station=0&channelType=-1";
		PortalHomeHost = "recommend/group_xml";
		PortalChannelsHost = "channel/classificationxml";
		myHotelTopLevelHost= "myhotel/categoryxml";
		myHotelSecHost = "myhotel/categoryxml";
		myHotelPictureListHost ="myhotel/detailxml";
		ChannelListPublic = "channel/channelxml";
		ChannelTypeHost = "ut/hw_UT.jsp?Action=getChannelType";
		ChannelInfoHost = "ut/hw_UT.jsp?Action=getChannelInfo";
		ProgBillHost = "ut/hw_UT.jsp?Action=getProgBill";
		AllChannelProgBillHost = "ut/hw_GetAllChannelProgBill.jsp?lang="+TvApplication.language+"&length=200&station=0";
		VodTriggerPlayUrlHost = "ut/hw_UT_vod.jsp?Action=getTriggerPlayUrlHWCTC&beginTime=0&endTime=20000";
		HotelPlayUrlHost = "vod/playUrl";
		VodTopHost = "vod/categories_top_xml";
		VodSecHost = "vod/categories_second_xml";
		VodThdHost = "ut/hw_UT_vod.jsp";
		VodThdHostJSON = "ut/vod_getCategoriesThirdJson.jsp";
		
		VodChannelListHost = "ut/hw_UT_vod.jsp";
		HotelProgramListHost = "vod/programlist";
		VodDetailInfoHost = "ut/hw_UT_vod.jsp?Action=getVodDetailInfo";
		HotelProgramDetailHost ="vod/program";
		HotelProgramDetaiJsonlHost ="vod/programjson";
		VodPlayRecordReportHost = "vod_play_record/report";
		LiveChannelPlayRecordReportHost = "channel_play_record/report";
		orderVodHost ="order/order_vod";
		orderVodConfirmPasswordHost ="order/confirm_password_and_order";
		orderOneDayPackageHost = "order/order_day";
		vodDetailCheckOrderStatusHost = "order/check_vod_order_status";
		VodSearchHost = "ut/hw_UT_search.jsp?Action=searchFilmsByCode&asSubVod=0";
		RecChanHost = "ut/hw_UT_schedule.jsp?Action=getRecChan&length=1000&station=0";
		RecBillHost = "ut/schedule_recbill.jsp";
		RecTriggerPlayUrlHost = "ut/hw_UT_schedule.jsp?Action=getTriggerPlayUrlHWCTC&beginTime=0&endTime=20000";
		screenProtect ="screen_protect/detail";
		advertisement ="advertisement/detail";
		videoScreenProtect ="screen_protect/video_detail";
		welcomeInfo ="welcome/detail";
		
		dashboardHost ="dashboard/list";
		homePromptHost ="home_prompt/list";

		vodGetPlayUrlJson="ut/vodGetPlayUrl.jsp";
		vodGetVodTailinfo="ut/vodGetVodTailinfo.jsp";
		 getSitcomList="ut/getSitcomList.jsp";
		 vodGetVodListByTypeIdZTE="ut/vodGetVodListByTypeIdZTE.jsp";
		 getReBillUrl="ut/getReBillUrl.jsp";
		 getChannelList="ut/getChannelList.jsp";
		 getChanBill="ut/getChanBill.jsp";
		 VodSearchByCode="ut/VodSearchByCode.jsp";
		 AllChannelProgBillHostZTE = "ut/zte_GetAllChannelProgBill.jsp";
	}
	
	public static AuthServiceBinder mAuthServiceBinder = null;
	public static final String EXTRA_PARAMS = "params";
	public static final String ORDER_STATUS = "orderstatus";
	public static final String SIMPLIFIED_CHINESE_CODE = "zh";
	public static final String ENGLISH_CODE = "en";
	public static final String PLAY_URL_PARAMS = "play_url_params";
	public static final String SUB_VOD_CHANNELID_PARAMS = "sub_vod_channelid_params";
	public static final String RECBILL_PARAMS = "recbill_params";
	public static String EpgHost = "";
	
	//Hotel's information server url;
	public static String hotelEpgHost = TvApplication.hotelEpgHost;

	public static String Host ="";
	//中兴
	public static String zteHost = "/iptvepg/";
	public static String huaweiHost = "/EPG/jsp/gdgaoqing/en/";
	public static String HotelHost = "/hotel/";
	public static String IptvAccountInitHost = "login/initbystb";
	public static String hotelLoginHost = "login/first";
	
	// 获取客户端的模块儿配置情况
	//public static String hotelColumnEnabledJsonHost ="config/modelsxml";
	public static String ChannelListHost = "ut/hw_UT_channel.jsp?Action=get3aChannelList";

	// 公共的频道
	public static String ChannelListPublic = "channel/channelxml";
	//public static String CheckUpdataHost = "update/updateinfo";
	public static String CheckIptvUpDateHost ="update/checkupdate";
	/*
		typeId： 栏目ID -1 提取所有栏目的频道
		length： 返回结果最大数>=0
		station： 查询开始位置>=0
		channelType：频道类型 ， 1：视频频道；2：音频频道；3：页面频道；-1：所有频道
	*/
	public static String ChannelListByTypeIdHost = "ut/hw_UT.jsp?Action=getChanListByTypeId&typeId=-1&length=1000&station=0&channelType=-1";
	
	// 首页推荐
	public static String PortalHomeHost = "recommend/group_xml";
	
	// 直播分类和推荐频道信息
	public static String PortalChannelsHost = "channel/classificationxml";
	
	// 我的酒店的一级分类
	public static String myHotelTopLevelHost = "myhotel/categoryxml";
	public static String myHotelSecHost = "myhotel/categoryxml";
	// 酒店介绍图片列表
	public static String myHotelPictureListHost = "myhotel/detailxml?userid=" + TvApplication.account + "&lang=" + TvApplication.language;

	// 直播分类
	public static String ChannelTypeHost = "ut/hw_UT.jsp?Action=getChannelType";
	
	// 直播频道信息
	public static String ChannelInfoHost = "ut/hw_UT.jsp?Action=getChannelInfo&chanId=156";
	
	// 直播频道节目单信息
	public static String ProgBillHost = "ut/hw_UT.jsp?Action=getProgBill";
	
	// 所有直播频道节目单信息
	public static String AllChannelProgBillHost = "ut/hw_GetAllChannelProgBill.jsp?lang="+TvApplication.language+"&length=200&station=0";

	/**
	 * 获取直播频道节目单信息__中兴
	 */
	public static String AllChannelProgBillHostZTE = "ut/zte_GetAllChannelProgBill.jsp";

	/**
		playType int 播放类型： 
			1：VOD播放
			2：TV播放
			3：NVOD播放
			4：TVOD播放
			5：片花播放
			6：书签播放
			7：时移播放
			8：直播、轮播频道的节目播放
			9：直播、轮播频道的节目PVR播放
			10：增值业务
		progId int 节目编号，可以是频道和VOD
		playBillId int 节目单编号（可选参数），仅当progId是频道时有效
		beginTime String 播放开始时间
		格式为：YYYYMMDDhhmmss
		endTime String 播放结束时间
		格式为：YYYYMMDDhhmmss
		productId String 产品编号
		serviceId String 服务编号
		contentType String 内容类型：
			0：视频VOD
			1：视频频道
			2：音频频道
			3：频道
			4：音频VOD
			10：VOD
			100：增值业务
			200：栏目
			300：频道节目 
			9999：混合内容
		categoryId String 栏目ID
	 */
	// 点播播放地址
	public static String VodTriggerPlayUrlHost = "ut/hw_UT_vod.jsp?Action=getTriggerPlayUrlHWCTC&beginTime=0&endTime=20000";
	public static String HotelPlayUrlHost = "vod/playUrl";
	public static String VodTriggerPlayUrlHostJson = "ut/vod_getPlayUrlJson.jsp";
	/**
	 * 中兴获取点播 播放URL
	 */
	public static String vodGetPlayUrlJson="ut/vodGetPlayUrl.jsp";
	
/**
 *中兴获取点播详情
 */
	public static String vodGetVodTailinfo="ut/vodGetVodTailinfo.jsp";
	
	/**
	 * 中兴获取三级列表
	 */
	public static String zte_getCategoriesThirdJson="ut/zte_getCategoriesThirdJson.jsp";
	
	/**
	 * 获取电视剧列表
	 */
	public static String getSitcomList="ut/getSitcomList.jsp";
	
	/**
	 * 中兴获取节目列表
	 */
	public static String vodGetVodListByTypeIdZTE="ut/vodGetVodListByTypeIdZTE.jsp";
	
	/**
	 * 中兴回看播放URL
	 */
	public static String getReBillUrl="ut/getReBillUrl.jsp";
	
	/**
	 * 中兴回看节目列表
	 */
	public static String getChannelList="ut/getChannelList.jsp";
	
	/**
	 * 中兴会看节目
	 */
	public static String getChanBill="ut/getChanBill.jsp";
	/**
	 * 根据节目名称首字母查询
	 */
	public static String VodSearchByCode="ut/VodSearchByCode.jsp";
	// 点播一级分类
	public static String VodTopHost = "vod/categories_top_xml";
	public static String VodSecHost = "vod/categories_second_xml";
	public static String VodThdHost = "ut/hw_UT_vod.jsp";
	//JSON格式
	public static String VodThdHostJSON = "ut/vod_getCategoriesThirdJson.jsp";
		
	// 点播列表 
	public static String VodChannelListHost = "ut/hw_UT_vod.jsp";
	
	//点拨列表 JSON
	public static String VodChannelListHostJson = "ut/vod_getVodListByTypeIdJson.jsp";
	
	public static String HotelProgramListHost = "vod/programlist";
	// 点播详情
	public static String VodDetailInfoHost = "ut/hw_UT_vod.jsp?Action=getVodDetailInfo";
	/**
	 * 节目鉴权地址
	 */
	public static String VodAuthJson="ut/auth_z.jsp";
	//李东东添加 
	public static String VodDetailInfoHostJson = "ut/vod_getVodDetailInfo.jsp";
	public static String HotelProgramDetailHost ="vod/program";
	public static String HotelProgramDetaiJsonlHost ="vod/programjson";
	// 点播播放记录上报
	public static String VodPlayRecordReportHost = "vod_play_record/report";
	// 直播播放记录上报
	public static String LiveChannelPlayRecordReportHost = "channel_play_record/report";
	// 业务订购，订购单片VOD
	public static String orderVodHost ="order/order_vod";
	
	// 屏幕保护
	public static String screenProtect ="screen_protect/detail";
	
	public static String advertisement ="advertisement/detail";
	
	public static String videoScreenProtect ="screen_protect/video_detail";
	// 欢迎界面
	public static String welcomeInfo ="welcome/detail";
	public static String orderVodConfirmPasswordHost ="order/confirm_password_and_order";
	public static String vodDetailCheckOrderStatusHost = "order/check_vod_order_status";
	// 订购包天点播包
	public static String orderOneDayPackageHost = "order/order_day";
	// 首字母搜索
	public static String VodSearchHost = "ut/hw_UT_search.jsp?Action=searchFilmsByCode&asSubVod=0";
	// 回看频道列表
	public static String RecChanHost = "ut/hw_UT_schedule.jsp?Action=getRecChan&length=1000&station=0";
	//lidongdong ADD JSON
	public static String RecChanHostJson = "ut/sch_getrecchanjson.jsp";
	// 频道的回看节目单
	public static String RecBillHost = "ut/schedule_recbill.jsp";
	//ldd add JSON
	public static String RecBillHostJson = "ut/sch_getrecbilljson.jsp"; // ?statusFlag=1
	// 回看播放地址
	public static String RecTriggerPlayUrlHost = "ut/hw_UT_schedule.jsp?Action=getTriggerPlayUrlHWCTC&beginTime=0&endTime=20000";
	//LDD ADD JSON
	public static String CmsHost = "http://tv.api.pptv.com/";
	public static String RecTriggerPlayUrlHostJson = "ut/sch_getplayurljson.jsp";
	
	public static String dashboardHost ="dashboard/list";
	public static String homePromptHost ="home_prompt/list";
	
	public static Header[] getHeader()
	{		
		Header[] headers = null;
		try {
			if (mAuthServiceBinder != null && !TextUtils.isEmpty(mAuthServiceBinder.getEPGSessionId()))
			{
				headers = new Header[] { new BasicHeader("Cookie","JSESSIONID="+ mAuthServiceBinder.getEPGSessionId()+ ";") };
			}
		} catch (Exception e) {
			LogUtils.error(e.toString());
		}
		
		return headers;
	}
	
}
