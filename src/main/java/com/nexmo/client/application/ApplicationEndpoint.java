/*
 * Copyright (c) 2011-2019 Nexmo Inc
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
package com.nexmo.client.application;

import com.nexmo.client.HttpWrapper;
import com.nexmo.client.NexmoClientException;

import java.io.IOException;

class ApplicationEndpoint {
    private CreateApplicationMethod createApplicationMethod;
    private UpdateApplicationMethod updateApplicationMethod;
    private GetApplicationMethod getApplicationMethod;
    private DeleteApplicationMethod deleteApplicationMethod;
    private ListApplicationsMethod listApplicationsMethod;

    ApplicationEndpoint(HttpWrapper httpWrapper) {
        this.createApplicationMethod = new CreateApplicationMethod(httpWrapper);
        this.updateApplicationMethod = new UpdateApplicationMethod(httpWrapper);
        this.getApplicationMethod = new GetApplicationMethod(httpWrapper);
        this.deleteApplicationMethod = new DeleteApplicationMethod(httpWrapper);
        this.listApplicationsMethod = new ListApplicationsMethod(httpWrapper);
    }

    Application create(Application application) throws IOException, NexmoClientException {
        return this.createApplicationMethod.execute(application);
    }

    Application update(Application application) throws IOException, NexmoClientException {
        return this.updateApplicationMethod.execute(application);
    }

    Application get(String id) throws IOException, NexmoClientException {
        return this.getApplicationMethod.execute(id);
    }

    void delete(String id) throws IOException, NexmoClientException {
        this.deleteApplicationMethod.execute(id);
    }

    public ApplicationList list(ListApplicationRequest request) throws IOException, NexmoClientException {
        return this.listApplicationsMethod.execute(request);
    }
}
