package com.nexmo.insight.sdk;
/*
 * Copyright (c) 2011-2016 Nexmo Inc
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

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import com.nexmo.common.LegacyClient;
import com.nexmo.common.NexmoResponseParseException;
import com.nexmo.common.util.XmlUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Client for talking to the Nexmo REST interface.
 * <p>
 * Number insight searches for information about a phone number and returns it to a single URL callback
 * via a GET/POST request.
 * To request an insight, call {@link #request}. Please refer to Nexmo website for current insight fees.
 * Error codes are listed in {@link InsightResult} and also on the documentation website.
 * More information on method parameters can be found at Nexmo website:
 * <a href="https://docs.nexmo.com/number-insight">https://docs.nexmo.com/number-insight</a>.
 *
 * @author Daniele Ricci
 */
public class NexmoInsightClient extends LegacyClient {

    private static final Log log = LogFactory.getLog(NexmoInsightClient.class);

    /**
     * The service url used unless over-ridden on the constructor
     */
    private static final String DEFAULT_BASE_URL = "https://rest.nexmo.com";

    /**
     * The endpoint path for submitting number insight requests
     */
    private static final String PATH_INSIGHT = "/ni/xml";

    /**
     * Default connection timeout of 5000ms used by this client unless specifically overridden onb the constructor
     */
    private static final int DEFAULT_CONNECTION_TIMEOUT = 5000;

    /**
     * Default read timeout of 30000ms used by this client unless specifically overridden onb the constructor
     */
    private static final int DEFAULT_SO_TIMEOUT = 30000;

    private HttpClient httpClient = null;

    /**
     * Instantiate a new NexmoInsightClient instance that will communicate using the supplied credentials.
     *
     * @param apiKey Your Nexmo account api key
     * @param apiSecret Your Nexmo account api secret
     */
    public NexmoInsightClient(final String apiKey,
                              final String apiSecret) {
        this(DEFAULT_BASE_URL,
             apiKey,
             apiSecret,
             DEFAULT_CONNECTION_TIMEOUT,
             DEFAULT_SO_TIMEOUT);
    }

    /**
     * Instantiate a new NexmoInsightClient instance that will communicate using the supplied credentials, and will use the supplied connection and read timeout values.<br>
     * Additionally, you can specify an alternative service base url. For example submitting to a testing sandbox environment,
     * or if requested to submit to an alternative address by Nexmo, for example, in cases where it may be necessary to prioritize your traffic.
     *
     * @param baseUrl The base URL to use instead of <code>DEFAULT_BASE_URL</code>
     * @param apiKey Your Nexmo account api key
     * @param apiSecret Your Nexmo account api secret
     * @param connectionTimeout over-ride the default connection timeout with this value (in milliseconds)
     * @param soTimeout over-ride the default read-timeout with this value (in milliseconds)
     */
    public NexmoInsightClient(final String baseUrl,
                              final String apiKey,
                              final String apiSecret,
                              final int connectionTimeout,
                              final int soTimeout) {

        super(baseUrl, apiKey, apiSecret, connectionTimeout, soTimeout);
    }

    public InsightResult request(final String number,
                                 final String callbackUrl) throws IOException,
                                                                  NexmoResponseParseException {
        return request(number,
                       callbackUrl,
                       null,
                       -1,
                       "GET",
                       null,
                       null);
    }

