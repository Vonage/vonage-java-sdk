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

import com.vonage.client.auth.AuthMethod;
import com.vonage.client.common.HttpMethod;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import java.io.IOException;
import java.net.URI;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Enables convenient declaration of endpoints without directly implementing {@link AbstractMethod}.
 * This decouples the endpoint's implementation from the underlying HTTP library.
 *
 * @param <T> The request body type.
 * @param <R> The response body type.
 *
 * @since 7.7.0
 */
@SuppressWarnings("unchecked")
public class DynamicEndpoint<T, R> extends AbstractMethod<T, R> {
	protected final Logger logger = Logger.getLogger(getClass().getName());

	protected final Set<Class<? extends AuthMethod>> authMethods;
	protected final String contentType, accept;
	protected final HttpMethod requestMethod;
	protected final BiFunction<DynamicEndpoint<T, R>, ? super T, String> pathGetter;
	protected final Class<? extends VonageApiResponseException> responseExceptionType;
	protected final Class<R> responseType;
	protected T cachedRequestBody;

	protected DynamicEndpoint(Builder<T, R> builder) {
		super(builder.wrapper);
		authMethods = Objects.requireNonNull(builder.authMethods, "At least one auth method must be defined.");
		requestMethod = Objects.requireNonNull(builder.requestMethod, "HTTP request method is required.");
		pathGetter = Objects.requireNonNull(builder.pathGetter, "Path function is required.");
		if ((responseType = builder.responseType) == Object.class) {
			throw new IllegalStateException(
					"Could not infer the response type." +
					"Please provide it explicitly, or do not use var when assigning the result."
			);
		}
		responseExceptionType = builder.responseExceptionType;
		contentType = builder.contentType;
        accept = builder.accept == null &&
				(Jsonable.class.isAssignableFrom(responseType) || isJsonableArrayResponse()) ?
				ContentType.APPLICATION_JSON.getMimeType() : builder.accept;
	}

	/**
	 * This trick enables initialisation of the builder whilst inferring the response type {@code <R>}
	 * without directly providing the class by using varargs as a parameter. See usages for examples.
	 *
	 * @param responseType The response type array, not provided directly but via a varargs parameter.
	 *
	 * @return A new Builder.
	 *
	 * @param <T> The request type.
	 * @param <R> The response type.
	 * @since 7.9.0
	 */
	public static <T, R> Builder<T, R> builder(R[] responseType) {
		return builder((Class<R>) responseType.getClass().getComponentType());
	}

	public static <T, R> Builder<T, R> builder(Class<R> responseType) {
		return new Builder<>(responseType);
	}

	public static final class Builder<T, R> {
		private final Class<R> responseType;
		private Set<Class<? extends AuthMethod>> authMethods;
		private HttpWrapper wrapper;
		private String contentType, accept;
		private HttpMethod requestMethod;
		private BiFunction<DynamicEndpoint<T, R>, ? super T, String> pathGetter;
		private Class<? extends VonageApiResponseException> responseExceptionType;

		Builder(Class<R> responseType) {
			this.responseType = Objects.requireNonNull(responseType, "Response type class cannot be null.");
		}

		public Builder<T, R> wrapper(HttpWrapper wrapper) {
			this.wrapper = wrapper;
			return this;
		}

		public Builder<T, R> requestMethod(HttpMethod requestMethod) {
			this.requestMethod = requestMethod;
			return this;
		}

		public Builder<T, R> pathGetter(BiFunction<DynamicEndpoint<T, R>, T, String> pathGetter) {
			this.pathGetter = pathGetter;
			return this;
		}

		public Builder<T, R> authMethod(Class<? extends AuthMethod> primary, Class<? extends AuthMethod>... others) {
			authMethods = new LinkedHashSet<>(2);
			authMethods.add(Objects.requireNonNull(primary, "Primary auth method cannot be null."));
			if (others != null) {
				for (Class<? extends AuthMethod> amc : others) {
					if (amc != null) {
						authMethods.add(amc);
					}
				}
			}
			return this;
		}

		public Builder<T, R> responseExceptionType(Class<? extends VonageApiResponseException> responseExceptionType) {
			this.responseExceptionType = responseExceptionType;
			return this;
		}

		public Builder<T, R> urlFormEncodedContentType(boolean formEncoded) {
			return contentTypeHeader(formEncoded ? "application/x-www-form-urlencoded" : null);
		}

		public Builder<T, R> contentTypeHeader(String contentType) {
			this.contentType = contentType;
			return this;
		}

		public Builder<T, R> acceptHeader(String accept) {
			this.accept = accept;
			return this;
		}

		public DynamicEndpoint<T, R> build() {
			return new DynamicEndpoint<>(this);
		}
	}

	static RequestBuilder createRequestBuilderFromRequestMethod(HttpMethod requestMethod) {
		switch (requestMethod) {
			default: return RequestBuilder.get();
			case POST: return RequestBuilder.post();
			case PATCH: return RequestBuilder.patch();
			case DELETE: return RequestBuilder.delete();
			case PUT: return RequestBuilder.put();
		}
	}

	@Override
	protected final Set<Class<? extends AuthMethod>> getAcceptableAuthMethods() {
		return authMethods;
	}

	private boolean isJsonableArrayResponse() {
		return responseType.isArray() && Jsonable.class.isAssignableFrom(responseType.getComponentType());
	}

	private String getRequestHeader(T requestBody) {
		if (contentType != null) {
			return contentType;
		}
		else if (requestBody instanceof Jsonable) {
			return ContentType.APPLICATION_JSON.getMimeType();
		}
		else if (requestBody instanceof BinaryRequest) {
			return ((BinaryRequest) requestBody).getContentType();
		}
		else {
			return null;
		}
	}

