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

import com.vonage.client.AbstractClientTest;
import static com.vonage.client.TestUtils.testJsonableBaseObject;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.net.URI;
import java.util.List;
import java.util.Map;

public class ReportsClientTest extends AbstractClientTest<ReportsClient> {

    static final String REPORT_ID = "aaaaaaaa-bbbb-cccc-dddd-0123456789ab";
    static final String FILE_ID = "bbbbbbbb-cccc-dddd-eeee-0123456789ab";
    static final String DOWNLOAD_URL = "https://api.nexmo.com/v3/media/" + FILE_ID;
    static final String ACCOUNT_ID = "12aa3456";

    static final String REPORT_RESPONSE_JSON = "{\n" +
            "  \"request_id\": \"" + REPORT_ID + "\",\n" +
            "  \"request_status\": \"PENDING\",\n" +
            "  \"receive_time\": \"2024-02-07T14:22:08+00:00\",\n" +
            "  \"start_time\": \"2024-02-07T14:22:10+00:00\",\n" +
            "  \"items_count\": 1523,\n" +
            "  \"product\": \"SMS\",\n" +
            "  \"account_id\": \"" + ACCOUNT_ID + "\",\n" +
            "  \"date_start\": \"2024-02-02T13:50:00+00:00\",\n" +
            "  \"date_end\": \"2024-02-07T14:22:08+00:00\",\n" +
            "  \"direction\": \"outbound\",\n" +
            "  \"_links\": {\n" +
            "    \"self\": {\"href\": \"https://api.nexmo.com/v2/reports/" + REPORT_ID + "\"},\n" +
            "    \"download_report\": {\"href\": \"https://api.nexmo.com/v3/media/" + FILE_ID + "\"}\n" +
            "  }\n" +
            "}";

    static final String SUCCESS_REPORT_RESPONSE_JSON = "{\n" +
            "  \"request_id\": \"" + REPORT_ID + "\",\n" +
            "  \"request_status\": \"SUCCESS\",\n" +
            "  \"items_count\": 50,\n" +
            "  \"product\": \"VOICE-CALL\",\n" +
            "  \"account_id\": \"" + ACCOUNT_ID + "\",\n" +
            "  \"_links\": {\n" +
            "    \"download_report\": {\"href\": \"https://api.nexmo.com/v3/media/" + FILE_ID + "\"}\n" +
            "  }\n" +
            "}";

    static final String RECORDS_RESPONSE_JSON = "{\n" +
            "  \"request_id\": \"a91b34c2-5d98-4c0e-8f23-a6b1c7d4e9f0\",\n" +
            "  \"request_status\": \"SUCCESS\",\n" +
            "  \"received_at\": \"2024-02-07T14:22:08+00:00\",\n" +
            "  \"items_count\": 2,\n" +
            "  \"product\": \"SMS\",\n" +
            "  \"cursor\": \"MTY0OTQ3ODAwMDAwMA\",\n" +
            "  \"iv\": \"8a2c4e6f-12d3-45b6-78c9-0a1b2c3d4e5f\",\n" +
            "  \"_links\": {\n" +
            "    \"self\": {\"href\": \"https://api.nexmo.com/v2/reports/sms/records\"},\n" +
            "    \"next\": {\"href\": \"https://api.nexmo.com/v2/reports/sms/records?cursor=MTY0OTQ3ODAwMDAwMA\"}\n" +
            "  },\n" +
            "  \"records\": [\n" +
            "    {\"message_id\": \"msg-1\", \"from\": \"447700900001\", \"to\": \"447700900000\", \"status\": \"delivered\"},\n" +
            "    {\"message_id\": \"msg-2\", \"from\": \"447700900001\", \"to\": \"447700900002\", \"status\": \"failed\"}\n" +
            "  ]\n" +
            "}";

    public ReportsClientTest() {
        client = new ReportsClient(wrapper);
    }

    // ========== Product enum tests ==========

