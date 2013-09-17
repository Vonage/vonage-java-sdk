package com.nexmo.common.util;

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
            for (int i = 0; i < bytes.length; i++) {
                int b = bytes[i];
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

}
