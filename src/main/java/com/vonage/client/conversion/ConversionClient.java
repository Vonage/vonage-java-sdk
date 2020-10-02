/*
 *   Copyright 2020 Vonage
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package com.vonage.client.conversion;


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

        conversionEndpoint = new ConversionEndpoint(httpWrapper);
    }

    /**
     * Submit a request to the Conversion API indicating whether or not a message was delivered.
     *
     * @param type      The {@link ConversionRequest.Type} type of com.vonage.client.conversion.
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
        conversionEndpoint.submitConversion(new ConversionRequest(type, messageId, delivered, timestamp));
    }
}
