package com.nexmo.verify.sdk;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.ParserConfigurationException;

import com.nexmo.common.LegacyClient;
import com.nexmo.common.NexmoResponseParseException;
import com.nexmo.common.util.XmlUtil;
import com.nexmo.verify.sdk.endpoints.CheckClient;
import com.nexmo.verify.sdk.endpoints.SearchClient;
import com.nexmo.verify.sdk.endpoints.VerifyClient;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
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

/**
 * A client for talking to the Nexmo Verify API interface.
 * <p>
 * To request a verification to Nexmo, call one of the {@link #verify} methods.
 * The only mandatory parameters are the phone number and the brand name. The verification text message
 * and/or voice call will have the brand name you provide so the user can recognize it.
 * <p>
 * After receiving the verification code from the user, you should send it to Nexmo using the
 * {@link #check} method.
 * <p>
 * You can search for an in-progress or past verification request using {@link #search}.
 * <p>
 * Error codes are listed in {@link BaseResult} and also on the documentation website.
 * <p>
 * More information on method parameters can be found at Nexmo website:
 * <a href="https://docs.nexmo.com/verify">https://docs.nexmo.com/verify</a>
 *
 * @author Daniele Ricci
 */
public class NexmoVerifyClient extends LegacyClient {

    private static final Log log = LogFactory.getLog(NexmoVerifyClient.class);

    public enum LineType {

        ALL,
        MOBILE,
        LANDLINE;

    }

    /**
     * https://rest.nexmo.com<br>
     * Service url used unless over-ridden on the constructor
     */
    public static final String DEFAULT_BASE_URL = "https://api.nexmo.com";

    /**
     * Default connection timeout of 5000ms used by this client unless specifically overridden onb the constructor
     */
    public static final int DEFAULT_CONNECTION_TIMEOUT = 5000;

    /**
     * Default read timeout of 30000ms used by this client unless specifically overridden onb the constructor
     */
    public static final int DEFAULT_SO_TIMEOUT = 30000;

    private CheckClient checkClient;
    private VerifyClient verifyClient;
    private SearchClient searchClient;

    /**
     * Instantiate a new NexmoVerifyClient instance that will communicate using the supplied credentials.
     *
     * @param apiKey Your Nexmo account api key
     * @param apiSecret Your Nexmo account api secret
     *
     * @throws ParserConfigurationException if the XML parser could not be configured.
     */
    public NexmoVerifyClient(final String apiKey,
                             final String apiSecret) throws ParserConfigurationException {
        this(DEFAULT_BASE_URL,
             apiKey,
             apiSecret,
             DEFAULT_CONNECTION_TIMEOUT,
             DEFAULT_SO_TIMEOUT);
    }

    /**
     * Instantiate a new NexmoVerifyClient instance that will communicate using the supplied credentials, and will use the supplied connection and read timeout values.<br>
     * Additionally, you can specify an alternative service base url. For example submitting to a testing sandbox environment,
     * or if requested to submit to an alternative address by Nexmo, for example, in cases where it may be necessary to prioritize your traffic.
     *
     * @param baseUrl The base URL to be used instead of <code>DEFAULT_BASE_URL</code>
     * @param apiKey Your Nexmo account api key
     * @param apiSecret Your Nexmo account api secret
     * @param connectionTimeout over-ride the default connection timeout with this value (in milliseconds)
     * @param soTimeout over-ride the default read-timeout with this value (in milliseconds)
     *
     * @throws ParserConfigurationException if the XML parser could not be configured.
     */
    public NexmoVerifyClient(final String baseUrl,
                             final String apiKey,
                             final String apiSecret,
                             final int connectionTimeout,
                             final int soTimeout) throws ParserConfigurationException {

        super(baseUrl, apiKey, apiSecret, connectionTimeout, soTimeout);
        this.checkClient = new CheckClient(baseUrl, apiKey, apiSecret, connectionTimeout, soTimeout);
        this.searchClient = new SearchClient(baseUrl, apiKey, apiSecret, connectionTimeout, soTimeout);
        this.verifyClient = new VerifyClient(baseUrl, apiKey, apiSecret, connectionTimeout, soTimeout);
    }

    public VerifyResult verify(final String number,
                               final String brand) throws IOException,
                                                          NexmoResponseParseException {
        return verifyClient.verify(number, brand);
    }

    public VerifyResult verify(final String number,
                               final String brand,
                               final String from) throws IOException,
                                                         NexmoResponseParseException {
        return verifyClient.verify(number, brand, from);
    }

    public VerifyResult verify(final String number,
                               final String brand,
                               final String from,
                               final int length,
                               final Locale locale) throws IOException,
                                                           NexmoResponseParseException {
        return verifyClient.verify(number, brand, from, length, locale);
    }

    public VerifyResult verify(final String number,
                               final String brand,
                               final String from,
                               final int length,
                               final Locale locale,
                               final NexmoVerifyClient.LineType type) throws IOException,
                                                                             NexmoResponseParseException {
        return verifyClient.verify(number, brand, from, length, locale, type);
    }



    public CheckResult check(final String requestId,
                             final String code) throws IOException, NexmoResponseParseException {
        return checkClient.check(requestId, code, null);
    }

    public CheckResult check(final String requestId,
                             final String code,
                             final String ipAddress) throws IOException, NexmoResponseParseException {
        return checkClient.check(requestId, code, ipAddress);
    }

    public SearchResult search(String requestId) throws IOException, NexmoResponseParseException {
        return searchClient.search(requestId);
    }

    public SearchResult[] search(String... requestIds) throws IOException, NexmoResponseParseException {
        return searchClient.search(requestIds);
    }

    @Override
    public void setHttpClient(HttpClient httpClient) {
        super.setHttpClient(httpClient);
        this.checkClient.setHttpClient(httpClient);
        this.searchClient.setHttpClient(httpClient);
        this.verifyClient.setHttpClient(httpClient);
    }
}
