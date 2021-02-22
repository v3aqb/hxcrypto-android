package com.v3aqb.hxcrypto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.chaquo.python.PyException;
import com.chaquo.python.PyObject;
import com.chaquo.python.Python;

import java.util.Objects;

public class ecdhActivity extends AppCompatActivity {

    private PyObject ecc;
    private EditText workingText;
    private PyObject skey;
    private String other_key;
    private long last_click = 0;
    private final String TAG = "ecdhActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ecdh);
        if (null == this.ecc) {
            resetPrivateKey();
        }
        workingText = (EditText)findViewById(R.id.ecworkingText);
        workingText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onViewClick(view);
            }
        });
    }
    public void resetPrivateKey(View view) {
        resetPrivateKey();
    }
    public void resetPrivateKey() {
        this.ecc = null;
        Python py = Python.getInstance();
        PyObject hxcrypto = py.getModule("hxcrypto");
        this.ecc = hxcrypto.callAttr("ECC", 256);
        String tag = this.ecc.callAttr("b64u_to_hash", get_pubkey_b64u()).toString();
        Button cp = (Button)findViewById(R.id.copyPubkeyButton);
        cp.setText(getResources().getString(R.string.copy_public_key, tag));
        if (null != other_key) {
            exchange(other_key);
        }
    }
    private PyObject get_pubkey_b64u(){
        return this.ecc.callAttr("get_pub_key_b64u");
    }
    private void exchange(String other_key) {
        this.skey = this.ecc.callAttr("get_dh_key_b64u", other_key);
    }
    public void copyPubKey(View view) {
        ClipboardManager clipboard = (ClipboardManager)
                getSystemService(Context.CLIPBOARD_SERVICE);
        String pubkey = get_pubkey_b64u().toString();
        ClipData clip = ClipData.newPlainText("simple text", pubkey);
        clipboard.setPrimaryClip(clip);
    }
    public void doExchange(View view) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        if (!(clipboard.hasPrimaryClip())) {
            Toast.makeText(this, R.string.clipboard_empty, Toast.LENGTH_SHORT).show();
            return;
        }

        ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);
        String temp = "";
        try {
            temp = item.getText().toString();
            exchange(temp);
        } catch (PyException err) {
            Toast.makeText(this, err.getMessage(), Toast.LENGTH_SHORT).show();
            return;
        }
        this.other_key = temp;
        String pubk_hash = this.ecc.callAttr("b64u_to_hash", this.other_key).toString();
        Button bu = (Button)view;
        bu.setText(pubk_hash);
        bu.setEnabled(false);
    }
    public void resetExchange(View view) {
        this.skey = null;
        this.other_key = null;
        Button bu = (Button)findViewById(R.id.doECDHButton);
        bu.setText(R.string.do_ecdh);
        bu.setEnabled(true);
    }
    public void encrypt(View view) {
        last_click = 0;
        if (null == this.skey) {
            Toast.makeText(this, "Key exchange required.", Toast.LENGTH_SHORT).show();
            return;
        }
        String data = workingText.getText().toString();
        if (data.length() == 0) return;

        String result = PSKCipher.encrypt(this.skey, data);
        workingText.setText(result);
    }
    public void decrypt(View view) {
        last_click = 0;
        if (null == this.skey) {
            Toast.makeText(this, "Key exchange required.", Toast.LENGTH_SHORT).show();
            return;
        }
        String data = workingText.getText().toString();
        if (data.length() == 0) return;

        try {
            String result = PSKCipher.decrypt(this.skey, data);
            if (result.length() == 0) {
                Toast.makeText(this, "decrypt failed", Toast.LENGTH_SHORT).show();
            } else workingText.setText(result);
        } catch (PyException err) {
            Toast.makeText(this, err.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    public void onSend(View view) {
        String data = workingText.getText().toString();
        if (data.length() == 0) return;
        startActivity(Util.sendTextIntent(this, data));
    }
    private void onViewClick(View view) {
        if (System.currentTimeMillis() - last_click > 5000) Util.setViewFocus(view);
        last_click = System.currentTimeMillis();
    }
}
