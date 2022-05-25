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
import static org.junit.Assert.assertTrue;

public class SmsRequestTest {

	@Test
	public void testSerializeValid() {
		String from = "447900000001", to = "317900000002", msg = "Hello, World!";
		SmsRequest sms = new SmsRequest(from, to, msg);
		String json = sms.toJson();
		assertTrue(json.contains("\"text\":\""+msg+"\""));
		assertTrue(json.contains("\"from\":\""+from+"\""));
		assertTrue(json.contains("\"to\":\""+to+"\""));
		assertTrue(json.contains("\"message_type\":\"text\""));
		assertTrue(json.contains("\"channel\":\"sms\""));
	}

	@Test(expected = NullPointerException.class)
	public void testNullText() {
		new SmsRequest("447900000001", "317900000002", null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEmptyText() {
		new SmsRequest("447900000001", "317900000002", "");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testLongText() {
		StringBuilder text = new StringBuilder(1002);
		for (int i = 0; i < 999; i++) {
			text.append('*');
		}
		assertEquals(999, text.length());
		SmsRequest sms = new SmsRequest("447900000001", "317900000002", text.toString());
		assertEquals(text.toString(), sms.getText());
		text.append("tt");
		assertEquals(1001, text.length());
		new SmsRequest(sms.getFrom(), sms.getTo(), text.toString());
	}
}
