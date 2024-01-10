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

import com.vonage.client.ClientTest;
import com.vonage.client.DynamicEndpointTestSpec;
import com.vonage.client.RestEndpoint;
import com.vonage.client.VonageApiResponseException;
import com.vonage.client.auth.AuthMethod;
import com.vonage.client.auth.TokenAuthMethod;
import com.vonage.client.common.HttpMethod;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

public class NumberInsight2ClientTest extends ClientTest<NumberInsight2Client> {
	private static final String PHONE_NUMBER = "447009000002";

	public NumberInsight2ClientTest() {
		client = new NumberInsight2Client(wrapper);
	}
	
	void assert401ResponseException(Executable invocation) throws Exception {
		assert401ApiResponseException(NumberInsight2ResponseException.class, invocation);
	}

	@Test
	public void testFraudCheck() throws Exception {
		String responseJson = "{\"phone\":{\"phone\":\""+PHONE_NUMBER+"\"}}";
		stubResponse(200, responseJson);
		FraudCheckResponse response = client.fraudCheck(PHONE_NUMBER, Insight.FRAUD_SCORE);
		assertNotNull(response);
		assertEquals(PHONE_NUMBER, response.getPhone().getNumber());

		stubResponseAndAssertThrows(200, responseJson,
				() -> client.fraudCheck(null, Insight.FRAUD_SCORE),
				NullPointerException.class
		);
		stubResponseAndAssertThrows(200, responseJson,
				() -> client.fraudCheck(PHONE_NUMBER, null),
				NullPointerException.class
		);
		stubResponseAndAssertThrows(200, responseJson,
				() -> client.fraudCheck(PHONE_NUMBER, null, Insight.FRAUD_SCORE),
				NullPointerException.class
		);
		stubResponseAndAssertThrows(200, responseJson,
				() -> client.fraudCheck(PHONE_NUMBER, Insight.SIM_SWAP, (Insight) null),
				NullPointerException.class
		);

		stubResponseAndAssertThrows(401,
				() -> client.fraudCheck(PHONE_NUMBER, Insight.SIM_SWAP),
				NumberInsight2ResponseException.class
		);
		assert401ResponseException(() -> client.fraudCheck(PHONE_NUMBER, Insight.SIM_SWAP, Insight.FRAUD_SCORE));
	}
	
	@Test
	public void testFraudCheckEndpoint() throws Exception {
		new DynamicEndpointTestSpec<FraudCheckRequest, FraudCheckResponse>() {


			@Override
			protected Collection<Class<? extends AuthMethod>> expectedAuthMethods() {
				return Arrays.asList(TokenAuthMethod.class);
			}

			@Override
			protected Class<? extends VonageApiResponseException> expectedResponseExceptionType() {
				return NumberInsight2ResponseException.class;
			}

			@Override
			protected String expectedDefaultBaseUri() {
				return "https://api.nexmo.com";
			}

			@Override
			protected RestEndpoint<FraudCheckRequest, FraudCheckResponse> endpoint() {
				return client.fraudCheck;
			}

			@Override
			protected HttpMethod expectedHttpMethod() {
				return HttpMethod.POST;
			}

			@Override
			protected String expectedEndpointUri(FraudCheckRequest request) {
				return "/v2/ni";
			}

			@Override
			protected FraudCheckRequest sampleRequest() {
				return new FraudCheckRequest(PHONE_NUMBER, Insight.SIM_SWAP);
			}

			@Override
			protected String sampleRequestBodyString() {
				return "{\"type\":\"phone\",\"phone\":\""+PHONE_NUMBER+"\",\"insights\":[\"sim_swap\"]}";
			}

			@Override
			public void runTests() throws Exception {
				super.runTests();
				testBothInsightsIncludedInSameOrderWithoutDuplicates();
			}

			private void testBothInsightsIncludedInSameOrderWithoutDuplicates() throws Exception {
				FraudCheckRequest request = new FraudCheckRequest(PHONE_NUMBER,
						Insight.FRAUD_SCORE, Insight.SIM_SWAP, Insight.FRAUD_SCORE,
						Insight.FRAUD_SCORE, Insight.SIM_SWAP, Insight.SIM_SWAP
				);
				assertEquals(2, request.getInsights().size());
				Iterator<Insight> insightIter = request.getInsights().iterator();
				assertEquals(Insight.FRAUD_SCORE, insightIter.next());
				assertEquals(Insight.SIM_SWAP, insightIter.next());
				String expectedJson = sampleRequestBodyString().replace(
						"[\"sim_swap\"]", "[\"fraud_score\",\"sim_swap\"]"
				);
				assertRequestUriAndBody(request, expectedJson);
			}
		}
		.runTests();
	}

}