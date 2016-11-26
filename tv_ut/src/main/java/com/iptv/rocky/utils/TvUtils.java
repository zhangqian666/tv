package com.iptv.rocky.utils;

import java.io.File;
import java.util.Calendar;
import java.util.Date;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;

import com.iptv.common.HttpEventHandler;
import com.iptv.common.data.Auth;
import com.iptv.common.data.EnumType;
import com.iptv.common.data.MyHotelPicture;
import com.iptv.common.data.RecBill;
import com.iptv.common.data.RecChan;
import com.iptv.common.data.RecUrl;
import com.iptv.common.data.VodChannel;
import com.iptv.common.data.VodDetailInfo;
import com.iptv.common.data.VodUrl;
import com.iptv.common.utils.Constants;
import com.iptv.common.utils.LogUtils;
import com.iptv.rocky.R;
import com.iptv.rocky.hwdata.IPTVUriUtils;
import com.iptv.rocky.hwdata.json.AuthJsonFactry;
import com.iptv.rocky.hwdata.json.RecUrlJsonFactory;
import com.iptv.rocky.hwdata.json.VodUrlJsonFactory;
import com.iptv.rocky.hwdata.xml.RecUrlFactory;
import com.iptv.rocky.hwdata.xml.VodUrlFactory;
import com.iptv.rocky.model.ActivityStack;
import com.iptv.rocky.model.TvApplication;
import com.iptv.rocky.tcl.RecChannelPlayActivity;
import com.iptv.rocky.tcl.VodChannelPlayActivity;
import com.ui.player.TVPlayerActivity;

public final class TvUtils {

	
	private static long exitTime = 0;

	private static final String[] WEEK = 
		{
			TvApplication.getInstance().getString(R.string.monday),
			TvApplication.getInstance().getString(R.string.tuesday),
			TvApplication.getInstance().getString(R.string.wednesday),
			TvApplication.getInstance().getString(R.string.thursday),
			TvApplication.getInstance().getString(R.string.friday),
			TvApplication.getInstance().getString(R.string.saturday),
			TvApplication.getInstance().getString(R.string.sunday)
		};
	
