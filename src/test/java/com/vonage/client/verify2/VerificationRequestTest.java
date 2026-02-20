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
package com.vonage.client.verify2;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.VonageUnexpectedException;
import com.vonage.client.verify2.VerificationRequest.Builder;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
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
			FROM_NUMBER = "14157386102",
			TO_EMAIL = "alice@example.org",
			FROM_EMAIL = "bob@example.org",
			CLIENT_REF = "my-personal-reference",
			APP_HASH = "kkeid8sksd3",
			CONTENT_ID = "1107158078772563946",
			ENTITY_ID = "1101407360000017170",
			REDIRECT_URL = "https://acme-app.com/sa/redirect";

	Builder newBuilder() {
		return VerificationRequest.builder().brand(BRAND);
	}

	/**
	 * Loads a JSON resource file and replaces placeholders with provided values.
	 * This follows the pattern used in identityinsights tests for better maintainability.
	 *
	 * @param filename The resource filename (relative to this test class)
	 * @param placeholders Variable arguments of key-value pairs (key, value, key, value, ...)
	 * @return The JSON string with placeholders replaced
	 * @throws IOException If the resource cannot be loaded
	 */
	private String loadJsonResource(String filename, String... placeholders) throws IOException {
		try (InputStream is = getClass().getResourceAsStream(filename)) {
			if (is == null) {
				throw new IOException("Could not find resource: " + filename);
			}
			byte[] buffer = new byte[1024];
			StringBuilder sb = new StringBuilder();
			int bytesRead;
			while ((bytesRead = is.read(buffer)) != -1) {
				sb.append(new String(buffer, 0, bytesRead, StandardCharsets.UTF_8));
			}
			String json = sb.toString().trim();
			
			// Replace placeholders
			for (int i = 0; i < placeholders.length; i += 2) {
				if (i + 1 < placeholders.length) {
					json = json.replace(placeholders[i], placeholders[i + 1]);
				}
			}
			
			return json;
		}
	}

	Builder newBuilderAllParams() {
		return newBuilder().codeLength(CODE_LENGTH).clientRef(CLIENT_REF)
				.channelTimeout(CHANNEL_TIMEOUT).locale(LOCALE).fraudCheck();
	}

	Workflow getWorkflowRequiredParamsForChannel(Channel channel) {
        return switch (channel) {
            case SMS -> new SmsWorkflow(TO_NUMBER);
            case VOICE -> new VoiceWorkflow(TO_NUMBER);
            case WHATSAPP -> WhatsappWorkflow.builder(TO_NUMBER, FROM_NUMBER).build();
            case EMAIL -> new EmailWorkflow(TO_EMAIL);
            case SILENT_AUTH -> new SilentAuthWorkflow(TO_NUMBER);
        };
	}

	Workflow getWorkflowAllParamsForChannel(Channel channel) {
        return switch (channel) {
            case SILENT_AUTH -> new SilentAuthWorkflow(TO_NUMBER, SANDBOX, REDIRECT_URL);
            case SMS -> SmsWorkflow.builder(TO_NUMBER)
                    .contentId(CONTENT_ID).entityId(ENTITY_ID)
                    .from(FROM_NUMBER).appHash(APP_HASH).build();
            case EMAIL -> new EmailWorkflow(TO_EMAIL, FROM_EMAIL);
            default -> getWorkflowRequiredParamsForChannel(channel);
        };
	}

	Builder getBuilderRequiredParamsSingleWorkflow(Channel channel) {
		return newBuilder().addWorkflow(getWorkflowRequiredParamsForChannel(channel));
	}

	Builder getBuilderAllParamsSingleWorkflow(Channel channel) {
		Builder builder = newBuilderAllParams()
				.addWorkflow(getWorkflowAllParamsForChannel(channel));
		if (channel == Channel.SILENT_AUTH) {
			builder.codeLength = null;
		}
		return builder;
	}

	String getExpectedRequiredParamsForSingleWorkflowJson(Channel channel) {
		var to = channel == Channel.EMAIL ? TO_EMAIL : TO_NUMBER;
		var json = "{\"brand\":\""+BRAND+"\",\"workflow\":[{" +
				"\"channel\":\""+channel+"\",\"to\":\""+to+'"';
		if (channel == Channel.WHATSAPP) {
			json += ",\"from\":\"" + FROM_NUMBER + '"';
		}
		json += "}]}";
		return json;
	}

	String getExpectedAllParamsForSingleWorkflowJson(Channel channel) {
		String expectedJson = getExpectedRequiredParamsForSingleWorkflowJson(channel), prefix, replacement;

		if (channel == Channel.SMS) {
			prefix = TO_NUMBER + '"';
			replacement = prefix + ",\"app_hash\":\""+APP_HASH+"\"," +
					"\"content_id\":\""+CONTENT_ID+"\",\"entity_id\":\""+ENTITY_ID+"\"";
			expectedJson = expectedJson.replace(prefix, replacement);
		}
		if (channel == Channel.SMS) {
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

		if (channel != Channel.SILENT_AUTH) {
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
	public void testInvalidBrand() {
		for (Channel channel : Channel.values()) {
			Builder builder = getBuilderRequiredParamsSingleWorkflow(channel);
			assertEquals(BRAND, builder.build().getBrand());
			builder.brand(null);
			assertThrows(IllegalArgumentException.class, builder::build);
			builder.brand("  ");
			assertThrows(IllegalArgumentException.class, builder::build);
			assertEquals(17, builder.brand("ABCDEFGHIJKLMNOPQ").build().getBrand().length());
		}
	}

	@Test
	public void testAllWorkflowsWithoutRecipient() {
		for (String invalid : new String[]{"", " ", null}) {
			assertThrows(RuntimeException.class, () -> SilentAuthWorkflow.builder(invalid).build());
			assertThrows(RuntimeException.class, () -> new SmsWorkflow(invalid));
			assertThrows(RuntimeException.class, () -> new VoiceWorkflow(invalid));
			assertThrows(RuntimeException.class, () -> new WhatsappWorkflow(invalid, FROM_NUMBER));
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
		assertTrue(requiredBuilder.build().isCodeless());
		requiredBuilder.codeLength(9);
		assertThrows(IllegalStateException.class, requiredBuilder::build);
	}

	@Test
	public void testCodelessAndCodeValidation() {
		Builder requiredBuilder = getBuilderRequiredParamsSingleWorkflow(Channel.SILENT_AUTH);
		assertTrue(requiredBuilder.build().isCodeless());
		assertTrue(requiredBuilder.build().isCodeless());
		requiredBuilder.code("12345678");
		assertThrows(IllegalStateException.class, requiredBuilder::build);
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
		int min = 15, max = 900;
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
	public void testInvalidSmsAppHash() {
		var builder = SmsWorkflow.builder(TO_NUMBER);
		String valid = "1234567890a";
		assertThrows(IllegalArgumentException.class, () -> builder.appHash(valid.substring(1)).build());
		assertThrows(IllegalArgumentException.class, () -> builder.appHash(valid + 'b').build());
		SmsWorkflow workflow = builder.appHash(valid).build();
		String appHash = workflow.getAppHash();
		assertNotNull(appHash);
		assertEquals(11, appHash.length());
		assertEquals(workflow, SmsWorkflow.builder(TO_NUMBER).appHash(valid).build());
	}

	@Test
	public void testInvalidSmsFrom() {
		SmsWorkflow.Builder builder = SmsWorkflow.builder(TO_NUMBER).from(FROM_NUMBER);
		assertEquals(FROM_NUMBER, builder.build().getFrom());
		assertNotNull(builder.from("Abc").build().getFrom());
		assertNotNull(builder.from("AbcDefGhk12").build().getFrom());
		assertNotNull(builder.from("1-800-123-4567890").build().getFrom());
		assertThrows(IllegalArgumentException.class, () -> builder.from("Ab").build());
		assertThrows(IllegalArgumentException.class, () -> builder.from("1800123456789102").build());
		assertThrows(IllegalArgumentException.class, () -> builder.from("AbcDefGhk123").build());
	}

	@Test
	public void testInvalidSmsContentId() {
		SmsWorkflow.Builder builder = SmsWorkflow.builder(TO_NUMBER);
		String valid = "1234567890abcdefghij";
		assertThrows(IllegalArgumentException.class, () -> builder.contentId("").build());
		assertThrows(IllegalArgumentException.class, () -> builder.contentId(valid + 'k').build());
		String contentId = builder.contentId(valid).build().getContentId();
		assertNotNull(valid);
		assertEquals(20, contentId.length());
	}

	@Test
	public void testInvalidSmsEntityId() {
		SmsWorkflow.Builder builder = SmsWorkflow.builder(TO_NUMBER);
		String valid = "1234567890abcdefghij";
		assertThrows(IllegalArgumentException.class, () -> builder.entityId("").build());
		assertThrows(IllegalArgumentException.class, () -> builder.entityId(valid + 'k').build());
		String entityId = builder.entityId(valid).build().getEntityId();
		assertNotNull(valid);
		assertEquals(20, entityId.length());
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
		assertEquals(saw, builder.addWorkflow(saw).build().getWorkflows().getFirst());
		WhatsappWorkflow waw = new WhatsappWorkflow(saw.getTo(), FROM_NUMBER);
		assertEquals(waw, builder.addWorkflow(waw).build().getWorkflows().get(1));
		builder.workflows(Arrays.asList(waw, saw));
		assertEquals(2, builder.workflows.size());
		assertThrows(IllegalStateException.class, builder::build);
	}

	@Test
	public void testSilentAuthConstructorWithRedirectUrl() {
		SilentAuthWorkflow workflow = new SilentAuthWorkflow(TO_NUMBER, REDIRECT_URL);
		assertEquals(TO_NUMBER, workflow.getTo());
		assertEquals(REDIRECT_URL, workflow.getRedirectUrl().toString());
		assertNull(workflow.getSandbox());
	}

	@Test
	public void testInvalidLocale() {
		VerificationRequest.Builder builder = getBuilderRequiredParamsSingleWorkflow(Channel.SMS);
		assertThrows(IllegalArgumentException.class, () -> builder.locale("--++").build());
		assertThrows(IllegalArgumentException.class, () -> builder.locale("en_GB").build());
		assertThrows(IllegalArgumentException.class, () -> builder.locale("  ").build());
		assertThrows(IllegalArgumentException.class, () -> builder.locale((Locale) null).build());
		assertThrows(NullPointerException.class, () -> builder.locale((String) null).build());
		assertNotNull(builder.locale("ab-cd").build().getLocale());
	}

	@Test
	public void testWhatsappWorkflowWithoutSender() {
		var request = newBuilder()
				.addWorkflow(WhatsappWorkflow.builder(TO_NUMBER, null).build())
				.build();
		assertNotNull(request);
		var expectedJson = "{\"brand\":\""+BRAND+"\",\"workflow\":[" +
				"{\"channel\":\"whatsapp\",\"to\":\""+TO_NUMBER+"\"}]}";
		assertEquals(expectedJson, request.toJson());
	}

	@Test
	public void testTemplateId() throws Exception {
		String templateId = "4ed3027d-8762-44a0-aa3f-c393717413a4";
		
		// Test with SMS workflow - using JSON resource file
		String expectedSmsJson = loadJsonResource(
			"verification-request-with-template-id.json",
			"TEMPLATE_ID_PLACEHOLDER", templateId
		);
		
		VerificationRequest smsRequest = newBuilder()
				.addWorkflow(new SmsWorkflow(TO_NUMBER))
				.templateId(templateId)
				.build();
		assertEquals(templateId, smsRequest.getTemplateId());
		assertEquals(expectedSmsJson, smsRequest.toJson());
		
		// Test without template_id - using JSON resource file
		String expectedJsonWithoutTemplate = loadJsonResource(
			"verification-request-without-template-id.json"
		);
		
		VerificationRequest requestWithoutTemplate = newBuilder()
				.addWorkflow(new SmsWorkflow(TO_NUMBER))
				.build();
		assertNull(requestWithoutTemplate.getTemplateId());
		assertEquals(expectedJsonWithoutTemplate, requestWithoutTemplate.toJson());
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
				.addWorkflow(new SmsWorkflow(TO_NUMBER)).brand("Test")).toJson()
		);
	}
}
