package com.iptv.rocky.view.usercenter.vodstore;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.iptv.common.data.StoreChannelInfo;
import com.iptv.common.utils.Constants;
import com.iptv.rocky.hwdata.local.VodStoreLocalFactory;
import com.iptv.rocky.model.usercenter.vodstore.StoreAdapter;
import com.iptv.rocky.utils.AppCommonUtils;
import com.iptv.rocky.view.TvHorizontalListView;
import com.iptv.rocky.R;

public class StoreHorizontalListView extends TvHorizontalListView {

	private int mCurrentPage = -1;
	private int mMaxPage = 1;
	
    private Context mContext;
    private VodStoreLocalFactory mStoreFactory;
	private StoreAdapter mStoreAdapter;
	private ArrayList<StoreChannelInfo> mDatas = new ArrayList<StoreChannelInfo>(32);

	public StoreHorizontalListView(Context context) {
		this(context, null, 0);
	}

	public StoreHorizontalListView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public StoreHorizontalListView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		
        mContext = context;
        mStoreFactory = new VodStoreLocalFactory(context);
        
        int size = (int)mStoreFactory.getRecordCount();
        mMaxPage = (size / Constants.cRequestNumber) + (size % Constants.cRequestNumber > 0 ? 1 : 0);
    }
	
	public void createView() {
		clear();
		loadDatas(); 
	}
	
	private void clear() {
		mDatas.clear();
		mCurrentPage = -1;
		mMaxPage = 1;
		mDataChanged = true;
	}

	@Override
	public void destroy() {
		super.destroy();
	}
	@Override
	protected void loadDatas() {
		if (++mCurrentPage <= mMaxPage) {
			int pageNumber = Constants.cRequestNumber;
        	int pageIndex = mCurrentPage * pageNumber;
        	
        	ArrayList<StoreChannelInfo> datas = mStoreFactory.findRecords(pageIndex, pageNumber);
        	if (datas == null) {
				mCurrentPage--;
				AppCommonUtils.showToast(mContext, mContext.getString(R.string.http_fail));
				return;
			}
            mDatas.addAll(datas);

            if (mStoreAdapter == null) {
                mStoreAdapter = new StoreAdapter(getContext(), mDatas, this);
                setAdapter(mStoreAdapter);
            } else {
                mDataChanged = true;
                mStoreAdapter.notifyDataSetChanged();
            }
            mDataChanged = false;
            loaddingComplete();
		}
	}

    public int getItemCount() {
        return mDatas.size();
    }

    /**
     * 将ListView置为编辑状态
     */
    public void setListViewEdit() {
        mStoreAdapter.isEdit = true;
        for(ImageView v:mStoreAdapter.mEditViews) {
            v.setVisibility(VISIBLE);
        }
    }
    
    /**
     * 取消ListView编辑状态
     */
    public void cancelListViewEdit() {
        mStoreAdapter.isEdit = false;
        for(ImageView v:mStoreAdapter.mEditViews) {
            v.setVisibility(GONE);
        }
    }
    
    /**
     * 清空我的收藏
     */
    public void clearStore() {
        mDataChanged = true;
        mStoreAdapter.clearStore();
        mStoreAdapter.notifyDataSetChanged();
        new VodStoreLocalFactory(mContext).deletedRecords();
    }
    
    /**
     * 是否处于编辑状态
     * @return boolean
     */
    public boolean isEditing() {
        return mStoreAdapter.isEdit;
    }

    public void setIsChanged(boolean isChanged) {
        this.mDataChanged = isChanged;
    }
}
