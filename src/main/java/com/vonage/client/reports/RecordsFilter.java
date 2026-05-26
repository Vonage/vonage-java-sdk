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

import com.vonage.client.AbstractQueryParamsRequest;
import java.util.Map;
import java.util.Objects;

/**
 * Query parameters for synchronously retrieving report records ({@code GET /v2/reports/records}).
 * <p>
 * Build using {@link #builder(Product, String)}, providing the required {@code product} and
 * {@code account_id}. Use either date-based queries ({@link Builder#dateStart}/{@link Builder#dateEnd})
 * or ID-based queries ({@link Builder#id}) — when using ID queries, only {@code product},
 * {@code account_id}, {@code direction}, {@code id}, {@code includeMessage} and
 * {@code showConcatenated} are allowed.
 * </p>
 */
public class RecordsFilter extends AbstractQueryParamsRequest {

    private final Product product;
    private final String accountId;
    private final String dateStart, dateEnd, cursor, iv, id, direction, status;
    private final String from, to, country, network, clientRef, accountRef;
    private final String callId, conversationId, legId, provider, channel, parentRequestId, locale;
    private final String number, productName, requestType, requestSessionId, productPath, correlationId;
    private final String sessionId, meetingId, numberType, risk;
    private final Boolean includeMessage, showConcatenated, swapped;

    private RecordsFilter(Builder builder) {
        product = Objects.requireNonNull(builder.product, "Product is required.");
        accountId = Objects.requireNonNull(builder.accountId, "Account ID is required.");
        if (accountId.trim().isEmpty()) {
            throw new IllegalArgumentException("Account ID cannot be empty.");
        }
        dateStart = builder.dateStart;
        dateEnd = builder.dateEnd;
        cursor = builder.cursor;
        iv = builder.iv;
        id = builder.id;
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
        numberType = builder.numberType;
        risk = builder.risk;
        swapped = builder.swapped;
        productName = builder.productName;
        requestType = builder.requestType;
        requestSessionId = builder.requestSessionId;
        productPath = builder.productPath;
        correlationId = builder.correlationId;
        sessionId = builder.sessionId;
        meetingId = builder.meetingId;
    }

    @Override
    public Map<String, String> makeParams() {
        Map<String, String> params = super.makeParams();
        conditionalAdd("product", product);
        conditionalAdd("account_id", accountId);
        conditionalAdd("date_start", dateStart);
        conditionalAdd("date_end", dateEnd);
        conditionalAdd("cursor", cursor);
        conditionalAdd("iv", iv);
        conditionalAdd("id", id);
        conditionalAdd("direction", direction);
        conditionalAdd("status", status);
        conditionalAdd("from", from);
        conditionalAdd("to", to);
        conditionalAdd("country", country);
        conditionalAdd("network", network);
        conditionalAdd("client_ref", clientRef);
        conditionalAdd("account_ref", accountRef);
        conditionalAdd("include_message", includeMessage);
        conditionalAdd("show_concatenated", showConcatenated);
        conditionalAdd("call_id", callId);
        conditionalAdd("conversation_id", conversationId);
        conditionalAdd("leg_id", legId);
        conditionalAdd("provider", provider);
        conditionalAdd("channel", channel);
        conditionalAdd("parent_request_id", parentRequestId);
        conditionalAdd("locale", locale);
        conditionalAdd("number", number);
        conditionalAdd("number_type", numberType);
        conditionalAdd("risk", risk);
        conditionalAdd("swapped", swapped);
        conditionalAdd("product_name", productName);
        conditionalAdd("request_type", requestType);
        conditionalAdd("request_session_id", requestSessionId);
        conditionalAdd("product_path", productPath);
        conditionalAdd("correlation_id", correlationId);
        conditionalAdd("session_id", sessionId);
        conditionalAdd("meeting_id", meetingId);
        return params;
    }

    /**
     * The product type for this records query.
     *
     * @return The product enum value.
     */
    public Product getProduct() {
        return product;
    }

    /**
     * The account ID (API key) being queried.
     *
     * @return The account ID string.
     */
    public String getAccountId() {
        return accountId;
    }

    /**
     * Start date for the query in ISO-8601 format.
     *
     * @return The start date string, or {@code null} if not set.
     */
    public String getDateStart() {
        return dateStart;
    }

    /**
     * End date for the query in ISO-8601 format.
     *
     * @return The end date string, or {@code null} if not set.
     */
    public String getDateEnd() {
        return dateEnd;
    }

