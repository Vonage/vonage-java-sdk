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

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.vonage.client.auth.ApiKeyAuthMethod;
import com.vonage.client.auth.JWTAuthMethod;
import com.vonage.client.auth.NoAuthMethod;
import com.vonage.client.common.HttpMethod;
import org.apache.http.HttpResponse;
import java.util.Collection;
import java.util.Map;

/**
 * Client for making custom requests to Vonage APIs unsupported by this SDK.
 * This is useful for testing out beta APIs or making custom requests where the SDK falls short.
 * You can specify the HTTP method, endpoint URL, request body parameters and response object to parse.
 * The implementation is based on {@link DynamicEndpoint}.
 * <p>
 * The supported request and response types (i.e. the {@code <T>} and {@code <R>} generics)
 * should be instances of {@link Jsonable}. See the {@linkplain DynamicEndpoint#parseResponse(HttpResponse)}
 * method for how deserialisation is handled.
 * <p>
 * The valid types for the return type parameter {@code <R>} are generally:
 * <ul>
 *     <li>{@link Jsonable} - for parsing the response body into a JSON object</li>
 *     <li>{@link Map} - for parsing the response body as JSON into a tree structure</li>
 *     <li>{@link Collection} - for parsing the response body as JSON into a list of objects</li>
 *     <li>{@link Void} - for ignoring the response body</li>
 *     <li>{@code byte[]} - for parsing the response body as binary</li>
 *     <li>{@link String} - for returning the response body directly as a string</li>
 * </ul>
 *
 * @since 9.1.0
 */
@SuppressWarnings("unchecked")
public class CustomClient {
    private final HttpWrapper httpWrapper;

    /**
     * Wrapper for converting Map to JSON and vice versa.
     */
    static final class JsonableMap extends JsonableBaseObject {
        @JsonAnyGetter @JsonAnySetter Map<String, Object> body;

        JsonableMap(Map<String, ?> body) {
            this.body = (Map<String, Object>) body;
        }
    }

    /**
     * Constructor for creating a custom client.
     *
     * @param httpWrapper Shared HTTP wrapper object and configuration used for making REST calls.
     */
    public CustomClient(HttpWrapper httpWrapper) {
        this.httpWrapper = httpWrapper;
    }

    /**
     * Most flexible method for making custom requests. This advanced option should only be used
     * if you are familiar with the underlying {@linkplain DynamicEndpoint} implementation.
     *
     * @param requestMethod The HTTP method to use for the request.
     * @param url Absolute URL to send the request to as a string.
     * @param requestBody The payload to send in the request body.
     * @param responseType Hack for type inference. Do not provide this field (especially, DO NOT pass {@code null}).
     *
     * @return The parsed response object, or {@code null} if absent / not applicable.
     * @throws VonageApiResponseException If the HTTP response code is >= 400.
     *
     * @param <T> The request body type.
     * @param <R> The response body type.
     */
    public <T, R> R makeRequest(HttpMethod requestMethod, String url, T requestBody, R... responseType) {
        return DynamicEndpoint.<T, R> builder(fixResponseType(responseType))
                .wrapper(httpWrapper).requestMethod(requestMethod)
                .authMethod(JWTAuthMethod.class, ApiKeyAuthMethod.class, NoAuthMethod.class)
                .pathGetter((de, req) -> url)
                .build().execute(requestBody);
    }

    private <R> R[] fixResponseType(R... responseType) {
        return responseType == null || Object.class.equals(responseType.getClass().getComponentType()) ?
                (R[]) new Void[0] : responseType;
    }

    /**
     * Convenience method for making DELETE requests.
     * In most cases, you should assign the return value to Void.
     *
     * @param url URL to send the request to as a string.
     * @param responseType Hack for type inference. Do not provide this field (especially, DO NOT pass {@code null}).
     *
     * @return The parsed response object, or {@code null} if absent / not applicable.
     * @throws VonageApiResponseException If the HTTP response code is >= 400.
     *
     * @param <R> The response body type, most likely {@linkplain Void}.
     */
    public <R> R delete(String url, R... responseType) {
        return makeRequest(HttpMethod.DELETE, url, null, responseType);
    }

