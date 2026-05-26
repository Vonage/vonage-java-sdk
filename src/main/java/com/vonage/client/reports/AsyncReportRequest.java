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
import java.util.Objects;

/**
 * Request body for creating an asynchronous report ({@code POST /v2/reports}).
 * <p>
 * Build using {@link #builder(Product, String)}, providing the required {@code product} and
 * {@code account_id}. Optional parameters vary by product type — refer to the
 * <a href="https://developer.vonage.com/en/api/reports">Reports API documentation</a> for details.
 * </p>
 */
public class AsyncReportRequest extends JsonableBaseObject {

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

    // Product-specific fields (optional, null = excluded from JSON)

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

    private AsyncReportRequest() {}

    private AsyncReportRequest(Builder builder) {
        product = Objects.requireNonNull(builder.product, "Product is required.");
        accountId = Objects.requireNonNull(builder.accountId, "Account ID is required.");
        if (accountId.trim().isEmpty()) {
            throw new IllegalArgumentException("Account ID cannot be empty.");
        }
        dateStart = builder.dateStart;
        dateEnd = builder.dateEnd;
        includeSubaccounts = builder.includeSubaccounts;
        callbackUrl = builder.callbackUrl;
        direction = builder.direction;
        status = builder.status;
        from = builder.from;
        to = builder.to;
        country = builder.country;
        network = builder.network;
        clientRef = builder.clientRef;
        accountRef = builder.accountRef;
        includeMessage = builder.includeMessage;
        showConcatenated = builder.showConcatenated;
        callId = builder.callId;
        conversationId = builder.conversationId;
        legId = builder.legId;
        provider = builder.provider;
        channel = builder.channel;
        parentRequestId = builder.parentRequestId;
        locale = builder.locale;
        number = builder.number;
        productName = builder.productName;
        requestType = builder.requestType;
        requestSessionId = builder.requestSessionId;
        productPath = builder.productPath;
        correlationId = builder.correlationId;
        sessionId = builder.sessionId;
        meetingId = builder.meetingId;
    }

    /**
     * The product type for this report.
     *
     * @return The product enum value.
     */
    public Product getProduct() {
        return product;
    }

    /**
     * The account ID (API key) to report on.
     *
     * @return The account ID string.
     */
    public String getAccountId() {
        return accountId;
    }

    /**
     * Start date for the report in ISO-8601 format.
     *
     * @return The start date string, or {@code null} if not set.
     */
    public String getDateStart() {
        return dateStart;
    }

    /**
     * End date for the report in ISO-8601 format.
     *
     * @return The end date string, or {@code null} if not set.
     */
    public String getDateEnd() {
        return dateEnd;
    }

    /**
     * Whether to include sub-account data in the report.
     *
     * @return {@code true} if sub-accounts are included, or {@code null} if not set.
     */
    public Boolean getIncludeSubaccounts() {
        return includeSubaccounts;
    }

    /**
     * Webhook URL to receive a notification when the report is ready.
     *
     * @return The callback URL, or {@code null} if not set.
     */
    public URI getCallbackUrl() {
        return callbackUrl;
    }

    /**
     * Direction filter: {@code inbound} or {@code outbound}.
     *
     * @return The direction string, or {@code null} if not set.
     */
    public String getDirection() {
        return direction;
    }

    /**
     * Status filter for the records.
     *
     * @return The status string, or {@code null} if not set.
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sender ID or phone number filter.
     *
     * @return The sender filter string, or {@code null} if not set.
     */
    public String getFrom() {
        return from;
    }

    /**
     * Recipient phone number filter.
     *
     * @return The recipient filter string, or {@code null} if not set.
     */
    public String getTo() {
        return to;
    }

    /**
     * ISO two-letter country code filter.
     *
     * @return The country code, or {@code null} if not set.
     */
    public String getCountry() {
        return country;
    }

    /**
     * Mobile network code filter.
     *
     * @return The network code, or {@code null} if not set.
     */
    public String getNetwork() {
        return network;
    }

    /**
     * Custom client reference filter (SMS).
     *
     * @return The client reference string, or {@code null} if not set.
     */
    public String getClientRef() {
        return clientRef;
    }

