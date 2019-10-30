package com.v3aqb.hxcrypto;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.chaquo.python.PyException;

public class pskActivity extends AppCompatActivity {

    private long last_active = System.currentTimeMillis();
    private final String TAG = "pskActivity";
    private EditText workingText;

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
    }
    public void encrypt(View view) {
        if (System.currentTimeMillis() - last_active < 200) return;
        // read PSK
        EditText pskKey = (EditText)findViewById(R.id.pskKey);
        String psk = pskKey.getText().toString();

        // read plain text
        String plainText = workingText.getText().toString();

        if (plainText.length() == 0) return;

        String cipherText = PSKCipher.encrypt(psk, plainText);

        workingText.setText(cipherText);
        last_active = System.currentTimeMillis();
    }
    public void decrypt(View view) {
        if (System.currentTimeMillis() - last_active < 200) return;
        // read PSK
        EditText pskKey = (EditText)findViewById(R.id.pskKey);
        String psk = pskKey.getText().toString();

        // read cipher text
        String cipherText = workingText.getText().toString();

        if (cipherText.length() == 0) return;

        try {
            String plainText = PSKCipher.decrypt(psk, cipherText);
            if (plainText.length() == 0) {
                Toast.makeText(this, "decrypt failed", Toast.LENGTH_SHORT).show();
                return;
            } else workingText.setText(plainText);
        } catch (PyException err) {
            Toast.makeText(this, err.getMessage(), Toast.LENGTH_SHORT).show();
            return;
        }
        last_active = System.currentTimeMillis();
    }
}
