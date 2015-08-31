package com.nexmo.common.util;

import java.io.UnsupportedEncodingException;
/*
 * Copyright (c) 2011-2013 Nexmo Inc
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

/**
 * HexUtil.java<br><br>
 *
 * Static helper methods for working with hex values<br><br>
 *
 * Created on 5 January 2011, 17:34
 *
 * @author  Paul Cook
 * @version 1.0
 */
public class HexUtil {

    private static final char[] HEX_CHARS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', };

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
     * @param separator This string will be injected into the output inbetween each octet in the stream
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
     * @param s The String to be encoded.
     *
     * @return A byte vector representing the String.
     */
    public static byte[] hexToBytes(String str) {
        if (str == null)
            return null;
        byte[] hexChars = null;
        try {
            hexChars = str.toUpperCase().getBytes("ISO_8859-1");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("SHOULD NOT HAPPEN  ISO_8859-1 UNSUPPORTED ENCODING!!");
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