    /**
     * Account reference filter (SMS).
     *
     * @return The account reference string, or {@code null} if not set.
     */
    public String getAccountRef() {
        return accountRef;
    }

    /**
     * Whether to include message body content in the report.
     *
     * @return {@code true} if message content is included, or {@code null} if not set.
     */
    public Boolean getIncludeMessage() {
        return includeMessage;
    }

    /**
     * Whether to include concatenation info in the report (SMS outbound only).
     *
     * @return {@code true} if concatenation info is included, or {@code null} if not set.
     */
    public Boolean getShowConcatenated() {
        return showConcatenated;
    }

    /**
     * Call identifier filter (VOICE-CALL, VOICE-FAILED, ASR, WEBSOCKET-CALL, AMD).
     *
     * @return The call ID, or {@code null} if not set.
     */
    public String getCallId() {
        return callId;
    }

    /**
     * Conversation ID filter (IN-APP-VOICE, CONVERSATION-EVENT, CONVERSATION-MESSAGE).
     *
     * @return The conversation ID, or {@code null} if not set.
     */
    public String getConversationId() {
        return conversationId;
    }

    /**
     * Leg ID filter (IN-APP-VOICE).
     *
     * @return The leg ID, or {@code null} if not set.
     */
    public String getLegId() {
        return legId;
    }

    /**
     * Messaging provider filter (MESSAGES).
     *
     * @return The provider string (e.g. {@code whatsapp}), or {@code null} if not set.
     */
    public String getProvider() {
        return provider;
    }

    /**
     * Verification channel filter (VERIFY-V2).
     *
     * @return The channel string, or {@code null} if not set.
     */
    public String getChannel() {
        return channel;
    }

    /**
     * Parent request ID filter to correlate v2 verification events (VERIFY-V2).
     *
     * @return The parent request ID, or {@code null} if not set.
     */
    public String getParentRequestId() {
        return parentRequestId;
    }

    /**
     * Language/locale filter (VERIFY-V2).
     *
     * @return The locale string, or {@code null} if not set.
     */
    public String getLocale() {
        return locale;
    }

    /**
     * Phone number filter for Number Insight reports.
     *
     * @return The phone number, or {@code null} if not set.
     */
    public String getNumber() {
        return number;
    }

    /**
     * Product name filter for Network API events (NETWORK-API-EVENT).
     *
     * @return The product name, or {@code null} if not set.
     */
    public String getProductName() {
        return productName;
    }

    /**
     * Request type filter for Network API events (NETWORK-API-EVENT).
     *
     * @return The request type, or {@code null} if not set.
     */
    public String getRequestType() {
        return requestType;
    }

    /**
     * Session ID filter for Network API events (NETWORK-API-EVENT).
     *
     * @return The request session ID, or {@code null} if not set.
     */
    public String getRequestSessionId() {
        return requestSessionId;
    }

    /**
     * API path filter for Network API events (NETWORK-API-EVENT).
     *
     * @return The product path, or {@code null} if not set.
     */
    public String getProductPath() {
        return productPath;
    }

    /**
     * Correlation ID filter for Network API events (NETWORK-API-EVENT).
     *
     * @return The correlation ID, or {@code null} if not set.
     */
    public String getCorrelationId() {
        return correlationId;
    }

    /**
     * Video session ID filter (VIDEO-API).
     *
     * @return The session ID, or {@code null} if not set.
     */
    public String getSessionId() {
        return sessionId;
    }

    /**
     * Meeting ID filter (VIDEO-API).
     *
     * @return The meeting ID, or {@code null} if not set.
     */
    public String getMeetingId() {
        return meetingId;
    }

    /**
     * Entry point for constructing an instance of this class.
     *
     * @param product   The product type for the report.
     * @param accountId The account ID (API key) to report on.
     *
     * @return A new Builder.
     */
    public static Builder builder(Product product, String accountId) {
        return new Builder(product, accountId);
    }

