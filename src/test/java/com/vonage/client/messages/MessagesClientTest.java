/*
 *   Copyright 2022 Vonage
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

import com.vonage.client.ClientTest;
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
import org.apache.http.client.methods.RequestBuilder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Test;
import java.util.Collections;
import java.util.UUID;

public class MessagesClientTest extends ClientTest<MessagesClient> {

	private static final String
			TEXT = "Nexmo Verification code: 12345. Valid for 10 minutes.",
			IMAGE = "https://www.example.com/picture.jpeg",
			VIDEO = "https://www.example.com/trailer.mp4",
			AUDIO = "https://www.example.com/song.mp3",
			FILE = "https://www.example.com/document.pdf",
			VCARD = "https://example.com/contact.vcf";

	private static final MessageRequest REQUEST = SmsTextRequest.builder()
			.from("447700900001").to("447700900000").text("Hello").build();

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

	void assertException(int statusCode, MessageResponseException expectedResponse) throws Exception {
		wrapper.setHttpClient(stubHttpClient(statusCode, expectedResponse.toJson()));
		expectedResponse.statusCode = statusCode;
		try {
			client.sendMessage(REQUEST);
			fail("Expected MessageResponseException");
		}
		catch (MessageResponseException mrx) {
			assertEquals(expectedResponse, mrx);
		}
	}

	@Test
	public void test429Response() throws Exception {
		assertException(429, MessageResponseException.fromJson("{\n" +
				"  \"type\": \"https://developer.nexmo.com/api-errors/messages-olympus#1010\",\n" +
				"  \"title\": \"Rate Limit Hit\",\n" +
				"  \"detail\": \"Please wait, then retry your request\",\n" +
				"  \"instance\": \"bf0ca0bf927b3b52e3cb03217e1a1ddf\"\n" +
				"}"
		));
	}

	@Test
	public void test401Response() throws Exception {
		assertException(401, MessageResponseException.fromJson("{\n" +
				"  \"type\": \"https://developer.nexmo.com/api-errors/#unathorized\",\n" +
				"  \"title\": \"You did not provide correct credentials.\",\n" +
				"  \"detail\": \"Check that you're using the correct credentials, and that your account has this feature enabled\",\n" +
				"  \"instance\": \"bf0ca0bf927b3b52e3cb03217e1a1ddf\"\n" +
				"}"
		));
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
		assertResponse(ViberVideoRequest.builder().url(VIDEO));
		assertResponse(ViberFileRequest.builder().url(FILE));
	}

	@Test
	public void testSendWhatsappSuccess() throws Exception {
		assertResponse(WhatsappTextRequest.builder().text(TEXT));
		assertResponse(WhatsappImageRequest.builder().url(IMAGE));
		assertResponse(WhatsappAudioRequest.builder().url(AUDIO));
		assertResponse(WhatsappVideoRequest.builder().url(VIDEO));
		assertResponse(WhatsappFileRequest.builder().url(FILE));
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
	public void testSandboxUriToggle() {
		final String defaultUri = "https://api.nexmo.com/v1/messages";
		final String sandboxUri = "https://messages-sandbox.nexmo.com/v1/messages";
		SendMessageEndpoint endpoint = client.sendMessage;

		MessageRequest sms = SmsTextRequest.builder()
				.from("447700900001").to("447700900000")
				.text("Hello, World!").build();

		RequestBuilder builder = endpoint.makeRequest(sms);
		assertEquals("POST", builder.getMethod());
		assertEquals(defaultUri, builder.build().getURI().toString());
		client.useSandboxEndpoint();
		builder = endpoint.makeRequest(sms);
		assertEquals(sandboxUri, builder.build().getURI().toString());
		client.useRegularEndpoint();
		builder = endpoint.makeRequest(sms);
		assertEquals(defaultUri, builder.build().getURI().toString());
	}
}
