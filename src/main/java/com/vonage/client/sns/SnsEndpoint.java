/*
 *   Copyright 2020 Vonage
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package com.vonage.client.sns;

import com.vonage.client.AbstractMethod;
import com.vonage.client.HttpWrapper;
import com.vonage.client.VonageResponseParseException;
import com.vonage.client.auth.SignatureAuthMethod;
import com.vonage.client.auth.TokenAuthMethod;
import com.vonage.client.legacyutils.XmlParser;
import com.vonage.client.legacyutils.XmlUtil;
import com.vonage.client.sns.request.SnsRequest;
import com.vonage.client.sns.response.SnsPublishResponse;
import com.vonage.client.sns.response.SnsResponse;
import com.vonage.client.sns.response.SnsSubscribeResponse;
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

class SnsEndpoint extends AbstractMethod<SnsRequest, SnsResponse> {
    private static final Log log = LogFactory.getLog(SnsClient.class);

    private static final Class[] ALLOWED_AUTH_METHODS = new Class[]{SignatureAuthMethod.class, TokenAuthMethod.class};

    private static final String PATH = "/sns/xml";

    private XmlParser xmlParser = new XmlParser();

    SnsEndpoint(HttpWrapper httpWrapper) {
        super(httpWrapper);
    }

    @Override
    protected Class[] getAcceptableAuthMethods() {
        return ALLOWED_AUTH_METHODS;
    }

    @Override
    public RequestBuilder makeRequest(SnsRequest snsRequest) throws UnsupportedEncodingException {
        RequestBuilder requestBuilder = RequestBuilder
                .post(httpWrapper.getHttpConfig().getSnsBaseUri() + PATH)
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
                        log.error("xml parser .. invalid value in <resultCode> node [ " + XmlUtil.stringValue(node)
                                + " ] ");
                        resultCode = SnsResponse.STATUS_INTERNAL_ERROR;
                    }
                } else if (node.getNodeName().equals("resultMessage")) {
                    resultMessage = XmlUtil.stringValue(node);
                } else if (node.getNodeName().equals("transactionId")) {
                    transactionId = XmlUtil.stringValue(node);
                } else if (node.getNodeName().equals("subscriberArn")) {
                    subscriberArn = XmlUtil.stringValue(node);
                } else log.error("xml parser .. unknown node found in nexmo-sns [ " + node.getNodeName() + " ] ");
            }
        }

        if (resultCode == -1) {
            throw new VonageResponseParseException("Xml Parser - did not find a <resultCode> node");
        }

        if ("publish".equals(command)) {
            return new SnsPublishResponse(resultCode, resultMessage, transactionId);
        } else if ("subscribe".equals(command)) {
            return new SnsSubscribeResponse(resultCode, resultMessage, subscriberArn);
        } else {
            throw new VonageResponseParseException("Unknown command value: " + command);
        }
    }
}