    @Test
    public void testProductEnumValues() {
        assertEquals("SMS", Product.SMS.getValue());
        assertEquals("SMS-TRAFFIC-CONTROL", Product.SMS_TRAFFIC_CONTROL.getValue());
        assertEquals("VOICE-CALL", Product.VOICE_CALL.getValue());
        assertEquals("VOICE-FAILED", Product.VOICE_FAILED.getValue());
        assertEquals("VOICE-TTS", Product.VOICE_TTS.getValue());
        assertEquals("IN-APP-VOICE", Product.IN_APP_VOICE.getValue());
        assertEquals("WEBSOCKET-CALL", Product.WEBSOCKET_CALL.getValue());
        assertEquals("ASR", Product.ASR.getValue());
        assertEquals("AMD", Product.AMD.getValue());
        assertEquals("VERIFY-API", Product.VERIFY_API.getValue());
        assertEquals("VERIFY-V2", Product.VERIFY_V2.getValue());
        assertEquals("NUMBER-INSIGHT", Product.NUMBER_INSIGHT.getValue());
        assertEquals("CONVERSATION-EVENT", Product.CONVERSATION_EVENT.getValue());
        assertEquals("CONVERSATION-MESSAGE", Product.CONVERSATION_MESSAGE.getValue());
        assertEquals("MESSAGES", Product.MESSAGES.getValue());
        assertEquals("VIDEO-API", Product.VIDEO_API.getValue());
        assertEquals("NETWORK-API-EVENT", Product.NETWORK_API_EVENT.getValue());
        assertEquals("REPORTS-USAGE", Product.REPORTS_USAGE.getValue());
        assertEquals(18, Product.values().length);
    }

    @Test
    public void testProductFromValue() {
        assertEquals(Product.SMS, Product.fromValue("SMS"));
        assertEquals(Product.VOICE_CALL, Product.fromValue("VOICE-CALL"));
        assertEquals(Product.MESSAGES, Product.fromValue("messages"));
        assertNull(Product.fromValue("UNKNOWN"));
        assertNull(Product.fromValue(null));
    }

    // ========== ReportStatus enum tests ==========

    @Test
    public void testReportStatusFromValue() {
        assertEquals(ReportStatus.PENDING, ReportStatus.fromValue("PENDING"));
        assertEquals(ReportStatus.PROCESSING, ReportStatus.fromValue("processing"));
        assertEquals(ReportStatus.SUCCESS, ReportStatus.fromValue("SUCCESS"));
        assertEquals(ReportStatus.ABORTED, ReportStatus.fromValue("ABORTED"));
        assertEquals(ReportStatus.FAILED, ReportStatus.fromValue("FAILED"));
        assertEquals(ReportStatus.TRUNCATED, ReportStatus.fromValue("TRUNCATED"));
        assertNull(ReportStatus.fromValue("UNKNOWN"));
        assertNull(ReportStatus.fromValue(null));
    }

    // ========== AsyncReportRequest tests ==========

    @Test
    public void testAsyncReportRequestRequiredFields() {
        var request = AsyncReportRequest.builder(Product.SMS, ACCOUNT_ID).build();
        assertEquals(Product.SMS, request.getProduct());
        assertEquals(ACCOUNT_ID, request.getAccountId());
        String json = request.toJson();
        assertTrue(json.contains("\"product\":\"SMS\""));
        assertTrue(json.contains("\"account_id\":\"" + ACCOUNT_ID + "\""));
        // Optional fields should not be serialised
        assertFalse(json.contains("date_start"));
        assertFalse(json.contains("direction"));
    }

    @Test
    public void testAsyncReportRequestNullProduct() {
        assertThrows(NullPointerException.class, () ->
                AsyncReportRequest.builder(null, ACCOUNT_ID).build()
        );
    }

    @Test
    public void testAsyncReportRequestNullAccountId() {
        assertThrows(NullPointerException.class, () ->
                AsyncReportRequest.builder(Product.SMS, null).build()
        );
    }

    @Test
    public void testAsyncReportRequestEmptyAccountId() {
        assertThrows(IllegalArgumentException.class, () ->
                AsyncReportRequest.builder(Product.SMS, "  ").build()
        );
    }

