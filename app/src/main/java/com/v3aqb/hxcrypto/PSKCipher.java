package com.v3aqb.hxcrypto;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;

public class PSKCipher {

    public static String encrypt(String key, String data) {
        Python py = Python.getInstance();
        PyObject hxcrypto = py.getModule("hxcrypto.util");
        PyObject key_in_bytes = hxcrypto.callAttr("key_to_bytes", key);
        return encrypt(key_in_bytes, data);
    }
    public static String encrypt(PyObject key, String data) {
        Python py = Python.getInstance();
        PyObject hxcrypto = py.getModule("hxcrypto.util");
        return  hxcrypto.callAttr("encrypt", key, data).toString();
    }
    public static String decrypt(String key, String data) {
        Python py = Python.getInstance();
        PyObject hxcrypto = py.getModule("hxcrypto.util");
        PyObject key_in_bytes = hxcrypto.callAttr("key_to_bytes", key);
        return decrypt(key_in_bytes, data);
    }
    public static String decrypt(PyObject key, String data) {
        Python py = Python.getInstance();
        PyObject hxcrypto = py.getModule("hxcrypto.util");
        PyObject result = hxcrypto.callAttr("decrypt", key, data);
        return result.toString();
    }
}