    /**
     * Builder for {@link AsyncReportRequest}.
     */
    public static class Builder {
        private final Product product;
        private final String accountId;
        private String dateStart, dateEnd, direction, status, from, to, country, network;
        private String clientRef, accountRef, callId, conversationId, legId, provider;
        private String channel, parentRequestId, locale, number;
        private String productName, requestType, requestSessionId, productPath, correlationId;
        private String sessionId, meetingId;
        private Boolean includeSubaccounts, includeMessage, showConcatenated;
        private URI callbackUrl;

        private Builder(Product product, String accountId) {
            this.product = product;
            this.accountId = accountId;
        }

        /**
         * Start date for the report in ISO-8601 format (e.g. {@code 2024-02-02T13:50:00+00:00}).
         * Defaults to seven days ago if not specified.
         *
         * @param dateStart The start date string.
         *
         * @return This builder.
         */
        public Builder dateStart(String dateStart) {
            this.dateStart = dateStart;
            return this;
        }

        /**
         * End date for the report in ISO-8601 format (e.g. {@code 2024-02-07T14:22:08+00:00}).
         * Defaults to the current time if not specified.
         *
         * @param dateEnd The end date string.
         *
         * @return This builder.
         */
        public Builder dateEnd(String dateEnd) {
            this.dateEnd = dateEnd;
            return this;
        }

        /**
         * Whether to include sub-account data in the report.
         *
         * @param includeSubaccounts {@code true} to include sub-account data.
         *
         * @return This builder.
         */
        public Builder includeSubaccounts(boolean includeSubaccounts) {
            this.includeSubaccounts = includeSubaccounts;
            return this;
        }

        /**
         * Webhook URL to receive a notification when the report is ready.
         *
         * @param callbackUrl The callback URL.
         *
         * @return This builder.
         */
        public Builder callbackUrl(URI callbackUrl) {
            this.callbackUrl = callbackUrl;
            return this;
        }

        /**
         * Direction filter: {@code inbound} or {@code outbound}.
         * Required for SMS and MESSAGES products.
         *
         * @param direction The direction string.
         *
         * @return This builder.
         */
        public Builder direction(String direction) {
            this.direction = direction;
            return this;
        }

        /**
         * Status filter for the records.
         *
         * @param status The status string.
         *
         * @return This builder.
         */
        public Builder status(String status) {
            this.status = status;
            return this;
        }

        /**
         * Sender ID or phone number filter.
         *
         * @param from The sender filter string.
         *
         * @return This builder.
         */
        public Builder from(String from) {
            this.from = from;
            return this;
        }

        /**
         * Recipient phone number filter.
         *
         * @param to The recipient filter string.
         *
         * @return This builder.
         */
        public Builder to(String to) {
            this.to = to;
            return this;
        }

        /**
         * ISO two-letter country code filter.
         *
         * @param country The country code.
         *
         * @return This builder.
         */
        public Builder country(String country) {
            this.country = country;
            return this;
        }

        /**
         * Mobile network code filter.
         *
         * @param network The network code.
         *
         * @return This builder.
         */
        public Builder network(String network) {
            this.network = network;
            return this;
        }

        /**
         * Custom client reference filter (SMS).
         *
         * @param clientRef The client reference string.
         *
         * @return This builder.
         */
        public Builder clientRef(String clientRef) {
            this.clientRef = clientRef;
            return this;
        }

        /**
         * Account reference filter (SMS).
         *
         * @param accountRef The account reference string.
         *
         * @return This builder.
         */
        public Builder accountRef(String accountRef) {
            this.accountRef = accountRef;
            return this;
        }

        /**
         * Whether to include message body content in the report (SMS, MESSAGES).
         *
         * @param includeMessage {@code true} to include message content.
         *
         * @return This builder.
         */
        public Builder includeMessage(boolean includeMessage) {
            this.includeMessage = includeMessage;
            return this;
        }

        /**
         * Whether to include concatenation info in the report (SMS outbound only).
         *
         * @param showConcatenated {@code true} to include concatenation info.
         *
         * @return This builder.
         */
        public Builder showConcatenated(boolean showConcatenated) {
            this.showConcatenated = showConcatenated;
            return this;
        }

