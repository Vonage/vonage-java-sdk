package com.nexmo.messaging.sdk;

import java.util.List;
import java.util.ArrayList;
import java.io.StringReader;
import java.math.BigDecimal;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.NamedNodeMap;
import org.xml.sax.InputSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.message.BasicNameValuePair;

import com.nexmo.common.util.HexUtil;
import com.nexmo.common.http.HttpClientUtils;
import com.nexmo.security.RequestSigning;
import com.nexmo.messaging.sdk.messages.Message;
import com.nexmo.messaging.sdk.messages.parameters.ValidityPeriod;

/**
 * NexmoSmsClient.java<br><br>
 *
 * Client for talking to the Nexmo REST interface<br><br>
 *
 * Usage
 *
 * To submit a message, first you should instanciate a NexmoSmsClient, passing the credentials for your Nexmo account on the constructor.
 * Then, you should instanciate the appropriate {@link com.nexmo.messaging.sdk.messages.Message} subclass depending on which type of message you are going to submit.<br>
 * The following subclasses are available ...<br><br>
 *
 * com.nexmo.messaging.sdk.messages.{@link com.nexmo.messaging.sdk.messages.TextMessage}<br>
 * com.nexmo.messaging.sdk.messages.{@link com.nexmo.messaging.sdk.messages.BinaryMessage}<br>
 * com.nexmo.messaging.sdk.messages.{@link com.nexmo.messaging.sdk.messages.WapPushMessage}<br>
 * com.nexmo.messaging.sdk.messages.{@link com.nexmo.messaging.sdk.messages.UnicodeMessage}<br>
 *
 * Each of these subclasses requires different message parameters to be passed on the constructor.
 * See the included javadocs for further details.<br><br>
 *
 * Once you have a {@link com.nexmo.messaging.sdk.messages.Message} object, you simply pass this to the {@link #submitMessage(Message)} method in the NexmoSmsClient instance.
 * This will construct and post the request to the Nexmo REST service.<br>
 * This method will return an array of {@link com.nexmo.messaging.sdk.SmsSubmissionResult}, with 1 entry for every sms message that was sent.<br>
 * Certain messages, for example, long text messages greater than 160 characters, will require multiple sms's to be submitted.<br>
 * Each entry in this array will contain an individual messageId as well as an individual status detailing the success or reason for failure of each message.<br><br>
 *
 * The list of possible status codes is listed below ..<br><br>
 *
 * // 0 = success  -- the message was successfully accepted for delivery by nexmo<br>
 * // 1 = throttled -- You have exceeded the submission capacity allowed on this account, please back-off and re-try<br>
 * // 2 = missing params -- Your request is incomplete and missing some mandatory parameters<br>
 * // 3 = invalid params -- The value of 1 or more parameters is invalid<br>
 * // 4 = invalid credentials -- The api key / secret you supplied is either invalid or disabled<br>
 * // 5 = internal error -- An error has occurred in the nexmo platform whilst processing this message<br>
 * // 6 = invalid message -- The Nexmo platform was unable to process this message, for example, an un-recognized number prefix<br>
 * // 7 = number barred -- The number you are trying to submit to is blacklisted and may not receive messages<br>
 * // 8 = partner account barred -- The api key you supplied is for an account that has been barred from submitting messages<br>
 * // 9 = partner quota exceeded -- Your pre-pay account does not have sufficient credit to process this message<br>
 * // 10 = too many existing binds -- The number of simultaneous connections to the platform exceeds the capabilities of your account<br>
 * // 11 = account not enabled for http -- This account is not provisioned for http / rest submission, you should use SMPP instead<br>
 * // 12 = message too long -- The message length exceeds the maximum allowed<br>
 * // 13 = comms failure -- There was a network failure attempting to contact Nexmo
 * // 14 = Invalid Signature -- The signature supplied with this request was not verified successfully
 * // 15 = invalid sender address -- The sender address was not allowed for this message
 * // 16 = invalid TTL -- The ttl parameter values, or combination of parameters is invalid
 * // 17 = number unreachable -- this destination cannot be delivered to at this time (if reachable=true is specified)
 * // 18 = too many destinations -- There are more than the maximum allowed number of destinations in this request
 * // 19 = Facility Not Allowed - Your request makes use of a facility that is not enabled on your account
 * // 20 = Invalid Message Class - The message class value supplied was out of range (0 - 3)
 *
 * @author  Paul Cook
 * @version 1.0
 */
