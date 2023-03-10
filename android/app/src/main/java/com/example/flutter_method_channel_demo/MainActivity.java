package com.example.flutter_method_channel_demo;

import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.flutter.embedding.android.FlutterActivity;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.plugin.common.EventChannel;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;

public class MainActivity extends FlutterActivity {
    private String CHANNEL = "channel_demo";
    private String ANOTHERCHANNEL = "another_channel";
    private String STREAM = "stream_demo";
    final String TAG_NAME = "From_Native";
    private int count = 1;
    private Handler handler;
    private EventChannel.EventSink event;
    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            int TOTAL_COUNT = 100;
            if (count > TOTAL_COUNT) {
                event.endOfStream();
            } else {
                double percentage = ((double) count / TOTAL_COUNT);
                Log.w(TAG_NAME, "\nParsing From Native:  " + percentage);
                Map<String,Map<String,Object>> container = new HashMap<>();
                Map<String,Object> content = new HashMap<>();
                content.put("percentage",percentage);
                container.put("functionName",content);
                event.success(container);
            }
            count++;
            handler.postDelayed(this, 200);
        }
    };


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
        new MethodChannel(flutterEngine.getDartExecutor().getBinaryMessenger(),ANOTHERCHANNEL).setMethodCallHandler(new MethodChannel.MethodCallHandler() {
            @Override
            public void onMethodCall(@NonNull MethodCall call, @NonNull MethodChannel.Result result) {
                if(call.method.equals("getStringResult")){
                    String param = call.argument("msg");
                    String msg = param+" I am another channel";
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
        new EventChannel(flutterEngine.getDartExecutor().getBinaryMessenger(), STREAM).setStreamHandler(
                new EventChannel.StreamHandler() {
                    @Override
                    public void onListen(Object args, final EventChannel.EventSink events) {
                        Log.w(TAG_NAME, "Adding listener");
                        event = events;
                        count = 1;
                        handler = new Handler();
                        runnable.run();
                    }

                    @Override
                    public void onCancel(Object args) {
                        Log.w(TAG_NAME, "Cancelling listener");
                        handler.removeCallbacks(runnable);
                        handler = null;
                        count = 1;
                        event = null;
                        System.out.println("StreamHandler - onCanceled: ");
                    }
                }
        );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
        handler = null;
        event = null;
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
