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

import org.junit.Test;
import java.net.URI;
import java.util.Currency;
import java.util.UUID;
import static org.junit.Assert.assertEquals;

public class MessageStatusTest {

	@Test
	public void testSerdesAllFields() {
		UUID messageUuid = UUID.randomUUID();
		String to = "447700900000", from = "447700900001";
		String timestamp = "2020-01-01 15:43:21 +0200";
		MessageStatus.Status status = MessageStatus.Status.SUBMITTED;
		Channel channel = Channel.SMS;
		URI type = URI.create("https://developer.nexmo.com/api-errors/messages-olympus#1000");
		int title = 1000;
		String detail = "Throttled - You have exceeded the submission capacity allowed on this account.";
		String instance = "bf0ca0bf927b3b52e3cb03217e1a1ddf";
		Currency currency = Currency.getInstance("EUR");
		double price = 0.0333;
		MessageStatus.Error error = new MessageStatus.Error();
		error.type = type;
		error.title = title+"";
		error.detail = detail;
		error.instance = instance;
		MessageStatus.Usage usage = new MessageStatus.Usage();
		usage.price = price;
		usage.currency = currency;

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
				"  }\n" +
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
		assertEquals(timestamp, ms.getTimestamp().format(MessageStatus.ISO_8601));
		assertEquals(status, ms.getStatus());
		assertEquals("submitted", status.toString());
		assertEquals(channel, ms.getChannel());
		assertEquals("sms", channel.toString());
		assertEquals(error, ms.getError());
		assertEquals(error.toString(), ms.getError().toString());
		assertEquals(usage, ms.getUsage());
		assertEquals(usage.toString(), ms.getUsage().toString());
	}

	@Test
	public void testSerdesRequiredFields() {
		UUID messageUuid = UUID.randomUUID();
		String to = "447700900000", from = "447700900001";
		String timestamp = "2020-01-08 15:43:21 +0000";
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
		assertEquals(timestamp, ms.getTimestamp().format(MessageStatus.ISO_8601));
		assertEquals(status, ms.getStatus());
		assertEquals("undeliverable", status.toString());
		assertEquals(channel, ms.getChannel());
		assertEquals("mms", channel.toString());
	}
}