    @Test
    public void testAsyncReportRequestAllFields() {
        var callbackUrl = URI.create("https://example.com/webhook");
        var request = AsyncReportRequest.builder(Product.MESSAGES, ACCOUNT_ID)
                .dateStart("2024-02-02T13:50:00+00:00")
                .dateEnd("2024-02-07T14:22:08+00:00")
                .includeSubaccounts(true)
                .callbackUrl(callbackUrl)
                .direction("outbound")
                .status("delivered")
                .from("447700900001")
                .to("447700900000")
                .country("GB")
                .network("23415")
                .clientRef("my-ref")
                .accountRef("acc-ref")
                .includeMessage(true)
                .showConcatenated(false)
                .callId("call-123")
                .conversationId("CON-abc123")
                .legId("leg-uuid")
                .provider("whatsapp")
                .channel("v2")
                .parentRequestId("parent-uuid")
                .locale("en-gb")
                .number("447700900000")
                .productName("camara-sim-swap")
                .requestType("check")
                .requestSessionId("sess-uuid")
                .productPath("/camara/sim-swap/v040/check")
                .correlationId("corr-uuid")
                .sessionId("sess-123")
                .meetingId("meet-123")
                .build();

        assertEquals(Product.MESSAGES, request.getProduct());
        assertEquals(ACCOUNT_ID, request.getAccountId());
        assertEquals("2024-02-02T13:50:00+00:00", request.getDateStart());
        assertEquals("2024-02-07T14:22:08+00:00", request.getDateEnd());
        assertTrue(request.getIncludeSubaccounts());
        assertEquals(callbackUrl, request.getCallbackUrl());
        assertEquals("outbound", request.getDirection());
        assertEquals("delivered", request.getStatus());
        assertEquals("447700900001", request.getFrom());
        assertEquals("447700900000", request.getTo());
        assertEquals("GB", request.getCountry());
        assertEquals("23415", request.getNetwork());
        assertEquals("my-ref", request.getClientRef());
        assertEquals("acc-ref", request.getAccountRef());
        assertTrue(request.getIncludeMessage());
        assertFalse(request.getShowConcatenated());
        assertEquals("call-123", request.getCallId());
        assertEquals("CON-abc123", request.getConversationId());
        assertEquals("leg-uuid", request.getLegId());
        assertEquals("whatsapp", request.getProvider());
        assertEquals("v2", request.getChannel());
        assertEquals("parent-uuid", request.getParentRequestId());
        assertEquals("en-gb", request.getLocale());
        assertEquals("447700900000", request.getNumber());
        assertEquals("camara-sim-swap", request.getProductName());
        assertEquals("check", request.getRequestType());
        assertEquals("sess-uuid", request.getRequestSessionId());
        assertEquals("/camara/sim-swap/v040/check", request.getProductPath());
        assertEquals("corr-uuid", request.getCorrelationId());
        assertEquals("sess-123", request.getSessionId());
        assertEquals("meet-123", request.getMeetingId());

        testJsonableBaseObject(request);
        String json = request.toJson();
        assertTrue(json.contains("\"product\":\"MESSAGES\""));
        assertTrue(json.contains("\"include_subaccounts\":true"));
        assertTrue(json.contains("\"callback_url\":\"https://example.com/webhook\""));
    }

    // ========== RecordsFilter tests ==========

    @Test
    public void testRecordsFilterRequiredFields() {
        var filter = RecordsFilter.builder(Product.SMS, ACCOUNT_ID).build();
        assertEquals(Product.SMS, filter.getProduct());
        assertEquals(ACCOUNT_ID, filter.getAccountId());
        var params = filter.makeParams();
        assertEquals("SMS", params.get("product"));
        assertEquals(ACCOUNT_ID, params.get("account_id"));
        assertFalse(params.containsKey("date_start"));
        assertFalse(params.containsKey("direction"));
    }

    @Test
    public void testRecordsFilterNullProduct() {
        assertThrows(NullPointerException.class, () ->
                RecordsFilter.builder(null, ACCOUNT_ID).build()
        );
    }

    @Test
    public void testRecordsFilterNullAccountId() {
        assertThrows(NullPointerException.class, () ->
                RecordsFilter.builder(Product.SMS, null).build()
        );
    }

