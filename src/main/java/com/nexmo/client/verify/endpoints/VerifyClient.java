/*
 * Copyright (c) 2011-2017 Nexmo Inc
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
package com.nexmo.client.verify.endpoints;

import com.nexmo.common.LegacyClient;
import com.nexmo.client.NexmoResponseParseException;
import com.nexmo.client.verify.BaseResult;
import com.nexmo.client.verify.VerifyResult;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class VerifyClient extends LegacyClient {
    private static final Log log = LogFactory.getLog(VerifyClient.class);

    /**
     * The endpoint path for submitting verification requests
     */
    public static final String PATH_VERIFY = "/verify/xml";

    /**
     * Create a new VerifyClient.
     * <p>
     * This client is used for calling the verify API's verify endpoint.
     *
     * @param baseUrl The base URL to be used instead of <code>DEFAULT_BASE_URL</code>
     * @param apiKey Your Nexmo account api key
     * @param apiSecret Your Nexmo account api secret
     * @param connectionTimeout over-ride the default connection timeout with this value (in milliseconds)
     * @param soTimeout over-ride the default read-timeout with this value (in milliseconds)
     */
    public VerifyClient(final String baseUrl,
                        final String apiKey,
                        final String apiSecret,
                        final int connectionTimeout,
                        final int soTimeout) {

        super(baseUrl, apiKey, apiSecret, connectionTimeout, soTimeout);
    }

    public VerifyResult verify(final String number,
                               final String brand) throws IOException,
                                                          NexmoResponseParseException {
        return verify(number,
                brand,
                null,
                -1,
                null,
                null);
    }

    public VerifyResult verify(final String number,
                               final String brand,
                               final String from) throws IOException,
                                                         NexmoResponseParseException {
        return verify(number,
                brand,
                from,
                -1,
                null,
                null);
    }

    public VerifyResult verify(final String number,
                               final String brand,
                               final String from,
                               final int length,
                               final Locale locale) throws IOException,
                                                           NexmoResponseParseException {
        return verify(number,
                brand,
                from,
                length,
                locale,
                null);
    }

    public VerifyResult verify(final String number,
                               final String brand,
                               final String from,
                               final int length,
                               final Locale locale,
                               final com.nexmo.client.verify.VerifyClient.LineType type) throws IOException,
                                                                                                NexmoResponseParseException {
        List<NameValuePair> params = constructVerifyParams(number, brand, from, length, locale, type);

        String verifyBaseUrl = this.makeUrl(PATH_VERIFY);

        // Now that we have generated a query string, we can instantiate a HttpClient,
        // construct a POST method and execute to submit the request
        String response;
        HttpPost httpPost = new HttpPost(verifyBaseUrl);
        httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
        String url = verifyBaseUrl + "?" + URLEncodedUtils.format(params, "utf-8");

        try {
            HttpResponse httpResponse = this.getHttpClient().execute(httpPost);
            response = new BasicResponseHandler().handleResponse(httpResponse);
            log.info(".. SUBMITTED NEXMO-HTTP URL [ " + url + " ] -- response [ " + response + " ] ");
        } catch (HttpResponseException e) {
            httpPost.abort();
            log.error("communication failure: ", e);

            // return a COMMS failure ...
            return new VerifyResult(BaseResult.STATUS_COMMS_FAILURE,
                    null,
                    "Failed to communicate with NEXMO-HTTP url [ " + url + " ] ..." + e,
                    true);
        }

        return parseVerifyResponse(response);
    }

    protected VerifyResult parseVerifyResponse(String response) throws NexmoResponseParseException {
        Document doc = parseXml(response);

        Element root = doc.getDocumentElement();
        if (!"verify_response".equals(root.getNodeName()))
            throw new NexmoResponseParseException("No valid response found [ " + response + "] ");

        return SharedParsers.parseVerifyResponseXmlNode(root);
    }


    protected List<NameValuePair> constructVerifyParams(
            String number, String brand, String from, int length, Locale locale, com.nexmo.client.verify.VerifyClient.LineType type) {
        if (number == null || brand == null)
            throw new IllegalArgumentException("number and brand parameters are mandatory.");
        if (length > 0 && length != 4 && length != 6)
            throw new IllegalArgumentException("code length must be 4 or 6.");

        log.debug("HTTP-Number-Verify Client .. to [ " + number + " ] brand [ " + brand + " ] ");

        List<NameValuePair> params = this.constructParams();
        params.add(new BasicNameValuePair("number", number));
        params.add(new BasicNameValuePair("brand", brand));

        if (from != null)
            params.add(new BasicNameValuePair("sender_id", from));

        if (length > 0)
            params.add(new BasicNameValuePair("code_length", String.valueOf(length)));

        if (locale != null)
            params.add(new BasicNameValuePair("lg",
                    (locale.getLanguage() + "-" + locale.getCountry()).toLowerCase()));

        if (type != null)
            params.add(new BasicNameValuePair("require_type", type.toString()));
        return params;
    }
}
