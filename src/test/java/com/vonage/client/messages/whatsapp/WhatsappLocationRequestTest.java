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
package com.vonage.client.messages.whatsapp;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class WhatsappLocationRequestTest {

	@Test
	public void testSerializeAllParams() {
		double longitude = 51.5231022, latitude = -0.0876017;
		String name = "Vonage", address = "15 Bonhill St, London EC2A 4DN", text = "Here it is!";
		String json = WhatsappLocationRequest.builder()
				.name(name).address(address)
				.from("317900000002").to("447900000001")
				.longitude(longitude).latitude(latitude)
				.build().toJson();
		assertTrue(json.contains("\"custom\":{\"type\":\"location\",\"location\":{" +
				"\"latitude\":"+latitude+",\"longitude\":"+longitude+
				",\"name\":\""+name+"\",\"address\":\""+address+"\"}}"
		));
		assertTrue(json.contains("\"message_type\":\"custom\""));
		assertTrue(json.contains("\"channel\":\"whatsapp\""));
	}

	@Test
	public void testSerializeRequiredParams() {
		String json = WhatsappLocationRequest.builder()
				.longitude(51.5216317).latitude(-0.0890882)
				.from("317900000002").to("447900000001")
				.build().toJson();
		assertTrue(json.contains("\"location\":{\"latitude\":-0.0890882,\"longitude\":51.5216317}"));
		assertTrue(json.contains("\"message_type\":\"custom\""));
		assertTrue(json.contains("\"channel\":\"whatsapp\""));
	}


	@Test
	public void testConstructNoLatitude() {
		assertThrows(IllegalStateException.class, () -> WhatsappLocationRequest.builder()
				.from("317900000002").to("447900000001").longitude(Math.random()).build()
		);
	}

	@Test
	public void testConstructNoLongitude() {
		assertThrows(IllegalStateException.class, () -> WhatsappLocationRequest.builder()
				.from("317900000002").to("447900000001").latitude(Math.random()).build()
		);
	}
}
