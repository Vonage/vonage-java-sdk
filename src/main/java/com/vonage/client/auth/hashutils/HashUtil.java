package com.vonage.client.auth.hashutils;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility methods for hashing strings.
 */
public class HashUtil {

    private static final HashUtil instance = new HashUtil();
    private Map<HashType, Hasher> hashTypes;

    private HashUtil() {
        hashTypes = new HashMap<>();
        hashTypes.put(HashType.MD5, new MD5Util());
        hashTypes.put(HashType.SHA256, new SHA256Util());
    }

    public static HashUtil getInstance() {
        return instance;
    }

    /**
     * Calculates hash for string. assume string is UTF-8 encoded.
     * @param input string which is going to be encoded into requested format
     * @param hashType The type of hash to be applied to the input string
     * @return representation of the input string with given hash type
     * @throws NoSuchAlgorithmException if the algorithm is not available.
     */
    public String calculate(String input, HashType hashType) throws NoSuchAlgorithmException {
        return hashTypes.get(hashType).calculate(input);
    }

    /**
     * Calculates hash for string.
     * @param input string which is going to be encoded into requested format
     * @param encoding character encoding of the string which is going to be encoded into requested format
     * @param hashType The type of hash to be applied to the input string
     * @return representation of the input string with given hash type
     * @throws NoSuchAlgorithmException if the algorithm is not available.
     * @throws UnsupportedEncodingException if the specified encoding is unavailable.
     */
    public String calculate(String input, String encoding, HashType hashType) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        return hashTypes.get(hashType).calculate(input, encoding);
    }

    public enum HashType {
        MD5,
        SHA256
    }
}
