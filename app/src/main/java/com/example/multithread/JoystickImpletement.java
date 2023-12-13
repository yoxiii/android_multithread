package com.example.multithread;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Map;

public class JoystickImpletement extends AppCompatActivity {
    private TextView angleTextView;
    private TextView powerTextView;
    private TextView directionTextView;
    private SocketService.MyBinder mbinder;
    // Importing also other views
    private JoystickView joystick;
    private TextView tvContent;
    private String strFromNet;
    private Button btnConnect, btnDisconnect, btnSend;
    private EditText mEdit, ip_addr, port;
    BufferedReader br;

    // 接收服务器发送过来的消息
    String response;
    InputStream is;

    // 输入流读取器对象
    InputStreamReader isr;

    private TextView Receive, receive_message;
    private int port_intValue;
    static OutputStream outputStream;
    private boolean isConnected = false; // 连接状态标志位
    private boolean connetion2_flag = false;

    public JoystickImpletement() {
    }

    //private SocketService.MyBinder mbinder;
//    private ServiceConnection connection=new ServiceConnection() {
//        @Override
//        public void onServiceConnected(ComponentName name, IBinder service) {
//            mbinder=(SocketService.MyBinder)service;
//            Log.d("MainActivity", "Service connected. Attempting to connect to server...");
//            mbinder.sendData(1,1);
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName name) {
//
//        }
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joystick);
        angleTextView = (TextView) findViewById(R.id.angleTextView);
        powerTextView = (TextView) findViewById(R.id.powerTextView);
        directionTextView = (TextView) findViewById(R.id.directionTextView);
        //Referencing also other views
        joystick = (JoystickView) findViewById(R.id.joystickView);
        tvContent = findViewById(R.id.textView);
        //isConnected=findViewById(R.id.textView)
        btnConnect = (Button) findViewById(R.id.connect);
        //btnDisconnect = (Button) findViewById(R.id.disconnect);
        mEdit = (EditText) findViewById(R.id.edit);
        ip_addr = (EditText) findViewById(R.id.ip_addr);
        port = (EditText) findViewById(R.id.port);


        receive_message = (TextView) findViewById(R.id.receive_message);
        Map<String, Object> IPinfo = SaveIP.getIPInfo(this);
        String savedIP = (String) IPinfo.get("ip");
        String savedPortStr = String.valueOf(IPinfo.get("port"));
        ip_addr.setText(savedIP);
        port.setText(savedPortStr);


        //Event listener that always returns the variation of the angle in degrees, motion power in percentage and direction of movement
        joystick.setOnJoystickMoveListener(new JoystickView.OnJoystickMoveListener() {

            @Override
            public void onValueChanged(int angle, int power, int direction) {
                // TODO Auto-generated method stub
                angleTextView.setText(" " + String.valueOf(angle) + "°");
                powerTextView.setText(" " + String.valueOf(power) + "%");
                Log.d("joystick", "angle: " + angle + " power: " + power + " direction: " + direction);
                if (mbinder != null) {
                    mbinder.sendData(angle, power);
                }
                switch (direction) {
                    case JoystickView.FRONT:
                        directionTextView.setText(R.string.front_lab);
                        break;
                    case JoystickView.FRONT_RIGHT:
                        directionTextView.setText(R.string.front_right_lab);
                        break;
                    case JoystickView.RIGHT:
                        directionTextView.setText(R.string.right_lab);
                        break;
                    case JoystickView.RIGHT_BOTTOM:
                        directionTextView.setText(R.string.right_bottom_lab);
                        break;
                    case JoystickView.BOTTOM:
                        directionTextView.setText(R.string.bottom_lab);
                        break;
                    case JoystickView.BOTTOM_LEFT:
                        directionTextView.setText(R.string.bottom_left_lab);
                        break;
                    case JoystickView.LEFT:
                        directionTextView.setText(R.string.left_lab);
                        break;
                    case JoystickView.LEFT_FRONT:
                        directionTextView.setText(R.string.left_front_lab);
                        break;
                    default:
                        directionTextView.setText(R.string.center_lab);
                }
            }
        }, JoystickView.DEFAULT_LOOP_INTERVAL);
    }

    public void send(View view) {
        Intent intent = new Intent(this, SocketService.class);
        bindService(intent, connection, BIND_AUTO_CREATE);
        //Log.d("MainActivity", "Service connected. Attempting to send...");
    }

    private ServiceConnection connection = new ServiceConnection() {//连接
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mbinder = (SocketService.MyBinder) service;
            Log.d("MainActivity", "Service connected. Attempting to connect to server...");
            mbinder.connect(ip_addr.getText().toString(), Integer.parseInt(port.getText().toString()));
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    private ServiceConnection connection1 = new ServiceConnection() {//发送数据
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mbinder = (SocketService.MyBinder) service;
            Log.d("MainActivity", "Service connected. Attempting to send...");
            mbinder.sendData(1, 1);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    private ServiceConnection connection2=new ServiceConnection() {//断开连接
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mbinder=(SocketService.MyBinder)service;
            Log.d("MainActivity", "Service connected. Attempting to disconnect...");
            mbinder.disconnect();
            connetion2_flag=true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            connetion2_flag=false;
        }
    };


    public void connect1(View view) {  //连接
        Intent intent = new Intent(this, SocketService.class);
        Log.d("MainActivity", String.valueOf(isConnected));
//        intent.putExtra("ip", ip_addr.getText().toString());
//        intent.putExtra("port", Integer.parseInt(port.getText().toString()));
        if(mbinder.isConnected()==false){
        bindService(intent, connection, BIND_AUTO_CREATE);
        bindService(intent, connection1, BIND_AUTO_CREATE);
            Log.d("MainActivity2", String.valueOf(isConnected));

            btnConnect.setText("DISCONNECT");}
        //Intent intent = new Intent(this, SocketService.class);
        if(mbinder.isConnected()){
        bindService(intent, connection2, BIND_AUTO_CREATE);
//
            btnConnect.setText("CONNECT");}
}

public void connect(View view) {  //连接
    Intent intent = new Intent(this, SocketService.class);
    Log.d("MainActivity", String.valueOf(isConnected));
    if(mbinder == null || !mbinder.isConnected()){
        if(connetion2_flag==true)
        unbindService(connection2);
        bindService(intent, connection, BIND_AUTO_CREATE);

        //if(mbinder.isConnected())
        btnConnect.setText("DISCONNECT");
    } else {

        bindService(intent, connection2, BIND_AUTO_CREATE);
        unbindService(connection);
        //unbindService(connection2);
        btnConnect.setText("CONNECT");
    }
}

}