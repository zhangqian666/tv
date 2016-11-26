package com.iptv.rocky.model.vodplay;

import com.iptv.rocky.model.TvApplication;
import com.iptv.rocky.R;

public class KeyBoardData {
    public static final String[] KEYBOARD_VALUES = {
            "1","2","3","4","5","6","7","8","9","0","OK","delete"
    };
    public static final String[] KEYBOARD_TEXTS = {
            "1","2","3","4","5","6","7","8","9","0",TvApplication.getInstance().getString(R.string.dialog_confirm),TvApplication.getInstance().getString(R.string.search_keyboard_delete)
    };
}
