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
package com.vonage.client.auth;

import com.vonage.client.TestUtils;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class VonageUnacceptableAuthExceptionTest {
    @Test
    public void testAllAuthMethodHaveAppropriateDescriptions() throws IOException {
        VonageUnacceptableAuthException exception = new VonageUnacceptableAuthException(
                Arrays.asList(new TokenAuthMethod(null, null),
                        new SignatureAuthMethod(null, null),
                        new JWTAuthMethod("application_id", new TestUtils().loadKey("test/keys/application_key"))),
                Arrays.asList(TokenAuthMethod.class, SignatureAuthMethod.class, JWTAuthMethod.class)
        );

        assertEquals(
                "No acceptable authentication type could be found. Acceptable types are: API Key and Secret, API Key and Signature Secret, Application ID and Private Key. Supplied types were: API Key and Secret, API Key and Signature Secret, Application ID and Private Key",
                exception.getMessage()
        );
    }
}
