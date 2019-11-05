package com.v3aqb.hxcrypto;

import android.content.Context;
import android.content.Intent;
import android.view.View;

public class Util {
    public static void setViewFocus(View view){
        if(view.isFocused()){
            view.clearFocus();
            view.requestFocus();
        }else{
            view.requestFocus();
            view.clearFocus();
        }
    }
    public static Intent sendTextIntent(Context context, String text) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, text);
        intent.setType("text/plain");
        return Intent.createChooser(intent, context.getString(R.string.send_title));
    }
}
