package com.iptv.rocky.model.home;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.Log;
import android.widget.Toast;

import com.iptv.common.data.EnumType;
import com.iptv.common.utils.CommonUtils;
import com.iptv.common.utils.Constants;
import com.iptv.common.utils.DataReloadUtil;
import com.iptv.common.utils.LogUtils;
import com.iptv.rocky.AboutActivity;
import com.iptv.rocky.AccountSettingActivity;
import com.iptv.rocky.LiveTypeActivity;
import com.iptv.rocky.MyHotelSecActivity;
import com.iptv.rocky.MyHotelShowPicturesActivity;
import com.iptv.rocky.PlaySettingActivity;
import com.iptv.rocky.R;
import com.iptv.rocky.RecChanActivity;
import com.iptv.rocky.SpecialCategoryActivity;
import com.iptv.rocky.SpecialDetailActivity;
import com.iptv.rocky.UserSettingActivity;
import com.iptv.rocky.VodChannelDetailActivity;
import com.iptv.rocky.VodHistoryActivity;
import com.iptv.rocky.VodMovieActivity;
import com.iptv.rocky.VodMovieColumnActivity;
import com.iptv.rocky.VodMovieListActivity;
import com.iptv.rocky.VodSecActivity;
import com.iptv.rocky.VodStoreActivity;
import com.iptv.rocky.VodTypeColumnActivity;
import com.iptv.rocky.base.BaseMetroItemData;
import com.iptv.rocky.model.TvApplication;
import com.iptv.rocky.tcl.LiveChannelPlayActivity;
import com.iptv.rocky.utils.AppCommonUtils;

public abstract class HomeItemData extends BaseMetroItemData {
	
	public HomePageItem pageItem;

