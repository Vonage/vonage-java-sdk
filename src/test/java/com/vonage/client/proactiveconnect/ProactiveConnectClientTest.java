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
import org.junit.function.ThrowingRunnable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class ProactiveConnectClientTest extends ClientTest<ProactiveConnectClient> {

	static Path SAMPLE_CSV_PATH;
	static final Instant
		SAMPLE_CREATED_AT = Instant.parse("2022-06-19T17:59:28.085Z"),
		SAMPLE_UPDATED_AT = Instant.now();
	static final UUID
			SAMPLE_LIST_ID = UUID.fromString("29192c4a-4058-49da-86c2-3e349d1065b7"),
			SAMPLE_ITEM_ID = UUID.fromString("4cb98f71-a879-49f7-b5cf-2314353eb52c");
	static final String
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

	void assert409ResponseException(ThrowingRunnable invocation) throws Exception {
		int statusCode = 409;
		ProactiveConnectResponseException expectedResponse = ProactiveConnectResponseException.fromJson(
				"{\n" +
				"   \"type\": \"https://developer.vonage.com/en/api-errors\",\n" +
				"   \"title\": \"Request data did not validate\",\n" +
				"   \"detail\": \"Bad Request\",\n" +
				"   \"instance\": \"14225136-3501-4f29-8ee1-6b6301e9b64c\",\n" +
				"   \"errors\": [\n" +
				"      \"First problem\",\n" +
				"      \"another problem.\"\n" +
				"   ]\n" +
				"}"
		);

		String expectedJson = expectedResponse.toJson();
		assertEquals(212, expectedJson.length());
		wrapper.setHttpClient(stubHttpClient(statusCode, expectedJson));
		expectedResponse.setStatusCode(statusCode);
		String failPrefix = "Expected "+expectedResponse.getClass().getSimpleName()+", but got ";

		try {
			invocation.run();
			fail(failPrefix + "nothing.");
		}
		catch (ProactiveConnectResponseException ex) {
			assertEquals(expectedResponse, ex);
			assertEquals(expectedJson, ex.toJson());
		}
		catch (Throwable ex) {
			fail(failPrefix + ex);
		}
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
		assertEquals(SAMPLE_LIST_ID, parsed.getId());
		assertEquals(500, parsed.getItemsCount().intValue());
	}

	static void assertEqualsSampleListItem(ListItem parsed) {
		assertNotNull(parsed);
		assertEquals(SAMPLE_ITEM_ID, parsed.getId());
		assertEquals(SAMPLE_LIST_ID, parsed.getListId());
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
		assert409ResponseException(() -> client.createList(request));
		ContactsList response = stubResponseAndGet(SAMPLE_LIST_RESPONSE, () -> client.createList(request));
		assertEquals(request, response);
		assertEqualsSampleList(response);
		stubResponseAndAssertThrows(201, () -> client.createList(null), NullPointerException.class);
		stubResponseAndAssertThrows(201, () ->
				client.createList(ContactsList.builder(null).build()),
				IllegalArgumentException.class
		);
		stubResponseAndAssertThrows(201, () ->
				client.createList(ContactsList.builder("").build()),
				IllegalArgumentException.class
		);
	}
	
	@Test
	public void testGetList() throws Exception {
		stubResponse(200, SAMPLE_LIST_RESPONSE);
		assertEqualsSampleList(client.getList(SAMPLE_LIST_ID));
		stubResponseAndAssertThrows(() -> client.getList(null), NullPointerException.class);
		assert409ResponseException(() -> client.getList(SAMPLE_LIST_ID));
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
		assert409ResponseException(() -> client.updateList(SAMPLE_LIST_ID, request));
	}
	
	@Test
	public void testDeleteList() throws Exception {
		stubResponseAndRun(202, () -> client.deleteList(SAMPLE_LIST_ID));
		stubResponseAndAssertThrows(202, () -> client.deleteList(null), NullPointerException.class);
		assert409ResponseException(() -> client.deleteList(SAMPLE_LIST_ID));
	}
	
	@Test
	public void testClearList() throws Exception {
		stubResponseAndRun(202, () -> client.clearList(SAMPLE_LIST_ID));
		stubResponseAndAssertThrows(202, () -> client.clearList(null), NullPointerException.class);
		assert409ResponseException(() -> client.clearList(SAMPLE_LIST_ID));
	}
	
	@Test
	public void testFetchList() throws Exception {
		stubResponseAndRun(202, () -> client.fetchList(SAMPLE_LIST_ID));
		stubResponseAndAssertThrows(202, () -> client.fetchList(null), NullPointerException.class);
		assert409ResponseException(() -> client.fetchList(SAMPLE_LIST_ID));
	}
	
	@Test
	public void testListLists() throws Exception {
		stubResponse(HAL_TEMPLATE_RESPONSE + "  \"lists\": [{},"+SAMPLE_LIST_RESPONSE+",{}]\n}  \n}");
		ListListsResponse parsed = client.listLists(2, 10, SortOrder.ASC);
		assertEqualsSampleHal(parsed);
		List<ContactsList> lists = parsed.getLists();
		assertNotNull(lists);
		assertEquals(3, lists.size());
		assertNullList(lists.get(0));
		assertEqualsSampleList(lists.get(1));
		assertNullList(lists.get(2));
		assertNull(stubResponseAndGet("{}", () -> client.listLists(1, 1, null)).getLists());
		stubResponseAndAssertThrows("{}", () ->
				client.listLists(1, 0, SortOrder.ASC), IllegalArgumentException.class
		);
		stubResponseAndAssertThrows("{}", () ->
				client.listLists(0, 25, SortOrder.DESC), IllegalArgumentException.class
		);
		assertNull(stubResponseAndGet("{}", client::listLists));
		String emptyListsResponse = "{\"_embedded\":{\"lists\":[]}}";
		assertEquals(0, stubResponseAndGet(emptyListsResponse, client::listLists).size());
		assert409ResponseException(client::listLists);
	}
	
	@Test
	public void testCreateListItem() throws Exception {
		Map<String, ?> data = Collections.emptyMap();
		assertEqualsSampleListItem(stubResponseAndGet(SAMPLE_LIST_ITEM_RESPONSE, () ->
				client.createListItem(SAMPLE_LIST_ID, data)
		));
		stubResponseAndAssertThrows(200, () ->
				client.createListItem(null, data), NullPointerException.class
		);
		stubResponseAndAssertThrows(200, () ->
				client.createListItem(SAMPLE_LIST_ID, null), NullPointerException.class
		);
		assert409ResponseException(() -> client.createListItem(SAMPLE_LIST_ID, data));
	}
	
	@Test
	public void testGetListItem() throws Exception {
		assertEqualsSampleListItem(stubResponseAndGet(
				SAMPLE_LIST_ITEM_RESPONSE, () -> client.getListItem(SAMPLE_LIST_ID, SAMPLE_ITEM_ID)
		));
		stubResponseAndAssertThrows(SAMPLE_LIST_ITEM_RESPONSE, () ->
				client.getListItem(null, SAMPLE_ITEM_ID), NullPointerException.class
		);
		stubResponseAndAssertThrows(SAMPLE_LIST_ITEM_RESPONSE, () ->
				client.getListItem(SAMPLE_LIST_ID, null), NullPointerException.class
		);
		stubResponseAndAssertThrows(SAMPLE_LIST_ITEM_RESPONSE, () ->
				client.getListItem(null, null), NullPointerException.class
		);
		assert409ResponseException(() -> client.getListItem(SAMPLE_LIST_ID, SAMPLE_ITEM_ID));
	}
	
	@Test
	public void testUpdateListItem() throws Exception {
		Map<String, ?> data = Collections.singletonMap("Foo", "baR");
		assertEqualsSampleListItem(stubResponseAndGet(SAMPLE_LIST_ITEM_RESPONSE, () ->
				client.updateListItem(SAMPLE_LIST_ID, SAMPLE_ITEM_ID, data)
		));
		stubResponseAndAssertThrows(SAMPLE_LIST_ITEM_RESPONSE, () ->
				client.updateListItem(null, SAMPLE_ITEM_ID, data), NullPointerException.class
		);
		stubResponseAndAssertThrows(SAMPLE_LIST_ITEM_RESPONSE, () ->
				client.updateListItem(SAMPLE_LIST_ID, null, data), NullPointerException.class
		);
		stubResponseAndAssertThrows(SAMPLE_LIST_ITEM_RESPONSE, () ->
				client.updateListItem(SAMPLE_LIST_ID, SAMPLE_ITEM_ID, null), NullPointerException.class
		);
		assert409ResponseException(() -> client.updateListItem(SAMPLE_LIST_ID, SAMPLE_ITEM_ID, data));
	}
	
	@Test
	public void testDeleteListItem() throws Exception {
		stubResponseAndRun(204, () -> client.deleteListItem(SAMPLE_LIST_ID, SAMPLE_ITEM_ID));
		stubResponseAndAssertThrows(SAMPLE_LIST_ITEM_RESPONSE, () ->
				client.deleteListItem(null, SAMPLE_ITEM_ID), NullPointerException.class
		);
		stubResponseAndAssertThrows(SAMPLE_LIST_ITEM_RESPONSE, () ->
				client.deleteListItem(SAMPLE_LIST_ID, null), NullPointerException.class
		);
		stubResponseAndAssertThrows(SAMPLE_LIST_ITEM_RESPONSE, () ->
				client.deleteListItem(null, null), NullPointerException.class
		);
		assert409ResponseException(() -> client.deleteListItem(SAMPLE_LIST_ID, SAMPLE_ITEM_ID));
	}
	
	@Test
	public void testDownloadListItems() throws Exception {
		String stub = "<BINARY>";
		String parsed = stubResponseAndGet(stub, () -> client.downloadListItems(SAMPLE_LIST_ID));
		assertEquals(stub, parsed);
		stubResponseAndAssertThrows(stub, () ->
				client.downloadListItems(null), NullPointerException.class
		);
		byte[] fileContents = Files.readAllBytes(SAMPLE_CSV_PATH);
		assertEquals(0, fileContents.length);

		stub = "Item Key 1,K2,K3\nval1,Value B,123";
		stubResponseAndRun(stub, () -> client.downloadListItems(SAMPLE_LIST_ID, SAMPLE_CSV_PATH));
		fileContents = Files.readAllBytes(SAMPLE_CSV_PATH);
		assertEquals(stub, new String(fileContents));

		stubResponseAndAssertThrows(stub, () ->
				client.downloadListItems(SAMPLE_LIST_ID, null), NullPointerException.class
		);
		stubResponseAndAssertThrows(stub, () ->
				client.downloadListItems(null, SAMPLE_CSV_PATH), NullPointerException.class
		);
		assert409ResponseException(() -> client.downloadListItems(SAMPLE_LIST_ID));
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
				client.uploadListItems(null, SAMPLE_CSV_PATH), NullPointerException.class
		);
		stubResponseAndAssertThrows(json, () ->
				client.uploadListItems(SAMPLE_LIST_ID, null), NullPointerException.class
		);
		stubResponseAndAssertThrows(json, () ->
				client.uploadListItems(SAMPLE_LIST_ID, Paths.get("dummy")), VonageClientException.class
		);
		assert409ResponseException(() -> client.uploadListItems(SAMPLE_LIST_ID, SAMPLE_CSV_PATH));
	}
	
	@Test
	public void testListItems() throws Exception {
		stubResponse(HAL_TEMPLATE_RESPONSE + "  \"items\": [{},"+ SAMPLE_LIST_ITEM_RESPONSE +",{}]\n}  \n}");
		ListItemsResponse parsed = client.listItems(SAMPLE_LIST_ID, 2, 10, SortOrder.ASC);
		assertEqualsSampleHal(parsed);
		List<ListItem> items = parsed.getItems();
		assertNotNull(items);
		assertEquals(3, items.size());
		assertNullListItem(items.get(0));
		assertEqualsSampleListItem(items.get(1));
		assertNullListItem(items.get(2));
		assertNull(stubResponseAndGet("{}", () ->
				client.listItems(SAMPLE_LIST_ID, 1, 1, null).getItems()
		));
		stubResponseAndAssertThrows("{}", () ->
				client.listItems(SAMPLE_LIST_ID, 1, 0, SortOrder.DESC), IllegalArgumentException.class
		);
		stubResponseAndAssertThrows("{}", () ->
				client.listItems(SAMPLE_LIST_ID, 0, 25, SortOrder.DESC), IllegalArgumentException.class
		);
		stubResponseAndAssertThrows("{}", () ->
				client.listItems(null), NullPointerException.class
		);
		stubResponseAndAssertThrows("{}", () ->
				client.listItems(null, 3, 25, SortOrder.ASC), NullPointerException.class
		);
		assertNull(stubResponseAndGet("{}", () -> client.listItems(SAMPLE_LIST_ID)));
		String emptyListsResponse = "{\"_embedded\":{\"items\":[]}}";
		assertEquals(0, stubResponseAndGet(emptyListsResponse, () -> client.listItems(SAMPLE_LIST_ID)).size());
		assert409ResponseException(() -> client.listItems(SAMPLE_LIST_ID));
	}
	
	@Test
	public void testListEvents() throws Exception {
		stubResponse(HAL_TEMPLATE_RESPONSE + "  \"events\": [{},"+ SAMPLE_EVENT_RESPONSE +",{}]\n}  \n}");
		List<Event> events = client.listEvents(null);
		assertNotNull(events);
		assertEquals(3, events.size());
		assertNullEvent(events.get(0));
		assertEqualsSampleEvent(events.get(1));
		assertNullEvent(events.get(2));
		assertNull(stubResponseAndGet("{}", () -> client.listEvents(null)));
		stubResponseAndAssertThrows("{}", () ->
				client.listEvents(ListEventsFilter.builder()
						.startDate(Instant.now())
						.endDate(Instant.now().minus(Duration.ofHours(3)))
						.build()
				),
				IllegalStateException.class
		);
		String emptyListsResponse = "{\"_embedded\":{\"events\":[]}}";
		assertEquals(0, stubResponseAndGet(emptyListsResponse, () -> client.listEvents(null)).size());
		assert409ResponseException(() -> client.listEvents(ListEventsFilter.builder().build()));
	}
}