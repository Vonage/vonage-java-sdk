package com.nexmo.messaging.sdk;
/*
 * Copyright (c) 2011-2013 Nexmo Inc
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

/**
 * NexmoSmsClientSignedRequests.java<br><br>
 *
 * Client for talking to the Nexmo REST interface that signs requests using a MD5 signature derived from a secret key<br><br>
 *
 * For usage information see {@link com.nexmo.messaging.sdk.NexmoSmsClient}<br>
 *
 * @author  Paul Cook
 * @version 1.0
 */
public class NexmoSmsClientSignedRequests extends NexmoSmsClient {

    /**
     * Instanciate a new NexmoSmsClientSignedRequests instance that will communicate using the supplied credentials.
     *
     * @param apiKey Your Nexmo account api key
     * @param signatureSecretKey Your Nexmo account secret key for signing api requests
     */
    public NexmoSmsClientSignedRequests(final String apiKey,
                                        final String signatureSecretKey) throws Exception {
        super(DEFAULT_BASE_URL,
             apiKey,
             null,   // password,
             DEFAULT_CONNECTION_TIMEOUT,
             DEFAULT_SO_TIMEOUT,
             true,   // signRequests
             signatureSecretKey);
    }

    /**
     * Instanciate a new NexmoSmsClientSignedRequests instance that will communicate using the supplied credentials, and will use the supplied connection and read timeout values.
     *
     * @param apiKey Your Nexmo account api key
     * @param signatureSecretKey Your Nexmo account secret key for signing api requests
     * @param connectionTimeout over-ride the default connection timeout with this value (in milliseconds)
     * @param soTimeout over-ride the default read-timeout with this value (in milliseconds)
     */
    public NexmoSmsClientSignedRequests(final String apiKey,
                                        final String signatureSecretKey,
                                        final int connectionTimeout,
                                        final int soTimeout) throws Exception {
        super(DEFAULT_BASE_URL,
             apiKey,
             null,   // password,
             connectionTimeout,
             soTimeout,
             true,   // signRequests
             signatureSecretKey);
    }

}
