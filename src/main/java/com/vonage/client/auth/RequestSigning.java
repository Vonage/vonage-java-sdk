/*
 * Copyright (c) 2020 Vonage
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
package com.vonage.client.auth;


import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import com.vonage.client.VonageUnexpectedException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

/**
 * A helper class for generating or verifying MD5 signatures when signing REST requests for submission to Vonage.
 *
 * @author  Paul Cook
 */
public class RequestSigning {
    public static final int MAX_ALLOWABLE_TIME_DELTA = 5 * 60 * 1000;

    public static final String PARAM_SIGNATURE = "sig";
    public static final String PARAM_TIMESTAMP = "timestamp";

    private static Log log = LogFactory.getLog(RequestSigning.class);

    /**
     * Signs a set of request parameters.
     * <p>
     * Generates additional parameters to represent the timestamp and generated signature.
     * Uses the supplied pre-shared secret key to generate the signature.
     *
     * @param params List of NameValuePair instances containing the query parameters for the request that is to be signed
     * @param secretKey the pre-shared secret key held by the client
     *
     */
    public static void constructSignatureForRequestParameters(List<NameValuePair> params, String secretKey) {
        constructSignatureForRequestParameters(params, secretKey, System.currentTimeMillis() / 1000);
    }

    /**
     * Signs a set of request parameters.
     * <p>
     * Generates additional parameters to represent the timestamp and generated signature.
     * Uses the supplied pre-shared secret key to generate the signature.
     *
     * @param params List of NameValuePair instances containing the query parameters for the request that is to be signed
     * @param secretKey the pre-shared secret key held by the client
     * @param currentTimeSeconds the current time in seconds since 1970-01-01
     *
     */
     protected static void constructSignatureForRequestParameters(
            List<NameValuePair> params, String secretKey, long currentTimeSeconds) {
        // First, inject a 'timestamp=' parameter containing the current time in seconds since Jan 1st 1970
        params.add(new BasicNameValuePair(PARAM_TIMESTAMP, Long.toString(currentTimeSeconds)));

        Map<String, String> sortedParams = new TreeMap<>();
        for (NameValuePair param: params) {
            String name = param.getName();
            String value = param.getValue();
            if (name.equals(PARAM_SIGNATURE))
                continue;
            if (value == null)
                value = "";
            if (!value.trim().equals(""))
                sortedParams.put(name, value);
        }

        // Now, walk through the sorted list of parameters and construct a string
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> param: sortedParams.entrySet()) {
            String name = param.getKey();
            String value = param.getValue();
            sb.append("&").append(clean(name)).append("=").append(clean(value));
        }

        // Now, append the secret key, and calculate an MD5 signature of the resultant string
        sb.append(secretKey);

        String str = sb.toString();

        String md5 = "no signature";
        try {
            md5 = MD5Util.calculateMd5(str);
        } catch (Exception e) {
            log.error("error...", e);
        }

        log.debug("SECURITY-KEY-GENERATION -- String [ " + str + " ] Signature [ " + md5 + " ] ");

        params.add(new BasicNameValuePair(PARAM_SIGNATURE, md5));
    }

    /**
     * Verifies the signature in an HttpServletRequest.
     *
     * @param request The HttpServletRequest to be verified
     * @param secretKey The pre-shared secret key used by the sender of the request to create the signature
     *
     * @return true if the signature is correct for this request and secret key.
     */
    public static boolean verifyRequestSignature(HttpServletRequest request, String secretKey) {
        return verifyRequestSignature(request, secretKey, System.currentTimeMillis());
    }

    /**
     * Verifies the signature in an HttpServletRequest.
     *
     * @param request The HttpServletRequest to be verified
     * @param secretKey The pre-shared secret key used by the sender of the request to create the signature
     * @param currentTimeMillis The current time, in milliseconds.
     *
     * @return true if the signature is correct for this request and secret key.
     */
     protected static boolean verifyRequestSignature(HttpServletRequest request,
                                                     String secretKey,
                                                     long currentTimeMillis) {
        // identify the signature supplied in the request ...
        String suppliedSignature = request.getParameter(PARAM_SIGNATURE);
        if (suppliedSignature == null)
            return false;

        // Firstly, extract the timestamp parameter and verify that it is within 5 minutes of 'current time'
        String timeString = request.getParameter(PARAM_TIMESTAMP);
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

        // Next, construct a sorted list of the name-value pair parameters supplied in the request, excluding the signature parameter
        Map<String, String> sortedParams = new TreeMap<>();
        for (Map.Entry<String, String[]> entry: request.getParameterMap().entrySet()) {
            String name = entry.getKey();
            String value = entry.getValue()[0];
            log.info("" + name + " = " + value);
            if (name.equals(PARAM_SIGNATURE))
                continue;
            if (value == null || value.trim().equals("")) {
                continue;
            }
            sortedParams.put(name, value);
        }

        // walk this sorted list of parameters and construct a string
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> param: sortedParams.entrySet()) {
            String name = param.getKey();
            String value = param.getValue();
            sb.append("&").append(clean(name)).append("=").append(clean(value));
        }

        // append the secret key and calculate an md5 signature of the resultant string
        sb.append(secretKey);

        String str = sb.toString();

        String md5;
        try {
            md5 = MD5Util.calculateMd5(str);
        } catch (Exception e) {
            log.error("error...", e);
            return false;
        }

        log.info("SECURITY-KEY-VERIFICATION -- String [ " + str + " ] Signature [ " + md5 + " ] SUPPLIED SIGNATURE [ " + suppliedSignature + " ] ");

        // verify that the supplied signature matches generated one
        // use MessageDigest.isEqual as an alternative to String.equals() to defend against timing based attacks
        try {
            if (!MessageDigest.isEqual(md5.getBytes("UTF-8"), suppliedSignature.getBytes("UTF-8")))
                return false;
        } catch (UnsupportedEncodingException e) {
            throw new VonageUnexpectedException("Failed to decode signature as UTF-8", e);
        }

        return true;
    }

    public static String clean(String str) {
        return str == null ? null : str.replaceAll("[=&]", "_");
    }

}
