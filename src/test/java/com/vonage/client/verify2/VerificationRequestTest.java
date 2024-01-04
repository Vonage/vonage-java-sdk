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
package com.vonage.client.verify2;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.VonageUnexpectedException;
import com.vonage.client.verify2.VerificationRequest.Builder;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;

public class VerificationRequestTest {
	static final boolean SANDBOX = true;
	static final Locale LOCALE = Locale.forLanguageTag("pt-pt");
	static final int CODE_LENGTH = 8, CHANNEL_TIMEOUT = 120;
	static final String
			BRAND = "Vonage",
			TO_NUMBER = "447700900000",
			FROM_NUMBER = "447900100000",
			TO_EMAIL = "alice@example.org",
			FROM_EMAIL = "bob@example.org",
			CLIENT_REF = "my-personal-reference",
			APP_HASH = "kkeid8sksd3",
			REDIRECT_URL = "https://acme-app.com/sa/redirect";

	Builder newBuilder() {
		return VerificationRequest.builder().brand(BRAND);
	}

	Builder newBuilderAllParams() {
		return newBuilder().codeLength(CODE_LENGTH).clientRef(CLIENT_REF)
				.channelTimeout(CHANNEL_TIMEOUT).locale(LOCALE).fraudCheck();
	}

	Workflow getWorkflowRequiredParamsForChannel(Channel channel) {
		switch (channel) {
			default: throw new IllegalStateException();
			case SMS:
				return new SmsWorkflow(TO_NUMBER);
			case VOICE:
				return new VoiceWorkflow(TO_NUMBER);
			case WHATSAPP:
				return new WhatsappWorkflow(TO_NUMBER);
			case WHATSAPP_INTERACTIVE:
				return new WhatsappCodelessWorkflow(TO_NUMBER);
			case EMAIL:
				return new EmailWorkflow(TO_EMAIL);
			case SILENT_AUTH:
				return new SilentAuthWorkflow(TO_NUMBER);
		}
	}

	Workflow getWorkflowAllParamsForChannel(Channel channel) {
		switch (channel) {
			case SILENT_AUTH:
				return new SilentAuthWorkflow(TO_NUMBER, SANDBOX, REDIRECT_URL);
			case SMS:
				return new SmsWorkflow(TO_NUMBER, FROM_NUMBER, APP_HASH);
			case WHATSAPP:
				return new WhatsappWorkflow(TO_NUMBER, FROM_NUMBER);
			case EMAIL:
				return new EmailWorkflow(TO_EMAIL, FROM_EMAIL);
			default:
				return getWorkflowRequiredParamsForChannel(channel);
		}
	}

	Builder getBuilderRequiredParamsSingleWorkflow(Channel channel) {
		return newBuilder().addWorkflow(getWorkflowRequiredParamsForChannel(channel));
	}

	Builder getBuilderAllParamsSingleWorkflow(Channel channel) {
		Builder builder = newBuilderAllParams()
				.addWorkflow(getWorkflowAllParamsForChannel(channel));
		if (channel == Channel.SILENT_AUTH || channel == Channel.WHATSAPP_INTERACTIVE) {
			builder.codeLength = null;
		}
		return builder;
	}

	String getExpectedRequiredParamsForSingleWorkflowJson(Channel channel) {
		String to = channel == Channel.EMAIL ? TO_EMAIL : TO_NUMBER;
		return "{\"brand\":\""+BRAND+"\",\"workflow\":[{" +
				"\"channel\":\""+channel+"\",\"to\":\""+to+"\"}]}";
	}

	String getExpectedAllParamsForSingleWorkflowJson(Channel channel) {
		String expectedJson = getExpectedRequiredParamsForSingleWorkflowJson(channel), prefix, replacement;

		if (channel == Channel.SMS) {
			prefix = TO_NUMBER + '"';
			replacement = prefix + ",\"app_hash\":\""+APP_HASH+"\"";
			expectedJson = expectedJson.replace(prefix, replacement);
		}
		if (channel == Channel.WHATSAPP || channel == Channel.SMS) {
			prefix = TO_NUMBER + '"';
			replacement = prefix + ",\"from\":\""+FROM_NUMBER+"\"";
			expectedJson = expectedJson.replace(prefix, replacement);
		}
		if (channel == Channel.EMAIL) {
			prefix = TO_EMAIL + '"';
			replacement = prefix + ",\"from\":\""+FROM_EMAIL+"\"";
			expectedJson = expectedJson.replace(prefix, replacement);
		}
		if (channel == Channel.SILENT_AUTH) {
			prefix = TO_NUMBER + '"';
			replacement = prefix + ",\"sandbox\":" + SANDBOX + ",\"redirect_url\":\"" + REDIRECT_URL + '"';
			expectedJson = expectedJson.replace(prefix, replacement);
		}

		prefix = "{\"locale\":\"pt-pt\",\"channel_timeout\":"+ CHANNEL_TIMEOUT;

		if (channel != Channel.SILENT_AUTH && channel != Channel.WHATSAPP_INTERACTIVE) {
			prefix += ",\"code_length\":"+CODE_LENGTH;
		}
		expectedJson = prefix + expectedJson.replaceFirst("\\{", ",");
		prefix = "\"brand\":\"" + BRAND + "\",";
		replacement = "\"fraud_check\":false," + prefix + "\"client_ref\":\""+CLIENT_REF+"\",";
		expectedJson = expectedJson.replace(prefix, replacement);
		return expectedJson;
	}

