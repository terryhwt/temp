package com.yf.mqtt.utils;

import android.content.Context;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public class Utils {
    private static final int BUFFER_SIZE = 4096;
    private static final String TAG = Constant.TAG_PREFIX + "Utils";

    private static Context sContext;

    public static void initContext(Context context) {
        context = context.getApplicationContext();
    }

    public static Context getContext() {
        return sContext;
    }

    public static byte[] inputStreamToByteArray(InputStream in) throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] data = new byte[BUFFER_SIZE];
        int count;
        while ((count = in.read(data, 0, BUFFER_SIZE)) != -1) {
            outStream.write(data, 0, count);
        }
        return outStream.toByteArray();
    }

    public static InputStream byteArrayToStream(byte[] datas) throws UnsupportedEncodingException {
        Log.d(TAG, "byteArrayToStream: dataStr = " + (new String(datas)));
        return new ByteArrayInputStream(datas);
    }

    public static void closeStream(Closeable stream) {
        if (null != stream) {
            try {
                stream.close();
            } catch (IOException e) {
                Log.e(TAG, "closeStream: exception ", e);
            }
        }
    }

    public static byte[] concatTag(byte[] tag, byte[] content) {
        byte[] outBytes = new byte[content.length + 1];
        System.arraycopy(tag, 0, outBytes, 0, 1);
        System.arraycopy(content, 0, outBytes, 1, content.length);
        return outBytes;
    }

    public static byte getTag(byte[] input) {
        return input[0];
    }

    public static byte[] getContent(byte[] input) {
        return Arrays.copyOfRange(input, 1, input.length);
    }
}
