package com.nexmo.insight.sdk;
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

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.nexmo.common.http.HttpClientUtils;

/**
 * Client for talking to the Nexmo REST interface<br><br>
 *
 * Usage<br><br>
 *
 * Number insight searches for information about a phone number and returns it to a single URL callback
 * via a GET/POST request.<br>
 * To request an insight, call {@link #request}. Please refer to Nexmo website for current insight fees.<br>
 * <br>
 * Error codes are listed in {@link InsightResult} and also on the documentation website.<br>
 * <br>
 * More information on method parameters can be found at Nexmo website:
 * <a href="https://docs.nexmo.com/index.php/number-insight">https://docs.nexmo.com/index.php/number-insight</a>
 *
 * @author Daniele Ricci
 */
public class NexmoInsightClient {

    private static final Log log = LogFactory.getLog(NexmoInsightClient.class);

    /**
     * https://rest.nexmo.com<br>
     * Service url used unless over-ridden on the constructor
     */
    public static final String DEFAULT_BASE_URL = "https://rest.nexmo.com";

    /**
     * The endpoint path for submitting number insight requests
     */
    public static final String PATH_INSIGHT = "/ni/xml";

    /**
     * Default connection timeout of 5000ms used by this client unless specifically overridden onb the constructor
     */
    public static final int DEFAULT_CONNECTION_TIMEOUT = 5000;

    /**
     * Default read timeout of 30000ms used by this client unless specifically overridden onb the constructor
     */
    public static final int DEFAULT_SO_TIMEOUT = 30000;

    private final DocumentBuilderFactory documentBuilderFactory;
    private final DocumentBuilder documentBuilder;

    private final String baseUrl;
    private final String apiKey;
    private final String apiSecret;

    private final int connectionTimeout;
    private final int soTimeout;

    private HttpClient httpClient = null;

    /**
     * Instanciate a new NexmoInsightClient instance that will communicate using the supplied credentials.
     *
     * @param apiKey Your Nexmo account api key
     * @param apiSecret Your Nexmo account api secret
     */
    public NexmoInsightClient(final String apiKey,
                              final String apiSecret) throws ParserConfigurationException {
        this(DEFAULT_BASE_URL,
             apiKey,
             apiSecret,
             DEFAULT_CONNECTION_TIMEOUT,
             DEFAULT_SO_TIMEOUT);
    }

    /**
     * Instanciate a new NexmoInsightClient instance that will communicate using the supplied credentials, and will use the supplied connection and read timeout values.<br>
     * Additionally, you can specify an alternative service base url. For example submitting to a testing sandbox environment,
     * or if requested to submit to an alternative address by Nexmo, for example, in cases where it may be necessary to prioritize your traffic.
     *
     * @param apiKey Your Nexmo account api key
     * @param apiSecret Your Nexmo account api secret
     * @param connectionTimeout over-ride the default connection timeout with this value (in milliseconds)
     * @param soTimeout over-ride the default read-timeout with this value (in milliseconds)
     */
    public NexmoInsightClient(final String baseUrl,
                              final String apiKey,
                              final String apiSecret,
                              final int connectionTimeout,
                              final int soTimeout) throws ParserConfigurationException {

        // Derive a http and a https version of the supplied base url
        if (baseUrl == null)
            throw new IllegalArgumentException("base url is null");
        String url = baseUrl.trim();
        String lc = url.toLowerCase();
        if (!lc.startsWith("https://"))
            throw new IllegalArgumentException("base url does not start with https://");

        this.baseUrl = url;
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
        this.connectionTimeout = connectionTimeout;
        this.soTimeout = soTimeout;
        this.documentBuilderFactory = DocumentBuilderFactory.newInstance();
        this.documentBuilder = this.documentBuilderFactory.newDocumentBuilder();
    }

