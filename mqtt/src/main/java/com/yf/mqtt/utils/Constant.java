package com.yf.mqtt.utils;

public class Constant {
    public static final String TAG_PREFIX = "MQTT_";
    public static final byte TAG_NORMAL = 0x01;
    public static final byte TAG_INFO_XML = 0x03;
    public static final byte TAG_DYN_XML = 0x04;
    public static final byte TAG_ENV_XML = 0x05;
    public static final byte TAG_VEHICLE_XML = 0x06;
    public static final byte TAG_USER = 0x07;
    public static final byte TAG_NEW_KEY = 0x08;
    public static final byte TAG_PHONE_REGISTER_XML = 0x09;
    public static final byte TAG_PHONE_QUERY_XML = 0x0a;
    public static final byte TAG_PHONE_UPDATE_XML = 0x0b;
    public static final byte TAG_PHONE_DELETE_XML = 0x0c;


    public static final String ACCOUNT_INFO_XML_FILE = "account_info.xml";
    public static final String ACCOUNT_DYNAMIC_XML_FILE = "account_dynamic_info.xml";
    public static final String ACCOUNT_CLIENT_REQUEST_FILE = "client_request.xml";
    public static final String ACCOUNT_RECORD_SAVETO_DB_FILE = "record_saveto_db.xml";
    public static final String ACCOUNT_ENV_INFO_XML_FILE = "account_environment.xml";
    public static final String ACCOUNT_VEHICLE_XML_FILE = "account_vehicle.xml";
    public static final String ACCOUNT_RECORD_TO_DB_NEW_KEY_XML_FILE = "record_saveto_db2.xml";

    // phone xml
    public static final String PHONE_REGISTER_XML_FILE = "phone_request_info.xml";
    public static final String PHONE_QUERY_XML_FILE = "phone_request_info.xml";
    public static final String PHONE_UPDATE_XML_FILE = "phone_request_info2.xml";
    public static final String PHONE_DELETE_XML_FILE = "phone_request_info.xml";

    public static final String PHONE_TOPIC_PREFIX = "data_from_phone/command/";
    public static final String PHONE_REGISTER_TOPIC = PHONE_TOPIC_PREFIX + "register";
    public static final String PHONE_QUERY_TOPIC = PHONE_TOPIC_PREFIX + "query";
    public static final String PHONE_UPDATE_TOPIC = PHONE_TOPIC_PREFIX + "update";
    public static final String PHONE_DELETE_TOPIC = PHONE_TOPIC_PREFIX + "delete";


    //getSendPayload topic
    private static final String PUBLISH_PERIODIC_PREFIX = "data_collect/periodic/";
    private static final String PUBLISH_EVENTS_PREFIX = "data_collect/events/";

    public static final String PUBLISH_PERIODIC_BODY_INFO_TOPIC = PUBLISH_PERIODIC_PREFIX + "body_info";
    public static final String PUBLISH_PERIODIC_DYNAMIC_TOPIC = PUBLISH_PERIODIC_PREFIX + "dynamic";
    public static final String PUBLISH_PERIODIC_ENV_TOPIC = PUBLISH_PERIODIC_PREFIX + "environment";
    public static final String PUBLISH_PERIODIC_CLIENT_REQUEST_TOPIC = PUBLISH_PERIODIC_PREFIX + "client_request";
    public static final String PUBLISH_PERIODIC_RECORD_SAVE_DB_TOPIC = PUBLISH_PERIODIC_PREFIX + "record_saveto_db";
    public static final String PUBLISH_EVENTS_VEHICLE_TOPIC = PUBLISH_EVENTS_PREFIX + "vehicle";
    public static final String PUBLISH_LWT_TOPIC = "data_collect/lwt";

    public static final int PUBLISH_QOS = 2;

    public static final String PUBLISH_USER_TOPIC = "yft_user";

    //subscribe topic
    private static final String SUBSCRIBE_EVENTS_PREFIX = "data_from_server/events/";

    public static final String SUBSCRIBE_EVENTS_VEHICLE_TOPIC = SUBSCRIBE_EVENTS_PREFIX + "vehicle";

    public static final int SUBSCRIBE_QOS = 1;
}
