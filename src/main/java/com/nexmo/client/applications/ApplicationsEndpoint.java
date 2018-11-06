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
import com.nexmo.client.NexmoClientException;

import java.io.IOException;

public class ApplicationsEndpoint {
    private CreateApplicationMethod create;
    private UpdateApplicationMethod update;
    private GetApplicationEndpoint get;
    private ListApplicationsEndpoint list;
    private DeleteApplicationMethod delete;

    public ApplicationsEndpoint(HttpWrapper httpWrapper) {
        this.create = new CreateApplicationMethod(httpWrapper);
        this.update = new UpdateApplicationMethod(httpWrapper);
        this.get = new GetApplicationEndpoint(httpWrapper);
        this.list = new ListApplicationsEndpoint(httpWrapper);
        this.delete = new DeleteApplicationMethod(httpWrapper);
    }

    public ApplicationDetails post(CreateApplicationRequest request)
            throws IOException, NexmoClientException {
        return this.create.execute(request);
    }

    public ApplicationDetails put(UpdateApplicationRequest request)
            throws IOException, NexmoClientException {
        return this.update.execute(request);
    }

    public ApplicationDetails get(String applicationId)
            throws IOException, NexmoClientException {
        return this.get.execute(applicationId);
    }

    public ListApplicationsResponse get(ListApplicationsRequest request)
            throws IOException, NexmoClientException {
        return this.list.execute(request);
    }

    public void delete(String applicationId)
            throws IOException, NexmoClientException {
        this.delete.execute(applicationId);
    }
}