    /**
     * Pagination cursor for retrieving the next page of results (date-based queries only).
     *
     * @return The cursor string, or {@code null} if not set.
     */
    public String getCursor() {
        return cursor;
    }

    /**
     * Initialization vector for encrypted cursor pagination (date-based queries only).
     *
     * @return The IV string, or {@code null} if not set.
     */
    public String getIv() {
        return iv;
    }

    /**
     * UUID(s) of specific messages or calls to retrieve (comma-separated, maximum 20).
     *
     * @return The ID string, or {@code null} if not set.
     */
    public String getId() {
        return id;
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
     * @return The provider string, or {@code null} if not set.
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
     * Parent request ID filter (VERIFY-V2).
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
     * Phone number filter (NUMBER-INSIGHT).
     *
     * @return The phone number, or {@code null} if not set.
     */
    public String getNumber() {
        return number;
    }

    /**
     * Number type filter (VERIFY-API).
     *
     * @return The number type, or {@code null} if not set.
     */
    public String getNumberType() {
        return numberType;
    }

    /**
     * Risk assessment level filter.
     *
     * @return The risk level, or {@code null} if not set.
     */
    public String getRisk() {
        return risk;
    }

    /**
     * Whether the number has been ported/swapped filter.
     *
     * @return {@code true} if filtering for swapped numbers, or {@code null} if not set.
     */
    public Boolean getSwapped() {
        return swapped;
    }

    /**
     * Product name for Network API event filter (NETWORK-API-EVENT).
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
     * @param product   The product type to retrieve records for.
     * @param accountId The account ID (API key) to query.
     *
     * @return A new Builder.
     */
    public static Builder builder(Product product, String accountId) {
        return new Builder(product, accountId);
    }

    /**
     * Builder for {@link RecordsFilter}.
     */
    public static class Builder {
        private final Product product;
        private final String accountId;
        private String dateStart, dateEnd, cursor, iv, id, direction, status;
        private String from, to, country, network, clientRef, accountRef;
        private String callId, conversationId, legId, provider, channel, parentRequestId, locale;
        private String number, numberType, risk;
        private String productName, requestType, requestSessionId, productPath, correlationId;
        private String sessionId, meetingId;
        private Boolean includeMessage, showConcatenated, swapped;

        private Builder(Product product, String accountId) {
            this.product = product;
            this.accountId = accountId;
        }

        /**
         * Start date for the query in ISO-8601 format (e.g. {@code 2024-02-02T13:50:00+00:00}).
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
         * End date for the query in ISO-8601 format (e.g. {@code 2024-02-07T14:22:08+00:00}).
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
         * Pagination cursor for retrieving the next page of results (date-based queries only).
         *
         * @param cursor The cursor from a previous response.
         *
         * @return This builder.
         */
        public Builder cursor(String cursor) {
            this.cursor = cursor;
            return this;
        }

        /**
         * Initialization vector for encrypted cursor pagination (date-based queries only).
         *
         * @param iv The IV from a previous response.
         *
         * @return This builder.
         */
        public Builder iv(String iv) {
            this.iv = iv;
            return this;
        }

        /**
         * UUID(s) of specific messages or calls to retrieve.
         * You can specify a comma-separated list of up to 20 UUIDs.
         * When set, only {@code product}, {@code account_id}, {@code direction},
         * {@code include_message} and {@code show_concatenated} are also applicable.
         *
         * @param id The UUID or comma-separated list of UUIDs.
         *
         * @return This builder.
         */
        public Builder id(String id) {
            this.id = id;
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
         * Parent request ID filter (VERIFY-V2).
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
         * Phone number filter (NUMBER-INSIGHT).
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
         * Number type filter (VERIFY-API).
         *
         * @param numberType The number type.
         *
         * @return This builder.
         */
        public Builder numberType(String numberType) {
            this.numberType = numberType;
            return this;
        }

        /**
         * Risk assessment level filter.
         *
         * @param risk The risk level.
         *
         * @return This builder.
         */
        public Builder risk(String risk) {
            this.risk = risk;
            return this;
        }

        /**
         * Filter for ported/swapped numbers.
         *
         * @param swapped {@code true} to filter for swapped numbers.
         *
         * @return This builder.
         */
        public Builder swapped(boolean swapped) {
            this.swapped = swapped;
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
         * Builds the {@link RecordsFilter}.
         *
         * @return A new RecordsFilter instance.
         */
        public RecordsFilter build() {
            return new RecordsFilter(this);
        }
    }
}
