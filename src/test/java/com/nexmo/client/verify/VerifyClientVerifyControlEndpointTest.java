/*
 * Copyright (c) 2011-2018 Vonage Inc
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
package com.nexmo.client.verify;

import com.nexmo.client.ClientTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class VerifyClientVerifyControlEndpointTest extends ClientTest<VerifyClient> {

    @Before
    public void setUp() {
        client = new VerifyClient(wrapper);
    }

    @Test
    public void testAdvanceVerification() throws Exception {
        String json = "{\n" + "  \"status\":\"0\",\n" + "  \"command\":\"trigger_next_event\"\n" + "}";
        wrapper.setHttpClient(this.stubHttpClient(200, json));

        ControlResponse response = client.advanceVerification("a-request-id");
        Assert.assertEquals("0", response.getStatus());
        Assert.assertEquals(VerifyControlCommand.TRIGGER_NEXT_EVENT, response.getCommand());
    }

    @Test
    public void testCancelVerification() throws Exception {
        String json = "{\n" + "  \"status\":\"0\",\n" + "  \"command\":\"cancel\"\n" + "}";
        wrapper.setHttpClient(this.stubHttpClient(200, json));

        ControlResponse response = client.cancelVerification("a-request-id");
        Assert.assertEquals("0", response.getStatus());
        Assert.assertEquals(VerifyControlCommand.CANCEL, response.getCommand());
    }
}
