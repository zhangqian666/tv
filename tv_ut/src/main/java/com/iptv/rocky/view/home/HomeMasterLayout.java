package com.iptv.rocky.view.home;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.iptv.common.HttpEventHandler;
import com.iptv.common.data.EnumType;
import com.iptv.common.data.EnumType.Platform;
import com.iptv.common.data.LiveChannel;
import com.iptv.common.data.LiveChannelBill;
import com.iptv.common.data.MyHotelTop;
import com.iptv.common.data.PortalChannels;
import com.iptv.common.data.PortalHome;
import com.iptv.common.data.StbType;
import com.iptv.common.data.VodTop;
import com.iptv.common.utils.LogUtils;
import com.iptv.common.utils.TimeUtil;
import com.iptv.rocky.DashboardActivity;
import com.iptv.rocky.HomeActivity;
import com.iptv.rocky.R;
import com.iptv.rocky.hwdata.IPTVUriUtils;
import com.iptv.rocky.hwdata.json.AllChannelProgBilJsonlFactory;
import com.iptv.rocky.hwdata.local.AAALiveChannelsLocalFactory;
import com.iptv.rocky.hwdata.local.LiveAllChannelBillLocalFactory;
import com.iptv.rocky.hwdata.local.LiveTypeLocalFactory;
import com.iptv.rocky.hwdata.local.PortalHomeLiveChannelsLocalFactory;
import com.iptv.rocky.hwdata.local.PortalHomeLocalFactory;
import com.iptv.rocky.hwdata.local.PortalHomeMyHotelTopFactory;
import com.iptv.rocky.hwdata.local.PortalHomeVodTopLocalFactory;
import com.iptv.rocky.hwdata.xml.AllChannelProgBillFactory;
import com.iptv.rocky.hwdata.xml.MyHotelTopFactory;
import com.iptv.rocky.hwdata.xml.PortalHomeFactory;
import com.iptv.rocky.hwdata.xml.PortalLiveChannelsFactory;
import com.iptv.rocky.hwdata.xml.PublicLiveChannelsFactory;
import com.iptv.rocky.hwdata.xml.VodTopFactory;
import com.iptv.rocky.model.TvApplication;
import com.iptv.rocky.model.home.HomeLayoutFactory;
import com.iptv.rocky.model.home.HomeLayoutItem;
import com.iptv.rocky.utils.TvUtils;
import com.iptv.rocky.view.TabView;

public class HomeMasterLayout extends RelativeLayout implements ViewPager.OnPageChangeListener, TabView.SelectChangedListener {
	
	public TabView mTabView;
	private HomeViewPager mPageView;
	public ProgressBar mProgressBar;
	
	private static int homeIndex =0;
	private  static int liveIndex =1;
	private  static int vodIndex =2;
	private  static int myHotelIndex =3;
	private static int travelIndex =4;
	// private  static int hotelServiceIndex =4;
	
	// 读取配置文件布局首页
	private HomeLayoutFactory mHomeLayoutFactory;
	// 推荐首页
	private PortalHomeFactory mPortalHomeFactory;
	
	// 直播
	private PortalLiveChannelsFactory recommendChannelAndClassificationFactory;
	
	// 主要为了切换中英文频道名字
	private PublicLiveChannelsFactory mPublicLiveChannelsFactory;
	
	// 点播
	private VodTopFactory mVodTopFactory;
	
	private MyHotelTopFactory mMyHotelTopFactory;
	
	private MyHotelTopFactory mTravelFactory;
	
	// 推荐首页缓存
	private PortalHomeLocalFactory mPortalHomeLocalFactory;
	// 直播
	private PortalHomeLiveChannelsLocalFactory mPortalHomeLiveChannelsLocalFactory;
	// 点播
	private PortalHomeVodTopLocalFactory mPortalHomeVodTopLocalFactory;
	// 酒店
	private PortalHomeMyHotelTopFactory mPortalHomeMyHotelTopLevelFactory;
	
	public HomeMasterLayout(Context context) {
		this(context, null, 0);
	}
	
	public HomeMasterLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public HomeMasterLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	public void destroy() {
		mPageView.destroy();
	}
	
	public void resume() {
		mTabView.autoFocusStart();
	}
	
