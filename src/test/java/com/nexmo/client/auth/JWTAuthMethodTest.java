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
package com.nexmo.client.auth;


import com.nexmo.client.TestUtils;
import org.apache.http.client.methods.RequestBuilder;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;

public class JWTAuthMethodTest {
    private TestUtils testUtils;
    private JWTAuthMethod auth;

    @Before
    public void setUp() throws Exception {
        this.testUtils = new TestUtils();

        byte[] keyBytes = testUtils.loadKey("test/keys/application_key");
        auth = new JWTAuthMethod("application-id", keyBytes);
    }

    @Test
    public void testSavedKeyUsingPath() throws Exception {
        auth = new JWTAuthMethod("application-id", Paths.get("src/test/resources/com/nexmo/client/test/keys/application_key2"));
    }

    @Test
    public void testApply() throws Exception {
        RequestBuilder req = RequestBuilder.get();
        auth.apply(req);

        assertEquals(1, req.getHeaders("Authorization").length);
        assertEquals("Bearer ", req.getFirstHeader("Authorization").getValue().substring(0, 7));
    }
}