    /**
     * Convenience method for making GET requests.
     *
     * @param url URL to send the request to as a string.
     * @param responseType Hack for type inference. Do not provide this field (especially, DO NOT pass {@code null}).
     *
     * @return The parsed response object.
     * @throws VonageApiResponseException If the HTTP response code is >= 400.
     *
     * @param <R> The response body type.
     */
    public <R> R get(String url, R... responseType) {
        return makeRequest(HttpMethod.GET, url, null, responseType);
    }

    /**
     * Convenience method for making POST requests.
     *
     * @param url URL to send the request to as a string.
     * @param requestBody The payload to send in the request body.
     * @param responseType Hack for type inference. Do not provide this field (especially, DO NOT pass {@code null}).
     *
     * @return The parsed response object, or {@code null} if absent / not applicable.
     * @throws VonageApiResponseException If the HTTP response code is >= 400.
     *
     * @param <R> The response body type.
     */
    public <R> R post(String url, Jsonable requestBody, R... responseType) {
        return makeRequest(HttpMethod.POST, url, requestBody, responseType);
    }

    /**
     * Convenience method for making JSON-based POST requests.
     *
     * @param url URL to send the request to as a string.
     * @param requestBody The payload to convert to JSON and send in the request body.
     * @param responseType Hack for type inference. Do not provide this field (especially, DO NOT pass {@code null}).
     *
     * @return The parsed response object, or {@code null} if absent / not applicable.
     * @throws VonageApiResponseException If the HTTP response code is >= 400.
     *
     * @param <R> The response body type.
     */
    public <R> R post(String url, Map<String, ?> requestBody, R... responseType) {
        return post(url, new JsonableMap(requestBody), responseType);
    }

    /**
     * Convenience method for making PUT requests.
     *
     * @param url URL to send the request to as a string.
     * @param requestBody The payload to send in the request body.
     * @param responseType Hack for type inference. Do not provide this field (especially, DO NOT pass {@code null}).
     *
     * @return The parsed response object, or {@code null} if absent / not applicable.
     * @throws VonageApiResponseException If the HTTP response code is >= 400.
     *
     * @param <R> The response body type.
     */
    public <R> R put(String url, Jsonable requestBody, R... responseType) {
        return makeRequest(HttpMethod.PUT, url, requestBody, responseType);
    }

    /**
     * Convenience method for making JSON-based PUT requests.
     *
     * @param url URL to send the request to as a string.
     * @param requestBody The payload to convert to JSON and send in the request body.
     * @param responseType Hack for type inference. Do not provide this field (especially, DO NOT pass {@code null}).
     *
     * @return The parsed response object, or {@code null} if absent / not applicable.
     * @throws VonageApiResponseException If the HTTP response code is >= 400.
     *
     * @param <R> The response body type.
     */
    public <R> R put(String url, Map<String, ?> requestBody, R... responseType) {
        return put(url, new JsonableMap(requestBody), responseType);
    }

    /**
     * Convenience method for making PATCH requests.
     *
     * @param url URL to send the request to as a string.
     * @param requestBody The payload to send in the request body.
     * @param responseType Hack for type inference. Do not provide this field (especially, DO NOT pass {@code null}).
     *
     * @return The parsed response object, or {@code null} if absent / not applicable.
     * @throws VonageApiResponseException If the HTTP response code is >= 400.
     *
     * @param <R> The response body type.
     */
    public <R> R patch(String url, Jsonable requestBody, R... responseType) {
        return makeRequest(HttpMethod.PATCH, url, requestBody, responseType);
    }

    /**
     * Convenience method for making JSON-based PATCH requests.
     *
     * @param url URL to send the request to as a string.
     * @param requestBody The payload to convert to JSON and send in the request body.
     * @param responseType Hack for type inference. Do not provide this field (especially, DO NOT pass {@code null}).
     *
     * @return The parsed response object, or {@code null} if absent / not applicable.
     * @throws VonageApiResponseException If the HTTP response code is >= 400.
     *
     * @param <R> The response body type.
     */
    public <R> R patch(String url, Map<String, ?> requestBody, R... responseType) {
        return patch(url, new JsonableMap(requestBody), responseType);
    }
}
