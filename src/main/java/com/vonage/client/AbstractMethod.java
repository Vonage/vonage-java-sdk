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
 * @param <REQ> The request object type that will be used to construct the HTTP request body.
 * @param <RES>  The response object type which will be constructed from the returned HTTP response body.
 *
 * @see DynamicEndpoint for an abstract implementation which handles the most common use cases.
 */
public abstract class AbstractMethod<REQ, RES> implements RestEndpoint<REQ, RES> {
    private static final Logger LOGGER = Logger.getLogger(AbstractMethod.class.getName());
    private static final Level LOG_LEVEL = Level.FINE;

    private static boolean shouldLog() {
        return LOGGER.isLoggable(LOG_LEVEL);
    }

    private final HttpWrapper httpWrapper;

    /**
     * Construct a new AbstractMethod instance with the given HTTP client.
     *
     * @param httpWrapper The wrapper containing the HTTP client and configuration.
     */
    protected AbstractMethod(HttpWrapper httpWrapper) {
        this.httpWrapper = httpWrapper;
    }

    /**
     * Gets the underlying HTTP client wrapper.
     *
     * @return The {@link HttpWrapper} used by this endpoint.
     */
    public HttpWrapper getHttpWrapper() {
        return httpWrapper;
    }

    /**
     * Method which allows further modification of the response object after it has been parsed.
     *
     * @param response The unmarshalled response object.
     *
     * @return The final result object to return; usually the same object that was passed in.
     */
    protected RES postProcessParsedResponse(RES response) {
        return response;
    }

    private HttpUriRequest createFullHttpRequest(REQ request) throws VonageClientException {
        return applyAuth(makeRequest(request))
                .setHeader(HttpHeaders.USER_AGENT, httpWrapper.getUserAgent())
                .setCharset(StandardCharsets.UTF_8).build();
    }

    /**
     * Executes the REST call represented by this endpoint.
     *
     * @param request The request object representing input to the REST call to be made.
     *
     * @return The result object representing the response from the executed REST call.
     *
     * @throws VonageResponseParseException if there was a problem parsing the HTTP response.
     * @throws VonageMethodFailedException if there was a problem executing the HTTP request.
     */
    @Override
    public RES execute(REQ request) throws VonageMethodFailedException, VonageResponseParseException {
        final HttpUriRequest httpRequest = createFullHttpRequest(request);

        if (shouldLog()) {
            LOGGER.log(LOG_LEVEL, "Request " + httpRequest.getMethod() + " " + httpRequest.getURI());
            Header[] headers = httpRequest.getAllHeaders();
            if (headers != null && headers.length > 0) {
                StringBuilder headersStr = new StringBuilder("--- REQUEST HEADERS ---");
                for (Header header : headers) {
                    headersStr.append('\n').append(header.getName()).append(": ").append(header.getValue());
                }
                LOGGER.log(LOG_LEVEL, headersStr.toString());
            }
            if (request != null) {
                LOGGER.log(LOG_LEVEL, "--- REQUEST BODY ---\n" + request);
            }
        }

        try (final CloseableHttpResponse response = httpWrapper.getHttpClient().execute(httpRequest)) {
            try {
                if (shouldLog()) {
                    LOGGER.log(LOG_LEVEL, "Response " + response.getStatusLine());
                    Header[] headers = response.getAllHeaders();
                    if (headers != null && headers.length > 0) {
                        StringBuilder headersStr = new StringBuilder("--- RESPONSE HEADERS ---");
                        for (Header header : headers) {
                            headersStr.append('\n').append(header.getName()).append(": ").append(header.getValue());
                        }
                        LOGGER.log(LOG_LEVEL, headersStr.toString());
                    }
                }

                final RES responseBody = parseResponse(response);
                if (responseBody != null && shouldLog()) {
                    LOGGER.log(LOG_LEVEL, "--- RESPONSE BODY ---\n" + responseBody);
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
     * Apply an appropriate authentication method (specified by {@link #getAcceptableAuthMethods()}) to the
     * provided {@link RequestBuilder}, and return the result.
     *
     * @param request A RequestBuilder which has not yet had authentication information applied.
     *
     * @return A RequestBuilder with appropriate authentication information applied
     * (may or not be the same instance as <pre>request</pre>).
     *
     * @throws VonageClientException If no appropriate {@link AuthMethod} is available.
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

    /**
     * Gets applicable authentication methods for this endpoint.
     *
     * @return The set of acceptable authentication method classes (at least one must be provided).
     */
    protected abstract Set<Class<? extends AuthMethod>> getAcceptableAuthMethods();

    /**
     * Construct and return a RequestBuilder instance from the provided request.
     *
     * @param request A request object representing input to the REST call to be made.
     *
     * @return A RequestBuilder instance representing the HTTP request to be made.
     */
    protected abstract RequestBuilder makeRequest(REQ request);

    /**
     * Construct a response object representing the contents of the HTTP response returned from the Vonage API.
     *
     * @param response An HttpResponse returned from the Vonage API.
     *
     * @return The unmarshalled result of the REST call.
     *
     * @throws IOException if a problem occurs parsing the response.
     */
    protected abstract RES parseResponse(HttpResponse response) throws IOException;
}
