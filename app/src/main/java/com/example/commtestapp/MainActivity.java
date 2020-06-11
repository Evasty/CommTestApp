package com.example.commtestapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    private DiscoveryHandler mDiscoHan;//Solo

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDiscoHan = DiscoveryHandler.newInstance(this.getApplicationContext(), new DiscoveryHandler.callback() {
            @Override
            public void append(String str) {
                adder(str);
            }
        });
        findViewById(R.id.start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                esutarto();
            }
        });
        findViewById(R.id.stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endo();
            }
        });
    }

    public void adder(final String str){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((EditText)findViewById(R.id.METV)).append("\n"+str);
            }
        });
    }
    private void endo() {
        mDiscoHan.stopDiscovery();
    }

    private void esutarto() {
    mDiscoHan.discoverServices();
    }

}
