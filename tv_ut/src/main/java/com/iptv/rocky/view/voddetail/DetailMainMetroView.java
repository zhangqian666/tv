package com.iptv.rocky.view.voddetail;

import android.content.Context;
import android.util.AttributeSet;

import com.iptv.common.data.HistoryChannelInfo;
import com.iptv.common.data.VodDetailInfo;
import com.iptv.rocky.R;
import com.iptv.rocky.base.BaseMetroItemData;
import com.iptv.rocky.base.BaseMetroView;
import com.iptv.rocky.model.TvApplication;
import com.iptv.rocky.model.voddetail.DetailLocalHelper;
import com.iptv.rocky.model.voddetail.DetailMainControlContentData;
import com.iptv.rocky.model.voddetail.DetailMainControlData;
import com.iptv.rocky.utils.TvUtils;

public class DetailMainMetroView extends BaseMetroView {

	private VodDetailInfo vodDetailObj;
	private Context context;
	private DetailMainControlData controlPlay;
	
	//private VodDetailCheckOrderStatusFactory checkOrderStatusFactory;

	public DetailMainMetroView(Context context) {
		this(context, null);
	}

	public DetailMainMetroView(Context context, AttributeSet attrs) {
		super(context, attrs);

		this.context = context;
		setPadding(TvApplication.sTvLeftMargin, TvApplication.sTvTabHeight,TvApplication.sTvLeftMargin, 0);
	}

	@Override
	protected int getColumenNumber() {
		return 1;
	}

	@Override
	protected int getItemUnitNumber() {
		return TvApplication.sTvChannelUnit;
	}

	@Override
	protected boolean isRelection(BaseMetroItemData itemData) {
		return false;
	}

	/**
	 * 点播详细信息的左侧按钮部分
	 * 此处修改为根据是否付费做判断 
	 */
	public void setData(VodDetailInfo vodDetailObj) {
		this.vodDetailObj = vodDetailObj;
		switch (TvApplication.billingType) {
		case Day_UsedDay_FreeAllVod: //第一期会启用，适用电视房
			addMetroItem(createPlay());
			if (vodDetailObj.ISSITCOM == 1) {
				addMetroItem(createSelector());
			}
			addMetroItem(createChaseOrStore());
			break;
		case Day_UsedDay_FreeDaylyPayVod_PayPPV:
			
			if(vodDetailObj.price >0){
				addMetroItem(createTry());
			}else{
				addMetroItem(createPlay());
			}

			if (vodDetailObj.ISSITCOM == 1) {
				addMetroItem(createSelector());
			}
			addMetroItem(createChaseOrStore());
			break;
		case Day_UsedDay_PayDaylyPayVod_PayPPV:
			//addMetroItem(createBuy());
			
			if(vodDetailObj.price >0){
				addMetroItem(createTry());
			}else{
				addMetroItem(createTry());
			}

			if (vodDetailObj.ISSITCOM == 1) {
				addMetroItem(createSelector());
			}
			addMetroItem(createChaseOrStore());
			break;
		case Free_All:
			addMetroItem(createPlay());
			if (vodDetailObj.ISSITCOM == 1) {
				addMetroItem(createSelector());
			}
			//addMetroItem(createChaseOrStore());
			break;
		case Free_PayAllVod:  //第一期会启用
			
			if(vodDetailObj.ordered){
				addMetroItem(createPlay());
				if (vodDetailObj.ISSITCOM == 1) {
					addMetroItem(createSelector());
				}
			}else{
				//addMetroItem(createBuy());
				addMetroItem(createTry());
				if (vodDetailObj.ISSITCOM == 1) {
					addMetroItem(createSelector());
				}
			}
			addMetroItem(createChaseOrStore());
			
			break;
		case Month_FreeAllVod:
			addMetroItem(createPlay());
			if (vodDetailObj.ISSITCOM == 1) {
				addMetroItem(createSelector());
			}
			addMetroItem(createChaseOrStore());
			break;
		case Month_FreeMonthlyPayVod_PayPPV:
			
			if(vodDetailObj.price >0){
				addMetroItem(createTry());
			}else{
				addMetroItem(createPlay());
			}

			if (vodDetailObj.ISSITCOM == 1) {
				addMetroItem(createSelector());
			}
			addMetroItem(createChaseOrStore());
			break;
		case Month_PayMonthlyPayVod_payPPV:
			//addMetroItem(createPlay());
			addMetroItem(createTry());
			
			if (vodDetailObj.ISSITCOM == 1) {
				addMetroItem(createSelector());
			}
			addMetroItem(createChaseOrStore());
			break;
		case UNKNOW:	
			break;
		default:
			break;
		}
	}

