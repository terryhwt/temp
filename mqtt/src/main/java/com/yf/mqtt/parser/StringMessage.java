package com.yf.mqtt.parser;

public class StringMessage implements Message {

    @Override
    public String parse(byte[] payload) {
        return new String(payload);
    }

    @Override
    public byte[] getSendPayload(String msg) {
        return msg.getBytes();
    }
}
