/*
 *   Copyright 2025 Vonage
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
import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.AbstractMap;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Abstract class to assist in implementing a call against a REST endpoint.
 * <p>
 * Concrete implementations must implement {@link #makeRequest(Object)} to construct a {@link RequestBuilder} from the
 * provided parameterized request object, and {@link #parseResponse(HttpResponse)} to construct the parameterized
 * {@link HttpResponse} object.
 * <p>
 * The REST call is executed by calling {@link #execute(Object)}.
 *
 * @param <RequestT> The request object type that will be used to construct the HTTP request body.
 * @param <ResultT>  The response object type which will be constructed from the returned HTTP response body.
 *
 * @see DynamicEndpoint for an abstract implementation which handles the most common use cases.
 */
public abstract class AbstractMethod<RequestT, ResultT> implements RestEndpoint<RequestT, ResultT> {
    private static final Logger LOGGER = Logger.getLogger(AbstractMethod.class.getName());

    private static boolean shouldLog() {
        return LOGGER.isLoggable(Level.INFO);
    }

    protected final HttpWrapper httpWrapper;

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
        HttpUriRequest httpRequest = applyAuth(makeRequest(request))
                .setHeader(HttpHeaders.USER_AGENT, httpWrapper.getUserAgent())
                .setCharset(StandardCharsets.UTF_8).build();

        if (shouldLog()) {
            LOGGER.info("Request " + httpRequest.getMethod() + " " + httpRequest.getURI());
            Header[] headers = httpRequest.getAllHeaders();
            if (headers != null && headers.length > 0) {
                StringBuilder headersStr = new StringBuilder("--- REQUEST HEADERS ---\n");
                for (Header header : headers) {
                    headersStr.append('\n').append(header.getName()).append(": ").append(header.getValue());
                }
                LOGGER.info(headersStr.toString());
            }
            LOGGER.info("Request body: " + request);
        }

        try (CloseableHttpResponse response = httpWrapper.getHttpClient().execute(httpRequest)) {
            try {
                if (shouldLog()) {
                    LOGGER.info("Response " + response.getStatusLine());
                    Header[] headers = response.getAllHeaders();
                    if (headers != null && headers.length > 0) {
                        StringBuilder headersStr = new StringBuilder("Response headers:\n");
                        for (Header header : headers) {
                            headersStr.append('\n').append(header.getName()).append(": ").append(header.getValue());
                        }
                        LOGGER.info(headersStr.toString());
                    }
                }

                ResultT responseBody = parseResponse(response);
                if (shouldLog()) {
                    LOGGER.info("Response body: " + responseBody);
                }

                return postProcessParsedResponse(responseBody);
            }
            catch (IOException iox) {
                LOGGER.log(Level.WARNING, "Failed to parse response", iox);
                throw new VonageResponseParseException(iox);
            }
        }
        catch (IOException iox) {
            LOGGER.log(Level.WARNING, "Failed to execute HTTP request", iox);
            throw new VonageMethodFailedException("Something went wrong while executing the HTTP request.", iox);
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
    final RequestBuilder applyAuth(RequestBuilder request) throws VonageClientException {
        AuthMethod am = getAuthMethod();
        if (am instanceof HeaderAuthMethod) {
            request.setHeader("Authorization", ((HeaderAuthMethod) am).getHeaderValue());
        }
        if (am instanceof QueryParamsAuthMethod) {
            RequestQueryParams qp = am instanceof ApiKeyQueryParamsAuthMethod ? null : normalRequestParams(request);
            ((QueryParamsAuthMethod) am).getAuthParams(qp).forEach(request::addParameter);
        }
        return request;
    }

    static RequestQueryParams normalRequestParams(RequestBuilder request) {
        return request.getParameters().stream()
                .map(nvp -> new AbstractMap.SimpleEntry<>(nvp.getName(), nvp.getValue()))
                .collect(Collectors.toCollection(RequestQueryParams::new));
    }

    /**
     * Gets the highest priority available authentication method according to its sort key.
     *
     * @return An AuthMethod created from the accepted auth methods.
     * @throws VonageUnexpectedException If no AuthMethod is available.
     */
    protected AuthMethod getAuthMethod() throws VonageUnexpectedException {
        return httpWrapper.getAuthCollection().getAcceptableAuthMethod(getAcceptableAuthMethods());
    }

    protected abstract Set<Class<? extends AuthMethod>> getAcceptableAuthMethods();

    /**
     * Construct and return a RequestBuilder instance from the provided request.
     *
     * @param request A RequestT representing input to the REST call to be made
     *
     * @return A ResultT representing the response from the executed REST call
     */
    protected abstract RequestBuilder makeRequest(RequestT request);

    /**
     * Construct a ResultT representing the contents of the HTTP response returned from the Vonage Voice API.
     *
     * @param response An HttpResponse returned from the Vonage Voice API
     *
     * @return A ResultT type representing the result of the REST call
     *
     * @throws IOException if a problem occurs parsing the response
     */
    protected abstract ResultT parseResponse(HttpResponse response) throws IOException;
}
