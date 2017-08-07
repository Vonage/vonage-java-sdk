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

import com.nexmo.client.HttpWrapper;
import com.nexmo.client.NexmoClientException;
import com.nexmo.client.NexmoResponseParseException;
import com.nexmo.client.auth.TokenAuthMethod;
import com.nexmo.client.legacyutils.XmlParser;
import com.nexmo.client.verify.BaseResult;
import com.nexmo.client.verify.SearchRequest;
import com.nexmo.client.verify.SearchResult;
import com.nexmo.client.verify.VerifyResult;
import com.nexmo.client.voice.endpoints.AbstractMethod;
import com.nexmo.client.legacyutils.XmlUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SearchEndpoint extends AbstractMethod<SearchRequest, SearchResult[]> {
    private static final Log log = LogFactory.getLog(SearchEndpoint.class);

    private static final Class[] ALLOWED_AUTH_METHODS = new Class[]{TokenAuthMethod.class};

    private static final String DEFAULT_URI = "https://api.nexmo.com/verify/search/xml";

    private XmlParser xmlParser = new XmlParser();

    private String uri = DEFAULT_URI;

    private static final ThreadLocal<SimpleDateFormat> sDateTimePattern = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            sdf.setLenient(false);

            return sdf;
        }
    };

    /**
     * Create a new SearchEndpoint.
     * <p>
     * This client is used for calling the verify API's search endpoint.
     */
    public SearchEndpoint(HttpWrapper httpWrapper) {
        super(httpWrapper);
    }

    public SearchEndpoint(HttpWrapper httpWrapper, String baseUri) {
        super(httpWrapper);
        uri = baseUri + "/verify/search/xml";
    }

    public String getUri() {
        return uri;
    }

    @Override
    protected Class[] getAcceptableAuthMethods() {
        return ALLOWED_AUTH_METHODS;
    }

    @Override
    public RequestBuilder makeRequest(SearchRequest request) throws NexmoClientException, UnsupportedEncodingException {
        RequestBuilder result = RequestBuilder.post(this.uri);
        if (request.getRequestIds().length == 1) {
            result.addParameter("request_id", request.getRequestIds()[0]);
        } else {
            for (String requestId : request.getRequestIds())
                result.addParameter("request_ids", requestId);
        }
        return result;
    }

    @Override
    public SearchResult[] parseResponse(HttpResponse response) throws IOException {
        return parseSearchResponse(EntityUtils.toString(response.getEntity()));
    }

    public SearchResult search(String requestId) throws IOException, NexmoClientException {
        SearchResult[] result = search(new String[]{requestId});
        return result != null && result.length > 0 ? result[0] : null;
    }

    public SearchResult[] search(String... requestIds) throws IOException, NexmoClientException {
        return this.execute(new SearchRequest(requestIds));
    }

    protected SearchResult[] parseSearchResponse(String response) throws NexmoResponseParseException {
        Document doc = xmlParser.parseXml(response);

        Element root = doc.getDocumentElement();
        if ("verify_response".equals(root.getNodeName())) {
            // error response
            VerifyResult result = SharedParsers.parseVerifyResponseXmlNode(root);
            return new SearchResult[]{
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
            return new SearchResult[]{parseVerifyRequestXmlNode(root)};
        } else if ("verification_requests".equals(root.getNodeName())) {
            List<SearchResult> results = new ArrayList<>();

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
        List<SearchResult.VerifyCheck> checks = new ArrayList<>();
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

}
