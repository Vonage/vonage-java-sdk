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
import com.vonage.client.messages.mms.*;
import com.vonage.client.messages.sms.*;
import com.vonage.client.messages.whatsapp.*;
import com.vonage.client.messages.messenger.*;
import com.vonage.client.messages.viber.*;
import org.junit.Test;

import java.util.Collections;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class MessagesClientTest extends ClientTest<MessagesClient> {

	private static final String
			TEXT = "Nexmo Verification code: 12345. Valid for 10 minutes.",
			IMAGE = "https://www.example.com/picture.jpeg",
			VIDEO = "https://www.example.com/trailer.mp4",
			AUDIO = "https://www.example.com/song.mp3",
			FILE = "https://www.example.com/document.pdf",
			VCARD = "https://example.com/contact.vcf";

	public MessagesClientTest() {
		client = new MessagesClient(wrapper);
	}

	void assertResponse(MessageRequest.Builder<?, ?> builder) throws Exception {
		assertResponse(builder.from("447700900001").to("447700900000").build());
	}

	void assertResponse(MessageRequest request) throws Exception {
		String uuid = UUID.randomUUID().toString();
		String responseJson = "{\"message_uuid\":\""+uuid+"\"}";
		wrapper.setHttpClient(stubHttpClient(202, responseJson));
		MessageResponse responseObject = client.sendMessage(request);
		assertEquals(uuid, responseObject.getMessageUuid());
	}

	@Test
	public void testSendSmsSuccess() throws Exception {
		assertResponse(SmsRequest.builder().text(TEXT));
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
	}

	@Test
	public void testSendWhatsappSuccess() throws Exception {
		assertResponse(WhatsappTextRequest.builder().text(TEXT));
		assertResponse(WhatsappImageRequest.builder().url(IMAGE));
		assertResponse(WhatsappAudioRequest.builder().url(AUDIO));
		assertResponse(WhatsappVideoRequest.builder().url(VIDEO));
		assertResponse(WhatsappFileRequest.builder().url(FILE));
		assertResponse(WhatsappCustomRequest.builder().custom(Collections.emptyMap()));
	}

	@Test
	public void testSendMessengerSuccess() throws Exception {
		assertResponse(MessengerTextRequest.builder().text(TEXT));
		assertResponse(MessengerImageRequest.builder().url(IMAGE));
		assertResponse(MessengerAudioRequest.builder().url(AUDIO));
		assertResponse(MessengerVideoRequest.builder().url(VIDEO));
		assertResponse(MessengerFileRequest.builder().url(FILE));
	}
}
