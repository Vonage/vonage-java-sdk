package com.nexmo.messaging.sdk;

import java.util.List;
import java.util.ArrayList;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.math.BigDecimal;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.util.EncodingUtil;

import com.nexmo.common.util.HexUtil;
import com.nexmo.common.http.HttpClientUtils;
import com.nexmo.messaging.sdk.messages.Message;

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
 * // 4 = invalid credentials -- The username / password you supplied is either invalid or disabled<br>
 * // 5 = internal error -- An error has occurred in the nexmo platform whilst processing this message<br>
 * // 6 = invalid message -- The Nexmo platform was unable to process this message, for example, an un-recognized number prefix<br>
 * // 7 = number barred -- The number you are trying to submit to is blacklisted and may not receive messages<br>
 * // 8 = partner account barred -- The username you supplied is for an account that has been barred from submitting messages<br>
 * // 9 = partner quota exceeded -- Your pre-pay account does not have sufficient credit to process this message<br>
 * // 10 = too many existing binds -- The number of simultaneous connections to the platform exceeds the capabilities of your account<br>
 * // 11 = account not enabled for http -- This account is not provisioned for http / rest submission, you should use SMPP instead<br>
 * // 12 = message too long -- The message length exceeds the maximum allowed<br>
 *
 * @author  Paul Cook
 * @version 1.0
 */
public class NexmoSmsClient {

    private static Log log = LogFactory.getLog(NexmoSmsClient.class);

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
    private final String username;
    private final String password;

    private final int connectionTimeout;
    private final int soTimeout;

    private HttpClient httpClient = null;

    /**
     * Instanciate a new NexmoSmsClient instance that will communicate using the supplied credentials.
     *
     * @param username Your Nexmo account id
     * @param password Your Nexmo account password
     */
    public NexmoSmsClient(final String username,
                          final String password) throws Exception {
        this(DEFAULT_BASE_URL,
             username,
             password,
             DEFAULT_CONNECTION_TIMEOUT,
             DEFAULT_SO_TIMEOUT);
    }

    /**
     * Instanciate a new NexmoSmsClient instance that will communicate using the supplied credentials, and will use the supplied connection and read timeout values.
     *
     * @param username Your Nexmo account id
     * @param password Your Nexmo account password
     * @param connectionTimeout over-ride the default connection timeout with this value (in milliseconds)
     * @param soTimeout over-ride the default read-timeout with this value (in milliseconds)
     */
    public NexmoSmsClient(final String username,
                          final String password,
                          final int connectionTimeout,
                          final int soTimeout) throws Exception {
        this(DEFAULT_BASE_URL,
             username,
             password,
             connectionTimeout,
             soTimeout);
    }

    /**
     * Instanciate a new NexmoSmsClient instance that will communicate using the supplied credentials, and will use the supplied connection and read timeout values.<br>
     * Additionally, you can specify an alternative service base url. For example submitting to a testing sandbox environment,
     * or if requested to submit to an alternative address by Nexmo, for example, in cases where it may be necessary to prioritize your traffic.
     *
     * @param username Your Nexmo account id
     * @param password Your Nexmo account password
     * @param connectionTimeout over-ride the default connection timeout with this value (in milliseconds)
     * @param soTimeout over-ride the default read-timeout with this value (in milliseconds)
     */
    public NexmoSmsClient(String baseUrl,
                          final String username,
                          final String password,
                          final int connectionTimeout,
                          final int soTimeout) throws Exception {

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

        this.username = username;
        this.password = password;
        this.connectionTimeout = connectionTimeout;
        this.soTimeout = soTimeout;
        try {
            this.documentBuilderFactory = DocumentBuilderFactory.newInstance();
            this.documentBuilder = this.documentBuilderFactory.newDocumentBuilder();
        } catch (javax.xml.parsers.ParserConfigurationException e) {
            throw new Exception("ERROR initializing XML Document builder!", e);
        }
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
        boolean https = false;
        return submitMessage(message, https);
    }

