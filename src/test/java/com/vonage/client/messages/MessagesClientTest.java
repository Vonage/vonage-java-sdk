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
package com.vonage.client.messages;

import com.vonage.client.AbstractClientTest;
import com.vonage.client.DynamicEndpointTestSpec;
import com.vonage.client.RestEndpoint;
import com.vonage.client.VonageApiResponseException;
import com.vonage.client.auth.AuthMethod;
import com.vonage.client.auth.JWTAuthMethod;
import com.vonage.client.auth.ApiKeyHeaderAuthMethod;
import com.vonage.client.common.HttpMethod;
import com.vonage.client.messages.messenger.*;
import com.vonage.client.messages.mms.MmsAudioRequest;
import com.vonage.client.messages.mms.MmsImageRequest;
import com.vonage.client.messages.mms.MmsVcardRequest;
import com.vonage.client.messages.sms.SmsTextRequest;
import com.vonage.client.messages.viber.ViberFileRequest;
import com.vonage.client.messages.viber.ViberImageRequest;
import com.vonage.client.messages.viber.ViberTextRequest;
import com.vonage.client.messages.viber.ViberVideoRequest;
import com.vonage.client.messages.whatsapp.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

public class MessagesClientTest extends AbstractClientTest<MessagesClient> {

	private static final String
			TEXT = "Nexmo Verification code: 12345. Valid for 10 minutes.",
			IMAGE = "https://www.example.com/picture.jpeg",
			VIDEO = "https://www.example.com/trailer.mp4",
			AUDIO = "https://www.example.com/song.mp3",
			FILE = "https://www.example.com/document.pdf",
			VCARD = "https://example.com/contact.vcf",
			STICKER = "https://example.com/sticker.webp";

	public MessagesClientTest() {
		client = new MessagesClient(wrapper);
	}

	void assertResponse(MessageRequest.Builder<?, ?> builder) throws Exception {
		assertResponse(builder.from("447700900001").to("447700900000").build());
	}

	void assertResponse(MessageRequest request) throws Exception {
		UUID uuid = UUID.randomUUID();
		String responseJson = "{\"message_uuid\":\""+uuid+"\"}";
		wrapper.setHttpClient(stubHttpClient(202, responseJson));
		MessageResponse responseObject = client.sendMessage(request);
		assertEquals(uuid, responseObject.getMessageUuid());
	}

	void assertException(int statusCode, String json) throws Exception {
		assertApiResponseException(statusCode, json, MessageResponseException.class, () ->
				client.sendMessage(SmsTextRequest.builder()
					.from("447700900001").to("447700900000")
					.text("Hello").build()
				)
		);
	}

	@Test
	public void testVerifySignature() {
		String secret = "XsA09z2MhUxYcdbXaUX3aTT7TzGmnCLfkdILf0NIyC9hN9criTEUdlI3OZ5hRjR";
		String header = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9";
		String payload = "eyJzdWIiOiJTaW5hIiwibmFtZSI6IkphdmFfU0RLIiwiaWF0IjoxNjk4NjgwMzkyfQ";
		String signature = "4qJpi46NSYURiLI1xoLIfGRygA8IUI2QSG9P2Kus1Oo";
		String token = header + '.' + payload + '.' + signature;
		assertTrue(MessagesClient.verifySignature(token, secret));
		String badToken = header + '.' + payload + '.' + "XsaXHXqxe2kfIbPy-JH2J6hfbHnEv8jdWsOhEuvzU98";
		assertFalse(MessagesClient.verifySignature(badToken, secret));
		assertThrows(NullPointerException.class, () -> MessagesClient.verifySignature(null, secret));
		assertThrows(NullPointerException.class, () -> MessagesClient.verifySignature(token, null));
	}

	@Test
	public void test429Response() throws Exception {
		assertException(429, "{\n" +
				"  \"type\": \"https://developer.nexmo.com/api-errors/messages-olympus#1010\",\n" +
				"  \"title\": \"Rate Limit Hit\",\n" +
				"  \"detail\": \"Please wait, then retry your request\",\n" +
				"  \"instance\": \"bf0ca0bf927b3b52e3cb03217e1a1ddf\"\n" +
				"}"
		);
	}

	@Test
	public void test401Response() throws Exception {
		assertException(401, "{\n" +
				"  \"type\": \"https://developer.nexmo.com/api-errors/#unathorized\",\n" +
				"  \"title\": \"You did not provide correct credentials.\",\n" +
				"  \"detail\": \"Check that you're using the correct credentials, and that your account has this feature enabled\",\n" +
				"  \"instance\": \"bf0ca0bf927b3b52e3cb03217e1a1ddf\"\n" +
				"}"
		);
	}

	@Test
	public void testSendSmsSuccess() throws Exception {
		assertResponse(SmsTextRequest.builder().text(TEXT));
	}

	@Test
	public void testSendMmsSuccess() throws Exception {
		assertResponse(MmsVcardRequest.builder().url(VCARD));
		assertResponse(MmsImageRequest.builder().url(IMAGE));
		assertResponse(MmsAudioRequest.builder().url(AUDIO));
		assertResponse(MmsAudioRequest.builder().url(VIDEO));
	}

