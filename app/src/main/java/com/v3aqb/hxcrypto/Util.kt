package com.v3aqb.hxcrypto

import android.content.Context
import android.content.Intent
import android.view.View

object Util {
    fun setViewFocus(view: View) {
        if (view.isFocused) {
            view.clearFocus()
            view.requestFocus()
        } else {
            view.requestFocus()
            view.clearFocus()
        }
    }

    fun sendTextIntent(context: Context, text: String?): Intent {
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.putExtra(Intent.EXTRA_TEXT, text)
        intent.type = "text/plain"
        return Intent.createChooser(intent, context.getString(R.string.send_title))
    }
}