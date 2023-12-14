package com.example.multithread;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ListView extends AppCompatActivity {
    private Button remoteControl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide(); // 隐藏 ActionBar
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview2);
    }
    public void remoteControl(View view){
        Intent intent = new Intent(this, JoystickImpletement.class);
        startActivity(intent);
    }
}