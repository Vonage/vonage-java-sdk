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
package com.vonage.client.sns;


import com.vonage.client.HttpWrapper;
import com.vonage.client.VonageClient;
import com.vonage.client.VonageClientException;
import com.vonage.client.VonageResponseParseException;
import com.vonage.client.sns.request.SnsPublishRequest;
import com.vonage.client.sns.request.SnsSubscribeRequest;
import com.vonage.client.sns.response.SnsPublishResponse;
import com.vonage.client.sns.response.SnsSubscribeResponse;

/**
 * A client for talking to the Vonage Voice API. The standard way to obtain an instance of this class is to use {@link
 * VonageClient#getSnsClient()}.
 */
public class SnsClient {
    private SnsEndpoint endpoint;

    /**
     * Constructor.
     *
     * @param httpWrapper (required) shared HTTP wrapper object used for making REST calls.
     */
    public SnsClient(HttpWrapper httpWrapper) {
        endpoint = new SnsEndpoint(httpWrapper);
    }

    public SnsPublishResponse publish(SnsPublishRequest request) throws VonageClientException, VonageResponseParseException {
        return (SnsPublishResponse) endpoint.execute(request);
    }

    public SnsSubscribeResponse subscribe(SnsSubscribeRequest request) throws VonageClientException, VonageResponseParseException {
        return (SnsSubscribeResponse) endpoint.execute(request);
    }
}
