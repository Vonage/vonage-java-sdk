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

import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
 * To request a verification to Nexmo, call one of the {@link #verify} methods.
 * The only mandatory parameters are the phone number and the brand name. The verification text message
 * and/or voice call will have the brand name you provide so the user can recognize it.<br>
 * <br>
 * After receiving the verification code from the user, you should send it to Nexmo using the
 * {@link #check} method.<br>
 * <br>
 * You can search for an in-progress or past verification request using {@link #search}.<br>
 * <br>
 * Error codes are listed in {@link BaseResult} and also on the documentation website.<br>
 * <br>
 * More information on method parameters can be found at Nexmo website:
 * <a href="https://docs.nexmo.com/index.php/verify">https://docs.nexmo.com/index.php/verify</a>
 *
 * @author Daniele Ricci
 */
public class NexmoVerifyClient {

    private static final Log log = LogFactory.getLog(NexmoVerifyClient.class);

    public enum LineType {

        ALL,
        MOBILE,
        LANDLINE;

        @Override
        public String toString() {
            String name = name();
            return Character.toUpperCase(name.charAt(0)) + name.substring(1);
        }

    }

    /**
     * https://rest.nexmo.com<br>
     * Service url used unless over-ridden on the constructor
     */
    public static final String DEFAULT_BASE_URL = "https://api.nexmo.com";

    /**
     * The endpoint path for submitting verification requests
     */
    public static final String PATH_VERIFY = "/verify/xml";

    /**
     * The endpoint path for submitting verification check requests
     */
    public static final String PATH_VERIFY_CHECK = "/verify/check/xml";

    /**
     * The endpoint path for submitting verification search requests
     */
    public static final String PATH_VERIFY_SEARCH = "/verify/search/xml";

    /**
     * Default connection timeout of 5000ms used by this client unless specifically overridden onb the constructor
     */
    public static final int DEFAULT_CONNECTION_TIMEOUT = 5000;

    /**
     * Default read timeout of 30000ms used by this client unless specifically overridden onb the constructor
     */
    public static final int DEFAULT_SO_TIMEOUT = 30000;

    /**
     * Number of maximum request IDs that can be searched for.
     */
    private static final int MAX_SEARCH_REQUESTS = 10;

    private static final ThreadLocal<SimpleDateFormat> sDateTimePattern = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            sdf.setLenient(false);

            return sdf;
        }
    };

    private final DocumentBuilderFactory documentBuilderFactory;
    private final DocumentBuilder documentBuilder;

    private final String baseUrl;
    private final String apiKey;
    private final String apiSecret;

    private final int connectionTimeout;
    private final int soTimeout;

    private HttpClient httpClient = null;

    /**
     * Instanciate a new NexmoVerifyClient instance that will communicate using the supplied credentials.
     *
     * @param apiKey Your Nexmo account api key
     * @param apiSecret Your Nexmo account api secret
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
     * Instanciate a new NexmoVerifyClient instance that will communicate using the supplied credentials, and will use the supplied connection and read timeout values.<br>
     * Additionally, you can specify an alternative service base url. For example submitting to a testing sandbox environment,
     * or if requested to submit to an alternative address by Nexmo, for example, in cases where it may be necessary to prioritize your traffic.
     *
     * @param apiKey Your Nexmo account api key
     * @param apiSecret Your Nexmo account api secret
     * @param connectionTimeout over-ride the default connection timeout with this value (in milliseconds)
     * @param soTimeout over-ride the default read-timeout with this value (in milliseconds)
     */
    public NexmoVerifyClient(final String baseUrl,
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

    public VerifyResult verify(final String number,
                               final String brand) throws IOException,
                                                          SAXException {
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
                                                         SAXException {
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
                                                           SAXException {
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
                               final LineType type) throws IOException,
                                                           SAXException {
        if (number == null || brand == null)
            throw new IllegalArgumentException("number and brand parameters are mandatory.");
        if (length > 0 && length != 4 && length != 6)
            throw new IllegalArgumentException("code length must be 4 or 6.");

        log.debug("HTTP-Number-Verify Client .. to [ " + number + " ] brand [ " + brand + " ] ");

        List<NameValuePair> params = new ArrayList<>();

        params.add(new BasicNameValuePair("api_key", this.apiKey));
        params.add(new BasicNameValuePair("api_secret", this.apiSecret));

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

        String verifyBaseUrl = this.baseUrl + PATH_VERIFY;

        // Now that we have generated a query string, we can instanciate a HttpClient,
        // construct a POST method and execute to submit the request
        String response = null;
        for (int pass=1;pass<=2;pass++) {
            HttpPost httpPost = new HttpPost(verifyBaseUrl);
            httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            HttpUriRequest method = httpPost;
            String url = verifyBaseUrl + "?" + URLEncodedUtils.format(params, "utf-8");

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
                return new VerifyResult(BaseResult.STATUS_COMMS_FAILURE,
                                        null,
                                        "Failed to communicate with NEXMO-HTTP url [ " + url + " ] ..." + e,
                                        true);
            }
        }

        Document doc;
        synchronized(this.documentBuilder) {
            doc = this.documentBuilder.parse(new InputSource(new StringReader(response)));
        }

        Element root = doc.getDocumentElement();
        if (!"verify_response".equals(root.getNodeName()))
            throw new IOException("No valid response found [ " + response + "] ");

        return parseVerifyResult(root);
    }

    private static VerifyResult parseVerifyResult(Element root) throws IOException {
        String requestId = null;
        int status = -1;
        String errorText = null;

        NodeList fields = root.getChildNodes();
        for (int i = 0; i < fields.getLength(); i++) {
            Node node = fields.item(i);
            if (node.getNodeType() != Node.ELEMENT_NODE)
                continue;

            String name = node.getNodeName();
            if ("request_id".equals(name)) {
                requestId = node.getFirstChild() == null ? null : node.getFirstChild().getNodeValue();
            } else if ("status".equals(name)) {
                String str = node.getFirstChild() == null ? null : node.getFirstChild().getNodeValue();
                try {
                    if (str != null)
                        status = Integer.parseInt(str);
                } catch (NumberFormatException e) {
                    log.error("xml parser .. invalid value in <status> node [ " + str + " ] ");
                    status = BaseResult.STATUS_INTERNAL_ERROR;
                }
            } else if ("error_text".equals(name)) {
                errorText = node.getFirstChild() == null ? null : node.getFirstChild().getNodeValue();
            }
        }

        if (status == -1)
            throw new IOException("Xml Parser - did not find a <status> node");

        // Is this a temporary error ?
        boolean temporaryError = (status == BaseResult.STATUS_THROTTLED || status == BaseResult.STATUS_INTERNAL_ERROR);

        return new VerifyResult(status,
                                requestId,
                                errorText,
                                temporaryError);
    }

    public CheckResult check(final String requestId, final String code) throws IOException, SAXException {
        return check(requestId, code, null);
    }

    public CheckResult check(final String requestId, final String code, final String ipAddress) throws IOException, SAXException {
        if (requestId == null || code == null)
            throw new IllegalArgumentException("request ID and code parameters are mandatory.");

        log.debug("HTTP-Number-Verify-Check Client .. for [ " + requestId + " ] code [ " + code + " ] ");

        List<NameValuePair> params = new ArrayList<>();

        params.add(new BasicNameValuePair("api_key", this.apiKey));
        params.add(new BasicNameValuePair("api_secret", this.apiSecret));

        params.add(new BasicNameValuePair("request_id", requestId));
        params.add(new BasicNameValuePair("code", code));

        if (ipAddress != null)
            params.add(new BasicNameValuePair("ip_address", ipAddress));

        String verifyCheckBaseUrl = this.baseUrl + PATH_VERIFY_CHECK;

        // Now that we have generated a query string, we can instanciate a HttpClient,
        // construct a POST method and execute to submit the request
        String response = null;
        for (int pass=1;pass<=2;pass++) {
            HttpPost httpPost = new HttpPost(verifyCheckBaseUrl);
            httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            HttpUriRequest method = httpPost;
            String url = verifyCheckBaseUrl + "?" + URLEncodedUtils.format(params, "utf-8");

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
                return new CheckResult(BaseResult.STATUS_COMMS_FAILURE,
                        null,
                        0,
                        null,
                        "Failed to communicate with NEXMO-HTTP url [ " + url + " ] ..." + e,
                        true);
            }
        }

        Document doc;
        synchronized(this.documentBuilder) {
            doc = this.documentBuilder.parse(new InputSource(new StringReader(response)));
        }

        Element root = doc.getDocumentElement();
        if (!"verify_response".equals(root.getNodeName()))
            throw new IOException("No valid response found [ " + response + "] ");

        String eventId = null;
        int status = -1;
        float price = -1;
        String currency = null;
        String errorText = null;

        NodeList fields = root.getChildNodes();
        for (int i = 0; i < fields.getLength(); i++) {
            Node node = fields.item(i);
            if (node.getNodeType() != Node.ELEMENT_NODE)
                continue;

            String name = node.getNodeName();
            if ("event_id".equals(name)) {
                eventId = node.getFirstChild() == null ? null : node.getFirstChild().getNodeValue();
            } else if ("status".equals(name)) {
                String str = node.getFirstChild() == null ? null : node.getFirstChild().getNodeValue();
                try {
                    if (str != null)
                        status = Integer.parseInt(str);
                } catch (NumberFormatException e) {
                    log.error("xml parser .. invalid value in <status> node [ " + str + " ] ");
                    status = BaseResult.STATUS_INTERNAL_ERROR;
                }
            } else if ("price".equals(name)) {
                String str = node.getFirstChild() == null ? null : node.getFirstChild().getNodeValue();
                try {
                    if (str != null)
                        price = Float.parseFloat(str);
                } catch (NumberFormatException e) {
                    log.error("xml parser .. invalid value in <price> node [ " + str + " ] ");
                }
            } else if ("currency".equals(name)) {
                currency = node.getFirstChild() == null ? null : node.getFirstChild().getNodeValue();
            } else if ("error_text".equals(name)) {
                errorText = node.getFirstChild() == null ? null : node.getFirstChild().getNodeValue();
            }
        }

        if (status == -1)
            throw new IOException("Xml Parser - did not find a <status> node");

        // Is this a temporary error ?
        boolean temporaryError = (status == BaseResult.STATUS_THROTTLED || status == BaseResult.STATUS_INTERNAL_ERROR);

        return new CheckResult(status, eventId, price, currency, errorText, temporaryError);
    }

    public SearchResult search(String requestId) throws IOException, SAXException {
        SearchResult[] result = search(new String[] { requestId });
        return result != null && result.length > 0 ? result[0] : null;
    }

    public SearchResult[] search(String... requestIds) throws IOException, SAXException {
        if (requestIds == null || requestIds.length == 0)
            throw new IllegalArgumentException("request ID parameter is mandatory.");

        if (requestIds.length > MAX_SEARCH_REQUESTS)
            throw new IllegalArgumentException("too many request IDs. Max is " + MAX_SEARCH_REQUESTS);

        log.debug("HTTP-Number-Verify-Search Client .. for [ " + Arrays.toString(requestIds) + " ] ");

        List<NameValuePair> params = new ArrayList<>();

        params.add(new BasicNameValuePair("api_key", this.apiKey));
        params.add(new BasicNameValuePair("api_secret", this.apiSecret));

        if (requestIds.length == 1) {
            params.add(new BasicNameValuePair("request_id", requestIds[0]));
        } else {
            for (String requestId : requestIds)
                params.add(new BasicNameValuePair("request_ids", requestId));
        }

        String verifySearchBaseUrl = this.baseUrl + PATH_VERIFY_SEARCH;

        // Now that we have generated a query string, we can instanciate a HttpClient,
        // construct a POST method and execute to submit the request
        String response = null;
        for (int pass=1;pass<=2;pass++) {
            HttpPost httpPost = new HttpPost(verifySearchBaseUrl);
            httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            HttpUriRequest method = httpPost;
            String url = verifySearchBaseUrl + "?" + URLEncodedUtils.format(params, "utf-8");

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
                return new SearchResult[] {
                    new SearchResult(BaseResult.STATUS_COMMS_FAILURE,
                                     null,
                                     null,
                                     null,
                                     null,
                                     0, null,
                                     null,
                                     null, null,
                                     null, null,
                                     null,
                                     "Failed to communicate with NEXMO-HTTP url [ " + url + " ] ..." + e,
                                     true)
                };
            }
        }

        Document doc;
        synchronized(this.documentBuilder) {
            doc = this.documentBuilder.parse(new InputSource(new StringReader(response)));
        }

        Element root = doc.getDocumentElement();
        if ("verify_response".equals(root.getNodeName())) {
            // error response
            VerifyResult result = parseVerifyResult(root);
            return new SearchResult[] {
                new SearchResult(result.getStatus(),
                                 result.getRequestId(),
                                 null,
                                 null,
                                 null,
                                 0, null,
                                 null,
                                 null, null,
                                 null, null,
                                 null,
                                 result.getErrorText(),
                                 result.isTemporaryError())
            };
        } else if (("verify_request").equals(root.getNodeName())) {
            return new SearchResult[] { parseSearchResult(root) };
        } else if ("verification_requests".equals(root.getNodeName())) {
            List<SearchResult> results = new ArrayList<>();

            NodeList fields = root.getChildNodes();
            for (int i = 0; i < fields.getLength(); i++) {
                Node node = fields.item(i);
                if (node.getNodeType() != Node.ELEMENT_NODE)
                    continue;

                if ("verify_request".equals(node.getNodeName()))
                    results.add(parseSearchResult((Element) node));
            }

            return results.toArray(new SearchResult[results.size()]);
        } else {
            throw new IOException("No valid response found [ " + response + "] ");
        }
    }

    private static SearchResult parseSearchResult(Element root) throws IOException {
        String requestId = null;
        String accountId = null;
        String number = null;
        String senderId = null;
        Date dateSubmitted = null;
        Date dateFinalized = null;
        Date firstEventDate = null;
        Date lastEventDate = null;
        float price = -1;
        String currency = null;
        SearchResult.VerificationStatus status = null;
        List<SearchResult.VerifyCheck> checks = new ArrayList<>();
        String errorText = null;

        NodeList fields = root.getChildNodes();
        for (int i = 0; i < fields.getLength(); i++) {
            Node node = fields.item(i);
            if (node.getNodeType() != Node.ELEMENT_NODE)
                continue;

            String name = node.getNodeName();
            if ("request_id".equals(name)) {
                requestId = node.getFirstChild() == null ? null : node.getFirstChild().getNodeValue();
            } else if ("account_id".equals(name)) {
                accountId = node.getFirstChild() == null ? null : node.getFirstChild().getNodeValue();
            } else if ("status".equals(name)) {
                String str = node.getFirstChild() == null ? null : node.getFirstChild().getNodeValue();
                if (str != null) {
                    try {
                        status = SearchResult.VerificationStatus.valueOf(str.replace(' ', '_'));
                    } catch (IllegalArgumentException e) {
                        log.error("xml parser .. invalid value in <status> node [ " + str + " ] ");
                    }
                }
            } else if ("number".equals(name)) {
                number = node.getFirstChild() == null ? null : node.getFirstChild().getNodeValue();
            } else if ("price".equals(name)) {
                String str = node.getFirstChild() == null ? null : node.getFirstChild().getNodeValue();
                try {
                    if (str != null)
                        price = Float.parseFloat(str);
                } catch (NumberFormatException e) {
                    log.error("xml parser .. invalid value in <price> node [ " + str + " ] ");
                }
            } else if ("currency".equals(name)) {
                currency = node.getFirstChild() == null ? null : node.getFirstChild().getNodeValue();
            } else if ("sender_id".equals(name)) {
                senderId = node.getFirstChild() == null ? null : node.getFirstChild().getNodeValue();
            } else if ("date_submitted".equals(name)) {
                String str = node.getFirstChild() == null ? null : node.getFirstChild().getNodeValue();
                if (str != null) {
                    try {
                        dateSubmitted = parseDateTime(str);
                    } catch (ParseException e) {
                        log.error("xml parser .. invalid value in <date_submitted> node [ " + str + " ] ");
                    }
                }
            } else if ("date_finalized".equals(name)) {
                String str = node.getFirstChild() == null ? null : node.getFirstChild().getNodeValue();
                if (str != null) {
                    try {
                        dateFinalized = parseDateTime(str);
                    } catch (ParseException e) {
                        log.error("xml parser .. invalid value in <date_finalized> node [ " + str + " ] ");
                    }
                }
            } else if ("first_event_date".equals(name)) {
                String str = node.getFirstChild() == null ? null : node.getFirstChild().getNodeValue();
                if (str != null) {
                    try {
                        firstEventDate = parseDateTime(str);
                    } catch (ParseException e) {
                        log.error("xml parser .. invalid value in <first_event_date> node [ " + str + " ] ");
                    }
                }
            } else if ("last_event_date".equals(name)) {
                String str = node.getFirstChild() == null ? null : node.getFirstChild().getNodeValue();
                if (str != null) {
                    try {
                        lastEventDate = parseDateTime(str);
                    } catch (ParseException e) {
                        log.error("xml parser .. invalid value in <last_event_date> node [ " + str + " ] ");
                    }
                }
            } else if ("checks".equals(name)) {
                NodeList checkNodes = node.getChildNodes();
                for (int j = 0; j < checkNodes.getLength(); j++) {
                    Node checkNode = checkNodes.item(j);
                    if (checkNode.getNodeType() != Node.ELEMENT_NODE)
                        continue;

                    if ("check".equals(checkNode.getNodeName()))
                        checks.add(parseVerifyCheck((Element) checkNode));
                }
            } else if ("error_text".equals(name)) {
                errorText = node.getFirstChild() == null ? null : node.getFirstChild().getNodeValue();
            }
        }

        if (status == null)
            throw new IOException("Xml Parser - did not find a <status> node");

        return new SearchResult(BaseResult.STATUS_OK,
                                requestId,
                                accountId,
                                status,
                                number,
                                price, currency,
                                senderId,
                                dateSubmitted, dateFinalized,
                                firstEventDate, lastEventDate,
                                checks,
                                errorText, false);
    }

    private static SearchResult.VerifyCheck parseVerifyCheck(Element root) throws IOException {
        String code = null;
        SearchResult.VerifyCheck.Status status = null;
        Date dateReceived = null;
        String ipAddress = null;

        NodeList fields = root.getChildNodes();
        for (int i = 0; i < fields.getLength(); i++) {
            Node node = fields.item(i);
            if (node.getNodeType() != Node.ELEMENT_NODE)
                continue;

            String name = node.getNodeName();
            if ("code".equals(name)) {
                code = node.getFirstChild() == null ? null : node.getFirstChild().getNodeValue();
            } else if ("status".equals(name)) {
                String str = node.getFirstChild() == null ? null : node.getFirstChild().getNodeValue();
                if (str != null) {
                    try {
                        status = SearchResult.VerifyCheck.Status.valueOf(str);
                    } catch (IllegalArgumentException e) {
                        log.error("xml parser .. invalid value in <status> node [ " + str + " ] ");
                    }
                }
            } else if ("ip_address".equals(name)) {
                ipAddress = node.getFirstChild() == null ? null : node.getFirstChild().getNodeValue();
            } else if ("date_received".equals(name)) {
                String str = node.getFirstChild() == null ? null : node.getFirstChild().getNodeValue();
                if (str != null) {
                    try {
                        dateReceived = parseDateTime(str);
                    } catch (ParseException e) {
                        log.error("xml parser .. invalid value in <date_received> node [ " + str + " ] ");
                    }
                }
            }
        }

        if (status == null)
            throw new IOException("Xml Parser - did not find a <status> node");

        return new SearchResult.VerifyCheck(dateReceived, code, status, ipAddress);
    }

    private static Date parseDateTime(String str) throws ParseException {
        return sDateTimePattern.get().parse(str);
    }

}