    @Test
    public void testRecordsFilterEmptyAccountId() {
        assertThrows(IllegalArgumentException.class, () ->
                RecordsFilter.builder(Product.SMS, " ").build()
        );
    }

    @Test
    public void testRecordsFilterAllFields() {
        var filter = RecordsFilter.builder(Product.VOICE_CALL, ACCOUNT_ID)
                .dateStart("2024-02-02T13:50:00+00:00")
                .dateEnd("2024-02-07T14:22:08+00:00")
                .cursor("MTY0OTQ3ODAwMDAwMA")
                .iv("8a2c4e6f-12d3-45b6-78c9-0a1b2c3d4e5f")
                .id(REPORT_ID)
                .direction("outbound")
                .status("ANSWERED")
                .from("12345678912")
                .to("22345678912")
                .country("GB")
                .network("23415")
                .clientRef("my-ref")
                .accountRef("acc-ref")
                .includeMessage(true)
                .showConcatenated(false)
                .callId("dfc0c915f38ae6701d7d114cde2556b1-1")
                .conversationId("CON-abc123")
                .legId("leg-uuid")
                .provider("whatsapp")
                .channel("email")
                .parentRequestId("parent-uuid")
                .locale("en-gb")
                .number("447700900000")
                .numberType("mobile")
                .risk("low")
                .swapped(true)
                .productName("camara-sim-swap")
                .requestType("check")
                .requestSessionId("sess-uuid")
                .productPath("/camara/sim-swap")
                .correlationId("corr-uuid")
                .sessionId("sess-123")
                .meetingId("meet-123")
                .build();

        var params = filter.makeParams();
        assertEquals("VOICE-CALL", params.get("product"));
        assertEquals(ACCOUNT_ID, params.get("account_id"));
        assertEquals("2024-02-02T13:50:00+00:00", params.get("date_start"));
        assertEquals("MTY0OTQ3ODAwMDAwMA", params.get("cursor"));
        assertEquals("8a2c4e6f-12d3-45b6-78c9-0a1b2c3d4e5f", params.get("iv"));
        assertEquals(REPORT_ID, params.get("id"));
        assertEquals("outbound", params.get("direction"));
        assertEquals("ANSWERED", params.get("status"));
        assertEquals("12345678912", params.get("from"));
        assertEquals("22345678912", params.get("to"));
        assertEquals("GB", params.get("country"));
        assertEquals("23415", params.get("network"));
        assertEquals("my-ref", params.get("client_ref"));
        assertEquals("acc-ref", params.get("account_ref"));
        assertEquals("true", params.get("include_message"));
        assertEquals("false", params.get("show_concatenated"));
        assertEquals("dfc0c915f38ae6701d7d114cde2556b1-1", params.get("call_id"));
        assertEquals("CON-abc123", params.get("conversation_id"));
        assertEquals("leg-uuid", params.get("leg_id"));
        assertEquals("whatsapp", params.get("provider"));
        assertEquals("email", params.get("channel"));
        assertEquals("parent-uuid", params.get("parent_request_id"));
        assertEquals("en-gb", params.get("locale"));
        assertEquals("447700900000", params.get("number"));
        assertEquals("mobile", params.get("number_type"));
        assertEquals("low", params.get("risk"));
        assertEquals("true", params.get("swapped"));
        assertEquals("camara-sim-swap", params.get("product_name"));
        assertEquals("check", params.get("request_type"));
        assertEquals("sess-uuid", params.get("request_session_id"));
        assertEquals("/camara/sim-swap", params.get("product_path"));
        assertEquals("corr-uuid", params.get("correlation_id"));
        assertEquals("sess-123", params.get("session_id"));
        assertEquals("meet-123", params.get("meeting_id"));

        assertEquals("MTY0OTQ3ODAwMDAwMA", filter.getCursor());
        assertEquals("8a2c4e6f-12d3-45b6-78c9-0a1b2c3d4e5f", filter.getIv());
        assertEquals(REPORT_ID, filter.getId());
        assertEquals("mobile", filter.getNumberType());
        assertEquals("low", filter.getRisk());
        assertTrue(filter.getSwapped());
    }