public class NexmoSmsClient {

    private static final Log log = LogFactory.getLog(NexmoSmsClient.class);

    /**
     * http://rest.nexmo.com/sms/xml<br>
     * Submission url used unless over-ridden on the constructor
     */
    public static final String DEFAULT_BASE_URL = "http://rest.nexmo.com/sms/xml";

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

    private final boolean signRequests;
    private final String signatureSecretKey;

    private final boolean useSSL;

    private HttpClient httpClient = null;

    /**
     * Instanciate a new NexmoSmsClient instance that will communicate using the supplied credentials.
     *
     * @param apiKey Your Nexmo account api key
     * @param apiSecret Your Nexmo account api secret
     */
    public NexmoSmsClient(final String apiKey,
                          final String apiSecret) throws Exception {
        this(DEFAULT_BASE_URL,
             apiKey,
             apiSecret,
             DEFAULT_CONNECTION_TIMEOUT,
             DEFAULT_SO_TIMEOUT,
             false,  // signRequests
             null,   // signatureSecretKey
             false); // useSSL
    }

    /**
     * Instanciate a new NexmoSmsClient instance that will communicate using the supplied credentials, and will use the supplied connection and read timeout values.
     *
     * @param apiKey Your Nexmo account api key
     * @param apiSecret Your Nexmo account api secret
     * @param connectionTimeout over-ride the default connection timeout with this value (in milliseconds)
     * @param soTimeout over-ride the default read-timeout with this value (in milliseconds)
     */
    public NexmoSmsClient(final String apiKey,
                          final String apiSecret,
                          final int connectionTimeout,
                          final int soTimeout) throws Exception {
        this(DEFAULT_BASE_URL,
             apiKey,
             apiSecret,
             connectionTimeout,
             soTimeout,
             false,  // signRequests
             null,   // signatureSecretKey
             false); // useSSL
    }

    /**
     * Instanciate a new NexmoSmsClient instance that will communicate using the supplied credentials, and will use the supplied connection and read timeout values.<br>
     * Additionally, you can specify an alternative service base url. For example submitting to a testing sandbox environment,
     * or if requested to submit to an alternative address by Nexmo, for example, in cases where it may be necessary to prioritize your traffic.
     *
     * @param apiKey Your Nexmo account api key
     * @param apiSecret Your Nexmo account api secret
     * @param connectionTimeout over-ride the default connection timeout with this value (in milliseconds)
     * @param soTimeout over-ride the default read-timeout with this value (in milliseconds)
     * @param signRequests do we generate a signature for this request using the secret key
     * @param signatureSecretKey the secret key we will use to generate the signatures for signed requests
     * @param useSSL do we use a SSL / HTTPS connection for submitting requests
     */
    public NexmoSmsClient(String baseUrl,
                          final String apiKey,
                          final String apiSecret,
                          final int connectionTimeout,
                          final int soTimeout,
                          final boolean signRequests,
                          final String signatureSecretKey,
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

        this.signRequests = signRequests;
        this.signatureSecretKey = signatureSecretKey;

        this.useSSL = useSSL;
    }

    /**
     * submit a message submission request object.
     * This will use the supplied object to construct a request and post it to the Nexmo REST interface.<br>
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
     * @throws Exception There has been a general failure either within the Client class, or whilst attempting to communicate with the Nexmo service (eg, Network failure)
     */
    public SmsSubmissionResult[] submitMessage(Message message) throws Exception {
        boolean performReachabilityCheck = false;
        ValidityPeriod validityPeriod = null;
        String networkCode = null;
        return submitMessage(message, validityPeriod, networkCode, performReachabilityCheck);
    }

    /**
     * submit a message submission request object.
     * This will use the supplied object to construct a request and post it to the Nexmo REST interface.<br>
     * This method will respond with an array of SmsSubmissionResult objects. Depending on the nature and length of the submitted message, Nexmo may automatically
     * split the message into multiple sms messages in order to deliver to the handset. For example, a long text sms of greater than 160 chars will need to be split
     * into multiple 'concatenated' sms messages. The Nexmo service will handle this automatically for you.<br>
     * The array of SmsSubmissionResult objects will contain a SmsSubmissionResult object for every actual sms that was required to submit the message.
     * each message can potentially have a different status result, and each message will have a different message id.
     * Delivery notifications will be generated for each sms message within this set and will be posted to your application containing the appropriate message id.
     *
     * @param message The message request object that describes the type of message and the contents to be submitted.
     * @param validityPeriod The validity period (Time-To-Live) for this message. Specifies the time before this mesage will be expired if not yet delivered
     *
     * @return SmsSubmissionResult[] an array of results, 1 object for each sms message that was required to submit this message in its entirety
     *
     * @throws Exception There has been a general failure either within the Client class, or whilst attempting to communicate with the Nexmo service (eg, Network failure)
     */
    public SmsSubmissionResult[] submitMessage(Message message, ValidityPeriod validityPeriod) throws Exception {
        boolean performReachabilityCheck = false;
        String networkCode = null;
        return submitMessage(message, validityPeriod, networkCode, performReachabilityCheck);
    }

