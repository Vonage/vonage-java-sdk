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

import com.vonage.client.AbstractMethod;
import com.vonage.client.HttpWrapper;
import com.vonage.client.VonageClient;
import com.vonage.client.VonageClientException;
import com.vonage.client.common.HalPageResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * A client for talking to the Vonage Proactive Connect API. The standard way to obtain an instance
 * of this class is to use {@link VonageClient#getProactiveConnectClient()}.
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

	private String validateUuid(String name, String uuid) {
		return UUID.fromString(Objects.requireNonNull(uuid, name+" is required.")).toString();
	}

	private <R extends HalPageResponse> R halRequest(AbstractMethod<HalRequestWrapper, R> endpoint, Integer page, Integer pageSize) {
		if (page != null && page < 1) {
			throw new IllegalArgumentException("Page number must be positive.");
		}
		if (pageSize != null && pageSize < 1) {
			throw new IllegalArgumentException("Page size must be positive.");
		}
		return endpoint.execute(new HalRequestWrapper(page, pageSize, null));
	}

	/**
	 * Create a new list.
	 *
	 * @param list The new list's properties.
	 *
	 * @return The list that was created with updated metadata.
	 */
	public ContactsList createList(ContactsList list) {
		return createList.execute(Objects.requireNonNull(list, "List structure is required."));
	}

	/**
	 * Retrieve a list.
	 *
	 * @param listId Unique ID of the list.
	 *
	 * @return The list associated with the ID.
	 */
	public ContactsList getList(String listId) {
		return getList.execute(validateUuid("List ID", listId));
	}

	/**
	 * Update an existing list.
	 *
	 * @param listId Unique ID of the list.
	 * @param updatedList The new list properties.
	 *
	 * @return The updated list.
	 */
	public ContactsList updateList(String listId, ContactsList updatedList) {
		return updateList.execute(new UpdateListRequestWrapper(
				validateUuid("List ID", listId),
				Objects.requireNonNull(updatedList, "List structure is required.")
		));
	}

	/**
	 * Delete a list.
	 *
	 * @param listId Unique ID of the list.
	 */
	public void deleteList(String listId) {
		deleteList.execute(validateUuid("List ID", listId));
	}

	/**
	 * Delete all items in a list.
	 *
	 * @param listId Unique ID of the list.
	 */
	public void clearList(String listId) {
		clearList.execute(validateUuid("List ID", listId));
	}

	/**
	 * Fetch and replace all items from datasource.
	 *
	 * @param listId Unique ID of the list.
	 */
	public void fetchList(String listId) {
		fetchList.execute(validateUuid("List ID", listId));
	}

	/**
	 * Gets the first 1000 lists in the application.
	 *
	 * @return The lists in order of creation.
	 */
	public List<ContactsList> listLists() {
		return halRequest(listLists, 1, 1000).getLists();
	}

	/**
	 * Get all lists on a particular page (with the default number of lists per page).
	 *
	 * @param page The page number of the HAL response to parse results.
	 *
	 * @return The lists page.
	 * @see #listLists(int, int)
	 */
	public ListsResponse listLists(int page) {
		return halRequest(listLists, page, null);
	}

	/**
	 * Get all lists on a particular page.
	 *
	 * @param page The page number of the HAL response to parse results.
	 * @param pageSize Number of results per page in the HAL response.
	 *
	 * @return The lists page.
	 */
	public ListsResponse listLists(int page, int pageSize) {
		return halRequest(listLists, page, pageSize);
	}

	/**
	 * Create a new list item.
	 *
	 * @param listId Unique ID of the list.
	 * @param data The new item's data as a Map.
	 *
	 * @return The created list item.
	 */
	public ListItem createListItem(String listId, Map<String, ?> data) {
		return createListItem.execute(new ListItemRequestWrapper(
				validateUuid("List ID", listId), null,
				Objects.requireNonNull(data, "List data is required.")
		));
	}

	/**
	 * Retrieve a list item.
	 *
	 * @param listId Unique ID of the list.
	 * @param itemId Unique ID of the item.
	 *
	 * @return The requested list item.
	 */
	public ListItem getListItem(String listId, String itemId) {
		return getListItem.execute(new ListItemRequestWrapper(
				validateUuid("List ID", listId), validateUuid("Item ID", itemId), null
		));
	}

	/**
	 * Update an existing list item.
	 *
	 * @param listId Unique ID of the list.
	 * @param itemId Unique ID of the item.
	 * @param data The updated item data as a Map.
	 *
	 * @return The updated list item.
	 */
	public ListItem updateListItem(String listId, String itemId, Map<String, ?> data) {
		return updateListItem.execute(new ListItemRequestWrapper(
				validateUuid("List ID", listId),
				validateUuid("Item ID", itemId),
				Objects.requireNonNull(data, "List item data is required.")
		));
	}

	/**
	 * Delete a list item.
	 *
	 * @param listId Unique ID of the list.
	 * @param itemId Unique ID of the item.
	 */
	public void deleteListItem(String listId, String itemId) {
		deleteListItem.execute(new ListItemRequestWrapper(
				validateUuid("List ID", listId), validateUuid("Item ID", itemId), null
		));
	}

	/**
	 * Download all items in a list in CSV format.
	 * Use {@link #downloadListItems(String, Path)} to save the CSV as a file.
	 *
	 * @param listId Unique ID of the list.
	 *
	 * @return The list items CSV file contents as a String.
	 * @see #downloadListItems(String, Path)
	 */
	public String downloadListItems(String listId) {
		return new String(downloadListItems.execute(new DownloadListItemsRequestWrapper(
				validateUuid("List ID", listId), null
		)));
	}

	/**
	 * Download all items in a list in CSV format.
	 * Use {@link #downloadListItems(String)} to get the results as a raw binary.
	 *
	 * @param listId Unique ID of the list.
	 * @param file Path of the file to write the downloaded results to.
	 */
	public void downloadListItems(String listId, Path file) {
		downloadListItems.execute(new DownloadListItemsRequestWrapper(
				validateUuid("List ID", listId),
				Objects.requireNonNull(file, "CSV file is required.")
		));
	}

	/**
	 * Import list items from a CSV file.
	 *
	 * @param listId Unique ID of the list.
	 * @param csvFile Path to the CSV file to upload.
	 *
	 * @return Result of the upload if successful.
	 */
	public UploadListItemsResponse uploadListItems(String listId, Path csvFile) {
		try {
			byte[] data = Files.readAllBytes(csvFile);
			return uploadListItems.execute(new UploadListItemsRequestWrapper(
					validateUuid("List ID", listId), data
			));
		}
		catch (IOException ex) {
			throw new VonageClientException("Could not read from file.", ex);
		}
	}

	/**
	 * Gets the first 1000 events in the application.
	 *
	 * @return The events in order of creation.
	 */
	public List<ListItem> listItems() {
		return halRequest(listItems, 1, 1000).getItems();
	}

	/**
	 * Get all items on a particular page (with the default number of items per page).
	 *
	 * @param page The page number of the HAL response to parse results.
	 *
	 * @return The items page.
	 * @see #listItems(int, int)
	 */
	public ListItemsResponse listItems(int page) {
		return halRequest(listItems, page, null);
	}

	/**
	 * Get all items on a particular page.
	 *
	 * @param page The page number of the HAL response to parse results.
	 * @param pageSize Number of results per page in the HAL response.
	 *
	 * @return The items page.
	 */
	public ListItemsResponse listItems(int page, int pageSize) {
		return halRequest(listItems, page, pageSize);
	}

	/**
	 * Gets the first 1000 events in the application.
	 *
	 * @return The events in order of creation.
	 */
	public List<Event> listEvents() {
		return halRequest(listEvents, 1, 1000).getEvents();
	}

	/**
	 * Get all events on a particular page (with the default number of events per page).
	 *
	 * @param page The page number of the HAL response to parse results.
	 *
	 * @return The events page.
	 * @see #listEvents(int, int)
	 */
	public ListEventsResponse listEvents(int page) {
		return halRequest(listEvents, page, null);
	}

	/**
	 * Get all events on a particular page.
	 *
	 * @param page The page number of the HAL response to parse results.
	 * @param pageSize Number of results per page in the HAL response.
	 *
	 * @return The events page.
	 */
	public ListEventsResponse listEvents(int page, int pageSize) {
		return halRequest(listEvents, page, pageSize);
	}
}
