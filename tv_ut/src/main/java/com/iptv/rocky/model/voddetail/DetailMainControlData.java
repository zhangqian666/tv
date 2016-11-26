package com.iptv.rocky.model.voddetail;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.iptv.common.data.EnumType;
import com.iptv.common.data.HistoryChannelInfo;
import com.iptv.common.data.StoreChannelInfo;
import com.iptv.common.utils.LogUtils;
import com.iptv.rocky.R;
import com.iptv.rocky.RecChanActivity;
import com.iptv.rocky.VodChannelDetailActivity;
import com.iptv.rocky.base.BaseMetroItemData;
import com.iptv.rocky.model.TvApplication;
import com.iptv.rocky.utils.TvUtils;
import com.iptv.rocky.view.voddetail.DetailMainControlView;
import com.iptv.rocky.view.voddetail.SelectNumberMasterView;

public class DetailMainControlData extends BaseMetroItemData {

	private Context context;
	private DetailMainControlContentData contentData;
	private DetailMainControlView view;
	
	private boolean hasFocus;

	public DetailMainControlData(Context context, DetailMainControlContentData contentData) {
		this.context = context;
		this.contentData = contentData;
		super.heightSpan = 0.8;
		super.widthSpan = 0.8;
	}
	
	@Override
	public View getView(Context context) {
		if (view == null) {
			LayoutInflater inflater = LayoutInflater.from(context);
			view = (DetailMainControlView)inflater.inflate(R.layout.vod_detail_main_control, null);
			view.initView(contentData);
		}
		return view;
	}
	
	public void refreshControlText() {
		if (view == null) {
			getView(context);
		}
		if (contentData != null) {
			
			if(contentData.type == DetailMainControlContentData.Type.PLAY){
				HistoryChannelInfo info = DetailLocalHelper.instance(context).getLastPlay(contentData.vodDetailObj.VODID);
				if (info != null) {
					contentData.text = TvUtils.createPlayPosition(context, info.subtitle, info.playposition);
					contentData.isMarquee = true && hasFocus;
					contentData.textSize = (float)(TvApplication.sTvMasterTextSize * 0.7);
					view.initView(contentData);
				}
			}else if(contentData.type == DetailMainControlContentData.Type.TRY){
				
			}
		}
	}

	@Override
	public void onClick(final Context context) {
		String subVideoChannelID = null;
		switch (contentData.type) {
		case PLAY:
			
			if (contentData.vodDetailObj.ISSITCOM == 1)
			{
				HistoryChannelInfo info = DetailLocalHelper.instance(context).getLastPlay(contentData.vodDetailObj.VODID);
				if (info != null) {
					subVideoChannelID = info.VODID;
				} else {
					subVideoChannelID = contentData.vodDetailObj.SUBVODIDLIST.get(0).VODID;
				}
			}
			else
			{
				subVideoChannelID = contentData.vodDetailObj.VODID;
			}
			TvUtils.playVideo(context, contentData.vodDetailObj, subVideoChannelID);

			break;
		case SELECTOR:
//			if (contentData.selectType == SelectType.NUMBER) {
				showNumberDialog();
//			} else {
//				showDialog();
//			}
			break;
//		case CHASE:
//			if (view.getText().equals(context.getString(R.string.detail_main_chase))) {
//				chase();
//			} else {
//				long channelId = Long.valueOf(contentData.vodDetailObj.getVid());
//				
//				unChase(channelId);
//			}
//			break;
		case BUY:
			Map<String,String> reasons= new HashMap<String,String>();
			
			Intent intent = new Intent(VodChannelDetailActivity.ACTION_OPEN_ORDER_FRAGMENT);
			
			Bundle bundle = new Bundle();
			bundle.putString("VODID", contentData.vodDetailObj.VODID);
			bundle.putString("VODPRICE", contentData.vodDetailObj.VODPRICE);
			intent.putExtras(bundle);
			DetailMainControlData.this.context.sendBroadcast(intent);
						
			break;
		case TRY:
			subVideoChannelID = null;
			
			//适配中兴
			if (TvApplication.platform==EnumType.Platform.ZTE) {
				if (contentData.vodDetailObj.programtype.equals("14")){
					HistoryChannelInfo info = DetailLocalHelper.instance(context).getLastPlay(contentData.vodDetailObj.VODID);
					if (info != null) {
						subVideoChannelID = info.VODID;
					} else {
						subVideoChannelID = contentData.vodDetailObj.SUBVODIDLIST.get(0).VODID;
					}
				} else {
					subVideoChannelID = contentData.vodDetailObj.VODID;
				}
			}else{
				if (contentData.vodDetailObj.ISSITCOM == 1){
					HistoryChannelInfo info = DetailLocalHelper.instance(context).getLastPlay(contentData.vodDetailObj.VODID);
					if (info != null) {
						subVideoChannelID = info.VODID;
					} else {
						subVideoChannelID = contentData.vodDetailObj.SUBVODIDLIST.get(0).VODID;
					}
				} else {
					subVideoChannelID = contentData.vodDetailObj.VODID;
				}
			}
			TvUtils.tryVideo(context, contentData.vodDetailObj, subVideoChannelID);
			break;
		case STORE:
			if (view.getText().equals(context.getString(R.string.detail_main_store))) {
				store();
			} else {
				unStore(contentData.vodDetailObj.VODID);
			}
			break;
		default:
			break;
		}
	}

