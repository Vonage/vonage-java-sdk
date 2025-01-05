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
package com.vonage.client.verify;

import com.vonage.client.AbstractClientTest;
import com.vonage.client.RestEndpoint;
import com.vonage.client.VonageResponseParseException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class VerifyClientSearchEndpointTest extends AbstractClientTest<VerifyClient> {

    public VerifyClientSearchEndpointTest() {
        client = new VerifyClient(wrapper);
    }

    @Test
    public void testSearchSuccessWithSingleResult() throws Exception {
        //language=JSON
        String json = "{\n" + "  \"request_id\": \"not-a-search-request-id\",\n"
                + "  \"account_id\": \"not-an-account-id\",\n" + "  \"number\": \"447700900999\",\n"
                + "  \"sender_id\": \"447700900991\",\n" + "  \"date_submitted\": \"2014-03-04 10:11:12\",\n"
                + "  \"date_finalized\": \"2015-02-03 07:08:09\",\n" + "  \"checks\": [\n" + "    {\n"
                + "      \"date_received\": \"2012-01-02 03:04:05\",\n" + "      \"code\": \"code\",\n"
                + "      \"status\": \"VALID\",\n" + "      \"ip_address\": \"\"\n" + "    },\n" + "    {\n"
                + "      \"date_received\": \"2012-01-02 03:04:05\",\n" + "      \"code\": \"code\",\n"
                + "      \"status\": \"VALID\",\n" + "      \"ip_address\": \"\"\n" + "    }\n" + "  ],\n"
                + "  \"first_event_date\": \"2012-01-02 03:04:05\",\n"
                + "  \"last_event_date\": \"2012-01-02 03:04:06\",\n" + "  \"price\": \"0.03000000\",\n"
                + "  \"currency\": \"EUR\",\n" + "  \"status\": \"SUCCESS\",\n"
                + "  \"estimated_price_messages_sent\": \"0.03330000\"\n}";
        stubResponse(200, json);

        SearchVerifyResponse response = client.search("not-a-search-request-id");
        assertEquals(VerifyStatus.OK, response.getStatus());
        assertNull(response.getErrorText());
        List<VerifyDetails> requests = response.getVerificationRequests();

        assertEquals(1, requests.size());

        VerifyDetails details = requests.get(0);

        assertEquals("not-an-account-id", details.getAccountId());
        assertEquals("EUR", details.getCurrency());
        assertEquals(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2015-02-03 07:08:09"),
                details.getDateFinalized()
        );
        assertEquals(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2014-03-04 10:11:12"),
                details.getDateSubmitted()
        );
        assertEquals(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2012-01-02 03:04:05"),
                details.getFirstEventDate()
        );
        assertEquals(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2012-01-02 03:04:06"),
                details.getLastEventDate()
        );
        assertEquals("447700900999", details.getNumber());
        assertEquals(new BigDecimal("0.03000000"), details.getPrice());
        assertEquals("not-a-search-request-id", details.getRequestId());
        assertEquals("447700900991", details.getSenderId());
        assertEquals(VerifyDetails.Status.SUCCESS, details.getStatus());
        assertEquals(0.03330000, details.getEstimatedPriceMessagesSent().doubleValue(), 0.001);

        List<VerifyCheck> checks = details.getChecks();

        assertEquals(2, checks.size());

        for (VerifyCheck check : checks) {
            assertEquals("code", check.getCode());
            assertEquals(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2012-01-02 03:04:05"), check.getDate());
            assertEquals(VerifyCheck.Status.VALID, check.getStatus());
        }
    }

    @Test
    public void testSearchError() throws Exception {
        //language=JSON
        String json = "{\n" + "  \"request_id\": \"\",\n" + "  \"status\": \"16\",\n"
                + "  \"error_text\": \"The code inserted does not match the expected value.\"\n" + "}";
        stubResponse(200, json);

        SearchVerifyResponse response = client.search("AAAAA");
        assertEquals(VerifyStatus.INVALID_CODE, response.getStatus());
    }

    @Test
    public void testSearchFailed() throws Exception {
        //language=JSON
        String json = "{\n" + "  \"request_id\": \"a-random-request-id\",\n" + "  \"account_id\": \"account-id\",\n"
                + "  \"number\": \"not-a-number\",\n" + "  \"sender_id\": \"verify\",\n"
                + "  \"date_submitted\": \"2016-10-19 11:18:56\",\n"
                + "  \"date_finalized\": \"2016-10-19 11:20:00\",\n" + "  \"checks\": [],\n"
                + "  \"first_event_date\": \"2016-10-19 11:18:56\",\n"
                + "  \"last_event_date\": \"2016-10-19 11:18:56\",\n" + "  \"price\": \"0.03000000\",\n"
                + "  \"currency\": \"EUR\",\n" + "  \"status\": \"FAILED\"\n" + "}";
        stubResponse(200, json);

        SearchVerifyResponse response = client.search("AAAAA");
        assertEquals(VerifyStatus.OK, response.getStatus());

        VerifyDetails details = response.getVerificationRequests().get(0);
        assertEquals(VerifyDetails.Status.FAILED, details.getStatus());
    }

    @Test
    public void testSearchExpired() throws Exception {
        String json = "{\n" + "  \"request_id\": \"a-random-request-id\",\n" + "  \"account_id\": \"account-id\",\n"
                + "  \"number\": \"not-a-number\",\n" + "  \"sender_id\": \"verify\",\n"
                + "  \"date_submitted\": \"2016-10-19 11:25:19\",\n"
                + "  \"date_finalized\": \"2016-10-19 11:35:27\",\n" + "  \"checks\": [],\n"
                + "  \"first_event_date\": \"2016-10-19 11:25:19\",\n"
                + "  \"last_event_date\": \"2016-10-19 11:30:26\",\n" + "  \"price\": \"0\",\n"
                + "  \"currency\": \"EUR\",\n" + "  \"status\": \"EXPIRED\"\n" + "}\n";
        stubResponse(200, json);

        SearchVerifyResponse response = client.search("AAAAA");
        assertEquals(VerifyStatus.OK, response.getStatus());

        VerifyDetails details = response.getVerificationRequests().get(0);
        assertEquals(VerifyDetails.Status.EXPIRED, details.getStatus());
    }

    @Test
    public void testSearchInProgress() throws Exception {
        String json = "{\n" + "  \"request_id\": \"a-random-request-id\",\n" + "  \"account_id\": \"account-id\",\n"
                + "  \"number\": \"not-a-number\",\n" + "  \"sender_id\": \"verify\",\n"
                + "  \"date_submitted\": \"2016-10-19 11:25:19\",\n" + "  \"date_finalized\": \"\",\n"
                + "  \"checks\": [],\n" + "  \"first_event_date\": \"2016-10-19 11:25:19\",\n"
                + "  \"last_event_date\": \"2016-10-19 11:30:26\",\n" + "  \"price\": \"0.10000000\",\n"
                + "  \"currency\": \"EUR\",\n" + "  \"status\": \"IN PROGRESS\"\n" + "}\n";
        stubResponse(200, json);

        SearchVerifyResponse response = client.search("AAAAA");
        assertEquals(VerifyStatus.OK, response.getStatus());

        VerifyDetails details = response.getVerificationRequests().get(0);
        assertEquals(VerifyDetails.Status.IN_PROGRESS, details.getStatus());
    }

    @Test
    public void testSearchWithWrongParams() throws Exception {
        String json = "{\n" + "  \"request_id\": \"\",\n" + "  \"status\": 6,\n"
                + "  \"error_text\": \"The Vonage platform was unable to process this message for the following reason: Wrong parameters.\"\n"
                + "}";
        stubResponse(200, json);

        SearchVerifyResponse response = client.search("AAAAA");
        assertEquals(VerifyStatus.INVALID_REQUEST, response.getStatus());
    }

    @Test
    public void testSearchWithMultipleResults() throws Exception {
        String json = "{\n" + "  \"verification_requests\": [\n" + "    { \n"
                + "      \"request_id\": \"a-random-request-id\",\n" + "      \"account_id\": \"abcde\",\n"
                + "      \"number\": \"447700900999\",\n" + "      \"sender_id\": \"verify\",\n"
                + "      \"date_submitted\": \"2016-10-21 15:41:02\",\n" + "      \"date_finalized\": \"\",\n"
                + "      \"checks\": [],\n" + "      \"first_event_date\": \"2016-10-21 15:41:02\",\n"
                + "      \"last_event_date\": \"2016-10-21 15:43:07\",\n" + "      \"price\": \"0.10000000\",\n"
                + "      \"currency\": \"EUR\",\n" + "      \"status\": \"IN PROGRESS\"\n" + "    },\n" + "    { \n"
                + "      \"request_id\": \"another-random-request-id\",\n" + "      \"account_id\": \"fghij\",\n"
                + "      \"number\": \"447700900991\",\n" + "      \"sender_id\": \"verify2\",\n"
                + "      \"date_submitted\": \"2016-10-21 15:41:58\",\n" + "      \"date_finalized\": \"\",\n"
                + "      \"checks\": [],\n" + "      \"first_event_date\": \"2016-10-21 15:41:58\",\n"
                + "      \"last_event_date\": \"2016-10-21 15:41:58\",\n" + "      \"price\": \"0.10000000\",\n"
                + "      \"currency\": \"EUR\",\n" + "      \"status\": \"EXPIRED\"\n" + "    }\n" + "  ]\n" + "}";
        stubResponse(200, json);

        SearchVerifyResponse response = client.search("not-a-search-request-id");
        assertEquals(VerifyStatus.OK, response.getStatus());
        assertNull(response.getErrorText());
        List<VerifyDetails> requests = response.getVerificationRequests();

        assertEquals(2, requests.size());

        VerifyDetails first = requests.get(0);
        VerifyDetails second = requests.get(1);

        assertEquals("abcde", first.getAccountId());
        assertEquals("EUR", first.getCurrency());
        assertNull(first.getDateFinalized());
        assertEquals(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2016-10-21 15:41:02"),
                first.getDateSubmitted()
        );
        assertEquals(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2016-10-21 15:41:02"),
                first.getFirstEventDate()
        );
        assertEquals(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2016-10-21 15:43:07"),
                first.getLastEventDate()
        );
        assertEquals("447700900999", first.getNumber());
        assertEquals(new BigDecimal("0.10000000"), first.getPrice());
        assertEquals("a-random-request-id", first.getRequestId());
        assertEquals("verify", first.getSenderId());
        assertEquals(VerifyDetails.Status.IN_PROGRESS, first.getStatus());

        assertEquals("fghij", second.getAccountId());
        assertEquals("EUR", second.getCurrency());
        assertNull(second.getDateFinalized());
        assertEquals(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2016-10-21 15:41:58"),
                second.getDateSubmitted()
        );
        assertEquals(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2016-10-21 15:41:58"),
                second.getFirstEventDate()
        );
        assertEquals(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2016-10-21 15:41:58"),
                second.getLastEventDate()
        );
        assertEquals("447700900991", second.getNumber());
        assertEquals(new BigDecimal("0.10000000"), second.getPrice());
        assertEquals("another-random-request-id", second.getRequestId());
        assertEquals("verify2", second.getSenderId());
        assertEquals(VerifyDetails.Status.EXPIRED, second.getStatus());
    }

    @Test
    public void testSearchMultipleRequests() throws Exception {
        String accountId = "abcde";
        String json = "{\"verification_requests\":[{" +
                "\"request_id\":\"request-id-1\"," +
                "\"account_id\": \"abcde1\"},{" +
                "\"request_id\":\"request-id-2\"," +
                "\"account_id\": \"abcde2\"},{" +
                "\"request_id\":\"request-id-3\"," +
                "\"account_id\": \"abcde3\"}]}";

        stubResponse(200, json);
        SearchVerifyResponse response = client.search("request-id-1", "request-id-2", "request-id-3");
        List<VerifyDetails> requests = response.getVerificationRequests();
        assertEquals(3, requests.size());
        assertEquals(accountId+"1", requests.get(0).getAccountId());
        assertEquals(accountId+"2", requests.get(1).getAccountId());
        assertEquals(accountId+"3", requests.get(2).getAccountId());
    }

    @Test
    public void testNoSearchRequests() throws Exception {
        assertThrows(IllegalArgumentException.class, client::search);
    }

    @Test
    public void testTooManySearchRequests() throws Exception {
        String[] requestIds = new String[11];
        for (int i = 1; i <= requestIds.length; i++) {
            requestIds[i-1] = "request-id-"+i;
        }
        assertThrows(IllegalArgumentException.class, () -> client.search(requestIds));
    }

    @Test
    public void testSearchInvalidDates() throws Exception {
        String json = "    { \n" + "      \"request_id\": \"a-random-request-id\",\n"
                + "      \"account_id\": \"account-id\",\n" + "      \"number\": \"not-a-number\",\n"
                + "      \"sender_id\": \"verify\",\n" + "      \"date_submitted\": \"aaa\",\n"
                + "      \"date_finalized\": \"ddd\",\n" + "      \"checks\": [],\n"
                + "      \"first_event_date\": \"bbb\",\n" + "      \"last_event_date\": \"ccc\",\n"
                + "      \"price\": \"0.10000000\",\n" + "      \"currency\": \"EUR\",\n"
                + "      \"status\": \"SUCCESS\"\n" + "    }";

        stubResponse(200, json);
        assertThrows(VonageResponseParseException.class, () -> client.search("a-random-request-id"));
    }

    @Test
    public void testEndpoint() throws Exception {
        new VerifyEndpointTestSpec<SearchRequest, SearchVerifyResponse>() {

            @Override
            protected RestEndpoint<SearchRequest, SearchVerifyResponse> endpoint() {
                return client.search;
            }

            @Override
            protected String expectedEndpointUri(SearchRequest request) {
                return "/verify/search/json";
            }

            @Override
            protected SearchRequest sampleRequest() {
                return new SearchRequest("ab2c34e5d", "9f87b5");
            }

            @Override
            protected Map<String, ?> sampleQueryParams() {
                return Collections.singletonMap("request_ids", sampleRequest().getRequestIds());
            }

            @Override
            public void runTests() throws Exception {
                super.runTests();
                testSingleId();
            }

            void testSingleId() throws Exception {
                String id = UUID.randomUUID().toString().replace("-", "");
                assertRequestParams(Collections.singletonMap("request_id", id), new SearchRequest(id));
            }
        }
        .runTests();
    }
}