	private static void applyQueryParams(Map<String, ?> params, RequestBuilder rqb) {
		params.forEach((k, v) -> {
			Consumer<Object> logic = obj -> rqb.addParameter(k, String.valueOf(obj));
			if (v instanceof Object[]) {
				for (Object nested : (Object[]) v) {
					logic.accept(nested);
				}
			}
			else if (v instanceof Iterable<?>) {
				for (Object nested : (Iterable<?>) v) {
					logic.accept(nested);
				}
			}
			else {
				logic.accept(v);
			}
		});
	}

	public static URI buildUri(String base, Map<String, ?> requestParams) {
		RequestBuilder requestBuilder = RequestBuilder.get(base);
		applyQueryParams(requestParams, requestBuilder);
		return requestBuilder.build().getURI();
	}

	@Override
	protected final RequestBuilder makeRequest(T requestBody) {
		if (requestBody instanceof Jsonable && responseType.isAssignableFrom(requestBody.getClass())) {
			cachedRequestBody = requestBody;
		}
		RequestBuilder rqb = createRequestBuilderFromRequestMethod(requestMethod);
		String header = getRequestHeader(requestBody);
		if (header != null) {
			rqb.setHeader("Content-Type", header);
		}
		if (accept != null) {
			rqb.setHeader("Accept", accept);
		}
		if (requestBody instanceof QueryParamsRequest) {
			applyQueryParams(((QueryParamsRequest) requestBody).makeParams(), rqb);
		}
		if (requestBody instanceof Jsonable) {
			rqb.setEntity(new StringEntity(((Jsonable) requestBody).toJson(), ContentType.APPLICATION_JSON));
		}
		else if (requestBody instanceof BinaryRequest) {
			BinaryRequest bin = (BinaryRequest) requestBody;
			rqb.setEntity(new ByteArrayEntity(bin.toByteArray(), ContentType.getByMimeType(bin.getContentType())));
		}
		else if (requestBody instanceof byte[]) {
			rqb.setEntity(new ByteArrayEntity((byte[]) requestBody));
		}
		return rqb.setUri(pathGetter.apply(this, requestBody));
	}

	@Override
	protected final R parseResponse(HttpResponse response) throws IOException {
		StatusLine statusLine = response.getStatusLine();
		int statusCode = statusLine.getStatusCode();
		logger.fine(() -> "Response status: " + statusCode);
		try {
			if (statusCode < 200) {
				logger.info(statusLine::getReasonPhrase);
				return null;
			}
			if (statusCode < 300) {
				return parseResponseSuccess(response);
			}
			if (statusCode < 400) {
				return parseResponseRedirect(response);
			}
			else {
				return parseResponseFailure(response);
			}
		}
		finally {
			cachedRequestBody = null;
		}
	}

	protected R parseResponseFromString(String response) {
		return null;
	}

	private R parseResponseRedirect(HttpResponse response) throws IOException {
		final String location = response.getFirstHeader("Location").getValue();
		logger.fine(() -> "Redirect: " + location);

		if (java.net.URI.class.equals(responseType)) {
			return (R) URI.create(location);
		}
		else if (String.class.equals(responseType)) {
			return (R) location;
		}
		else {
			return parseResponseSuccess(response);
		}
	}

	private R parseResponseSuccess(HttpResponse response) throws IOException {
		if (Void.class.equals(responseType)) {
			logger.fine(() -> "No response body.");
			return null;
		}
		else if (byte[].class.equals(responseType)) {
			byte[] result = EntityUtils.toByteArray(response.getEntity());
			logger.fine(() -> "Binary response body of length " + result.length);
			return (R) result;
		}
		else {
			String deser = EntityUtils.toString(response.getEntity());
			logger.fine(() -> deser);

			if (responseType.equals(String.class)) {
				return (R) deser;
			}

			if (cachedRequestBody instanceof Jsonable) {
				((Jsonable) cachedRequestBody).updateFromJson(deser);
				return (R) cachedRequestBody;
			}

			if (Jsonable.class.isAssignableFrom(responseType)) {
				return (R) Jsonable.fromJson(deser, (Class<? extends Jsonable>) responseType);
			}
			else if (
					Map.class.isAssignableFrom(responseType) ||
					Collection.class.isAssignableFrom(responseType) ||
					isJsonableArrayResponse()
			) {
				return Jsonable.createDefaultObjectMapper().readValue(deser, responseType);
			}
			else {
				R customParsedResponse = parseResponseFromString(deser);
				if (customParsedResponse == null) {
					String errorMsg = "Unhandled return type: " + responseType;
					logger.severe(errorMsg);
					throw new IllegalStateException(errorMsg);
				}
				else {
					return customParsedResponse;
				}
			}
		}
	}

	private R parseResponseFailure(HttpResponse response) throws IOException {
		String exMessage = EntityUtils.toString(response.getEntity());
		if (responseExceptionType != null) {
			VonageApiResponseException varex = Jsonable.fromJson(exMessage,
					(Class<? extends VonageApiResponseException>) responseExceptionType
			);
			if (varex.title == null) {
				varex.title = response.getStatusLine().getReasonPhrase();
			}
			varex.statusCode = response.getStatusLine().getStatusCode();
			logger.log(Level.WARNING, "Failed to parse response", varex);
			throw varex;
		}
		R customParsedResponse = parseResponseFromString(exMessage);
		if (customParsedResponse == null) {
			logger.warning(exMessage);
			throw new VonageApiResponseException(exMessage);
		}
		else {
			return customParsedResponse;
		}
	}
}
