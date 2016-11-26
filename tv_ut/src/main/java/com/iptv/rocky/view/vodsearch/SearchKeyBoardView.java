package com.iptv.rocky.view.vodsearch;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.GridLayout;

import com.iptv.rocky.model.vodsearch.KeyBoardData;
import com.iptv.rocky.utils.ScreenUtils;
import com.iptv.rocky.R;

/**
 * 搜索页键盘组件
 */
public class SearchKeyBoardView extends GridLayout {

	private int keys_base_id = 100; 
	
    private Context mContext;
    private Animation aniKeysFocus;
    private Animation aniKeysDown;
    private Animation aniKeysUp;

    public SearchKeyBoardView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mContext = context;
        int keyUnit = ScreenUtils.getSearchKeyUnit();
        int padding = ScreenUtils.getSearchKeyPadding();
        int textSize = ScreenUtils.getKeyTextSize();
        
        this.setPadding(0, 0, 0, keyUnit / 3);
        this.setColumnCount(13);
        this.setOrientation(HORIZONTAL);

        for (int i = 0; i < KeyBoardData.KEYBOARD_VALUES.length; i++) {
            Button btn = new Button(context);
            GridLayout.LayoutParams btnParams = new GridLayout.LayoutParams();
            btnParams.setMargins(padding, padding, padding, padding);
            if (i >= 0 && i <= 11) {
                btnParams.topMargin = btnParams.topMargin * 2;
            }
            if (i == 0 || i == 12 || i == 25) {
                btnParams.leftMargin = btnParams.leftMargin * 2;
                int nextLeftId = keys_base_id + i + 12;
                if (i == 0) {
                    nextLeftId = nextLeftId - 1;
                }
                btn.setNextFocusLeftId(nextLeftId);
            }
            if (i == 11 || i == 24 || i == 37) {
                btnParams.rightMargin = btnParams.rightMargin * 2;
                int nextRightId = keys_base_id + i - 12;
                if (i == 11) {
                    nextRightId = nextRightId + 1;
                }
                btn.setNextFocusRightId(nextRightId);
            }
            if (i >= 25) {
                btnParams.bottomMargin = btnParams.bottomMargin * 2;
                int nextDownId = keys_base_id + i - 26;
                if (i <= 34) {
                    nextDownId = nextDownId + 1;
                }
                btn.setNextFocusDownId(nextDownId);
            }
            
            btnParams.width = keyUnit;
            btnParams.height = keyUnit;
            if(KeyBoardData.KEYBOARD_VALUES[i].equals("delete")) {
                btnParams.columnSpec = GridLayout.spec(9, 2);
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

            this.addView(btn);
        }
        aniKeysDown = AnimationUtils.loadAnimation(mContext, R.anim.search_keyboard_keys_down);
        aniKeysUp = AnimationUtils.loadAnimation(mContext, R.anim.search_keyboard_keys_up);
        aniKeysFocus = AnimationUtils.loadAnimation(mContext, R.anim.search_keyboard_keys_focused);
        aniKeysDown.setFillAfter(true);
        aniKeysUp.setFillAfter(true);
        aniKeysFocus.setFillAfter(true);
    }

    public SearchKeyBoardView(Context context) {
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
