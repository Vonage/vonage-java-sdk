/*
 * Copyright (c) 2020 Vonage
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
package com.vonage.client.conversion;

import com.vonage.client.ClientTest;
import com.vonage.client.VonageBadRequestException;
import org.junit.Before;
import org.junit.Test;

import java.text.SimpleDateFormat;

import static org.junit.Assert.fail;

public class ConversionClientTest extends ClientTest<ConversionClient> {
    @Before
    public void setUp() {
        client = new ConversionClient(wrapper);
    }

    @Test
    public void testSuccessfulResponse() {
        try {
            wrapper.setHttpClient(this.stubHttpClient(200, ""));
            this.client.submitConversion(ConversionRequest.Type.VOICE,
                                         "MESSAGE-ID",
                                         true,
                                         new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2014-03-04 10:11:12"));
        } catch (Exception e) {
            fail("No exceptions should be thrown.");
        }
    }

    @Test(expected = VonageBadRequestException.class)
    public void testWrongCredentials() throws Exception {
        wrapper.setHttpClient(this.stubHttpClient(401, ""));
        this.client.submitConversion(ConversionRequest.Type.SMS,
                                     "MESSAGE-ID",
                                     true,
                                     new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2014-03-04 10:11:12"));
    }

    @Test(expected = VonageBadRequestException.class)
    public void testConversionNotEnabled() throws Exception {
        wrapper.setHttpClient(this.stubHttpClient(402, ""));
        this.client.submitConversion(ConversionRequest.Type.SMS,
                                     "MESSAGE-ID",
                                     true,
                                     new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2014-03-04 10:11:12"));
    }

    @Test(expected = VonageBadRequestException.class)
    public void testInvalidParameters() throws Exception {
        wrapper.setHttpClient(this.stubHttpClient(423, ""));
        this.client.submitConversion(ConversionRequest.Type.SMS,
                                     "MESSAGE-ID",
                                     true,
                                     new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2014-03-04 10:11:12"));
    }

    @Test(expected = VonageBadRequestException.class)
    public void testInvalidParametersEnhanceCalm() throws Exception {
        wrapper.setHttpClient(this.stubHttpClient(420, ""));
        this.client.submitConversion(ConversionRequest.Type.SMS,
                                     "MESSAGE-ID",
                                     true,
                                     new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2014-03-04 10:11:12"));
    }


}
