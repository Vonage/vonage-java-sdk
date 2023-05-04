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

public class ProactiveConnectClientTest extends ClientTest<ProactiveConnectClient> {
	
	public ProactiveConnectClientTest() {
		client = new ProactiveConnectClient(wrapper);
	}

	
	/*@Test
	public void testCreateList() throws Exception {
		ContactsList request = null;
		String responseJson = "{}";
		stubResponseAndRun(200, responseJson, () -> client.createList(request));
		stubResponseAndAssertThrows(200, () -> client.createList(null), IllegalArgumentException.class);
	}
	
	@Test
	public void testGetList() throws Exception {
		String request = null;
		String responseJson = "{}";
		stubResponseAndRun(200, responseJson, () -> client.getList(request));
		stubResponseAndAssertThrows(200, () -> client.getList(null), IllegalArgumentException.class);
	}
	
	@Test
	public void testUpdateList() throws Exception {
		UpdateListRequestWrapper request = null;
		String responseJson = "{}";
		stubResponseAndRun(200, responseJson, () -> client.updateList(request));
		stubResponseAndAssertThrows(200, () -> client.updateList(null), IllegalArgumentException.class);
	}
	
	@Test
	public void testDeleteList() throws Exception {
		String request = null;
		String responseJson = "{}";
		stubResponseAndRun(200, responseJson, () -> client.deleteList(request));
		stubResponseAndAssertThrows(200, () -> client.deleteList(null), IllegalArgumentException.class);
	}
	
	@Test
	public void testClearList() throws Exception {
		String request = null;
		String responseJson = "{}";
		stubResponseAndRun(200, responseJson, () -> client.clearList(request));
		stubResponseAndAssertThrows(200, () -> client.clearList(null), IllegalArgumentException.class);
	}
	
	@Test
	public void testFetchList() throws Exception {
		String request = null;
		String responseJson = "{}";
		stubResponseAndRun(200, responseJson, () -> client.fetchList(request));
		stubResponseAndAssertThrows(200, () -> client.fetchList(null), IllegalArgumentException.class);
	}
	
	@Test
	public void testListLists() throws Exception {
		HalRequestWrapper request = null;
		String responseJson = "{}";
		stubResponseAndRun(200, responseJson, () -> client.listLists(request));
		stubResponseAndAssertThrows(200, () -> client.listLists(null), IllegalArgumentException.class);
	}
	
	@Test
	public void testCreateListItem() throws Exception {
		ListItemRequestWrapper request = null;
		String responseJson = "{}";
		stubResponseAndRun(200, responseJson, () -> client.createListItem(request));
		stubResponseAndAssertThrows(200, () -> client.createListItem(null), IllegalArgumentException.class);
	}
	
	@Test
	public void testGetListItem() throws Exception {
		ListItemRequestWrapper request = null;
		String responseJson = "{}";
		stubResponseAndRun(200, responseJson, () -> client.getListItem(request));
		stubResponseAndAssertThrows(200, () -> client.getListItem(null), IllegalArgumentException.class);
	}
	
	@Test
	public void testUpdateListItem() throws Exception {
		ListItemRequestWrapper request = null;
		String responseJson = "{}";
		stubResponseAndRun(200, responseJson, () -> client.updateListItem(request));
		stubResponseAndAssertThrows(200, () -> client.updateListItem(null), IllegalArgumentException.class);
	}
	
	@Test
	public void testDeleteListItem() throws Exception {
		ListItemRequestWrapper request = null;
		String responseJson = "{}";
		stubResponseAndRun(200, responseJson, () -> client.deleteListItem(request));
		stubResponseAndAssertThrows(200, () -> client.deleteListItem(null), IllegalArgumentException.class);
	}
	
	@Test
	public void testDownloadListItems() throws Exception {
		String request = null;
		String responseJson = "{}";
		stubResponseAndRun(200, responseJson, () -> client.downloadListItems(request));
		stubResponseAndAssertThrows(200, () -> client.downloadListItems(null), IllegalArgumentException.class);
	}
	
	@Test
	public void testUploadListItems() throws Exception {
		UploadListItemsRequestWrapper request = null;
		String responseJson = "{}";
		stubResponseAndRun(200, responseJson, () -> client.uploadListItems(request));
		stubResponseAndAssertThrows(200, () -> client.uploadListItems(null), IllegalArgumentException.class);
	}
	
	@Test
	public void testListItems() throws Exception {
		HalRequestWrapper request = null;
		String responseJson = "{}";
		stubResponseAndRun(200, responseJson, () -> client.listItems(request));
		stubResponseAndAssertThrows(200, () -> client.listItems(null), IllegalArgumentException.class);
	}
	
	@Test
	public void testListEvents() throws Exception {
		HalRequestWrapper request = null;
		String responseJson = "{}";
		stubResponseAndRun(200, responseJson, () -> client.listEvents(request));
		stubResponseAndAssertThrows(200, () -> client.listEvents(null), IllegalArgumentException.class);
	}*/
}