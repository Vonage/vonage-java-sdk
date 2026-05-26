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
import java.net.URI;
import java.util.Map;

/**
 * Response for asynchronous report operations (create, get status, cancel).
 * Contains the report's current status along with the original request parameters.
 */
public class ReportResponse extends JsonableBaseObject {

    @JsonProperty("request_id")
    private String requestId;

    @JsonProperty("request_status")
    private ReportStatus requestStatus;

    @JsonProperty("receive_time")
    private String receiveTime;

    @JsonProperty("start_time")
    private String startTime;

    @JsonProperty("items_count")
    private Long itemsCount;

    @JsonProperty("_links")
    private Map<String, Map<String, String>> links;

    // Original request fields returned in the response
    @JsonProperty("product")
    private Product product;

    @JsonProperty("account_id")
    private String accountId;

    @JsonProperty("date_start")
    private String dateStart;

    @JsonProperty("date_end")
    private String dateEnd;

    @JsonProperty("include_subaccounts")
    private Boolean includeSubaccounts;

    @JsonProperty("callback_url")
    private URI callbackUrl;

    @JsonProperty("direction")
    private String direction;

    @JsonProperty("status")
    private String status;

    @JsonProperty("from")
    private String from;

    @JsonProperty("to")
    private String to;

    @JsonProperty("country")
    private String country;

    @JsonProperty("network")
    private String network;

    @JsonProperty("client_ref")
    private String clientRef;

    @JsonProperty("account_ref")
    private String accountRef;

    @JsonProperty("include_message")
    private Boolean includeMessage;

    @JsonProperty("show_concatenated")
    private Boolean showConcatenated;

    @JsonProperty("call_id")
    private String callId;

    @JsonProperty("conversation_id")
    private String conversationId;

    @JsonProperty("leg_id")
    private String legId;

    @JsonProperty("provider")
    private String provider;

    @JsonProperty("channel")
    private String channel;

    @JsonProperty("parent_request_id")
    private String parentRequestId;

    @JsonProperty("locale")
    private String locale;

    @JsonProperty("number")
    private String number;

    @JsonProperty("product_name")
    private String productName;

    @JsonProperty("request_type")
    private String requestType;

    @JsonProperty("request_session_id")
    private String requestSessionId;

    @JsonProperty("product_path")
    private String productPath;

    @JsonProperty("correlation_id")
    private String correlationId;

    @JsonProperty("session_id")
    private String sessionId;

    @JsonProperty("meeting_id")
    private String meetingId;

    ReportResponse() {}

    /**
     * UUID of the report request.
     *
     * @return The request ID string, or {@code null} if not available.
     */
    public String getRequestId() {
        return requestId;
    }

    /**
     * Current processing status of the report.
     *
     * @return The report status enum value, or {@code null} if not available.
     */
    public ReportStatus getRequestStatus() {
        return requestStatus;
    }

    /**
     * Time at which the report request was received by Vonage.
     *
     * @return The receive time string, or {@code null} if not available.
     */
    public String getReceiveTime() {
        return receiveTime;
    }

    /**
     * Time at which processing of the report started.
     *
     * @return The start time string, or {@code null} if not available.
     */
    public String getStartTime() {
        return startTime;
    }

    /**
     * Total number of records in the report.
     *
     * @return The items count, or {@code null} if not available.
     */
    public Long getItemsCount() {
        return itemsCount;
    }

    /**
     * HAL links for the report. Typically contains {@code self} and {@code download_report} links.
     * The {@code download_report} link contains the URL to download the report file.
     *
     * @return The links map, or {@code null} if not available.
     */
    public Map<String, Map<String, String>> getLinks() {
        return links;
    }

    /**
     * Convenience method to get the download URL for the report.
     * Returns the {@code href} value from the {@code download_report} HAL link.
     *
     * @return The download URL string, or {@code null} if not available.
     */
    public String getDownloadUrl() {
        if (links == null) return null;
        Map<String, String> downloadReport = links.get("download_report");
        if (downloadReport == null) return null;
        return downloadReport.get("href");
    }

    /**
     * The product type for this report.
     *
     * @return The product enum value, or {@code null} if not available.
     */
    public Product getProduct() {
        return product;
    }

    /**
     * The account ID (API key) this report was created for.
     *
     * @return The account ID string, or {@code null} if not available.
     */
    public String getAccountId() {
        return accountId;
    }

    /**
     * Start date of the report range.
     *
     * @return The start date string, or {@code null} if not set.
     */
    public String getDateStart() {
        return dateStart;
    }

    /**
     * End date of the report range.
     *
     * @return The end date string, or {@code null} if not set.
     */
    public String getDateEnd() {
        return dateEnd;
    }