	public void restart() {
		if (controlPlay != null) {
			controlPlay.refreshControlText();
		}
	}

	// 创建播放按钮
	private DetailMainControlData createPlay() {
		DetailMainControlContentData playData = new DetailMainControlContentData();
		playData.backgroundResId = R.drawable.shap_play;
		playData.imageResId = R.drawable.icon_play;
		HistoryChannelInfo info = DetailLocalHelper.instance(getContext()).getLastPlay(vodDetailObj.VODID);
		if (info == null) {
			playData.text = context.getResources().getString(R.string.detail_main_paly);
			playData.textSize = TvApplication.sTvMasterTextSize;
			playData.isMarquee = false;
		} else {
			playData.text = TvUtils.createPlayPosition(context, info.subtitle, info.playposition);
			playData.textSize = (float) (TvApplication.sTvMasterTextSize * 0.7);
			playData.isMarquee = true;
		}
		playData.type = DetailMainControlContentData.Type.PLAY;
		playData.vodDetailObj = vodDetailObj;
		controlPlay = new DetailMainControlData(context, playData);
		return controlPlay;
	}
	
	
	// 创建试播按钮
	private DetailMainControlData createTry() {
		DetailMainControlContentData playData = new DetailMainControlContentData();
		playData.backgroundResId = R.drawable.shap_play;
		playData.imageResId = R.drawable.icon_play;
		HistoryChannelInfo info = DetailLocalHelper.instance(getContext()).getLastPlay(vodDetailObj.VODID);
		//if (info == null) {
		playData.text = context.getResources().getString(R.string.detail_main_try);
		playData.textSize = TvApplication.sTvMasterTextSize;
		playData.isMarquee = false;
		/*} else {
			playData.text = TvUtils.createPlayPosition(context, info.subtitle, info.playposition);
			playData.textSize = (float) (TvApplication.sTvMasterTextSize * 0.7);
			playData.isMarquee = true;
		}*/
		playData.type = DetailMainControlContentData.Type.TRY;
		playData.vodDetailObj = vodDetailObj;
		controlPlay = new DetailMainControlData(context, playData);
		return controlPlay;
	}
	
	// 创建购买按钮
	private DetailMainControlData createBuy() {
		DetailMainControlContentData playData = new DetailMainControlContentData();
		playData.backgroundResId = R.drawable.shap_play;
		playData.imageResId = R.drawable.icon_play;
		HistoryChannelInfo info = DetailLocalHelper.instance(getContext()).getLastPlay(vodDetailObj.VODID);
		//if (info == null) {
			playData.text = context.getResources().getString(R.string.detail_main_buy);
			playData.textSize = TvApplication.sTvMasterTextSize;
			playData.isMarquee = false;
		/*} else {
			playData.text = TvUtils.createPlayPosition(context, info.subtitle, info.playposition);
			playData.textSize = (float) (TvApplication.sTvMasterTextSize * 0.7);
			playData.isMarquee = true;
		}*/
		playData.type = DetailMainControlContentData.Type.BUY;
		playData.vodDetailObj = vodDetailObj;
		controlPlay = new DetailMainControlData(context, playData);
		return controlPlay;
	}
	

