/*
 *   Copyright 2024 Vonage
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
package com.vonage.client.numbers;

import com.vonage.client.AbstractClientTest;
import com.vonage.client.RestEndpoint;
import static com.vonage.client.TestUtils.*;
import com.vonage.client.common.HttpMethod;
import com.vonage.client.numbers.UpdateNumberRequest.CallbackType;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.function.Executable;
import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

public class NumbersClientTest extends AbstractClientTest<NumbersClient> {
    private static final String MSISDN = "447700900000", COUNTRY = "GB";

    public NumbersClientTest() {
        client = new NumbersClient(wrapper);
    }

    private void assert401ResponseException(Executable invocation) throws Exception {
        String response = """
                {
                   "error-code": "401",
                   "error-code-label": "authentication failed"
                }""";
        NumbersResponseException ex = assertApiResponseException(
                401, response, NumbersResponseException.class, invocation
        );
        assertEquals("authentication failed", ex.getErrorCodeLabel());
    }

    private void stubBaseSuccessResponse() throws Exception {
        stubResponse(200, """
            {
              "error-code": "200",
              "error-code-label": "success"
            }"""
        );
    }

    private void assertEqualsSampleListNumbers(Supplier<ListNumbersResponse> invocation) throws Exception {
        int count = 127;
        String moHttpUrl = "https://example.com/mo", voiceCallbackValue = "sip:nexmo@example.com ",
                json = "{\n" +
                        "  \"count\": "+count+",\n" +
                        "  \"numbers\": [{\"features\": null},\n" +
                        "    {\n" +
                        "      \"country\": \""+COUNTRY+"\",\n" +
                        "      \"msisdn\": \""+MSISDN+"\",\n" +
                        "      \"moHttpUrl\": \""+moHttpUrl+"\",\n" +
                        "      \"type\": \"mobile-lvn\",\n" +
                        "      \"features\": [\n" +
                        "        \"SMS\",\n" +
                        "        \"MMS\"\n" +
                        "      ],\n" +
                        "      \"messagesCallbackType\": \"app\",\n" +
                        "      \"messagesCallbackValue\": \""+ APPLICATION_ID_STR+"\",\n" +
                        "      \"voiceCallbackType\": \"sip\",\n" +
                        "      \"voiceCallbackValue\": \""+voiceCallbackValue+"\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "       \"type\": \"landline-toll-free\",\n" +
                        "       \"features\": []\n" +
                        "    }\n" +
                        "  ]\n" +
                        "}";

        stubResponse(200, json);

        var parsed = invocation.get();
        testJsonableBaseObject(parsed);
        assertEquals(parsed, ListNumbersResponse.fromJson(json));

        assertEquals(count, parsed.getCount());
        var numbers = parsed.getNumbers();
        assertNotNull(numbers);
        assertEquals(3, numbers.length);
        assertNotNull(numbers[0]);
        assertNull(numbers[0].getFeatures());
        var main = numbers[1];
        testJsonableBaseObject(main);
        assertEquals(COUNTRY, main.getCountry());
        assertEquals(MSISDN, main.getMsisdn());
        // TODO change to URI
        assertEquals(URI.create(moHttpUrl).toString(), main.getMoHttpUrl());
        // TODO change to enum
        assertEquals(Type.MOBILE_LVN, Type.fromString(main.getType()));
        var features = main.getFeatures();
        assertNotNull(features);
        assertEquals(2, features.length);
        // TODO change to enum
        assertEquals(Feature.SMS, Feature.fromString(features[0]));
        assertEquals(Feature.MMS, Feature.fromString(features[1]));
        // TODO change to enum
        assertEquals(CallbackType.SIP, CallbackType.fromString(main.getVoiceCallbackType()));
        assertEquals(voiceCallbackValue, main.getVoiceCallbackValue());
        assertEquals(APPLICATION_ID, main.getMessagesCallbackValue());
        var last = numbers[2];
        testJsonableBaseObject(last);
        // TODO change to enum
        assertEquals(Type.LANDLINE_TOLL_FREE, Type.fromString(last.getType()));
        assertNotNull(last.getFeatures());
        assertEquals(0, last.getFeatures().length);
    }

    private void assertEqualsSampleSearchNumbers(Supplier<SearchNumbersResponse> invocation) throws Exception {
        int count = 1234;
        String json = "{\n" +
                "  \"count\": "+count+",\n" +
                "  \"numbers\": [{},{},\n" +
                "    {\n" +
                "      \"country\": \""+COUNTRY+"\",\n" +
                "      \"msisdn\": \""+MSISDN+"\",\n" +
                "      \"cost\": \"0.80\",\n" +
                "      \"type\": \"landline\",\n" +
                "      \"features\": [\n" +
                "        \"VOICE\"\n" +
                "      ]\n" +
                "    },{},{}\n" +
                "  ]\n" +
                "}";
        stubResponse(200, json);

        var parsed = invocation.get();
        testJsonableBaseObject(parsed);
        assertEquals(parsed, SearchNumbersResponse.fromJson(json));

        assertEquals(1234, parsed.getCount());
        var numbers = parsed.getNumbers();
        assertNotNull(numbers);
        assertEquals(5, numbers.length);
        var main = numbers[2];
        testJsonableBaseObject(main);
        assertEquals(COUNTRY, main.getCountry());
        assertEquals(MSISDN, main.getMsisdn());
        assertEquals("0.80", main.getCost());
        // TODO use enum
        assertEquals(Type.LANDLINE, Type.fromString(main.getType()));
        var features = main.getFeatures();
        assertNotNull(features);
        assertEquals(1, features.length);
        // TODO use enum
        assertEquals(Feature.VOICE, Feature.fromString(features[0]));
    }

    @Test
    public void testListNumbers() throws Exception {
        assertEqualsSampleListNumbers(client::listNumbers);
        assert401ResponseException(client::listNumbers);

        var filter = ListNumbersFilter.builder()
                .pattern(SearchPattern.ENDS_WITH, "2345")
                .applicationId(APPLICATION_ID_STR)
                .hasApplication(false).country("us")
                .index(9).size(24).build();

        assertEquals("US", filter.getCountry());
        assertEquals("ENDS_WITH", filter.getSearchPattern().toString());
        assertEquals("2345", filter.getPattern());
        assertEquals(APPLICATION_ID, filter.getApplicationId());
        assertEquals(false, filter.getHasApplication());
        assertEquals(9, filter.getIndex());
        assertEquals(24, filter.getSize());

        assertEqualsSampleListNumbers(() -> client.listNumbers(filter));
        assert401ResponseException(() -> client.listNumbers(filter));
    }

    @Test
    public void testSearchNumbers() throws Exception {
        var filter = SearchNumbersFilter.builder().build();
        assertEqualsSampleSearchNumbers(() -> client.searchNumbers("in"));
        assertEqualsSampleSearchNumbers(() -> client.searchNumbers(filter));
        assert401ResponseException(() -> client.searchNumbers(filter));
    }

    @Test
    public void testCancelNumber() throws Exception {
        stubBaseSuccessResponse();
        client.cancelNumber(COUNTRY, MSISDN);
        assert401ResponseException(() -> client.cancelNumber(COUNTRY, MSISDN));
    }

    @Test
    public void testBuyNumber() throws Exception {
        stubBaseSuccessResponse();
        client.buyNumber(COUNTRY, MSISDN);
        assert401ResponseException(() -> client.buyNumber(COUNTRY, MSISDN));
    }

    @Test
    public void testUpdateNumber() throws Exception {
        stubBaseSuccessResponse();
        UpdateNumberRequest request = UpdateNumberRequest.builder("447700900328", "us").build();
        assertNotNull(request.getCountry());
        assertNotNull(request.getMsisdn());
        assertNull(request.getMoSmppSysType());
        assertNull(request.getApplicationId());
        assertNull(request.getMoHttpUrl());
        assertNull(request.getVoiceCallbackType());
        assertNull(request.getVoiceCallbackValue());
        assertNull(request.getVoiceStatusCallback());
        client.updateNumber(request);
        assert401ResponseException(() -> client.updateNumber(request));
    }

    @Test
    public void testLinkNumber() throws Exception {
        stubBaseSuccessResponse();
        client.linkNumber("447700900328", "UK", APPLICATION_ID_STR);
        stubBaseSuccessResponse();
        assertThrows(IllegalArgumentException.class, () ->
                client.linkNumber(MSISDN, COUNTRY, "not-an-application_ID")
        );
    }

    // ENDPOINTS

    @Test
    public void testListNumbersEndpoint() throws Exception {
        new NumbersEndpointTestSpec<ListNumbersFilter, ListNumbersResponse>() {

            @Override
            protected RestEndpoint<ListNumbersFilter, ListNumbersResponse> endpoint() {
                return client.listNumbers;
            }

            @Override
            protected HttpMethod expectedHttpMethod() {
                return HttpMethod.GET;
            }

            @Override
            protected String expectedEndpointUri(ListNumbersFilter request) {
                return "/account/numbers";
            }

            @Override
            protected ListNumbersFilter sampleRequest() {
                return ListNumbersFilter.builder()
                        .applicationId(APPLICATION_ID).hasApplication(true)
                        .pattern(SearchPattern.STARTS_WITH, "*1337*")
                        .index(10).size(20).country("DE").build();
            }

            @Override
            protected Map<String, String> sampleQueryParams() {
                ListNumbersFilter request = sampleRequest();
                Map<String, String> params = new LinkedHashMap<>();
                params.put("pattern", request.getPattern());
                params.put("search_pattern", String.valueOf(request.getSearchPattern().getValue()));
                params.put("index", String.valueOf(request.getIndex()));
                params.put("size", String.valueOf(request.getSize()));
                params.put("country", request.getCountry());
                params.put("application_id", request.getApplicationId().toString());
                params.put("has_application", request.getHasApplication().toString());
                return params;
            }

            @Override
            public void runTests() throws Exception {
                super.runTests();
                testValidation();
                testBlank();
                testDeprecated();
            }

            private void testValidation() {
                assertThrows(IllegalArgumentException.class, () -> ListNumbersFilter.builder().country("ABC").build());
                assertEquals(1, ListNumbersFilter.builder().index(1).build().getIndex());
                assertThrows(IllegalArgumentException.class, () -> ListNumbersFilter.builder().index(0).build());
                assertEquals(100, ListNumbersFilter.builder().size(100).build().getSize());
                assertThrows(IllegalArgumentException.class, () -> ListNumbersFilter.builder().size(101).build());
                assertThrows(IllegalArgumentException.class, () -> ListNumbersFilter.builder().size(0).build());
            }

            private void testBlank() throws Exception {
                var blank = new ListNumbersFilter();
                assertNull(blank.getHasApplication());
                assertNull(blank.getApplicationId());
                assertNull(blank.getSearchPattern());
                assertNull(blank.getPattern());
                assertNull(blank.getIndex());
                assertNull(blank.getSize());
                assertNull(blank.getCountry());
                assertRequestUriAndBody(blank, Map.of());
            }

            private void testDeprecated() {
                var old = new ListNumbersFilter(2, 99, "*", SearchPattern.ANYWHERE);
                assertEquals(2, old.getIndex());
                assertEquals(99, old.getSize());
                assertEquals("*", old.getPattern());
                assertEquals(SearchPattern.ANYWHERE, old.getSearchPattern());
                assertNull(old.getHasApplication());
                assertNull(old.getApplicationId());
                assertNull(old.getCountry());
            }
        }
        .runTests();
    }

    @Test
    public void testSearchNumbersEndpoint() throws Exception {
        new NumbersEndpointTestSpec<SearchNumbersFilter, SearchNumbersResponse>() {

            @Override
            protected RestEndpoint<SearchNumbersFilter, SearchNumbersResponse> endpoint() {
                return client.searchNumbers;
            }

            @Override
            protected HttpMethod expectedHttpMethod() {
                return HttpMethod.GET;
            }

            @Override
            protected String expectedEndpointUri(SearchNumbersFilter request) {
                return "/number/search";
            }

            @Override
            protected SearchNumbersFilter sampleRequest() {
                return SearchNumbersFilter.builder()
                        .country("BB").index(11).size(25)
                        .pattern(SearchPattern.ENDS_WITH, "789")
                        .features(Feature.SMS, Feature.VOICE)
                        .type(Type.LANDLINE_TOLL_FREE).build();
            }

            @Override
            protected Map<String, String> sampleQueryParams() {
                SearchNumbersFilter filter = sampleRequest();
                Map<String, String> params = new LinkedHashMap<>();
                params.put("country", filter.getCountry());
                params.put("features", String.join(",", filter.getFeatures()));
                params.put("pattern", filter.getPattern());
                params.put("search_pattern", String.valueOf(filter.getSearchPattern().getValue()));
                params.put("index", String.valueOf(filter.getIndex()));
                params.put("size", String.valueOf(filter.getSize()));
                params.put("type", filter.getType().name().toLowerCase().replace('_', '-'));
                return params;
            }

            @Override
            public void runTests() throws Exception {
                super.runTests();
                testDeprecated();
            }

            private void testDeprecated() throws Exception {
                var filter = new SearchNumbersFilter("io");
                assertRequestUriAndBody(filter, Map.of("country", "IO"));

                var sample = sampleRequest();
                filter.setType(sample.getType());
                filter.setIndex(sample.getIndex());
                filter.setSize(sample.getSize());
                filter.setFeatures(sample.getFeatures());
                filter.setPattern(sample.getPattern());
                filter.setSearchPattern(sample.getSearchPattern());
                var params = sampleQueryParams();
                params.put("country", filter.getCountry());
                assertRequestUriAndBody(filter, params);
            }
        }
        .runTests();
    }

    @Test
    public void testBuyNumberEndpoint() throws Exception {
        new BuyCancelNumberRequestEndpointTestSpec() {

            @Override
            String endpointName() {
                return "buy";
            }

            @Override
            protected RestEndpoint<BuyCancelNumberRequest, Void> endpoint() {
                return client.buyNumber;
            }
        }
        .runTests();
    }

    @Test
    public void testCancelNumberEndpoint() throws Exception {
        new BuyCancelNumberRequestEndpointTestSpec() {

            @Override
            String endpointName() {
                return "cancel";
            }

            @Override
            protected RestEndpoint<BuyCancelNumberRequest, Void> endpoint() {
                return client.cancelNumber;
            }
        }
        .runTests();
    }

    @Test
    public void testUpdateNumberEndpoint() throws Exception {
        new BaseNumberRequestEndpointTestSpec<UpdateNumberRequest>() {

            @Override
            String endpointName() {
                return "update";
            }

            @Override
            protected RestEndpoint<UpdateNumberRequest, Void> endpoint() {
                return client.updateNumber;
            }

            @Override
            protected UpdateNumberRequest sampleRequest() {
                return UpdateNumberRequest.builder("447700900013", "UK")
                        .applicationId(APPLICATION_ID_STR)
                        .voiceStatusCallback("https://api.example.com/callback")
                        .voiceCallback(CallbackType.TEL, "1234-5678-9123-4567")
                        .moHttpUrl("https://api.example.com/mo").moSmppSysType("inbound").build();
            }

            @Override
            protected void populateSampleQueryParams(UpdateNumberRequest request) {
                super.populateSampleQueryParams(request);
                params.put("country", request.getCountry());
                params.put("msisdn", request.getMsisdn());
                params.put("app_id", request.getApplicationId().toString());
                params.put("moHttpUrl", request.getMoHttpUrl());
                params.put("moSmppSysType", request.getMoSmppSysType());
                params.put("voiceCallbackValue", request.getVoiceCallbackValue());
                params.put("voiceCallbackType", request.getVoiceCallbackType().toString());
                params.put("voiceStatusCallback", request.getVoiceStatusCallback());
            }

            @Override
            public void runTests() throws Exception {
                super.runTests();
                testDeprecated();
            }

            private void testDeprecated() throws Exception {
                var sample = sampleRequest();
                var setterReq = new UpdateNumberRequest(sample.getMsisdn(), sample.getCountry());
                setterReq.setVoiceStatusCallback(sample.getVoiceStatusCallback());
                setterReq.setVoiceCallbackValue(sample.getVoiceCallbackValue());
                setterReq.setVoiceCallbackType(sample.getVoiceCallbackType());
                setterReq.setMoHttpUrl(sample.getMoHttpUrl());
                setterReq.setMoSmppSysType(sample.getMoSmppSysType());
                setterReq.setMessagesCallbackValue("not-an-Application-ID");

                params.remove("app_id");
                params.put("messagesCallbackValue", setterReq.getMessagesCallbackValue());
                params.put("messagesCallbackType", "app");

                assertRequestUriAndBody(setterReq, params);
            }
        }
        .runTests();
    }
}
