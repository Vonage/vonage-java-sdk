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

    /**
     * Create a new Application.
     *
     * @param request A CreateApplicationRequest describing the application to be created.
     * @return An ApplicationDetails object describing the newly created application, including generated private and
     * public keys.
     */
    public ApplicationDetails createApplication(CreateApplicationRequest request)
            throws IOException, NexmoClientException {
        return this.applications.post(request);
    }

    /**
     * Update an existing application with the provided details.
     *
     * @param request An UpdateApplicationRequest describing the new application details.
     * @return An ApplicationDetails object describing the newly modified application.
     */
    public ApplicationDetails updateApplication(UpdateApplicationRequest request)
            throws IOException, NexmoClientException {
        return this.applications.put(request);
    }

    /**
     * List the applications associated with the authenticated account. By default returns the first page of
     * application objects.
     *
     * @param request A ListApplicationsRequest object containing the required paging information.
     * @return A ListApplicationsResponse, which is an Iterator of ApplicationDetails objects.
     */
    public ListApplicationsResponse listApplications(ListApplicationsRequest request)
            throws IOException, NexmoClientException {
        return this.applications.get(request);
    }

    /**
     * Obtain the details of an existing application.
     *
     * @param applicationId The id of the application
     * @return An ApplicationDetails object describing the requested application.
     */
    public ApplicationDetails getApplication(String applicationId)
            throws IOException, NexmoClientException {
        return this.applications.get(applicationId);
    }

    /**
     * Delete an application.
     *
     * @param applicationId The ID of the application to be deleted.
     */
    public void deleteApplication(String applicationId)
            throws IOException, NexmoClientException {
        this.applications.delete(applicationId);
    }
}
