package com.yf.mqtt.parser;

import android.content.res.AssetManager;
import android.util.Log;
import android.util.Xml;

import com.yf.mqtt.utils.Constant;
import com.yf.mqtt.utils.Utils;

import org.xmlpull.v1.XmlPullParser;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class XmlMessage implements Message {
    private static final String TAG = Constant.TAG_PREFIX + "XmlMessage";
    private static final String MY_RECORDS = "myrecords";

    @Override
    public HashMap<String, String> parse(byte[] payload) {
        //here means will get xml msg from broker , ParseStream xml , return Hashmap<String, String> e.g: key,value : (faceID, xxxx)
        return InnerParse(payload);
    }

    @Override
    public byte[] getSendPayload(String filePath) { // here get the xml file path , and
        return getBytesFromFile(filePath);
    }

    private byte[] getBytesFromFile(String filePath) {
        InputStream inputStream = readFile(filePath);

        byte[] bytes = null;
        try {
            bytes = Utils.inputStreamToByteArray(inputStream);
            Log.d(TAG, "getBytesFromFile: bytes.length = " + bytes.length);
        } catch (IOException e) {
            Log.e(TAG, "getBytesFromFile: getbytes exception , return ", e);
        } finally {
            Utils.closeStream(inputStream);
        }
        return bytes;
    }

    private HashMap<String, String> InnerParse(byte[] content) {
        Log.d(TAG, "processXmlParser2: ");
        InputStream inputStream = null;
        HashMap<String, String> detailInfos = null;
        String receivedStr = null;
        StringBuilder mStringBuilder = new StringBuilder();
        try {
            inputStream = Utils.byteArrayToStream(content);
            detailInfos = ParseStream(inputStream);
            Log.d(TAG, "messageArrived: receivedStr = " + receivedStr);
        } catch (Exception e) {
            Log.e(TAG, "messageArrived: get inputStream  exception", e);
        } finally {
            Utils.closeStream(inputStream);
        }
        return detailInfos;
    }

    private HashMap<String, String> ParseStream(InputStream inputStream) throws Exception {
        Log.d(TAG, "ParseStream: ");
        if (null == inputStream) {
            Log.d(TAG, "ParseStream: instream == null, will return !");
            return null;
        }
        HashMap<String, String> xmlResultMap = new HashMap<>();
        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(inputStream, "utf-8");
        int eventType = parser.getEventType();
        boolean isValidTag = false;
        while (eventType != XmlPullParser.END_DOCUMENT) {
            String nodeName = parser.getName();
            String text = parser.getText();
            Log.d(TAG, "ParseStream:eventType = " + eventType + ", nodeName = " + nodeName + ", text = ");
            switch (eventType) {
                case (XmlPullParser.START_DOCUMENT): //0
                    //do nothing
                    break;
                case (XmlPullParser.START_TAG): //2
                    String tagName = parser.getName();
                    if (MY_RECORDS.equals(tagName)) {
                        isValidTag = true;
                        break;
                    }

                    if (isValidTag) {
                        Object value = parser.nextText();
                        Log.d(TAG, "ParseStream: tagName = " + tagName + ", value = " + value);
                        xmlResultMap.put(tagName, (String) value);
                    }
                    break;
                case (XmlPullParser.END_TAG):
                    //do nothing
                    break;
            }
            eventType = parser.next();
            Log.d(TAG, "ParseStream: eventType = " + eventType);
        }
        return xmlResultMap;
    }


    private InputStream readFile(String file) {
        AssetManager assets = Utils.getContext().getResources().getAssets();
        InputStream inputStream = null;
        Log.d(TAG, "readFile : filename = " + file);
        try {
            inputStream = assets.open(file);
            Log.d(TAG, "run: open file successful");
        } catch (IOException e) {
            Log.e(TAG, "open file exception ", e);
        }
        return inputStream;
    }
}
