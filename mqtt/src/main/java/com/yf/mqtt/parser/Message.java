package com.yf.mqtt.parser;

public interface Message {
    public <T> T parse(byte[] payload);

    public byte[] getSendPayload(String msg);
}
