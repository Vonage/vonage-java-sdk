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
package com.nexmo.client.sms;


import com.nexmo.client.HttpWrapper;
import com.nexmo.client.NexmoClientException;
import com.nexmo.client.NexmoResponseParseException;
import com.nexmo.client.sms.messages.Message;

import java.io.IOException;

/**
 * Client for talking to the Nexmo API.
 * <p>
 * To submit a message, instantiate a SmsClient, passing the credentials for your Nexmo account on
 * the constructor. Then instantiate the appropriate {@link com.nexmo.client.sms.messages.Message}
 * subclass depending on which type of message you are going to submit. The following subclasses are available:
 * <p>
 * <ul>
 * <li>{@link com.nexmo.client.sms.messages.TextMessage}
 * <li>{@link com.nexmo.client.sms.messages.BinaryMessage}
 * <li>{@link com.nexmo.client.sms.messages.WapPushMessage}
 * </ul>
 * <p>
 * Once you have a {@link com.nexmo.client.sms.messages.Message} object, pass it to {@link #submitMessage(Message)}
 * which will submit the message to the Nexmo API. It returns an array of
 * {@link com.nexmo.client.sms.SmsSubmissionResult}, with 1 entry for every sms message that was sent.
 * Text messages greater than 160 characters will require multiple SMS messages to be submitted.
 * Each entry in this array will contain an individual messageId as well as an individual status detailing the success or reason for failure of each message.
 * <p>
 * The list of possible status codes is listed below:
 * <ul>
 * <li>0 = success  -- the message was successfully accepted for delivery by nexmo
 * <li>1 = throttled -- You have exceeded the submission capacity allowed on this account, please back-off and re-try
 * <li>2 = missing params -- Your request is incomplete and missing some mandatory parameters
 * <li>3 = invalid params -- The value of 1 or more parameters is invalid
 * <li>4 = invalid credentials -- The api key / secret you supplied is either invalid or disabled
 * <li>5 = internal error -- An error has occurred in the nexmo platform whilst processing this message
 * <li>6 = invalid message -- The Nexmo platform was unable to process this message, for example, an un-recognized number prefix
 * <li>7 = number barred -- The number you are trying to submit to is blacklisted and may not receive messages
 * <li>8 = partner account barred -- The api key you supplied is for an account that has been barred from submitting messages
 * <li>9 = partner quota exceeded -- Your pre-pay account does not have sufficient credit to process this message
 * <li>10 = too many existing binds -- The number of simultaneous connections to the platform exceeds the capabilities of your account
 * <li>11 = account not enabled for http -- This account is not provisioned for http / rest submission, you should use SMPP instead
 * <li>12 = message too long -- The message length exceeds the maximum allowed
 * <li>13 = comms failure -- There was a network failure attempting to contact Nexmo
 * <li>14 = Invalid Signature -- The signature supplied with this request was not verified successfully
 * <li>15 = invalid sender address -- The sender address was not allowed for this message
 * <li>16 = invalid TTL -- The ttl parameter values, or combination of parameters is invalid
 * <li>17 = number unreachable -- this destination cannot be delivered to at this time (if reachable=true is specified)
 * <li>18 = too many destinations -- There are more than the maximum allowed number of destinations in this request
 * <li>19 = Facility Not Allowed - Your request makes use of a facility that is not enabled on your account
 * <li>20 = Invalid Message Class - The message class value supplied was out of range (0 - 3)
 * </ul>
 *
 * @author Paul Cook
 */
public class SmsClient {
    private SendMessageEndpoint message;

    /**
     * Create a new SmsClient.
     */
    public SmsClient(HttpWrapper httpWrapper) {
        this.message = new SendMessageEndpoint(httpWrapper);
    }

    /**
     * Send an SMS message.
     * <p>
     * This uses the supplied object to construct a request and post it to the Nexmo API.<br>
     * This method will respond with an array of SmsSubmissionResult objects. Depending on the nature and length of the submitted message, Nexmo may automatically
     * split the message into multiple sms messages in order to deliver to the handset. For example, a long text sms of greater than 160 chars will need to be split
     * into multiple 'concatenated' sms messages. The Nexmo service will handle this automatically for you.<br>
     * The array of SmsSubmissionResult objects will contain a SmsSubmissionResult object for every actual sms that was required to submit the message.
     * each message can potentially have a different status result, and each message will have a different message id.
     * Delivery notifications will be generated for each sms message within this set and will be posted to your application containing the appropriate message id.
     *
     * @param message The message request object that describes the type of message and the contents to be submitted.
     * @return SmsSubmissionResult[] an array of results, 1 object for each sms message that was required to submit this message in its entirety
     * @throws NexmoResponseParseException if the HTTP response could not be parsed.
     * @throws IOException                 There has been an error attempting to communicate with the Nexmo service (e.g. Network failure).
     */
    public SmsSubmissionResult[] submitMessage(Message message) throws IOException, NexmoClientException {
        return this.message.execute(message);
    }

}
