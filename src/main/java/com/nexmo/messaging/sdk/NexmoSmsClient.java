package com.nexmo.messaging.sdk;
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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.nexmo.common.LegacyClient;
import com.nexmo.client.NexmoResponseParseException;
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
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.nexmo.common.util.HexUtil;
import com.nexmo.messaging.sdk.messages.Message;
import com.nexmo.messaging.sdk.messages.parameters.ValidityPeriod;

/**
 * Client for talking to the Nexmo API.
 * <p>
 * To submit a message, instantiate a NexmoSmsClient, passing the credentials for your Nexmo account on
 * the constructor. Then instantiate the appropriate {@link com.nexmo.messaging.sdk.messages.Message}
 * subclass depending on which type of message you are going to submit. The following subclasses are available:
 *
 * <ul>
 * <li>{@link com.nexmo.messaging.sdk.messages.TextMessage}
 * <li>{@link com.nexmo.messaging.sdk.messages.BinaryMessage}
 * <li>{@link com.nexmo.messaging.sdk.messages.WapPushMessage}
 * <li>{@link com.nexmo.messaging.sdk.messages.UnicodeMessage}
 * </ul>
 * <p>
 * Once you have a {@link com.nexmo.messaging.sdk.messages.Message} object, pass it to {@link #submitMessage(Message)}
 * which will submit the message to the Nexmo API. It returns an array of
 * {@link com.nexmo.messaging.sdk.SmsSubmissionResult}, with 1 entry for every sms message that was sent.
 * Text messages greater than 160 characters will require multiple SMS messages to be submitted.
 * Each entry in this array will contain an individual messageId as well as an individual status detailing the success or reason for failure of each message.
 * <p>
 * The list of possible status codes is listed below:
 * <ul>
 *  <li>0 = success  -- the message was successfully accepted for delivery by nexmo
 *  <li>1 = throttled -- You have exceeded the submission capacity allowed on this account, please back-off and re-try
 *  <li>2 = missing params -- Your request is incomplete and missing some mandatory parameters
 *  <li>3 = invalid params -- The value of 1 or more parameters is invalid
 *  <li>4 = invalid credentials -- The api key / secret you supplied is either invalid or disabled
 *  <li>5 = internal error -- An error has occurred in the nexmo platform whilst processing this message
 *  <li>6 = invalid message -- The Nexmo platform was unable to process this message, for example, an un-recognized number prefix
 *  <li>7 = number barred -- The number you are trying to submit to is blacklisted and may not receive messages
 *  <li>8 = partner account barred -- The api key you supplied is for an account that has been barred from submitting messages
 *  <li>9 = partner quota exceeded -- Your pre-pay account does not have sufficient credit to process this message
 *  <li>10 = too many existing binds -- The number of simultaneous connections to the platform exceeds the capabilities of your account
 *  <li>11 = account not enabled for http -- This account is not provisioned for http / rest submission, you should use SMPP instead
 *  <li>12 = message too long -- The message length exceeds the maximum allowed
 *  <li>13 = comms failure -- There was a network failure attempting to contact Nexmo
 *  <li>14 = Invalid Signature -- The signature supplied with this request was not verified successfully
 *  <li>15 = invalid sender address -- The sender address was not allowed for this message
 *  <li>16 = invalid TTL -- The ttl parameter values, or combination of parameters is invalid
 *  <li>17 = number unreachable -- this destination cannot be delivered to at this time (if reachable=true is specified)
 *  <li>18 = too many destinations -- There are more than the maximum allowed number of destinations in this request
 *  <li>19 = Facility Not Allowed - Your request makes use of a facility that is not enabled on your account
 *  <li>20 = Invalid Message Class - The message class value supplied was out of range (0 - 3)
 * </ul>
 * @author  Paul Cook
 */
public class NexmoSmsClient extends LegacyClient {

    private static final Log log = LogFactory.getLog(NexmoSmsClient.class);

    /**
     * Service url used unless over-ridden on the constructor
     */
    protected static final String DEFAULT_BASE_URL = "https://rest.nexmo.com";

    /**
     * The endpoint path for submitting sms messages
     */
    protected static final String SUBMISSION_PATH_SMS = "/sms/xml";

    /**
     * Default connection timeout of 5000ms used by this client unless specifically overridden onb the constructor
     */
    protected static final int DEFAULT_CONNECTION_TIMEOUT = 5000;

    /**
     * Default read timeout of 30000ms used by this client unless specifically overridden onb the constructor
     */
    protected static final int DEFAULT_SO_TIMEOUT = 30000;

    /**
     * Instantiate a new NexmoSmsClient without request signing.
     *
     * @param apiKey Your Nexmo account api key
     * @param apiSecret Your Nexmo account api secret
     */
    public NexmoSmsClient(final String apiKey,
                          final String apiSecret) {
        this(DEFAULT_BASE_URL,
             apiKey,
             apiSecret,
             DEFAULT_CONNECTION_TIMEOUT,
             DEFAULT_SO_TIMEOUT,
             false,  // signRequests
             null);  // signatureSecretKey
    }

