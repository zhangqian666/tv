package com.iptv.rocky.view.usercenter.vodhistory;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.iptv.common.data.HistoryChannelInfo;
import com.iptv.common.utils.Constants;
import com.iptv.rocky.hwdata.local.VodHistoryLocalFactory;
import com.iptv.rocky.model.usercenter.vodhistory.HistoryAdapter;
import com.iptv.rocky.utils.AppCommonUtils;
import com.iptv.rocky.view.TvHorizontalListView;
import com.iptv.rocky.R;

public class HistoryHorizontalListView extends TvHorizontalListView {

    private int mCurrentPage = -1;
    private int mMaxPage = 1;
    
    private Context mContext;
    private VodHistoryLocalFactory mHistoryFactory;
    private HistoryAdapter mHistoryAdapter;
    private ArrayList<HistoryChannelInfo> mDatas = new ArrayList<HistoryChannelInfo>(32);

    public HistoryHorizontalListView(Context context) {
        this(context, null, 0);
    }

    public HistoryHorizontalListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HistoryHorizontalListView(Context context, AttributeSet attrs,
    		int defStyle) {
        super(context, attrs, defStyle);
        
        mContext = context;
        mHistoryFactory = new VodHistoryLocalFactory(context);
        
        int size = (int)mHistoryFactory.getRecordCount();
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
    protected void loadDatas() {
        if (++mCurrentPage <= mMaxPage) {
        	int pageNumber = Constants.cRequestNumber;
        	int pageIndex = mCurrentPage * pageNumber;
        	
        	ArrayList<HistoryChannelInfo> datas = mHistoryFactory.findRecords(pageIndex, pageNumber);
			if (datas == null) {
				mCurrentPage--;
				AppCommonUtils.showToast(mContext, mContext.getString(R.string.http_fail));
				return;
			}
            mDatas.addAll(datas);

            if (mHistoryAdapter == null) {
                mHistoryAdapter = new HistoryAdapter(getContext(), mDatas, this);
                setAdapter(mHistoryAdapter);
            } else {
                mDataChanged = true;
                mHistoryAdapter.notifyDataSetChanged();
            }
            mDataChanged = false;
            loaddingComplete();
        }
    }

    /**
     * 将ListView置为编辑状态
     */
    public void setListViewEdit() {
        mHistoryAdapter.isEdit = true;
        for (ImageView v : mHistoryAdapter.mEditViews) {
            v.setVisibility(VISIBLE);
        }
    }
    
    /**
     * 取消ListView编辑状态
     */
    public void cancelListViewEdit() {
        mHistoryAdapter.isEdit = false;
        for (ImageView v : mHistoryAdapter.mEditViews) {
            v.setVisibility(GONE);
        }
    }
    
    /**
     * 清空历史记录
     */
    public void clearHistory() {
        mDataChanged = true;
        mHistoryAdapter.clearHistory();
        mHistoryAdapter.notifyDataSetChanged();
        new VodHistoryLocalFactory(mContext).deletedRecords();
    }
    
    /**
     * 是否处于编辑状态
     * @return boolean
     */
    public boolean isEditing() {
        return mHistoryAdapter.isEdit;
    }

    public void setIsChanged(boolean isChanged) {
        this.mDataChanged = isChanged;
    }

    public int getItemCount() {
        return mDatas.size();
    }
}
