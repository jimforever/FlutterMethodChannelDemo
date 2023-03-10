import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      theme: ThemeData(
        // This is the theme of your application.
        //
        // Try running your application with "flutter run". You'll see the
        // application has a blue toolbar. Then, without quitting the app, try
        // changing the primarySwatch below to Colors.green and then invoke
        // "hot reload" (press "r" in the console where you ran "flutter run",
        // or simply save your changes to "hot reload" in a Flutter IDE).
        // Notice that the counter didn't reset back to zero; the application
        // is not restarted.
        primarySwatch: Colors.blue,
      ),
      home: const MyHomePage(title: 'Flutter Method Channel Demo'),
    );
  }
}

class MyHomePage extends StatefulWidget {
  const MyHomePage({super.key, required this.title});

  // This widget is the home page of your application. It is stateful, meaning
  // that it has a State object (defined below) that contains fields that affect
  // how it looks.

  // This class is the configuration for the state. It holds the values (in this
  // case the title) provided by the parent (in this case the App widget) and
  // used by the build method of the State. Fields in a Widget subclass are
  // always marked "final".

  final String title;

  @override
  State<MyHomePage> createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  static const platform = MethodChannel("channel_demo");
  static const anotherPlatform = MethodChannel("another_channel");
  static const stream = EventChannel("stream_demo");
  late StreamSubscription _streamSubscription;
  @override
  void initState() {
    // TODO: implement initState
    super.initState();
    _startListener();
  }

  void _startListener() {
    _streamSubscription = stream.receiveBroadcastStream().listen(_listenStream);
  }

  void _cancelListener() {
    _streamSubscription.cancel();
  }

  void _listenStream(value) {
    print("Received From Native:  $value\n");
  }

  Future<void> _getStringResult() async {
    try {
      final String result =
          await platform.invokeMethod('getStringResult', {"msg": "Jim"});
      print("_getStringResult is " + result);
    } on PlatformException catch (e) {
      print("Failed to _getStringResult: ${e.message}");
    }
  }

  Future<void> _getAnotherStringResult() async {
    try {
      final String result =
          await anotherPlatform.invokeMethod('getStringResult', {"msg": "Jim"});
      print("_getStringResult is " + result);
    } on PlatformException catch (e) {
      print("Failed to _getStringResult: ${e.message}");
    }
  }

  Future<void> _getListResult() async {
    try {
      final List result = await platform.invokeMethod('getListResult', {
        "msg": ["Python"]
      });
      print("_getlistResult is " + result.toString());
    } on PlatformException catch (e) {
      print("Failed to _getlistResult: ${e.message}");
    }
  }

  Future<void> _getMapResult() async {
    try {
      final Map result = await platform.invokeMethod('getMapResult', {
        "msg": {"hobby": "PingPang"}
      });
      print("_getMapResult is " + result.toString());
    } on PlatformException catch (e) {
      print("Failed to _getMapResult: ${e.message}");
    }
  }

  @override
  Widget build(BuildContext context) {
    // This method is rerun every time setState is called, for instance as done
    // by the _incrementCounter method above.
    //
    // The Flutter framework has been optimized to make rerunning build methods
    // fast, so that you can just rebuild anything that needs updating rather
    // than having to individually change instances of widgets.
    return Scaffold(
      appBar: AppBar(
        // Here we take the value from the MyHomePage object that was created by
        // the App.build method, and use it to set our appbar title.
        title: Text(widget.title),
      ),
      body: Center(
        // Center is a layout widget. It takes a single child and positions it
        // in the middle of the parent.
        child: Column(
          // Column is also a layout widget. It takes a list of children and
          // arranges them vertically. By default, it sizes itself to fit its
          // children horizontally, and tries to be as tall as its parent.
          //
          // Invoke "debug painting" (press "p" in the console, choose the
          // "Toggle Debug Paint" action from the Flutter Inspector in Android
          // Studio, or the "Toggle Debug Paint" command in Visual Studio Code)
          // to see the wireframe for each widget.
          //
          // Column has various properties to control how it sizes itself and
          // how it positions its children. Here we use mainAxisAlignment to
          // center the children vertically; the main axis here is the vertical
          // axis because Columns are vertical (the cross axis would be
          // horizontal).
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            TextButton(
                onPressed: () => _getStringResult(),
                child: Text("_getStringResult")),
            TextButton(
                onPressed: () => _getListResult(),
                child: Text("_getListResult")),
            TextButton(
                onPressed: () => _getMapResult(), child: Text("_getMapResult")),
            TextButton(
                onPressed: () => _getAnotherStringResult(),
                child: Text("_getAnotherPlatformStringResult"))
          ],
        ),
      ),
    );
  }
}
