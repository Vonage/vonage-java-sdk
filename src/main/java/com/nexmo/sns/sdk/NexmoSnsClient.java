package com.nexmo.sns.sdk;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.BasicResponseHandler;

import com.nexmo.common.http.HttpClientUtils;
import com.nexmo.sns.sdk.request.Request;
import com.nexmo.sns.sdk.response.SnsServiceResult;

/**
 * NexmoSmsClient.java<br><br>
 *
 * Client for talking to the Nexmo REST interface<br><br>
 *
 * @author  Paul Cook
 * @version 1.0
 */
public class NexmoSnsClient {

    private static final Log log = LogFactory.getLog(NexmoSnsClient.class);

    /**
     * http://rest.nexmo.com/sms/xml<br>
     * Submission url used unless over-ridden on the constructor
     */
    public static final String DEFAULT_BASE_URL = "http://sns.nexmo.com/sns/xml";

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

    private HttpClient httpClient = null;

    /**
     * Instanciate a new NexmoSnsClient instance that will communicate using the supplied credentials.
     *
     * @param apiKey Your Nexmo account api key
     * @param apiSecret Your Nexmo account api secret
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
     * Instanciate a new NexmoSnsClient instance that will communicate using the supplied credentials, and will use the supplied connection and read timeout values.
     *
     * @param apiKey Your Nexmo account api key
     * @param apiSecret Your Nexmo account api secret
     * @param connectionTimeout over-ride the default connection timeout with this value (in milliseconds)
     * @param soTimeout over-ride the default read-timeout with this value (in milliseconds)
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
     * Instanciate a new NexmoSnsClient instance that will communicate using the supplied credentials, and will use the supplied connection and read timeout values.<br>
     * Additionally, you can specify an alternative service base url. For example submitting to a testing sandbox environment,
     * or if requested to submit to an alternative address by Nexmo, for example, in cases where it may be necessary to prioritize your traffic.
     *
     * @param apiKey Your Nexmo account api key
     * @param apiSecret Your Nexmo account api secret
     * @param connectionTimeout over-ride the default connection timeout with this value (in milliseconds)
     * @param soTimeout over-ride the default read-timeout with this value (in milliseconds)
     */
    public NexmoSnsClient(String baseUrl,
                          final String apiKey,
                          final String apiSecret,
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
    }

    public SnsServiceResult submit(Request request) throws Exception {
        boolean https = false;
        return submit(request, https);
    }

    public SnsServiceResult submitHttps(Request request) throws Exception {
        boolean https = true;
        return submit(request, https);
    }

    public SnsServiceResult submit(Request request, boolean https) throws Exception {

        log.info("NEXMO-REST-SNS-SERVICE-CLIENT ... submit request [ " + request.toString() + " ] ");

        // Construct a query string as a list of NameValuePairs

        List<NameValuePair> params = new ArrayList<NameValuePair>();

        params.add(new BasicNameValuePair("api_key", this.apiKey));
        params.add(new BasicNameValuePair("api_secret", this.apiSecret));
        params.add(new BasicNameValuePair("cmd", request.getCommand()));
        if (request.getQueryParameters() != null)
            for (Map.Entry<String, String> entry: request.getQueryParameters().entrySet())
                params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));

        String baseUrl = https ? this.baseUrlHttps : this.baseUrlHttp;

        // Now that we have generated a query string, we can instanciate a HttpClient,
        // construct a POST or GET method and execute to submit the request
        String response = null;
        HttpPost method = new HttpPost(baseUrl);
        method.setEntity(new UrlEncodedFormEntity(params, "UTF-8")); 
        String url = baseUrl + "?" + URLEncodedUtils.format(params, "utf-8");
        try {
            if (this.httpClient == null)
                this.httpClient = HttpClientUtils.getInstance(this.connectionTimeout, this.soTimeout).getNewHttpClient();
            HttpResponse httpResponse = this.httpClient.execute(method);
            int status = httpResponse.getStatusLine().getStatusCode();
            if (status != 200)
                throw new Exception("got a non-200 response [ " + status + " ] from Nexmo-HTTP for url [ " + url + " ] ");
            response = new BasicResponseHandler().handleResponse(httpResponse);
            log.info(".. SUBMITTED NEXMO-HTTP URL [ " + url + " ] -- response [ " + response + " ] ");
        } catch (Exception e) {
            log.info("communication failure: " + e);
            method.abort();
            return new SnsServiceResult(request.getCommand(),
                                        SnsServiceResult.STATUS_COMMS_FAILURE,
                                        "Failed to communicate with NEXMO-HTTP url [ " + url + " ] ..." + e,
                                        null,
                                        null);
        }

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

        Document doc = null;
        synchronized(this.documentBuilder) {
            try {
                doc = this.documentBuilder.parse(new InputSource(new StringReader(response)));
            } catch (Exception e) {
                throw new Exception("Failed to build a DOM doc for the xml document [ " + response + " ] ", e);
            }
        }

        String command = null;
        int resultCode = -1;
        String resultMessage = null;
        String transactionId = null;
        String subscriberArn = null;

        NodeList replies = doc.getElementsByTagName("nexmo-sns");
        for (int i=0;i<replies.getLength();i++) {
            Node reply = replies.item(i);
            NodeList nodes = reply.getChildNodes();

            for (int i2=0;i2<nodes.getLength();i2++) {
                Node node = nodes.item(i2);
                if (node.getNodeType() != Node.ELEMENT_NODE)
                    continue;
                if (node.getNodeName().equals("command")) {
                    command = node.getFirstChild() == null ? null : node.getFirstChild().getNodeValue();
                } else if (node.getNodeName().equals("resultCode")) {
                    String str = node.getFirstChild() == null ? null : node.getFirstChild().getNodeValue();
                    try {
                        resultCode = Integer.parseInt(str);
                    } catch (Exception e) {
                        log.error("xml parser .. invalid value in <resultCode> node [ " + str + " ] ");
                        resultCode = SnsServiceResult.STATUS_INTERNAL_ERROR;
                    }
                } else if (node.getNodeName().equals("resultMessage")) {
                    resultMessage = node.getFirstChild() == null ? null : node.getFirstChild().getNodeValue();
                } else if (node.getNodeName().equals("transactionId")) {
                    transactionId = node.getFirstChild() == null ? null : node.getFirstChild().getNodeValue();
                } else if (node.getNodeName().equals("subscriberArn")) {
                    subscriberArn = node.getFirstChild() == null ? null : node.getFirstChild().getNodeValue();
                } else
                    log.error("xml parser .. unknown node found in nexmo-sns [ " + node.getNodeName() + " ] ");
            }
        }

        if (resultCode == -1)
            throw new Exception("Xml Parser - did not find a <resultCode> node");

        return new SnsServiceResult(command,
                                    resultCode,
                                    resultMessage,
                                    transactionId,
                                    subscriberArn);
    }

}
