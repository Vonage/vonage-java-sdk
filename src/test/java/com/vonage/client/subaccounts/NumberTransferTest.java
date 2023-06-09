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
package com.vonage.client.subaccounts;

import com.vonage.client.VonageUnexpectedException;
import static org.junit.Assert.*;
import org.junit.Test;

public class NumberTransferTest {

	@Test
	public void testConstructRequiredParameters() {
		NumberTransfer.Builder builder = NumberTransfer.builder();
		assertThrows(NullPointerException.class, builder::build);
		builder.from("ad6dc56f").to("ad6dc56f");
		assertThrows(NullPointerException.class, builder::build);
		builder.country("abc").number("         ");
		assertThrows(IllegalArgumentException.class, builder::build);
		builder.number("23507703696");
		assertThrows(IllegalArgumentException.class, builder::build);
	}

	@Test
	public void testSerializeAndParseAllParameters() {
		String from = "7c9738e6";
		String to = "ad6dc56f";
		String country = "GB";
		String number = "23507703696";
		NumberTransfer request = NumberTransfer.builder()
				.from(from).to(to).country(country).number(number).build();
		
		String json = request.toJson();
		assertTrue(json.contains("\"from\":\""+from+"\""));
		assertTrue(json.contains("\"to\":\""+to+"\""));
		assertTrue(json.contains("\"country\":\""+country+"\""));
		assertTrue(json.contains("\"number\":\""+number+"\""));
	
		NumberTransfer response = NumberTransfer.fromJson(json);
		assertEquals(request.getFrom(), response.getFrom());
		assertEquals(request.getTo(), response.getTo());
		assertEquals(request.getCountry(), response.getCountry());
		assertEquals(request.getNumber(), response.getNumber());
	}
	
	@Test
	public void testFromJsonAllFields() {
		String from = "7c9738e6";
		String to = "ad6dc56f";
		String country = "GB";
		String number = "23507703696";
	
		NumberTransfer response = NumberTransfer.fromJson("{\n" +
				"\"from\":\""+from+"\",\n" +
				"\"to\":\""+to+"\",\n" +
				"\"country\":\""+country+"\",\n" +
				"\"number\":\""+number+"\"\n" +
		"}");
		
		assertEquals(from, response.getFrom());
		assertEquals(to, response.getTo());
		assertEquals(country, response.getCountry());
		assertEquals(number, response.getNumber());
	}
	
	@Test(expected = VonageUnexpectedException.class)
	public void testFromJsonInvalid() {
		NumberTransfer.fromJson("{malformed]");
	}

	@Test
	public void testFromJsonEmpty() {
		NumberTransfer response = NumberTransfer.fromJson("{}");
		assertNull(response.getFrom());
		assertNull(response.getTo());
		assertNull(response.getCountry());
		assertNull(response.getNumber());
	}
}