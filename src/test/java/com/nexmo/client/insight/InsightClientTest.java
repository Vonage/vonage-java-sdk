/*
 * Copyright (c) 2011-2017 Nexmo Inc
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
package com.nexmo.client.insight;

import com.nexmo.client.insight.advanced.AdvancedInsightEndpoint;
import com.nexmo.client.insight.advanced.AdvancedInsightRequest;
import com.nexmo.client.insight.basic.BasicInsightEndpoint;
import com.nexmo.client.insight.basic.BasicInsightRequest;
import com.nexmo.client.insight.standard.StandardInsightEndpoint;
import com.nexmo.client.insight.standard.StandardInsightRequest;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class InsightClientTest {
    private InsightClient client;

    @Before
    public void setUpWithoutBaseUri() {
        client = new InsightClient(null);
        client.basic = mock(BasicInsightEndpoint.class);
        client.standard = mock(StandardInsightEndpoint.class);
        client.advanced = mock(AdvancedInsightEndpoint.class);
    }

    @Test
    public void testGetBasicNumberInsight() throws Exception {
        client.getBasicNumberInsight("12345");
        verify(client.basic).execute(eq(new BasicInsightRequest("12345")));
    }

    @Test
    public void testGetBasicNumberInsight1() throws Exception {
        client.getBasicNumberInsight("12345", "GB");
        verify(client.basic).execute(eq(new BasicInsightRequest("12345", "GB")));
    }


    @Test
    public void testGetStandardNumberInsight() throws Exception {
        client.getStandardNumberInsight("12345");
        verify(client.standard).execute(eq(new StandardInsightRequest("12345")));
    }

    @Test
    public void testGetStandardNumberInsight1() throws Exception {
        client.getStandardNumberInsight("12345", "GB");
        verify(client.standard).execute(eq(new StandardInsightRequest("12345", "GB")));
    }

    @Test
    public void testGetStandardNumberInsight2() throws Exception {
        client.getStandardNumberInsight("12345", "GB", true);
        verify(client.standard).execute(eq(new StandardInsightRequest("12345", "GB", true)));
    }

    @Test
    public void testGetAdvancedNumberInsight() throws Exception {
        client.getAdvancedNumberInsight("12345");
        verify(client.advanced).execute(eq(new AdvancedInsightRequest("12345")));

    }

    @Test
    public void testGetAdvancedNumberInsight1() throws Exception {
        client.getAdvancedNumberInsight("12345", "GB");
        verify(client.advanced).execute(eq(new AdvancedInsightRequest("12345", "GB")));
    }

    @Test
    public void testGetAdvancedNumberInsight2() throws Exception {
        client.getAdvancedNumberInsight("12345", "GB", "123.123.123.123");
        verify(client.advanced).execute(eq(new AdvancedInsightRequest("12345", "GB", "123.123.123.123")));
    }

    @Test
    public void testGetAdvancedNumberInsight3() throws Exception {
        client.getAdvancedNumberInsight("12345", "GB", "123.123.123.123", true);
        verify(client.advanced).execute(eq(new AdvancedInsightRequest("12345", "GB", "123.123.123.123", true)));
    }

}
