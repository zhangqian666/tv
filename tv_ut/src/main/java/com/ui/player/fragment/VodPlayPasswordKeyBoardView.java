package com.ui.player.fragment;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.GridLayout;


import com.iptv.common.utils.LogUtils;
import com.iptv.rocky.model.vodplay.KeyBoardData;
import com.iptv.rocky.utils.ScreenUtils;
import com.iptv.rocky.R;

/**
 * 点播订购，密码输入页键盘组件
 */
public class VodPlayPasswordKeyBoardView extends GridLayout {

	private int keys_base_id = 100; 
	
    private Context mContext;
    private Animation aniKeysFocus;
    private Animation aniKeysDown;
    private Animation aniKeysUp;

    public VodPlayPasswordKeyBoardView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mContext = context;
        int keyUnit = ScreenUtils.getSearchKeyUnit();
        int padding = ScreenUtils.getSearchKeyPadding();
        int textSize = ScreenUtils.getKeyTextSize();
        
        //this.setPadding(0, 0, 0, keyUnit / 3);
        this.setPadding(0, 0, 0, keyUnit / 3);
        this.setColumnCount(5);
        this.setOrientation(HORIZONTAL);

        for (int i = 0; i < KeyBoardData.KEYBOARD_VALUES.length; i++) {
        	LogUtils.info(KeyBoardData.KEYBOARD_TEXTS[i].toString());
            Button btn = new Button(context);
            GridLayout.LayoutParams btnParams = new GridLayout.LayoutParams();
            btnParams.setMargins(padding, padding, padding, padding);
            if (i >= 0 && i <= 4) {
                btnParams.topMargin = btnParams.topMargin * 2;
            }
            if (i == 0 || i == 5 || i == 10) {
                btnParams.leftMargin = btnParams.leftMargin * 4;
                LogUtils.error("Left Margin :"+btnParams.leftMargin + " Text:"+KeyBoardData.KEYBOARD_TEXTS[i]);
                int nextLeftId = keys_base_id + i + 5;
                if (i == 0) {
                    nextLeftId = nextLeftId - 1;
                }
                LogUtils.error("Next Focus LeftId "+nextLeftId);
                btn.setNextFocusLeftId(nextLeftId);
            }
            
            if (i == 4 || i == 9 || i == 11) 
            {
                btnParams.rightMargin = btnParams.rightMargin * 2;
                int nextRightId = keys_base_id + i - 5;
                if (i == 5) {
                    nextRightId = nextRightId + 1;
                }
                LogUtils.error("Next Focus RightId "+nextRightId);
                btn.setNextFocusRightId(nextRightId);
            }
            if (i >= 10) {
                btnParams.bottomMargin = btnParams.bottomMargin * 2;
                int nextDownId = keys_base_id + i - 10;
                if (i <= 11) {
                    nextDownId = nextDownId + 1;
                }
                btn.setNextFocusDownId(nextDownId);
            }
            
            btnParams.width = keyUnit;
            btnParams.height = keyUnit;
            if(KeyBoardData.KEYBOARD_VALUES[i].equals("delete")) {
                btnParams.columnSpec = GridLayout.spec(2, 2);
                btnParams.width = keyUnit * 2 + padding * 2;
            }
            if(KeyBoardData.KEYBOARD_VALUES[i].equals("OK")) {
                btnParams.columnSpec = GridLayout.spec(0, 2);
                btnParams.width = keyUnit * 2 + padding * 2;
            }
            btn.setId(keys_base_id + i);
            btn.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textSize);
            btn.setPadding(0, 0, 0, 0);
            btn.setLayoutParams(btnParams);
            btn.setBackgroundResource(R.drawable.search_keyboard_keybtn);
            btn.setText(KeyBoardData.KEYBOARD_TEXTS[i]);
            btn.setTag(KeyBoardData.KEYBOARD_VALUES[i]);
            btn.setOnFocusChangeListener(keysForcusChangeListener);
            btn.setOnKeyListener(keysOnKeyListener);
            
            /*btn.setOnClickListener(new View.OnClickListener(){
    			@Override
    			public void onClick(View v) {
    				LogUtils.error("按键收到下嗯信息");
    				
    			}
    		});*/
            
            
            this.addView(btn);
        }
        aniKeysDown = AnimationUtils.loadAnimation(mContext, R.anim.search_keyboard_keys_down);
        aniKeysUp = AnimationUtils.loadAnimation(mContext, R.anim.search_keyboard_keys_up);
        aniKeysFocus = AnimationUtils.loadAnimation(mContext, R.anim.search_keyboard_keys_focused);
        aniKeysDown.setFillAfter(true);
        aniKeysUp.setFillAfter(true);
        aniKeysFocus.setFillAfter(true);
    }

    public VodPlayPasswordKeyBoardView(Context context) {
        super(context);
    }

    private OnFocusChangeListener keysForcusChangeListener = new OnFocusChangeListener() {

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {           	
                v.startAnimation(aniKeysFocus);
            } else {
                v.clearAnimation();
            }
        }
    };

    private OnKeyListener keysOnKeyListener = new OnKeyListener() {
        private boolean isKeyDowning = false;
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {           
            if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (!isKeyDowning) {
                        v.startAnimation(aniKeysDown);
                    }
                    isKeyDowning = true;
                    return false;
                } else if (event.getAction() == KeyEvent.ACTION_UP) {
                    v.startAnimation(aniKeysUp);
                    isKeyDowning = false;
                    return false;
                }
            }
            return false;
        }
    };
}
