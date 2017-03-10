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
package com.nexmo.client.sns;

import com.nexmo.client.HttpWrapper;
import com.nexmo.client.NexmoClientException;
import com.nexmo.client.NexmoResponseParseException;
import com.nexmo.client.auth.TokenAuthMethod;
import com.nexmo.client.legacyutils.XmlParser;
import com.nexmo.client.sns.request.SnsRequest;
import com.nexmo.client.sns.response.SnsPublishResponse;
import com.nexmo.client.sns.response.SnsResponse;
import com.nexmo.client.sns.response.SnsSubscribeResponse;
import com.nexmo.client.voice.endpoints.AbstractMethod;
import com.nexmo.client.legacyutils.XmlUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.BasicResponseHandler;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

public class SnsEndpoint extends AbstractMethod<SnsRequest, SnsResponse> {
    private static final Log log = LogFactory.getLog(SnsClient.class);

    private static final Class[] ALLOWED_AUTH_METHODS = new Class[]{TokenAuthMethod.class};

    private static final String DEFAULT_BASE_URL = "https://sns.nexmo.com/sns/xml";

    private String uri = DEFAULT_BASE_URL;

    private XmlParser xmlParser = new XmlParser();

    public SnsEndpoint(HttpWrapper httpWrapper) {
        super(httpWrapper);
    }

    @Override
    protected Class[] getAcceptableAuthMethods() {
        return ALLOWED_AUTH_METHODS;
    }

    @Override
    public RequestBuilder makeRequest(SnsRequest snsRequest) throws NexmoClientException, UnsupportedEncodingException {
        RequestBuilder requestBuilder = RequestBuilder
                .post(this.uri)
                .addParameter("cmd", snsRequest.getCommand());
        for (Map.Entry<String, String> entry : snsRequest.getQueryParameters().entrySet()) {
            requestBuilder.addParameter(entry.getKey(), entry.getValue());
        }
        return requestBuilder;
    }

    @Override
    public SnsResponse parseResponse(HttpResponse response) throws IOException {
        return parseSubmitResponse(new BasicResponseHandler().handleResponse(response));
    }

    protected SnsResponse parseSubmitResponse(String response) {
        // parse the response doc ...

        /*
            We receive a response from the api that looks like this, parse the document
            and turn it into an instance of SnsResponse

                <nexmo-sns>
                    <command>subscribe|publish</command>
                    <resultCode>0</resultCode>
                    <resultMessage>OK!</resultMessage>
                    <transactionId>${transaction-id}</transactionId>
                    <subscriberArn>${subscriber}</subscriberArn>
                </nexmo-sns>

        */

        Document doc = xmlParser.parseXml(response);

        String command = null;
        int resultCode = -1;
        String resultMessage = null;
        String transactionId = null;
        String subscriberArn = null;

        NodeList replies = doc.getElementsByTagName("nexmo-sns");
        Node reply = replies.item(0);   // If there's more than one reply, we ignore the extras.
        NodeList nodes = reply.getChildNodes();

        for (int i2 = 0; i2 < nodes.getLength(); i2++) {
            Node node = nodes.item(i2);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                if (node.getNodeName().equals("command")) {
                    command = XmlUtil.stringValue(node);
                } else if (node.getNodeName().equals("resultCode")) {
                    try {
                        resultCode = XmlUtil.intValue(node);
                    } catch (Exception e) {
                        log.error("xml parser .. invalid value in <resultCode> node [ " + XmlUtil.stringValue(node) +
                                " ] ");
                        resultCode = SnsResponse.STATUS_INTERNAL_ERROR;
                    }
                } else if (node.getNodeName().equals("resultMessage")) {
                    resultMessage = XmlUtil.stringValue(node);
                } else if (node.getNodeName().equals("transactionId")) {
                    transactionId = XmlUtil.stringValue(node);
                } else if (node.getNodeName().equals("subscriberArn")) {
                    subscriberArn = XmlUtil.stringValue(node);
                } else
                    log.error("xml parser .. unknown node found in nexmo-sns [ " + node.getNodeName() + " ] ");
            }
        }

        if (resultCode == -1) {
            throw new NexmoResponseParseException("Xml Parser - did not find a <resultCode> node");
        }

        if (command.equals("publish")) {
            return new SnsPublishResponse(resultCode, resultMessage, transactionId);
        } else if (command.equals("subscribe")) {
            return new SnsSubscribeResponse(resultCode, resultMessage, subscriberArn);
        } else {
            throw new NexmoResponseParseException("Unknown command value: " + command);
        }
    }
}
