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
import java.util.Arrays;
import java.util.List;
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

		String expectedJson = expectedResponse.toJson();
		assertEquals(183, expectedJson.length());
		wrapper.setHttpClient(stubHttpClient(statusCode, expectedJson));
		expectedResponse.setStatusCode(statusCode);
		String failPrefix = "Expected VerifyResponseException, but got ";

		try {
			invocation.run();
			fail(failPrefix + "nothing.");
		}
		catch (VerifyResponseException mrx) {
			assertEquals(expectedResponse, mrx);
			assertEquals(expectedJson, mrx.toJson());
		}
		catch (Throwable ex) {
			fail(failPrefix + ex);
		}
	}

	VerificationRequest newVerificationRequestWithAllParamsAndWorkflows() {
		String toNumber = "447100000009", fromNumber = "447900000001",
				toEmail = "email@domain.tld", fromEmail = "hello@example.com";
		List<Workflow> workflows = Arrays.asList(
				new SmsWorkflow(toNumber),
				new EmailWorkflow(toEmail, fromEmail),
				new VoiceWorkflow(toNumber),
				new WhatsappWorkflow(toNumber, fromNumber),
				new WhatsappCodelessWorkflow(toNumber),
				new SilentAuthWorkflow(toNumber)
		);
		return VerificationRequest.builder()
				.brand("Nexmo").fraudCheck(false)
				.code("ab2c3de5").codeLength(8)
				.channelTimeout(500)
				.locale(Locale.GERMAN_GERMANY)
				.clientRef("callback-ref0x1")
				.workflows(workflows).build();
	}

	void stubSuccessfulVerifyUserResponseAndRun(VerificationRequest request) throws Exception {
		stubResponse(202, VERIFICATION_RESPONSE);
		VerificationResponse response = client.sendVerification(request);
		assertNotNull(response);
		assertEquals(REQUEST_ID, response.getRequestId());
	}

	@Test
	public void testVerifyUserSuccess() throws Exception {
		stubSuccessfulVerifyUserResponseAndRun(newVerificationRequestWithAllParamsAndWorkflows());
	}

	@Test
	public void testVerifyUser429() throws Exception {
		assert429ResponseException(() -> client.sendVerification(newVerificationRequestWithAllParamsAndWorkflows()));
	}

	@Test
	public void testVerifyCodeSuccess() throws Exception {
		stubResponseAndRun(204, () -> client.checkVerificationCode(REQUEST_ID, CODE));
	}

	@Test
	public void testVerifyCodeFailure() throws Exception {
		assert429ResponseException(() -> client.checkVerificationCode(REQUEST_ID, CODE));

		stubResponseAndAssertThrows(204, () ->
				client.checkVerificationCode(null, CODE),
				NullPointerException.class
		);
		stubResponseAndAssertThrows(204, () ->
				client.checkVerificationCode(REQUEST_ID, null),
				NullPointerException.class
		);
	}

	@Test
	public void testCancelVerificationSuccess() throws Exception {
		stubResponseAndRun(204, () -> client.cancelVerification(REQUEST_ID));
	}

	@Test
	public void testCancelVerificationFailure() throws Exception {
		stubResponseAndAssertThrows(404, () ->
				client.cancelVerification(REQUEST_ID),
				VerifyResponseException.class
		);
	}
}
