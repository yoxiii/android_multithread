package com.example.multithread;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class SocketService extends Service {
    private static final String TAG = "MyBinder";
    public boolean isConnected = false; // 连接状态标志位
    private Socket socket;
    private OutputStream outputStream;
    //private final IBinder binder = new LocalBinder();

    public void test() {
        Log.d("SocketService", "test");
    }

    public class MyBinder extends Binder {
        public boolean isConnected() {
            return isConnected;
        }
        private static final String TAG = "MyBinder";
       private SocketService msocketService;
        public MyBinder(SocketService socketService) {
            msocketService=socketService;

        }
        public void test(){

            msocketService.test();
        }
        public void connect(String ip, int port) {
            msocketService.connect(ip,port);
            Log.d(TAG, "connect: ");
        }
        public void disconnect() {
            msocketService.disconnect();
            Log.d(TAG, "disconnect: ");
        }
        public void sendData(int angle, int power) {
    msocketService.sendData(angle,power);
    Log.d(TAG, "sendData: ");
        }

//        public void sendData() {
////            new Thread(() -> {
////                String message = "Angle: " + angle + ", Power: " + power;
////                try {
////                    outputStream.write(message.getBytes());
//                    System.out.println("sendData");
////                } catch (IOException e) {
////                    e.printStackTrace();
////                }
////            }).start();
//        }
        SocketService getService() {
            return SocketService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("SocketService", "onCreate");

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.d("SocketService", "onDestroy");
        super.onDestroy();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d("SocketService", "onUnbind");
        return super.onUnbind(intent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d("SocketService", "onBind");
        return new MyBinder(this);
    }

//    public void connect(String ip, int port) {
//        new Thread(() -> {
//            try {
//                socket = new Socket(ip, port);
//                outputStream = socket.getOutputStream();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }).start();
//    }
public void connect(String ip, int port) {  //连接
    //if (isConnected== false) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //String port_text = port.getText().toString();
                    try {
                       // port_intValue = Integer.parseInt(port_text); //port转为int类型
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                    socket = new Socket(ip, port);
                    boolean isIPsaved = SaveIP.saveIPInfo(SocketService.this, ip.toString(), port);
                    System.out.println(isIPsaved);
                    isConnected = true;
                    Log.d("SocketService", "isConnected: "+isConnected);



                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d("SocketService", "isConnected: "+isConnected+ip+port);
                }
                // Toast.makeText(MainActivity.this,"任务完成！",Toast.LENGTH_SHORT).show();
            }

        }).start();
    //}
//    else{
//        try {
//            if (socket != null && socket.isConnected()) {
//                socket.close();}
////            try{
////                Thread.sleep(1000);
////            } catch (InterruptedException e) {
////                throw new RuntimeException(e);
////            }
//            System.out.println("Output Shutdown: "+socket.isOutputShutdown());
//            System.out.println("Input Shutdown: " +socket.isInputShutdown());
//            //socket=null;
//            isConnected=false;
//
//        }catch (IOException e)
//        {e.printStackTrace();}
//    }
}
    public void disconnect() {
        try {
                if (socket != null && socket.isConnected()) {
                    socket.close();
                    isConnected=false;

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendData(int angle, int power) {
        Log.d(TAG, "sendData: ");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {Thread.sleep(60);
                    String message = "Angle: " + angle + ", Power: " + power;
                    if (socket != null) {
                    outputStream=socket.getOutputStream();
                    outputStream.write((message+"\n").getBytes());
                    //数据的结尾加上换行符才可让服务器端的readline()停止阻塞
                    outputStream.flush();
                    Log.d(TAG, "sendData: "+message);}
                }catch (IOException e)
                {
                    e.printStackTrace();
                    Log.d(TAG, "e: "+e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

}