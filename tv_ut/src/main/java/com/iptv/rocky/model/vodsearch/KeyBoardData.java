package com.iptv.rocky.model.vodsearch;

import com.iptv.rocky.model.TvApplication;
import com.iptv.rocky.R;

public class KeyBoardData {
    public static final String[] KEYBOARD_VALUES = {
            "A","B","C","D","E","F","G","H","I","delete","0","1",
            "J","K","L","M","N","O","P","Q","R","2","3","4","5",
            "S","T","U","V","W","X","Y","Z"," ","6","7","8","9"
    };
    public static final String[] KEYBOARD_TEXTS = {
            "A","B","C","D","E","F","G","H","I",
            TvApplication.getInstance().getString(R.string.search_keyboard_delete),"0","1",
            "J","K","L","M","N","O","P","Q","R","2","3","4","5",
            "S","T","U","V","W","X","Y","Z",
            TvApplication.getInstance().getString(R.string.search_keyboard_blank),"6","7","8","9"
    };
}
