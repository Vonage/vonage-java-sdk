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
package com.vonage.client.sms;


import com.vonage.client.HttpWrapper;
import com.vonage.client.VonageClient;
import com.vonage.client.VonageClientException;
import com.vonage.client.VonageResponseParseException;
import com.vonage.client.sms.messages.Message;


/**
 * A client for talking to the Vonage Voice API. The standard way to obtain an instance of this class is to use {@link
 * VonageClient#getSmsClient()}.
 */
public class SmsClient {
    final SendMessageEndpoint message;

    /**
     * Create a new SmsClient.
     * @param httpWrapper Http Wrapper used to create a Sms Request
     */
    public SmsClient(HttpWrapper httpWrapper) {
        message = new SendMessageEndpoint(httpWrapper);
    }

    /**
     * Send an SMS message.
     * <p>
     * This uses the supplied object to construct a request and post it to the Vonage API.<br> This method will respond
     * with an SmsSubmissionResponse object. Depending on the nature and length of the submitted message, Vonage may
     * automatically split the message into multiple sms messages in order to deliver to the handset. For example, a
     * long text sms of greater than 160 chars will need to be split into multiple 'concatenated' sms messages. The
     * Vonage service will handle this automatically for you.<br> The messages are stored as a Collection of
     * SmsSubmissionResponseMessage objects on the SmsSubmissionResponse object. Each message can potentially have a
     * different status result, and each message will have a different message id. Delivery notifications will be
     * generated for each sms message within this set and will be posted to your application containing the appropriate
     * message id.
     *
     * @param message The message request object that describes the type of message and the contents to be submitted.
     *
     * @return SmsSubmissionResponse an object containing a collection of SmsSubmissionResponseMessage objects for each
     * actual sms that was required to submit the message.
     *
     * @throws VonageClientException        if there was a problem with the Vonage request or response objects.
     * @throws VonageResponseParseException if the response from the API could not be parsed.
     */
    public SmsSubmissionResponse submitMessage(Message message) throws VonageResponseParseException, VonageClientException {
        return this.message.execute(message);
    }

}
