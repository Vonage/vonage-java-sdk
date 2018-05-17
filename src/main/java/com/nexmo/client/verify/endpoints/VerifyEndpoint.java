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
package com.nexmo.client.verify.endpoints;

import com.nexmo.client.HttpWrapper;
import com.nexmo.client.NexmoClientException;
import com.nexmo.client.NexmoResponseParseException;
import com.nexmo.client.auth.SignatureAuthMethod;
import com.nexmo.client.auth.TokenAuthMethod;
import com.nexmo.client.legacyutils.XmlParser;
import com.nexmo.client.verify.VerifyRequest;
import com.nexmo.client.verify.VerifyResult;
import com.nexmo.client.voice.endpoints.AbstractMethod;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Locale;

/**
 * @deprecated Relies on XML Endpoint, use {@link com.nexmo.client.verify.VerifyEndpoint}
 */
@Deprecated
public class VerifyEndpoint extends AbstractMethod<VerifyRequest, VerifyResult> {
    private static final Class[] ALLOWED_AUTH_METHODS = new Class[]{SignatureAuthMethod.class, TokenAuthMethod.class};

    private static final String DEFAULT_URI = "https://api.nexmo.com/verify/xml";

    private XmlParser xmlParser = new XmlParser();

    private String uri = DEFAULT_URI;

    /**
     * Create a new VerifyEndpoint.
     * <p>
     * This client is used for calling the verify API's verify endpoint.
     */
    public VerifyEndpoint(HttpWrapper httpWrapper) {
        super(httpWrapper);
    }

    @Override
    protected Class[] getAcceptableAuthMethods() {
        return ALLOWED_AUTH_METHODS;
    }

    @Override
    public RequestBuilder makeRequest(VerifyRequest request) throws NexmoClientException, UnsupportedEncodingException {
        RequestBuilder result = RequestBuilder.post(this.uri)
                .addParameter("number", request.getNumber())
                .addParameter("brand", request.getBrand());

        if (request.getFrom() != null) {
            result.addParameter("sender_id", request.getFrom());
        }

        if (request.getLength() > 0) {
            result.addParameter("code_length", Integer.toString(request.getLength()));
        }

        if (request.getLocale() != null) {
            result.addParameter(new BasicNameValuePair("lg",
                    (request.getLocale().getLanguage() + "-" + request.getLocale().getCountry()).toLowerCase()));
        }

        if (request.getType() != null) {
            result.addParameter("require_type", request.getType().toString());
        }

        if (request.getCountry() != null) {
            result.addParameter("country", request.getCountry());
        }

        if (request.getPinExpiry() != null) {
            result.addParameter("pin_expiry", request.getPinExpiry().toString());
        }

        if (request.getNextEventWait() != null) {
            result.addParameter("next_event_wait", request.getNextEventWait().toString());
        }

        return result;
    }

    @Override
    public VerifyResult parseResponse(HttpResponse response) throws IOException {
        return parseVerifyResponse(new BasicResponseHandler().handleResponse(response));
    }

    public VerifyResult verify(final String number,
                               final String brand) throws IOException,
                                                          NexmoClientException {
        return execute(new VerifyRequest(number, brand));
    }

    public VerifyResult verify(final String number,
                               final String brand,
                               final String from) throws IOException,
                                                         NexmoClientException {
        return execute(new VerifyRequest(number, brand, from));
    }

    public VerifyResult verify(final String number,
                               final String brand,
                               final String from,
                               final int length,
                               final Locale locale) throws IOException,
                                                           NexmoClientException {
        return execute(new VerifyRequest(number, brand, from, length, locale));
    }

    public VerifyResult verify(final String number,
                               final String brand,
                               final String from,
                               final int length,
                               final Locale locale,
                               final VerifyRequest.LineType type) throws IOException,
                                                                         NexmoClientException {
        return execute(new VerifyRequest(number, brand, from, length, locale, type));
    }

    public VerifyResult verify(VerifyRequest request) throws IOException, NexmoClientException {
        return execute(request);
    }

    protected VerifyResult parseVerifyResponse(String response) throws NexmoResponseParseException {
        Document doc = xmlParser.parseXml(response);

        Element root = doc.getDocumentElement();
        if (!"verify_response".equals(root.getNodeName()))
            throw new NexmoResponseParseException("No valid response found [ " + response + "] ");

        return SharedParsers.parseVerifyResponseXmlNode(root);
    }
}