	// 创建剧集按钮
	private DetailMainControlData createSelector() {
		DetailMainControlContentData playData = new DetailMainControlContentData();
		playData.backgroundResId = R.drawable.shap_select;
		playData.imageResId = R.drawable.icon_select;
		playData.text = context.getResources().getString(R.string.detail_main_select);
		playData.textSize = TvApplication.sTvMasterTextSize;
		playData.type = DetailMainControlContentData.Type.SELECTOR;
		playData.vodDetailObj = vodDetailObj;
//		if (AtvUtils.isEpisode(String.valueOf(vodDetailObj.getVt()),
//				vodDetailObj.getVideoList())) {
			playData.selectType = DetailMainControlContentData.SelectType.NUMBER;
//		} else {
//			playData.selectType = DetailMainControlContentData.SelectType.NUMBER_NO;
//		}
		DetailMainControlData play = new DetailMainControlData(context, playData);
		return play;
	}

	//保存追剧或添加收藏按钮
	private DetailMainControlData createChaseOrStore() {
		DetailMainControlContentData playData = new DetailMainControlContentData();
		playData.backgroundResId = R.drawable.shap_like;

//		if (isStore()) {
			boolean isStored = DetailLocalHelper.instance(getContext()).isStored("vid", vodDetailObj.VODID+"");
			playData.imageResId = isStored ? R.drawable.icon_store_yel : R.drawable.icon_store;
			playData.text = isStored ? context.getString(R.string.detail_main_stored) : context.getString(R.string.detail_main_store);
			playData.textSize = TvApplication.sTvMasterTextSize;
			playData.type = DetailMainControlContentData.Type.STORE;
//		} else {
//			boolean isChased = DetailLocalHelper.instance(getContext())
//					.isChased("vid", vodDetailObj.VODID);
//			playData.imageResId = isChased ? R.drawable.icon_heart_yel
//					: R.drawable.icon_heart;
//			playData.text = isChased ? context
//					.getString(R.string.detail_main_chased) : context
//					.getString(R.string.detail_main_chase);
//			playData.textSize = TvApplication.sTvMasterTextSize;
//			playData.type = DetailMainControlContentData.Type.CHASE;
//		}
		playData.vodDetailObj = vodDetailObj;
		DetailMainControlData play = new DetailMainControlData(context,	playData);
		return play;
	}

//	private boolean hasSelector() {
//		int vt = vodDetailObj.getVt();
//		List<Video> videos = vodDetailObj.getVideoList();
//		return (vt == 21 || vt == 22) && (videos != null && videos.size() > 0);
//	}
//
//	private boolean isStore() {
//		return !hasSelector();
//	}
	
	
/*	private HttpEventHandler<VodOrderStatusResult> checkOrderStatusHandler = new HttpEventHandler<VodOrderStatusResult>() {
		@Override
		public void HttpSucessHandler(VodOrderStatusResult result) {
			
			if(result.getResult() == 2000){
				addMetroItem(createPlay());
				if (vodDetailObj.ISSITCOM == 1) {
					addMetroItem(createSelector());
				}
			}
			else if(result.getResult() == 2002)
			{
				//addMetroItem(createBuy());
				addMetroItem(createTry());
				if (vodDetailObj.ISSITCOM == 1) {
					addMetroItem(createSelector());
				}
			}
			else if(result.getResult() == 2001)
			{
				//addMetroItem(createBuy());
				addMetroItem(createTry());
				if (vodDetailObj.ISSITCOM == 1) {
					addMetroItem(createSelector());
				}
			}
			addMetroItem(createChaseOrStore());
		}
		
		@Override
		public void HttpFailHandler() {
			LogUtils.debug("HttpFailHandler");
			//TvUtils.processHttpFail(HomeActivity.this);
		}
	};*/

}
