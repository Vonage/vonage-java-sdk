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

import com.vonage.client.HttpWrapper;
import com.vonage.client.VonageClient;
import com.vonage.client.VonageClientException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

/**
 * A client for talking to the Vonage Proactive Connect API. The standard way to obtain an instance of this class is to use
 * {@link VonageClient#getProactiveConnectClient()}.
 */
public class ProactiveConnectClient {
	final CreateListEndpoint createList;
	final GetListEndpoint getList;
	final UpdateListEndpoint updateList;
	final DeleteListEndpoint deleteList;
	final ClearListEndpoint clearList;
	final FetchListEndpoint fetchList;
	final ListListsEndpoint listLists;
	final CreateListItemEndpoint createListItem;
	final GetListItemEndpoint getListItem;
	final UpdateListItemEndpoint updateListItem;
	final DeleteListItemEndpoint deleteListItem;
	final DownloadListItemsEndpoint downloadListItems;
	final UploadListItemsEndpoint uploadListItems;
	final ListItemsEndpoint listItems;
	final ListEventsEndpoint listEvents;

	/**
	 * Constructor.
	 *
	 * @param httpWrapper (REQUIRED) shared HTTP wrapper object used for making REST calls.
	 */
	public ProactiveConnectClient(HttpWrapper httpWrapper) {
		createList = new CreateListEndpoint(httpWrapper);
		getList = new GetListEndpoint(httpWrapper);
		updateList = new UpdateListEndpoint(httpWrapper);
		deleteList = new DeleteListEndpoint(httpWrapper);
		clearList = new ClearListEndpoint(httpWrapper);
		fetchList = new FetchListEndpoint(httpWrapper);
		listLists = new ListListsEndpoint(httpWrapper);
		createListItem = new CreateListItemEndpoint(httpWrapper);
		getListItem = new GetListItemEndpoint(httpWrapper);
		updateListItem = new UpdateListItemEndpoint(httpWrapper);
		deleteListItem = new DeleteListItemEndpoint(httpWrapper);
		downloadListItems = new DownloadListItemsEndpoint(httpWrapper);
		uploadListItems = new UploadListItemsEndpoint(httpWrapper);
		listItems = new ListItemsEndpoint(httpWrapper);
		listEvents = new ListEventsEndpoint(httpWrapper);
	}

	/**
	 *
	 * @param request
	 *
	 * @return The list that was created with updated metadata.
	 */
	public ContactsList createList(ContactsList request) {
		return createList.execute(request);
	}

	/**
	 *
	 * @param listId
	 *
	 * @return The list associated with the ID.
	 */
	public ContactsList getList(String listId) {
		return getList.execute(listId);
	}

	/**
	 *
	 * @param listId
	 * @param updatedList
	 *
	 * @return
	 */
	public ContactsList updateList(String listId, ContactsList updatedList) {
		return updateList.execute(new UpdateListRequestWrapper(listId, updatedList));
	}

	/**
	 *
	 * @param listId
	 *
	 * @return The list that was deleted.
	 */
	public ContactsList deleteList(String listId) {
		return deleteList.execute(listId);
	}

	/**
	 *
	 * @param listId
	 */
	public void clearList(String listId) {
		clearList.execute(listId);
	}

	/**
	 *
	 * @param listId
	 */
	public void fetchList(String listId) {
		fetchList.execute(listId);
	}

	/**
	 * Gets the first 1000 lists in the application.
	 *
	 * @return The lists in order of creation.
	 */
	public List<ContactsList> getLists() {
		return listLists(1, 1000).getLists();
	}

	/**
	 *
	 * @param page
	 *
	 * @return
	 */
	public ListsResponse getLists(int page) {
		return listLists(page, null);
	}

	/**
	 *
	 * @param page
	 * @param pageSize
	 *
	 * @return
	 */
	public ListsResponse getLists(int page, int pageSize) {
		return listLists(page, pageSize);
	}

	private ListsResponse listLists(Integer page, Integer pageSize) {
		return listLists.execute(new HalRequestWrapper(page, pageSize, null));
	}

	/**
	 *
	 * @param listId
	 * @param data
	 *
	 * @return
	 */
	public ListItem createListItem(String listId, Map<String, ?> data) {
		return createListItem.execute(new ListItemRequestWrapper(listId, null, new DataWrapper(data)));
	}

	/**
	 *
	 * @param listId
	 * @param itemId
	 *
	 * @return
	 */
	public ListItem getListItem(String listId, String itemId) {
		return getListItem.execute(new ListItemRequestWrapper(listId, itemId, null));
	}

	/**
	 *
	 * @param listId
	 * @param itemId
	 * @param request
	 *
	 * @return
	 */
	public ListItem updateListItem(String listId, String itemId, DataWrapper request) {
		return updateListItem.execute(new ListItemRequestWrapper(listId, itemId, request));
	}

	/**
	 *
	 * @param listId
	 * @param itemId
	 *
	 * @return
	 */
	public ListItem deleteListItem(String listId, String itemId) {
		return deleteListItem.execute(new ListItemRequestWrapper(listId, itemId, null));
	}

	/**
	 *
	 * @param listId
	 *
	 * @return
	 */
	public byte[] downloadListItems(String listId) {
		return downloadListItems.execute(new DownloadListItemsRequestWrapper(listId, null));
	}

	/**
	 *
	 * @param listId
	 * @param file
	 */
	public void downloadListItems(String listId, Path file) {
		downloadListItems.execute(new DownloadListItemsRequestWrapper(listId, file));
	}

	/**
	 *
	 * @param listId
	 * @param csvFile
	 *
	 * @return
	 */
	public UploadListItemsResponse uploadListItems(String listId, Path csvFile) {
		try {
			byte[] data = Files.readAllBytes(csvFile);
			return uploadListItems.execute(new UploadListItemsRequestWrapper(listId, data));
		}
		catch (IOException ex) {
			throw new VonageClientException("Could not read from file.", ex);
		}
	}


	public ListItemsResponse listItems(HalRequestWrapper request) {
		return listItems.execute(request);
	}


	private ListEventsResponse listEventsImpl(Integer page, Integer pageSize) {
		return listEvents.execute(new HalRequestWrapper(page, pageSize, null));
	}

	/**
	 * Gets the first 1000 events in the application.
	 *
	 * @return The events in order of creation.
	 */
	public List<Event> listEvents() {
		return listEventsImpl(1, 1000).getItems();
	}

	/**
	 *
	 * @param page
	 *
	 * @return
	 */
	public ListEventsResponse listEvents(int page) {
		return listEventsImpl(page, null);
	}

	/**
	 *
	 * @param page
	 * @param pageSize
	 *
	 * @return
	 */
	public ListEventsResponse listEvents(int page, int pageSize) {
		return listEventsImpl(page, pageSize);
	}
}
