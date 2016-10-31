package com.nexmo.common;
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

import com.nexmo.common.http.HttpClientUtils;
import com.nexmo.common.util.XmlUtil;
import com.nexmo.security.RequestSigning;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


// TODO: This may be better as a field inside the various clients.
public abstract class LegacyClient {
    /**
     * A lock associated with {@link #documentBuilder}.
     */
    private final Lock documentBuilderLock = new ReentrantLock();

    /**
     * Used for parsing XML data.
     *
     * Do not use this without locking on {@link #documentBuilderLock}
     */
    private DocumentBuilder documentBuilder;

    private HttpClient httpClient;

    private final String baseUrl;

    private final String apiKey;

    private final String apiSecret;

    private final int connectionTimeout;

    private final int soTimeout;

    private final boolean signRequests;

    private final String signatureSecretKey;

    public LegacyClient(final String baseUrl,
                        final String apiKey,
                        final String apiSecret,
                        final int connectionTimeout,
                        final int soTimeout) {
        this(baseUrl, apiKey, apiSecret, connectionTimeout, soTimeout, false, null);
    }

    public LegacyClient(final String baseUrl,
                        final String apiKey,
                        final String apiSecret,
                        final int connectionTimeout,
                        final int soTimeout,
                        boolean signRequests,
                        String signatureKey) {

        // Derive a http and a https version of the supplied base url
        if (baseUrl == null)
            throw new IllegalArgumentException("base url is null");
        String url = baseUrl.trim();
        String lc = url.toLowerCase();
        if (!lc.startsWith("https://"))
            throw new IllegalArgumentException("base url does not start with https://");

        this.baseUrl = url;
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
        this.connectionTimeout = connectionTimeout;
        this.soTimeout = soTimeout;
        this.signRequests = signRequests;
        this.signatureSecretKey = signatureKey;
    }

    /**
     * Parse a provided XML String and return the generated DOM Document.
     *
     * @param xml A String containing XML.
     * @return A Document generated from the parsed XML.
     * @throws NexmoResponseParseException If there is a problem initializing the XML parser or parsing the XML.
     */
    protected Document parseXml(String xml) throws NexmoResponseParseException {
        // TODO: Maybe an Error subclass for XML initialization errors, as these are serious and unexpected.
        Document doc;
        this.documentBuilderLock.lock();
        try {
            if (this.documentBuilder == null) {
                DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                this.documentBuilder = documentBuilderFactory.newDocumentBuilder();
            }
            doc = XmlUtil.parseXmlString(this.documentBuilder, xml);
        } catch (ParserConfigurationException e) {
            throw new NexmoResponseParseException("Exception initialing XML parser", e);
        } finally {
            this.documentBuilderLock.unlock();
        }
        return doc;
    }

    protected HttpClient getHttpClient() {
        if (this.httpClient == null) {
            this.httpClient = HttpClientUtils.getInstance(this.connectionTimeout, this.soTimeout).getNewHttpClient();
        }
        return this.httpClient;
    }

    protected List<NameValuePair> constructParams() {
        List<NameValuePair> params = new ArrayList<>();

        params.add(new BasicNameValuePair("api_key", this.apiKey));
        if (!this.signRequests) {
            params.add(new BasicNameValuePair("api_secret", this.apiSecret));
        }

        return params;
    }

    protected void signParams(List<NameValuePair> params) {
        if (this.signRequests) {
            RequestSigning.constructSignatureForRequestParameters(params, this.signatureSecretKey);
        }
    }

    protected String makeUrl(final String path) {
        if (path != null) {
            return this.baseUrl + path;
        } else {
            return this.baseUrl;
        }
    }

    // Allowing users of this client to plugin their own HttpClient.
    public void setHttpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }
}