	public void pause() {
		mTabView.autoFocusStop();
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		
		mTabView = (TabView)findViewById(R.id.tv_tablayout);
		mPageView = (HomeViewPager)findViewById(R.id.home_viewpage);
		mProgressBar = (ProgressBar) findViewById(R.id.tv_progressbar);
		
		mTabView.setSelectChanngeListener(this);
		mPageView.setOnPageChangeListener(this);
	}
	
	public void DownloadDatas() {
//		LogUtils.error("stbType:"+TvApplication.stbType);
		if(TvApplication.stbType == StbType.IPTV_STB){
			mHomeLayoutFactory = new HomeLayoutFactory(getContext());
			mHomeLayoutFactory.setHttpEventHandler(homeLayoutHandler);
			
			
			// 推荐首页
			mPortalHomeFactory = new PortalHomeFactory();
			mPortalHomeFactory.setHttpEventHandler(homeHandler);
			mPortalHomeFactory.setHttpHeaderParams(IPTVUriUtils.getHeader());
			
			// 直播
			recommendChannelAndClassificationFactory = new PortalLiveChannelsFactory();
			recommendChannelAndClassificationFactory.setHttpEventHandler(portalLiveChannelsAndChannelsTypeHandler);
			//recommendChannelAndClassificationFactory.setHttpHeaderParams(IPTVUriUtils.getHeader());
			
			// 公共频道的补充信息，公共频道的中英文名字
			mPublicLiveChannelsFactory = new PublicLiveChannelsFactory();
			mPublicLiveChannelsFactory.setHttpEventHandler(publicLiveChannelsHandler);
			//mPublicLiveChannelsFactory.setHttpHeaderParams(IPTVUriUtils.getHeader());
			
			// 点播
			mVodTopFactory = new VodTopFactory();
			mVodTopFactory.setHttpEventHandler(vodTopCategoryHandler);
			//mVodTopFactory.setHttpHeaderParams(IPTVUriUtils.getHeader());
			
			// 旅游
			mTravelFactory = new MyHotelTopFactory();
			mTravelFactory.setHttpEventHandler(travelTopHandler);
			
			
			// 我的酒店
			mMyHotelTopFactory = new MyHotelTopFactory();
			mMyHotelTopFactory.setHttpEventHandler(myHotelTopHandler);
			//mMyHotelTopFactory.setHttpHeaderParams(IPTVUriUtils.getHeader());
			
			mPortalHomeLocalFactory = new PortalHomeLocalFactory(getContext());
			mPortalHomeLiveChannelsLocalFactory = new PortalHomeLiveChannelsLocalFactory(getContext());
			mPortalHomeVodTopLocalFactory = new PortalHomeVodTopLocalFactory(getContext());
			mPortalHomeMyHotelTopLevelFactory = new PortalHomeMyHotelTopFactory(getContext()); 		// 酒店介绍
			//LogUtils.error("开始下载 mHomeLayoutFactory 数据");
			mHomeLayoutFactory.DownloadDatas();
			
		}else if(TvApplication.stbType == StbType.DASHBOARD_STB){
			LogUtils.debug("告示类型，开始启动");
			Intent intent = new Intent(getContext(), DashboardActivity.class);
			getContext().startActivity(intent);
		}
	}

