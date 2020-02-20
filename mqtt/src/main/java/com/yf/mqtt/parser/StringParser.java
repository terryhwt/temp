package com.yf.mqtt.parser;

public class StringParser implements Parser {

    @Override
    public Message getMessageInfo() {
        return new StringMessage();
    }
}