        /**
         * Call identifier filter (VOICE-CALL, VOICE-FAILED, ASR, WEBSOCKET-CALL, AMD).
         *
         * @param callId The call ID.
         *
         * @return This builder.
         */
        public Builder callId(String callId) {
            this.callId = callId;
            return this;
        }

        /**
         * Conversation ID filter (IN-APP-VOICE, CONVERSATION-EVENT, CONVERSATION-MESSAGE).
         *
         * @param conversationId The conversation ID.
         *
         * @return This builder.
         */
        public Builder conversationId(String conversationId) {
            this.conversationId = conversationId;
            return this;
        }

        /**
         * Leg ID filter (IN-APP-VOICE).
         *
         * @param legId The leg ID.
         *
         * @return This builder.
         */
        public Builder legId(String legId) {
            this.legId = legId;
            return this;
        }

        /**
         * Messaging provider filter (MESSAGES).
         * Valid values: {@code whatsapp}, {@code sms}, {@code mms}, {@code messenger},
         * {@code viber_service_msg}, {@code instagram}, {@code rcs}.
         *
         * @param provider The provider string.
         *
         * @return This builder.
         */
        public Builder provider(String provider) {
            this.provider = provider;
            return this;
        }

        /**
         * Verification channel filter (VERIFY-V2).
         * Valid values: {@code v2}, {@code email}, {@code silent_auth}.
         *
         * @param channel The channel string.
         *
         * @return This builder.
         */
        public Builder channel(String channel) {
            this.channel = channel;
            return this;
        }

        /**
         * Parent request ID filter to correlate v2 verification events (VERIFY-V2).
         *
         * @param parentRequestId The parent request ID.
         *
         * @return This builder.
         */
        public Builder parentRequestId(String parentRequestId) {
            this.parentRequestId = parentRequestId;
            return this;
        }

        /**
         * Language/locale filter (VERIFY-V2).
         *
         * @param locale The locale string.
         *
         * @return This builder.
         */
        public Builder locale(String locale) {
            this.locale = locale;
            return this;
        }

        /**
         * Phone number filter for Number Insight reports.
         *
         * @param number The phone number.
         *
         * @return This builder.
         */
        public Builder number(String number) {
            this.number = number;
            return this;
        }

        /**
         * Product name filter for Network API events (NETWORK-API-EVENT).
         *
         * @param productName The product name (e.g. {@code camara-sim-swap}).
         *
         * @return This builder.
         */
        public Builder productName(String productName) {
            this.productName = productName;
            return this;
        }

        /**
         * Request type filter for Network API events (NETWORK-API-EVENT).
         *
         * @param requestType The request type.
         *
         * @return This builder.
         */
        public Builder requestType(String requestType) {
            this.requestType = requestType;
            return this;
        }

        /**
         * Session ID filter for Network API events (NETWORK-API-EVENT).
         *
         * @param requestSessionId The session ID.
         *
         * @return This builder.
         */
        public Builder requestSessionId(String requestSessionId) {
            this.requestSessionId = requestSessionId;
            return this;
        }

        /**
         * API path filter for Network API events (NETWORK-API-EVENT).
         *
         * @param productPath The product path.
         *
         * @return This builder.
         */
        public Builder productPath(String productPath) {
            this.productPath = productPath;
            return this;
        }

        /**
         * Correlation ID filter for Network API events (NETWORK-API-EVENT).
         *
         * @param correlationId The correlation ID.
         *
         * @return This builder.
         */
        public Builder correlationId(String correlationId) {
            this.correlationId = correlationId;
            return this;
        }

        /**
         * Video session ID filter (VIDEO-API).
         *
         * @param sessionId The session ID.
         *
         * @return This builder.
         */
        public Builder sessionId(String sessionId) {
            this.sessionId = sessionId;
            return this;
        }

        /**
         * Meeting ID filter (VIDEO-API).
         *
         * @param meetingId The meeting ID.
         *
         * @return This builder.
         */
        public Builder meetingId(String meetingId) {
            this.meetingId = meetingId;
            return this;
        }

        /**
         * Builds the {@link AsyncReportRequest}.
         *
         * @return A new AsyncReportRequest instance.
         */
        public AsyncReportRequest build() {
            return new AsyncReportRequest(this);
        }
    }
}
