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
package com.vonage.client.reports;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.JsonableBaseObject;
import java.util.List;
import java.util.Map;

/**
 * Response for synchronous record retrieval ({@code GET /v2/reports/records}).
 * Contains pagination metadata and the list of records for the requested product.
 * <p>
 * Record fields vary by product type; use {@link #getRecords()} to access the raw field maps.
 * </p>
 */
public class RecordsResponse extends JsonableBaseObject {

    @JsonProperty("request_id")
    private String requestId;

    @JsonProperty("request_status")
    private ReportStatus requestStatus;

    @JsonProperty("received_at")
    private String receivedAt;

    @JsonProperty("items_count")
    private Long itemsCount;

    @JsonProperty("cursor")
    private String cursor;

    @JsonProperty("iv")
    private String iv;

    @JsonProperty("ids_not_found")
    private String idsNotFound;

    @JsonProperty("product")
    private Product product;

    @JsonProperty("_links")
    private Map<String, Map<String, String>> links;

    @JsonProperty("records")
    private List<Map<String, Object>> records;

    RecordsResponse() {}

    /**
     * Unique ID associated with this synchronous request.
     *
     * @return The request ID, or {@code null} if not available.
     */
    public String getRequestId() {
        return requestId;
    }

    /**
     * Result status of the synchronous request.
     * Either {@link ReportStatus#SUCCESS} or {@link ReportStatus#TRUNCATED}.
     *
     * @return The request status, or {@code null} if not available.
     */
    public ReportStatus getRequestStatus() {
        return requestStatus;
    }

    /**
     * Timestamp when the request was processed by the Reports API.
     *
     * @return The received-at timestamp string, or {@code null} if not available.
     */
    public String getReceivedAt() {
        return receivedAt;
    }

    /**
     * The number of records returned in this page/response.
     *
     * @return The items count, or {@code null} if not available.
     */
    public Long getItemsCount() {
        return itemsCount;
    }

    /**
     * Cursor for paginating to the next page of results (present only if more records exist).
     *
     * @return The pagination cursor, or {@code null} if there are no more pages.
     */
    public String getCursor() {
        return cursor;
    }

    /**
     * Initialization vector for cursor processing (present only if pagination is applicable).
     *
     * @return The IV string, or {@code null} if pagination is not applicable.
     */
    public String getIv() {
        return iv;
    }

    /**
     * Comma-separated list of IDs that were not found when using ID-based queries.
     *
     * @return The not-found IDs string, or {@code null} if all IDs were found.
     */
    public String getIdsNotFound() {
        return idsNotFound;
    }

    /**
     * The product type for the records in this response.
     *
     * @return The product enum value, or {@code null} if not available.
     */
    public Product getProduct() {
        return product;
    }

    /**
     * HAL links for navigation. May contain {@code self} and {@code next} links.
     * The {@code next} link is only present if more records are available.
     *
     * @return The links map, or {@code null} if not available.
     */
    public Map<String, Map<String, String>> getLinks() {
        return links;
    }

    /**
     * The records returned by this request. Each record is a map of field names to values.
     * The available fields depend on the product type — refer to the
     * <a href="https://developer.vonage.com/en/api/reports">Reports API documentation</a>
     * for details on each product's record schema.
     *
     * @return The list of record maps, or {@code null} if no records were returned.
     */
    public List<Map<String, Object>> getRecords() {
        return records;
    }
}
