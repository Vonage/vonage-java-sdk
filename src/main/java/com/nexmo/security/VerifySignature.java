package com.nexmo.security;

import java.util.Map;
import java.util.TreeMap;
import java.util.Iterator;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.nexmo.common.util.MD5Util;

/**
 * VerifySignature.java<br><br>
 *
 * Helper class to assist with verifying sigmatures on inbound message requests
 * Upon receiving a request, the HttpServletRequest object should be passed to the verifyRequestSignature method
 * along with the secret key assigned to your account.
 * This will verify that the message request was signed by and originated from the Nexmo platform.
 * Additionally, the timestamp will be checked to ensure it is within an acceptable time window to help ensure
 * that a properly signed request has not been intercepted and later re-played
 *
 * Created on 5 January 2011, 17:34
 *
 * @author  Paul Cook
 * @version 1.0
 */
public class VerifySignature implements SecurityConstants {

    private static Log log = LogFactory.getLog(VerifySignature.class);

    /**
     * looks at the current http request and verifies that the request signature, if supplied is valid.
     *
     * @param request The servlet request object to be verified
     * @param secretKey the pre-shared secret key used by the sender of the request to create the signature
     *
     * @return boolean returns true only if the signature is correct for this request and secret key.
     */
    public static boolean verifyRequestSignature(HttpServletRequest request, String secretKey) {
        // identify the signature supplied in the request ...
        String suppliedSignature = request.getParameter(PARAM_SIGNATURE);

        // Firstly, extract the timestamp parameter and verify that it is within 5 minutes of 'current time'
        String timeString = request.getParameter(PARAM_TIMESTAMP);
        long time = -1;
        try {
            time = Long.parseLong(timeString) * 1000;
        } catch (NumberFormatException e) {
        	time = 0;
        }
        long diff = System.currentTimeMillis() - time;
        if (diff > MAX_ALLOWABLE_TIME_DELTA || diff < -MAX_ALLOWABLE_TIME_DELTA) {
            log.debug("SECURITY-KEY-VERIFICATION -- BAD-TIMESTAMP ... Timestamp [ " + time + " ] delta [ " + diff + " ] max allowed delta [ " + -MAX_ALLOWABLE_TIME_DELTA + " ] ");
            return false;
        }

        // Next, construct a sorted list of the name-value pair parameters supplied in the request, excluding the signature parameter
        Map<String, String> sortedParams = new TreeMap<String, String>();
        for (Iterator<?> I = request.getParameterMap().entrySet().iterator();I.hasNext();) {
            Map.Entry<?, ?> param = (Map.Entry<?, ?>)I.next();
            String name = (String)param.getKey();
            String value = ((String[])(param.getValue()))[0];
            if (name.equals(PARAM_SIGNATURE))
                continue;
            if (value == null || value.trim().equals(""))
                continue;
            sortedParams.put(name, value);
        }

        // walk this sorted list of parameters and construct a string
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> param: sortedParams.entrySet()) {
            String name = param.getKey();
            String value = param.getValue();
            sb.append(name).append(value);
        }

        // append the secret key and calculate an md5 signature of the resultant string
        sb.append(secretKey);

        String str = sb.toString();

        String md5 = "no signature";
        try {
            md5 = MD5Util.calculateMd5(str);
        } catch (Exception e) {
            log.error("error...", e);
        }

        log.debug("SECURITY-KEY-VERIFICATION -- String [ " + str + " ] Signature [ " + md5 + " ] SUPPLIED SIGNATURE [ " + suppliedSignature + " ] ");

        // verify that the secre
        if (!md5.equals(suppliedSignature))
            return false;

        return true;
    }

}
