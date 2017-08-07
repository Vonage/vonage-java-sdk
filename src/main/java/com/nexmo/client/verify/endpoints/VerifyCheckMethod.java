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
import com.nexmo.client.verify.CheckResult;
import com.nexmo.client.verify.CheckRequest;
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

public class VerifyCheckMethod extends AbstractMethod<CheckRequest, CheckResult> {
    private static final Log log = LogFactory.getLog(VerifyCheckMethod.class);
    private static final Class[] ALLOWED_AUTH_METHODS = new Class[]{TokenAuthMethod.class};

    private static final String DEFAULT_URI = "https://api.nexmo.com/verify/check/xml";

    private XmlParser xmlParser = new XmlParser();
    private String uri = DEFAULT_URI;

    public VerifyCheckMethod(HttpWrapper httpWrapper) {
        super(httpWrapper);
    }

    public VerifyCheckMethod(HttpWrapper httpWrapper, String baseUri) {
        super(httpWrapper);
        uri = baseUri + "/verify/check/xml";
    }

    @Override
    protected Class[] getAcceptableAuthMethods() {
        return ALLOWED_AUTH_METHODS;
    }

    @Override
    public RequestBuilder makeRequest(CheckRequest request) throws NexmoClientException, UnsupportedEncodingException {
        if (request.getRequestId() == null || request.getCode() == null)
            throw new IllegalArgumentException("request ID and code parameters are mandatory.");

        RequestBuilder result = RequestBuilder.post(this.uri)
                .addParameter("request_id", request.getRequestId())

                .addParameter("code", request.getCode());
        if (request.getIpAddress() != null)
            result.addParameter("ip_address", request.getIpAddress());

        return result;
    }

    @Override
    public CheckResult parseResponse(HttpResponse response) throws IOException {
        return parseCheckResponse(EntityUtils.toString(response.getEntity()));
    }

    private CheckResult parseCheckResponse(String response) throws NexmoResponseParseException {
        Document doc = xmlParser.parseXml(response);

        Element root = doc.getDocumentElement();
        if (!"verify_response".equals(root.getNodeName()))
            throw new NexmoResponseParseException("No valid response found [ " + response + "] ");

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
                eventId = XmlUtil.stringValue(node);
            } else if ("status".equals(name)) {
                String str = XmlUtil.stringValue(node);
                try {
                    if (str != null)
                        status = Integer.parseInt(str);
                } catch (NumberFormatException e) {
                    log.error("xml parser .. invalid value in <status> node [ " + str + " ] ");
                    status = BaseResult.STATUS_INTERNAL_ERROR;
                }
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
            } else if ("error_text".equals(name)) {
                errorText = XmlUtil.stringValue(node);
            }
        }

        if (status == -1)
            throw new NexmoResponseParseException("Xml Parser - did not find a <status> node");

        // Is this a temporary error ?
        boolean temporaryError = (status == BaseResult.STATUS_THROTTLED || status == BaseResult.STATUS_INTERNAL_ERROR);

        return new CheckResult(status, eventId, price, currency, errorText, temporaryError);
    }
}
