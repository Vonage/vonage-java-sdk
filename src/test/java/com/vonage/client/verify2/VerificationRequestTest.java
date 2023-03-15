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

import static org.junit.Assert.*;
import org.junit.Test;

public class VerificationRequestTest {
	static final Locale LOCALE = Locale.PORTUGUESE_PORTUGAL;
	static final int CODE_LENGTH = 8, CHANNEL_TIMEOUT = 120;
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
		return applyBaseRequiredParams(builder);
	}

	<B extends RegularVerificationRequest.Builder<?, B>> B applyRegularAllParams(B builder) {
		return applyRegularRequiredParams(builder)
				.codeLength(CODE_LENGTH).clientRef(CLIENT_REF).timeout(CHANNEL_TIMEOUT).locale(LOCALE);
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

	@SuppressWarnings("unchecked")
	<B extends VerificationRequest.Builder<?, ? extends B>> B getBuilderWithAllParamsForChannel(Channel channel) {
		switch (channel) {
			default: throw new IllegalStateException();
			case SMS:
				return (B) applyRegularAllParams(SmsVerificationRequest.builder().appHash(APP_HASH));
			case VOICE:
				return (B) applyRegularAllParams(VoiceVerificationRequest.builder());
			case WHATSAPP:
				return (B) applyRegularAllParams(WhatsappVerificationRequest.builder());
			case WHATSAPP_INTERACTIVE:
				return (B) applyRegularAllParams(WhatsappInteractiveVerificationRequest.builder());
			case EMAIL:
				return (B) applyEmailRequiredParams(applyRegularAllParams(EmailVerificationRequest.builder()));
			case SILENT_AUTH:
				return (B) applyBaseRequiredParams(SilentAuthVerificationRequest.builder());
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
		expectedJson += "}]}";
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
			prefix = "}]";
			replacement = prefix + ",\"locale\":\"pt-pt\",\"channel_timeout\":"+ CHANNEL_TIMEOUT +
					",\"code_length\":"+CODE_LENGTH;
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
	public void testWithoutBrand() {
		for (Channel channel : Channel.values()) {
			VerificationRequest.Builder<?, ?> builder = getBuilderWithRequiredParamsForChannel(channel);
			assertEquals(BRAND, builder.build().getBrand());
			builder.brand(null);
			assertNull(builder.brand);
			assertThrows(IllegalArgumentException.class, builder::build);
			builder.brand("");
			assertTrue(builder.brand.isEmpty());
			assertThrows(IllegalArgumentException.class, builder::build);
		}
	}

	@Test
	public void testWithoutRecipient() {
		for (Channel channel : Channel.values()) {
			VerificationRequest.Builder<?, ?> builder = getBuilderWithRequiredParamsForChannel(channel);
			String to = channel == Channel.EMAIL ? TO_EMAIL : TO_NUMBER;
			assertEquals(to, builder.build().getRecipient());
			builder.to(null);
			assertNull(builder.to);
			assertThrows(IllegalArgumentException.class, builder::build);
			builder.to("");
			assertTrue(builder.to.isEmpty());
			assertThrows(IllegalArgumentException.class, builder::build);
		}
	}

	@Test
	public void testCodeLengthBoundaries() {
		for (Channel channel : Channel.values()) {
			if (channel == Channel.SILENT_AUTH) continue;
			RegularVerificationRequest.Builder<?, ?> builder = getBuilderWithAllParamsForChannel(channel);
			assertEquals(CODE_LENGTH, builder.build().getCodeLength().intValue());
			int min = 4, max = 10;
			builder.codeLength(min - 1);
			assertThrows(IllegalArgumentException.class, builder::build);
			builder.codeLength(min);
			assertEquals(min, builder.build().getCodeLength().intValue());
			builder.codeLength(max + 1);
			assertThrows(IllegalArgumentException.class, builder::build);
			builder.codeLength(max);
			assertEquals(max, builder.build().getCodeLength().intValue());
		}
	}

	@Test
	public void testChannelTimeoutBoundaries() {
		for (Channel channel : Channel.values()) {
			if (channel == Channel.SILENT_AUTH) continue;
			RegularVerificationRequest.Builder<?, ?> builder = getBuilderWithAllParamsForChannel(channel);
			assertEquals(CHANNEL_TIMEOUT, builder.build().getChannelTimeout().intValue());
			int min = 60, max = 900;
			builder.timeout(min - 1);
			assertThrows(IllegalArgumentException.class, builder::build);
			builder.timeout(min);
			assertEquals(min, builder.build().getChannelTimeout().intValue());
			builder.timeout(max + 1);
			assertThrows(IllegalArgumentException.class, builder::build);
			builder.timeout(max);
			assertEquals(max, builder.build().getChannelTimeout().intValue());
		}
	}

	@Test
	public void testInvalidAppHash() {
		SmsVerificationRequest.Builder builder = getBuilderWithRequiredParamsForChannel(Channel.SMS);
		assertThrows(IllegalArgumentException.class, () -> builder.appHash("1234567890").build());
		assertThrows(IllegalArgumentException.class, () -> builder.appHash("1234567890ab").build());
		String appHash = builder.appHash("1234567890a").build().getAppHash();
		assertNotNull(appHash);
		assertEquals(11, appHash.length());
	}

	@Test
	public void testEmailWithoutSender() {
		EmailVerificationRequest.Builder builder = getBuilderWithRequiredParamsForChannel(Channel.EMAIL);
		assertEquals(FROM_EMAIL, builder.build().getSender());
		builder.from(null);
		assertNull(builder.from);
		assertThrows(IllegalArgumentException.class, builder::build);
		builder.from("");
		assertTrue(builder.from.isEmpty());
		assertThrows(IllegalArgumentException.class, builder::build);
	}
}
