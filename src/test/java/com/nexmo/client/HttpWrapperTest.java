package com.nexmo.client;
/*
 * Copyright (c) 2011-2016 Nexmo Inc
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

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.nexmo.client.auth.AuthCollection;
import org.apache.http.client.HttpClient;
import org.junit.Before;
import org.junit.Test;

public class HttpWrapperTest {
    HttpWrapper hw;
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
        this.hw.setAuthMethods(auths);
        assertEquals(auths, this.hw.getAuthMethods());
    }
}
