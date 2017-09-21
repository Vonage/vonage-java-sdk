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
package com.nexmo.client.applications;

import com.nexmo.client.HttpWrapper;
import com.nexmo.client.NexmoClient;
import com.nexmo.client.NexmoClientException;
import com.nexmo.client.applications.endpoints.ApplicationsEndpoint;

import java.io.IOException;

/**
 * A client for talking to the Nexmo Number Applications API.
 * The standard way to obtain an instance of this class is to
 * use
 * {@link NexmoClient#getApplicationClient()}.
 */
public class ApplicationClient {
    protected ApplicationsEndpoint applications;

    /**
     * Constructor.
     *
     * @param httpWrapper (required) shared HTTP wrapper object used for making REST calls.
     */
    public ApplicationClient(HttpWrapper httpWrapper) {
        this.applications = new ApplicationsEndpoint(httpWrapper);
    }

    public ApplicationDetails createApplication(CreateApplicationRequest request)
            throws IOException, NexmoClientException {
        return this.applications.post(request);
    }

    public ApplicationDetails updateApplication(UpdateApplicationRequest request)
            throws IOException, NexmoClientException {
        return this.applications.put(request);
    }

    public ListApplicationsResponse listApplications(ListApplicationsRequest request)
            throws IOException, NexmoClientException {
        return this.applications.get(request);
    }

    public ApplicationDetails getApplication(String applicationId)
            throws IOException, NexmoClientException {
        return this.applications.get(applicationId);
    }

    public void deleteApplication(String applicationId)
            throws IOException, NexmoClientException {
        this.applications.delete(applicationId);
    }
}