    public InsightResult request(final String number,
                                 final String callbackUrl) throws IOException,
                                                                  SAXException {
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
                                                                SAXException {
        if (number == null || callbackUrl == null)
            throw new IllegalArgumentException("number and callbackUrl parameters are mandatory.");
        if (callbackTimeout >= 0 && (callbackTimeout < 1000 || callbackTimeout > 30000))
            throw new IllegalArgumentException("callback timeout must be between 1000 and 30000.");

        log.debug("HTTP-Number-Insight Client .. to [ " + number + " ] ");

        List<NameValuePair> params = new ArrayList<>();

        params.add(new BasicNameValuePair("api_key", this.apiKey));
        params.add(new BasicNameValuePair("api_secret", this.apiSecret));

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

        String inshightBaseUrl = this.baseUrl + PATH_INSIGHT;

        // Now that we have generated a query string, we can instanciate a HttpClient,
        // construct a POST or GET method and execute to submit the request
        String response = null;
        for (int pass=1;pass<=2;pass++) {
            HttpPost httpPost = new HttpPost(inshightBaseUrl);
            httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            HttpUriRequest method = httpPost;
            String url = inshightBaseUrl + "?" + URLEncodedUtils.format(params, "utf-8");

            try {
                if (this.httpClient == null)
                    this.httpClient = HttpClientUtils.getInstance(this.connectionTimeout, this.soTimeout).getNewHttpClient();
                HttpResponse httpResponse = this.httpClient.execute(method);
                int status = httpResponse.getStatusLine().getStatusCode();
                if (status != 200)
                    throw new Exception("got a non-200 response [ " + status + " ] from Nexmo-HTTP for url [ " + url + " ] ");
                response = new BasicResponseHandler().handleResponse(httpResponse);
                log.info(".. SUBMITTED NEXMO-HTTP URL [ " + url + " ] -- response [ " + response + " ] ");
                break;
            } catch (Exception e) {
                method.abort();
                log.info("communication failure: " + e);
                String exceptionMsg = e.getMessage();
                if (exceptionMsg.indexOf("Read timed out") >= 0) {
                    log.info("we're still connected, but the target did not respond in a timely manner ..  drop ...");
                } else {
                    if (pass == 1) {
                        log.info("... re-establish http client ...");
                        this.httpClient = null;
                        continue;
                    }
                }

                // return a COMMS failure ...
                return new InsightResult(InsightResult.STATUS_COMMS_FAILURE,
                                         null,
                                         null,
                                         0,
                                         0,
                                         "Failed to communicate with NEXMO-HTTP url [ " + url + " ] ..." + e,
                                         true);
            }
        }

        Document doc;
        synchronized(this.documentBuilder) {
            doc = this.documentBuilder.parse(new InputSource(new StringReader(response)));
        }

        Element root = doc.getDocumentElement();
        if (!"lookup".equals(root.getNodeName()))
            throw new IOException("No valid response found [ " + response + "] ");

        return parseInsightResult(root);
    }

    private static String strJoin(String[] aArr, String sSep) {
        StringBuilder sbStr = new StringBuilder();
        for (int i = 0, il = aArr.length; i < il; i++) {
            if (i > 0)
                sbStr.append(sSep);
            sbStr.append(aArr[i]);
        }
        return sbStr.toString();
    }

    private static InsightResult parseInsightResult(Element root) throws IOException {
        String requestId = null;
        String number = null;
        float price = -1;
        float balance = -1;
        int status = -1;
        String errorText = null;

        NodeList fields = root.getChildNodes();
        for (int i = 0; i < fields.getLength(); i++) {
            Node node = fields.item(i);
            if (node.getNodeType() != Node.ELEMENT_NODE)
                continue;

            String name = node.getNodeName();
            if ("requestId".equals(name)) {
                requestId = node.getFirstChild() == null ? null : node.getFirstChild().getNodeValue();
            } else if ("number".equals(name)) {
                number = node.getFirstChild() == null ? null : node.getFirstChild().getNodeValue();
            } else if ("status".equals(name)) {
                String str = node.getFirstChild() == null ? null : node.getFirstChild().getNodeValue();
                try {
                    if (str != null)
                        status = Integer.parseInt(str);
                } catch (NumberFormatException e) {
                    log.error("xml parser .. invalid value in <status> node [ " + str + " ] ");
                    status = InsightResult.STATUS_INTERNAL_ERROR;
                }
            } else if ("requestPrice".equals(name)) {
                String str = node.getFirstChild() == null ? null : node.getFirstChild().getNodeValue();
                try {
                    if (str != null)
                        price = Float.parseFloat(str);
                } catch (NumberFormatException e) {
                    log.error("xml parser .. invalid value in <requestPrice> node [ " + str + " ] ");
                }
            } else if ("remainingBalance".equals(name)) {
                String str = node.getFirstChild() == null ? null : node.getFirstChild().getNodeValue();
                try {
                    if (str != null)
                        balance = Float.parseFloat(str);
                } catch (NumberFormatException e) {
                    log.error("xml parser .. invalid value in <remainingBalance> node [ " + str + " ] ");
                }
            } else if ("errorText".equals(name)) {
                errorText = node.getFirstChild() == null ? null : node.getFirstChild().getNodeValue();
            }
        }

        if (status == -1)
            throw new IOException("Xml Parser - did not find a <status> node");

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

}
