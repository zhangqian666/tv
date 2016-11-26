package com.iptv.rocky.view.vodthd;

import java.util.ArrayList;

import com.iptv.common.HttpEventHandler;
import com.iptv.common.data.VodSec;
import com.iptv.common.utils.Constants;
import com.iptv.rocky.hwdata.IPTVUriUtils;
import com.iptv.rocky.hwdata.json.VodThdJsonFactory;
import com.iptv.rocky.model.vodthd.VodThdAdapter;
import com.iptv.rocky.utils.TvUtils;
import com.iptv.rocky.view.TvGallery;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * 二级专题页面 分类页
 * */
public class VodThdGallery extends TvGallery {
	
	private View mProgressView;
	private String mColumnCode;
//	private VodThdFactory mVodThdFactory;
	private VodThdJsonFactory mvodThdJsonFactory;
	
	public VodThdGallery(Context context) {
		this(context, null, 0);
	}
	
	public VodThdGallery(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public VodThdGallery(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		
//		setOnItemSelectedListener(new OnItemSelectedListener() {
//
//			@Override
//			public void onItemSelected(AdapterView<?> parent, View view,
//					int position, long id) {
//				SpecialCategoryMetroView selected = (SpecialCategoryMetroView)view;
//				selected.stopMarquee();
//			}
//
//			@Override
//			public void onNothingSelected(AdapterView<?> parent) {
//			}
//		});
		//华为平台
//		mVodThdFactory = new VodThdFactory();
//		mVodThdFactory.setHttpHeaderParams(IPTVUriUtils.getHeader());
//		mVodThdFactory.setHttpEventHandler(new HttpEventHandler<ArrayList<VodSec>>() {
//			
//			@Override
//			public void HttpSucessHandler(ArrayList<VodSec> result) {
//				mProgressView.setVisibility(View.GONE);
//				
//				int pageNumber = Constants.cSpecialCategoryNumber;
//				int size = result.size();
//				int page = (size / pageNumber) + (size % pageNumber > 0 ? 1 : 0);
//				
//				ArrayList<SpecialCategoryMetroView> metroViews = new ArrayList<SpecialCategoryMetroView>(page);
//				for (int i = 0; i < page; i++) {
//					SpecialCategoryMetroView metroView = new SpecialCategoryMetroView(getContext());
//					if (i == 0) {
//						metroView.isAutoFocus = true;
//					}
//					metroViews.add(metroView);
//				}
//				
//				setAdapter(new VodThdAdapter(result, metroViews));
//			}
//			
//			@Override
//			public void HttpFailHandler() {
//				mProgressView.setVisibility(View.GONE);
//				TvUtils.processHttpFail(getContext());
//			}
//		});
		
		//中兴平台
		mvodThdJsonFactory = new VodThdJsonFactory();
		mvodThdJsonFactory.setHttpHeaderParams(IPTVUriUtils.getHeader());
		mvodThdJsonFactory.setHttpEventHandler(new HttpEventHandler<ArrayList<VodSec>>() {
			
			@Override
			public void HttpSucessHandler(ArrayList<VodSec> result) {
				mProgressView.setVisibility(View.GONE);
				int pageNumber = Constants.cSpecialCategoryNumber;
				int size = result.size();
				int page = (size / pageNumber) + (size % pageNumber > 0 ? 1 : 0);
				
				ArrayList<SpecialCategoryMetroView> metroViews = new ArrayList<SpecialCategoryMetroView>(page);
				for (int i = 0; i < page; i++) {
					SpecialCategoryMetroView metroView = new SpecialCategoryMetroView(getContext());
					if (i == 0) {
						metroView.isAutoFocus = true;
					}
					metroViews.add(metroView);
				}
				
				setAdapter(new VodThdAdapter(result, metroViews,mColumnCode));
			}
			
			@Override
			public void HttpFailHandler() {
				mProgressView.setVisibility(View.GONE);
				TvUtils.processHttpFail(getContext());
			}
		});
	}
	
	public void crateView(String id, View progressView) {
		mProgressView = progressView;
//		if (TvApplication.platform==EnumType.Platform.ZTE) {
		 this.mColumnCode=id;
			mvodThdJsonFactory.DownloadDatas(id);
//		}else{
//			mVodThdFactory.DownloadDatas(id);
//		}
	}
	
	public void destroy(){
//		if (TvApplication.platform==EnumType.Platform.ZTE) {
			mvodThdJsonFactory.cancel();
//		}else{
//			mVodThdFactory.cancel();
//		}
		
	}
}
