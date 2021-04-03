package com.v3aqb.hxcrypto

import com.chaquo.python.PyObject
import com.chaquo.python.Python

object PSKCipher {
    fun encrypt(key: String?, data: String?): String {
        val py = Python.getInstance()
        val hxcrypto = py.getModule("hxcrypto.util")
        val key_in_bytes = hxcrypto.callAttr("key_to_bytes", key)
        return encrypt(key_in_bytes, data)
    }

    fun encrypt(key: PyObject?, data: String?): String {
        val py = Python.getInstance()
        val hxcrypto = py.getModule("hxcrypto.util")
        return hxcrypto.callAttr("encrypt", key, data).toString()
    }

    fun decrypt(key: String?, data: String?): String {
        val py = Python.getInstance()
        val hxcrypto = py.getModule("hxcrypto.util")
        val key_in_bytes = hxcrypto.callAttr("key_to_bytes", key)
        return decrypt(key_in_bytes, data)
    }

    fun decrypt(key: PyObject?, data: String?): String {
        val py = Python.getInstance()
        val hxcrypto = py.getModule("hxcrypto.util")
        val result = hxcrypto.callAttr("decrypt", key, data)
        return result.toString()
    }
}