package com.v3aqb.hxcrypto;

import android.view.View;

public class Util {
    public static void onViewClick(View view){
        if(view.isFocused()){
            view.clearFocus();
            view.requestFocus();
        }else{
            view.requestFocus();
            view.clearFocus();
        }
    }
}