    /**
     * Instantiate a new NexmoSmsClient without request signing.
     *
     * @param apiKey Your Nexmo account api key
     * @param apiSecret Your Nexmo account api secret
     * @param connectionTimeout over-ride the default connection timeout with this value (in milliseconds)
     * @param soTimeout over-ride the default read-timeout with this value (in milliseconds)
     */
    public NexmoSmsClient(final String apiKey,
                          final String apiSecret,
                          final int connectionTimeout,
                          final int soTimeout) {
        this(DEFAULT_BASE_URL,
             apiKey,
             apiSecret,
             connectionTimeout,
             soTimeout,
             false,  // signRequests
             null);  // signatureSecretKey
    }

    /**
     * Instantiate a new NexmoSmsClient.
     * <p>
     * This constructor can set the baseUrl used to construct URLs for submitting to a testing sandbox or other
     * environment.
     *
     * @param baseUrl The base URL to be used instead of <code>DEFAULT_BASE_URL</code>
     * @param apiKey Your Nexmo account api key
     * @param apiSecret Your Nexmo account api secret
     * @param connectionTimeout over-ride the default connection timeout with this value (in milliseconds)
     * @param soTimeout over-ride the default read-timeout with this value (in milliseconds)
     * @param signRequests do we generate a signature for this request using the secret key
     * @param signatureSecretKey the secret key we will use to generate the signatures for signed requests
     */
    public NexmoSmsClient(final String baseUrl,
                          final String apiKey,
                          final String apiSecret,
                          final int connectionTimeout,
                          final int soTimeout,
                          final boolean signRequests,
                          final String signatureSecretKey) {
        super(baseUrl, apiKey, apiSecret, connectionTimeout, soTimeout, signRequests, signatureSecretKey);
    }

    /**
     * Send an SMS message.
     *
     * This uses the supplied object to construct a request and post it to the Nexmo API.<br>
     * This method will respond with an array of SmsSubmissionResult objects. Depending on the nature and length of the submitted message, Nexmo may automatically
     * split the message into multiple sms messages in order to deliver to the handset. For example, a long text sms of greater than 160 chars will need to be split
     * into multiple 'concatenated' sms messages. The Nexmo service will handle this automatically for you.<br>
     * The array of SmsSubmissionResult objects will contain a SmsSubmissionResult object for every actual sms that was required to submit the message.
     * each message can potentially have a different status result, and each message will have a different message id.
     * Delivery notifications will be generated for each sms message within this set and will be posted to your application containing the appropriate message id.
     *
     * @param message The message request object that describes the type of message and the contents to be submitted.
     *
     * @return SmsSubmissionResult[] an array of results, 1 object for each sms message that was required to submit this message in its entirety
     *
     * @throws NexmoResponseParseException if the HTTP response could not be parsed.
     * @throws IOException There has been an error attempting to communicate with the Nexmo service (e.g. Network failure).
     */
    public SmsSubmissionResult[] submitMessage(Message message) throws IOException, NexmoResponseParseException {
        return submitMessage(message,  null);
    }

    /**
     * Submit a message submission request object.
     * This will use the supplied object to construct a request and post it to the Nexmo REST interface.<br>
     * This method will respond with an array of SmsSubmissionResult objects. Depending on the nature and length of the submitted message, Nexmo may automatically
     * split the message into multiple sms messages in order to deliver to the handset. For example, a long text sms of greater than 160 chars will need to be split
     * into multiple 'concatenated' sms messages. The Nexmo service will handle this automatically for you.<br>
     * The array of SmsSubmissionResult objects will contain a SmsSubmissionResult object for every actual sms that was required to submit the message.
     * each message can potentially have a different status result, and each message will have a different message id.
     * Delivery notifications will be generated for each sms message within this set and will be posted to your application containing the appropriate message id.
     *
     * @param message The message request object that describes the type of message and the contents to be submitted.
     * @param validityPeriod The validity period (Time-To-Live) for this message. Specifies the time before this message will be expired if not yet delivered
     *
     * @return SmsSubmissionResult[] an array of results, 1 object for each sms message that was required to submit this message in its entirety
     *
     * @throws NexmoResponseParseException if the HTTP response could not be parsed.
     * @throws IOException There has been a general failure either within the Client class, or whilst attempting to communicate with the Nexmo service (eg, Network failure)
     */
    public SmsSubmissionResult[] submitMessage(final Message message,
                                               final ValidityPeriod validityPeriod) throws IOException, NexmoResponseParseException {

        log.debug("HTTP-Message-Submission Client .. from [ " + message.getFrom() + " ] to [ " + message.getTo() + " ] msg [ " + message.getMessageBody() + " ] ");

        // From the Message object supplied, construct an appropriate request to be submitted to the Nexmo REST Service.

        // Determine what 'product' type we are submitting, and select the appropriate endpoint path
        List<NameValuePair> params = constructParams(message, validityPeriod);

        String baseUrl = makeUrl(SUBMISSION_PATH_SMS);

        // Now that we have generated a query string, we can instantiate a HttpClient,
        // and construct a POST request:

        String url;

        HttpPost httpPost = new HttpPost(baseUrl);
        httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
        url = baseUrl + "?" + URLEncodedUtils.format(params, "utf-8");

        HttpResponse httpResponse = this.getHttpClient().execute(httpPost);

        String response;
        try {
            response = new BasicResponseHandler().handleResponse(httpResponse);
            log.info(".. SUBMITTED NEXMO-HTTP URL [ " + url + " ] -- response [ " + response + " ] ");
        } catch (HttpResponseException e) {
            httpPost.abort();
            log.error("communication failure", e);

            // return a COMMS failure ...
            return new SmsSubmissionResult[] {
                    new SmsSubmissionResult(SmsSubmissionResult.STATUS_COMMS_FAILURE,
                            null,
                            null,
                            "Failed to communicate with NEXMO-HTTP url [ " + url + " ] ..." + e,
                            message.getClientReference(),
                            null,
                            null,
                            true,
                            null,
                            null)
            };
        }

        return parseResponse(response);
    }

