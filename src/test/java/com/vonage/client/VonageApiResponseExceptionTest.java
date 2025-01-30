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
package com.vonage.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.account.AccountResponseException;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import java.net.URI;
import java.util.Collections;
import java.util.List;

public class VonageApiResponseExceptionTest {

	static class ConcreteVonageApiResponseException extends VonageApiResponseException {}

	static class VARXWithoutNoArgs extends VonageApiResponseException {
		public VARXWithoutNoArgs(String message) {
			super(message);
		}

		public VARXWithoutNoArgs(String message, Throwable cause) {
			super(message, cause);
		}

		public VARXWithoutNoArgs(Throwable cause) {
			super(cause);
		}
	}

	static class VARXWithField extends VonageApiResponseException {
		@JsonProperty boolean dummy;
	}

	final int status = 402;
	final String
			type = "https://developer.nexmo.com/api-errors/#low-balance",
			title = "Low balance",
			detail = "This request could not be performed due to your account balance being low.",
			instance = "bf0ca0bf927b3b52e3cb03217e1a1ddf";
	final ConcreteVonageApiResponseException expected = new ConcreteVonageApiResponseException();

	VonageApiResponseExceptionTest() {
		expected.statusCode = status;
		expected.type = URI.create(type);
		expected.title = title;
		expected.detail = detail;
		expected.instance = instance;
	}

	@Test
	public void testConstructFromValidJson() {
		ConcreteVonageApiResponseException actual = VonageApiResponseException.fromJson(
				ConcreteVonageApiResponseException.class, "{\n" +
			"  \"type\": \""+type+"\",\n" +
			"  \"title\": \""+title+"\",\n" +
			"  \"detail\": \""+detail+"\",\n" +
			"  \"instance\": \""+instance+"\",\n" +
			"  \"errors\": [\"Bad name\", {\"errorKey\": \"errorValue\"}]\n" +
			"}"
		);
		actual.statusCode = status;
		assertEquals(expected, actual);
		assertEquals(expected.hashCode(), actual.hashCode());
		assertEquals(expected.toString(), actual.toString());
		List<?> errors = actual.getErrors();
		assertNotNull(errors);
		assertEquals(2, errors.size());
		assertEquals("Bad name", errors.get(0));
		assertEquals(Collections.singletonMap("errorKey", "errorValue"), errors.get(1));
	}

	@Test
	public void testToString() {
		ConcreteVonageApiResponseException crx = new ConcreteVonageApiResponseException();
		crx.statusCode = 500;
		assertEquals(crx.getLocalizedMessage(), crx.getMessage());
		assertEquals(crx.getClass().getName(), crx.toString());

		crx.title = "Internal error";
		assertEquals("500 (Internal error)", crx.getMessage());

		crx.detail = "There was an error processing your request in the Platform.";
		assertEquals(
		"500 (Internal error): There was an error processing your request in the Platform.",
				crx.getMessage()
		);
	}

	@Test
	public void testConstructEmpty() {
		assertEquals(
				new ConcreteVonageApiResponseException(),
				VonageApiResponseException.fromJson(ConcreteVonageApiResponseException.class, "{}")
		);
	}

	@Test
	public void testConstructFromInvalidJson() {
		assertThrows(VonageUnexpectedException.class, () ->
				VonageApiResponseException.fromJson(ConcreteVonageApiResponseException.class, "{_malformed_}")
		);
	}

	@Test
	public void testBadConstructor() {
		assertThrows(VonageUnexpectedException.class, () ->
				VonageApiResponseException.fromJson(VARXWithoutNoArgs.class, "{}")
		);
	}

	@Test
	public void testToJsonIncludesFields() {
		String json = "{\"dummy\":true}";
		VARXWithField crx = VonageApiResponseException.fromJson(VARXWithField.class, json);
		assertNotNull(crx);
		assertTrue(crx.dummy);
		assertEquals(json, crx.toJson());
	}

	@Test
	public void triggerJsonProcessingException() throws Exception {
		class SelfReferencing extends VonageApiResponseException {
			@JsonProperty("self") final SelfReferencing self = this;
		}
		SelfReferencing sr = new SelfReferencing();
		assertThrows(VonageUnexpectedException.class, sr::toJson);
	}

	@Test
	public void testFromJsonWithBadConstructorDefinition() throws Exception {
		class MissingNoArgs extends VonageApiResponseException {
			public MissingNoArgs(boolean dummy) {}
		}
		assertThrows(VonageUnexpectedException.class, () ->
				VonageApiResponseException.fromJson(MissingNoArgs.class, "")
		);
	}

	@Test
	public void testMessageAndCause() {
		Throwable cause = new Throwable("Cause");
		VARXWithoutNoArgs varx = new VARXWithoutNoArgs("Message", cause);
		assertEquals("Message", varx.getMessage());
		assertEquals(cause, varx.getCause());
	}

	@Test
	public void testCauseOnly() {
		Throwable cause = new RuntimeException();
		VARXWithoutNoArgs varx = new VARXWithoutNoArgs(cause);
		assertEquals(cause, varx.getCause());
	}

	@Test
	public void testMessageOnly() {
		var message = "Oops! Something went wrong :(";
		VARXWithoutNoArgs varx = new VARXWithoutNoArgs(message);
		assertEquals(message, varx.getMessage());
	}

	@Test
	public void testSetErrorCode() {
		var ex = VonageApiResponseException.fromJson(
				ConcreteVonageApiResponseException.class,
				"{\"error-code\":null}"
		);
		assertNotNull(ex);
		ex = VonageApiResponseException.fromJson(
				ConcreteVonageApiResponseException.class,
				"{\"error-code\":\"\"}"
		);
		assertNotNull(ex);
	}

	@Test
	public void testEmptyJson() {
		var ex = VonageApiResponseException.fromJson(
				ConcreteVonageApiResponseException.class,
				"{}"
		);
		assertNotNull(ex);
	}

	@Test
	public void testNullJson() {
		assertThrows(VonageUnexpectedException.class, () ->
				VonageApiResponseException.fromJson(ConcreteVonageApiResponseException.class, null)
		);
	}

	@Test
	public void testEquals() {
		var statusOnlyJson = "{\"error-code\":"+status+"}";
		var ex1 = VonageApiResponseException.fromJson(
				ConcreteVonageApiResponseException.class,
				statusOnlyJson
		);
		var ex2 = VonageApiResponseException.fromJson(
				ConcreteVonageApiResponseException.class,
				statusOnlyJson
		);
		assertEquals(ex1, ex2);
		assertEquals(ex1.hashCode(), ex2.hashCode());
		ex2 = ex1;
		assertEquals(ex1, ex2);
		ex2 = null;
		boolean equals = ex1.equals(ex2);
		assertFalse(equals);
		equals = ex1.equals(new Object());
		assertFalse(equals);

		ex2 = VonageApiResponseException.fromJson(
				ConcreteVonageApiResponseException.class,
				expected.toJson()
		);
		assertNotEquals(expected, ex2);
		ex2.statusCode = status;
		assertEquals(expected, ex2);
		assertNotEquals(ex2, ex1);
		ex2.type = null;
		assertNotEquals(expected, ex2);
		ex2.type = expected.type;
		ex2.title = null;
		assertNotEquals(expected, ex2);
		ex2.title = expected.title;
		ex2.detail = null;
		assertNotEquals(expected, ex2);
		ex2.detail = expected.detail;
		ex2.instance = null;
		assertNotEquals(expected, ex2);
		ex2.instance = expected.instance;
		assertEquals(expected, ex2);
	}
}
