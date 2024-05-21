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
package com.vonage.client.numberinsight2;

import com.vonage.client.DynamicEndpoint;
import com.vonage.client.HttpWrapper;
import com.vonage.client.RestEndpoint;
import com.vonage.client.VonageClient;
import com.vonage.client.auth.ApiKeyHeaderAuthMethod;
import com.vonage.client.common.HttpMethod;

/**
 * A client for talking to the Vonage NumberInsight2Client API. The standard way to obtain an instance of this class is to use
 * {@link VonageClient#getNumberInsight2Client()}.
 */
public class NumberInsight2Client {
	final RestEndpoint<FraudCheckRequest, FraudCheckResponse> fraudCheck;

	/**
	 * Constructor.
	 *
	 * @param wrapper (REQUIRED) shared HTTP wrapper object used for making REST calls.
	 */
	public NumberInsight2Client(HttpWrapper wrapper) {
		@SuppressWarnings("unchecked")
		final class Endpoint<T, R> extends DynamicEndpoint<T, R> {
			Endpoint(R... type) {
				super(DynamicEndpoint.<T, R> builder(type)
					.authMethod(ApiKeyHeaderAuthMethod.class)
					.responseExceptionType(NumberInsight2ResponseException.class)
					.requestMethod(HttpMethod.POST).wrapper(wrapper).pathGetter((de, req) ->
						de.getHttpWrapper().getHttpConfig().getApiBaseUri() + "/v2/ni"
					)
				);
			}
		}
		
		fraudCheck = new Endpoint<>();
	}

	/**
	 * Make fraud check requests with a phone number by looking up fraud score and/or by checking SIM swap status.
	 *
	 * @param phoneNumber The phone number to run the check on, in E.164 format.
	 * @param insight The insight service type to request.
	 * @param additional Optional additional insight(s) to send in the request.
	 *
	 * @return Results of the fraud check.
	 */
	public FraudCheckResponse fraudCheck(String phoneNumber, Insight insight, Insight... additional) {
		return fraudCheck.execute(new FraudCheckRequest(phoneNumber, insight, additional));
	}
}