	@Test
	public void testSendViberSuccess() throws Exception {
		assertResponse(ViberTextRequest.builder().text(TEXT));
		assertResponse(ViberImageRequest.builder().url(IMAGE));
		assertResponse(ViberVideoRequest.builder().url(VIDEO).thumbUrl(IMAGE).duration(15).fileSize(8));
		assertResponse(ViberFileRequest.builder().url(FILE));
	}

	@Test
	public void testSendWhatsappSuccess() throws Exception {
		assertResponse(WhatsappTextRequest.builder().text(TEXT));
		assertResponse(WhatsappImageRequest.builder().url(IMAGE));
		assertResponse(WhatsappAudioRequest.builder().url(AUDIO));
		assertResponse(WhatsappVideoRequest.builder().url(VIDEO));
		assertResponse(WhatsappFileRequest.builder().url(FILE));
		assertResponse(WhatsappStickerRequest.builder().url(STICKER));
		assertResponse(WhatsappLocationRequest.builder().latitude(40.34772).longitude(-74.18847));
		assertResponse(WhatsappSingleProductRequest.builder().catalogId("c1d").productRetailerId("p1d"));
		assertResponse(WhatsappMultiProductRequest.builder()
				.catalogId("ca7").bodyText("b0d").headerText("Check it out")
				.addProductsSection("test", Arrays.asList("5ku1", "p2"))
		);
		assertResponse(WhatsappCustomRequest.builder().custom(Collections.emptyMap()));
		assertResponse(WhatsappTemplateRequest.builder().name("fb"));
	}

	@Test
	public void testSendMessengerSuccess() throws Exception {
		assertResponse(MessengerTextRequest.builder().text(TEXT));
		assertResponse(MessengerImageRequest.builder().url(IMAGE));
		assertResponse(MessengerAudioRequest.builder().url(AUDIO));
		assertResponse(MessengerVideoRequest.builder().url(VIDEO));
		assertResponse(MessengerFileRequest.builder().url(FILE));
	}

	@Test
	public void testSendMessageEndpoint() throws Exception {
		new DynamicEndpointTestSpec<MessageRequest, MessageResponse>() {
			boolean sandbox;

			@Override
			protected RestEndpoint<MessageRequest, MessageResponse> endpoint() {
				return sandbox ? client.sendMessageSandbox : client.sendMessage;
			}

			@Override
			protected String customBaseUri() {
				return sandbox ? expectedDefaultBaseUri() : super.customBaseUri();
			}

			@Override
			protected HttpMethod expectedHttpMethod() {
				return HttpMethod.POST;
			}

			@Override
			protected Collection<Class<? extends AuthMethod>> expectedAuthMethods() {
				return Arrays.asList(JWTAuthMethod.class, ApiKeyHeaderAuthMethod.class);
			}

			@Override
			protected Class<? extends VonageApiResponseException> expectedResponseExceptionType() {
				return MessageResponseException.class;
			}

			@Override
			protected String expectedDefaultBaseUri() {
				return sandbox ? "https://messages-sandbox.nexmo.com" : "https://api.nexmo.com";
			}

			@Override
			protected String expectedEndpointUri(MessageRequest request) {
				return "/v1/messages";
			}

			@Override
			protected MessageRequest sampleRequest() {
				return SmsTextRequest.builder()
						.from("447700900001").to("447700900000")
						.text("Hello, World!").build();
			}

			@Override
			protected String sampleRequestBodyString() {
				return "{\"message_type\":\"text\",\"channel\":\"sms\",\"from\":" +
						"\"447700900001\",\"to\":\"447700900000\",\"text\":\"Hello, World!\"}";
			}

			@Override
			public void runTests() throws Exception {
				super.runTests();
				testParseResponseSuccess();
				testSandboxEndpoint();
				testParseResponseFailureAllParams();
			}

			void testParseResponseSuccess() throws Exception {
				UUID uuid = UUID.randomUUID();
				String json = "{\"message_uuid\":\""+uuid+"\"}";
				MessageResponse response = parseResponse(json, 202);
				assertEquals(uuid, response.getMessageUuid());
			}

			void testSandboxEndpoint() throws Exception {
				sandbox = true;
				assertEquals(client, client.useSandboxEndpoint());
				assertRequestUriAndBody();
				sandbox = false;
				assertEquals(client, client.useRegularEndpoint());
				assertRequestUriAndBody();
			}

			void testParseResponseFailureAllParams() throws Exception {
				final int statusCode = 422;
				final String json = "{\n" +
						"  \"type\": \"https://developer.nexmo.com/api-errors/messages-olympus#1120\",\n" +
						"  \"title\": \"Invalid sender\",\n" +
						"  \"detail\": \"The `from` parameter is invalid for the given channel.\",\n" +
						"  \"instance\": \"bf0ca0bf927b3b52e3cb03217e1a1ddf\"\n" +
						"}";

				try {
					parseResponse(json, statusCode);
					fail("Expected "+ MessageResponseException.class.getName());
				}
				catch (MessageResponseException mrx) {
					MessageResponseException expected = MessageResponseException.fromJson(json);
					expected.setStatusCode(statusCode);
					assertEquals(expected, mrx);
					assertEquals(statusCode, mrx.getStatusCode());
					assertNotNull(mrx.getType());
					assertNotNull(mrx.getTitle());
					assertNotNull(mrx.getDetail());
					assertNotNull(mrx.getInstance());
				}
			}
		}
		.runTests();
	}
}
