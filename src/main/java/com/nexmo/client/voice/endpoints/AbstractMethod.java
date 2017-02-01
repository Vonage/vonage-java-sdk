package com.nexmo.client.voice.endpoints;/*
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

import com.nexmo.client.HttpWrapper;
import com.nexmo.client.NexmoClientException;
import com.nexmo.client.NexmoUnexpectedException;
import com.nexmo.client.auth.AuthMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Set;

public abstract class AbstractMethod<RequestT, ResultT> implements Method<RequestT, ResultT> {
    private static final Log LOG = LogFactory.getLog(AbstractMethod.class);
    private final HttpWrapper httpWrapper;
    private Set<Class> acceptable;

    public AbstractMethod(HttpWrapper httpWrapper) {
        this.httpWrapper = httpWrapper;
    }

    // TODO: Consider wrapping IOException in a nexmo-specific transport exception.
    public ResultT execute(RequestT request) throws IOException, NexmoClientException {
        try {
            HttpResponse response = this.httpWrapper.getHttpClient().execute(applyAuth(makeRequest(request)).build());
            LOG.debug("Response: " + response.getStatusLine());
            return parseResponse(response);
        } catch (UnsupportedEncodingException uee) {
            throw new NexmoUnexpectedException("UTF-8 encoding is not supported by this JVM.", uee);
        }
    }

    protected RequestBuilder applyAuth(RequestBuilder request) throws NexmoClientException {
        return getAuthMethod(getAcceptableAuthMethods()).apply(request);
    }

    /**
     *
     *
     * Takes an array of classes, representing auth methods that are acceptable for this
     * @param acceptableAuthMethods
     * @return
     * @throws NexmoClientException
     */
    protected AuthMethod getAuthMethod(Class[] acceptableAuthMethods) throws NexmoClientException {
        if (acceptable == null) {
            this.acceptable = new HashSet<>();
            for (Class c : acceptableAuthMethods) {
                acceptable.add(c);
            }
        }

        return this.httpWrapper.getAuthMethods().getAcceptableAuthMethod(acceptable);
    }

    public void setHttpClient(HttpClient client) {
        this.httpWrapper.setHttpClient(client);
    }

    protected abstract Class[] getAcceptableAuthMethods();

    public abstract RequestBuilder makeRequest(RequestT request)
            throws NexmoClientException, UnsupportedEncodingException;

    public abstract ResultT parseResponse(HttpResponse response) throws IOException;
}
