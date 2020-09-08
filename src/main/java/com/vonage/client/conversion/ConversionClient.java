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
package com.vonage.client.conversion;

import com.nexmo.client.*;
import com.vonage.client.*;

import java.util.Date;

/**
 * A client for talking to the Vonage Conversion API. The standard way to obtain an instance of this class is to use
 * {@link VonageClient#getConversionClient()}.
 * <p>
 * Allows you to tell Vonage about the reliability of your 2FA communications.
 * <p>
 * More information on method parameters can be found at Vonage website:
 * <a href="https://developer.nexmo.com/messaging/conversion-api/overview">https://developer.nexmo.com/messaging/conversion-api/overview</a>
 */
public class ConversionClient extends AbstractClient {
    private ConversionEndpoint conversionEndpoint;

    public ConversionClient(HttpWrapper httpWrapper) {
        super(httpWrapper);

        this.conversionEndpoint = new ConversionEndpoint(httpWrapper);
    }

    /**
     * Submit a request to the Conversion API indicating whether or not a message was delivered.
     *
     * @param type      The {@link ConversionRequest.Type} type of com.nexmo.client.conversion.
     * @param messageId The id of the message that was sent.
     * @param delivered A boolean indicating whether or not it was delivered.
     * @param timestamp A timestamp of when it was known to be delivered.
     *
     * @throws VonageClientException        if there was a problem with the Vonage request or response objects.
     * @throws VonageResponseParseException if the response from the API could not be parsed.
     */
    public void submitConversion(ConversionRequest.Type type,
                                 String messageId,
                                 boolean delivered,
                                 Date timestamp) throws VonageResponseParseException, VonageClientException {
        this.conversionEndpoint.submitConversion(new ConversionRequest(type, messageId, delivered, timestamp));
    }
}
