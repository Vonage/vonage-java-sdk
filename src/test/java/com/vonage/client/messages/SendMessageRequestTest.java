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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vonage.client.VonageUnexpectedException;
import org.junit.Test;

import static org.junit.Assert.*;

public class SendMessageRequestTest {

	static class ConcreteSendMessageRequest extends SendMessageRequest {
		ConcreteSendMessageRequest() {
			super();
		}

		ConcreteSendMessageRequest(String from, String to, MessageType messageType, Channel channel, String clientRef) {
			super(from, to, messageType, channel);
			this.clientRef = clientRef;
		}

		static ConcreteSendMessageRequest fromJson(String json) throws JsonProcessingException {
			return new ObjectMapper().readValue(json, ConcreteSendMessageRequest.class);
		}
	}

	@Test
	public void testSerializeAllFields() throws Exception {
		SendMessageRequest smr = new ConcreteSendMessageRequest(
				"447900000009",
				"12002009000",
				MessageType.VIDEO,
				Channel.MMS,
				"<40 character string"
		);

		String generatedJson = smr.toJson();
		SendMessageRequest generatedObject = ConcreteSendMessageRequest.fromJson(generatedJson);
		assertEquals(generatedJson, generatedObject.toJson());

		assertTrue(generatedJson.contains("\"client_ref\":\"<40 character string\""));
		assertTrue(generatedJson.contains("\"from\":\"447900000009\""));
		assertTrue(generatedJson.contains("\"to\":\"12002009000\""));
		assertTrue(generatedJson.contains("\"channel\":\"mms\""));
		assertTrue(generatedJson.contains("\"message_type\":\"video\""));
	}

	@Test
	public void testSerializeFieldsWithoutClientRef() throws Exception {
		SendMessageRequest smr = new ConcreteSendMessageRequest(
				"447900000009",
				"12002009000",
				MessageType.IMAGE,
				Channel.VIBER,
				null
		);

		String generatedJson = smr.toJson();
		SendMessageRequest generatedObject = ConcreteSendMessageRequest.fromJson(generatedJson);
		assertEquals(generatedJson, generatedObject.toJson());

		assertFalse(generatedJson.contains("client_ref"));
		assertTrue(generatedJson.contains("\"from\":\"447900000009\""));
		assertTrue(generatedJson.contains("\"to\":\"12002009000\""));
		assertTrue(generatedJson.contains("\"channel\":\"viber_service\""));
		assertTrue(generatedJson.contains("\"message_type\":\"image\""));
	}

	@Test
	public void testSerializeFieldsWithoutChannelOrMessageType() throws Exception {
		SendMessageRequest smr = new ConcreteSendMessageRequest(
				"12002009000",
				"447900000009",
				MessageType.CUSTOM,
				Channel.WHATSAPP,
				"a reference"
		);

		smr.messageType = null;
		smr.channel = null;
		smr.setClientRef(null);

		String generatedJson = smr.toJson();
		SendMessageRequest generatedObject = ConcreteSendMessageRequest.fromJson(generatedJson);
		assertEquals(generatedJson, generatedObject.toJson());

		assertFalse(generatedJson.contains("client_ref"));
		assertFalse(generatedJson.contains("message_type"));
		assertFalse(generatedJson.contains("channel"));
		assertTrue(generatedJson.contains("\"to\":\"447900000009\""));
		assertTrue(generatedJson.contains("\"from\":\"12002009000\""));
	}

	@Test(expected = VonageUnexpectedException.class)
	public void testSerializeNoNumbers() {
		SendMessageRequest smr = new ConcreteSendMessageRequest(
				"447900000009",
				"447900000001",
				MessageType.TEXT,
				Channel.SMS,
				null
		);
		smr.from = null;
		smr.to = null;
		smr.toJson();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructInvalidMessageType() {
		new ConcreteSendMessageRequest(
				"447900000001",
				"447900000009",
				MessageType.IMAGE,
				Channel.SMS,
				null
		);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructEmptyNumbers() {
		new ConcreteSendMessageRequest(
				"",
				"",
				MessageType.FILE,
				Channel.MESSENGER,
				null
		);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructNoNumbers() {
		new ConcreteSendMessageRequest(
				"",
				null,
				MessageType.FILE,
				Channel.MESSENGER,
				null
		);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructInvalidNumber() {
		new ConcreteSendMessageRequest(
				"447900000001",
				"+0 NaN",
				MessageType.FILE,
				Channel.MESSENGER,
				null
		);
	}

	@Test
	public void testConstructMalformedButSalvagableNumbers() {
		new ConcreteSendMessageRequest(
				"+44 7900090000",
				"+1 900-900-0000",
				MessageType.AUDIO,
				Channel.MESSENGER,
				null
		);
	}

	@Test(expected = NullPointerException.class)
	public void testConstructNoMessageType() {
		new ConcreteSendMessageRequest(
				"447900000001",
				"447900000009",
				null,
				Channel.MESSENGER,
				null
		);
	}

	@Test(expected = NullPointerException.class)
	public void testConstructNoChannel() {
		new ConcreteSendMessageRequest(
				"447900000001",
				"447900000009",
				MessageType.VCARD,
				null,
				null
		);
	}
}
