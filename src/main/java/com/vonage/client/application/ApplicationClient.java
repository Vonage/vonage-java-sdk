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


import com.vonage.client.*;

/**
 * A client for talking to the Vonage Application API. The standard way to obtain an instance of this class is to use
 * {@link VonageClient#getApplicationClient()}
 */
public class ApplicationClient extends AbstractClient {
    private final CreateApplicationEndpoint createApplicationEndpoint;
    private final UpdateApplicationEndpoint updateApplicationEndpoint;
    private final GetApplicationEndpoint getApplicationEndpoint;
    private final DeleteApplicationEndpoint deleteApplicationEndpoint;
    private final ListApplicationsEndpoint listApplicationsEndpoint;

    public ApplicationClient(HttpWrapper httpWrapper) {
        super(httpWrapper);
        createApplicationEndpoint = new CreateApplicationEndpoint(httpWrapper);
        updateApplicationEndpoint = new UpdateApplicationEndpoint(httpWrapper);
        getApplicationEndpoint = new GetApplicationEndpoint(httpWrapper);
        deleteApplicationEndpoint = new DeleteApplicationEndpoint(httpWrapper);
        listApplicationsEndpoint = new ListApplicationsEndpoint(httpWrapper);
    }

    /**
     * Create a new application.
     *
     * @param application The application properties for the application to be created with.
     *
     * @return The application which has been created.
     *
     * @throws VonageResponseParseException if the response from the API could not be parsed.
     * @throws VonageClientException        if there was a problem with the Vonage request.
     */
    public Application createApplication(Application application) throws VonageResponseParseException, VonageClientException {
        return createApplicationEndpoint.execute(application);
    }

    /**
     * Update an existing application.
     *
     * @param application The application properties for the application to be updated with.
     *
     * @return The application which has been updated.
     *
     * @throws VonageResponseParseException if the response from the API could not be parsed.
     * @throws VonageClientException        if there was a problem with the Vonage request.
     */
    public Application updateApplication(Application application) throws VonageResponseParseException, VonageClientException {
        return updateApplicationEndpoint.execute(application);
    }

    /**
     * Retrieve an application.
     *
     * @param id The id of the application to retrieve.
     *
     * @return The corresponding application.
     *
     * @throws VonageResponseParseException if the response from the API could not be parsed.
     * @throws VonageClientException        if there was a problem with the Vonage request.
     */
    public Application getApplication(String id) throws VonageResponseParseException, VonageClientException {
        return getApplicationEndpoint.execute(id);
    }

    /**
     * Delete an application.
     *
     * @param id The id of the application to delete.
     *
     * @throws VonageResponseParseException if the response from the API could not be parsed.
     * @throws VonageClientException        if there was a problem with the Vonage request.
     */
    public void deleteApplication(String id) throws VonageResponseParseException, VonageClientException {
        deleteApplicationEndpoint.execute(id);
    }

    /**
     * List the first page of available applications.
     *
     * @return The list of available applications.
     *
     * @throws VonageResponseParseException if the response from the API could not be parsed.
     * @throws VonageClientException        if there was a problem with the Vonage request.
     */
    public ApplicationList listApplications() throws VonageResponseParseException, VonageClientException {
        return listApplications(null);
    }

    /**
     * List the available applications.
     *
     * @param listApplicationRequest The page and number of applications per page to list.
     *
     * @return The list of available applications.
     *
     * @throws VonageResponseParseException if the response from the API could not be parsed.
     * @throws VonageClientException        if there was a problem with the Vonage request.
     */
    public ApplicationList listApplications(ListApplicationRequest listApplicationRequest) throws VonageResponseParseException, VonageClientException {
        return listApplicationsEndpoint.execute(listApplicationRequest);
    }
}
