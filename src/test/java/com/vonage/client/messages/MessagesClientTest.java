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
package com.vonage.client.messages;

import com.vonage.client.*;
import com.vonage.client.auth.ApiKeyHeaderAuthMethod;
import com.vonage.client.auth.AuthMethod;
import com.vonage.client.auth.JWTAuthMethod;
import com.vonage.client.common.HttpMethod;
import com.vonage.client.common.MessageType;
import com.vonage.client.messages.messenger.*;
import com.vonage.client.messages.mms.*;
import com.vonage.client.messages.rcs.*;
import com.vonage.client.messages.sms.SmsTextRequest;
import com.vonage.client.messages.viber.ViberFileRequest;
import com.vonage.client.messages.viber.ViberImageRequest;
import com.vonage.client.messages.viber.ViberTextRequest;
import com.vonage.client.messages.viber.ViberVideoRequest;
import com.vonage.client.messages.whatsapp.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import java.util.*;

public class MessagesClientTest extends AbstractClientTest<MessagesClient> {
	private static final String
			MESSAGE_ID = UUID.randomUUID().toString(),
			TEXT = "Nexmo Verification code: 12345. Valid for 10 minutes.",
			IMAGE = "https://www.example.com/picture.jpeg",
			VIDEO = "https://www.example.com/trailer.mp4",
			AUDIO = "https://www.example.com/song.mp3",
			FILE = "https://www.example.com/document.pdf",
			VCARD = "https://example.com/contact.vcf",
			STICKER = "https://example.com/sticker.webp";
	private static final Map<String, ?> CUSTOM = Map.of("One", 1);

	public MessagesClientTest() {
		client = new MessagesClient(wrapper);
	}

	void assertResponse(MessageRequest.Builder<?, ?> builder) throws Exception {
		assertResponse(builder.from("447700900001").to("447700900000").build());
	}

	void assertResponse(MessageRequest request) throws Exception {
		String responseJson = "{\"message_uuid\":\""+MESSAGE_ID+"\"}";
		stubResponse(202, responseJson);
		MessageResponse responseObject = client.useRegularEndpoint().sendMessage(request);
		assertEquals(UUID.fromString(MESSAGE_ID), responseObject.getMessageUuid());
		var messageType = request.getMessageType();
		var channel = request.getChannel();
        if (messageType == MessageType.TEXT) {
            assertInstanceOf(TextMessageRequest.class, request);
        }
		else if (channel == Channel.MMS || channel == Channel.RCS || channel == Channel.MESSENGER) {
			if (messageType != MessageType.CUSTOM) {
            	assertInstanceOf(MediaMessageRequest.class, request);
			}
        }
		else if (messageType == MessageType.VCARD ||
				messageType == MessageType.FILE ||
				messageType == MessageType.VIDEO ||
				messageType == MessageType.AUDIO ||
				messageType == MessageType.IMAGE
		) {
			if (
				(channel == Channel.WHATSAPP && messageType == MessageType.AUDIO) ||
				(channel == Channel.VIBER && (messageType == MessageType.IMAGE || messageType == MessageType.FILE))
			) {
				assertInstanceOf(MediaMessageRequest.class, request);
			}
			else {
				assertInstanceOf(CaptionMediaMessageRequest.class, request);
			}
		}
	}

