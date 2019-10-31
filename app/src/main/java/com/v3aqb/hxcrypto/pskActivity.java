package com.v3aqb.hxcrypto;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.chaquo.python.PyException;

public class pskActivity extends AppCompatActivity {

    private final String TAG = "pskActivity";
    private EditText workingText;
    private EditText pskEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_psk);
        workingText = (EditText)findViewById(R.id.workingText);
        workingText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.onViewClick(view);
            }
        });
        pskEdit = (EditText)findViewById(R.id.pskKey);
    }
    public void encrypt(View view) {
        // read plain text
        String plainText = workingText.getText().toString();

        if (plainText.length() == 0) return;

        String cipherText = PSKCipher.encrypt(this.getPSK(), plainText);

        workingText.setText(cipherText);
    }
    public void decrypt(View view) {
        // read cipher text
        String data = workingText.getText().toString();

        if (data.length() == 0) return;

        try {
            String result = PSKCipher.decrypt(this.getPSK(), data);
            if (result.length() == 0) {
                Toast.makeText(this, "decrypt failed", Toast.LENGTH_SHORT).show();
            } else workingText.setText(result);
        } catch (PyException err) {
            Toast.makeText(this, err.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    private String getPSK() {
        return this.pskEdit.getText().toString();
    }
    public void pskShowToggle(View view) {
        if (getPSK().length() == 0) return;
        if (null == this.pskEdit.getTransformationMethod()) {
            this.pskEdit.setTransformationMethod(new PasswordTransformationMethod());
        }else {
            this.pskEdit.setTransformationMethod(null);
        }
    }
    public void onSend(View view) {
        String data = workingText.getText().toString();
        if (data.length() == 0) return;
        startActivity(Util.sendTextIntent(this, data));
    }
}
