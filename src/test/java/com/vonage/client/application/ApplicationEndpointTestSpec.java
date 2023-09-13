package com.vonage.client.application;

import com.vonage.client.DynamicEndpointTestSpec;
import com.vonage.client.auth.AuthMethod;
import com.vonage.client.auth.TokenAuthMethod;
import com.vonage.client.common.HttpMethod;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

abstract class ApplicationEndpointTestSpec<T, R> extends DynamicEndpointTestSpec<T, R> {

	@Override
	protected Collection<Class<? extends AuthMethod>> expectedAuthMethods() {
		return Collections.singletonList(TokenAuthMethod.class);
	}

	@Override
	protected Class<? extends Exception> expectedResponseExceptionType() {
		return ApplicationResponseException.class;
	}

	@Override
	protected String expectedDefaultBaseUri() {
		return "https://api.nexmo.com";
	}

	@Override
	protected String expectedEndpointUri(T request) {
		String base = "/v2/applications", suffix;
		if (request instanceof UUID) {
			suffix = request.toString();
		}
		else if (request instanceof Application && HttpMethod.PUT.equals(expectedHttpMethod())) {
			suffix = ((Application) request).getId();
		}
		else {
			suffix = null;
		}
		return suffix != null ? base + "/" + suffix : base;
	}

}
