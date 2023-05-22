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

import com.vonage.client.ClientTest;
import com.vonage.client.VonageClientException;
import com.vonage.client.common.HalLinks;
import com.vonage.client.common.HalPageResponse;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.*;

public class ProactiveConnectClientTest extends ClientTest<ProactiveConnectClient> {

	static Path SAMPLE_CSV_PATH;
	static final Instant
		SAMPLE_CREATED_AT = Instant.parse("2022-06-19T17:59:28.085Z"),
		SAMPLE_UPDATED_AT = Instant.now();
	static final String
		SAMPLE_LIST_ID = "29192c4a-4058-49da-86c2-3e349d1065b7",
		SAMPLE_ITEM_ID = "4cb98f71-a879-49f7-b5cf-2314353eb52c",
		BASE_URL = "https://api-eu.vonage.com/v0.1/bulk",
		HAL_TEMPLATE_RESPONSE = "{\n" +
			"   \"page_size\": 10,\n" +
			"   \"page\": 3,\n" +
			"   \"total_pages\": 24,\n" +
			"   \"total_items\": 99,\n" +
			"   \"_links\": {\n" +
			"      \"first\": {\n" +
			"         \"href\": \""+BASE_URL+"/lists?page=1&page_size=10\"\n" +
			"      },\n" +
			"      \"self\": {\n" +
			"         \"href\": \""+BASE_URL+"/lists?page=3&page_size=10\"\n" +
			"      },\n" +
			"      \"prev\": {\n" +
			"         \"href\": \""+BASE_URL+"/lists?page=2&page_size=10\"\n" +
			"      },\n" +
			"      \"next\": {\n" +
			"         \"href\": \""+BASE_URL+"/lists?page=4&page_size=10\"\n" +
			"      }\n" +
			"   },\n" +
			"   \"_embedded\": {\n  ",
		SAMPLE_LIST_RESPONSE = "{\n" +
			"   \"name\": \"my list\",\n" +
			"   \"description\": \"my description\",\n" +
			"   \"tags\": [\n" +
			"      \"vip\",\n" +
			"      \"sport\"\n" +
			"   ],\n" +
			"   \"attributes\": [{},\n" +
			"      {\n" +
			"         \"name\": \"phone_number\",\n" +
			"         \"alias\": \"phone\",\n" +
			"         \"key\": false\n" +
			"      }\n" +
			"   ],\n" +
			"   \"datasource\": {\n" +
			"      \"type\": \"manual\"\n" +
			"   },\n" +
			"   \"id\": \""+SAMPLE_LIST_ID+"\",\n" +
			"   \"created_at\": \""+SAMPLE_CREATED_AT+"\",\n" +
			"   \"updated_at\": \""+SAMPLE_UPDATED_AT+"\",\n" +
			"   \"sync_status\": {\n" +
			"      \"value\": \"paused\",\n" +
			"      \"details\": \"Foo bar baz\",\n" +
			"      \"metadata_modified\": true,\n" +
			"      \"data_modified\": false,\n" +
			"      \"dirty\": false\n" +
			"   },\n" +
			"   \"items_count\": 500\n" +
			"}",
		SAMPLE_LIST_ITEM_RESPONSE = "{\n" +
			"   \"id\": \""+SAMPLE_ITEM_ID+"\",\n" +
			"   \"created_at\": \""+SAMPLE_CREATED_AT+"\",\n" +
			"   \"updated_at\": \""+SAMPLE_UPDATED_AT+"\",\n" +
			"   \"data\": {\n" +
			"      \"firstName\": \"Adrianna\",\n" +
			"      \"LastName\": \"Campbell\",\n" +
			"      \"phone\": 15550067383\n" +
			"   },\n" +
			"   \"list_id\": \""+SAMPLE_LIST_ID+"\"\n" +
			"}",
		SAMPLE_EVENT_RESPONSE = "{\n" +
				"   \"id\": \"92cc64e5-f62b-464f-ab9c-4376bd05adfc\",\n" +
				"   \"type\": \"action-call-succeeded\",\n" +
				"   \"src_ctx\": \"uk-customers\",\n" +
				"   \"occurred_at\": \""+SAMPLE_CREATED_AT+"\",\n" +
				"   \"data\": {\n" +
				"      \"city\": \"North Assunta\",\n" +
				"      \"country\": \"Iceland\",\n" +
				"      \"state\": \"PH\"\n" +
				"   },\n" +
				"   \"job_id\": \"e721113d-0948-4edb-b4af-e3f0578d708c\",\n" +
				"   \"run_id\": \"51aca838-2cf6-4100-b0d2-e74ac0e95c88\",\n" +
				"   \"recipient_id\": 15551584817,\n" +
				"   \"run_item_id\": \"51aca838-2cf6-4100-b0d2-e74ac0e95c88\",\n" +
				"   \"action_id\": \"99ea10e0-1f14-4f55-b976-3c88ea8ec4cd\",\n" +
				"   \"invocation_id\": \"a09aea78-69a4-4b5b-8e53-0d6b1b02c235\"\n" +
				"}";

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		SAMPLE_CSV_PATH = Files.createTempFile("ProactiveConnectClientTest_list", "csv");
	}

	@AfterClass
	public static void setUpAfterClass() throws Exception {
		Files.deleteIfExists(SAMPLE_CSV_PATH);
	}

	public ProactiveConnectClientTest() {
		client = new ProactiveConnectClient(wrapper);
	}

	static void assertEqualsSampleHal(HalPageResponse response) {
		assertNotNull(response);
		assertEquals(10, response.getPageSize().intValue());
		assertEquals(3, response.getPage().intValue());
		assertEquals(24, response.getTotalPages().intValue());
		assertEquals(99, response.getTotalItems().intValue());
		HalLinks links = response.getLinks();
		assertNotNull(links);
		String linkTemplate = BASE_URL + "/lists?page=%s&page_size=10";
		assertEquals(String.format(linkTemplate, "1"), links.getFirstUrl().toString());
		assertEquals(String.format(linkTemplate, "2"), links.getPrevUrl().toString());
		assertEquals(String.format(linkTemplate, "3"), links.getSelfUrl().toString());
		assertEquals(String.format(linkTemplate, "4"), links.getNextUrl().toString());
	}

	static void assertNullList(ContactsList response) {
		assertNotNull(response);
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

	static void assertEqualsSampleList(ContactsList parsed) {
		assertNotNull(parsed);
		assertEquals("my list", parsed.getName());
		assertEquals("my description", parsed.getDescription());
		assertEquals(Arrays.asList("vip", "sport"), parsed.getTags());
		List<ListAttribute> attributes = parsed.getAttributes();
		assertNotNull(attributes);
		assertEquals(2, attributes.size());
		ListAttribute attribute0 = attributes.get(0);
		assertNotNull(attribute0);
		assertNull(attribute0.getName());
		assertNull(attribute0.getAlias());
		assertNull(attribute0.getKey());
		ListAttribute attribute1 = attributes.get(1);
		assertEquals("phone_number", attribute1.getName());
		assertEquals("phone", attribute1.getAlias());
		assertFalse(attribute1.getKey());
		assertEquals(ManualDatasource.class, parsed.getDatasource().getClass());
		assertEquals(SAMPLE_CREATED_AT, parsed.getCreatedAt());
		assertEquals(SAMPLE_UPDATED_AT, parsed.getUpdatedAt());
		SyncStatus syncStatus = parsed.getSyncStatus();
		assertNotNull(syncStatus);
		assertEquals(SyncStatusValue.PAUSED, syncStatus.getValue());
		assertEquals("Foo bar baz", syncStatus.getDetails());
		assertFalse(syncStatus.getDataModified());
		assertTrue(syncStatus.getMetadataModified());
		assertFalse(syncStatus.getDirty());
		assertEquals(UUID.fromString(SAMPLE_LIST_ID), parsed.getId());
		assertEquals(500, parsed.getItemsCount().intValue());
	}

	static void assertEqualsSampleListItem(ListItem parsed) {
		assertNotNull(parsed);
		assertEquals(UUID.fromString(SAMPLE_ITEM_ID), parsed.getId());
		assertEquals(UUID.fromString(SAMPLE_LIST_ID), parsed.getListId());
		assertEquals(SAMPLE_CREATED_AT, parsed.getCreatedAt());
		assertEquals(SAMPLE_UPDATED_AT, parsed.getUpdatedAt());
		Map<String, ?> data = parsed.getData();
		assertNotNull(data);
		assertEquals(3, data.size());
		assertEquals("Adrianna", data.get("firstName"));
		assertEquals("Campbell", data.get("LastName"));
		assertEquals(15550067383L, data.get("phone"));
	}

	static void assertNullListItem(ListItem parsed) {
		assertNotNull(parsed);
		assertNull(parsed.getId());
		assertNull(parsed.getListId());
		assertNull(parsed.getCreatedAt());
		assertNull(parsed.getUpdatedAt());
		assertNull(parsed.getData());
	}

	static void assertEqualsSampleEvent(Event parsed) {
		assertNotNull(parsed);
		assertEquals(UUID.fromString("92cc64e5-f62b-464f-ab9c-4376bd05adfc"), parsed.getId());
		assertEquals(UUID.fromString("e721113d-0948-4edb-b4af-e3f0578d708c"), parsed.getJobId());
		assertEquals(UUID.fromString("51aca838-2cf6-4100-b0d2-e74ac0e95c88"), parsed.getRunId());
		assertEquals(UUID.fromString("51aca838-2cf6-4100-b0d2-e74ac0e95c88"), parsed.getRunItemId());
		assertEquals(UUID.fromString("99ea10e0-1f14-4f55-b976-3c88ea8ec4cd"), parsed.getActionId());
		assertEquals(UUID.fromString("a09aea78-69a4-4b5b-8e53-0d6b1b02c235"), parsed.getInvocationId());
		assertEquals("15551584817", parsed.getRecipientId());
		assertEquals("uk-customers", parsed.getSourceContext());
		assertEquals(SAMPLE_CREATED_AT, parsed.getOccurredAt());
		assertEquals(EventType.ACTION_CALL_SUCCEEDED, parsed.getType());
		Map<String, ?> data = parsed.getData();
		assertNotNull(data);
		assertEquals("North Assunta", data.get("city"));
		assertEquals("Iceland", data.get("country"));
		assertEquals("PH", data.get("state"));
	}

	static void assertNullEvent(Event response) {
		assertNotNull(response);
		assertNull(response.getId());
		assertNull(response.getJobId());
		assertNull(response.getRunId());
		assertNull(response.getRunItemId());
		assertNull(response.getActionId());
		assertNull(response.getInvocationId());
		assertNull(response.getData());
		assertNull(response.getRecipientId());
		assertNull(response.getSourceContext());
		assertNull(response.getOccurredAt());
		assertNull(response.getType());
	}

	@Test
	public void testCreateList() throws Exception {
		ContactsList request = ContactsList.builder("my list").build();
		assertNull(request.getDatasource());
		assertNull(request.getCreatedAt());
		assertNull(request.getUpdatedAt());
		assertNull(request.getTags());
		assertNull(request.getAttributes());
		assertNull(request.getDescription());
		assertNull(request.getId());
		assertNull(request.getSyncStatus());
		assertNull(request.getItemsCount());
		assertNotNull(request.getName());
		stubResponseAndRun(SAMPLE_LIST_RESPONSE, () -> client.createList(request));
		assertEqualsSampleList(request);
		stubResponseAndAssertThrows(200, () -> client.createList(null), NullPointerException.class);
		stubResponseAndAssertThrows(200, () ->
				client.createList(ContactsList.builder(null).build()),
				IllegalArgumentException.class
		);
		stubResponseAndAssertThrows(200, () ->
						client.createList(ContactsList.builder("").build()),
				IllegalArgumentException.class
		);
	}
	
	@Test
	public void testGetList() throws Exception {
		stubResponse(200, SAMPLE_LIST_RESPONSE);
		assertEqualsSampleList(client.getList(SAMPLE_LIST_ID));
		stubResponseAndAssertThrows( () -> client.getList(null), NullPointerException.class);
		stubResponseAndAssertThrows(() -> client.getList("not_a/uuid"), IllegalArgumentException.class);
	}
	
	@Test
	public void testUpdateList() throws Exception {
		ContactsList request = ContactsList.builder("New name")
				.tags("t1").description("New desc")
				.attributes(ListAttribute.builder().name("Attr").build())
				.datasource(new ManualDatasource()).build();

		ContactsList response = stubResponseAndGet(request.toJson(), () ->
				client.updateList(SAMPLE_LIST_ID, request)
		);
		assertEquals(request, response);
		assertEquals(1, response.getTags().size());
		assertEquals("t1", response.getTags().get(0));
		assertEquals("New desc", response.getDescription());
		assertEquals("New name", response.getName());
		assertEquals(1, response.getAttributes().size());
		assertEquals("Attr", response.getAttributes().get(0).getName());
		assertNull(response.getAttributes().get(0).getAlias());
		assertNull(response.getAttributes().get(0).getKey());
		assertEquals("manual", response.getDatasource().getType());

		stubResponseAndAssertThrows(() -> client.updateList(null, request), NullPointerException.class);
		stubResponseAndAssertThrows(() -> client.updateList(SAMPLE_LIST_ID, null), NullPointerException.class);
	}
	
	@Test
	public void testDeleteList() throws Exception {
		assertEqualsSampleList(stubResponseAndGet(SAMPLE_LIST_RESPONSE, () -> client.deleteList(SAMPLE_LIST_ID)));
		stubResponseAndAssertThrows(200, () -> client.deleteList("ID"), IllegalArgumentException.class);
	}
	
	@Test
	public void testClearList() throws Exception {
		stubResponseAndRun(200, () -> client.clearList(SAMPLE_LIST_ID));
		stubResponseAndAssertThrows(200, () -> client.clearList("123"), IllegalArgumentException.class);
	}
	
	@Test
	public void testFetchList() throws Exception {
		stubResponseAndRun(200, () -> client.fetchList(SAMPLE_LIST_ID));
		stubResponseAndAssertThrows(200, () -> client.fetchList("1d"), IllegalArgumentException.class);
	}
	
	@Test
	public void testListLists() throws Exception {
		stubResponse(HAL_TEMPLATE_RESPONSE + "  \"lists\": [{},"+SAMPLE_LIST_RESPONSE+",{}]\n}  \n}");
		ListsResponse parsed = client.listLists(2, 10);
		assertEqualsSampleHal(parsed);
		List<ContactsList> lists = parsed.getLists();
		assertNotNull(lists);
		assertEquals(3, lists.size());
		assertNullList(lists.get(0));
		assertEqualsSampleList(lists.get(1));
		assertNullList(lists.get(2));
		assertNull(stubResponseAndGet("{}", () -> client.listLists(1, 1)).getLists());
		stubResponseAndAssertThrows("{}", () ->
				client.listLists(0), IllegalArgumentException.class
		);
		stubResponseAndAssertThrows("{}", () ->
				client.listLists(1, 0), IllegalArgumentException.class
		);
		assertNull(stubResponseAndGet("{}", client::listLists));
		String emptyListsResponse = "{\"_embedded\":{\"lists\":[]}}";
		assertEquals(0, stubResponseAndGet(emptyListsResponse, client::listLists).size());
	}
	
	@Test
	public void testCreateListItem() throws Exception {
		Map<String, ?> data = Collections.emptyMap();
		stubResponseAndRun(SAMPLE_LIST_ITEM_RESPONSE, () ->
				client.createListItem(SAMPLE_LIST_ID, data)
		);
		stubResponseAndAssertThrows(200, () ->
				client.createListItem("ID", data), IllegalArgumentException.class
		);
		stubResponseAndAssertThrows(200, () ->
				client.createListItem(SAMPLE_LIST_ID, null), NullPointerException.class
		);
	}
	
	@Test
	public void testGetListItem() throws Exception {
		assertEqualsSampleListItem(stubResponseAndGet(
				SAMPLE_LIST_ITEM_RESPONSE, () -> client.getListItem(SAMPLE_LIST_ID, SAMPLE_ITEM_ID)
		));
		stubResponseAndAssertThrows(SAMPLE_LIST_ITEM_RESPONSE, () ->
				client.getListItem("abc", SAMPLE_ITEM_ID), IllegalArgumentException.class
		);
		stubResponseAndAssertThrows(SAMPLE_LIST_ITEM_RESPONSE, () ->
				client.getListItem(SAMPLE_LIST_ID, "abc"), IllegalArgumentException.class
		);
		stubResponseAndAssertThrows(SAMPLE_LIST_ITEM_RESPONSE, () ->
				client.getListItem(null, null), NullPointerException.class
		);
	}
	
	@Test
	public void testUpdateListItem() throws Exception {
		Map<String, ?> data = Collections.singletonMap("Foo", "baR");
		assertEqualsSampleListItem(stubResponseAndGet(SAMPLE_LIST_ITEM_RESPONSE, () ->
				client.updateListItem(SAMPLE_LIST_ID, SAMPLE_ITEM_ID, data)
		));
		stubResponseAndAssertThrows(SAMPLE_LIST_ITEM_RESPONSE, () ->
				client.updateListItem("1", SAMPLE_ITEM_ID, data), IllegalArgumentException.class
		);
		stubResponseAndAssertThrows(SAMPLE_LIST_ITEM_RESPONSE, () ->
				client.updateListItem(SAMPLE_LIST_ID, "1", data), IllegalArgumentException.class
		);
		stubResponseAndAssertThrows(SAMPLE_LIST_ITEM_RESPONSE, () ->
				client.updateListItem(SAMPLE_LIST_ID, SAMPLE_ITEM_ID, null), NullPointerException.class
		);
	}
	
	@Test
	public void testDeleteListItem() throws Exception {
		assertEqualsSampleListItem(stubResponseAndGet(SAMPLE_LIST_ITEM_RESPONSE, () ->
				client.deleteListItem(SAMPLE_LIST_ID, SAMPLE_ITEM_ID)
		));
		stubResponseAndAssertThrows(SAMPLE_LIST_ITEM_RESPONSE, () ->
				client.deleteListItem("abc", SAMPLE_ITEM_ID), IllegalArgumentException.class
		);
		stubResponseAndAssertThrows(SAMPLE_LIST_ITEM_RESPONSE, () ->
				client.deleteListItem(SAMPLE_LIST_ID, "abc"), IllegalArgumentException.class
		);
		stubResponseAndAssertThrows(SAMPLE_LIST_ITEM_RESPONSE, () ->
				client.deleteListItem(null, null), NullPointerException.class
		);
	}
	
	@Test
	public void testDownloadListItems() throws Exception {
		String responseBin = "<BINARY>";
		stubResponse(responseBin);
		byte[] response = client.downloadListItems(SAMPLE_LIST_ID);
		assertNotNull(response);
		assertEquals(responseBin.length(), response.length);
		stubResponseAndAssertThrows(responseBin, () ->
				client.downloadListItems("Id"), IllegalArgumentException.class
		);
		byte[] fileContents = Files.readAllBytes(SAMPLE_CSV_PATH);
		assertEquals(0, fileContents.length);

		stubResponseAndRun(responseBin, () -> client.downloadListItems(SAMPLE_LIST_ID, SAMPLE_CSV_PATH));
		fileContents = Files.readAllBytes(SAMPLE_CSV_PATH);
		assertArrayEquals(response, fileContents);

		stubResponseAndAssertThrows(responseBin, () ->
				client.downloadListItems(SAMPLE_LIST_ID, null), NullPointerException.class
		);
		stubResponseAndAssertThrows(responseBin, () ->
				client.downloadListItems("not-an-id", SAMPLE_CSV_PATH), IllegalArgumentException.class
		);
	}
	
	@Test
	public void testUploadListItems() throws Exception {
		String json = "{\"inserted\": 25,\"updated\": 11,\"deleted\": 6}";
		stubResponse(json);
		UploadListItemsResponse response = client.uploadListItems(SAMPLE_LIST_ID, SAMPLE_CSV_PATH);
		assertNotNull(response);
		assertEquals(25, response.getInserted().intValue());
		assertEquals(11, response.getUpdated().intValue());
		assertEquals(6, response.getDeleted().intValue());
		stubResponseAndAssertThrows(json, () ->
				client.uploadListItems("id", SAMPLE_CSV_PATH), IllegalArgumentException.class
		);
		stubResponseAndAssertThrows(json, () ->
				client.uploadListItems(SAMPLE_LIST_ID, null), NullPointerException.class
		);
		stubResponseAndAssertThrows(json, () ->
				client.uploadListItems(SAMPLE_LIST_ID, Paths.get("dummy")), VonageClientException.class
		);
	}
	
	@Test
	public void testListItems() throws Exception {
		stubResponse(HAL_TEMPLATE_RESPONSE + "  \"items\": [{},"+ SAMPLE_LIST_ITEM_RESPONSE +",{}]\n}  \n}");
		ListItemsResponse parsed = client.listItems(2, 10);
		assertEqualsSampleHal(parsed);
		List<ListItem> items = parsed.getItems();
		assertNotNull(items);
		assertEquals(3, items.size());
		assertNullListItem(items.get(0));
		assertEqualsSampleListItem(items.get(1));
		assertNullListItem(items.get(2));
		assertNull(stubResponseAndGet("{}", () -> client.listItems(1, 1).getItems()));
		stubResponseAndAssertThrows("{}", () ->
				client.listItems(1, 0), IllegalArgumentException.class
		);
		stubResponseAndAssertThrows("{}", () ->
				client.listItems(0), IllegalArgumentException.class
		);
		assertNull(stubResponseAndGet("{}", client::listItems));
		String emptyListsResponse = "{\"_embedded\":{\"items\":[]}}";
		assertEquals(0, stubResponseAndGet(emptyListsResponse, client::listItems).size());
	}
	
	@Test
	public void testListEvents() throws Exception {
		stubResponse(HAL_TEMPLATE_RESPONSE + "  \"events\": [{},"+ SAMPLE_EVENT_RESPONSE +",{}]\n}  \n}");
		ListEventsResponse parsed = client.listEvents(2, 10);
		assertEqualsSampleHal(parsed);
		List<Event> events = parsed.getEvents();
		assertNotNull(events);
		assertEquals(3, events.size());
		assertNullEvent(events.get(0));
		assertEqualsSampleEvent(events.get(1));
		assertNullEvent(events.get(2));
		assertNull(stubResponseAndGet("{}", () -> client.listEvents(1, 1)).getEvents());
		stubResponseAndAssertThrows("{}", () ->
				client.listEvents(1, 0), IllegalArgumentException.class
		);
		stubResponseAndAssertThrows("{}", () -> client.listEvents(0), IllegalArgumentException.class);
		assertNull(stubResponseAndGet("{}", client::listEvents));
		String emptyListsResponse = "{\"_embedded\":{\"events\":[]}}";
		assertEquals(0, stubResponseAndGet(emptyListsResponse, client::listEvents).size());
	}
}