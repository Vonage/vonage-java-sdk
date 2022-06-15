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

import static org.junit.Assert.assertEquals;

public class MessageStatusTest {

	@Test
	public void testConstructAllFields() {
		String json = "{\n" +
				"  \"message_uuid\": \"aaaaaaaa-bbbb-cccc-dddd-0123456789ab\",\n" +
				"  \"to\": \"447700900000\",\n" +
				"  \"from\": \"447700900001\",\n" +
				"  \"timestamp\": \"2020-01-01 14:00:00 +0000\",\n" +
				"  \"status\": \"submitted\",\n" +
				"  \"channel\": \"sms\",\n" +
				"  \"error\": {\n" +
				"    \"type\": \"https://developer.nexmo.com/api-errors/messages-olympus#1000\",\n" +
				"    \"title\": 1000,\n" +
				"    \"detail\": \"Throttled - You have exceeded the submission capacity allowed on this account. Please wait and retry\",\n" +
				"    \"instance\": \"bf0ca0bf927b3b52e3cb03217e1a1ddf\"\n" +
				"  },\n" +
				"  \"usage\": {\n" +
				"    \"currency\": \"EUR\",\n" +
				"    \"price\": \"0.0333\"\n" +
				"  }\n" +
				"}";

		MessageStatus status = MessageStatus.fromJson(json);
		String generatedJson = status.toJson();
		assertEquals(generatedJson, MessageStatus.fromJson(generatedJson).toJson());
	}
}