	void assertException(int statusCode, String json) throws Exception {
		assertApiResponseException(statusCode, json, MessageResponseException.class, () ->
				client.useRegularEndpoint().sendMessage(SmsTextRequest.builder()
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
	public void testSendMessage429Response() throws Exception {
		assertException(429, """
                {
                  "type": "https://developer.nexmo.com/api-errors/messages-olympus#1010",
                  "title": "Rate Limit Hit",
                  "detail": "Please wait, then retry your request",
                  "instance": "bf0ca0bf927b3b52e3cb03217e1a1ddf"
                }"""
		);
	}

	@Test
	public void testSendMessage401Response() throws Exception {
		assertException(401, """
                {
                  "type": "https://developer.nexmo.com/api-errors/#unathorized",
                  "title": "You did not provide correct credentials.",
                  "detail": "Check that you're using the correct credentials, and that your account has this feature enabled",
                  "instance": "bf0ca0bf927b3b52e3cb03217e1a1ddf"
                }"""
		);
	}

	@Test
	public void testSensSmsSandboxFailure() {
		assertThrows(MessageResponseException.class, () -> client.useSandboxEndpoint()
				.sendMessage(SmsTextRequest.builder()
					.text(TEXT).from("447700900001").to("447700900002").build()
				)
		);
		client.useRegularEndpoint();
	}

	@Test
	public void testSendSmsSuccess() throws Exception {
		assertResponse(SmsTextRequest.builder().text(TEXT));
	}

	@Test
	public void testSendMmsSuccess() throws Exception {
		assertResponse(MmsTextRequest.builder().text(TEXT));
		assertResponse(MmsVcardRequest.builder().url(VCARD));
		assertResponse(MmsImageRequest.builder().url(IMAGE));
		assertResponse(MmsAudioRequest.builder().url(AUDIO));
		assertResponse(MmsVideoRequest.builder().url(VIDEO));
		assertResponse(MmsFileRequest.builder().url(FILE));
		assertResponse(MmsContentRequest.builder().addVcard(VCARD));
	}

	@Test
	public void testSendRcsSuccess() throws Exception {
		assertResponse(RcsTextRequest.builder().text(TEXT));
		assertResponse(RcsImageRequest.builder().url(IMAGE));
		assertResponse(RcsVideoRequest.builder().url(VIDEO));
		assertResponse(RcsFileRequest.builder().url(FILE));
		assertResponse(RcsCustomRequest.builder().custom(CUSTOM));
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
		assertResponse(WhatsappReactionRequest.builder().unreact().contextMessageId(MESSAGE_ID));
		assertResponse(WhatsappLocationRequest.builder().latitude(40.34772).longitude(-74.18847));
		assertResponse(WhatsappSingleProductRequest.builder().catalogId("c1d").productRetailerId("p1d"));
		assertResponse(WhatsappMultiProductRequest.builder()
				.catalogId("ca7").bodyText("b0d").headerText("Check it out")
				.addProductsSection("test", Arrays.asList("5ku1", "p2"))
		);
		assertResponse(WhatsappCustomRequest.builder().custom(CUSTOM));
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
	public void testAckInboundMessage() throws Exception {
		stubResponseAndRun(200, () -> client.ackInboundMessage(MESSAGE_ID, null));
		stubResponseAndAssertThrows(() -> client.ackInboundMessage(null, ApiRegion.API_US), NullPointerException.class);
		stubResponseAndAssertThrows(() -> client.ackInboundMessage("not-an-id", null), IllegalArgumentException.class);
		stubResponseAndRun(200, () -> client.ackInboundMessage(MESSAGE_ID, ApiRegion.API_EU));
		assertApiResponseException(404, """
                {
                   "type": "https://developer.vonage.com/api-errors#not-found",
                   "title": "Not Found",
                   "detail": "External Id not found for message unknown_id.",
                   "instance": "bf0ca0bf927b3b52e3cb03217e1a1ddf"
                }""", MessageResponseException.class,
				() -> client.ackInboundMessage(MESSAGE_ID, ApiRegion.API_US)
		);
	}

	@Test
	public void testRevokeOutboundMessage() throws Exception {
		stubResponseAndRun(200, () -> client.ackInboundMessage(MESSAGE_ID, null));
		stubResponseAndAssertThrows(() -> client.revokeOutboundMessage(null, ApiRegion.API_EU), NullPointerException.class);
		stubResponseAndAssertThrows(() -> client.revokeOutboundMessage("not-an-id", null), IllegalArgumentException.class);
		stubResponseAndRun(200, () -> client.revokeOutboundMessage(MESSAGE_ID, ApiRegion.API_US));
		assertApiResponseException(422, """
                {
                   "type": "https://developer.vonage.com/api-errors#unprocessable-entity",
                   "title": "Unprocessable Entity",
                   "detail": "Operation not supported for this channel.",
                   "instance": "bf0ca0bf927b3b52e3cb03217e1a1ddf"
                }""", MessageResponseException.class,
				() -> client.revokeOutboundMessage(MESSAGE_ID, ApiRegion.API_EU)
		);
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
				final String json = """
                        {
                          "type": "https://developer.nexmo.com/api-errors/messages-olympus#1120",
                          "title": "Invalid sender",
                          "detail": "The `from` parameter is invalid for the given channel.",
                          "instance": "bf0ca0bf927b3b52e3cb03217e1a1ddf"
                        }""";

				try {
					parseResponse(json, statusCode);
					fail("Expected "+ MessageResponseException.class.getName());
				}
				catch (MessageResponseException mrx) {
					MessageResponseException expected = Jsonable.fromJson(json);
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

	@Test
	public void testUpdateMessageEndpoint() throws Exception {
		new DynamicEndpointTestSpec<UpdateStatusRequest, Void>() {

			@Override
			protected RestEndpoint<UpdateStatusRequest, Void> endpoint() {
				return client.updateMessage;
			}

			@Override
			protected HttpMethod expectedHttpMethod() {
				return HttpMethod.PATCH;
			}

			@Override
			protected Collection<Class<? extends AuthMethod>> expectedAuthMethods() {
				return List.of(JWTAuthMethod.class);
			}

			@Override
			protected Class<? extends Exception> expectedResponseExceptionType() {
				return MessageResponseException.class;
			}

			@Override
			protected String expectedDefaultBaseUri() {
				return wrapper.getHttpConfig().getRegionalBaseUri(sampleRequest().region).toString();
			}

			@Override
			protected String expectedCustomBaseUri() {
				return expectedDefaultBaseUri().replace("vonage", "example");
			}

			@Override
			protected String expectedEndpointUri(UpdateStatusRequest request) {
				return "/v1/messages/" + request.messageId;
			}

			@Override
			protected UpdateStatusRequest sampleRequest() {
				return new UpdateStatusRequest("Updated", MESSAGE_ID, null);
			}

			@Override
			protected String sampleRequestBodyString() {
				return "{\"status\":\"Updated\"}";
			}

			@Override
			public void runTests() throws Exception {
				super.runTests();
				assertEquals("https://api-eu.vonage.com", expectedDefaultBaseUri());

			}
		}
		.runTests();
	}
}
