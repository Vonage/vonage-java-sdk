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
package com.vonage.client.messages;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.VonageUnexpectedException;
import com.vonage.client.messages.whatsapp.ConversationType;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.net.URI;
import java.time.Instant;
import java.util.Currency;
import java.util.Map;
import java.util.UUID;

public class MessageStatusTest {

	@Test
	public void testSerdesAllFields() {
		UUID messageUuid = UUID.randomUUID();
		String to = "447700900000", from = "447700900001", networkCode = "54321";
		String timestamp = "2020-01-01T14:00:03.010Z";
		MessageStatus.Status status = MessageStatus.Status.SUBMITTED;
		Channel channel = Channel.SMS;
		URI type = URI.create("https://developer.nexmo.com/api-errors/messages-olympus#1000");
		int title = 1000, countTotal = 3;
		String detail = "Throttled - You have exceeded the submission capacity allowed on this account.";
		String instance = "bf0ca0bf927b3b52e3cb03217e1a1ddf";
		Currency currency = Currency.getInstance("EUR");
		double price = 0.0333;
		MessageStatus.Error error = new MessageStatus.Error();
		error.type = type;
		error.title = String.valueOf(title);
		error.detail = detail;
		error.instance = instance;
		MessageStatus.Usage usage = new MessageStatus.Usage();
		usage.price = price;
		usage.currency = currency;
		MessageStatus.Sms sms = new MessageStatus.Sms();
		sms.countTotal = countTotal;
		MessageStatus.Destination destination = new MessageStatus.Destination();
		destination.networkCode = networkCode;
		MessageStatus.Whatsapp whatsapp = new MessageStatus.Whatsapp();
		MessageStatus.Whatsapp.Conversation conversation = new MessageStatus.Whatsapp.Conversation();
		whatsapp.conversation = conversation;
		MessageStatus.Whatsapp.Conversation.Origin origin = new MessageStatus.Whatsapp.Conversation.Origin();
		conversation.origin = origin;
		ConversationType whatsappConversationType = ConversationType.REFERRAL_CONVERSION;
		origin.type = whatsappConversationType;
		String whatsappConversationId = "1234567890";
		conversation.id = whatsappConversationId;

		String json = "{\n" +
				"  \"message_uuid\": \""+messageUuid+"\",\n" +
				"  \"to\": \""+to+"\",\n" +
				"  \"from\": \""+from+"\",\n" +
				"  \"timestamp\": \""+timestamp+"\",\n" +
				"  \"status\": \""+status+"\",\n" +
				"  \"channel\": \""+channel+"\",\n" +
				"  \"error\": {\n" +
				"    \"type\": \""+type+"\",\n" +
				"    \"title\": "+title+",\n" +
				"    \"detail\": \""+detail+"\",\n" +
				"    \"instance\": \""+instance+"\"\n" +
				"  },\n" +
				"  \"usage\": {\n" +
				"    \"currency\": \""+currency+"\",\n" +
				"    \"price\": \""+price+"\"\n" +
				"  },\n" +
				"  \"sms\": {\n" +
				"    \"count_total\": \""+countTotal+"\"\n" +
				"  },\n" +
				"  \"destination\": {\n" +
				"    \"network_code\": \""+networkCode+"\"\n" +
				"  },\n" +
				"  \"whatsapp\": {\n" +
				"      \"conversation\": {\n" +
				"         \"id\": \""+whatsappConversationId+"\",\n" +
				"         \"origin\": {\n" +
				"            \"type\": \""+whatsappConversationType+"\"\n" +
				"         }\n" +
				"      }\n" +
				"   }\n" +
				"}";

		MessageStatus ms = MessageStatus.fromJson(json);
		String generatedJson = ms.toJson();
		MessageStatus generatedMessageStatus = MessageStatus.fromJson(generatedJson);
		assertEquals(ms, generatedMessageStatus);
		assertEquals(ms.hashCode(), generatedMessageStatus.hashCode());
		assertEquals(generatedJson, generatedMessageStatus.toJson());
		assertEquals(ms.toString(), generatedMessageStatus.toString());

		assertEquals(messageUuid, ms.getMessageUuid());
		assertEquals(to, ms.getTo());
		assertEquals(from, ms.getFrom());
		assertEquals(Instant.parse(timestamp), ms.getTimestamp());
		assertEquals(status, ms.getStatus());
		assertEquals("submitted", status.toString());
		assertEquals(channel, ms.getChannel());
		assertEquals("sms", channel.toString());
		assertEquals(error, ms.getError());
		assertEquals(error.toString(), ms.getError().toString());
		assertEquals(usage, ms.getUsage());
		assertEquals(usage.toString(), ms.getUsage().toString());
		assertEquals(networkCode, ms.getDestinationNetworkCode());
		assertEquals(countTotal, ms.getSmsTotalCount());
		assertEquals(whatsappConversationId, ms.getWhatsappConversationId());
		assertEquals("referral_conversion", ms.getWhatsappConversationType().toString());
		assertNull(ms.getAdditionalProperties());
	}

