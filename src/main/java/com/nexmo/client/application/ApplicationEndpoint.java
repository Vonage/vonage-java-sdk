/*
 * Copyright (c) 2011-2019 Vonage Inc
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

    Application create(Application application) {
        return this.createApplicationMethod.execute(application);
    }

    Application update(Application application) {
        return this.updateApplicationMethod.execute(application);
    }

    Application get(String id) {
        return this.getApplicationMethod.execute(id);
    }

    void delete(String id) {
        this.deleteApplicationMethod.execute(id);
    }

    ApplicationList list(ListApplicationRequest request) {
        return this.listApplicationsMethod.execute(request);
    }
}
