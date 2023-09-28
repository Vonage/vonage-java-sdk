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
package com.vonage.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.Test;
import java.net.URI;
import java.util.Collections;
import java.util.List;

public class VonageApiResponseExceptionTest {

	static class ConcreteVonageApiResponseException extends VonageApiResponseException {
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	@JsonInclude(JsonInclude.Include.NON_NULL)
	static class VARXWithoutNoArgs extends VonageApiResponseException {
		public VARXWithoutNoArgs(boolean dummy) {
		}
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	@JsonInclude(JsonInclude.Include.NON_NULL)
	static class VARXWithField extends VonageApiResponseException {
		@JsonProperty boolean dummy;
	}

	@Test
	public void testConstructFromValidJson() {
		final int status = 402;
		final String
			type = "https://developer.nexmo.com/api-errors/#low-balance",
			title = "Low balance",
			detail = "This request could not be performed due to your account balance being low.",
			instance = "bf0ca0bf927b3b52e3cb03217e1a1ddf";

		ConcreteVonageApiResponseException expected = new ConcreteVonageApiResponseException();
		expected.statusCode = status;
		expected.type = URI.create(type);
		expected.title = title;
		expected.detail = detail;
		expected.instance = instance;

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

	@Test(expected = VonageUnexpectedException.class)
	public void testConstructFromInvalidJson() {
		throw VonageApiResponseException.fromJson(ConcreteVonageApiResponseException.class, "{_malformed_}");
	}

	@Test(expected = VonageUnexpectedException.class)
	public void testBadConstructor() {
		VonageApiResponseException.fromJson(VARXWithoutNoArgs.class, "{}");
	}

	@Test
	public void testToJsonIncludesFields() {
		String json = "{\"dummy\":true}";
		VARXWithField crx = VonageApiResponseException.fromJson(VARXWithField.class, json);
		assertNotNull(crx);
		assertTrue(crx.dummy);
		assertEquals(json, crx.toJson());
	}

	@Test(expected = VonageUnexpectedException.class)
	public void triggerJsonProcessingException() throws Exception {
		class SelfReferencing extends VonageApiResponseException {
			@JsonProperty("self") final SelfReferencing self = this;
		}
		SelfReferencing selfReferencing = new SelfReferencing();
		selfReferencing.toJson();
		try {
			throw selfReferencing;
		}
		catch (Exception ex) {
			fail();
		}
	}

	@Test(expected = VonageUnexpectedException.class)
	public void testFromJsonWithBadConstructorDefinition() throws Exception {
		class MissingNoArgs extends VonageApiResponseException {
			public MissingNoArgs(boolean dummy) {}
		}
		VonageApiResponseException.fromJson(MissingNoArgs.class, "");
	}
}
