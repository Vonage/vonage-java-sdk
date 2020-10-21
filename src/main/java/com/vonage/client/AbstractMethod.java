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
package com.vonage.client;

import com.vonage.client.auth.AuthMethod;
import com.vonage.client.logging.LoggingUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Abstract class to assist in implementing a call against a REST endpoint.
 * <p>
 * Concrete implementations must implement {@link #makeRequest(Object)} to construct a {@link RequestBuilder} from the
 * provided parameterized request object, and {@link #parseResponse(HttpResponse)} to construct the parameterized {@link
 * HttpResponse} object.
 * <p>
 * The REST call is executed by calling {@link #execute(Object)}.
 *
 * @param <RequestT> The type of the method-specific request object that will be used to construct an HTTP request
 * @param <ResultT>  The type of method-specific response object which will be constructed from the returned HTTP
 *                   response
 */
public abstract class AbstractMethod<RequestT, ResultT> implements Method<RequestT, ResultT> {
    private static final Log LOG = LogFactory.getLog(AbstractMethod.class);

    protected final HttpWrapper httpWrapper;
    private Set<Class> acceptable;

    public AbstractMethod(HttpWrapper httpWrapper) {
        this.httpWrapper = httpWrapper;
    }

    /**
     * Execute the REST call represented by this method object.
     *
     * @param request A RequestT representing input to the REST call to be made
     *
     * @return A ResultT representing the response from the executed REST call
     *
     * @throws VonageClientException if there is a problem parsing the HTTP response
     */
    public ResultT execute(RequestT request) throws VonageResponseParseException, VonageClientException {
        try {
            RequestBuilder requestBuilder = applyAuth(makeRequest(request));
            HttpUriRequest httpRequest = requestBuilder.build();

            // If we have a URL Encoded form entity, we may need to regenerate it as UTF-8
            // due to a bug (or two!) in RequestBuilder:
            //
            // This fix can be removed when HttpClient is upgraded to 4.5, although 4.5 also
            // has a bug where RequestBuilder.put(uri) and RequestBuilder.post(uri) use the
            // wrong encoding, whereas RequestBuilder.put().setUri(uri) uses UTF-8.
            // - MS 2017-04-12
            if (httpRequest instanceof HttpEntityEnclosingRequest) {
                HttpEntityEnclosingRequest entityRequest = (HttpEntityEnclosingRequest) httpRequest;
                HttpEntity entity = entityRequest.getEntity();
                if (entity instanceof UrlEncodedFormEntity) {
                    entityRequest.setEntity(new UrlEncodedFormEntity(requestBuilder.getParameters(),
                            Charset.forName("UTF-8")
                    ));
                }
            }
            LOG.debug("Request: " + httpRequest);
            if (LOG.isDebugEnabled() && httpRequest instanceof HttpEntityEnclosingRequestBase) {
                HttpEntityEnclosingRequestBase enclosingRequest = (HttpEntityEnclosingRequestBase) httpRequest;
                LOG.debug(EntityUtils.toString(enclosingRequest.getEntity()));
            }
            HttpResponse response = httpWrapper.getHttpClient().execute(httpRequest);

            LOG.debug("Response: " + LoggingUtils.logResponse(response));

            try{
                return parseResponse(response);
            }
            catch (IOException io){
                throw new VonageResponseParseException("Unable to parse response.", io);
            }
        } catch (UnsupportedEncodingException uee) {
            throw new VonageUnexpectedException("UTF-8 encoding is not supported by this JVM.", uee);
        } catch (IOException io) {
            throw new VonageMethodFailedException("Something went wrong while executing the HTTP request: " +
                    io.getMessage() + ".", io);
        }
    }

    /**
     * Apply an appropriate authentication method (specified by {@link #getAcceptableAuthMethods()} to the provided
     * {@link RequestBuilder}, and return the result.
     *
     * @param request A RequestBuilder which has not yet had authentication information applied
     *
     * @return A RequestBuilder with appropriate authentication information applied (may or not be the same instance as
     * <pre>request</pre>)
     *
     * @throws VonageClientException If no appropriate {@link AuthMethod} is available
     */
    protected RequestBuilder applyAuth(RequestBuilder request) throws VonageClientException {
        return getAuthMethod(getAcceptableAuthMethods()).apply(request);
    }

    /**
     * Utility method for obtaining an appropriate {@link AuthMethod} for this call.
     *
     * @param acceptableAuthMethods an array of classes, representing authentication methods that are acceptable for
     *                              this endpoint
     *
     * @return An AuthMethod created from one of the provided acceptableAuthMethods.
     *
     * @throws VonageClientException If no AuthMethod is available from the provided array of acceptableAuthMethods.
     */
    protected AuthMethod getAuthMethod(Class[] acceptableAuthMethods) throws VonageClientException {
        if (acceptable == null) {
            acceptable = new HashSet<>();
            Collections.addAll(acceptable, acceptableAuthMethods);
        }

        return httpWrapper.getAuthCollection().getAcceptableAuthMethod(acceptable);
    }

    public void setHttpClient(HttpClient client) {
        httpWrapper.setHttpClient(client);
    }

    protected abstract Class[] getAcceptableAuthMethods();

    /**
     * Construct and return a RequestBuilder instance from the provided request.
     *
     * @param request A RequestT representing input to the REST call to be made
     *
     * @return A ResultT representing the response from the executed REST call
     *
     * @throws UnsupportedEncodingException if UTF-8 encoding is not supported by the JVM
     */
    public abstract RequestBuilder makeRequest(RequestT request) throws UnsupportedEncodingException;

    /**
     * Construct a ResultT representing the contents of the HTTP response returned from the Vonage Voice API.
     *
     * @param response An HttpResponse returned from the Vonage Voice API
     *
     * @return A ResultT type representing the result of the REST call
     *
     * @throws IOException if a problem occurs parsing the response
     */
    public abstract ResultT parseResponse(HttpResponse response) throws IOException;
}