	/**
	 * 追剧
	 */
//	private void chase() {
//		ChaseChannelInfo info = new ChaseChannelInfo();
//		info.vid = CommonUtils.parseInt(contentData.vodDetailObj.getVid());
//		info.title = contentData.vodDetailObj.getTitle();
//		info.imgurl = contentData.vodDetailObj.getImgurl();
//		info.sloturl = contentData.vodDetailObj.getSloturl();
//		info.updateText = DetailMainView.chaseSubTitle;
//		info.ctime = System.currentTimeMillis();
//		contentData.imageResId = R.drawable.icon_heart_yel;
//		DetailLocalHelper.instance(context).chase(info);
//		view.setImageResource(R.drawable.icon_heart_yel);
//		view.setText(context.getString(R.string.detail_main_chased));
//	}

	/**
	 * 收藏
	 */
	private void store() {
		StoreChannelInfo info = new StoreChannelInfo();
		info.VODID = contentData.vodDetailObj.VODID;
		info.VODNAME = contentData.vodDetailObj.VODNAME;
		info.PICPATH = contentData.vodDetailObj.PICPATH;
		info.ctime = System.currentTimeMillis();
		info.platform = contentData.vodDetailObj.platform;
		DetailLocalHelper.instance(context).store(info);
		view.setImageResource(R.drawable.icon_store_yel);
		view.setText(context.getString(R.string.detail_main_stored));
	}

	private void unStore(String channelId) {
		DetailLocalHelper.instance(context).unStore(channelId);
		view.setText(context.getString(R.string.detail_main_store));
		view.setImageResource(R.drawable.icon_store);
	}

//	private void unChase(long channelId) {
//		contentData.imageResId = R.drawable.icon_heart;
//		DetailLocalHelper.instance(context).unChase(channelId);
//		view.setText(context.getString(R.string.detail_main_chase));
//		view.setImageResource(R.drawable.icon_heart);
//	}

	private void showNumberDialog() {
		Dialog dialog = new Dialog(context, R.style.Tv_Dialog);
		LayoutInflater inflater = LayoutInflater.from(context);
		SelectNumberMasterView view = (SelectNumberMasterView) inflater.inflate(R.layout.vod_select_episode_master, null);
		view.createView(contentData.vodDetailObj);
		ViewPager pager = (ViewPager) view.getChildAt(2);
		pager.requestFocus();
		dialog.setContentView(view);
		dialog.show();
	}

//	private void showDialog() {
//		Dialog dialog = new Dialog(context, R.style.Tv_Dialog);
//		LayoutInflater inflater = LayoutInflater.from(context);
//		SelectNotNumberMasterView masterView = (SelectNotNumberMasterView) inflater
//				.inflate(R.layout.select_gallery_master, null);
//		masterView.createView(contentData.vodDetailObj);
//		dialog.setContentView(masterView);
//		dialog.show();
//	}
	
	protected void broadcast(String action, Map<String, String> params) {
		
		Intent intent = new Intent(action);
		if (params != null) {
			Set<String> keys = params.keySet();
			for (String string : keys) {
				intent.putExtra(string, params.get(string));
			}
		}
		DetailMainControlData.this.context.sendBroadcast(intent);
	}
	
	@Override
	public void onOwnerFocusChange(boolean hasFocus) {
		this.hasFocus = hasFocus;
		view.setMarqueeable(hasFocus);
	}



}
