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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import java.util.UUID;

public class Verify2ClientTest extends ClientTest<Verify2Client> {
	static final UUID REQUEST_ID = UUID.randomUUID();
	static final String VERIFICATION_RESPONSE = "{\"request_id\": \""+REQUEST_ID+"\"}";

	public Verify2ClientTest() {
		client = new Verify2Client(wrapper);
	}

	@Test
	public void testVerifyCode() throws Exception {
		UUID requestId = UUID.randomUUID();
		stubResponseAndRun(200, () -> client.validateVerificationCode(requestId, "1234"));
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

	@Test
	public void testVerifyUserSuccess() throws Exception {
		testSuccessfulRegularVerificationRequest(SmsVerificationRequest.builder());
		testSuccessfulRegularVerificationRequest(VoiceVerificationRequest.builder());
		testSuccessfulRegularVerificationRequest(WhatsappVerificationRequest.builder());
		testSuccessfulRegularVerificationRequest(WhatsappInteractiveVerificationRequest.builder());

		stubSuccessfulVerifyUserResponseAndRun(
				applyAndBuildRegularVerification(EmailVerificationRequest.builder()
						.from("hello@example.com").to("email@domain.tld")
				)
		);
		stubSuccessfulVerifyUserResponseAndRun(
				applyBaseVerificationParams(SilentAuthVerificationRequest.builder()).build()
		);
	}
}
