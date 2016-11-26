package com.iptv.rocky.view.voddetail;

import java.text.DecimalFormat;
import java.util.ArrayList;

import com.iptv.common.HttpEventHandler;
import com.iptv.common.data.EnumType;
import com.iptv.common.data.EnumType.Platform;
import com.iptv.common.data.HistoryChannelInfo;
import com.iptv.common.data.VodChannel;
import com.iptv.common.data.VodDetailInfo;
import com.iptv.common.data.VodOrderStatusResult;
import com.iptv.common.utils.LogUtils;
import com.iptv.rocky.R;
import com.iptv.rocky.base.BaseTabItemData;
import com.iptv.rocky.hwdata.IPTVUriUtils;
import com.iptv.rocky.hwdata.json.VodDetailCheckOrderStatusFactory;
import com.iptv.rocky.hwdata.json.VodDetailInfoJsonFactory;
import com.iptv.rocky.hwdata.json.VodSitcomListJsonFactory;
import com.iptv.rocky.model.TvApplication;
import com.iptv.rocky.model.voddetail.DetailLocalHelper;
import com.iptv.rocky.utils.TvUtils;
import com.iptv.rocky.view.TabView;

import android.content.Context;
import android.graphics.Rect;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

public class DetailMasterView extends RelativeLayout 
	implements TabView.SelectChangedListener {
	
	private Context mContext;
	private DetailViewPager mViewPager;
	private TabView mTabView;
	private ProgressBar mProgressBar;
	public VodDetailInfo vodDetailObj;
	
	private VodDetailInfoJsonFactory mFactory;
	private VodSitcomListJsonFactory mVodSitcomListJsonFactory;
	private VodDetailCheckOrderStatusFactory checkOrderStatusFactory;
	
	private ArrayList<BaseTabItemData> mTabData;
	private String mColumnCode="";
	
	public DetailMasterView(Context context) {
		this(context, null);
	}
	
	public DetailMasterView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		
		mTabData = new ArrayList<BaseTabItemData>(2);
		mTabData.add(new BaseTabItemData(context.getString(R.string.detail_tab_1)));
		
		checkOrderStatusFactory = new VodDetailCheckOrderStatusFactory();
		checkOrderStatusFactory.setHttpEventHandler(checkOrderStatusHandler);

		mFactory = new VodDetailInfoJsonFactory();
		mFactory.setHttpHeaderParams(IPTVUriUtils.getHeader());
		mFactory.setHttpEventHandler(vodDetailHandler);
		
		mVodSitcomListJsonFactory=new VodSitcomListJsonFactory();
		mVodSitcomListJsonFactory.setHttpHeaderParams(IPTVUriUtils.getHeader());
		mVodSitcomListJsonFactory.setHttpEventHandler(mVodSitcomListJsonHandler);
	}
	
	public void load(EnumType.Platform platform, String vid,String columnCode_price){
		LogUtils.debug("平台:"+platform);
		mColumnCode=columnCode_price;
		mFactory.DownloadDatas(platform,vid);
		mProgressBar.setVisibility(View.VISIBLE);
	}
	
	public void load1(EnumType.Platform platform, String columnCode,String contentcode){
		mFactory.DownloadDatas(platform,columnCode,contentcode);
		mColumnCode=columnCode;
		mProgressBar.setVisibility(View.VISIBLE);
	}
	
	public void reLoad(EnumType.Platform platform, String vid,String columnCode_price){
		checkOrderStatusFactory.DownloadDatas(vodDetailObj.VODID,vodDetailObj.price,columnCode_price);
		mProgressBar.setVisibility(View.VISIBLE);
	}
	
	public void destory(){
		mViewPager.destory();
		mFactory.cancel();
	}
	
	public void resume() {
		mTabView.autoFocusStart();
		mViewPager.restart();
	}
	
	public void pause() {
		mTabView.autoFocusStop();
	}

	private OnPageChangeListener onPageChangdeListener = new OnPageChangeListener() {
		
		@Override
		public void onPageSelected(int index) {
			mTabView.tabSelected(index);
		}
		
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			
		}
		
		@Override
		public void onPageScrollStateChanged(int arg0) {
			
		}
	};
	
	@Override
	protected boolean onRequestFocusInDescendants(int direction,
			Rect previouslyFocusedRect) {
		return mViewPager.requestFocus(direction, previouslyFocusedRect);
	}
	
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		
		mViewPager = (DetailViewPager) findViewById(R.id.detail_viewpager);
		mProgressBar = (ProgressBar) findViewById(R.id.tv_progressbar);
		mTabView = (TabView) findViewById(R.id.tv_tablayout);
		mTabView.setSelectChanngeListener(this);
		
		mViewPager.setOnPageChangeListener(onPageChangdeListener);
		findViewById(R.id.tv_status_search).setVisibility(View.INVISIBLE);
	}
	
	@Override
	public void onChange(int index) {
		mViewPager.setCurrentItem(index);
	}	
	
	public void toPlay(){
		String subVideoChannelID = null;
		if (vodDetailObj.ISSITCOM == 1)
		{
			HistoryChannelInfo info = DetailLocalHelper.instance(mContext).getLastPlay(vodDetailObj.VODID);
			if (info != null) {
				subVideoChannelID = info.VODID;
			} else {
				subVideoChannelID = vodDetailObj.SUBVODIDLIST.get(0).VODID;
			}
		}
		else
		{
			subVideoChannelID = vodDetailObj.VODID;
		}
		TvUtils.playVideo(mContext, vodDetailObj, subVideoChannelID);
	}
	
	/**
	 * 获取点播的详细信息
	 */
	private HttpEventHandler<VodDetailInfo> vodDetailHandler = new HttpEventHandler<VodDetailInfo>() {
		
		@Override
		public void HttpSucessHandler(VodDetailInfo result) {
			vodDetailObj =result;
			if ("14".equals(vodDetailObj.programtype)) {
				vodDetailObj.ISSITCOM=1;
				mVodSitcomListJsonFactory.DownloadDatas(vodDetailObj.platform,vodDetailObj.seriesprogramcode);
			}else{
				checkOrderStatusFactory.DownloadDatas(vodDetailObj.VODID,vodDetailObj.price,mColumnCode);
			}
		}
		
		@Override
		public void HttpFailHandler() {
			mProgressBar.setVisibility(View.GONE);
			TvUtils.processHttpFail(mContext);
		}
	};
	
	/**
	 * 若为电视剧，则获取电视剧剧集及详情
	 * 
	 */
	private HttpEventHandler<ArrayList<VodChannel>>  mVodSitcomListJsonHandler=new HttpEventHandler<ArrayList<VodChannel>>() {

		@Override
		public void HttpSucessHandler(ArrayList<VodChannel> result) {
			if (result!=null&&result.size()>0) {
				vodDetailObj.SUBVODIDLIST.addAll(result);
				if (vodDetailObj.SUBVODNUMLIST!=null&&vodDetailObj.SUBVODNUMLIST.size()==0) {
					for (int i = 0; i < result.size(); i++) {
						vodDetailObj.SUBVODNUMLIST.add(result.get(i).nNumber);
					}
				}
				vodDetailObj.SITCOMNUM=result.size();
				checkOrderStatusFactory.DownloadDatas(vodDetailObj.VODID,vodDetailObj.price);
			}
			
		}

		@Override
		public void HttpFailHandler() {
			// TODO Auto-generated method stub
			
		}

	};
	private HttpEventHandler<VodOrderStatusResult> checkOrderStatusHandler = new HttpEventHandler<VodOrderStatusResult>() {
		@Override
		public void HttpSucessHandler(VodOrderStatusResult result) {
			
			
			//mViewPager.createView(vodDetailObj);
			//mTabView.removeAllTabs();
			//mTabView.createView(mTabData);
			//mProgressBar.setVisibility(View.GONE);
			if(result.getResult() == 2000){
				vodDetailObj.ordered = true;
				DecimalFormat vf= new DecimalFormat("#0.00");
				vodDetailObj.VODPRICE = vf.format(result.getPrice());
				vodDetailObj.price =result.getPrice();
				vodDetailObj.priceDisp= result.getPrice();
			}
			else if(result.getResult() == 2002)
			{
				DecimalFormat vf= new DecimalFormat("#0.00");
				vodDetailObj.VODPRICE = vf.format(result.getPrice());
				vodDetailObj.priceDisp= result.getPrice();
				vodDetailObj.price = result.getPrice();
				vodDetailObj.ordered = false;
			}
			else if(result.getResult() == 2001)
			{
				DecimalFormat vf= new DecimalFormat("#0.00");
				vodDetailObj.ordered = false;
				vodDetailObj.VODPRICE = vf.format(result.getPrice());
				vodDetailObj.priceDisp= result.getPrice();
				vodDetailObj.price = result.getPrice();
				
			}
			//changePrice();
			
			mViewPager.createView(vodDetailObj);
			mTabView.removeAllTabs();
			mTabView.createView(mTabData);
			mProgressBar.setVisibility(View.GONE);
		}
		
		@Override
		public void HttpFailHandler() {
			LogUtils.debug("HttpFailHandler");
			//TvUtils.processHttpFail(HomeActivity.this);
		}
	};

	/**
	 * 根据产品的价格还有已经订购的包，确定单个产品的价格。
	 */
	private void changePrice(){
		
		switch (TvApplication.billingType) {
			case Day_UsedDay_FreeAllVod: //第一期会启用，适用电视房
				if(vodDetailObj.price>0){
					
					//vodDetailObj.price
					
					vodDetailObj.priceDisp = 30;// vodDetailObj.price*3;
				}else{
					vodDetailObj.priceDisp = 0;
				}
				break;
			case Day_UsedDay_FreeDaylyPayVod_PayPPV:
				if(vodDetailObj.price>0){
					vodDetailObj.priceDisp = 30;// vodDetailObj.price*3;
				}else{
					vodDetailObj.priceDisp = 0;
				}
				break;
			case Day_UsedDay_PayDaylyPayVod_PayPPV:
				
				if(vodDetailObj.ordered){
					vodDetailObj.priceDisp =0;
				}else{
					if(vodDetailObj.price>0){
						//vodDetailObj.priceDisp = 30;// vodDetailObj.price*3;
						vodDetailObj.priceDisp = 10;// vodDetailObj.price*3;
					}else{
						//vodDetailObj.priceDisp = 20;
						vodDetailObj.priceDisp = 10;
					}
				}
				break;
			case Free_All:
				vodDetailObj.priceDisp =0;
				break;
			case Free_PayAllVod:  //第一期会启用
				if(vodDetailObj.ordered){
					vodDetailObj.priceDisp =0;
				}else{
					LogUtils.error("平台："+vodDetailObj.platform);
					if(vodDetailObj.platform.equals(Platform.HOTEL)){
						if(vodDetailObj.price>0){
							vodDetailObj.priceDisp = vodDetailObj.price;
						}else{
							vodDetailObj.priceDisp = 0;
						}
					}else{
						if(vodDetailObj.price>0){
							vodDetailObj.priceDisp = vodDetailObj.price*2;
						}else{
							vodDetailObj.priceDisp = 20;
						}
					}
					LogUtils.error("输出的价格"+vodDetailObj.priceDisp);
				}
				break;
			case Month_FreeAllVod:
				if(vodDetailObj.ordered){
					vodDetailObj.priceDisp =0;
				}else{
					if(vodDetailObj.price>0){
						vodDetailObj.priceDisp = 30;// vodDetailObj.price*3;
					}else{
						vodDetailObj.priceDisp = 20;
					}
				}
				break;
			case Month_FreeMonthlyPayVod_PayPPV:
				if(vodDetailObj.ordered){
					vodDetailObj.priceDisp =0;
				}else{
					if(vodDetailObj.price>0){
						vodDetailObj.priceDisp = 30;// vodDetailObj.price*3;
					}else{
						vodDetailObj.priceDisp = 0;
					}
				}
				break;
			case Month_PayMonthlyPayVod_payPPV:
				if(vodDetailObj.ordered){
					vodDetailObj.priceDisp =0;
				}else{
					if(vodDetailObj.price>0){
						vodDetailObj.priceDisp = 30;// vodDetailObj.price*3;
					}else{
						vodDetailObj.priceDisp = 0;
					}
				}
				break;
			case UNKNOW:	
				break;
			default:
				break;
		}
		DecimalFormat vf= new DecimalFormat("#0.00");
		vodDetailObj.VODPRICE = vf.format(vodDetailObj.priceDisp);
		
	}
}
