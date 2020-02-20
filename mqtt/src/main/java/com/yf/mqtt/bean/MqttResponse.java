package com.yf.mqtt.bean;

public class MqttResponse {

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