	void assertEqualsRequiredParams(Channel channel) {
		String expectedJson = getExpectedRequiredParamsForSingleWorkflowJson(channel);
		String actualJson = getBuilderRequiredParamsSingleWorkflow(channel).build().toJson();
		assertEquals(expectedJson, actualJson);
	}

	void assertEqualsAllParams(Channel channel) {
		String expectedJson = getExpectedAllParamsForSingleWorkflowJson(channel);
		String actualJson = getBuilderAllParamsSingleWorkflow(channel).build().toJson();
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
	public void testWithoutWorkflow() {
		assertThrows(IllegalStateException.class, () -> newBuilder().build());
		assertThrows(IllegalStateException.class, () -> newBuilderAllParams().build());
		assertThrows(IllegalStateException.class, () -> newBuilder().workflows(Collections.emptyList()).build());
		assertThrows(IllegalStateException.class, () -> newBuilderAllParams().workflows(Collections.emptyList()).build());
	}

	@Test
	public void testWithoutBrand() {
		for (Channel channel : Channel.values()) {
			Builder builder = getBuilderRequiredParamsSingleWorkflow(channel);
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
	public void testAllWorkflowsWithoutRecipient() {
		for (String invalid : new String[]{"", " ", null}) {
			assertThrows(RuntimeException.class, () -> new SilentAuthWorkflow(invalid));
			assertThrows(RuntimeException.class, () -> new SmsWorkflow(invalid));
			assertThrows(RuntimeException.class, () -> new VoiceWorkflow(invalid));
			assertThrows(RuntimeException.class, () -> new WhatsappWorkflow(invalid));
			assertThrows(RuntimeException.class, () -> new WhatsappCodelessWorkflow(invalid));
			assertThrows(RuntimeException.class, () -> new EmailWorkflow(invalid));
		}
	}

	@Test
	public void testSenderValidation() {
		assertEquals(FROM_NUMBER, new WhatsappWorkflow(TO_NUMBER, FROM_NUMBER).getFrom());
		assertEquals(FROM_EMAIL, new EmailWorkflow(TO_EMAIL, FROM_EMAIL).getFrom());
		for (String invalid : new String[]{"", " "}) {
			assertThrows(IllegalArgumentException.class, () -> new WhatsappWorkflow(TO_NUMBER, invalid));
			assertThrows(IllegalArgumentException.class, () -> new EmailWorkflow(TO_EMAIL, invalid));
		}
	}

	@Test
	public void testCodelessAndCodeLengthValidation() {
		Builder requiredBuilder = getBuilderRequiredParamsSingleWorkflow(Channel.SILENT_AUTH);
		assertTrue(requiredBuilder.build().isCodeless());
		requiredBuilder.addWorkflow(new WhatsappCodelessWorkflow(TO_NUMBER));
		assertTrue(requiredBuilder.build().isCodeless());
		requiredBuilder.codeLength(9);
		assertThrows(IllegalStateException.class, requiredBuilder::build);
		Builder allBuilder = getBuilderAllParamsSingleWorkflow(Channel.WHATSAPP_INTERACTIVE).codeLength(4);
		assertThrows(IllegalStateException.class, allBuilder::build);
	}

	@Test
	public void testCodelessAndCodeValidation() {
		Builder requiredBuilder = getBuilderRequiredParamsSingleWorkflow(Channel.SILENT_AUTH);
		assertTrue(requiredBuilder.build().isCodeless());
		requiredBuilder.addWorkflow(new WhatsappCodelessWorkflow(TO_NUMBER));
		assertTrue(requiredBuilder.build().isCodeless());
		requiredBuilder.code("12345678");
		assertThrows(IllegalStateException.class, requiredBuilder::build);
		Builder allBuilder = getBuilderAllParamsSingleWorkflow(Channel.WHATSAPP_INTERACTIVE).code("5670");
		assertThrows(IllegalStateException.class, allBuilder::build);
	}

	@Test
	public void testCodeLengthBoundaries() {
		Builder builder = getBuilderAllParamsSingleWorkflow(Channel.VOICE);
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

	@Test
	public void testCodeRegex() {
		Builder builder = getBuilderRequiredParamsSingleWorkflow(Channel.SMS);
		assertEquals(10, builder.code("AbC3dfghi0").build().getCode().length());
		assertEquals(4, builder.code("1234").build().getCode().length());
		assertEquals(9, builder.code("abcdefghi").build().getCode().length());
		assertEquals(4, builder.code("ABCD").build().getCode().length());
		assertThrows(IllegalArgumentException.class, () -> builder.code("123").build());
		assertThrows(IllegalArgumentException.class, () -> builder.code("01234567890").build());
		assertThrows(IllegalArgumentException.class, () -> builder.code("a*B*3*C*").build());
		assertThrows(IllegalArgumentException.class, () -> builder.code("2.567").build());
	}

	@Test
	public void testCodeAndCodeLengthMatch() {
		Builder builder = getBuilderRequiredParamsSingleWorkflow(Channel.WHATSAPP).code("1234567");
		assertEquals(7, builder.build().getCode().length());
		assertThrows(IllegalStateException.class, () -> builder.codeLength(5).build());
		assertEquals(7, builder.codeLength(7).build().getCode().length());
	}

	@Test
	public void testChannelTimeoutBoundaries() {
		Builder builder = getBuilderAllParamsSingleWorkflow(Channel.VOICE);
		assertEquals(CHANNEL_TIMEOUT, builder.build().getChannelTimeout().intValue());
		int min = 60, max = 900;
		builder.channelTimeout(min - 1);
		assertThrows(IllegalArgumentException.class, builder::build);
		builder.channelTimeout(min);
		assertEquals(min, builder.build().getChannelTimeout().intValue());
		builder.channelTimeout(max + 1);
		assertThrows(IllegalArgumentException.class, builder::build);
		builder.channelTimeout(max);
		assertEquals(max, builder.build().getChannelTimeout().intValue());
	}

	@Test
	public void testInvalidAppHash() {
		assertThrows(IllegalArgumentException.class, () -> new SmsWorkflow(TO_NUMBER, "1234567890"));
		assertThrows(IllegalArgumentException.class, () -> new SmsWorkflow(TO_NUMBER, "1234567890ab"));
		String appHash = new SmsWorkflow(TO_NUMBER, "1234567890a").getAppHash();
		assertNotNull(appHash);
		assertEquals(11, appHash.length());
	}

	@Test
	public void testFraudCheckOnlyIncludedIfFalse() {
		for (Channel channel : Channel.values()) {
			Builder builder = getBuilderRequiredParamsSingleWorkflow(channel).fraudCheck(true);
			String expectedJson = getExpectedRequiredParamsForSingleWorkflowJson(channel);
			VerificationRequest request = builder.build();
			assertNull(request.getFraudCheck());
			assertEquals(expectedJson, request.toJson());
			request = builder.fraudCheck(false).build();
			expectedJson = "{\"fraud_check\":false," + expectedJson.substring(1);
			assertEquals(expectedJson, request.toJson());
		}
	}

	@Test
	public void testSilentAuthMustBeFirstWorkflow() {
		VerificationRequest.Builder builder = VerificationRequest.builder().brand("Test");
		assertThrows(IllegalStateException.class, builder::build);
		SilentAuthWorkflow saw = new SilentAuthWorkflow("447900000001");
		assertEquals(saw, builder.addWorkflow(saw).build().getWorkflows().get(0));
		WhatsappWorkflow waw = new WhatsappWorkflow(saw.getTo());
		assertEquals(waw, builder.addWorkflow(waw).build().getWorkflows().get(1));
		builder.workflows(Arrays.asList(waw, saw));
		assertEquals(2, builder.workflows.size());
		assertThrows(IllegalStateException.class, builder::build);
	}

	@Test
	public void testInvalidLocale() throws Exception {
		VerificationRequest.Builder builder = getBuilderRequiredParamsSingleWorkflow(Channel.SMS);
		assertThrows(IllegalArgumentException.class, () -> builder.locale("--++").build());
		assertThrows(IllegalArgumentException.class, () -> builder.locale("en_GB").build());
		assertNotNull(builder.locale("ab-cd").build().getLocale());
	}

	@Test
	public void triggerJsonProcessingException() {
		class SelfRefrencing extends VerificationRequest {
			@JsonProperty("self") final SelfRefrencing self = this;

			SelfRefrencing(Builder builder) {
				super(builder);
			}
		}
		assertThrows(VonageUnexpectedException.class, () -> new SelfRefrencing(VerificationRequest.builder()
				.addWorkflow(new SmsWorkflow("447900000000")).brand("Test")).toJson()
		);
	}
}
