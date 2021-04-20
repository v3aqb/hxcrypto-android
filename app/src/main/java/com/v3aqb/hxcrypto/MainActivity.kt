package com.v3aqb.hxcrypto

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initPython()
    }

    fun openPSK(view: View?) {
        startActivity(Intent(this, PSKActivity::class.java))
    }

    fun openECDH(view: View?) {
        startActivity(Intent(this, ECDHActivity::class.java))
    }

    fun initPython() {
        if (!Python.isStarted()) {
            Python.start(AndroidPlatform(this))
        }
    }
}