	@Override
	public void onClick(Context context) {
		LogUtils.debug("pageItem.id:"+pageItem.id);
		
		if (pageItem.id == DataReloadUtil.HomeRecChanViewID) {
            Intent intent = new Intent(context, RecChanActivity.class);
            context.startActivity(intent);
            return;
		} else if (pageItem.id == DataReloadUtil.HomeHistoryViewID) {
            Intent intent = new Intent(context, VodHistoryActivity.class);
            context.startActivity(intent);
            return;
        } else if (pageItem.id == DataReloadUtil.HomeStoreViewID) {
            Intent intent = new Intent(context, VodStoreActivity.class);
            context.startActivity(intent);
            return;
        } else if (pageItem.id == DataReloadUtil.HomePlaySettingViewID) {
            Intent intent = new Intent(context, PlaySettingActivity.class);
            context.startActivity(intent);
            return;
        } else if (pageItem.id == DataReloadUtil.HomeUserSettingViewID) {
            Intent intent = new Intent(context, UserSettingActivity.class);
            context.startActivity(intent);
            return;
        } else if (pageItem.id == DataReloadUtil.HomeAccountViewID) {
            Intent intent = new Intent(context, AccountSettingActivity.class);
            context.startActivity(intent);
            return;
        } else if (pageItem.id == DataReloadUtil.HomeAboutViewID) {
            Intent intent = new Intent(context, AboutActivity.class);
            context.startActivity(intent);
            return;
        } else if (pageItem.id == DataReloadUtil.HomePersonalViewID) {
        	startActivity(context,"com.hybroad.personalallsec");
            return;
        } else if (pageItem.id == DataReloadUtil.HomeOrderViewID) {
        	AppCommonUtils.showToast(context, context.getString(R.string.service_tips));
            return;
        } else if (pageItem.id == DataReloadUtil.HomeTexiViewID) {
        	AppCommonUtils.showToast(context, context.getString(R.string.service_tips));
            return;
        } else if (pageItem.id == DataReloadUtil.HomeCleanViewID) {
        	AppCommonUtils.showToast(context, context.getString(R.string.service_tips));
            return;
        }

		boolean todo = false;
        LogUtils.info("点击页面单项，判断类型:"+pageItem.contenttype+"; title: "+pageItem.title+" "+pageItem.contentid+"; contenttype"+pageItem.contenttype+";    分类类型："+pageItem.subcontenttype);
        Intent intent = null;
        
        switch (pageItem.contenttype) {
            case HOME_MOVIE:
            	if (TvApplication.platform==EnumType.Platform.ZTE) {
            		 if (pageItem.contentid!=null && (!pageItem.contentid.isEmpty())) {
                 		LogUtils.error("节目ID "+pageItem.contentid +"   platform:"+pageItem.platform.toString());
                          intent = new Intent(context, VodChannelDetailActivity.class);
                          intent.putExtra(Constants.cPlatformExtra, pageItem.platform.toString());	
                          intent.putExtra(Constants.cDetailIdExtra, pageItem.contentid);
                          intent.putExtra(Constants.CDETAIL_COLUMNCODE_EXTRA, pageItem.columnCode);	
                          intent.putExtra(Constants.CDETAIL_CONTENTCODE_EXTRA, pageItem.contentCode);
                          context.startActivity(intent);
                      }
            	}else {
            		LogUtils.debug("pageItem.contentid:"+pageItem.contentid);
            		 if (pageItem.contentid != null && (!pageItem.contentid.isEmpty())) {
            			
            			 if(pageItem.contentid != null){
            				 LogUtils.error("节目ID "+pageItem.contentid +"   platform:"+pageItem.platform.toString());
                             intent = new Intent(context, VodChannelDetailActivity.class);
                             intent.putExtra(Constants.cPlatformExtra, pageItem.platform.toString());	
                             intent.putExtra(Constants.cDetailIdExtra, pageItem.contentid);
                             intent.putExtra(Constants.CDETAIL_COLUMNCODE_EXTRA, "");	
                             context.startActivity(intent);
            			 }
                 		
                      }
				}
            	
            	break;
            case HOME_SERIES:
            	if (TvApplication.platform==EnumType.Platform.ZTE) {
            		if (pageItem.contentid!=null&&!"".equals(pageItem.contentid)) {
                        intent = new Intent(context, VodChannelDetailActivity.class);
                        intent.putExtra(Constants.CDETAIL_COLUMNCODE_EXTRA, pageItem.columnCode);	
                        intent.putExtra(Constants.CDETAIL_CONTENTCODE_EXTRA, pageItem.contentCode);
                        intent.putExtra(Constants.cPlatformExtra, pageItem.platform.toString());
                        context.startActivity(intent);
                    }
				}else{
					if (pageItem.contentid != null) {
	                     intent = new Intent(context, VodChannelDetailActivity.class);
	                     intent.putExtra(Constants.cPlatformExtra, pageItem.platform.toString());
	                     intent.putExtra(Constants.cDetailIdExtra, pageItem.contentid);
	                     intent.putExtra(Constants.CDETAIL_COLUMNCODE_EXTRA, "");
	                     context.startActivity(intent);
	                 }
				}
            	 
            	break;
            case HOME_HOTEL_INTRODUCE:
            	if (pageItem.contenttype == EnumType.ContentType.HOME_HOTEL_INTRODUCE) {
            		intent = new Intent(context, MyHotelShowPicturesActivity.class);
            		intent.putExtra(Constants.cListIdExtra, pageItem.contentid);
                	intent.putExtra(Constants.cListNameExtra, "contentId");
                	context.startActivity(intent);
            	}
            	break;
            case HOME_SPECIAL:
            	//if (pageItem.contentid == "-1") {
            		intent = new Intent(context, SpecialDetailActivity.class);
            		intent.putExtra(Constants.cSpecialDetailExtra, pageItem.contentid);
            		intent.putExtra(Constants.cSpecialDetailPlatformExtra, pageItem.platform.toString());
            		context.startActivity(intent);
                //}
            	break;
            	
            	//intent = new Intent(context, SpecialDetailActivity.class);
            	//intent.putExtra(Constants.cSpecialDetailExtra, CommonUtils.parseInt(pageItem.contentid));
    			//context.startActivity(intent);
    			//break;
            case VIDEO:
            	 if (TvApplication.platform==EnumType.Platform.ZTE) {
            		 if (pageItem.contentid!=null&&"".equals(pageItem.contentid)) {
                         intent = new Intent(context, VodChannelDetailActivity.class);
                         intent.putExtra(Constants.CDETAIL_COLUMNCODE_EXTRA, pageItem.columnCode);
                    	 intent.putExtra(Constants.CDETAIL_CONTENTCODE_EXTRA, pageItem.contentid);
                    	 intent.putExtra(Constants.cPlatformExtra, pageItem.platform.toString());	
                         context.startActivity(intent);
                     }
                	
                	 LogUtils.info("platform---->"+TvApplication.platform);
				}else{
					if (CommonUtils.parseInt(pageItem.contentid) > 0) {
	                    intent = new Intent(context, VodChannelDetailActivity.class);
	                    intent.putExtra(Constants.cDetailIdExtra, CommonUtils.parseInt(pageItem.contentid));
	                    intent.putExtra(Constants.CDETAIL_COLUMNCODE_EXTRA, pageItem.columnCode);
	                    context.startActivity(intent);
	                }
				}
            	
                break;
            case LIVE_CHANNEL:
            {
            	// 后面加判断频道是哪种，看是否在一个播放器中还是独立的播放器实现？
                intent = new Intent(context, LiveChannelPlayActivity.class);
                intent.putExtra(Constants.cDetailIdExtra, pageItem.contentid);
                context.startActivity(intent);
            	break;
            }
            case LIVE_TYPE:
            {
        		intent = new Intent(context, LiveTypeActivity.class);
    			intent.putExtra(Constants.cListIdExtra, pageItem.contentid);
    			LogUtils.info("pageItem.contentid--->"+pageItem.contentid);
    			context.startActivity(intent);
            	break;
            }
            case VOD_TOP_CATEGORY:
            {
            	if (pageItem.subcontenttype == EnumType.SubContentType.IPTV_SECOND_CATEGORY_LIST)
            	{
            		intent = new Intent(context, VodSecActivity.class);
                    intent.putExtra(Constants.cListIdExtra, pageItem.contentid);
                    intent.putExtra(Constants.cListNameExtra, pageItem.title);
                    context.startActivity(intent);
            	} else if (pageItem.subcontenttype == EnumType.SubContentType.IPTV_VIRTICAL_POSTER_CONTENT_lIST) {
            		intent = new Intent(context, VodMovieActivity.class); 
            		intent.putExtra(Constants.cPlatformExtra, EnumType.Platform.createPlatform(TvApplication.platform));
            		intent.putExtra(Constants.cListIdExtra, pageItem.contentid);
                    intent.putExtra(Constants.cListNameExtra, pageItem.title);
                    context.startActivity(intent);
                    break;
            	} else if (pageItem.subcontenttype == EnumType.SubContentType.HOTEL_SECOND_CATEGORY_LIST) {
            		intent = new Intent(context, VodSecActivity.class);
                    intent.putExtra(Constants.cListIdExtra, pageItem.contentid);
                    intent.putExtra(Constants.cListNameExtra, pageItem.title);
                    context.startActivity(intent);
            	} else if (pageItem.subcontenttype == EnumType.SubContentType.HOTEL_HORIZONTAL_POSTER_CONTENT_lIST) {
            		/*if (CommonUtils.parseInt(pageItem.contentid) > 0) {
                        intent = new Intent(context, VodChannelDetailActivity.class);
                        intent.putExtra(Constants.cDetailIdExtra, CommonUtils.parseInt(pageItem.contentid));
                        context.startActivity(intent);
                    }*/
            	} else if (pageItem.subcontenttype == EnumType.SubContentType.HOTEL_VIRTICAL_POSTER_CONTENT_lIST) {
            		intent = new Intent(context, VodMovieActivity.class);	
            		intent.putExtra(Constants.cPlatformExtra, EnumType.Platform.HOTEL.toString());	
            		intent.putExtra(Constants.cListIdExtra, pageItem.contentid);
            		intent.putExtra(Constants.cListNameExtra, pageItem.title);
            		context.startActivity(intent);
            	} else if (pageItem.subcontenttype == EnumType.SubContentType.HOTEL_MOVIE) {
            		 intent = new Intent(context, VodChannelDetailActivity.class);
            		 intent.putExtra(Constants.cPlatformExtra, EnumType.Platform.HOTEL.toString());	
                     intent.putExtra(Constants.cDetailIdExtra, CommonUtils.parseInt(pageItem.contentid));
                     context.startActivity(intent);
            	} else if (pageItem.subcontenttype == EnumType.SubContentType.HOTEL_SERIES) {
            		intent = new Intent(context, VodChannelDetailActivity.class);
            		intent.putExtra(Constants.cPlatformExtra, EnumType.Platform.HOTEL.toString());	
                    intent.putExtra(Constants.cDetailIdExtra, CommonUtils.parseInt(pageItem.contentid));
                    context.startActivity(intent);
            	}   
                break;
            }
            case VOD_SEC_CATEGORY:
            {
            	intent = null;
            	if (pageItem.subcontenttype == EnumType.SubContentType.IPTV_MOVIE)
            	{
            		intent = new Intent(context, VodMovieActivity.class);
            	} else if (pageItem.subcontenttype == EnumType.SubContentType.IPTV_SERIES) {
            		if (TvApplication.platform==EnumType.Platform.ZTE) {
            			   intent = new Intent(context, VodChannelDetailActivity.class);
            			   intent.putExtra(Constants.cPlatformExtra, pageItem.platform.toString());	
                           intent.putExtra(Constants.CDETAIL_COLUMNCODE_EXTRA, pageItem.columnCode);	
                           intent.putExtra(Constants.CDETAIL_CONTENTCODE_EXTRA, pageItem.contentCode);
                           context.startActivity(intent);
					}else{
						if (CommonUtils.parseInt(pageItem.contentid) > 0) {
	                        intent = new Intent(context, VodChannelDetailActivity.class);
	                        intent.putExtra(Constants.cDetailIdExtra, CommonUtils.parseInt(pageItem.contentid));
	                        intent.putExtra(Constants.CDETAIL_COLUMNCODE_EXTRA, pageItem.columnCode);
	                        context.startActivity(intent);
	                    }
					}
            		
            	} else if (pageItem.subcontenttype == EnumType.SubContentType.IPTV_VIRTICAL_POSTER_CONTENT_lIST) {
            		intent = new Intent(context, VodMovieActivity.class); 
            		intent.putExtra(Constants.cPlatformExtra, EnumType.Platform.createPlatform(TvApplication.platform));
            	} else if (pageItem.subcontenttype == EnumType.SubContentType.IPTV_HORIZONTAL_POSTER_CONTENT_lIST) {
            		intent = new Intent(context, VodMovieColumnActivity.class);
            		intent.putExtra(Constants.cPlatformExtra, EnumType.Platform.createPlatform(TvApplication.platform));
            	} else if (pageItem.subcontenttype == EnumType.SubContentType.IPTV_THIRD_CATEGORY_LIST) {
            		intent = new Intent(context, VodTypeColumnActivity.class);
            		intent.putExtra(Constants.cPlatformExtra, EnumType.Platform.createPlatform(TvApplication.platform));
            	} else if (pageItem.subcontenttype == EnumType.SubContentType.IPTV_PURE_TEXT_CONTENT_LIST) {
            		intent = new Intent(context, VodMovieListActivity.class);
            		intent.putExtra(Constants.cPlatformExtra, EnumType.Platform.createPlatform(TvApplication.platform));
            	} else if (pageItem.subcontenttype == EnumType.SubContentType.HOTEL_HORIZONTAL_POSTER_CONTENT_lIST) {
            		intent = new Intent(context, VodMovieColumnActivity.class);
            		intent.putExtra(Constants.cPlatformExtra, EnumType.Platform.HOTEL.toString());
            	} else if (pageItem.subcontenttype == EnumType.SubContentType.HOTEL_VIRTICAL_POSTER_CONTENT_lIST) {
            		intent = new Intent(context, VodMovieActivity.class);	
            		intent.putExtra(Constants.cPlatformExtra, EnumType.Platform.HOTEL.toString());
            	} else if (pageItem.subcontenttype == EnumType.SubContentType.HOTEL_PURE_TEXT_CONTENT_LIST) {
            		intent = new Intent(context, VodMovieActivity.class);	
            		intent.putExtra(Constants.cPlatformExtra, EnumType.Platform.HOTEL.toString());	
            	} else if (pageItem.subcontenttype == EnumType.SubContentType.HOTEL_MOVIE) {
            		intent = new Intent(context, VodMovieActivity.class);
            		intent.putExtra(Constants.cPlatformExtra, EnumType.Platform.HOTEL.toString());	
            	} else if (pageItem.subcontenttype == EnumType.SubContentType.HOTEL_SERIES) {
            		if (CommonUtils.parseInt(pageItem.contentid) > 0) {
                        intent = new Intent(context, VodChannelDetailActivity.class);
                        intent.putExtra(Constants.cDetailIdExtra, CommonUtils.parseInt(pageItem.contentid));
                        intent.putExtra(Constants.CDETAIL_COLUMNCODE_EXTRA, pageItem.columnCode);
                        context.startActivity(intent);
                    }
            		intent.putExtra(Constants.cPlatformExtra, EnumType.Platform.HOTEL.toString());	
            	} else if (pageItem.subcontenttype == EnumType.SubContentType.HOTEL_SPECIAL) {
            		
            	}
            	
            	intent.putExtra(Constants.cListIdExtra, pageItem.contentid);
                intent.putExtra(Constants.cListNameExtra, pageItem.title);
                
                if (intent != null) {
                    context.startActivity(intent);
                } else {
                	AppCommonUtils.showToast(context, context.getString(R.string.http_notfound));
                }
                break;
            }
            case SPECIAL_CATEGORY:
            	LogUtils.error("special_category: pageItem.contentid"+pageItem.contentid);
            	//if (pageItem.contentid == "-1") {
            		intent = new Intent(context, SpecialCategoryActivity.class);
            		intent.putExtra(Constants.cListIdExtra, pageItem.contentid);
            		context.startActivity(intent);
                //}
            	break;
            case CATEGORY:
            	LogUtils.error("category: pageItem.contentid"+pageItem.contentid);
//                Intent intent = null;
//                if (CommonUtils.parseInt(pageItem.contentid) > 0) {
//                    intent = new Intent(context, ListActivity.class);
//                    intent.putExtra(Constants.cListIdExtra, pageItem.contentid);
//                    intent.putExtra(Constants.cListNameExtra, pageItem.title);
//                }
//                if (intent != null) {
//                    context.startActivity(intent);
//                } else {
//                	AppCommonUtils.showToast(context, context.getString(R.string.http_notfound));
//                }
            	if (pageItem.contentid == "-1") {
            		intent = new Intent(context, SpecialCategoryActivity.class);
            		context.startActivity(intent);
                }

                break;
            case HOTEL_TOP_CATEGORY:
            {
            	LogUtils.debug("子类:"+pageItem.myHotelSubContentType);
            	
            	if (pageItem.myHotelSubContentType == EnumType.MyHotelSubContentType.MYHOTEL_SECOND_CATEGORY_LIST) {
            		intent = new Intent(context, MyHotelSecActivity.class);
                    intent.putExtra(Constants.cListIdExtra, pageItem.contentid);
                    intent.putExtra(Constants.cListNameExtra, pageItem.title);
                    context.startActivity(intent);
            	} else if (pageItem.myHotelSubContentType == EnumType.MyHotelSubContentType.HOTEL_VIRTICAL_POSTER_CONTENT_lIST) {
            		intent = new Intent(context, VodMovieActivity.class);	
            		intent.putExtra(Constants.cPlatformExtra, EnumType.Platform.HOTEL.toString());
            		intent.putExtra(Constants.cListNameExtra, pageItem.title);
            		intent.putExtra(Constants.cListIdExtra, pageItem.contentid);
            		context.startActivity(intent);
            	} else if(pageItem.myHotelSubContentType == EnumType.MyHotelSubContentType.MYHOTEL_PICTURE_LIST){
            		intent = new Intent(context, MyHotelShowPicturesActivity.class);
            		intent.putExtra(Constants.cListIdExtra, pageItem.contentid);
                	intent.putExtra(Constants.cListNameExtra, "categoryId");
                	context.startActivity(intent);
            	}else {
            		LogUtils.debug("尚未实现的类型："+pageItem.myHotelSubContentType);
            	}
            	break;
            }	
            case HOTEL_SEC_CATEGORY:
            {	
            	intent = null;
            	Log.d("HomeItemData", "子类类型："+pageItem.myHotelSubContentType);
            	if (pageItem.myHotelSubContentType == EnumType.MyHotelSubContentType.MYHOTEL_PICTURE_LIST)
            	{
            		/*Log.d("HomeItemData","contentid is null?"+(pageItem.contentid));
                	Log.d("HomeItemData","pageItem.title is null?"+(pageItem.title));
            		intent = new Intent(context, MyHotelPictureListActivity.class);
            		intent.putExtra(Constants.cListIdExtra, pageItem.contentid);
                	intent.putExtra(Constants.cListNameExtra, pageItem.title);*/
            		
            		intent = new Intent(context, MyHotelShowPicturesActivity.class);
            		intent.putExtra(Constants.cListIdExtra, pageItem.contentid);
                	// intent.putExtra(Constants.cListNameExtra, pageItem.title);
                	
                	intent.putExtra(Constants.cListNameExtra, "categoryId");
            	}
            	
                if (intent != null) {
                    context.startActivity(intent);
                } else {
                	AppCommonUtils.showToast(context, context.getString(R.string.http_notfound));
                }
            	break;
            }	
            case UNKNOW:
            	todo = true;
            	break;
            default:
                break;
        } 
    }
	
    public void initSpecialData() {

    }
    
    
    private void startActivity(Context mContext, String packageName)
    {
        // 根据包名启动应用
        PackageManager pkmanager = mContext.getPackageManager();
        
        // 根据包名获取 启动该应用的意图
        Intent openInten = pkmanager.getLaunchIntentForPackage(packageName);
        
        if(openInten == null)
        {
            Toast toast = Toast.makeText(mContext, "应用不存在", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        
        openInten.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        
        // 根据 意图查找 可以启动的activities
        List<ResolveInfo> acts = pkmanager.queryIntentActivities(openInten, 0);
        
        // 存在，就打开此应用
        if (acts.size() > 0)
        {
            mContext.startActivity(openInten);
            //overridePendingTransition(R.anim.alpha_launcher_show, R.anim.alpha_launcher_hide);
        }
    }
    
}
