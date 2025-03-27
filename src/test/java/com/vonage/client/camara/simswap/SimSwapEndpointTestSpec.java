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
package com.vonage.client.camara.simswap;

import com.vonage.client.DynamicEndpointTestSpec;
import com.vonage.client.VonageApiResponseException;
import com.vonage.client.auth.AuthMethod;
import com.vonage.client.auth.camara.NetworkAuthMethod;
import com.vonage.client.auth.camara.FraudPreventionDetectionScope;
import com.vonage.client.camara.CamaraResponseException;
import com.vonage.client.common.HttpMethod;
import java.util.Set;

abstract class SimSwapEndpointTestSpec<R> extends DynamicEndpointTestSpec<SimSwapRequest, R> {
	protected final String msisdn = "346661113334";

	@Override
	protected Set<Class<? extends AuthMethod>> expectedAuthMethods() {
		return Set.of(NetworkAuthMethod.class);
	}

	@Override
	protected Class<? extends VonageApiResponseException> expectedResponseExceptionType() {
		return CamaraResponseException.class;
	}

	@Override
	protected String expectedDefaultBaseUri() {
		return "https://api-eu.vonage.com";
	}

	@Override
	protected HttpMethod expectedHttpMethod() {
		return HttpMethod.POST;
	}

	@Override
	protected SimSwapRequest sampleRequest() {
		return new SimSwapRequest(msisdn);
	}

	@Override
	protected final String expectedEndpointUri(SimSwapRequest request) {
		return "/camara/sim-swap/v040/" + switch (getScope()) {
			default -> throw new IllegalStateException();
			case CHECK_SIM_SWAP -> "check";
			case RETRIEVE_SIM_SWAP_DATE -> "retrieve-date";
		};
	}

	@Override
	protected String sampleRequestBodyString() {
		return "{\"phoneNumber\":\""+msisdn+"\"}";
	}

	protected abstract FraudPreventionDetectionScope getScope();
}
