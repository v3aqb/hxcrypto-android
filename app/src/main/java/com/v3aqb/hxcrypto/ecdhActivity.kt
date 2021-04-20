package com.v3aqb.hxcrypto

import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.chaquo.python.PyException
import com.chaquo.python.PyObject
import com.chaquo.python.Python

class ECDHActivity : AppCompatActivity() {
    private var ecc: PyObject? = null
    private var workingText: EditText? = null
    private var skey: PyObject? = null
    private var otherKey: String? = null
    private var lastClick: Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ecdh)
        if (null == ecc) {
            resetPrivateKey()
        }
        workingText = findViewById<View>(R.id.ecworkingText) as EditText
        workingText!!.setOnClickListener { view -> onViewClick(view) }
    }

    fun resetPrivateKey(view: View?) {
        resetPrivateKey()
    }

    fun resetPrivateKey() {
        ecc = null
        val py = Python.getInstance()
        val hxcrypto = py.getModule("hxcrypto")
        ecc = hxcrypto.callAttr("ECC", 256)
        val tag = ecc!!.callAttr("b64u_to_hash", get_pubkey_b64u()).toString()
        val cp = findViewById<View>(R.id.copyPubkeyButton) as Button
        cp.text = resources.getString(R.string.copy_public_key, tag)
        if (null != otherKey) {
            exchange(otherKey!!)
        }
    }

    private fun get_pubkey_b64u(): PyObject {
        return ecc!!.callAttr("get_pub_key_b64u")
    }

    private fun exchange(other_key: String) {
        skey = ecc!!.callAttr("get_dh_key_b64u", other_key)
    }

    fun copyPubKey(view: View?) {
        val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val pubkey = get_pubkey_b64u().toString()
        val clip = ClipData.newPlainText("simple text", pubkey)
        clipboard.setPrimaryClip(clip)
    }

    fun doExchange(view: View) {
        val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        if (!clipboard.hasPrimaryClip()) {
            Toast.makeText(this, R.string.clipboard_empty, Toast.LENGTH_SHORT).show()
            return
        }
        val item = clipboard.primaryClip!!.getItemAt(0)
        var temp = ""
        try {
            temp = item.text.toString()
            exchange(temp)
        } catch (err: PyException) {
            Toast.makeText(this, err.message, Toast.LENGTH_SHORT).show()
            return
        }
        otherKey = temp
        val pubk_hash = ecc!!.callAttr("b64u_to_hash", otherKey).toString()
        val bu = view as Button
        bu.text = pubk_hash
        bu.isEnabled = false
    }

    fun resetExchange(view: View?) {
        skey = null
        otherKey = null
        val bu = findViewById<View>(R.id.doECDHButton) as Button
        bu.setText(R.string.do_ecdh)
        bu.isEnabled = true
    }

    fun encrypt(view: View?) {
        lastClick = 0
        if (null == skey) {
            Toast.makeText(this, "Key exchange required.", Toast.LENGTH_SHORT).show()
            return
        }
        val data = workingText!!.text.toString()
        if (data.isEmpty()) return
        val result = PSKCipher.encrypt(skey, data)
        workingText!!.setText(result)
    }

    fun decrypt(view: View?) {
        lastClick = 0
        if (null == skey) {
            Toast.makeText(this, "Key exchange required.", Toast.LENGTH_SHORT).show()
            return
        }
        val data = workingText!!.text.toString()
        if (data.isEmpty()) return
        try {
            val result = PSKCipher.decrypt(skey, data)
            if (result.length == 0) {
                Toast.makeText(this, "decrypt failed", Toast.LENGTH_SHORT).show()
            } else workingText!!.setText(result)
        } catch (err: PyException) {
            Toast.makeText(this, err.message, Toast.LENGTH_SHORT).show()
        }
    }

    fun onSend(view: View?) {
        val data = workingText!!.text.toString()
        if (data.isEmpty()) return
        startActivity(Util.sendTextIntent(this, data))
    }

    private fun onViewClick(view: View) {
        if (System.currentTimeMillis() - lastClick > 5000) Util.setViewFocus(view)
        lastClick = System.currentTimeMillis()
    }
}