    // ========== ReportResponse deserialization tests ==========

    @Test
    public void testReportResponseDeserialization() {
        var response = com.vonage.client.Jsonable.fromJson(REPORT_RESPONSE_JSON, ReportResponse.class);
        testJsonableBaseObject(response);
        assertEquals(REPORT_ID, response.getRequestId());
        assertEquals(ReportStatus.PENDING, response.getRequestStatus());
        assertEquals("2024-02-07T14:22:08+00:00", response.getReceiveTime());
        assertEquals("2024-02-07T14:22:10+00:00", response.getStartTime());
        assertEquals(1523L, response.getItemsCount());
        assertEquals(Product.SMS, response.getProduct());
        assertEquals(ACCOUNT_ID, response.getAccountId());
        assertEquals("2024-02-02T13:50:00+00:00", response.getDateStart());
        assertEquals("outbound", response.getDirection());
        assertNotNull(response.getLinks());
        assertEquals("https://api.nexmo.com/v3/media/" + FILE_ID, response.getDownloadUrl());
    }

    @Test
    public void testReportResponseDownloadUrlNullWhenNoLinks() {
        var response = new ReportResponse();
        assertNull(response.getDownloadUrl());
    }

    @Test
    public void testReportResponseSuccessStatus() {
        var response = com.vonage.client.Jsonable.fromJson(SUCCESS_REPORT_RESPONSE_JSON, ReportResponse.class);
        assertEquals(ReportStatus.SUCCESS, response.getRequestStatus());
        assertEquals(Product.VOICE_CALL, response.getProduct());
        assertEquals(50L, response.getItemsCount());
        assertEquals("https://api.nexmo.com/v3/media/" + FILE_ID, response.getDownloadUrl());
    }

    // ========== RecordsResponse deserialization tests ==========

    @Test
    public void testRecordsResponseDeserialization() {
        var response = com.vonage.client.Jsonable.fromJson(RECORDS_RESPONSE_JSON, RecordsResponse.class);
        testJsonableBaseObject(response);
        assertEquals("a91b34c2-5d98-4c0e-8f23-a6b1c7d4e9f0", response.getRequestId());
        assertEquals(ReportStatus.SUCCESS, response.getRequestStatus());
        assertEquals("2024-02-07T14:22:08+00:00", response.getReceivedAt());
        assertEquals(2L, response.getItemsCount());
        assertEquals(Product.SMS, response.getProduct());
        assertEquals("MTY0OTQ3ODAwMDAwMA", response.getCursor());
        assertEquals("8a2c4e6f-12d3-45b6-78c9-0a1b2c3d4e5f", response.getIv());

        List<Map<String, Object>> records = response.getRecords();
        assertNotNull(records);
        assertEquals(2, records.size());
        assertEquals("msg-1", records.get(0).get("message_id"));
        assertEquals("delivered", records.get(0).get("status"));
        assertEquals("msg-2", records.get(1).get("message_id"));
        assertEquals("failed", records.get(1).get("status"));

        assertNotNull(response.getLinks());
        assertNotNull(response.getLinks().get("next"));
    }

    // ========== ReportsClient method tests ==========

    @Test
    public void testGetRecords() throws Exception {
        stubResponse(200, RECORDS_RESPONSE_JSON);
        var filter = RecordsFilter.builder(Product.SMS, ACCOUNT_ID)
                .dateStart("2024-02-02T13:50:00+00:00")
                .build();
        var response = client.getRecords(filter);
        assertNotNull(response);
        assertEquals(ReportStatus.SUCCESS, response.getRequestStatus());
        assertEquals(2L, response.getItemsCount());
    }

    @Test
    public void testGetRecordsNullFilter() {
        assertThrows(NullPointerException.class, () -> client.getRecords(null));
    }

    @Test
    public void testGetRecords401() throws Exception {
        assertApiResponseException(401, "{\"title\":\"Unauthorized\"}", ReportsResponseException.class,
                () -> client.getRecords(RecordsFilter.builder(Product.SMS, ACCOUNT_ID).build())
        );
    }

