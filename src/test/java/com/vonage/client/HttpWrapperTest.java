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
package com.vonage.client;


import com.vonage.client.auth.AuthCollection;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class HttpWrapperTest {
    private static final String EXPECTED_DEFAULT_API_BASE_URI = "https://api.nexmo.com";
    private static final String EXPECTED_DEFAULT_REST_BASE_URI = "https://rest.nexmo.com";
    private static final String EXPECTED_DEFAULT_SNS_BASE_URI = "https://sns.nexmo.com";

    private HttpWrapper hw;

    @Before
    public void setUp() {
        this.hw = new HttpWrapper(new AuthCollection());
    }

    @Test
    public void basicTest() {
        assertNotNull(this.hw.getHttpClient());
    }

    @Test
    public void testAuthMethodAccessors() {
        AuthCollection auths = new AuthCollection();
        this.hw.setAuthCollection(auths);
        assertEquals(auths, this.hw.getAuthCollection());
    }

    @Test
    public void testHttpConfigAccessor() {
        assertNotNull(this.hw.getHttpConfig());
    }

    @Test
    public void testDefaultConstructorSetsDefaultConfigValues() {
        HttpWrapper wrapper = new HttpWrapper();

        HttpConfig config = wrapper.getHttpConfig();
        assertEquals(EXPECTED_DEFAULT_API_BASE_URI, config.getApiBaseUri());
        assertEquals(EXPECTED_DEFAULT_REST_BASE_URI, config.getRestBaseUri());
        assertEquals(EXPECTED_DEFAULT_SNS_BASE_URI, config.getSnsBaseUri());
    }
}