	/**
	 * 日期转星期
	 */
	public static String dateToWeek(int i) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(nextDate(i));
		int dayIndex = cal.get(Calendar.DAY_OF_WEEK);
		if (dayIndex < 1 || dayIndex > Constants.WEEKDAYS) {
			return null;
		}
		return WEEK[dayIndex - 1];
	}
	
	/**
	 * 获得下一天的日期
	 */
	private static Date nextDate(int i) {
		Date date = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_YEAR, i);
		date = cal.getTime();
		return date;
	}
	
	public static Date beforeDate(int i) {
		Date date = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_YEAR, i);
		date = cal.getTime();
		return date;
	}
	
	public static void doubleClickQuitApp(final Context context) {
//		if ((System.currentTimeMillis() - exitTime) > 2000) {
//			AppCommonUtils.showToast(context, context.getString(R.string.back_quit));
//			exitTime = System.currentTimeMillis();
//		} else {
			ActivityStack.popAll();
//		}
	}

	public static File getImageCacheDirectory() {
		File directory = new File(Constants.cImageCacheDirectory);
		if (!directory.exists()) {
			directory.mkdir();
		}
		return directory;
	}

	@SuppressWarnings("deprecation")
	public static void processHttpFail(Context context) {
		AppCommonUtils.showToast(context, context.getString(R.string.http_fail));
		
		// 报错，认为是session失效了，暂时给重启项目
		ActivityManager am = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
		am.restartPackage("com.iptv.rocky");
		am.killBackgroundProcesses(context.getPackageName()); 
		
	}
	
	private static void quiteBackgroundMusic(Context context) {
		if (BackgroundMusic.getInstance(context).isBackgroundMusicPlaying()) {
			BackgroundMusic.getInstance(context).stopBackgroundMusic();
		}
	}
	
	/**
	 * 播放电视剧集。
	 */
	public static void playVideo(final Context context, final VodDetailInfo vodDetailobj, final String subVideoChannelID) {
		quiteBackgroundMusic(context);
		
		VodUrlFactory mVodUrlFactory = new VodUrlFactory();
		mVodUrlFactory.setHttpEventHandler(new HttpEventHandler<VodUrl>() {
			
			@Override
			public void HttpSucessHandler(VodUrl result) {
				LogUtils.debug("播放电视剧：获取到的播放信息:"+result.RTSPURL);
				if (vodDetailobj.platform == EnumType.Platform.HUAWEI) {
					Intent intent = new Intent(context, VodChannelPlayActivity.class);
					intent.putExtra(IPTVUriUtils.EXTRA_PARAMS, vodDetailobj);
						intent.putExtra(IPTVUriUtils.ORDER_STATUS, vodDetailobj.ordered);
					intent.putExtra(IPTVUriUtils.PLAY_URL_PARAMS,result.RTSPURL);
					intent.putExtra(IPTVUriUtils.SUB_VOD_CHANNELID_PARAMS, subVideoChannelID);
					intent.putExtra(Constants.cPlatformExtra, vodDetailobj.platform);
					context.startActivity(intent);
				} else if (vodDetailobj.platform == EnumType.Platform.HOTEL) {
		            Intent intent = new Intent(context, TVPlayerActivity.class);
					intent.putExtra(IPTVUriUtils.EXTRA_PARAMS, vodDetailobj);
					intent.putExtra(IPTVUriUtils.ORDER_STATUS, vodDetailobj.ordered);
					intent.putExtra(IPTVUriUtils.PLAY_URL_PARAMS, result.RTSPURL);
					intent.putExtra(Constants.cPlatformExtra, vodDetailobj.platform);
					intent.putExtra(IPTVUriUtils.SUB_VOD_CHANNELID_PARAMS, subVideoChannelID);
		            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);// | Intent.FLAG_ACTIVITY_CLEAR_TOP
			        
					context.startActivity(intent);
					LogUtils.info("启动MyActivity");
				}
			}
			
			@Override
			public void HttpFailHandler() {
				TvUtils.processHttpFail(context);
			}
		});
		mVodUrlFactory.setHttpHeaderParams(IPTVUriUtils.getHeader());
		VodUrlJsonFactory  mVodUrlJsonFactory=new VodUrlJsonFactory();
		mVodUrlJsonFactory.setHttpHeaderParams(IPTVUriUtils.getHeader());
		mVodUrlJsonFactory.setHttpEventHandler(new HttpEventHandler<VodUrl>() {
			
			@Override
			public void HttpSucessHandler(VodUrl result) {
					Intent intent = new Intent(context, VodChannelPlayActivity.class);
					intent.putExtra(IPTVUriUtils.EXTRA_PARAMS, vodDetailobj);
					intent.putExtra(IPTVUriUtils.ORDER_STATUS, vodDetailobj.ordered);
					intent.putExtra(IPTVUriUtils.PLAY_URL_PARAMS, result.RTSPURL);
					intent.putExtra(IPTVUriUtils.SUB_VOD_CHANNELID_PARAMS, subVideoChannelID);
					intent.putExtra(Constants.cPlatformExtra, vodDetailobj.platform);
					context.startActivity(intent);
			}
			
			@Override
			public void HttpFailHandler() {
				TvUtils.processHttpFail(context);
			}
		});
		
		LogUtils.info("准备下载播放数据！");
		if (TvApplication.platform==EnumType.Platform.ZTE) {
			mVodUrlJsonFactory.DownloadDatas(vodDetailobj.platform ,subVideoChannelID,"1","");
		}else{
		mVodUrlFactory.DownloadDatas(vodDetailobj.platform ,subVideoChannelID);
		}
	}
	
	
	/**
	 * 测试播放视频
	 */
	public static void tryVideo(final Context context, final VodDetailInfo vodDetailobj, final String subVideoChannelID) {
		quiteBackgroundMusic(context);
		AuthJsonFactry authJaonFactory=new AuthJsonFactry();
		authJaonFactory.setHttpHeaderParams(IPTVUriUtils.getHeader());
		
		VodUrlFactory mVodUrlFactory = new VodUrlFactory();
		mVodUrlFactory.setHttpHeaderParams(IPTVUriUtils.getHeader());
		
		final VodUrlJsonFactory vodUrlJsonFactory=new VodUrlJsonFactory();
		vodUrlJsonFactory.setHttpHeaderParams(IPTVUriUtils.getHeader());
		authJaonFactory.setHttpEventHandler(new HttpEventHandler<Auth>() {

			@Override
			public void HttpSucessHandler(Auth result) {
				String authID=result.AuthorizationID;
				if (authID!=null&&!authID.equals("")) {
					
					vodUrlJsonFactory.DownloadDatas(vodDetailobj.platform ,subVideoChannelID,"1",authID);
				}else{
					LogUtils.error("播放获取URL鉴权失败：    "+result._return_message);
				}
				
			}

			@Override
			public void HttpFailHandler() {
				TvUtils.processHttpFail(context);
				
			}
		});
		
		
		mVodUrlFactory.setHttpEventHandler(new HttpEventHandler<VodUrl>() {
			
			@Override
			public void HttpSucessHandler(VodUrl result) {
				LogUtils.error("tryVideo:"+vodDetailobj.platform);
				if (vodDetailobj.platform == EnumType.Platform.HUAWEI) {
					Intent intent = new Intent(context, VodChannelPlayActivity.class);
					//如果时长小于5分钟  不试播
					if (( vodDetailobj.ELAPSETIME-1)<=5) {
						if (vodDetailobj.ISSITCOM==1) 
							intent.putExtra(IPTVUriUtils.ORDER_STATUS, false);
						else
							intent.putExtra(IPTVUriUtils.ORDER_STATUS, true);
					}else{
						intent.putExtra(IPTVUriUtils.ORDER_STATUS, false);
					}
					intent.putExtra(IPTVUriUtils.EXTRA_PARAMS, vodDetailobj);
					intent.putExtra(IPTVUriUtils.PLAY_URL_PARAMS, result.RTSPURL);
					intent.putExtra(IPTVUriUtils.SUB_VOD_CHANNELID_PARAMS, subVideoChannelID);
					intent.putExtra(Constants.cPlatformExtra, vodDetailobj.platform);
					context.startActivity(intent);
				} else if (vodDetailobj.platform == EnumType.Platform.HOTEL) {
					// http串
		            Intent intent = new Intent(context, TVPlayerActivity.class);
					intent.putExtra(IPTVUriUtils.EXTRA_PARAMS, vodDetailobj);
					if (( vodDetailobj.ELAPSETIME-1)<=5) {
						intent.putExtra(IPTVUriUtils.ORDER_STATUS, true);
					}else{
						intent.putExtra(IPTVUriUtils.ORDER_STATUS, false);
					}
					intent.putExtra(IPTVUriUtils.PLAY_URL_PARAMS, result.RTSPURL);
					intent.putExtra(Constants.cPlatformExtra, vodDetailobj.platform);
					intent.putExtra(IPTVUriUtils.SUB_VOD_CHANNELID_PARAMS, subVideoChannelID);
		            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);// | Intent.FLAG_ACTIVITY_CLEAR_TOP
			        
					context.startActivity(intent);
					LogUtils.info("启动MyActivity");
				}
			}
			
			@Override
			public void HttpFailHandler() {
				TvUtils.processHttpFail(context);
			}
		});
		
		vodUrlJsonFactory.setHttpEventHandler(new HttpEventHandler<VodUrl>() {

			@Override
			public void HttpSucessHandler(VodUrl result) {
				if(vodDetailobj.platform == EnumType.Platform.ZTE) {
					Intent intent = new Intent(context, VodChannelPlayActivity.class);
					if (( vodDetailobj.ELAPSETIME-1)<=5) {
						intent.putExtra(IPTVUriUtils.ORDER_STATUS, true);
					}else{
						intent.putExtra(IPTVUriUtils.ORDER_STATUS, false);
					}
					intent.putExtra(IPTVUriUtils.EXTRA_PARAMS, vodDetailobj);
					intent.putExtra(IPTVUriUtils.PLAY_URL_PARAMS, result.RTSPURL);
					intent.putExtra(IPTVUriUtils.SUB_VOD_CHANNELID_PARAMS, subVideoChannelID);
					intent.putExtra(Constants.cPlatformExtra, vodDetailobj.platform);
					context.startActivity(intent);
				}
			}

			@Override
			public void HttpFailHandler() {
				TvUtils.processHttpFail(context);
				
			}
		});
		
		LogUtils.info("准备下载播放数据！");
		if (TvApplication.platform==EnumType.Platform.RUNHUAWEI || TvApplication.platform==EnumType.Platform.DEVHUAWEI) {
			mVodUrlFactory.DownloadDatas(vodDetailobj.platform ,subVideoChannelID);
		}else if (TvApplication.platform==EnumType.Platform.ZTE) {
			if (vodDetailobj.ISSITCOM==1) {
				vodUrlJsonFactory.DownloadDatas(vodDetailobj.platform ,vodDetailobj.SUBVODIDLIST.get(0).VODID,"1","");
			}else{
				authJaonFactory.DownloadDatas(vodDetailobj.columncode,vodDetailobj.VODID,vodDetailobj.CONTENTCODE,"0",vodDetailobj.programtype,"");
			}
		}
	}
	
	/**
	 * @param context
	 * @param video
	 */
	public static void playVideo(final Context context, final VodChannel video) {
		
		quiteBackgroundMusic(context);
		VodUrlFactory mVodUrlFactory = new VodUrlFactory();
		mVodUrlFactory.setHttpEventHandler(new HttpEventHandler<VodUrl>() {
			
			@Override
			public void HttpSucessHandler(VodUrl result) {
				if (video.platform == EnumType.Platform.HUAWEI) {
					Intent intent = new Intent(context, VodChannelPlayActivity.class);
					intent.putExtra(IPTVUriUtils.EXTRA_PARAMS, video);
					intent.putExtra(IPTVUriUtils.PLAY_URL_PARAMS, result.RTSPURL);
					intent.putExtra(IPTVUriUtils.SUB_VOD_CHANNELID_PARAMS, video.VODID);
					
					context.startActivity(intent);
				} else if (video.platform == EnumType.Platform.HOTEL) {
					// http串
		            Intent intent = new Intent(context, TVPlayerActivity.class);
					intent.putExtra(IPTVUriUtils.EXTRA_PARAMS, video);
					//result.RTSPURL = "http://192.168.0.108:1935/vod/mp4:hiltongroup.mp4/playlist.m3u8";
					intent.putExtra(IPTVUriUtils.PLAY_URL_PARAMS, result.RTSPURL);
					intent.putExtra(IPTVUriUtils.SUB_VOD_CHANNELID_PARAMS, video.VODID);
		            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);// | Intent.FLAG_ACTIVITY_CLEAR_TOP
		           
					context.startActivity(intent);
				}
			}
			
			@Override
			public void HttpFailHandler() {
				TvUtils.processHttpFail(context);
			}
		});
		mVodUrlFactory.setHttpHeaderParams(IPTVUriUtils.getHeader());
		mVodUrlFactory.DownloadDatas(video.platform, video.VODID);
	}
	
	/**
	 * @param context
	 * @param video
	 */
	public static void myHotelshowPictures(final Context context, final MyHotelPicture video) {
		quiteBackgroundMusic(context);
		
		VodUrlFactory mVodUrlFactory = new VodUrlFactory();
		mVodUrlFactory.setHttpEventHandler(new HttpEventHandler<VodUrl>() {
			
			@Override
			public void HttpSucessHandler(VodUrl result) {
				Intent intent = new Intent(context, VodChannelPlayActivity.class);
				intent.putExtra(IPTVUriUtils.EXTRA_PARAMS, video);
				intent.putExtra(IPTVUriUtils.PLAY_URL_PARAMS, result.RTSPURL);
				//intent.putExtra(IPTVUriUtils.SUB_VOD_CHANNELID_PARAMS, video.VODID);
				context.startActivity(intent);
			}
			
			@Override
			public void HttpFailHandler() {
				TvUtils.processHttpFail(context);
			}
		});
		mVodUrlFactory.setHttpHeaderParams(IPTVUriUtils.getHeader());
		//mVodUrlFactory.DownloaDatas(video.);
	}
	
	/**
	 * @param context
	 * @param recChan
	 * @param recBill
	 */
	public static void playVideo(final Context context, final RecChan recChan, final RecBill recBill) {
		quiteBackgroundMusic(context);
		
		RecUrlFactory mRecUrlFactory = new RecUrlFactory();
		mRecUrlFactory.setHttpHeaderParams(IPTVUriUtils.getHeader());
		mRecUrlFactory.setHttpEventHandler(new HttpEventHandler<RecUrl>() {
			
			@Override
			public void HttpSucessHandler(RecUrl result) {
				Intent intent = new Intent(context, RecChannelPlayActivity.class);
				intent.putExtra(IPTVUriUtils.EXTRA_PARAMS, recChan);
				intent.putExtra(IPTVUriUtils.RECBILL_PARAMS, recBill);
				intent.putExtra(IPTVUriUtils.PLAY_URL_PARAMS, result.RTSPURL);
				context.startActivity(intent);
			}
			
			@Override
			public void HttpFailHandler() {
				TvUtils.processHttpFail(context);
			}
		});
		
		final RecUrlJsonFactory mRecUrlJsonFactory = new RecUrlJsonFactory();
		mRecUrlJsonFactory.setHttpHeaderParams(IPTVUriUtils.getHeader());
		mRecUrlJsonFactory.setHttpEventHandler(new HttpEventHandler<RecUrl>() {
			
			@Override
			public void HttpSucessHandler(RecUrl result) {
				Intent intent = new Intent(context, RecChannelPlayActivity.class);
				intent.putExtra(IPTVUriUtils.EXTRA_PARAMS, recChan);
				intent.putExtra(IPTVUriUtils.RECBILL_PARAMS, recBill);
				intent.putExtra(IPTVUriUtils.PLAY_URL_PARAMS, result.RTSPURL);
				context.startActivity(intent);
			}
			
			@Override
			public void HttpFailHandler() {
				TvUtils.processHttpFail(context);
			}
		});
		
		AuthJsonFactry authJaonFactory=new AuthJsonFactry();
		authJaonFactory.setHttpHeaderParams(IPTVUriUtils.getHeader());
		authJaonFactory.setHttpEventHandler(new HttpEventHandler<Auth>() {

			@Override
			public void HttpSucessHandler(Auth result) {
				String authID=result.AuthorizationID;
				if (authID!=null&&!authID.equals("")) {
					mRecUrlJsonFactory.DownloadDatas(recChan.CHANNELIDZTE, recBill.programIdZte,authID);
				}else{
					LogUtils.error("播放获取URL鉴权失败：    "+result._return_message);
				}
				
			}

			@Override
			public void HttpFailHandler() {
				TvUtils.processHttpFail(context);
				
			}
		});
		
		
		
		if (TvApplication.platform==EnumType.Platform.ZTE) {
//			mRecUrlJsonFactory.DownloadDatas(recChan.CHANNELIDZTE, recBill.programIdZte);
			authJaonFactory.DownloadDatas("",recChan.CHANNELIDZTE,"","2","4","");
		}else{
			mRecUrlFactory.DownloadDatas(recChan.CHANNELID, recBill.programId);
		}
		
	}

	public static String createPlayPosition(Context context, String subTitle, long playPostion) {
		int positionM = AppCommonUtils.minutesOf(playPostion);
		int positions = AppCommonUtils.secondsOf(playPostion);
		
		if (subTitle == null) {
			subTitle = "";
		}
		if (subTitle.equals("")) {
			return String.format(context.getResources().getString(R.string.history_sub_title_1), positionM, positions);
		} else {
			return String.format(context.getResources().getString(R.string.history_sub_title_2), subTitle, positionM, positions);
		}
	}
	
}
