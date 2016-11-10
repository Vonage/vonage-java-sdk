package com.nexmo.sns.sdk;
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

import java.util.List;
import java.util.Map;

import com.nexmo.client.NexmoResponseParseException;
import com.nexmo.common.LegacyClient;
import com.nexmo.common.util.XmlUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.nexmo.sns.sdk.request.Request;
import com.nexmo.sns.sdk.response.SnsServiceResult;

/**
 * Client for talking to the Nexmo REST interface.
 *
 * @author  Paul Cook
 */
public class NexmoSnsClient extends LegacyClient {

    private static final Log log = LogFactory.getLog(NexmoSnsClient.class);

    /**
     * Submission url used unless over-ridden on the constructor.
     */
    private static final String DEFAULT_BASE_URL = "https://sns.nexmo.com/sns/xml";

    /**
     * Default connection timeout of 5000ms used by this client unless specifically overridden on the constructor.
     */
    private static final int DEFAULT_CONNECTION_TIMEOUT = 5000;

    /**
     * Default read timeout of 30000ms used by this client unless specifically overridden on the constructor.
     */
    private static final int DEFAULT_SO_TIMEOUT = 30000;

    /**
     * Constructs a NexmoSnsClient with the default base URL, a connection timeout of 5000ms and a read timeout of 30s.
     *
     * @param apiKey Your Nexmo account api key
     * @param apiSecret Your Nexmo account api secret
     *
     * @throws Exception If the XML parser could not be configured.
     */
    public NexmoSnsClient(final String apiKey,
                          final String apiSecret) throws Exception {
        this(DEFAULT_BASE_URL,
             apiKey,
             apiSecret,
             DEFAULT_CONNECTION_TIMEOUT,
             DEFAULT_SO_TIMEOUT);
    }

    /**
     * Constructs a NexmoSnsClient with the default base URL.
     *
     * @param apiKey Your Nexmo account api key
     * @param apiSecret Your Nexmo account api secret
     * @param connectionTimeout over-ride the default connection timeout with this value (in milliseconds)
     * @param soTimeout over-ride the default read-timeout with this value (in milliseconds)
     *
     * @throws Exception If the XML parser could not be configured.
     */
    public NexmoSnsClient(final String apiKey,
                          final String apiSecret,
                          final int connectionTimeout,
                          final int soTimeout) throws Exception {
        this(DEFAULT_BASE_URL,
             apiKey,
             apiSecret,
             connectionTimeout,
             soTimeout);
    }

    /**
     * Instantiate a new NexmoSnsClient instance that will communicate using the supplied credentials, and will use the supplied connection and read timeout values.<br>
     * Additionally, you can specify an alternative service base url. For example submitting to a testing sandbox environment,
     * or if requested to submit to an alternative address by Nexmo, for example, in cases where it may be necessary to prioritize your traffic.
     *
     * @param baseUrl The URL to be used instead of <pre>https://sns.nexmo.com/sns/xml</pre>
     * @param apiKey Your Nexmo account api key
     * @param apiSecret Your Nexmo account api secret
     * @param connectionTimeout over-ride the default connection timeout with this value (in milliseconds)
     * @param soTimeout over-ride the default read-timeout with this value (in milliseconds)
     */
    public NexmoSnsClient(final String baseUrl,
                          final String apiKey,
                          final String apiSecret,
                          final int connectionTimeout,
                          final int soTimeout) {

        super(baseUrl, apiKey, apiSecret, connectionTimeout, soTimeout, false, null);
    }

    public SnsServiceResult submit(final Request request) throws Exception {
        log.info("NEXMO-REST-SNS-SERVICE-CLIENT ... submit request [ " + request.toString() + " ] ");
        List<NameValuePair> params = constructSubmitParams(request);

        // Now that we have generated a query string, we can instantiate a HttpClient,
        // construct a POST or GET method and execute to submit the request
        String response;
        HttpPost method = new HttpPost(makeUrl(null));
        method.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
        try {
            HttpResponse httpResponse = this.getHttpClient().execute(method);
            response = new BasicResponseHandler().handleResponse(httpResponse);
            log.info(".. SUBMITTED NEXMO-HTTP URL [ " + method.toString() + " ] -- response [ " + response + " ] ");
        } catch (Exception e) {
            log.info("communication failure: " + e);
            method.abort();
            return new SnsServiceResult(request.getCommand(),
                                        SnsServiceResult.STATUS_COMMS_FAILURE,
                                        "Failed to communicate with NEXMO-HTTP url [ " + method.toString() + " ] ..." + e,
                                        null,
                                        null);
        }

        return parseSubmitResponse(response);
    }

    protected SnsServiceResult parseSubmitResponse(String response) throws Exception {
        // parse the response doc ...

        /*
            We receive a response from the api that looks like this, parse the document
            and turn it into an instance of SnsServiceResult

                <nexmo-sns>
                    <command>subscribe|publish</command>
                    <resultCode>0</resultCode>
                    <resultMessage>OK!</resultMessage>
                    <transactionId>${transaction-id}</transactionId>
                    <subscriberArn>${subscriber}</subscriberArn>
                </nexmo-sns>

        */

        Document doc = parseXml(response);

        String command = null;
        int resultCode = -1;
        String resultMessage = null;
        String transactionId = null;
        String subscriberArn = null;

        NodeList replies = doc.getElementsByTagName("nexmo-sns");
        Node reply = replies.item(0);   // If there's more than one reply, we ignore the extras.
        NodeList nodes = reply.getChildNodes();

        for (int i2=0;i2<nodes.getLength();i2++) {
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
                        resultCode = SnsServiceResult.STATUS_INTERNAL_ERROR;
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

        if (resultCode == -1)
            throw new NexmoResponseParseException("Xml Parser - did not find a <resultCode> node");

        return new SnsServiceResult(command,
                                    resultCode,
                                    resultMessage,
                                    transactionId,
                                    subscriberArn);
    }

    private List<NameValuePair> constructSubmitParams(Request request) {
        // Construct a query string as a list of NameValuePairs
        List<NameValuePair> params = constructParams();
        params.add(new BasicNameValuePair("cmd", request.getCommand()));
        if (request.getQueryParameters() != null)
            for (Map.Entry<String, String> entry: request.getQueryParameters().entrySet())
                params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        return params;
    }
}
