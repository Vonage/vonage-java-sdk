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

import com.vonage.client.VonageUnexpectedException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class MessageResponseExceptionTest {

	@Test
	public void testConstructFromValidJson() {
		final int status = 402;
		final String
			type = "https://developer.nexmo.com/api-errors/#low-balance",
			title = "Low balance",
			detail = "This request could not be performed due to your account balance being low.",
			instance = "bf0ca0bf927b3b52e3cb03217e1a1ddf";

		MessageResponseException expected = new MessageResponseException();
		expected.statusCode = status;
		expected.type = type;
		expected.title = title;
		expected.detail = detail;
		expected.instance = instance;

		MessageResponseException actual = MessageResponseException.fromJson(
			"{\n" +
			"  \"type\": \""+type+"\",\n" +
			"  \"title\": \""+title+"\",\n" +
			"  \"detail\": \""+detail+"\",\n" +
			"  \"instance\": \""+instance+"\"\n" +
			"}"
		);
		actual.statusCode = status;
		assertEquals(expected, actual);
		assertEquals(expected.hashCode(), actual.hashCode());
		assertEquals(expected.toString(), actual.toString());
	}

	@Test
	public void testConstructEmpty() {
		assertEquals(new MessageResponseException(), MessageResponseException.fromJson("{}"));
	}

	@Test(expected = VonageUnexpectedException.class)
	public void testConstructFromInvalidJson() {
		throw MessageResponseException.fromJson("{_malformed_}");
	}
}
