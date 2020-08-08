package com.nexmo.client.voice;

import com.nexmo.client.HttpWrapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class StopTalkMethodTest {
    private static final Log LOG = LogFactory.getLog(StopTalkMethodTest.class);

    private StopTalkMethod method;

    @Before
    public void setUp() throws Exception {
        method = new StopTalkMethod(new HttpWrapper());
    }

    @Test
    public void makeRequestTest() {
    }

    @Test
    public void parseResponseTest() {
    }

    @Test
    public void customUriTest() {

    }
}