    @Test
    public void testCreateReport() throws Exception {
        stubResponse(200, REPORT_RESPONSE_JSON);
        var request = AsyncReportRequest.builder(Product.SMS, ACCOUNT_ID)
                .direction("outbound")
                .dateStart("2024-02-02T13:50:00+00:00")
                .dateEnd("2024-02-07T14:22:08+00:00")
                .build();
        var response = client.createReport(request);
        assertNotNull(response);
        assertEquals(REPORT_ID, response.getRequestId());
        assertEquals(ReportStatus.PENDING, response.getRequestStatus());
    }

    @Test
    public void testCreateReportNullRequest() {
        assertThrows(NullPointerException.class, () -> client.createReport(null));
    }

    @Test
    public void testCreateReport401() throws Exception {
        assertApiResponseException(401, "{\"title\":\"Unauthorized\"}", ReportsResponseException.class,
                () -> client.createReport(AsyncReportRequest.builder(Product.SMS, ACCOUNT_ID).build())
        );
    }

    @Test
    public void testGetReport() throws Exception {
        stubResponse(200, SUCCESS_REPORT_RESPONSE_JSON);
        var response = client.getReport(REPORT_ID);
        assertNotNull(response);
        assertEquals(REPORT_ID, response.getRequestId());
        assertEquals(ReportStatus.SUCCESS, response.getRequestStatus());
    }

    @Test
    public void testGetReportNullId() {
        assertThrows(NullPointerException.class, () -> client.getReport(null));
    }

    @Test
    public void testGetReportEmptyId() {
        assertThrows(IllegalArgumentException.class, () -> client.getReport("  "));
    }

    @Test
    public void testGetReport404() throws Exception {
        assertApiResponseException(404, "{\"title\":\"Not Found\"}", ReportsResponseException.class,
                () -> client.getReport(REPORT_ID)
        );
    }

    @Test
    public void testCancelReport() throws Exception {
        stubResponse(200, REPORT_RESPONSE_JSON.replace("PENDING", "ABORTED"));
        var response = client.cancelReport(REPORT_ID);
        assertNotNull(response);
        assertEquals(REPORT_ID, response.getRequestId());
        assertEquals(ReportStatus.ABORTED, response.getRequestStatus());
    }

    @Test
    public void testCancelReportNullId() {
        assertThrows(NullPointerException.class, () -> client.cancelReport(null));
    }

    @Test
    public void testCancelReportEmptyId() {
        assertThrows(IllegalArgumentException.class, () -> client.cancelReport(""));
    }

    @Test
    public void testCancelReport409() throws Exception {
        assertApiResponseException(409, "{\"title\":\"Conflict\"}", ReportsResponseException.class,
                () -> client.cancelReport(REPORT_ID)
        );
    }

    @Test
    public void testDownloadReport() throws Exception {
        byte[] expectedBytes = "<BINARY_ZIP>".getBytes();
        stubResponse(200, new String(expectedBytes));
        assertArrayEquals(expectedBytes, client.downloadReport(DOWNLOAD_URL));
    }

    @Test
    public void testDownloadReportNullUrl() {
        assertThrows(IllegalArgumentException.class, () -> client.downloadReport(null));
    }

    @Test
    public void testDownloadReportEmptyUrl() {
        assertThrows(IllegalArgumentException.class, () -> client.downloadReport(""));
    }

    @Test
    public void testDownloadReportInvalidHost() {
        assertThrows(IllegalArgumentException.class, () ->
                client.downloadReport("https://evil.com/v3/media/" + FILE_ID)
        );
    }

    @Test
    public void testDownloadReport404() throws Exception {
        assertApiResponseException(404, "{\"title\":\"Not Found\"}", ReportsResponseException.class,
                () -> client.downloadReport(DOWNLOAD_URL)
        );
    }

    // ========== VonageClient integration test ==========

    @Test
    public void testVonageClientGetReportsClient() {
        var vonageClient = com.vonage.client.VonageClient.builder()
                .apiKey("key")
                .apiSecret("secret")
                .build();
        assertNotNull(vonageClient.getReportsClient());
    }
}
