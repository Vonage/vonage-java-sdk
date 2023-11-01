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

import com.vonage.client.*;
import com.vonage.client.auth.JWTAuthMethod;
import com.vonage.client.common.HalPageResponse;
import com.vonage.client.common.HttpMethod;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;

/**
 * A client for talking to the Vonage Proactive Connect API. The standard way to obtain an instance
 * of this class is to use {@link VonageClient#getProactiveConnectClient()}.
 */
public class ProactiveConnectClient {
	final RestEndpoint<ContactsList, ContactsList> createList, updateList;
	final RestEndpoint<UUID, ContactsList> getList;
	final RestEndpoint<UUID, Void> deleteList, clearList, fetchList;
	final RestEndpoint<HalRequestWrapper, ListListsResponse> listLists;
	final RestEndpoint<HalRequestWrapper, ListItemsResponse> listItems;
	final RestEndpoint<ListItemRequestWrapper, ListItem> createListItem, getListItem, updateListItem;
	final RestEndpoint<ListItemRequestWrapper, Void> deleteListItem;
	final RestEndpoint<UUID, byte[]> downloadListItems;
	final RestEndpoint<UploadListItemsRequestWrapper, UploadListItemsResponse> uploadListItems;
	final RestEndpoint<ListEventsFilter, ListEventsResponse> listEvents;

	/**
	 * Constructor.
	 *
	 * @param wrapper (REQUIRED) shared HTTP wrapper object used for making REST calls.
	 */
	public ProactiveConnectClient(HttpWrapper wrapper) {

		@SuppressWarnings("unchecked")
		class Endpoint<T, R> extends DynamicEndpoint<T, R> {
			Endpoint(Function<T, String> pathGetter, HttpMethod method, R... type) {
				super(DynamicEndpoint.<T, R> builder(type).authMethod(JWTAuthMethod.class)
						.responseExceptionType(ProactiveConnectResponseException.class)
						.requestMethod(method).wrapper(wrapper).pathGetter((de, req) -> {
							String base = de.getHttpWrapper().getHttpConfig().getApiEuBaseUri();
							return base + "/v0.1/bulk/" + pathGetter.apply(req);
						})
				);
			}
		}

		createList = new Endpoint<>(req -> "lists", HttpMethod.POST);
		getList = new Endpoint<>(listId -> "lists/"+listId, HttpMethod.GET);
		updateList = new Endpoint<>(list -> "lists/"+list.getId(), HttpMethod.PUT);
		deleteList = new Endpoint<>(listId -> "lists/"+listId, HttpMethod.DELETE);
		clearList = new Endpoint<>(listId -> "lists/"+listId+"/clear", HttpMethod.POST);
		fetchList = new Endpoint<>(listId -> "lists/"+listId+"/fetch", HttpMethod.POST);
		listLists = new Endpoint<>(req -> "lists", HttpMethod.GET);
		listItems = new Endpoint<>(req -> "lists/"+req.id+"/items", HttpMethod.GET);
		createListItem = new Endpoint<>(req -> "lists/"+req.listId+"/items", HttpMethod.POST);
		getListItem = new Endpoint<>(req -> "lists/"+req.listId+"/items/"+req.itemId, HttpMethod.GET);
		updateListItem = new Endpoint<>(req -> "lists/"+req.listId+"/items/"+req.itemId, HttpMethod.PUT);
		deleteListItem = new Endpoint<>(req -> "lists/"+req.listId+"/items/"+req.itemId, HttpMethod.DELETE);
		downloadListItems = new Endpoint<>(listId -> "lists/"+listId+"/items/download", HttpMethod.GET);
		uploadListItems = new Endpoint<>(req -> "lists/"+req.listId+"/items/import", HttpMethod.POST);
		listEvents = new Endpoint<>(req -> "events", HttpMethod.GET);
	}

	private UUID validateUuid(String name, UUID uuid) {
		return Objects.requireNonNull(uuid, name+" is required.");
	}

	private <R extends HalPageResponse> R halRequest(RestEndpoint<HalRequestWrapper, R> endpoint,
			String id, Integer page, Integer pageSize, SortOrder order) {
		if (page != null && page < 1) {
			throw new IllegalArgumentException("Page number must be positive.");
		}
		if (pageSize != null && pageSize < 1) {
			throw new IllegalArgumentException("Page size must be positive.");
		}
		return endpoint.execute(new HalRequestWrapper(page, pageSize, order, id));
	}

