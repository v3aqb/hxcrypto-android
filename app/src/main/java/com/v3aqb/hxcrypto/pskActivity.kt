package com.v3aqb.hxcrypto

import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.chaquo.python.PyException

class pskActivity : AppCompatActivity() {
    private val TAG = "pskActivity"
    private var workingText: EditText? = null
    private var pskEdit: EditText? = null
    private var last_click: Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_psk)
        workingText = findViewById<View>(R.id.workingText) as EditText
        workingText!!.setOnClickListener { view -> onViewClick(view) }
        pskEdit = findViewById<View>(R.id.pskKey) as EditText
        // pskEdit.setTransformationMethod(new PasswordTransformationMethod());
    }

    fun encrypt(view: View?) {
        last_click = 0
        // read plain text
        val plainText = workingText!!.text.toString()
        if (plainText.length == 0) return
        val cipherText = PSKCipher.encrypt(pSK, plainText)
        workingText!!.setText(cipherText)
    }

    fun decrypt(view: View?) {
        last_click = 0
        // read cipher text
        val data = workingText!!.text.toString()
        if (data.length == 0) return
        try {
            val result = PSKCipher.decrypt(pSK, data)
            if (result!!.length == 0) {
                Toast.makeText(this, "decrypt failed", Toast.LENGTH_SHORT).show()
            } else workingText!!.setText(result)
        } catch (err: PyException) {
            Toast.makeText(this, err.message, Toast.LENGTH_SHORT).show()
        }
    }

    private val pSK: String
        private get() = pskEdit!!.text.toString()

    fun pskShowToggle(view: View?) {
        if (pSK.length == 0) return
        if (null == pskEdit!!.transformationMethod) {
            pskEdit!!.transformationMethod = PasswordTransformationMethod()
        } else {
            pskEdit!!.transformationMethod = null
        }
    }

    fun onSend(view: View?) {
        val data = workingText!!.text.toString()
        if (data.length == 0) return
        startActivity(Util.sendTextIntent(this, data))
    }

    private fun onViewClick(view: View) {
        if (System.currentTimeMillis() - last_click > 5000) Util.setViewFocus(view)
        last_click = System.currentTimeMillis()
    }
}