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
package com.vonage.client.proactiveconnect;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.VonageResponseParseException;
import com.vonage.client.VonageUnexpectedException;
import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ContactsListTest {

	@Test
	public void testSerializeAndParseAllParameters() {
		String name = "My name";
		String description = "My description";
		List<String> tags = Arrays.asList("t1", "Tag 2", "TAG_3");
		ListAttribute attribute1 = ListAttribute.builder()
				.name("Attribute A").key(false).alias("a1").build(),
			attribute2 = ListAttribute.builder()
					.name("attribute B").key(true).build(),
			attribute3 = ListAttribute.builder()
					.name("Attrib 3").build();

		SalesforceDatasource datasource = new SalesforceDatasource(
				"salesforce_credentials",
				"select Id, LastName, FirstName, Phone, Email FROM Contact"
		);

		ContactsList request = ContactsList.builder(name)
				.description(description).tags(tags)
				.attributes(attribute1, attribute2, attribute3)
				.datasource(datasource).build();
		
		String json = request.toJson();
	
		ContactsList response = ContactsList.fromJson(json);
		assertEquals(request.getName(), response.getName());
		assertEquals(request.getDescription(), response.getDescription());
		assertEquals(request.getTags(), response.getTags());
		Datasource responseDatasource = response.getDatasource();
		assertEquals("salesforce", responseDatasource.getType());
		assertEquals(SalesforceDatasource.class, responseDatasource.getClass());
		assertEquals(datasource.getSoql(), ((SalesforceDatasource) responseDatasource).getSoql());
		assertEquals(datasource.getIntegrationId(), ((SalesforceDatasource) responseDatasource).getIntegrationId());
		List<ListAttribute> responseAttributes = response.getAttributes();
		assertEquals(3, responseAttributes.size());
		ListAttribute responseAttribute1 = responseAttributes.get(0);
		assertEquals(attribute1.getName(), responseAttribute1.getName());
		assertEquals(attribute1.getKey(), responseAttribute1.getKey());
		assertEquals(attribute1.getAlias(), responseAttribute1.getAlias());
		ListAttribute responseAttribute2 = responseAttributes.get(1);
		assertEquals(attribute2.getName(), responseAttribute2.getName());
		assertEquals(attribute2.getKey(), responseAttribute2.getKey());
		assertEquals(attribute2.getAlias(), responseAttribute2.getAlias());
		ListAttribute responseAttribute3 = responseAttributes.get(2);
		assertEquals(attribute3.getName(), responseAttribute3.getName());
		assertEquals(attribute3.getKey(), responseAttribute3.getKey());
		assertEquals(attribute3.getAlias(), responseAttribute3.getAlias());
	}
	
	@Test
	public void testFromJsonAllFields() {
		String name = "Test Name";
		String description = "Test Desc.";
		String createdAt = "2022-06-19T17:59:28.085Z";
		String updatedAt = "2023-07-19T17:51:30.187Z";
		Integer itemsCount = 500;
		UUID id = UUID.randomUUID();
	
		ContactsList response = ContactsList.fromJson("{\n" +
				"\"name\":\""+name+"\",\n" +
				"\"description\":\""+description+"\",\n" +
				"\"tags\":[],\n" +
				"\"attributes\":[],\n" +
				"\"datasource\":{\"type\":\"manual\"},\n" +
				"\"created_at\":\""+createdAt+"\",\n" +
				"\"updated_at\":\""+updatedAt+"\",\n" +
				"\"items_count\":\""+itemsCount+"\",\n" +
				"\"id\":\""+id+"\",\n" +
				"\"sync_status\": {\n" +
				"      \"value\": \"configured\",\n" +
				"      \"details\": \"Not found\",\n" +
				"      \"metadata_modified\": false,\n" +
				"      \"data_modified\": true,\n" +
				"      \"dirty\": true\n" +
				"}\n" +
		"}");
		
		assertEquals(name, response.getName());
		assertEquals(description, response.getDescription());
		assertEquals(0, response.getTags().size());
		assertEquals(0, response.getAttributes().size());
		assertEquals(ManualDatasource.class, response.getDatasource().getClass());
		assertEquals(createdAt, response.getCreatedAt().toString());
		assertEquals(updatedAt, response.getUpdatedAt().toString());
		assertEquals(itemsCount, response.getItemsCount());
		assertEquals(id, response.getId());
		SyncStatus syncStatus = response.getSyncStatus();
		assertNotNull(syncStatus);
		assertEquals(SyncStatusValue.CONFIGURED, syncStatus.getValue());
		assertEquals("Not found", syncStatus.getDetails());
		assertFalse(syncStatus.getMetadataModified());
		assertTrue(syncStatus.getDataModified());
		assertTrue(syncStatus.getDirty());
	}
	
	@Test(expected = VonageUnexpectedException.class)
	public void testFromJsonInvalid() {
		ContactsList.fromJson("{malformed]");
	}

	@Test
	public void testFromJsonEmpty() {
		ContactsList response = ContactsList.fromJson("{}");
		assertNull(response.getName());
		assertNull(response.getDescription());
		assertNull(response.getTags());
		assertNull(response.getAttributes());
		assertNull(response.getDatasource());
		assertNull(response.getCreatedAt());
		assertNull(response.getUpdatedAt());
		assertNull(response.getItemsCount());
		assertNull(response.getId());
		assertNull(response.getSyncStatus());
	}

	@Test(expected = VonageUnexpectedException.class)
	public void triggerJsonProcessingException() {
		class SelfRefrencing extends ContactsList {
			@JsonProperty("self") SelfRefrencing self = this;
		}
		new SelfRefrencing().toJson();
	}

	@Test(expected = VonageResponseParseException.class)
	public void testMalformedUpdateFromJson() {
		new ContactsList().updateFromJson("{malformed]");
	}

	@Test
	public void testConstructRequiredParameters() {
		ContactsList minimal = ContactsList.builder("T").build();
		assertEquals("{\"name\":\"T\"}", minimal.toJson());
		assertThrows(IllegalArgumentException.class, () -> ContactsList.builder(null).build());
		assertThrows(IllegalArgumentException.class, () -> ContactsList.builder("").build());
		assertThrows(IllegalArgumentException.class, () -> ContactsList.builder("  ").build());
	}

	@Test
	public void testTagsValidation() {
		String name = "Test";
		assertNull(ContactsList.builder(name).build().getTags());
		assertTrue(ContactsList.builder(name).tags().build().getTags().isEmpty());
		assertEquals(1, ContactsList.builder(name).tags("T1").build().getTags().size());
		List<String> ten = IntStream.rangeClosed(1, 10).mapToObj(i -> "Tag "+i).collect(Collectors.toList());
		assertEquals(10, ContactsList.builder(name).tags(ten).build().getTags().size());
		List<String> eleven = new ArrayList<>(11);
		eleven.addAll(ten);
		eleven.add("Tag 11");
		assertThrows(IllegalStateException.class, () -> ContactsList.builder(name).tags(eleven).build());
		int random = new Random().nextInt(ten.size()-1);
		ten.set(random, null);
		assertThrows(IllegalArgumentException.class, () -> ContactsList.builder(name).tags(ten).build());
		ten.set(random, "");
		assertThrows(IllegalArgumentException.class, () -> ContactsList.builder(name).tags(ten).build());
		ten.set(random, "  ");
		assertThrows(IllegalArgumentException.class, () -> ContactsList.builder(name).tags(ten).build());
		ten.set(random, "ABCDEFGHIJKLMNO");
		assertEquals(15, ContactsList.builder(name).tags(ten).build().getTags().get(random).length());
		ten.set(random, ten.get(random) + "P");
		assertThrows(IllegalArgumentException.class, () -> ContactsList.builder(name).tags(ten).build());
	}
}