    protected List<NameValuePair> constructParams(final Message message,
                                        final ValidityPeriod validityPeriod) {
        // Determine the type parameter based on the type of Message object.
        boolean binary = message.getType() == Message.MESSAGE_TYPE_BINARY;
        boolean unicode = message.isUnicode();
        boolean wapPush = message.getType() == Message.MESSAGE_TYPE_WAPPUSH;

        String mode = "text";

        if (binary)
            mode = "binary";

        if (unicode)
            mode = "unicode";

        if (wapPush)
            mode = "wappush";

        List<NameValuePair> params = constructParams();
        params.add(new BasicNameValuePair("from", message.getFrom()));
        params.add(new BasicNameValuePair("to", message.getTo()));
        params.add(new BasicNameValuePair("type", mode));
        if (wapPush) {
            params.add(new BasicNameValuePair("url", message.getWapPushUrl()));
            params.add(new BasicNameValuePair("title", message.getWapPushTitle()));
            if (message.getWapPushValidity() > 0)
                params.add(new BasicNameValuePair("validity", "" + message.getWapPushValidity()));
        } else if (binary) {
            // Binary Message
            if (message.getBinaryMessageUdh() != null)
                params.add(new BasicNameValuePair("udh", HexUtil.bytesToHex(message.getBinaryMessageUdh())));
            params.add(new BasicNameValuePair("body", HexUtil.bytesToHex(message.getBinaryMessageBody())));
        } else {
            // Text Message
            params.add(new BasicNameValuePair("text", message.getMessageBody()));
        }

        if (message.getClientReference() != null)
            params.add(new BasicNameValuePair("client-ref", message.getClientReference()));

        params.add(new BasicNameValuePair("status-report-req", "" + message.getStatusReportRequired()));

        if (message.getMessageClass() != null)
            params.add(new BasicNameValuePair("message-class", "" + message.getMessageClass().getMessageClass()));

        if (message.getProtocolId() != null)
            params.add(new BasicNameValuePair("protocol-id", "" + message.getProtocolId()));

        if (validityPeriod != null) {
            if (validityPeriod.getTimeToLive() != null)
                params.add(new BasicNameValuePair("ttl", "" + validityPeriod.getTimeToLive()));
            if (validityPeriod.getValidityPeriodHours() != null)
                params.add(new BasicNameValuePair("ttl-hours", "" + validityPeriod.getValidityPeriodHours()));
            if (validityPeriod.getValidityPeriodMinutes() != null)
                params.add(new BasicNameValuePair("ttl-minutes", "" + validityPeriod.getValidityPeriodMinutes()));
            if (validityPeriod.getValidityPeriodSeconds() != null)
                params.add(new BasicNameValuePair("ttl-seconds", "" + validityPeriod.getValidityPeriodSeconds()));
        }

        this.signParams(params);

        return params;
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

        Document doc = parseXml(response);

        NodeList replies = doc.getElementsByTagName("mt-submission-response");
        for (int i=0;i<replies.getLength();i++) {
            Node reply = replies.item(i);
            NodeList messageLists = reply.getChildNodes();
            for (int i2=0;i2<messageLists.getLength();i2++) {
                Node messagesNode = messageLists.item(i2);
                if (messagesNode.getNodeType() != Node.ELEMENT_NODE)
                    continue;
                if (messagesNode.getNodeName().equals("messages")) {
                    NodeList messages = messagesNode.getChildNodes();
                    for (int i3=0;i3<messages.getLength();i3++) {
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
        for (int i4=0;i4<nodes.getLength();i4++) {
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
