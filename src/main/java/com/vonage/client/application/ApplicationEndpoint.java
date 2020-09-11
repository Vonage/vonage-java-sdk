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
package com.vonage.client.application;

import com.vonage.client.HttpWrapper;

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