    /**
     * Whether sub-account data was included in this report.
     *
     * @return {@code true} if sub-accounts were included, or {@code null} if not set.
     */
    public Boolean getIncludeSubaccounts() {
        return includeSubaccounts;
    }

    /**
     * Webhook URL for report completion notifications.
     *
     * @return The callback URL, or {@code null} if not set.
     */
    public URI getCallbackUrl() {
        return callbackUrl;
    }

    /**
     * Direction filter applied to this report.
     *
     * @return The direction string, or {@code null} if not set.
     */
    public String getDirection() {
        return direction;
    }

    /**
     * Status filter applied to this report.
     *
     * @return The status string, or {@code null} if not set.
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sender filter applied to this report.
     *
     * @return The sender filter string, or {@code null} if not set.
     */
    public String getFrom() {
        return from;
    }

    /**
     * Recipient filter applied to this report.
     *
     * @return The recipient filter string, or {@code null} if not set.
     */
    public String getTo() {
        return to;
    }

    /**
     * Country filter applied to this report.
     *
     * @return The country code, or {@code null} if not set.
     */
    public String getCountry() {
        return country;
    }

    /**
     * Network filter applied to this report.
     *
     * @return The network code, or {@code null} if not set.
     */
    public String getNetwork() {
        return network;
    }

    /**
     * Client reference filter applied to this report.
     *
     * @return The client reference string, or {@code null} if not set.
     */
    public String getClientRef() {
        return clientRef;
    }

    /**
     * Account reference filter applied to this report.
     *
     * @return The account reference string, or {@code null} if not set.
     */
    public String getAccountRef() {
        return accountRef;
    }

    /**
     * Whether message body content was included in this report.
     *
     * @return {@code true} if message content was included, or {@code null} if not set.
     */
    public Boolean getIncludeMessage() {
        return includeMessage;
    }

    /**
     * Whether concatenation info was included in this report.
     *
     * @return {@code true} if concatenation info was included, or {@code null} if not set.
     */
    public Boolean getShowConcatenated() {
        return showConcatenated;
    }

    /**
     * Call identifier filter applied to this report.
     *
     * @return The call ID, or {@code null} if not set.
     */
    public String getCallId() {
        return callId;
    }

    /**
     * Conversation ID filter applied to this report.
     *
     * @return The conversation ID, or {@code null} if not set.
     */
    public String getConversationId() {
        return conversationId;
    }

    /**
     * Leg ID filter applied to this report.
     *
     * @return The leg ID, or {@code null} if not set.
     */
    public String getLegId() {
        return legId;
    }

    /**
     * Messaging provider filter applied to this report.
     *
     * @return The provider string, or {@code null} if not set.
     */
    public String getProvider() {
        return provider;
    }

    /**
     * Verification channel filter applied to this report.
     *
     * @return The channel string, or {@code null} if not set.
     */
    public String getChannel() {
        return channel;
    }

    /**
     * Parent request ID filter applied to this report.
     *
     * @return The parent request ID, or {@code null} if not set.
     */
    public String getParentRequestId() {
        return parentRequestId;
    }

    /**
     * Locale filter applied to this report.
     *
     * @return The locale string, or {@code null} if not set.
     */
    public String getLocale() {
        return locale;
    }

    /**
     * Phone number filter applied to this report.
     *
     * @return The phone number, or {@code null} if not set.
     */
    public String getNumber() {
        return number;
    }

    /**
     * Product name filter applied to this report.
     *
     * @return The product name, or {@code null} if not set.
     */
    public String getProductName() {
        return productName;
    }

    /**
     * Request type filter applied to this report.
     *
     * @return The request type, or {@code null} if not set.
     */
    public String getRequestType() {
        return requestType;
    }

    /**
     * Request session ID filter applied to this report.
     *
     * @return The request session ID, or {@code null} if not set.
     */
    public String getRequestSessionId() {
        return requestSessionId;
    }

    /**
     * Product path filter applied to this report.
     *
     * @return The product path, or {@code null} if not set.
     */
    public String getProductPath() {
        return productPath;
    }

    /**
     * Correlation ID filter applied to this report.
     *
     * @return The correlation ID, or {@code null} if not set.
     */
    public String getCorrelationId() {
        return correlationId;
    }

    /**
     * Video session ID filter applied to this report.
     *
     * @return The session ID, or {@code null} if not set.
     */
    public String getSessionId() {
        return sessionId;
    }

    /**
     * Meeting ID filter applied to this report.
     *
     * @return The meeting ID, or {@code null} if not set.
     */
    public String getMeetingId() {
        return meetingId;
    }
}
