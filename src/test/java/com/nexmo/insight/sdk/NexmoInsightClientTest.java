package com.nexmo.insight.sdk;

import com.nexmo.NexmoBaseTest;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;

import java.io.IOException;

import static org.junit.Assert.*;


public class NexmoInsightClientTest extends NexmoBaseTest {

    private NexmoInsightClient client;

    public NexmoInsightClientTest() throws IOException {
        super();
    }

    @Before
    public void setUp() throws ParserConfigurationException {
        client = new NexmoInsightClient(getApiKey(), getApiSecret());
    }

    @Test
    public void testInsight() throws IOException, SAXException {
        String[] features = null;
        String _features = getProperty("insight.features", false);
        if (_features != null)
            features = _features.split(",");

        InsightResult r = client.request(
                getProperty("insight.number"),
                getProperty("insight.callback.url"),
                features,
                getLong("insight.callback.timeout"),
                getProperty("insight.callback.method", false),
                getProperty("insight.callback.clientRef", false),
                getProperty("insight.callback.ipAddress", false));
        assertEquals(InsightResult.STATUS_OK, r.getStatus());
        assertEquals(getProperty("insight.number"), r.getNumber());
        assertEquals(0.03f, r.getRequestPrice(), 0);
    }

}