	/**
	 * Create a new list.
	 *
	 * @param list The new list's properties.
	 *
	 * @return The list that was created with updated metadata.
	 *
	 * @throws ProactiveConnectResponseException If the request was unsuccessful.
	 * This could be for the following reasons:
	 * <ul>
	 *   <li><b>400</b>: Invalid request parameter or body.</li>
	 * 	 <li><b>409</b>: Conflict.</li>
	 * 	 <li><b>422</b>: Resource limit reached / exceeded.</li>
	 * </ul>
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
	 *
	 * @throws ProactiveConnectResponseException If the list does not exist or couldn't be retrieved.
	 */
	public ContactsList getList(UUID listId) {
		return getList.execute(validateUuid("List ID", listId));
	}

	/**
	 * Update an existing list.
	 *
	 * @param listId Unique ID of the list.
	 * @param updatedList The new list properties.
	 *
	 * @return The updated list.
	 *
	 * @throws ProactiveConnectResponseException If the request was unsuccessful.
	 * This could be for the following reasons:
	 * <ul>
	 *   <li><b>400</b>: Invalid request parameter or body.</li>
	 * 	 <li><b>404</b>: List not found.</li>
	 * 	 <li><b>409</b>: Conflict.</li>
	 * </ul>
	 */
	public ContactsList updateList(UUID listId, ContactsList updatedList) {
		Objects.requireNonNull(updatedList, "List structure is required.");
		updatedList.id = validateUuid("List ID", listId);
		return updateList.execute(updatedList);
	}

	/**
	 * Delete a list.
	 *
	 * @param listId Unique ID of the list.
	 *
	 * @throws ProactiveConnectResponseException If the list does not exist or couldn't be deleted.
	 */
	public void deleteList(UUID listId) {
		deleteList.execute(validateUuid("List ID", listId));
	}

	/**
	 * Delete all items in a list.
	 *
	 * @param listId Unique ID of the list.
	 *
	 * @throws ProactiveConnectResponseException If the list does not exist or couldn't be cleared.
	 */
	public void clearList(UUID listId) {
		clearList.execute(validateUuid("List ID", listId));
	}

	/**
	 * Fetch and replace all items from datasource.
	 *
	 * @param listId Unique ID of the list.
	 *
	 * @throws ProactiveConnectResponseException If the list does not exist or couldn't be fetched.
	 */
	public void fetchList(UUID listId) {
		fetchList.execute(validateUuid("List ID", listId));
	}

	/**
	 * Gets the first 1000 lists in the application.
	 *
	 * @return The lists in order of creation.
	 *
	 * @throws ProactiveConnectResponseException If there was an error in retrieving the lists.
	 */
	public List<ContactsList> listLists() {
		return halRequest(listLists, null, 1, 1000, null).getLists();
	}

	/**
	 * Get all lists on a particular page.
	 *
	 * @param page The page number of the HAL response to parse results.
	 * @param pageSize Number of results per page in the HAL response.
	 * @param order The order to sort results by (ascending or descending).
	 *
	 * @return The lists page.
	 *
	 * @throws ProactiveConnectResponseException If there was an error in retrieving the lists.
	 */
	public ListListsResponse listLists(int page, int pageSize, SortOrder order) {
		return halRequest(listLists, null, page, pageSize, order);
	}

