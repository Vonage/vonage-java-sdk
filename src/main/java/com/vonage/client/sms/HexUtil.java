/*
 *   Copyright 2020 Vonage
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package com.vonage.client.sms;

import com.vonage.client.VonageUnexpectedException;

import java.io.UnsupportedEncodingException;


/**
 * Static helper methods for working with hex values.
 *
 *
 */
public class HexUtil {

    private static final char[] HEX_CHARS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', };

    private HexUtil() {
        // This class may not be instantiated.
    }

    /**
     * translate a byte array of raw data into a String with a hex representation of that data
     *
     * @param bytes raw binary data
     *
     * @return String Hex representation of the raw data
     */
    public static String bytesToHex(byte[] bytes) {
        return bytesToHex(bytes, null);
    }

    /**
     * translate a byte array of raw data into a String with a hex representation of that data.
     * Each octet will be separated with a specific separator.
     *
     * @param bytes raw binary data
     * @param separator This string will be injected into the output in between each octet in the stream
     *
     * @return String Hex representation of the raw data with each octet separated by 'separator'
     */
    public static String bytesToHex(byte[] bytes, String separator) {
        StringBuilder tmpBuffer = new StringBuilder();
        if (bytes != null) {
            for (byte c : bytes) {
                int b = c;
                if (b < 0)
                    b += 256;
                if (separator != null)
                    tmpBuffer.append(separator);
                tmpBuffer.append(HEX_CHARS[(b & 0xf0) / 0x10]); // note, this benchmarks faster than using >> 4
                tmpBuffer.append(HEX_CHARS[b & 0x0f]);
            }
        }
        return tmpBuffer.toString();
    }

    /**
     * Converts a Hex encoded String into a byte vector.
     *
     * @param str The String to be encoded.
     *
     * @return A byte vector representing the String.
     */
    public static byte[] hexToBytes(String str) {
        if (str == null)
            return null;
        byte[] hexChars;
        try {
            hexChars = str.toUpperCase().getBytes("ISO_8859-1");
        } catch (UnsupportedEncodingException e) {
            throw new VonageUnexpectedException("ISO_8859_1 is an unsupported encoding in this JVM");
        }
        int size = hexChars.length;
        byte[] bytes = new byte[size / 2];
        int first;
        int second;

        int rIndex = 0;
        // Convert to bytes.
        for (int i = 0; i+1 <size; i= i + 2) {

            // Convert first
            first = hexChars[i];
            if (first < 58)
                first = ((first - 48) * 16); // 0 - 9
            else
                first = ((first - 55) * 16); // A - F

            // Convert second
            second = hexChars[i + 1];
            if (second < 58)
                second = second - 48; // 0 - 9
            else
                second = second - 55; // A - F

            //Value must be between -128 and 127
            int total = (first + second);
            if (total > 127)
                total = (256 + total);

            bytes[rIndex] = (byte) total;
            rIndex++;
        }
        return bytes;
    }

}
