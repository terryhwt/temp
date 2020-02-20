package com.yf.mqtt.parser;

public class XmlParser implements Parser {

    @Override
    public Message getMessageInfo() {
        return new XmlMessage();
    }
}