	@Override
	public void onChange(int index) {
//		LogUtils.error("OnChange:"+index);
		if (mPageView.getCurrentItem() != index) {
			mPageView.setCurrentItem(index);
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	@Override
	public void onPageSelected(int arg0) {
		LogUtils.error("onPageSelected : arg0:"+ arg0);
		mTabView.tabSelected(arg0);
	}

	@Override
	protected boolean onRequestFocusInDescendants(int direction,
			Rect previouslyFocusedRect) {
		return mPageView.requestFocus(direction, previouslyFocusedRect);
	}
	
	private boolean isHomeLayoutHandlerReady = false;
	
	private boolean isHomeHandlerReady = false;
	
	private boolean isPortalLiveChannelsAndChannelsTypeHandlerReady = false;
	
	private boolean isChannelProgramBillHandlerReady = false;
	
	private boolean isPublicLiveChannelsHandlerReady = false;
	
	private boolean isVodTopCategoryHandlerReady = false;
	
	private boolean isMyHotelTopHandlerReady = false;
	private boolean isTravelHandlerReady = false;
	
	
	private boolean isReady() {
		return isHomeLayoutHandlerReady && isHomeHandlerReady && isPortalLiveChannelsAndChannelsTypeHandlerReady
				&& isChannelProgramBillHandlerReady && isPublicLiveChannelsHandlerReady && isVodTopCategoryHandlerReady
				&& isMyHotelTopHandlerReady;
	}
	
	private HttpEventHandler<ArrayList<HomeLayoutItem>> homeLayoutHandler = new HttpEventHandler<ArrayList<HomeLayoutItem>>() {
		
		@Override
		public void HttpSucessHandler(ArrayList<HomeLayoutItem> result) {
			mTabView.createView(result);
			mPageView.createView(result);
			
			//此处的序列号应该管用
			mPageView.createView(homeIndex, getFromDb(0), false);
			mPageView.createLiveView(liveIndex, getFromLiveDb(0), false);
			mPageView.createVodView(vodIndex, getFromVodTopDb(0), false);
			mPageView.createMyHotelView(myHotelIndex, getFromMyHotelDb(0), false);//自己加创建酒店的
			// mPageView.createTravelView(travelIndex, getFromMyHotelDb(0), false);//自己加创建酒店的
			recommendChannelAndClassificationFactory.DownloadDatas(TvApplication.account, TvApplication.language);
			mVodTopFactory.DownloadDatas(TvApplication.account, TvApplication.language);
			mPortalHomeFactory.DownloadDatas(TvApplication.account, TvApplication.language);
			
			mMyHotelTopFactory.DownloadDatas(TvApplication.account, TvApplication.language, "HOTEL");
			mTravelFactory.DownloadDatas(TvApplication.account, TvApplication.language, "TRAVEL");
			
			mPublicLiveChannelsFactory.DownloadDatas(TvApplication.account, TvApplication.language);
			mTabView.autoFocusStart();
			isHomeLayoutHandlerReady = true;
			TvApplication.status="FREE";
			TvApplication.position="HOME_ACTIVITY";
		}
		
		@Override
		public void HttpFailHandler() {
			LogUtils.debug("homeLayoutHandler HttpFailHandler");
			mProgressBar.setVisibility(View.GONE);
			TvUtils.processHttpFail(getContext());
		}
	};
	
	private HttpEventHandler<ArrayList<PortalHome>> homeHandler = new HttpEventHandler<ArrayList<PortalHome>>() {

		@Override
		public void HttpSucessHandler(ArrayList<PortalHome> result) {
			ArrayList<PortalHome> homeInfos = HomeLayoutFactory.insertInfoToProtal(result);
			mPageView.createView(homeIndex, homeInfos, true);
			insertToDb(result, 0);
			
			//requestChildFocus(mPageView, mPageView.findFocus());
			
			isHomeHandlerReady = true;
			
			if (isReady())
				mProgressBar.setVisibility(View.GONE);
		}

		@Override
		public void HttpFailHandler() {
			mProgressBar.setVisibility(View.GONE);
			TvUtils.processHttpFail(getContext());
		}
	};
	
	private HttpEventHandler<PortalChannels> portalLiveChannelsAndChannelsTypeHandler = new HttpEventHandler<PortalChannels>() {

		@Override
		public void HttpSucessHandler(PortalChannels result) {			
			// LogUtils.info("提取到服务器的推荐频道和直播分类信息");
			
			mPageView.createLiveView(liveIndex, result, false);
			insertToLiveDb(result, 0);
			
			// 保存到数据库-这是新增的一个数据库，只存储直播分类，其实可以删除，全部用 PortalLiveChannelsFactory
			LiveTypeLocalFactory liveTypeLocalFactory = new LiveTypeLocalFactory(getContext());
			liveTypeLocalFactory.insertLiveTypeInfos(result.lstPortalLiveType);
			isPortalLiveChannelsAndChannelsTypeHandlerReady = true;
			
			if (isReady())
				mProgressBar.setVisibility(View.GONE);
		}

		@Override
		public void HttpFailHandler() {
			mProgressBar.setVisibility(View.GONE);
			TvUtils.processHttpFail(getContext());
		}
	};
	
	private HttpEventHandler<ArrayList<LiveChannelBill>> channelProgramBillHandler = new HttpEventHandler<ArrayList<LiveChannelBill>>() {

		@Override
		public void HttpSucessHandler(ArrayList<LiveChannelBill> result) {
			isChannelProgramBillHandlerReady = true;
			if (isReady())
				mProgressBar.setVisibility(View.GONE);
		}

		@Override
		public void HttpFailHandler() {
			mProgressBar.setVisibility(View.GONE);
			TvUtils.processHttpFail(getContext());
		}
	};
	
	private HttpEventHandler<ArrayList<LiveChannelBill>> publicLiveChannelsHandler = new HttpEventHandler<ArrayList<LiveChannelBill>>() {

		@Override
		public void HttpSucessHandler(ArrayList<LiveChannelBill> result) {			
				if (HomeActivity.mapLiveChannel == null) {
					HomeActivity.mapLiveChannel = new LinkedHashMap<String, LiveChannel>();
				}
			
			AAALiveChannelsLocalFactory aaaLiveChannelsLocalFactory = new AAALiveChannelsLocalFactory(getContext());
			
			for (LiveChannelBill liveChannel : result) {
				if (HomeActivity.mapLiveChannel.containsKey(liveChannel.ChannelID)) {		
					LiveChannel liveChannelRecord = aaaLiveChannelsLocalFactory.findRecordZ(liveChannel.ChannelID);
					if (liveChannelRecord != null) {
//						LogUtils.debug("liveChannelRecord 不是空" +liveChannel.ChannelID+"; "+liveChannel.ChannelName);
						liveChannelRecord.platform = liveChannel.platform;
						liveChannelRecord.ChannelName = liveChannel.ChannelName;
						liveChannelRecord.TimeShift = liveChannel.TimeShift;
						liveChannelRecord.ChannelID = liveChannel.ChannelID;
						aaaLiveChannelsLocalFactory.updateRecord(liveChannelRecord);
						HomeActivity.mapLiveChannel.put(liveChannel.ChannelID, liveChannelRecord);
					} else {
//						LogUtils.debug("liveChannelRecord 是空" +liveChannel.ChannelID+"; "+liveChannel.ChannelName);
						aaaLiveChannelsLocalFactory.insertRecord(liveChannel);
						HomeActivity.mapLiveChannel.put(liveChannel.ChannelID, liveChannel);
					}
				} else  {
//					LogUtils.debug("AAA下载数据中不包含:" +liveChannel.ChannelID+"; "+liveChannel.ChannelName);
					LiveChannel liveChannelRecord = aaaLiveChannelsLocalFactory.findRecordZ(liveChannel.ChannelID);
					if (liveChannelRecord != null) {
//						LogUtils.debug("liveChannelRecord 不是空" +liveChannel.ChannelID);
						liveChannelRecord.platform = liveChannel.platform;
						liveChannelRecord.ChannelName = liveChannel.ChannelName;
						liveChannelRecord.TimeShift = liveChannel.TimeShift;
						liveChannelRecord.ChannelID = liveChannel.ChannelID;
						aaaLiveChannelsLocalFactory.updateRecord(liveChannelRecord);
						HomeActivity.mapLiveChannel.put(liveChannel.ChannelID, liveChannelRecord);
					} else {
//						LogUtils.debug("liveChannelRecord 是空：" +liveChannel.ChannelID+"; "+liveChannel.ChannelName+" 准备插入到本地数据库中去");
						//LogUtils.error("开始准备保存不存在频道:"+liveChannel.ChannelName +" "+liveChannel.ChannelID);
						aaaLiveChannelsLocalFactory.insertRecord(liveChannel);
						HomeActivity.mapLiveChannel.put(liveChannel.ChannelID, liveChannel);
					}
				}
			
			}
			// 开始剔除未下载的AAA频道信息
			List<LiveChannel> lists= aaaLiveChannelsLocalFactory.getAAALiveChannelListInfos();
			for(LiveChannel channel :lists){
				boolean find = false;
				for(LiveChannelBill channelBill:result){
					if(channelBill.ChannelID.equals(channel.ChannelID) ){
						find =true;
						break;
					}
				}
				if(!find){
//					LogUtils.error("删除频道："+channel.ChannelName+ " "+channel.ChannelID);
						aaaLiveChannelsLocalFactory.deletedRecordZTE(channel.ChannelID);
				}
			}
			
			if (TvApplication.platform==EnumType.Platform.ZTE) {
//				LogUtils.info("result.size------>"+result.size());
				if (AllChannelProgBilJsonlFactory.lstLiveChannelBill == null) {
					AllChannelProgBilJsonlFactory.lstLiveChannelBill = new ArrayList<LiveChannelBill>();
				}
				AllChannelProgBilJsonlFactory.lstLiveChannelBill.addAll(result);
							
				if (AllChannelProgBilJsonlFactory.mapLiveChannelBillZte == null) {
					AllChannelProgBilJsonlFactory.mapLiveChannelBillZte = new LinkedHashMap<String, LiveChannelBill>();
				}
				
			}else{
				if (AllChannelProgBillFactory.lstLiveChannelBill == null) {
					AllChannelProgBillFactory.lstLiveChannelBill = new ArrayList<LiveChannelBill>();
				}
				AllChannelProgBillFactory.lstLiveChannelBill.addAll(result);
							
				if (AllChannelProgBillFactory.mapLiveChannelBill == null) {
					AllChannelProgBillFactory.mapLiveChannelBill = new LinkedHashMap<String, LiveChannelBill>();
				}
			}
		
	
			for (LiveChannelBill bill : result) {
				LogUtils.debug("要插入的频道信息:"+bill.ChannelName+"  "+bill.ChannelID+ "  "+bill.UserChannelID+"bill.platform   :"+bill.platform);
					if (bill.platform==EnumType.Platform.ZTE) {
						AllChannelProgBilJsonlFactory.mapLiveChannelBillZte.put(bill.ChannelID, bill);
					}else{
						AllChannelProgBillFactory.mapLiveChannelBill.put(bill.ChannelID, bill);
					}
					
			}
			isChannelProgramBillHandlerReady = true;
			//下载节目单
			// 判断如果已经下载了当天的节目单，就不要下载了
			String checkDate=TimeUtil.getUserDate("yyyy-MM-dd");
			if (TvApplication.platform==EnumType.Platform.ZTE) {
				 checkDate = TimeUtil.getUserDate("yyyy.MM.dd");
			}
			LiveAllChannelBillLocalFactory liveAllChannelBillLocalFactory = new LiveAllChannelBillLocalFactory(getContext());
			
			LogUtils.debug("本地节目是否存在？"+liveAllChannelBillLocalFactory.isExit(checkDate));
			
			if (!liveAllChannelBillLocalFactory.isExit(checkDate))
			{
				LogUtils.debug("选择的语言:"+TvApplication.language);
				if (TvApplication.language.equals(IPTVUriUtils.ENGLISH_CODE)) {
					
				} else {
					if (TvApplication.shoulLoginIptv) {
						isChannelProgramBillHandlerReady = false;
						LogUtils.error("平台:"+TvApplication.platform);
						if (TvApplication.platform == EnumType.Platform.RUNHUAWEI || TvApplication.platform == EnumType.Platform.DEVHUAWEI) {
							LogUtils.debug("进入直播节目下载:"+TvApplication.platform);
							String date = TimeUtil.getUserDate("yyyyMMdd");
							AllChannelProgBillFactory allChannelProgBillFactory = new AllChannelProgBillFactory();
							allChannelProgBillFactory.setHttpEventHandler(channelProgramBillHandler);
							allChannelProgBillFactory.setHttpHeaderParams(IPTVUriUtils.getHeader());
							allChannelProgBillFactory.DownloadDatas(date);
						}else if (TvApplication.platform == EnumType.Platform.ZTE) {
							LogUtils.debug("进入【中兴】直播节目下载:"+TvApplication.platform);
							String date = TimeUtil.getUserDate("yyyyMMdd");
							AllChannelProgBilJsonlFactory allChannelProgBillFactory = new AllChannelProgBilJsonlFactory();
							allChannelProgBillFactory.setHttpEventHandler(channelProgramBillHandler);
							allChannelProgBillFactory.setHttpHeaderParams(IPTVUriUtils.getHeader());
							allChannelProgBillFactory.DownloadDatas("0");
						}
					}
				}
			}
			
			isPublicLiveChannelsHandlerReady = true;
			
			if (isReady())
				mProgressBar.setVisibility(View.GONE);
		}

		@Override
		public void HttpFailHandler() {
			LogUtils.debug("HttpFailHandler");
			mProgressBar.setVisibility(View.GONE);
			TvUtils.processHttpFail(getContext());
		}
	};
	
	private HttpEventHandler<ArrayList<VodTop>> vodTopCategoryHandler = new HttpEventHandler<ArrayList<VodTop>>() {

		@Override
		public void HttpSucessHandler(ArrayList<VodTop> result) {
			mPageView.createVodView(vodIndex, result, false);
			//mPageView.createVodView(2, result, false);
			insertToVodDb(result, 0);
			
			isVodTopCategoryHandlerReady = true;
			
			if (isReady())
				mProgressBar.setVisibility(View.GONE);
		}

		@Override
		public void HttpFailHandler() {
			LogUtils.debug("HttpFailHandler");
			mProgressBar.setVisibility(View.GONE);
			TvUtils.processHttpFail(getContext());
		}
	};
	
	private HttpEventHandler<ArrayList<MyHotelTop>> myHotelTopHandler = new HttpEventHandler<ArrayList<MyHotelTop>>() {
		@Override
		public void HttpSucessHandler(ArrayList<MyHotelTop> result) {
			
			mPageView.createMyHotelView(myHotelIndex, result, false);
			insertToMyHotelDb(result, 0);

			isMyHotelTopHandlerReady = true;
			
			if (isReady())
				mProgressBar.setVisibility(View.GONE);
		}

		@Override
		public void HttpFailHandler() {
			mProgressBar.setVisibility(View.GONE);
			TvUtils.processHttpFail(getContext());
		}
	};
	
	private HttpEventHandler<ArrayList<MyHotelTop>> travelTopHandler = new HttpEventHandler<ArrayList<MyHotelTop>>() {
		@Override
		public void HttpSucessHandler(ArrayList<MyHotelTop> result) {
			
			mPageView.createMyHotelView(travelIndex, result, false);
			//insertToMyHotelDb(result, 0);

			isTravelHandlerReady = true;
			
			if (isReady()) mProgressBar.setVisibility(View.GONE);
		}

		@Override
		public void HttpFailHandler() {
			mProgressBar.setVisibility(View.GONE);
			TvUtils.processHttpFail(getContext());
		}
	};
	
	private ArrayList<PortalHome> getFromDb(int index) {
		mPortalHomeLocalFactory.setIndex(index);
		return mPortalHomeLocalFactory.getHomeInfosByIndex(index);
	}
	
	private PortalChannels getFromLiveDb(int index) {
		mPortalHomeLiveChannelsLocalFactory.setIndex(index);
		return mPortalHomeLiveChannelsLocalFactory.getHomeInfosByIndex(index);
	}
	
	private ArrayList<VodTop> getFromVodTopDb(int index) {
		mPortalHomeVodTopLocalFactory.setIndex(index);
		return mPortalHomeVodTopLocalFactory.getHomeInfosByIndex(index);
	}
	
	private ArrayList<MyHotelTop> getFromMyHotelDb(int index) {
		mPortalHomeMyHotelTopLevelFactory.setIndex(index);
		return mPortalHomeMyHotelTopLevelFactory.getHomeInfosByIndex(index);
	}
	
	private void insertToDb(ArrayList<PortalHome> list, int index) {
		mPortalHomeLocalFactory.setIndex(index);
		mPortalHomeLocalFactory.insertHomeInfos(list);
	}
	
	private void insertToLiveDb(PortalChannels liveChannels, int index) {		
		mPortalHomeLiveChannelsLocalFactory.setIndex(index);
		mPortalHomeLiveChannelsLocalFactory.insertLiveChannelInfos(liveChannels);
	}
	
	private void insertToVodDb(ArrayList<VodTop> list, int index) {
		mPortalHomeVodTopLocalFactory.setIndex(index);
		mPortalHomeVodTopLocalFactory.insertVodTopInfos(list);
	}
	
	private void insertToMyHotelDb(ArrayList<MyHotelTop> list, int index) {
		mPortalHomeMyHotelTopLevelFactory.setIndex(index);
		mPortalHomeMyHotelTopLevelFactory.insertMyHotelTopInfos(list);
	}
	
}
