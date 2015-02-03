package com.nexmo.verify.sdk;
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.HttpClient;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Client for talking to the Nexmo REST interface<br><br>
 *
 * Usage<br><br>
 *
 * TODO
 *
 * @author Daniele Ricci
 */
public class NexmoVerifyClient {
    private static final Log log = LogFactory.getLog(NexmoVerifyClient.class);

    /**
     * http://rest.nexmo.com<br>
     * Service url used unless over-ridden on the constructor
     */
    public static final String DEFAULT_BASE_URL = "http://rest.nexmo.com";

    /**
     * The endpoint path for submitting verification requests
     */
    public static final String SUBMISSION_PATH_VERIFY = "/verify/xml";

    /**
     * The endpoint path for submitting ussd 'display' messages
     */
    public static final String SUBMISSION_PATH_VERIFY_CHECK = "/verify/check/xml";

    /**
     * Default connection timeout of 5000ms used by this client unless specifically overridden onb the constructor
     */
    public static final int DEFAULT_CONNECTION_TIMEOUT = 5000;

    /**
     * Default read timeout of 30000ms used by this client unless specifically overridden onb the constructor
     */
    public static final int DEFAULT_SO_TIMEOUT = 30000;

    private DocumentBuilderFactory documentBuilderFactory;
    private DocumentBuilder documentBuilder;

    private final String baseUrlHttp;
    private final String baseUrlHttps;
    private final String apiKey;
    private final String apiSecret;

    private final int connectionTimeout;
    private final int soTimeout;

    private final boolean useSSL;

    private HttpClient httpClient = null;

    /**
     * Instanciate a new NexmoVerifyClient instance that will communicate using the supplied credentials.
     *
     * @param apiKey Your Nexmo account api key
     * @param apiSecret Your Nexmo account api secret
     */
    public NexmoVerifyClient(final String apiKey,
                          final String apiSecret) throws Exception {
        this(DEFAULT_BASE_URL,
                apiKey,
                apiSecret,
                DEFAULT_CONNECTION_TIMEOUT,
                DEFAULT_SO_TIMEOUT,
                false); // useSSL
    }

    /**
     * Instanciate a new NexmoVerifyClient instance that will communicate using the supplied credentials, and will use the supplied connection and read timeout values.
     *
     * @param apiKey Your Nexmo account api key
     * @param apiSecret Your Nexmo account api secret
     * @param connectionTimeout over-ride the default connection timeout with this value (in milliseconds)
     * @param soTimeout over-ride the default read-timeout with this value (in milliseconds)
     */
    public NexmoVerifyClient(final String apiKey,
                          final String apiSecret,
                          final int connectionTimeout,
                          final int soTimeout) throws Exception {
        this(DEFAULT_BASE_URL,
                apiKey,
                apiSecret,
                connectionTimeout,
                soTimeout,
                false); // useSSL
    }

    /**
     * Instanciate a new NexmoVerifyClient instance that will communicate using the supplied credentials, and will use the supplied connection and read timeout values.<br>
     * Additionally, you can specify an alternative service base url. For example submitting to a testing sandbox environment,
     * or if requested to submit to an alternative address by Nexmo, for example, in cases where it may be necessary to prioritize your traffic.
     *
     * @param apiKey Your Nexmo account api key
     * @param apiSecret Your Nexmo account api secret
     * @param connectionTimeout over-ride the default connection timeout with this value (in milliseconds)
     * @param soTimeout over-ride the default read-timeout with this value (in milliseconds)
     * @param useSSL do we use a SSL / HTTPS connection for submitting requests
     */
    public NexmoVerifyClient(String baseUrl,
                          final String apiKey,
                          final String apiSecret,
                          final int connectionTimeout,
                          final int soTimeout,
                          final boolean useSSL) throws Exception {

        // Derive a http and a https version of the supplied base url
        baseUrl = baseUrl.trim();
        String lc = baseUrl.toLowerCase();
        if (!lc.startsWith("http://") && !lc.startsWith("https://"))
            throw new Exception("base url does not start with http:// or https://");
        if (lc.startsWith("http://")) {
            this.baseUrlHttp = baseUrl;
            this.baseUrlHttps = "https://" + baseUrl.substring(7);
        } else {
            this.baseUrlHttps = baseUrl;
            this.baseUrlHttp = "http://" + baseUrl.substring(8);
        }

        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
        this.connectionTimeout = connectionTimeout;
        this.soTimeout = soTimeout;
        try {
            this.documentBuilderFactory = DocumentBuilderFactory.newInstance();
            this.documentBuilder = this.documentBuilderFactory.newDocumentBuilder();
        } catch (javax.xml.parsers.ParserConfigurationException e) {
            throw new Exception("ERROR initializing XML Document builder!", e);
        }

        this.useSSL = useSSL;
    }

}
