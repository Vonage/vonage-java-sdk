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
 * NexmoSmsClientSSL.java<br><br>
 *
 * Client for talking to the Nexmo REST interface that uses uses a secure SSL / HTTPS connection to encrypt the requests<br><br>
 *
 * For usage information see {@link com.nexmo.messaging.sdk.NexmoSmsClient}<br>
 *
 * @author  Paul Cook
 * @version 1.0
 */
public class NexmoSmsClientSSL extends NexmoSmsClient {

    /**
     * Instanciate a new NexmoSmsClientSSL instance that will communicate using the supplied credentials.
     *
     * @param apiKey Your Nexmo account api key
     * @param apiSecret Your Nexmo account api secret
     */
    public NexmoSmsClientSSL(final String apiKey, final String apiSecret) throws Exception {
        super(DEFAULT_BASE_URL,
              apiKey,
              apiSecret,
              DEFAULT_CONNECTION_TIMEOUT,
              DEFAULT_SO_TIMEOUT,
              false,  // signRequests
              null,   // signatureSecretKey
              true);  // useSSL
    }

    /**
     * Instanciate a new NexmoSmsClientSSL instance that will communicate using the supplied credentials, and will use the supplied connection and read timeout values.
     *
     * @param apiKey Your Nexmo account api key
     * @param apiSecret Your Nexmo account api secret
     * @param connectionTimeout over-ride the default connection timeout with this value (in milliseconds)
     * @param soTimeout over-ride the default read-timeout with this value (in milliseconds)
     */
    public NexmoSmsClientSSL(final String apiKey,
                             final String apiSecret,
                             final int connectionTimeout,
                             final int soTimeout) throws Exception {
        super(DEFAULT_BASE_URL,
              apiKey,
              apiSecret,
              connectionTimeout,
              soTimeout,
              false,  // signRequests
              null,   // signatureSecretKey
              true);  // useSSL
    }

}
