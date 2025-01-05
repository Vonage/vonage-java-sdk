/*
 *   Copyright 2025 Vonage
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
package com.vonage.client.auth;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vonage.client.VonageUnexpectedException;
import com.vonage.client.auth.hashutils.HashUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.NameValuePair;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

/**
 * A helper class for generating or verifying MD5 signatures when signing REST requests for submission to Vonage.
 */
public class RequestSigning {
    public static final int MAX_ALLOWABLE_TIME_DELTA = 5 * 60 * 1000;

    public static final String PARAM_SIGNATURE = "sig";
    public static final String PARAM_TIMESTAMP = "timestamp";
    public static final String APPLICATION_JSON = "application/json";

    private static final Log log = LogFactory.getLog(RequestSigning.class);

    /**
     * Signs a set of request parameters.
     * <p>
     * Generates additional parameters to represent the timestamp and generated signature.
     * Uses the supplied pre-shared secret key to generate the signature.
     * Uses the default hash strategy of MD5.
     *
     * @param params List of NameValuePair instances containing the query parameters for the request that is to be signed
     * @param secretKey the pre-shared secret key held by the client
     *
     * @deprecated Use {@link #getSignatureForRequestParameters(Map, String, HashUtil.HashType)}.
     */
    @Deprecated
    public static void constructSignatureForRequestParameters(List<NameValuePair> params, String secretKey) {
        constructSignatureForRequestParameters(params, secretKey, HashUtil.HashType.HMAC_MD5);
    }

    /**
     * Signs a set of request parameters.
     * <p>
     * Generates additional parameters to represent the timestamp and generated signature.
     * Uses the supplied pre-shared secret key to generate the signature.
     *
     * @param params List of NameValuePair instances containing the query parameters for the request that is to be signed
     * @param secretKey the pre-shared secret key held by the client.
     * @param hashType The type of hash that is to be used in construction.
     * @deprecated Use {@link #constructSignatureForRequestParameters(Map, String, HashUtil.HashType)}.
     */
    @Deprecated
    public static void constructSignatureForRequestParameters(List<NameValuePair> params, String secretKey, HashUtil.HashType hashType) {
        Map<String, String> sortedParams = params.stream().collect(Collectors.toMap(
                NameValuePair::getName,
                NameValuePair::getValue,
                (v1, v2) -> v1,
                TreeMap::new
        ));
        constructSignatureForRequestParameters(sortedParams, secretKey, hashType);
    }

    /**
     * Signs a set of request parameters.
     * <p>
     * Generates additional parameters to represent the timestamp and generated signature.
     * Uses the supplied pre-shared secret key to generate the signature.
     * This method modifies the input params.
     *
     * @param params Query parameters for the request that is to be signed.
     * @param secretKey the pre-shared secret key held by the client.
     * @param hashType The type of hash that is to be used in construction.
     *
     * @deprecated Use {@link #getSignatureForRequestParameters(Map, String, HashUtil.HashType)}.
     */
    @Deprecated
    public static void constructSignatureForRequestParameters(Map<String, String> params, String secretKey, HashUtil.HashType hashType) {
        params.putAll(getSignatureForRequestParameters(params, secretKey, hashType));
    }

    /**
     * Signs a set of request parameters.
     * <p>
     * Generates additional parameters to represent the timestamp and generated signature.
     * Uses the supplied pre-shared secret key to generate the signature.
     * This method does not modify the input parameters.
     *
     * @param params Query parameters for the request that is to be signed.
     * @param secretKey the pre-shared secret key held by the client.
     * @param hashType The type of hash that is to be used in construction.
     *
     * @return A new Map with the signature query parameters.
     */
    public static Map<String, String> getSignatureForRequestParameters(Map<String, String> params, String secretKey, HashUtil.HashType hashType) {
        return constructSignatureForRequestParameters(params, secretKey, Instant.now().getEpochSecond(), hashType);
    }

    private static String clean(String str) {
        return str == null ? null : str.replaceAll("[=&]", "_");
    }