    /**
     * submit a message submission request object.
     * This will use the supplied object to construct a request and post it to the Nexmo REST interface.<br>
     * This method will respond with an array of SmsSubmissionResult objects. Depending on the nature and length of the submitted message, Nexmo may automatically
     * split the message into multiple sms messages in order to deliver to the handset. For example, a long text sms of greater than 160 chars will need to be split
     * into multiple 'concatenated' sms messages. The Nexmo service will handle this automatically for you.<br>
     * The array of SmsSubmissionResult objects will contain a SmsSubmissionResult object for every actual sms that was required to submit the message.
     * each message can potentially have a different status result, and each message will have a different message id.
     * Delivery notifications will be generated for each sms message within this set and will be posted to your application containing the appropriate message id.
     *
     * @param message The message request object that describes the type of message and the contents to be submitted.
     * @param validityPeriod The validity period (Time-To-Live) for this message. Specifies the time before this mesage will be expired if not yet delivered
     * @param networkCode (Optional) use this parameter to force this message to be associated with and delivered on this network. Use this in cases where you want to over-ride
     *                               the automatic network detection provided by Nexmo. This value will be used in order to determine the pricing and routing for this message.<br>
     *                               (Note) This feature must be enabled and available on your account or else this value will be ignored.
     * @param performReachabilityCheck Flag to indicate wether a reachability check should be performed on this message before delivery is attempted. If the destination is
     *                                 not reachable, the message will be rejected and a reachability status will be returned in the response field smsSubmissionReachabilityStatus<br>
     *                                 (Note) This feature must be enabled and available on your account or else the message request will be rejected. There may be additional cost
     *                                 associated with the use of this feature.
     *
     * @return SmsSubmissionResult[] an array of results, 1 object for each sms message that was required to submit this message in its entirety
     *
     * @throws Exception There has been a general failure either within the Client class, or whilst attempting to communicate with the Nexmo service (eg, Network failure)
     */
    public SmsSubmissionResult[] submitMessage(Message message,
                                               ValidityPeriod validityPeriod,
                                               String networkCode,
                                               boolean performReachabilityCheck) throws Exception {

        log.debug("HTTP-SMS-Submission Client .. from [ " + message.getFrom() + " ] to [ " + message.getTo() + " ] msg [ " + message.getMessageBody() + " ] ");

        // From the Message object supplied, construct an appropriate request to be submitted to the Nexmo REST Service.

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

        // Construct a query string as a list of NameValuePairs

        List<NameValuePair> params = new ArrayList<NameValuePair>();

        boolean doPost = false;

        params.add(new BasicNameValuePair("api_key", this.apiKey));
        if (!this.signRequests)
            params.add(new BasicNameValuePair("api_secret", this.apiSecret));
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
            if (message.getMessageBody() != null && message.getMessageBody().length() > 255)
                doPost = true;
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
                params.add(new BasicNameValuePair("ttl", "" + validityPeriod.getTimeToLive().intValue()));
            if (validityPeriod.getValidityPeriodHours() != null)
                params.add(new BasicNameValuePair("ttl-hours", "" + validityPeriod.getValidityPeriodHours().intValue()));
            if (validityPeriod.getValidityPeriodMinutes() != null)
                params.add(new BasicNameValuePair("ttl-minutes", "" + validityPeriod.getValidityPeriodMinutes().intValue()));
            if (validityPeriod.getValidityPeriodSeconds() != null)
                params.add(new BasicNameValuePair("ttl-seconds", "" + validityPeriod.getValidityPeriodSeconds().intValue()));
        }

        if (networkCode != null)
            params.add(new BasicNameValuePair("network-code", networkCode));

        if (performReachabilityCheck)
            params.add(new BasicNameValuePair("test-reachable", "true"));

