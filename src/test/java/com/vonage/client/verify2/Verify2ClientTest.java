/*
 *   Copyright 2023 Vonage
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
package com.vonage.client.verify2;

import com.vonage.client.ClientTest;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.function.ThrowingRunnable;
import java.util.UUID;

public class Verify2ClientTest extends ClientTest<Verify2Client> {
	static final UUID REQUEST_ID = UUID.randomUUID();
	static final String CODE = "1234";
	static final String VERIFICATION_RESPONSE = "{\"request_id\": \""+REQUEST_ID+"\"}";

	public Verify2ClientTest() {
		client = new Verify2Client(wrapper);
	}

	void assert429ResponseException(ThrowingRunnable invocation) throws Exception {
		int statusCode = 429;
		VerifyResponseException expectedResponse = VerifyResponseException.fromJson(
				"{\n" +
				"   \"title\": \"Rate Limit Hit\",\n" +
				"   \"type\": \"https://www.developer.vonage.com/api-errors#throttled\",\n" +
				"   \"detail\": \"Please wait, then retry your request\",\n" +
				"   \"instance\": \"bf0ca0bf927b3b52e3cb03217e1a1ddf\"\n" +
				"}"
		);

		wrapper.setHttpClient(stubHttpClient(statusCode, expectedResponse.toJson()));
		expectedResponse.setStatusCode(statusCode);
		String failPrefix = "Expected VerifyResponseException, but got ";

		try {
			invocation.run();
			fail(failPrefix + "nothing.");
		}
		catch (VerifyResponseException mrx) {
			assertEquals(expectedResponse, mrx);
		}
		catch (Throwable ex) {
			fail(failPrefix + ex);
		}
	}

	<B extends VerificationRequest.Builder<?, B>> B applyBaseVerificationParams(B builder) {
		if (builder.to == null) {
			builder.to("447100000009");
		}
		return builder.brand("Nexmo");
	}

	<V extends RegularVerificationRequest, B extends RegularVerificationRequest.Builder<V, B>> V applyAndBuildRegularVerification(B builder) {
		return applyBaseVerificationParams(builder)
				.codeLength(8).timeout(500)
				.locale(Locale.GERMAN_GERMANY)
				.clientRef("callback-ref0x1")
				.build();
	}

	void stubSuccessfulVerifyUserResponseAndRun(VerificationRequest request) throws Exception {
		stubResponse(202, VERIFICATION_RESPONSE);
		VerificationResponse response = client.sendVerification(request);
		assertNotNull(response);
		assertEquals(REQUEST_ID, response.getRequestId());
	}

	<V extends RegularVerificationRequest, B extends RegularVerificationRequest.Builder<V, B>> void testSuccessfulRegularVerificationRequest(B builder) throws Exception {
		stubSuccessfulVerifyUserResponseAndRun(applyAndBuildRegularVerification(builder));
	}

	EmailVerificationRequest.Builder emailBuilder() {
		return EmailVerificationRequest.builder().from("hello@example.com").to("email@domain.tld");
	}

	@Test
	public void testVerifyUserSuccess() throws Exception {
		testSuccessfulRegularVerificationRequest(SmsVerificationRequest.builder());
		testSuccessfulRegularVerificationRequest(VoiceVerificationRequest.builder());
		testSuccessfulRegularVerificationRequest(WhatsappVerificationRequest.builder());
		testSuccessfulRegularVerificationRequest(WhatsappInteractiveVerificationRequest.builder());
		stubSuccessfulVerifyUserResponseAndRun(applyAndBuildRegularVerification(emailBuilder()));
		stubSuccessfulVerifyUserResponseAndRun(applyBaseVerificationParams(SilentAuthVerificationRequest.builder()).build());
	}

	@Test
	public void testVerifyUser429() throws Exception {
		assert429ResponseException(() -> client.sendVerification(
				applyAndBuildRegularVerification(SmsVerificationRequest.builder())
		));
		assert429ResponseException(() -> client.sendVerification(
				applyAndBuildRegularVerification(VoiceVerificationRequest.builder())
		));
		assert429ResponseException(() -> client.sendVerification(
				applyAndBuildRegularVerification(WhatsappVerificationRequest.builder())
		));
		assert429ResponseException(() -> client.sendVerification(
				applyAndBuildRegularVerification(WhatsappInteractiveVerificationRequest.builder())
		));
		assert429ResponseException(() -> client.sendVerification(
				applyAndBuildRegularVerification(emailBuilder())
		));
		assert429ResponseException(() -> client.sendVerification(
				applyBaseVerificationParams(SilentAuthVerificationRequest.builder()).build()
		));
	}

	@Test
	public void testVerifyCodeSuccess() throws Exception {
		stubResponseAndRun(204, () -> client.validateVerificationCode(REQUEST_ID, CODE));
	}

	@Test
	public void testVerifyCodeFailure() throws Exception {
		assert429ResponseException(() -> client.validateVerificationCode(REQUEST_ID, CODE));

		stubResponseAndAssertThrows(204, () ->
				client.validateVerificationCode(null, CODE),
				NullPointerException.class
		);
		stubResponseAndAssertThrows(204, () ->
				client.validateVerificationCode(REQUEST_ID, null),
				NullPointerException.class
		);
	}
}
