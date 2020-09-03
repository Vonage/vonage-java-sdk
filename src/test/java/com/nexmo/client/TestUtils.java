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
package com.nexmo.client;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.HttpResponseException;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static junit.framework.Assert.fail;

public class TestUtils {
    public byte[] loadKey(String path) throws IOException {
        int len;
        int size = 1024;
        byte[] buf = new byte[size];

        ByteArrayOutputStream bos = new ByteArrayOutputStream(2048);
        InputStream is = this.getClass().getResourceAsStream(path);
        if (is != null) {
            while ((len = is.read(buf, 0, size)) != -1) {
                bos.write(buf, 0, len);
            }
        } else {
            throw new IOException("Could not find resource at: " + this.getClass().getResource(path));
        }
        return bos.toByteArray();

    }

    public static Map<String, String> makeParameterMap(List<NameValuePair> params) {
        Map<String, String> result = new HashMap<>();
        for (NameValuePair param : params) {
            result.put(param.getName(), param.getValue());
        }
        return result;
    }

    /**
     * Used in cases where the same url parameter is repeated, returns a map where the values are Lists of Strings,
     * rather than just an individual String
     */
    public static Map<String, List<String>> makeFullParameterMap(List<NameValuePair> params) {
        Map<String, List<String>> result = new HashMap<>();
        for (NameValuePair param : params) {
            List<String> values = result.get(param.getName());
            if (values == null) {
                values = new ArrayList<>(1);
                result.put(param.getName(), values);
            }
            values.add(param.getValue());
        }
        return result;
    }

    public static HttpResponse makeJsonHttpResponse(int statusCode, String json) {
        HttpResponse stubResponse = new BasicHttpResponse(
                new BasicStatusLine(new ProtocolVersion("1.1", 1, 1), statusCode, "OK")
        );
        InputStream jsonStream = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
        BasicHttpEntity entity = new BasicHttpEntity();
        entity.setContent(jsonStream);
        stubResponse.setEntity(entity);

        return stubResponse;
    }

    public static void test429(AbstractMethod methodUnderTest) throws Exception {
        try {
            methodUnderTest.parseResponse(TestUtils.makeJsonHttpResponse(429, "Don't know what this is"));
            fail("A 429 response should raise a HttpResponseException");
        } catch (HttpResponseException e) {
            // This is expected
        }
    }
}
