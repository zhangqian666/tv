package com.iptv.rocky.model.setting;

import java.util.HashMap;
import java.util.Iterator;

import android.content.Context;

import com.iptv.common.local.PlaySettingFactory;
import com.iptv.rocky.view.setting.SettingSelectItem;

/**
 * 切换选择型设置项适配器
 */
public class SettingSelectItemAdapter {
//  private Context mContext;
    //设置项标题
//  private String title;
    //该项数据在SharedPreferences存储的key
    private String key;
    //数据map
    private HashMap<Integer, String> dataMap;
    //当前的值
    private int curValue;
    //View
    private SettingSelectItem itemView;
    //SharedPreferences存储工厂类
    private PlaySettingFactory factory;

    private int[] valueArr;
    private int valueIndex;

    public SettingSelectItemAdapter(Context mContext, String title, String key, PlaySettingFactory factory,
                                    HashMap<Integer, String> dataMap, int defaultValue, SettingSelectItem itemView) {
//      this.title = title;
        this.factory = factory;
        this.curValue = factory.getInt(key, defaultValue);
//      this.mContext = mContext;
        this.key = key;
        this.dataMap = dataMap;
        this.itemView = itemView;
        valueArr = new int[this.dataMap.keySet().size()];
        Iterator<Integer> iterator = dataMap.keySet().iterator();
        int i = 0;
        while (iterator.hasNext()) {
            int value = iterator.next();
            if (value == this.curValue) {
                valueIndex = i;
            }
            valueArr[i] = value;
            i++;
        }
        itemView.initItemViews(title, dataMap.get(this.curValue));
        itemView.setOnItemValueChanngeListener(onItemValueChanngeListener);
    }

    private void changeValue() {
        itemView.setValueTxt(dataMap.get(curValue));
        factory.putInt(key, curValue);
    }

    private SettingSelectItem.OnItemValueChanngeListener onItemValueChanngeListener = new SettingSelectItem.OnItemValueChanngeListener() {

        @Override
        public void onChange(int orientation) {
            if (orientation == SettingSelectItem.OnItemValueChanngeListener.ORIENTATION_LEFT) {
                if (valueIndex > 0) {
                    valueIndex--;
                } else {
                    valueIndex = valueArr.length - 1;
                }
                curValue = valueArr[valueIndex];
                changeValue();
            }else if (orientation == SettingSelectItem.OnItemValueChanngeListener.ORIENTATION_RIGHT) {
                if (valueIndex < valueArr.length - 1) {
                    valueIndex++;
                } else {
                    valueIndex = 0;
                }
                curValue = valueArr[valueIndex];
                changeValue();
            }
        }
    };
}
