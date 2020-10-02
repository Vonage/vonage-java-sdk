/*
 *   Copyright 2020 Vonage
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
            wrapper.setHttpClient(stubHttpClient(200, ""));
            client.submitConversion(ConversionRequest.Type.VOICE,
                                         "MESSAGE-ID",
                                         true,
                                         new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2014-03-04 10:11:12"));
        } catch (Exception e) {
            fail("No exceptions should be thrown.");
        }
    }

    @Test(expected = VonageBadRequestException.class)
    public void testWrongCredentials() throws Exception {
        wrapper.setHttpClient(stubHttpClient(401, ""));
        client.submitConversion(ConversionRequest.Type.SMS,
                                     "MESSAGE-ID",
                                     true,
                                     new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2014-03-04 10:11:12"));
    }

    @Test(expected = VonageBadRequestException.class)
    public void testConversionNotEnabled() throws Exception {
        wrapper.setHttpClient(stubHttpClient(402, ""));
        client.submitConversion(ConversionRequest.Type.SMS,
                                     "MESSAGE-ID",
                                     true,
                                     new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2014-03-04 10:11:12"));
    }

    @Test(expected = VonageBadRequestException.class)
    public void testInvalidParameters() throws Exception {
        wrapper.setHttpClient(stubHttpClient(423, ""));
        client.submitConversion(ConversionRequest.Type.SMS,
                                     "MESSAGE-ID",
                                     true,
                                     new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2014-03-04 10:11:12"));
    }

    @Test(expected = VonageBadRequestException.class)
    public void testInvalidParametersEnhanceCalm() throws Exception {
        wrapper.setHttpClient(stubHttpClient(420, ""));
        client.submitConversion(ConversionRequest.Type.SMS,
                                     "MESSAGE-ID",
                                     true,
                                     new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2014-03-04 10:11:12"));
    }


}