	@Test
	public void testSerdesRequiredFields() {
		UUID messageUuid = UUID.randomUUID();
		String to = "447700900000", from = "447700900001";
		String timestamp = "2020-01-08T15:43:21.000Z";
		MessageStatus.Status status = MessageStatus.Status.UNDELIVERABLE;
		Channel channel = Channel.MMS;

		String json = "{\n" +
				"  \"message_uuid\": \""+messageUuid+"\",\n" +
				"  \"to\": \""+to+"\",\n" +
				"  \"from\": \""+from+"\",\n" +
				"  \"timestamp\": \""+timestamp+"\",\n" +
				"  \"status\": \""+status+"\",\n" +
				"  \"channel\": \""+channel+"\"\n" +
				"}";

		MessageStatus ms = MessageStatus.fromJson(json);
		String generatedJson = ms.toJson();
		assertEquals(generatedJson, MessageStatus.fromJson(generatedJson).toJson());

		assertEquals(messageUuid, ms.getMessageUuid());
		assertEquals(to, ms.getTo());
		assertEquals(from, ms.getFrom());
		assertEquals(Instant.parse(timestamp), ms.getTimestamp());
		assertEquals(status, ms.getStatus());
		assertEquals("undeliverable", status.toString());
		assertEquals(channel, ms.getChannel());
		assertEquals("mms", channel.toString());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testDeserializeUnknownProperties() {
		String json = "{\n" +
				"   \"message_uuid\": \"aaaaaaaa-bbbb-cccc-dddd-0123456789ab\",\n" +
				"   \"to\": \"447700900000\",\n" +
				"   \"from\": \"447700900001\",\n" +
				"   \"timestamp\": \"2020-01-01T14:00:00.000Z\",\n" +
				"   \"status\": \"read\",\n" +
				"   \"error\": {\n" +
				"      \"type\": \"https://developer.nexmo.com/api-errors/messages-olympus#1000\",\n" +
				"      \"title\": 1000,\n" +
				"      \"detail\": \"Throttled - You have exceeded the submission capacity allowed on this account. Please wait and retry\",\n" +
				"      \"instance\": \"bf0ca0bf927b3b52e3cb03217e1a1ddf\"\n" +
				"   },\n" +
				"   \"usage\": {\n" +
				"      \"currency\": \"EUR\",\n" +
				"      \"price\": \"0.0333\"\n" +
				"   },\n" +
				"   \"client_ref\": \"string\",\n" +
				"   \"channel\": \"whatsapp\",\n" +
				"   \"wubwub\": {\n" +
				"      \"Prop0\": {\n" +
				"         \"id\": \"ab12cdhfgjk3\",\n" +
				"         \"N3s7ed\": {\n" +
				"            \"type\": \"user_initiated\"\n" +
				"         }\n" +
				"      }\n" +
				"   }\n" +
				"}";
		MessageStatus ms = MessageStatus.fromJson(json);
		Map<String, ?> unknown = ms.getAdditionalProperties();
		assertNotNull(unknown);
		assertEquals(1, unknown.size());
		Map<String, ?> whatsapp = (Map<String, ?>) unknown.get("wubwub");
		Map<String, ?> conversation = (Map<String, ?>) whatsapp.get("Prop0");
		assertEquals("ab12cdhfgjk3", conversation.get("id"));
		Map<String, ?> origin = (Map<String, ?>) conversation.get("N3s7ed");
		assertEquals("user_initiated", origin.get("type"));
	}

	@Test
	public void testFromJsonInvalid() {
		assertThrows(VonageUnexpectedException.class, () -> MessageStatus.fromJson("{malformed]"));
	}

	@Test
	public void triggerJsonProcessingException() {
		class SelfRefrencing extends MessageStatus {
			@JsonProperty("self") final SelfRefrencing self = this;
		}
		assertThrows(VonageUnexpectedException.class, () -> new SelfRefrencing().toJson());
	}
}