        if (this.signRequests)
            RequestSigning.constructSignatureForRequestParameters(params, this.signatureSecretKey);

        String baseUrl = this.useSSL ? this.baseUrlHttps : this.baseUrlHttp;

        // Now that we have generated a query string, we can instanciate a HttpClient,
        // construct a POST or GET method and execute to submit the request
        String response = null;
        for (int pass=1;pass<=2;pass++) {
            HttpUriRequest method = null;
            doPost = true;
            String url = null;
            if (doPost) {
                HttpPost httpPost = new HttpPost(baseUrl);
                httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8")); 
                method = httpPost;
                url = baseUrl + "?" + URLEncodedUtils.format(params, "utf-8");
            } else {
                String query = URLEncodedUtils.format(params, "utf-8");
                method = new HttpGet(baseUrl + "?" + query);
                url = method.getRequestLine().getUri();
            }
            
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
                SmsSubmissionResult[] results = new SmsSubmissionResult[1];
                results[0] = new SmsSubmissionResult(SmsSubmissionResult.STATUS_COMMS_FAILURE,
                                                     null,
                                                     null,
                                                     "Failed to communicate with NEXMO-HTTP url [ " + url + " ] ..." + e,
                                                     message.getClientReference(),
                                                     null,
                                                     null,
                                                     true,
                                                     null);
                return results;
            }
        }

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
                        </message>
                    </messages>
                </mt-submission-response>
        */

        List<SmsSubmissionResult> results = new ArrayList<SmsSubmissionResult>();

        Document doc = null;
        synchronized(this.documentBuilder) {
            try {
                doc = this.documentBuilder.parse(new InputSource(new StringReader(response)));
            } catch (Exception e) {
                throw new Exception("Failed to build a DOM doc for the xml document [ " + response + " ] ", e);
            }
        }

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
                        Node messageNode = messages.item(i3);
                        if (messageNode.getNodeType() != Node.ELEMENT_NODE)
                            continue;

                        int status = -1;
                        String messageId = null;
                        String destination = null;
                        String errorText = null;
                        String clientReference = null;
                        BigDecimal remainingBalance = null;
                        BigDecimal messagePrice = null;
                        SmsSubmissionReachabilityStatus smsSubmissionReachabilityStatus = null;

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
                            } else if (node.getNodeName().equals("reachability")) {
                                NamedNodeMap attributes = node.getAttributes();
                                Node attr = attributes.getNamedItem("status");
                                String str = attr == null ? "" + SmsSubmissionReachabilityStatus.REACHABILITY_STATUS_UNKNOWN : attr.getNodeValue();
                                int reachabilityStatus = SmsSubmissionReachabilityStatus.REACHABILITY_STATUS_UNKNOWN;
                                try {
                                    reachabilityStatus = Integer.parseInt(str);
                                } catch (NumberFormatException e) {
                                    log.error("xml parser .. invalid value in 'status' attribute in <reachability> node [ " + str + " ] ");
                                    reachabilityStatus = SmsSubmissionReachabilityStatus.REACHABILITY_STATUS_UNKNOWN;
                                }

                                attr = attributes.getNamedItem("description");
                                String description = attr == null ? "-UNKNOWN-" : attr.getNodeValue();

                                smsSubmissionReachabilityStatus = new SmsSubmissionReachabilityStatus(reachabilityStatus, description);
                            } else if (node.getNodeName().equals("network")) {
                                // placeholder for future functionality
                            } else
                                log.error("xml parser .. unknown node found in status-return, expected [ messageId, to, status, errorText, clientRef, messagePrice, remainingBalance, reachability ] -- found [ " + node.getNodeName() + " ] ");
                        }

                        if (status == -1)
                            throw new Exception("Xml Parser - did not find a <status> node");

                        // Is this a temporary error ?
                        boolean temporaryError = (status == SmsSubmissionResult.STATUS_THROTTLED ||
                                                  status == SmsSubmissionResult.STATUS_INTERNAL_ERROR ||
                                                  status == SmsSubmissionResult.STATUS_TOO_MANY_BINDS);

                        results.add(new SmsSubmissionResult(status,
                                                            destination,
                                                            messageId,
                                                            errorText,
                                                            clientReference,
                                                            remainingBalance,
                                                            messagePrice,
                                                            temporaryError,
                                                            smsSubmissionReachabilityStatus));
                    }
                }
            }
        }

        return results.toArray(new SmsSubmissionResult[0]);
    }

}
