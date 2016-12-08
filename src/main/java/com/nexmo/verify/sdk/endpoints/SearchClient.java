package com.nexmo.verify.sdk.endpoints;/*
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

import com.nexmo.common.LegacyClient;
import com.nexmo.client.NexmoResponseParseException;
import com.nexmo.common.util.XmlUtil;
import com.nexmo.verify.sdk.BaseResult;
import com.nexmo.verify.sdk.SearchResult;
import com.nexmo.verify.sdk.VerifyResult;
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
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class SearchClient extends LegacyClient {
    private static final Log log = LogFactory.getLog(SearchClient.class);

    /**
     * The endpoint path for submitting verification search requests
     */
    private static final String PATH_VERIFY_SEARCH = "/verify/search/xml";

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

    /**
     * Create a new SearchClient.
     * <p>
     * This client is used for calling the verify API's search endpoint.
     *
     * @param baseUrl The base URL to be used instead of <code>DEFAULT_BASE_URL</code>
     * @param apiKey Your Nexmo account api key
     * @param apiSecret Your Nexmo account api secret
     * @param connectionTimeout over-ride the default connection timeout with this value (in milliseconds)
     * @param soTimeout over-ride the default read-timeout with this value (in milliseconds)
     */
    public SearchClient(final String baseUrl,
                       final String apiKey,
                       final String apiSecret,
                       final int connectionTimeout,
                       final int soTimeout) {

        super(baseUrl, apiKey, apiSecret, connectionTimeout, soTimeout);
    }

    public SearchResult search(String requestId) throws IOException, NexmoResponseParseException {
        SearchResult[] result = search(new String[] { requestId });
        return result != null && result.length > 0 ? result[0] : null;
    }

    public SearchResult[] search(String... requestIds) throws IOException, NexmoResponseParseException {
        if (requestIds == null || requestIds.length == 0)
            throw new IllegalArgumentException("request ID parameter is mandatory.");

        if (requestIds.length > MAX_SEARCH_REQUESTS)
            throw new IllegalArgumentException("too many request IDs. Max is " + MAX_SEARCH_REQUESTS);

        log.debug("HTTP-Number-Verify-Search Client .. for [ " + Arrays.toString(requestIds) + " ] ");

        List<NameValuePair> params = constructSearchParams(requestIds);

        String verifySearchBaseUrl = this.makeUrl(PATH_VERIFY_SEARCH);

        // Now that we have generated a query string, we can instantiate a HttpClient,
        // construct a POST method and execute to submit the request
        String response;

        HttpPost httpPost = new HttpPost(verifySearchBaseUrl);
        httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
        String url = verifySearchBaseUrl + "?" + URLEncodedUtils.format(params, "utf-8");
        try {
            HttpResponse httpResponse = this.getHttpClient().execute(httpPost);
            response = new BasicResponseHandler().handleResponse(httpResponse);
            log.info(".. SUBMITTED NEXMO-HTTP URL [ " + url + " ] -- response [ " + response + " ] ");
        } catch (HttpResponseException e) {
            httpPost.abort();
            log.error("communication failure", e);

            // return a COMMS failure ...
            return new SearchResult[]{
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

        return parseSearchResponse(response);
    }

    protected SearchResult[] parseSearchResponse(String response) throws NexmoResponseParseException {
        Document doc = parseXml(response);

        Element root = doc.getDocumentElement();
        if ("verify_response".equals(root.getNodeName())) {
            // error response
            VerifyResult result = SharedParsers.parseVerifyResponseXmlNode(root);
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
            return new SearchResult[] { parseVerifyRequestXmlNode(root) };
        } else if ("verification_requests".equals(root.getNodeName())) {
            List<SearchResult> results = new ArrayList<SearchResult>();

            NodeList fields = root.getChildNodes();
            for (int i = 0; i < fields.getLength(); i++) {
                Node node = fields.item(i);
                if (node.getNodeType() != Node.ELEMENT_NODE)
                    continue;

                if ("verify_request".equals(node.getNodeName()))
                    results.add(parseVerifyRequestXmlNode((Element) node));
            }

            return results.toArray(new SearchResult[results.size()]);
        } else {
            throw new NexmoResponseParseException("No valid response found [ " + response + "] ");
        }
    }

    protected static SearchResult parseVerifyRequestXmlNode(Element root) throws NexmoResponseParseException {
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
        List<SearchResult.VerifyCheck> checks = new ArrayList<SearchResult.VerifyCheck>();
        String errorText = null;

        NodeList fields = root.getChildNodes();
        for (int i = 0; i < fields.getLength(); i++) {
            Node node = fields.item(i);
            if (node.getNodeType() != Node.ELEMENT_NODE)
                continue;

            String name = node.getNodeName();
            if ("request_id".equals(name)) {
                requestId = XmlUtil.stringValue(node);
            } else if ("account_id".equals(name)) {
                accountId = XmlUtil.stringValue(node);
            } else if ("status".equals(name)) {
                String str = XmlUtil.stringValue(node);
                if (str != null) {
                    try {
                        status = SearchResult.VerificationStatus.valueOf(str.replace(' ', '_'));
                    } catch (IllegalArgumentException e) {
                        log.error("xml parser .. invalid value in <status> node [ " + str + " ] ");
                    }
                }
            } else if ("number".equals(name)) {
                number = XmlUtil.stringValue(node);
            } else if ("price".equals(name)) {
                String str = XmlUtil.stringValue(node);
                try {
                    if (str != null)
                        price = Float.parseFloat(str);
                } catch (NumberFormatException e) {
                    log.error("xml parser .. invalid value in <price> node [ " + str + " ] ");
                }
            } else if ("currency".equals(name)) {
                currency = XmlUtil.stringValue(node);
            } else if ("sender_id".equals(name)) {
                senderId = XmlUtil.stringValue(node);
            } else if ("date_submitted".equals(name)) {
                String str = XmlUtil.stringValue(node);
                if (str != null) {
                    try {
                        dateSubmitted = parseDateTime(str);
                    } catch (ParseException e) {
                        log.error("xml parser .. invalid value in <date_submitted> node [ " + str + " ] ");
                    }
                }
            } else if ("date_finalized".equals(name)) {
                String str = XmlUtil.stringValue(node);
                if (str != null) {
                    try {
                        dateFinalized = parseDateTime(str);
                    } catch (ParseException e) {
                        log.error("xml parser .. invalid value in <date_finalized> node [ " + str + " ] ");
                    }
                }
            } else if ("first_event_date".equals(name)) {
                String str = XmlUtil.stringValue(node);
                if (str != null) {
                    try {
                        firstEventDate = parseDateTime(str);
                    } catch (ParseException e) {
                        log.error("xml parser .. invalid value in <first_event_date> node [ " + str + " ] ");
                    }
                }
            } else if ("last_event_date".equals(name)) {
                String str = XmlUtil.stringValue(node);
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
                        checks.add(parseCheckXmlNode((Element) checkNode));
                }
            } else if ("error_text".equals(name)) {
                errorText = XmlUtil.stringValue(node);
            }
        }

        if (status == null)
            throw new NexmoResponseParseException("Xml Parser - did not find a <status> node");

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

    protected static SearchResult.VerifyCheck parseCheckXmlNode(Element root) throws NexmoResponseParseException {
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
                code = XmlUtil.stringValue(node);
            } else if ("status".equals(name)) {
                String str = XmlUtil.stringValue(node);
                if (str != null) {
                    try {
                        status = SearchResult.VerifyCheck.Status.valueOf(str);
                    } catch (IllegalArgumentException e) {
                        log.error("xml parser .. invalid value in <status> node [ " + str + " ] ");
                    }
                }
            } else if ("ip_address".equals(name)) {
                ipAddress = XmlUtil.stringValue(node);
            } else if ("date_received".equals(name)) {
                String str = XmlUtil.stringValue(node);
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
            throw new NexmoResponseParseException("Xml Parser - did not find a <status> node");

        return new SearchResult.VerifyCheck(dateReceived, code, status, ipAddress);
    }

    private static Date parseDateTime(String str) throws ParseException {
        return sDateTimePattern.get().parse(str);
    }

    protected List<NameValuePair> constructSearchParams(String[] requestIds) {
        List<NameValuePair> params = this.constructParams();

        if (requestIds.length == 1) {
            params.add(new BasicNameValuePair("request_id", requestIds[0]));
        } else {
            for (String requestId : requestIds)
                params.add(new BasicNameValuePair("request_ids", requestId));
        }
        return params;
    }
}
