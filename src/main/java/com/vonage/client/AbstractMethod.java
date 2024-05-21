/*
 *   Copyright 2024 Vonage
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

import com.vonage.client.auth.*;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.BasicResponseHandler;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

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
public abstract class AbstractMethod<RequestT, ResultT> implements RestEndpoint<RequestT, ResultT> {
    static {
        LogFactory.getLog(AbstractMethod.class);
    }

    protected static final BasicResponseHandler basicResponseHandler = new BasicResponseHandler();

    protected final HttpWrapper httpWrapper;
    private Set<Class<? extends AuthMethod>> acceptable;

    public AbstractMethod(HttpWrapper httpWrapper) {
        this.httpWrapper = httpWrapper;
    }

    public HttpWrapper getHttpWrapper() {
        return httpWrapper;
    }

    protected ResultT postProcessParsedResponse(ResultT response) {
        return response;
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
    @Override
    public ResultT execute(RequestT request) throws VonageResponseParseException, VonageClientException {
        try {
            HttpUriRequest httpRequest = applyAuth(makeRequest(request))
                    .setHeader("User-Agent", httpWrapper.getUserAgent())
                    .setCharset(StandardCharsets.UTF_8)
                    .build();

            HttpResponse response = httpWrapper.getHttpClient().execute(httpRequest);
            try {
                return postProcessParsedResponse(parseResponse(response));
            }
            catch (IOException io) {
                throw new VonageResponseParseException("Unable to parse response.", io);
            }
        }
        catch (UnsupportedEncodingException uee) {
            throw new VonageUnexpectedException("UTF-8 encoding is not supported by this JVM.", uee);
        }
        catch (IOException io) {
            throw new VonageMethodFailedException("Something went wrong while executing the HTTP request: " +
                    io.getMessage() + ".", io);
        }
    }

    /**
     * Apply an appropriate authentication method (specified by {@link #getAcceptableAuthMethods()}) to the provided
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
        AuthMethod am = getAuthMethod();

        if (am instanceof SignatureAuthMethod) {
            Map<String, String> params = request.getParameters().stream().collect(Collectors.toMap(
                    NameValuePair::getName,
                    NameValuePair::getValue,
                    (v1, v2) -> v1,
                    TreeMap::new
            ));
            ((SignatureAuthMethod) am).apply(params);
            request.getParameters().clear();
            params.forEach(request::addParameter);
        }
        else if (am instanceof HeaderAuthMethod) {
            return request.setHeader("Authorization", ((HeaderAuthMethod) am).getHeaderValue());
        }
        else if (am instanceof QueryParamsAuthMethod) {
            ((QueryParamsAuthMethod) am).getQueryParams().forEach(request::addParameter);
        }
        return request;
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
    @SuppressWarnings("unchecked")
    private AuthMethod getAuthMethod(Class<?>[] acceptableAuthMethods) throws VonageClientException {
        if (acceptable == null) {
            acceptable = Arrays.stream(acceptableAuthMethods)
                    .filter(AuthMethod.class::isAssignableFrom)
                    .map(c -> (Class<? extends AuthMethod>) c)
                    .collect(Collectors.toSet());
        }

        return httpWrapper.getAuthCollection().getAcceptableAuthMethod(acceptable);
    }

    /**
     * Gets the highest priority available authentication method according to {@link AuthMethod#getSortKey()}.
     *
     * @return An AuthMethod created from the accepted auth methods.
     * @throws VonageUnexpectedException If no AuthMethod is available.
     */
    protected AuthMethod getAuthMethod() throws VonageUnexpectedException {
        return getAuthMethod(getAcceptableAuthMethods());
    }

    /**
     * Utility method for obtaining the Application ID or API key from the auth method.
     *
     * @return The Application ID or API key.
     * @throws VonageUnexpectedException If no AuthMethod is available.
     * @throws IllegalStateException If the AuthMethod does not have an Application ID or API key.
     */
    public String getApplicationIdOrApiKey() throws VonageUnexpectedException {
        AuthMethod am = getAuthMethod();
        if (am instanceof JWTAuthMethod) {
            return ((JWTAuthMethod) am).getApplicationId();
        }
        if (am instanceof ApiKeyHeaderAuthMethod) {
            return ((ApiKeyHeaderAuthMethod) am).getApiKey();
        }
        if (am instanceof SignatureAuthMethod) {
            return ((SignatureAuthMethod) am).getApiKey();
        }
        throw new IllegalStateException(am.getClass().getSimpleName() + " does not have API key.");
    }

    protected abstract Class<?>[] getAcceptableAuthMethods();

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
