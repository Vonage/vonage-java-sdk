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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class WhatsappMultiProductRequestTest {

	@Test
	public void testSerializeAllParams() throws Exception {
		String from = "317900000001", to = "447900000002",
				catalogId = UUID.randomUUID().toString(),
				pid1 = UUID.randomUUID().toString(),
				pid2 = UUID.randomUUID().toString(),
				pid3 = UUID.randomUUID().toString(),
				title1 = "Cool products",
				title2 = "Awesome products",
				headerText = "Our top products",
				bodyText = "Check out these great products",
				footerText = "Sale now on!",
				expectedCustomJson = "{\n" +
				"    \"type\": \"interactive\",\n" +
				"    \"interactive\": {\n" +
				"      \"type\": \"product_list\",\n" +
				"      \"header\":{\n" +
				"        \"type\": \"text\",\n" +
				"        \"text\": \""+headerText+"\"\n" +
				"      },\n" +
				"      \"body\": {\n" +
				"        \"text\": \""+bodyText+"\"\n" +
				"      },\n" +
				"      \"footer\": {\n" +
				"        \"text\": \""+footerText+"\"\n" +
				"      },\n" +
				"      \"action\":{\n" +
				"        \"catalog_id\": \""+catalogId+"\",\n" +
				"        \"sections\": [\n" +
				"          {\n" +
				"            \"title\": \""+title1+"\",\n" +
				"            \"product_items\": [\n" +
				"              { \"product_retailer_id\": \""+pid1+"\" },\n" +
				"              { \"product_retailer_id\": \""+pid2+"\" }\n" +
				"            ]\n" +
				"          },\n" +
				"          {\n" +
				"            \"title\": \""+title2+"\",\n" +
				"            \"product_items\": [\n" +
				"              { \"product_retailer_id\": \""+pid3+"\" }\n" +
				"            ]\n" +
				"          }\n" +
				"        ]\n" +
				"      }\n" +
				"    }\n" +
				"  }",
				json = WhatsappMultiProductRequest.builder()
						.from(from).to(to).catalogId(catalogId)
						.headerText(headerText).bodyText(bodyText)
						.addProductsSection(title1, pid1, pid2)
						.addProductsSection(title2, pid3)
						.footerText(footerText).build().toJson();

		assertTrue(json.contains("\"from\":\""+from+"\""));
		assertTrue(json.contains("\"to\":\""+to+"\""));
		assertTrue(json.contains("\"message_type\":\"custom\""));
		assertTrue(json.contains("\"channel\":\"whatsapp\""));

		Map<Object, Object> parsedCustom = new ObjectMapper().readValue(
				expectedCustomJson, new TypeReference<LinkedHashMap<Object, Object>>() {}
		);
		String normalExpectedCustomJson = new ObjectMapper().writeValueAsString(parsedCustom);
		assertTrue(json.contains("\"custom\":"+normalExpectedCustomJson));
	}

	@Test
	public void testSerializeRequiredParams() {
		String actualJson = WhatsappMultiProductRequest.builder()
				.from("317900000001").to("447900000002")
				.catalogId("ca7_1d").bodyText("Body").headerText("Header")
				.addProductsSection("Section1", "sku1")
				.build().toJson();

		String expectedJson = "{\"message_type\":\"custom\",\"channel\":\"whatsapp\"," +
				"\"from\":\"317900000001\",\"to\":\"447900000002\",\"custom\":{" +
				"\"type\":\"interactive\",\"interactive\":{\"type\":\"product_list\"," +
				"\"header\":{\"type\":\"text\",\"text\":\"Header\"}," +
				"\"body\":{\"text\":\"Body\"},\"action\":{" +
				"\"catalog_id\":\"ca7_1d\",\"sections\":[{\"title\":\"Section1\"," +
				"\"product_items\":[{\"product_retailer_id\":\"sku1\"}]}]}}}}";

		assertEquals(expectedJson, actualJson);
	}

	@Test
	public void testConstructMissingParams() {
		WhatsappMultiProductRequest.Builder builder = WhatsappMultiProductRequest.builder()
				.from("317900000001").to("447900000002")
				.catalogId("ca7_1d").bodyText("Body").headerText("Header")
				.addProductsSection("Section1", "sku1");
		assertEquals(2, builder.build().getCustom().size());

		String temp = builder.catalogId;
		builder.catalogId = null;
		assertThrows(NullPointerException.class, builder::build);
		builder.catalogId = temp;
		temp = builder.bodyText;
		builder.bodyText = null;
		assertThrows(NullPointerException.class, builder::build);
		builder.bodyText = temp;
		temp = builder.headerText;
		builder.headerText = null;
		assertThrows(NullPointerException.class, builder::build);
		builder.headerText = temp;
		ProductSection tempSection = builder.sections.get(0);
		builder.sections.clear();
		assertThrows(IllegalStateException.class, builder::build);
		assertThrows(IllegalArgumentException.class, () ->
				new ProductSection(tempSection.title, Collections.emptyList())
		);
		assertThrows(NullPointerException.class, () -> new ProductSection(null,
				tempSection.getProducts().stream().map(ProductItem::getProductRetailerId).collect(Collectors.toList()))
		);
		assertThrows(IllegalStateException.class, builder::build);
	}
}
