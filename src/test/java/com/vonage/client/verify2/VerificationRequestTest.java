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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import org.junit.Test;

public class VerificationRequestTest {
	static final Locale LOCALE = Locale.PORTUGUESE_PORTUGAL;
	static final int CODE_LENGTH = 8, TIMEOUT = 120;
	static final String
			BRAND = "Vonage",
			TO_NUMBER = "447700900000",
			TO_EMAIL = "alice@example.org",
			FROM_EMAIL = "bob@example.org",
			CLIENT_REF = "my-personal-reference",
			APP_HASH = "kkeid8sksd3";

	<B extends VerificationRequest.Builder<?, B>> B applyBaseRequiredParams(B builder) {
		return builder.brand(BRAND).to(TO_NUMBER);
	}

	<B extends RegularVerificationRequest.Builder<?, B>> B applyRegularRequiredParams(B builder) {
		return applyBaseRequiredParams(builder).codeLength(CODE_LENGTH);
	}

	<B extends RegularVerificationRequest.Builder<?, B>> B applyRegularAllParams(B builder) {
		return applyRegularRequiredParams(builder).clientRef(CLIENT_REF).timeout(TIMEOUT).locale(LOCALE);
	}

	EmailVerificationRequest.Builder applyEmailRequiredParams(EmailVerificationRequest.Builder builder) {
		return applyRegularRequiredParams(builder).to(TO_EMAIL).from(FROM_EMAIL);
	}

	@SuppressWarnings("unchecked")
	<B extends VerificationRequest.Builder<?, B>> B getBuilderWithRequiredParamsForChannel(Channel channel) {
		switch (channel) {
			default: throw new IllegalStateException();
			case SMS:
				return (B) applyRegularRequiredParams(SmsVerificationRequest.builder());
			case VOICE:
				return (B) applyRegularRequiredParams(VoiceVerificationRequest.builder());
			case WHATSAPP:
				return (B) applyRegularRequiredParams(WhatsappVerificationRequest.builder());
			case WHATSAPP_INTERACTIVE:
				return (B) applyRegularRequiredParams(WhatsappInteractiveVerificationRequest.builder());
			case EMAIL:
				return (B) applyEmailRequiredParams(EmailVerificationRequest.builder());
			case SILENT_AUTH:
				return (B) applyBaseRequiredParams(SilentAuthVerificationRequest.builder());
		}
	}

	VerificationRequest.Builder<?, ?> getBuilderWithAllParamsForChannel(Channel channel) {
		switch (channel) {
			default: throw new IllegalStateException();
			case SMS:
				return applyRegularAllParams(SmsVerificationRequest.builder().appHash(APP_HASH));
			case VOICE:
				return applyRegularAllParams(VoiceVerificationRequest.builder());
			case WHATSAPP:
				return applyRegularAllParams(WhatsappVerificationRequest.builder());
			case WHATSAPP_INTERACTIVE:
				return applyRegularAllParams(WhatsappInteractiveVerificationRequest.builder());
			case EMAIL:
				return applyEmailRequiredParams(applyRegularAllParams(EmailVerificationRequest.builder()));
			case SILENT_AUTH:
				return applyBaseRequiredParams(SilentAuthVerificationRequest.builder());
		}
	}

	VerificationRequest createRequestWithRequiredParamsForChannel(Channel channel) {
		return getBuilderWithRequiredParamsForChannel(channel).build();
	}

	VerificationRequest createRequestWithAllParamsForChannel(Channel channel) {
		return getBuilderWithAllParamsForChannel(channel).build();
	}

	String getExpectedRequiredParamsJson(Channel channel) {
		String to = channel == Channel.EMAIL ? TO_EMAIL : TO_NUMBER;
		String expectedJson = "{\"brand\":\""+BRAND+"\",\"workflow\":[{" +
				"\"channel\":\""+channel+"\",\"to\":\""+to+"\"";
		if (channel == Channel.EMAIL) {
			expectedJson += ",\"from\":\""+FROM_EMAIL+"\"";
		}
		expectedJson += "}]";
		if (channel != Channel.SILENT_AUTH) {
			expectedJson += ",\"code_length\":" + CODE_LENGTH;
		}
		expectedJson += "}";
		return expectedJson;
	}

	String getExpectedAllParamsJson(Channel channel) {
		String expectedJson = getExpectedRequiredParamsJson(channel), prefix, replacement;
		if (channel == Channel.SMS) {
			prefix = TO_NUMBER + "\"";
			replacement = prefix + ",\"app_hash\":\""+APP_HASH+"\"";
			expectedJson = expectedJson.replace(prefix, replacement);
		}
		if (channel != Channel.SILENT_AUTH) {
			prefix = "}],";
			replacement = prefix + "\"locale\":\"pt-pt\",\"channel_timeout\":"+TIMEOUT+",";
			expectedJson = expectedJson.replace(prefix, replacement);
			prefix = "\"code_length\":"+CODE_LENGTH;
			replacement = prefix + ",\"client_ref\":\""+CLIENT_REF+"\"";
			expectedJson = expectedJson.replace(prefix, replacement);
		}
		return expectedJson;
	}

	void assertEqualsRequiredParams(Channel channel) {
		String expectedJson = getExpectedRequiredParamsJson(channel);
		String actualJson = createRequestWithRequiredParamsForChannel(channel).toJson();
		assertEquals(expectedJson, actualJson);
	}

	void assertEqualsAllParams(Channel channel) {
		String expectedJson = getExpectedAllParamsJson(channel);
		String actualJson = createRequestWithAllParamsForChannel(channel).toJson();
		assertEquals(expectedJson, actualJson);
	}

	@Test
	public void testAllChannelsValidParams() {
		for (Channel channel : Channel.values()) {
			assertEqualsRequiredParams(channel);
			assertEqualsAllParams(channel);
		}
	}

	@Test
	public void testInvalidAppHash() {
		SmsVerificationRequest.Builder builder = getBuilderWithRequiredParamsForChannel(Channel.SMS);
		assertThrows(IllegalArgumentException.class, () -> builder.appHash("1234567890").build());
		assertThrows(IllegalArgumentException.class, () -> builder.appHash("1234567890ab").build());
		assertEquals(SmsVerificationRequest.class, builder.appHash("1234567890a").build().getClass());
	}
}
