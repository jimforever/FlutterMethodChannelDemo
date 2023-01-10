package com.example.flutter_method_channel_demo;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.flutter.embedding.android.FlutterActivity;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;

public class MainActivity extends FlutterActivity {
    private String CHANNEL = "channel_demo";

    @Override
    public void configureFlutterEngine(@NonNull FlutterEngine flutterEngine) {
        super.configureFlutterEngine(flutterEngine);
        new MethodChannel(flutterEngine.getDartExecutor().getBinaryMessenger(),CHANNEL).setMethodCallHandler(new MethodChannel.MethodCallHandler() {
            @Override
            public void onMethodCall(@NonNull MethodCall call, @NonNull MethodChannel.Result result) {
                if(call.method.equals("getStringResult")){
                    String param = call.argument("msg");
                    String msg = getStringResult(param);
                    result.success(msg);
                }else if(call.method.equals("getListResult")){
                    List<String> params = call.argument("msg");
                    List<String> msg = getListResult(params);
                    result.success(msg);
                }else if(call.method.equals("getMapResult")){
                    Map<String,Object> params = call.argument("msg");
                    System.out.println("map params is "+params);
                    Map<String,Object> msg = getMapResult(params);
                    result.success(msg);
                }else {
                    result.notImplemented();
                }
            }
        });
    }

    public String getStringResult(String msg){
        return msg+"HelloWorld";
    }

    public List<String> getListResult(List<String> params){
        List<String> list = new ArrayList();
        list.add("Android");
        list.add("Ios");
        list.addAll(params);
        return list;
    }

    public Map<String,Object> getMapResult(Map<String,Object> params){
        Map<String,Object> map = new HashMap();
        map.put("name","jim");
        map.put("age",15);
        map.putAll(params);
        return map;
    }
}
