package com.nexmo.security;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.nexmo.common.util.MD5Util;

/**
 * RequestSigning
 *
 * Helper class for generating signatures when signing REST requests for submission to Nexmo.
 * Generates a signature based on the md5 hash of all of the request parameters,
 * including a timestamp parameter to protect against replay attacks.
 * The signature is generated using a pre-shared secret key that does not form part of the request.
 *
 * @author  Paul Cook
 * @version
 */
public class RequestSigning implements SecurityConstants {

    private static Log log = LogFactory.getLog(RequestSigning.class);

    /**
     * sign a set of request parameters, generating additional parameters to represent the timestamp and generated signature
     * uses the supplied pre-shared secret key to generate the signature
     *
     * @param params List of NameValuePair instances containing the query parameters for the request that is to be signed
     * @param secretKey the pre-shared secret key held by the client
     *
     * @return String the fully constructed url complete with signature
     */
    public static void constructSignatureForRequestParameters(List<NameValuePair> params, String secretKey) {
        // First, inject a 'timestamp=' parameter containing the current time in seconds since Jan 1st 1970
        params.add(new BasicNameValuePair(PARAM_TIMESTAMP, "" + System.currentTimeMillis() / 1000));

        Map<String, String> sortedParams = new TreeMap<String, String>();
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
            sb.append(name).append(value);
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

}
