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
package com.nexmo.verify.sdk.endpoints;

import com.nexmo.client.NexmoUnexpectedException;
import com.nexmo.common.LegacyClient;
import com.nexmo.client.NexmoResponseParseException;
import com.nexmo.common.util.XmlUtil;
import com.nexmo.verify.sdk.BaseResult;
import com.nexmo.verify.sdk.CheckResult;
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
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class CheckClient extends LegacyClient {
    private static final Log log = LogFactory.getLog(CheckClient.class);

    /**
     * The endpoint path for submitting verification check requests
     */
    private static final String ENDPOINT_PATH = "/verify/check/xml";

    /**
     * Create a new CheckClient.
     * <p>
     * This client is used for calling the verify API's check endpoint.
     *
     * @param baseUrl The base URL to be used instead of <code>DEFAULT_BASE_URL</code>
     * @param apiKey Your Nexmo account api key
     * @param apiSecret Your Nexmo account api secret
     * @param connectionTimeout over-ride the default connection timeout with this value (in milliseconds)
     * @param soTimeout over-ride the default read-timeout with this value (in milliseconds)
     */
    public CheckClient(final String baseUrl,
                       final String apiKey,
                       final String apiSecret,
                       final int connectionTimeout,
                       final int soTimeout) {

        super(baseUrl, apiKey, apiSecret, connectionTimeout, soTimeout);
    }

    public CheckResult check(final String requestId,
                             final String code) throws IOException, NexmoResponseParseException {
        return check(requestId, code, null);
    }

    public CheckResult check(final String requestId,
                             final String code,
                             final String ipAddress) throws IOException, NexmoResponseParseException {
        if (requestId == null || code == null)
            throw new IllegalArgumentException("request ID and code parameters are mandatory.");

        log.debug("HTTP-Number-Verify-Check Client .. for [ " + requestId + " ] code [ " + code + " ] ");

        List<NameValuePair> params = constructCheckParams(requestId, code);

        if (ipAddress != null)
            params.add(new BasicNameValuePair("ip_address", ipAddress));

        String verifyCheckBaseUrl = this.makeUrl(ENDPOINT_PATH);

        // Now that we have generated a query string, we can instantiate a HttpClient,
        // construct a POST method and execute to submit the request
        String response;
        HttpPost httpPost = new HttpPost(verifyCheckBaseUrl);
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
        } catch (UnsupportedEncodingException uee) {
            // This should never happen:
            throw new NexmoUnexpectedException("UTF-8 is an unsupported encoding in this JVM", uee);
        }
        String url = verifyCheckBaseUrl + "?" + URLEncodedUtils.format(params, "utf-8");

        try {
            HttpResponse httpResponse = this.getHttpClient().execute(httpPost);
            response = new BasicResponseHandler().handleResponse(httpResponse);
            log.info(".. SUBMITTED NEXMO-HTTP URL [ " + url + " ] -- response [ " + response + " ] ");
        } catch (HttpResponseException e) {
            httpPost.abort();
            log.error("communication failure: ", e);

            // return a COMMS failure ...
            return new CheckResult(BaseResult.STATUS_COMMS_FAILURE,
                    null,
                    0,
                    null,
                    "Failed to communicate with NEXMO-HTTP url [ " + url + " ] ..." + e,
                    true);
        }

        return parseCheckResponse(response);
    }

    private List<NameValuePair> constructCheckParams(String requestId, String code) {
        List<NameValuePair> params = this.constructParams();

        params.add(new BasicNameValuePair("request_id", requestId));
        params.add(new BasicNameValuePair("code", code));
        return params;
    }

    private CheckResult parseCheckResponse(String response) throws NexmoResponseParseException, IOException {
        Document doc = parseXml(response);

        Element root = doc.getDocumentElement();
        if (!"verify_response".equals(root.getNodeName()))
            throw new IOException("No valid response found [ " + response + "] ");

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
            throw new IOException("Xml Parser - did not find a <status> node");

        // Is this a temporary error ?
        boolean temporaryError = (status == BaseResult.STATUS_THROTTLED || status == BaseResult.STATUS_INTERNAL_ERROR);

        return new CheckResult(status, eventId, price, currency, errorText, temporaryError);
    }
}