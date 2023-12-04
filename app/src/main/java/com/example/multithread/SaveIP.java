package com.example.multithread;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;

public class SaveIP {
    public static boolean saveIPInfo(Context context, String ip, int port) {
    SharedPreferences sp =context.getSharedPreferences("data",Context.MODE_PRIVATE);
    SharedPreferences.Editor edit=sp.edit();
    edit.putInt("port",port);
    edit.putString("ip",ip);

    edit.apply();
    return true;
    }
    public static Map<String,Object> getIPInfo(Context context){
        SharedPreferences sp=context.getSharedPreferences("data",Context.MODE_PRIVATE);
        String ip_addr=sp.getString("ip","");
        int port=sp.getInt("port",3333);
        Map<String,Object> IPinfoMap=new HashMap<>();
        IPinfoMap.put("ip",ip_addr);
        IPinfoMap.put("port",port);
        return IPinfoMap;
    }
}
