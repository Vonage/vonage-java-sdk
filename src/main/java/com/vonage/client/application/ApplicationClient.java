/*
 *   Copyright 2023 Vonage
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
import com.vonage.client.auth.TokenAuthMethod;
import com.vonage.client.common.HttpMethod;
import java.util.List;
import java.util.function.Function;

/**
 * A client for talking to the Vonage Application API. The standard way to obtain an instance of
 * this class is to use {@link VonageClient#getApplicationClient()}
 */
public class ApplicationClient {
    final RestEndpoint<ListApplicationRequest, ApplicationList> listApplications;
    final RestEndpoint<Application, Application> createApplication;
    final RestEndpoint<String, Application> getApplication;
    final RestEndpoint<Application, Application> updateApplication;
    final RestEndpoint<String, Void> deleteApplication;

    public ApplicationClient(HttpWrapper wrapper) {
        @SuppressWarnings("unchecked")
        final class Endpoint<T, R> extends DynamicEndpoint<T, R> {
            Endpoint(Function<T, String> pathGetter, HttpMethod method, R... type) {
                super(DynamicEndpoint.<T, R>builder((Class<R>) type.getClass().getComponentType())
                        .responseExceptionType(VonageApiResponseException.class)
                        .wrapper(wrapper).requestMethod(method)
                        .authMethod(TokenAuthMethod.class).applyAsBasicAuth()
                        .pathGetter((de, req) -> {
                            String base = de.getHttpWrapper().getHttpConfig().getVersionedApiBaseUri("v2");
                            String path = base + "/applications";
                            if (pathGetter != null) {
                                path += "/" + pathGetter.apply(req);
                            }
                            return path;
                        })
                );
            }
        }

        listApplications = new Endpoint<>(null, HttpMethod.GET);
        createApplication = new Endpoint<>(null, HttpMethod.POST);
        getApplication = new Endpoint<>(id -> id, HttpMethod.GET);
        updateApplication = new Endpoint<>(Application::getId, HttpMethod.PUT);
        deleteApplication = new Endpoint<>(id -> id, HttpMethod.DELETE);
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
        return createApplication.execute(application);
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
        return updateApplication.execute(application);
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
        return getApplication.execute(id);
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
        deleteApplication.execute(id);
    }

    /**
     * Lists the first 1000 available applications.
     *
     * @return The list of available applications.
     *
     * @since 7.7.0
     */
    public List<Application> listAllApplications() {
        return listApplications(ListApplicationRequest.builder().pageSize(1000).build()).getApplications();
    }

    /**
     * Lists the first page of available applications.
     *
     * @return The ApplicationList HAL response.
     *
     * @throws VonageResponseParseException if the response from the API could not be parsed.
     * @throws VonageClientException        if there was a problem with the Vonage request.
     */
    public ApplicationList listApplications() throws VonageResponseParseException, VonageClientException {
        return listApplications(ListApplicationRequest.builder().build());
    }

    /**
     * List the available applications.
     *
     * @param listApplicationRequest The page and number of applications per page to list.
     *
     * @return The ApplicationList HAL response.
     *
     * @throws VonageResponseParseException if the response from the API could not be parsed.
     * @throws VonageClientException        if there was a problem with the Vonage request.
     */
    public ApplicationList listApplications(ListApplicationRequest listApplicationRequest) throws VonageResponseParseException, VonageClientException {
        return listApplications.execute(listApplicationRequest);
    }
}
