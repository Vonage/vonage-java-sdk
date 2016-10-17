package com.nexmo.insight.sdk;

import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.junit.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


public class NexmoInsightClientTest {
    private NexmoInsightClient client;

    public NexmoInsightClientTest() throws IOException {
        super();
    }

    @Before
    public void setUp() throws ParserConfigurationException {
        client = new NexmoInsightClient("not-an-api-key", "secret");
    }

    private HttpClient stubHttpClient(int statusCode, String content) {
        HttpClient result = mock(HttpClient.class);
        try {
            HttpResponse response = mock(HttpResponse.class);
            StatusLine sl = mock(StatusLine.class);
            HttpEntity entity = mock(HttpEntity.class);

            when(result.execute(any(HttpUriRequest.class))).thenReturn(response);
            when(entity.getContent()).thenReturn(new ByteArrayInputStream(content.getBytes("UTF-8")));
            when(sl.getStatusCode()).thenReturn(statusCode);
            when(response.getStatusLine()).thenReturn(sl);
            when(response.getEntity()).thenReturn(entity);
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
        return result;
    }

    @Test
    public void testInsight() throws IOException, SAXException {
        this.client.httpClient = this.stubHttpClient(
                200,
                "<?xml version='1.0' encoding='UTF-8' ?>\n" +
                        "  <lookup>\n" +
                        "      <requestId>a-fictitious-request-id</requestId>\n" +
                        "      <number>447700900999</number>\n" +
                        "      <status>0</status>\n" +
                        "      <errorText>error</errorText>\n" +
                        "      <requestPrice>0.03</requestPrice>\n" +
                        "      <remainingBalance>1.97</remainingBalance>\n" +
                        "  </lookup>");


        String[] features = new String[] { "type", "reachable"};

        InsightResult r = client.request(
                "447700900999",
                "http://some.callback.url/script.php",
                features,
                30000,
                "POST",
                "CLIENT-DATA",
                "127.0.0.1");
        assertEquals(InsightResult.STATUS_OK, r.getStatus());
        assertEquals("447700900999", r.getNumber());
        assertEquals(0.03f, r.getRequestPrice(), 0);
    }
}
