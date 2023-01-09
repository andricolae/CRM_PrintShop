package com.andreinicolae.app;
import java.io.*;
import java.util.Vector;

/**
 * @author Andrei Nicolae Căluțiu.
 * @version 1.0
 * The Util Class, for needed methods
 */
public class Util {
    /** Returns the Description of a Request
     * @param data the descriptions related to Requests for a specific client ID
     * @return a {@code String} value with the descriptions
     */
    public static String printDescription(Vector<String> data) {
        String result = "";
        if(data.size() == 0) {
            result = "No Requests";
            return result;
        }
        for (int i = 0; i < data.size(); i++)
            result +=  data.get(i);
        return result;
    }

    public static String convert(InputStream input) {
        String convertString = null;
        BufferedInputStream buffInput = new BufferedInputStream(input);
        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        int result;
        try {
            result = buffInput.read();
            while (result != -1) {
                byteOutput.write((byte) result);
                result = buffInput.read();
            }
            convertString = byteOutput.toString("UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return convertString;
    }
}
