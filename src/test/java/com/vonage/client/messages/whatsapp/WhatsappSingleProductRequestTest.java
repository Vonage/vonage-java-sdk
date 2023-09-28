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
package com.vonage.client.messages.whatsapp;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import java.util.UUID;

public class WhatsappSingleProductRequestTest {

	@Test
	public void testSerializeAllParams() {
		String bodyText = "Check out this cool product", footerText = "Sale now on!",
				from = "317900000002", to = "447900000001",
				productId = UUID.randomUUID().toString(), catalogId = UUID.randomUUID().toString(),
				json = WhatsappSingleProductRequest.builder()
						.from(from).to(to)
						.catalogId(catalogId).productRetailerId(productId)
						.bodyText(bodyText).footerText(footerText)
						.build().toJson();

		assertTrue(json.contains("\"from\":\""+from+"\""));
		assertTrue(json.contains("\"to\":\""+to+"\""));
		assertTrue(json.contains("\"message_type\":\"custom\""));
		assertTrue(json.contains("\"channel\":\"whatsapp\""));
		assertTrue(json.contains("\"custom\":{\"type\":\"interactive\",\"interactive\":{" +
				"\"type\":\"product\",\"body\":{\"text\":\""+bodyText+"\"},\"footer\":{" +
				"\"text\":\""+footerText+"\"},\"action\":{" +
				"\"catalog_id\":\""+catalogId+"\",\"product_retailer_id\":\""+productId+"\"}}}"
		));
	}

	@Test
	public void testSerializeRequiredParams() {
		String from = "317900000052", to = "447900000011",
				productId = UUID.randomUUID().toString(), catalogId = UUID.randomUUID().toString(),
				json = WhatsappSingleProductRequest.builder()
						.from(from).to(to).catalogId(catalogId).productRetailerId(productId)
						.build().toJson();

		assertTrue(json.contains("\"from\":\""+from+"\""));
		assertTrue(json.contains("\"to\":\""+to+"\""));
		assertTrue(json.contains("\"message_type\":\"custom\""));
		assertTrue(json.contains("\"channel\":\"whatsapp\""));
		assertTrue(json.contains("\"custom\":{\"type\":\"interactive\",\"interactive\":{\"type\":\"product\"," +
				"\"action\":{\"catalog_id\":\""+catalogId+"\",\"product_retailer_id\":\""+productId+"\"}}}"
		));
	}

	@Test
	public void testConstructNoId() {
		String id = UUID.randomUUID().toString();
		WhatsappSingleProductRequest.Builder builder = WhatsappSingleProductRequest.builder()
				.from("317900000002").to("447900000001");
		assertNotNull(builder.productRetailerId(id).catalogId(id).build());
		builder.catalogId = null;
		assertThrows(NullPointerException.class, builder::build);
		builder.catalogId = id;
		builder.productRetailerId = null;
		assertThrows(NullPointerException.class, builder::build);
	}
}