    public InsightResult request(final String number,
                                 final String callbackUrl,
                                 final String[] features,
                                 final long callbackTimeout,
                                 final String callbackMethod,
                                 final String clientRef,
                                 final String ipAddress) throws IOException,
                                                                NexmoResponseParseException {
        if (number == null || callbackUrl == null)
            throw new IllegalArgumentException("number and callbackUrl parameters are mandatory.");
        if (callbackTimeout >= 0 && (callbackTimeout < 1000 || callbackTimeout > 30000))
            throw new IllegalArgumentException("callback timeout must be between 1000 and 30000.");

        log.debug("HTTP-Number-Insight Client .. to [ " + number + " ] ");

        List<NameValuePair> params = constructParams();

        params.add(new BasicNameValuePair("number", number));
        params.add(new BasicNameValuePair("callback", callbackUrl));

        if (features != null)
            params.add(new BasicNameValuePair("features", strJoin(features, ",")));

        if (callbackTimeout >= 0)
            params.add(new BasicNameValuePair("callback_timeout", String.valueOf(callbackTimeout)));

        if (callbackMethod != null)
            params.add(new BasicNameValuePair("callback_method", callbackMethod));

        if (ipAddress != null)
            params.add(new BasicNameValuePair("ip", ipAddress));

        if (clientRef != null)
            params.add(new BasicNameValuePair("client_ref", clientRef));

        String inshightBaseUrl = this.makeUrl(PATH_INSIGHT);

        String response;
        HttpPost httpPost = new HttpPost(inshightBaseUrl);
        httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
        String url = inshightBaseUrl + "?" + URLEncodedUtils.format(params, "utf-8");

        try {
            HttpResponse httpResponse = this.httpClient.execute(httpPost);
            response = new BasicResponseHandler().handleResponse(httpResponse);
            log.info(".. SUBMITTED NEXMO-HTTP URL [ " + url + " ] -- response [ " + response + " ] ");
        } catch (HttpResponseException e) {
            httpPost.abort();
            log.error("Communication failure", e);

            // return a COMMS failure ...
            return new InsightResult(InsightResult.STATUS_COMMS_FAILURE,
                                     null,
                                     null,
                                     null,
                                     null,
                                     "Failed to communicate with NEXMO-HTTP url [ " + url + " ] ..." + e,
                                     true);
        }

        return parseInsightResult(response);
    }

    private static String strJoin(String[] aArr, String sSep) {
        StringBuilder sbStr = new StringBuilder();
        for (int i = 0; i < aArr.length; i++) {
            if (i > 0)
                sbStr.append(sSep);
            sbStr.append(aArr[i]);
        }
        return sbStr.toString();
    }

    protected InsightResult parseInsightResult(String response) throws NexmoResponseParseException, IOException {
        Document doc = parseXml(response);

        Element root = doc.getDocumentElement();
        if (!"lookup".equals(root.getNodeName()))
            throw new NexmoResponseParseException("No valid response found [ " + response + "] ");

        String requestId = null;
        String number = null;
        BigDecimal price = null;
        BigDecimal balance = null;
        int status = -1;
        String errorText = null;

        NodeList fields = root.getChildNodes();
        for (int i = 0; i < fields.getLength(); i++) {
            Node node = fields.item(i);
            if (node.getNodeType() != Node.ELEMENT_NODE)
                continue;

            String name = node.getNodeName();
            if ("requestId".equals(name)) {
                requestId = XmlUtil.stringValue(node);
            } else if ("number".equals(name)) {
                number = XmlUtil.stringValue(node);
            } else if ("status".equals(name)) {
                String str = XmlUtil.stringValue(node);
                if (str != null)
                    try {
                            status = Integer.parseInt(str);
                    } catch (NumberFormatException e) {
                        log.error("xml parser .. invalid value in <status> node [ " + str + " ] ");
                        status = InsightResult.STATUS_INTERNAL_ERROR;
                    }
            } else if ("requestPrice".equals(name)) {
                String str = XmlUtil.stringValue(node);
                if (str != null)
                    try {
                        price = new BigDecimal(str);
                    } catch (NumberFormatException nfe) {
                        log.error(String.format("<requestPrice> contained invalid value: %s", str));
                    }
            } else if ("remainingBalance".equals(name)) {
                String str = XmlUtil.stringValue(node);
                if (str != null)
                    try {
                        balance = new BigDecimal(str);
                    } catch (NumberFormatException nfe) {
                        log.error(String.format("<balance> contained invalid value: %s", str));
                    }
            } else if ("errorText".equals(name)) {
                errorText = XmlUtil.stringValue(node);
            }
        }

        if (status == -1)
            throw new NexmoResponseParseException("Xml Parser - did not find a <status> node");

        // Is this a temporary error ?
        boolean temporaryError = (status == InsightResult.STATUS_THROTTLED || status == InsightResult.STATUS_INTERNAL_ERROR);

        return new InsightResult(status,
                                 requestId,
                                 number,
                                 price,
                                 balance,
                                 errorText,
                                 temporaryError);
    }

    // Allowing users of this client to plugin their own HttpClient.
    public void setHttpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

}
