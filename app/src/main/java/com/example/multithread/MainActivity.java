package com.example.multithread;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.multithread.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


    private Socket socket;
    private TextView tvContent;
    private String strFromNet;
    private Button btnConnect, btnDisconnect, btnSend;
    private EditText mEdit,ip_addr,port;
    BufferedReader br ;

    // 接收服务器发送过来的消息
    String response;
    InputStream is;

    // 输入流读取器对象
    InputStreamReader isr ;

    private TextView Receive,receive_message;
    private int port_intValue;
    OutputStream outputStream;
    private boolean isConnected = false; // 连接状态标志位
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide(); // 隐藏 ActionBar
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout);

        tvContent = findViewById(R.id.textView);
        //isConnected=findViewById(R.id.textView)
        btnConnect = (Button) findViewById(R.id.connect);
        btnDisconnect = (Button) findViewById(R.id.disconnect);
        mEdit = (EditText) findViewById(R.id.edit);
        ip_addr = (EditText) findViewById(R.id.ip_addr);
        port = (EditText) findViewById(R.id.port);


        receive_message = (TextView) findViewById(R.id.receive_message);
        Map<String,Object> IPinfo=SaveIP.getIPInfo(this);
        String savedIP=(String)IPinfo.get("ip");
        String savedPortStr=String.valueOf(IPinfo.get("port"));
        ip_addr.setText(savedIP);
        port.setText(savedPortStr);
        Receive();
        detectConnection();
    }
    //handler负责接收子线程更新的信息
    private Handler mHandler=new Handler(Looper.myLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if(msg.what==0){
                //String strData=(String)msg.obj;
                //tvContent.setText(strData);
                receive_message.setText(response);
            }
        }
    };
    //因为4.0以后不能再主线程中进行网络操作,所以需要另外开辟一个线程
public void connect(View view) {  //连接
    new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                String port_text = port.getText().toString();
                try {
                    port_intValue = Integer.parseInt(port_text); //port转为int类型
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                socket = new Socket(ip_addr.getText().toString(), port_intValue);
                boolean isIPsaved=SaveIP.saveIPInfo(MainActivity.this,ip_addr.getText().toString(),port_intValue);
                System.out.println(isIPsaved);
                isConnected=true;

            } catch (IOException e) {
                e.printStackTrace();
                }
           // Toast.makeText(MainActivity.this,"任务完成！",Toast.LENGTH_SHORT).show();
        }
    }).start();

}
private void detectConnection(){
    new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                updateConnectionStatus();
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    });
}

    private void updateConnectionStatus() {
        if (isConnected) {
            tvContent.setText("Connected"); // connectionStatusTextView 为你的 TextView
        } else {
            tvContent.setText("Not Connected");
        }
    }
private void Receive()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true){
                        if(socket!=null&&socket.isConnected()) {
                            is = socket.getInputStream();
                            isr = new InputStreamReader(is);
                            br = new BufferedReader(isr);
                            response = br.readLine();
                            System.out.println(response);
                            if (response != null) {
                                Message msg = new Message();
                                msg.what = 0;
                                mHandler.sendMessage(msg);
                            }
                        }
                        Thread.sleep(100);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }


public void send(View view){
    new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                outputStream=socket.getOutputStream();
                outputStream.write((mEdit.getText().toString()+"\n").getBytes("utf-8"));
                //数据的结尾加上换行符才可让服务器端的readline()停止阻塞
                outputStream.flush();
            }catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }).start();
}

public void disconnect(View view){   //断开
        try {
            if (socket != null && socket.isConnected()) {
            socket.close();}
//            try{
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
            System.out.println("Output Shutdown: "+socket.isOutputShutdown());
            System.out.println("Input Shutdown: " +socket.isInputShutdown());
            //socket=null;
            isConnected=false;

        }catch (IOException e)
        {e.printStackTrace();}
}



//    public void start(View view) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                //strFromNet=getStringFromNet();
//                Message message=new Message();
//                message.what=1;
//                message.obj=strFromNet;
//                mHandler.sendMessage(message);
//            }
//        }).start();
//
//
//        // 做一个耗时任务
//        Toast.makeText(MainActivity.this,"任务完成！",Toast.LENGTH_SHORT).show();
////        String strFromNet = getStringFromNet();
//    }



//    private String getStringFromNet() {
//        // 假装从网络获取了一个字符串
//        String result = "";
//
//        StringBuilder stringBuilder = new StringBuilder();
//
//        // 模拟一个耗时操作
//        for (int i = 0; i < 100; i++) {
//            stringBuilder.append("字符串" + i);
//        }
//
//        try {
//            Thread.sleep(5000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        result = stringBuilder.toString();
//
//        return result;
//    }
}