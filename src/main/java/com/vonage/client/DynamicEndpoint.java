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

import com.vonage.client.auth.AuthMethod;
import com.vonage.client.common.HttpMethod;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;

/**
 * Enables convenient declaration of endpoints without directly implementing {@link AbstractMethod}.
 * This decouples the endpoint's implementation from the underlying HTTP library.
 *
 * @since 7.7.0
 */
@SuppressWarnings("unchecked")
public class DynamicEndpoint<T, R> extends AbstractMethod<T, R> {
	protected Set<Class<? extends AuthMethod>> authMethods;
	protected String contentType, accept;
	protected HttpMethod requestMethod;
	protected BiFunction<DynamicEndpoint<T, R>, ? super T, String> pathGetter;
	protected Class<? extends RuntimeException> responseExceptionType;
	protected Class<R> responseType;
	protected T cachedRequestBody;

	protected DynamicEndpoint(Builder<T, R> builder) {
		super(builder.wrapper);
		authMethods = builder.authMethods;
		requestMethod = builder.requestMethod;
		pathGetter = builder.pathGetter;
		responseExceptionType = builder.responseExceptionType;
		responseType = builder.responseType;
		contentType = builder.contentType;
		if ((accept = builder.accept) == null &&
				(Jsonable.class.isAssignableFrom(responseType) || isJsonableArrayResponse())
		) {
			accept = ContentType.APPLICATION_JSON.getMimeType();
		}
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
		private Class<? extends RuntimeException> responseExceptionType;

		Builder(Class<R> responseType) {
			this.responseType = responseType;
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
			authMethods.add(Objects.requireNonNull(primary, "Primary auth method cannot be null"));
			if (others != null) {
				for (Class<? extends AuthMethod> amc : others) {
					if (amc != null) {
						authMethods.add(amc);
					}
				}
			}
			return this;
		}

		public Builder<T, R> responseExceptionType(Class<? extends RuntimeException> responseExceptionType) {
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
			case GET: return RequestBuilder.get();
			case POST: return RequestBuilder.post();
			case PATCH: return RequestBuilder.patch();
			case DELETE: return RequestBuilder.delete();
			case PUT: return RequestBuilder.put();
			default: throw new IllegalStateException("Unknown request method.");
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
		int statusCode = response.getStatusLine().getStatusCode();
		try {
			if (statusCode >= 200 && statusCode < 300) {
				return parseResponseSuccess(response);
			}
			else if (statusCode >= 300 && statusCode < 400) {
				return parseResponseRedirect(response);
			}
			else {
				return parseResponseFailure(response);
			}
		}
		catch (InvocationTargetException ex) {
			Throwable wrapped = ex.getTargetException();
			if (wrapped instanceof RuntimeException) {
				throw (RuntimeException) wrapped;
			}
			else {
				throw new VonageUnexpectedException(wrapped);
			}
		}
		catch (ReflectiveOperationException ex) {
			throw new VonageUnexpectedException(ex);
		}
		finally {
			cachedRequestBody = null;
		}
	}

	protected R parseResponseFromString(String response) {
		return null;
	}

	private R parseResponseRedirect(HttpResponse response) throws ReflectiveOperationException, IOException {
		final String location = response.getFirstHeader("Location").getValue();

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

	private R parseResponseSuccess(HttpResponse response) throws IOException, ReflectiveOperationException {
		if (responseType == null || responseType.equals(Void.class)) {
			return null;
		}
		else if (byte[].class.equals(responseType)) {
			return (R) EntityUtils.toByteArray(response.getEntity());
		}
		else {
			String deser = EntityUtils.toString(response.getEntity());

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
			else if (Collection.class.isAssignableFrom(responseType) || isJsonableArrayResponse()) {
				return Jsonable.createDefaultObjectMapper().readValue(deser, responseType);
			}
			else {
				R customParsedResponse = parseResponseFromString(deser);
				if (customParsedResponse == null) {
					throw new IllegalStateException("Unhandled return type: " + responseType);
				}
				else {
					return customParsedResponse;
				}
			}
		}
	}

	private R parseResponseFailure(HttpResponse response) throws IOException, ReflectiveOperationException {
		String exMessage = EntityUtils.toString(response.getEntity());
		if (responseExceptionType != null) {
			if (VonageApiResponseException.class.isAssignableFrom(responseExceptionType)) {
				VonageApiResponseException varex = Jsonable.fromJson(exMessage,
						(Class<? extends VonageApiResponseException>) responseExceptionType
				);
				if (varex.title == null) {
					varex.title = response.getStatusLine().getReasonPhrase();
				}
				varex.statusCode = response.getStatusLine().getStatusCode();
				throw varex;
			}
			else {
				for (Constructor<?> constructor : responseExceptionType.getDeclaredConstructors()) {
					Class<?>[] params = constructor.getParameterTypes();
					if (params.length == 1 && String.class.equals(params[0])) {
						if (!constructor.isAccessible()) {
							constructor.setAccessible(true);
						}
						throw (RuntimeException) constructor.newInstance(exMessage);
					}
				}
			}
		}
		R customParsedResponse = parseResponseFromString(exMessage);
		if (customParsedResponse == null) {
			throw new VonageApiResponseException(exMessage);
		}
		else {
			return customParsedResponse;
		}
	}
}