    static String generateParamsString(Map<String, String> params) {
        SortedMap<String, String> sortedParams = params instanceof SortedMap ?
                (SortedMap<String, String>) params : new TreeMap<>(params);

        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> param : sortedParams.entrySet()) {
            String name = param.getKey(), value = param.getValue();
            if (PARAM_SIGNATURE.equals(name) || value == null || value.trim().isEmpty()) {
                continue;
            }
            sb.append("&").append(clean(name)).append("=").append(clean(value));
        }
        return sb.toString();
    }

    /**
     * Signs a set of request parameters.
     * <p>
     * Generates additional parameters to represent the timestamp and generated signature.
     * Uses the supplied pre-shared secret key to generate the signature.
     *
     * @param inputParams Query parameters for the request that is to be signed.
     * @param secretKey the pre-shared secret key held by the client.
     * @param currentTimeSeconds the current time in seconds since 1970-01-01.
     * @param hashType Hash type to be used to construct request parameters.
     *
     */
    static Map<String, String> constructSignatureForRequestParameters(Map<String, String> inputParams,
                                                                 String secretKey,
                                                                 long currentTimeSeconds,
                                                                 HashUtil.HashType hashType) {

        // First, inject a 'timestamp=' parameter containing the current time in seconds since Jan 1st 1970
        String timestampStr = Long.toString(currentTimeSeconds);
        Map<String, String> tempParams = new TreeMap<>(inputParams);
        tempParams.put(PARAM_TIMESTAMP, timestampStr);

        String hashed, str = generateParamsString(tempParams);
        try {
            hashed = HashUtil.calculate(str, secretKey, "UTF-8", hashType);
        }
        catch (Exception ex) {
            log.error("error...", ex);
            hashed = "no signature";
        }

        log.debug("SECURITY-KEY-GENERATION -- String [ " + str + " ] Signature [ " + hashed + " ] ");

        Map<String, String> outputParams = new LinkedHashMap<>(4);
        outputParams.put(PARAM_TIMESTAMP, timestampStr);
        outputParams.put(PARAM_SIGNATURE, hashed);
        return outputParams;
    }

    /**
     * Verifies the signature in an HttpServletRequest. Hashing strategy is MD5.
     *
     * @param contentType The request Content-Type header.
     * @param inputStream The request data stream.
     * @param parameterMap The request parameters.
     * @param secretKey The pre-shared secret key used by the sender of the request to create the signature.
     *
     * @return true if the signature is correct for this request and secret key.
     *
     * @since 8.0.0
     */
    public static boolean verifyRequestSignature(InputStream inputStream,
                                                    String contentType,
                                                    Map<String, String[]> parameterMap,
                                                    String secretKey) {
        return verifyRequestSignature(contentType, inputStream, parameterMap, secretKey, System.currentTimeMillis());
    }

    /**
     * Verifies the signature in an HttpServletRequest. Hashing strategy is MD5.
     *
     * @param contentType The request Content-Type header.
     * @param inputStream The request data stream.
     * @param parameterMap The request parameters.
     * @param secretKey The pre-shared secret key used by the sender of the request to create the signature.
     * @param currentTimeMillis The current time, in milliseconds.
     *
     * @return true if the signature is correct for this request and secret key.
     */
     protected static boolean verifyRequestSignature(String contentType,
                                                     InputStream inputStream,
                                                     Map<String, String[]> parameterMap,
                                                     String secretKey,
                                                     long currentTimeMillis) {
        return verifyRequestSignature(contentType, inputStream, parameterMap,
                secretKey, currentTimeMillis, HashUtil.HashType.MD5
        );
    }

    /**
     * Verifies the signature in an HttpServletRequest.
     *
     * @param contentType The request Content-Type header.
     * @param inputStream The request data stream.
     * @param parameterMap The request parameters.
     * @param secretKey The pre-shared secret key used by the sender of the request to create the signature.
     * @param currentTimeMillis The current time, in milliseconds.
     * @param hashType Hash type to be used to construct request parameters.
     *
     * @return true if the signature is correct for this request and secret key.
     */
    static boolean verifyRequestSignature(String contentType,
                                                    InputStream inputStream,
                                                    Map<String, String[]> parameterMap,
                                                    String secretKey,
                                                    long currentTimeMillis,
                                                    HashUtil.HashType hashType) {

        // Construct a sorted list of the name-value pair parameters supplied in the request, excluding the signature parameter
        Map<String, String> sortedParams = new TreeMap<>();
        if (APPLICATION_JSON.equals(contentType) && inputStream != null) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                Map<String,String> params = mapper.readValue(inputStream, new TypeReference<Map<String,String>>(){});
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    String name = entry.getKey();
                    String value = entry.getValue();
                    log.info(name + " = " + value);
                    if (value == null || value.trim().isEmpty()) {
                        continue;
                    }
                    sortedParams.put(name, value);
                }
            }
            catch (IOException ex) {
                throw new VonageUnexpectedException("Unexpected issue when parsing JSON", ex);
            }
        }
        else {
            for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
                String name = entry.getKey();
                String value = entry.getValue()[0];
                log.info(name + " = " + value);
                if (value == null || value.trim().isEmpty()) {
                    continue;
                }
                sortedParams.put(name, value);
            }
        }

        // identify the signature supplied in the request ...
        String suppliedSignature = sortedParams.get(PARAM_SIGNATURE);
        if (suppliedSignature == null) return false;

        // Extract the timestamp parameter and verify that it is within 5 minutes of 'current time'
        String timeString = sortedParams.get(PARAM_TIMESTAMP);
        long time = -1;
        try {
            if (timeString != null)
                time = Long.parseLong(timeString) * 1000;
        } catch (NumberFormatException e) {
            log.error("Error parsing 'time' parameter [ " + timeString + " ]", e);
            time = 0;
        }
        long diff = currentTimeMillis - time;
        if (diff > MAX_ALLOWABLE_TIME_DELTA || diff < -MAX_ALLOWABLE_TIME_DELTA) {
            log.warn("SECURITY-KEY-VERIFICATION -- BAD-TIMESTAMP ... Timestamp [ " + time + " ] delta [ " + diff + " ] max allowed delta [ " + -MAX_ALLOWABLE_TIME_DELTA + " ] ");
            return false;
        }


        // Walk this sorted list of parameters and construct a string
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> param : sortedParams.entrySet()) {
            if (param.getKey().equals(PARAM_SIGNATURE)) continue;
            String name = param.getKey();
            String value = param.getValue();
            sb.append("&").append(clean(name)).append("=").append(clean(value));
        }

        String str = sb.toString();

        String hashed;
        try {
            hashed = HashUtil.calculate(str, secretKey, "UTF-8", hashType);
        } catch (Exception e) {
            log.error("error...", e);
            return false;
        }

        log.info("SECURITY-KEY-VERIFICATION -- String [ " + str + " ] Signature [ " + hashed + " ] SUPPLIED SIGNATURE [ " + suppliedSignature + " ] ");

        // verify that the supplied signature matches generated one
        // use MessageDigest.isEqual as an alternative to String.equals() to defend against timing based attacks
        return MessageDigest.isEqual(
                hashed.toLowerCase().getBytes(StandardCharsets.UTF_8),
                suppliedSignature.toLowerCase().getBytes(StandardCharsets.UTF_8)
        );
    }
}
