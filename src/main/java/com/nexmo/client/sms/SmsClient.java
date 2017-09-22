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
import com.nexmo.client.NexmoClient;
import com.nexmo.client.NexmoClientException;
import com.nexmo.client.NexmoResponseParseException;
import com.nexmo.client.sms.messages.Message;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


/**
 * A client for talking to the Nexmo Voice API. The standard way to obtain an instance of this class is to use
 * {@link NexmoClient#getSmsClient()}.
 */
public class SmsClient {
    private SendMessageEndpoint message;
    private SmsSearchEndpoint search;

    /**
     * Create a new SmsClient.
     */
    public SmsClient(HttpWrapper httpWrapper) {
        this.message = new SendMessageEndpoint(httpWrapper);
        this.search = new SmsSearchEndpoint(httpWrapper);
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

    /**
     * Search for completed SMS transactions.
     * <p>
     * You should probably use the helper methods {@link #searchMessages(String, String...)} or
     * {@link #searchMessages(String, String...)} instead.
     */
    public SearchSmsResponse searchMessages(SearchSmsRequest request)
            throws IOException, NexmoClientException {
        return this.search.execute(request);
    }

    /**
     * Search for completed SMS transactions by ID
     *
     * @param id  the first ID to look up
     * @param ids optional extra IDs to look up
     * @return SMS data matching the provided criteria
     */
    public SearchSmsResponse searchMessages(String id, String... ids)
            throws IOException, NexmoClientException {
        List<String> idList = new ArrayList<>(ids.length + 1);
        idList.add(id);
        idList.addAll(Arrays.asList(ids));
        return this.searchMessages(new SmsIdSearchRequest(idList));
    }

    /**
     * Search for completed SMS transactions by date and recipient MSISDN.
     *
     * @param date the date of the SMS message to be looked up
     * @param to   the MSISDN number of the SMS recipient
     * @return SMS data matching the provided criteria
     */
    public SearchSmsResponse searchMessages(Date date, String to)
            throws IOException, NexmoClientException {
        return this.searchMessages(new SmsDateSearchRequest(date, to));
    }
}
