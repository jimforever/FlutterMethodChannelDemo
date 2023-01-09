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
                    String msg = getStringResult();
                    result.success(msg);
                }else if(call.method.equals("getListResult")){
                    List msg = getListResult();
                    result.success(msg);
                }else if(call.method.equals("getMapResult")){
                    Map<String,Object> msg = getMapResult();
                    result.success(msg);
                }else {
                    result.notImplemented();
                }
            }
        });
    }

    public String getStringResult(){
        return "HelloWorld";
    }

    public List getListResult(){
        List list = new ArrayList();
        list.add("Android");
        list.add("Ios");
        return list;
    }

    public Map<String,Object> getMapResult(){
        Map map = new HashMap();
        map.put("name","jim");
        map.put("age",15);
        return map;
    }
}
