/*
 * Copyright (c) 2011-2019 Nexmo Inc
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

import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TokenAuthMethodTest {
    @Test
    public void testAddingApiKeyAndSecretToJson() throws Exception {
        AuthMethod auth = new TokenAuthMethod("apikey", "secret");
        String before = "{\"name\":\"app name\",\"type\":\"voice\",\"answer_url\":\"https://example.com/answer\",\"event_url\":\"https://example.com/event\"}";
        RequestBuilder requestBuilder = RequestBuilder.get().setEntity(new StringEntity(before, ContentType.APPLICATION_JSON));

        RequestBuilder requestBuilderWithAuthentication = auth.applyAsJsonProperties(requestBuilder);

        String after = "{\"name\":\"app name\",\"type\":\"voice\",\"answer_url\":\"https://example.com/answer\",\"event_url\":\"https://example.com/event\",\"api_key\":\"apikey\",\"api_secret\":\"secret\"}";
        assertEquals(after, EntityUtils.toString(requestBuilderWithAuthentication.getEntity()));
    }
}
