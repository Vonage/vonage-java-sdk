package com.nexmo.insight.sdk;

import org.apache.http.client.HttpClient;
import org.junit.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;

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
        client.httpClient = mock(HttpClient.class);
    }

    @Test
    public void testInsight() throws IOException, SAXException {
        String[] features = new String[] { "type", "reachable"};

        System.out.println(client);
        System.out.println(client.httpClient);
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
