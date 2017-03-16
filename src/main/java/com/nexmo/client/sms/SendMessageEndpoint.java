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
package com.nexmo.client.sms;

import com.nexmo.client.HttpWrapper;
import com.nexmo.client.NexmoClientException;
import com.nexmo.client.NexmoResponseParseException;
import com.nexmo.client.auth.TokenAuthMethod;
import com.nexmo.client.legacyutils.XmlParser;
import com.nexmo.client.sms.messages.Message;
import com.nexmo.client.voice.endpoints.AbstractMethod;
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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class SendMessageEndpoint extends AbstractMethod<Message, SmsSubmissionResult[]> {
    private static final Log log = LogFactory.getLog(SmsClient.class);

    private static final Class[] ALLOWED_AUTH_METHODS = new Class[]{TokenAuthMethod.class};

    private static final String DEFAULT_URI = "https://rest.nexmo.com/sms/xml";

    private XmlParser xmlParser = new XmlParser();
    private String uri = DEFAULT_URI;

    public SendMessageEndpoint(HttpWrapper httpWrapper) {
        super(httpWrapper);
    }

    @Override
    protected Class[] getAcceptableAuthMethods() {
        return ALLOWED_AUTH_METHODS;
    }

    @Override
    public RequestBuilder makeRequest(Message message) throws NexmoClientException, UnsupportedEncodingException {
        RequestBuilder request = RequestBuilder.post(uri);
        message.addParams(request);
        return request;
    }

    @Override
    public SmsSubmissionResult[] parseResponse(HttpResponse response) throws IOException {
        return parseResponse(new BasicResponseHandler().handleResponse(response));
    }

    protected SmsSubmissionResult[] parseResponse(String response) throws NexmoResponseParseException {
        // parse the response doc ...

        /*
            We receive a response from the api that looks like this, parse the document
            and turn it into an array of SmsSubmissionResult, one object per <message> node

                <mt-submission-response>
                    <messages count='x'>
                        <message>
                            <to>xxx</to>
                            <messageId>xxx</messageId>
                            <status>xx</status>
                            <errorText>ff</errorText>
                            <clientRef>xxx</clientRef>
                            <remainingBalance>##.##</remainingBalance>
                            <messagePrice>##.##</messagePrice>
                            <reachability status='x' description='xxx' />
                            <network>23410</network>
                        </message>
                    </messages>
                </mt-submission-response>
        */

        List<SmsSubmissionResult> results = new ArrayList<>();

        Document doc = xmlParser.parseXml(response);

        NodeList replies = doc.getElementsByTagName("mt-submission-response");
        for (int i = 0; i < replies.getLength(); i++) {
            Node reply = replies.item(i);
            NodeList messageLists = reply.getChildNodes();
            for (int i2 = 0; i2 < messageLists.getLength(); i2++) {
                Node messagesNode = messageLists.item(i2);
                if (messagesNode.getNodeType() != Node.ELEMENT_NODE)
                    continue;
                if (messagesNode.getNodeName().equals("messages")) {
                    NodeList messages = messagesNode.getChildNodes();
                    for (int i3 = 0; i3 < messages.getLength(); i3++) {
                        SmsSubmissionResult message = parseMessageXmlNode(messages.item(i3));
                        if (message != null) {
                            results.add(message);
                        }
                    }
                }
            }
        }

        return results.toArray(new SmsSubmissionResult[results.size()]);
    }

    private SmsSubmissionResult parseMessageXmlNode(Node messageNode)
            throws NexmoResponseParseException {
        if (messageNode.getNodeType() != Node.ELEMENT_NODE)
            return null;

        int status = -1;
        String messageId = null;
        String destination = null;
        String errorText = null;
        String clientReference = null;
        BigDecimal remainingBalance = null;
        BigDecimal messagePrice = null;
        SmsSubmissionReachabilityStatus smsSubmissionReachabilityStatus = null;
        String network = null;

        NodeList nodes = messageNode.getChildNodes();
        for (int i4 = 0; i4 < nodes.getLength(); i4++) {
            Node node = nodes.item(i4);
            if (node.getNodeType() != Node.ELEMENT_NODE)
                continue;
            if (node.getNodeName().equals("messageId")) {
                messageId = node.getFirstChild() == null ? null : node.getFirstChild().getNodeValue();
            } else if (node.getNodeName().equals("to")) {
                destination = node.getFirstChild() == null ? null : node.getFirstChild().getNodeValue();
            } else if (node.getNodeName().equals("status")) {
                String str = node.getFirstChild() == null ? null : node.getFirstChild().getNodeValue();
                try {
                    status = Integer.parseInt(str);
                } catch (NumberFormatException e) {
                    log.error("xml parser .. invalid value in <status> node [ " + str + " ] ");
                    status = SmsSubmissionResult.STATUS_INTERNAL_ERROR;
                }
            } else if (node.getNodeName().equals("errorText")) {
                errorText = node.getFirstChild() == null ? null : node.getFirstChild().getNodeValue();
            } else if (node.getNodeName().equals("clientRef")) {
                clientReference = node.getFirstChild() == null ? null : node.getFirstChild().getNodeValue();
            } else if (node.getNodeName().equals("remainingBalance")) {
                String str = node.getFirstChild() == null ? null : node.getFirstChild().getNodeValue();
                try {
                    if (str != null)
                        remainingBalance = new BigDecimal(str);
                } catch (NumberFormatException e) {
                    log.error("xml parser .. invalid value in <remainingBalance> node [ " + str + " ] ");
                }
            } else if (node.getNodeName().equals("messagePrice")) {
                String str = node.getFirstChild() == null ? null : node.getFirstChild().getNodeValue();
                try {
                    if (str != null)
                        messagePrice = new BigDecimal(str);
                } catch (NumberFormatException e) {
                    log.error("xml parser .. invalid value in <messagePrice> node [ " + str + " ] ");
                }
            } else if (node.getNodeName().equals("network")) {
                network = node.getFirstChild() == null ? null : node.getFirstChild().getNodeValue();
            } else {
                log.error("xml parser .. unknown node found in status-return, expected [ messageId, to, status, errorText, clientRef, messagePrice, remainingBalance, reachability, network ] -- found [ " + node.getNodeName() + " ] ");
            }
        }

        if (status == -1)
            throw new NexmoResponseParseException("Xml Parser - did not find a <status> node");

        // Is this a temporary error ?
        boolean temporaryError = (status == SmsSubmissionResult.STATUS_THROTTLED ||
                status == SmsSubmissionResult.STATUS_INTERNAL_ERROR ||
                status == SmsSubmissionResult.STATUS_TOO_MANY_BINDS);

        return new SmsSubmissionResult(status,
                destination,
                messageId,
                errorText,
                clientReference,
                remainingBalance,
                messagePrice,
                temporaryError,
                smsSubmissionReachabilityStatus,
                network);
    }
}
