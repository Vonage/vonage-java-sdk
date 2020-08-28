/*
 * Copyright (c) 2011-2017 Vonage Inc
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.nexmo.client.numbers;

import com.nexmo.client.HttpConfig;
import com.nexmo.client.HttpWrapper;
import com.nexmo.client.TestUtils;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static com.nexmo.client.TestUtils.test429;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;


public class SearchNumbersEndpointTest {
    private SearchNumbersEndpoint endpoint;

    @Before
    public void setUp() throws Exception {
        this.endpoint = new SearchNumbersEndpoint(new HttpWrapper());
    }

    @Test
    public void makeRequest() throws Exception {
        SearchNumbersFilter filter = new SearchNumbersFilter("BB");
        filter.setIndex(10);
        filter.setSize(20);
        filter.setPattern("234");
        filter.setFeatures(new String[]{"SMS", "VOICE"});
        filter.setSearchPattern(SearchPattern.STARTS_WITH);
        filter.setType(Type.LANDLINE_TOLL_FREE);
        RequestBuilder request = endpoint.makeRequest(filter);

        assertEquals("GET", request.getMethod());
        Map<String, String> params = TestUtils.makeParameterMap(request.getParameters());
        assertEquals("BB", params.get("country"));
        assertEquals("SMS,VOICE", params.get("features"));
        assertEquals("234", params.get("pattern"));
        assertEquals("0", params.get("search_pattern"));
        assertEquals("10", params.get("index"));
        assertEquals("20", params.get("size"));
        assertEquals("landline-toll-free", params.get("type"));
    }

    @Test
    public void testNullFeatureParam() throws Exception {
        SearchNumbersFilter filter = new SearchNumbersFilter("BB");
        RequestBuilder request = endpoint.makeRequest(filter);

        Map<String, String> params = TestUtils.makeParameterMap(request.getParameters());
        assertEquals("BB", params.get("country"));
        assertNull(params.get("features"));
    }

    @Test
    public void testEmptyFeature() throws Exception {
        SearchNumbersFilter filter = new SearchNumbersFilter("BB");
        filter.setFeatures(new String[]{});
        RequestBuilder request = endpoint.makeRequest(filter);

        Map<String, String> params = TestUtils.makeParameterMap(request.getParameters());
        assertEquals("BB", params.get("country"));
        assertNull(params.get("features"));
    }

    @Test
    public void testParseResponse() throws Exception {
        HttpResponse stubResponse = new BasicHttpResponse(new BasicStatusLine(new ProtocolVersion("1.1", 1, 1),
                200,
                "OK"
        ));

        String json = "{\n" + "  \"count\": 4,\n" + "  \"numbers\": [\n" + "    {\n" + "      \"country\": \"GB\",\n"
                + "      \"msisdn\": \"447700900000\",\n" + "      \"cost\": \"0.50\",\n"
                + "      \"type\": \"mobile\",\n" + "      \"features\": [\n" + "        \"VOICE\",\n"
                + "        \"SMS\"\n" + "      ]\n" + "    }\n" + "  ]\n" + "}";
        InputStream jsonStream = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
        BasicHttpEntity entity = new BasicHttpEntity();
        entity.setContent(jsonStream);
        stubResponse.setEntity(entity);

        SearchNumbersResponse response = endpoint.parseResponse(stubResponse);
        assertEquals(4, response.getCount());
    }

    @Test
    public void testRequestThrottleResponse() throws Exception {
        test429(new SearchNumbersEndpoint(null));
    }

    @Test
    public void testDefaultUri() throws Exception {
        SearchNumbersFilter filter = new SearchNumbersFilter("BB");
        filter.setIndex(10);
        filter.setSize(20);
        filter.setPattern("234");
        filter.setFeatures(new String[]{"SMS", "VOICE"});
        filter.setSearchPattern(SearchPattern.STARTS_WITH);

        RequestBuilder builder = endpoint.makeRequest(filter);
        assertEquals("GET", builder.getMethod());
        assertEquals(
                "https://rest.nexmo.com/number/search?country=BB&features=SMS%2CVOICE&index=10&size=20&pattern=234&search_pattern=0",
                builder.build().getURI().toString()
        );
    }

    @Test
    public void testCustomUri() throws Exception {
        HttpWrapper wrapper = new HttpWrapper(HttpConfig.builder().baseUri("https://example.com").build());
        SearchNumbersEndpoint endpoint = new SearchNumbersEndpoint(wrapper);
        SearchNumbersFilter filter = new SearchNumbersFilter("BB");
        filter.setIndex(10);
        filter.setSize(20);
        filter.setPattern("234");
        filter.setFeatures(new String[]{"SMS", "VOICE"});
        filter.setSearchPattern(SearchPattern.STARTS_WITH);

        RequestBuilder builder = endpoint.makeRequest(filter);
        assertEquals("GET", builder.getMethod());
        assertEquals(
                "https://example.com/number/search?country=BB&features=SMS%2CVOICE&index=10&size=20&pattern=234&search_pattern=0",
                builder.build().getURI().toString()
        );
    }
}
