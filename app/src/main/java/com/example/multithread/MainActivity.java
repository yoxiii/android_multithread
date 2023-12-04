package com.example.multithread;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.multithread.R;

public class MainActivity extends AppCompatActivity {

    private TextView tvContent;
    private String strFromNet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvContent = findViewById(R.id.tv_content);


    }
    private Handler mHandler=new Handler(Looper.myLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if(msg.what==1){
                String strData=(String)msg.obj;
                tvContent.setText(strData);

            }
        }
    };
    Thread thread;
    public void start(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                strFromNet=getStringFromNet();
                Message message=new Message();
                message.what=1;
                message.obj=strFromNet;
                mHandler.sendMessage(message);
            }
        }).start();


        // 做一个耗时任务
        Toast.makeText(MainActivity.this,"任务完成！",Toast.LENGTH_SHORT).show();
//        String strFromNet = getStringFromNet();
    }


    private String getStringFromNet() {
        // 假装从网络获取了一个字符串
        String result = "";

        StringBuilder stringBuilder = new StringBuilder();

        // 模拟一个耗时操作
        for (int i = 0; i < 100; i++) {
            stringBuilder.append("字符串" + i);
        }

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        result = stringBuilder.toString();

        return result;
    }
}