package com.yf.mqtt.bean;

public class MqttPublishType
{

    //getSendPayload byte[]
    public static final int PUBLISH_BYTE_ARRAY = 100;
    //getSendPayload String
    public static final int PUBLISH_STRING = 101;

    public static final int PUBLISH_JSON_OBJ = 102;
    public static final int PUBLISH_JSON_ARRAY= 103;

    public static final int PUBLISH_XML = 104;
    public static final int PUBLISH_FILE = 105;
}
