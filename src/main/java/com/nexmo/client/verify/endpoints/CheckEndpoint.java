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
package com.nexmo.client.verify.endpoints;

import com.nexmo.client.HttpWrapper;
import com.nexmo.client.NexmoClientException;
import com.nexmo.client.verify.CheckResult;
import com.nexmo.client.verify.CheckRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;

public class CheckEndpoint {
    private static final Log log = LogFactory.getLog(CheckEndpoint.class);

    private VerifyCheckMethod checkMethod;

    /**
     * Create a new CheckEndpoint.
     * <p>
     * This client is used for calling the verify API's check endpoint.
     */
    public CheckEndpoint(HttpWrapper httpWrapper) {
        this.checkMethod = new VerifyCheckMethod(httpWrapper);
    }

    public CheckResult check(final String requestId,
                             final String code) throws IOException, NexmoClientException {
        return this.checkMethod.execute(new CheckRequest(requestId, code));
    }

    public CheckResult check(final String requestId,
                             final String code,
                             final String ipAddress) throws IOException, NexmoClientException {
        return this.checkMethod.execute(new CheckRequest(requestId, code, ipAddress));
    }

}