	/**
	 * Create a new list item.
	 *
	 * @param listId Unique ID of the list.
	 * @param data The new item's data as a Map.
	 *
	 * @return The created list item.
	 *
	 * @throws ProactiveConnectResponseException If the request was unsuccessful.
	 * This could be for the following reasons:
	 * <ul>
	 *   <li><b>400</b>: Invalid request parameter or body.</li>
	 * 	 <li><b>404</b>: List not found.</li>
	 * 	 <li><b>422</b>: Resource limit reached / exceeded.</li>
	 * </ul>
	 */
	public ListItem createListItem(UUID listId, Map<String, ?> data) {
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
	 *
	 * @throws ProactiveConnectResponseException If the list or item does not exist or couldn't be retrieved.
	 */
	public ListItem getListItem(UUID listId, UUID itemId) {
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
	 *
	 * @throws ProactiveConnectResponseException If the request was unsuccessful.
	 * This could be for the following reasons:
	 * <ul>
	 *   <li><b>400</b>: Invalid request parameter or body.</li>
	 * 	 <li><b>404</b>: List or item not found.</li>
	 * </ul>
	 */
	public ListItem updateListItem(UUID listId, UUID itemId, Map<String, ?> data) {
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
	 *
	 * @throws ProactiveConnectResponseException If the list or item does not exist or couldn't be deleted.
	 */
	public void deleteListItem(UUID listId, UUID itemId) {
		deleteListItem.execute(new ListItemRequestWrapper(
				validateUuid("List ID", listId), validateUuid("Item ID", itemId), null
		));
	}

	/**
	 * Download all items in a list in CSV format.
	 * Use {@link #downloadListItems(UUID, Path)} to save the CSV as a file.
	 *
	 * @param listId Unique ID of the list.
	 *
	 * @return The list items contents as a CSV-formatted String.
	 * @see #downloadListItems(UUID, Path)
	 *
	 * @throws ProactiveConnectResponseException If the list does not exist or couldn't be retrieved.
	 */
	public String downloadListItems(UUID listId) {
		return new String(downloadListItems.execute(validateUuid("List ID", listId)));
	}

	/**
	 * Download all items in a list in CSV format.
	 * Use {@link #downloadListItems(UUID)} to get the results as a String.
	 *
	 * @param listId Unique ID of the list.
	 * @param file Path of the file to write the downloaded results to.
	 *
	 * @throws ProactiveConnectResponseException If the list does not exist or couldn't be retrieved.
	 */
	public void downloadListItems(UUID listId, Path file) {
		try {
			Files.write(
					Objects.requireNonNull(file, "CSV file is required."),
					downloadListItems.execute(validateUuid("List ID", listId))
			);
		}
		catch (IOException ex) {
			throw new VonageUnexpectedException("Couldn't write list '"+listId+"' to file '"+file+"'", ex);
		}
	}

	/**
	 * Import list items from a CSV file.
	 *
	 * @param listId Unique ID of the list.
	 * @param csvFile Path to the CSV file to upload.
	 *
	 * @return Result of the upload if successful.
	 *
	 * @throws ProactiveConnectResponseException If the request was unsuccessful.
	 * This could be for the following reasons:
	 * <ul>
	 * 	 <li><b>404</b>: List not found.</li>
	 * 	 <li><b>422</b>: Resource limit reached / exceeded.</li>
	 * </ul>
	 */
	public UploadListItemsResponse uploadListItems(UUID listId, Path csvFile) {
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
	 * @param listId Unique ID of the list to retrieve items from.
	 *
	 * @return The events in order of creation.
	 *
	 * @throws ProactiveConnectResponseException If the list does not exist or the items couldn't be retrieved.
	 */
	public List<ListItem> listItems(UUID listId) {
		return halRequest(listItems,
				validateUuid("List ID", listId).toString(),
				1, 1000, null
		).getItems();
	}

	/**
	 * Get all items on a particular page.
	 *
	 * @param listId Unique ID of the list to retrieve items from.
	 * @param page The page number of the HAL response to parse results.
	 * @param pageSize Number of results per page in the HAL response.
	 * @param order The order to sort results by (ascending or descending).
	 *
	 * @return The items page.
	 *
	 * @throws ProactiveConnectResponseException If the list does not exist or the items couldn't be retrieved.
	 */
	public ListItemsResponse listItems(UUID listId, int page, int pageSize, SortOrder order) {
		return halRequest(listItems, validateUuid("List ID", listId).toString(), page, pageSize, order);
	}

	/**
	 * Gets all events in the application matching the criteria.
	 *
	 * @param filter Optional attributes to narrow down the results.
	 *
	 * @return The list of events applicable to the request criteria, in order of creation.
	 *
	 * @throws ProactiveConnectResponseException If the events couldn't be retrieved.
	 */
	public List<Event> listEvents(ListEventsFilter filter) {
		return listEvents.execute(filter != null ? filter : ListEventsFilter.builder().build()).getEvents();
	}
}
