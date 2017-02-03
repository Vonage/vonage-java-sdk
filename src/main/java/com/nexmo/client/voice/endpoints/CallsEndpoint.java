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
package com.nexmo.client.voice.endpoints;

import com.nexmo.client.HttpWrapper;
import com.nexmo.client.NexmoClientException;
import com.nexmo.client.voice.*;

import java.io.IOException;

/**
 * Represents Nexmo Voice API endpoints below <pre>/calls</pre>, and provides methods to read and update those endpoints.
 */
public class CallsEndpoint {
    private final CreateCallMethod createCall;
    private final ReadCallMethod readCall;
    private final ListCallsMethod listCalls;
    private final ModifyCallMethod modifyCall;

    public CallsEndpoint(HttpWrapper httpWrapper) {
        this.createCall = new CreateCallMethod(httpWrapper);
        this.readCall = new ReadCallMethod(httpWrapper);
        this.listCalls = new ListCallsMethod(httpWrapper);
        this.modifyCall = new ModifyCallMethod(httpWrapper);
    }

    /**
     * Start a call configured by the provided {@link Call} object.
     * <p>
     * Requires a {@link com.nexmo.client.auth.JWTAuthMethod} to be provided to the NexmoClient which constructs
     *
     * @param callRequest A Call object configuring the call to be created
     * @return A CallEvent describing the call that was initiated.
     * @throws IOException if an error occurs communicating with the Nexmo API
     * @throws NexmoClientException if an error occurs constructing the Nexmo API request or response
     */
    public CallEvent post(Call callRequest) throws IOException, NexmoClientException {
        return this.createCall.execute(callRequest);
    }

    /**
     * List calls which have been conducted by
     * @param filter A CallsFilter describing the calls to be searched, or {@code null} for all calls.
     * @return A CallInfoPage containing a single page of {@link CallInfo} results
     * @throws IOException if an error occurs communicating with the Nexmo API
     * @throws NexmoClientException if an error occurs constructing the Nexmo API request or response
     */
    public CallInfoPage get(CallsFilter filter) throws IOException, NexmoClientException {
        return this.listCalls.execute(filter);
    }

    /**
     * List calls which have been conducted by
     * @param uuid The uuid of the CallInfo object to be retrieved
     * @return A CallInfo object describing the state of the call that was made or is in progress
     * @throws IOException if an error occurs communicating with the Nexmo API
     * @throws NexmoClientException if an error occurs constructing the Nexmo API request or response
     */
    public CallInfo get(String uuid) throws IOException, NexmoClientException {
        return this.readCall.execute(uuid);
    }

    /**
     * Modify an ongoing call.
     *
     * @param uuid The uuid of the CallInfo object to be modified
     * @return A CallInfo object describing the state of the call that was modified
     * @throws IOException if an error occurs communicating with the Nexmo API
     * @throws NexmoClientException if an error occurs constructing the Nexmo API request or response
     */
    public CallInfo put(String uuid, String action) throws IOException, NexmoClientException {
        return this.modifyCall.execute(new CallModifier(uuid, action));
    }
}