    /**
     * submit a message submission request object using a secure SSL / HTTPS connection.
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
    public SmsSubmissionResult[] submitMessageHttps(Message message) throws Exception {
        boolean https = true;
        return submitMessage(message, https);
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
     * @param https Flag to indicate whether the request should be submitted using SSL/HTTPS rather than HTTP
     *
     * @return SmsSubmissionResult[] an array of results, 1 object for each sms message that was required to submit this message in its entirety
     *
     * @throws Exception There has been a general failure either within the Client class, or whilst attempting to communicate with the Nexmo service (eg, Network failure)
     */
    public SmsSubmissionResult[] submitMessage(Message message, boolean https) throws Exception {

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

        List<NameValuePair> params = new ArrayList();
        boolean doPost = false;

        params.add(new NameValuePair("username", this.username));
        params.add(new NameValuePair("password", this.password));
        params.add(new NameValuePair("from", message.getFrom()));
        params.add(new NameValuePair("to", message.getTo()));
        params.add(new NameValuePair("type", mode));
        if (wapPush) {
            params.add(new NameValuePair("url", message.getWapPushUrl()));
            params.add(new NameValuePair("title", message.getWapPushTitle()));
            if (message.getWapPushValidity() > 0)
                params.add(new NameValuePair("validity", "" + message.getWapPushValidity()));
        } else if (binary) {
            // Binary Message
            if (message.getBinaryMessageUdh() != null)
                params.add(new NameValuePair("udh", HexUtil.bytesToHex(message.getBinaryMessageUdh())));
            params.add(new NameValuePair("body", HexUtil.bytesToHex(message.getBinaryMessageBody())));
        } else {
            // Text Message
            params.add(new NameValuePair("text", message.getMessageBody()));
            if (message.getMessageBody() != null && message.getMessageBody().length() > 255)
                doPost = true;
        }

        if (message.getClientReference() != null)
            params.add(new NameValuePair("client-ref", message.getClientReference()));

        params.add(new NameValuePair("status-report-req", "" + message.getStatusReportRequired()));

        NameValuePair[] queryString = (NameValuePair[])params.toArray(new NameValuePair[params.size()]);

        String baseUrl = https ? this.baseUrlHttps : this.baseUrlHttp;

        // Now that we have generated a query string, we can instanciate a HttpClient,
        // construct a POST or GET method and execute to submit the request
        String response = null;
        for (int pass=1;pass<=2;pass++) {
            HttpMethod method = null;
            if (doPost)
                method = new PostMethod(baseUrl);
            else
                method = new GetMethod(baseUrl);
            method.setQueryString(EncodingUtil.formUrlEncode(queryString, "UTF-8"));
            String url = baseUrl + "?" + method.getQueryString();
            try {
                if (this.httpClient == null)
                    this.httpClient = HttpClientUtils.getInstance(this.connectionTimeout, this.soTimeout).getNewHttpClient();
                int status = this.httpClient.executeMethod(method);
                if (status != 200)
                    throw new Exception("got a non-200 response [ " + status + " ] from Nexmo-HTTP for url [ " + url + " ] ");
                response = method.getResponseBodyAsString();
                method.releaseConnection();
                log.info(".. SUBMITTED NEXMO-HTTP URL [ " + url + " ] -- response [ " + response + " ] ");
                break;
            } catch (Exception e) {
                if (method != null)
                    method.releaseConnection();
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
                                                     "Failed to communicate with NEXMO-HTTP url [ " + url + " ] ..." + e,
                                                     message.getClientReference(),
                                                     null,
                                                     null,
                                                     true);
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
                            <messageId>xxx</messageId>
                            <status>xx</status>
                            <errorText>ff</errorText>
                            <clientRef>xxx</clientRef>
                            <remainingBalance>##.##</remainingBalance>
                            <messagePrice>##.##</messagePrice>
                        </message>
                    </messages>
                </mt-submission-response>

            Response codes ...

                // 0 = success
                // 1 = throttled
                // 2 = missing params
                // 3 = invalid params
                // 4 = invalid credentials
                // 5 = internal error
                // 6 = invalid message
                // 7 = number barred
                // 8 = partner account barred
                // 9 = partner quota exceeded
                // 10 = too many existing binds
                // 11 = account not enabled for http
                // 12 = message too long

        */

        List<SmsSubmissionResult> results = new ArrayList();

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
                        String errorText = null;
                        String clientReference = null;
                        BigDecimal remainingBalance = null;
                        BigDecimal messagePrice = null;

                        NodeList nodes = messageNode.getChildNodes();
                        for (int i4=0;i4<nodes.getLength();i4++) {
                            Node node = nodes.item(i4);
                            if (node.getNodeType() != Node.ELEMENT_NODE)
                                continue;
                            if (node.getNodeName().equals("messageId")) {
                                messageId = node.getFirstChild() == null ? null : node.getFirstChild().getNodeValue();
                            } else if (node.getNodeName().equals("status")) {
                                String str = node.getFirstChild() == null ? null : node.getFirstChild().getNodeValue();
                                try {
                                    status = Integer.parseInt(str);
                                } catch (Exception e) {
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
                                    remainingBalance = new BigDecimal(str);
                                } catch (Exception e) {
                                    log.error("xml parser .. invalid value in <remainingBalance> node [ " + str + " ] ");
                                }
                            } else if (node.getNodeName().equals("messagePrice")) {
                                String str = node.getFirstChild() == null ? null : node.getFirstChild().getNodeValue();
                                try {
                                    messagePrice = new BigDecimal(str);
                                } catch (Exception e) {
                                    log.error("xml parser .. invalid value in <messagePrice> node [ " + str + " ] ");
                                }
                            } else
                                log.error("xml parser .. unknown node found in status-return, should be [ message-id, status, error-text or client-ref value ] -- found [ " + node.getNodeName() + " ] ");
                        }

                        if (status == -1)
                            throw new Exception("Xml Parser - did not find a <status> node");

                        // Is this a temporary error ?
                        boolean temporaryError = (status == SmsSubmissionResult.STATUS_THROTTLED ||
                                                  status == SmsSubmissionResult.STATUS_INTERNAL_ERROR ||
                                                  status == SmsSubmissionResult.STATUS_TOO_MANY_BINDS);

                        results.add(new SmsSubmissionResult(status,
                                                            messageId,
                                                            errorText,
                                                            clientReference,
                                                            remainingBalance,
                                                            messagePrice,
                                                            temporaryError));
                    }
                }
            }
        }

        return results.toArray(new SmsSubmissionResult[0]);
    }

}
