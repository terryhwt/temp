package com.yf.mqtt;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Binder;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

import com.yf.mqtt.bean.MqttPublishType;
import com.yf.mqtt.bean.MqttResponse;
import com.yf.mqtt.parser.Parser;
import com.yf.mqtt.parser.StringParser;
import com.yf.mqtt.parser.XmlParser;
import com.yf.mqtt.utils.Constant;
import com.yf.mqtt.utils.Utils;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MqttService extends Service {
    private static final String TAG = Constant.TAG_PREFIX + "MQTTService";

    private MqttBinder mBinder = new MqttBinder();
    private static final String serverURL = "tcp://192.168.0.104:1883"; //address do not change later, only connect to yft server
    private static final String MQTT_LWT_MSG = "LAST WILL MESSAGE";

    private static final boolean AUTO_RECONNECT = false;
    private static final boolean CLEAN_SESSIONS = true;
    private static final int CON_TIME_OUT = 10;
    private static final int KEEP_ALIVE_INTERVAL = 20; //20s

    private IMqttCallBack mClientCallBack;
    private MqttAndroidClient mClient;
    private MqttConnectOptions mConnectOptions;
    private String mClientId = null;
    private int mPublishType = MqttPublishType.PUBLISH_BYTE_ARRAY;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: ");
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind: ");
        return mBinder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
        disconnect();
    }

    public void setClientCallBack(IMqttCallBack callBack) {
        this.mClientCallBack = callBack;
    }

    public void init(String clientId) {
        init(this.getApplicationContext(), serverURL, clientId);
    }

    public void disconnect() {
        if (null == mClient) {
            Log.d(TAG, "disconnect: null == mClient");
            return;
        }
        try {
            mClient.disconnect();
        } catch (MqttException e) {
            Log.e(TAG, "onDestroy: MqttException ", e);
        }
    }

    public void publish(String topic, String msg) {
        publish(topic, msg, MqttPublishType.PUBLISH_STRING);
    }

    public void publish(String topic, String msg, int type) {

        if (TextUtils.isEmpty(msg)) {
            Log.d(TAG, "publish: msg is empty, will return ");
            return;
        }

        mPublishType = type;
        Parser parser = null;
        switch (type) {
            case MqttPublishType.PUBLISH_XML:
                parser = new XmlParser();
                break;
            case MqttPublishType.PUBLISH_STRING:
                parser = new StringParser();
                break;
            default:
                break;
        }
        if (null != parser) {
            byte[] sendPayload = parser.getMessageInfo().getSendPayload(msg);
            InnerPublish(topic, sendPayload);
        }

    }

    public void subscribe(String topic, int qos) {
        try {
            if (null != mClient) {
                mClient.subscribe(topic, qos);
            }
        } catch (MqttException e) {
            if (null != mClientCallBack) {
                mClientCallBack.onSubscribeStatus(MqttResponse.SUBSCRIBE_FAILED);
            }
        }
    }

    private void init(Context context, String serverURI, String clientId) {
        Log.d(TAG, "init: serverURL = " + serverURI + ", clientId = " + clientId);
        Utils.initContext(context);
        this.mClientId = clientId;

        mClient = new MqttAndroidClient(context, serverURI, clientId);
        mClient.setCallback(mqttCallback);

        mConnectOptions = new MqttConnectOptions();
        mConnectOptions.setAutomaticReconnect(AUTO_RECONNECT);
        mConnectOptions.setCleanSession(CLEAN_SESSIONS);
        mConnectOptions.setConnectionTimeout(CON_TIME_OUT);
        mConnectOptions.setKeepAliveInterval(KEEP_ALIVE_INTERVAL);

        boolean doConnect = true;
        try {
            mConnectOptions.setWill(Constant.PUBLISH_LWT_TOPIC, MQTT_LWT_MSG.getBytes(), Constant.PUBLISH_QOS, false);
        } catch (IllegalArgumentException exception) {
            Log.e(TAG, "init: ", exception);
            doConnect = false;
        } finally {
            if (doConnect) {
                doClientConnection();
            }
        }
    }

    private void InnerPublish(String topic, byte[] msg) { // final interface
        try {
            if (mClient != null) {
                mClient.publish(topic, msg, Constant.PUBLISH_QOS, false);
                Log.d(TAG, "InnerPublish: successful");
            } else {
                Log.d(TAG, "InnerPublish: will not publish,  client == null");
            }
        } catch (MqttException e) {
            Log.e(TAG, "InnerPublish: exception : ", e);
        }
    }

    private void doClientConnection() {
        if (!mClient.isConnected() && isNetworkAvaliable()) {
            try {
                mClient.connect(mConnectOptions, null, connectListener);
            } catch (MqttException e) {
                Log.e(TAG, "doClientConnection: MqttException", e);
            }
        }
    }

    private boolean isNetworkAvaliable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (null != connectivityManager) {
            NetworkInfo info = connectivityManager.getActiveNetworkInfo();
            if (info != null && info.isAvailable()) {
                String name = info.getTypeName();
                return true;
            }
        }
        if (null != mClientCallBack) {
            mClientCallBack.onConnected(MqttResponse.NO_NETWORK);
        }
        return false;
    }

    private IMqttActionListener connectListener = new IMqttActionListener() { // attention memory leak

        @Override
        public void onSuccess(IMqttToken arg0) {
            Log.d(TAG, "connect mqtt server , success");
            if (null != mClientCallBack) {
                mClientCallBack.onConnected(MqttResponse.CONNECT_SUCCESS);
            }
        }

        @Override
        public void onFailure(IMqttToken arg0, Throwable arg1) {
            Log.d(TAG, "connect mqtt server , failed");
            if (null != mClientCallBack) {
                mClientCallBack.onConnected(MqttResponse.CONNECT_FAILED);
            }
        }
    };

    private MqttCallback mqttCallback = new MqttCallback() {

        @Override
        public void messageArrived(String topic, MqttMessage message) {
            //here do not return result to client directly , must has ParseStream process
            parseResult(message);
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken token) {
            Log.d(TAG, "from server, deliveryComplete: " + token.toString());
        }

        @Override
        public void connectionLost(Throwable arg0) {
            if (null != mClientCallBack) {
                mClientCallBack.connectionLost(arg0);
            }
        }
    };

    private void parseResult(MqttMessage msg) {
        Object results = null;
        switch (mPublishType) {
            case MqttPublishType.PUBLISH_STRING:
                //here results is  hashmap<String, String>
                results = new StringParser().getMessageInfo().parse(msg.getPayload());
                break;
            case MqttPublishType.PUBLISH_XML:
                results = new XmlParser().getMessageInfo().parse(msg.getPayload());
                break;
            default:
                break;
        }

        if (null != results && null != mClientCallBack) {
            mClientCallBack.onMessageArrived(results);
        }
    }

    public interface IMqttCallBack { //callback in mqttservice , will return return status to client

        void onConnected(int errCode);

        void onSubscribeStatus(int errCode);

        <T> void onMessageArrived(T result);

        void connectionLost(Throwable arg0);
    }

    public class MqttBinder extends Binder {
        public MqttService getService() {
            return MqttService.this;
        }
    }

    public static class Response {

        //connection status
        public static final int CONNECT_SUCCESS = 0;
        public static final int CONNECT_FAILED = 1;

        //getSendPayload status
        public static final int PUBLISH_SUCCESS = 2;
        public static final int PUBLISH_FAILED = 3;

        //subscribe status
        public static final int SUBSCRIBE_SUCCESS = 4;
        public static final int SUBSCRIBE_FAILED = 5;

        //network not available
        public static final int NO_NETWORK = 6;
    }

}
