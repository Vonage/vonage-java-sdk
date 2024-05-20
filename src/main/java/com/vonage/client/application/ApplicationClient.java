/*
 *   Copyright 2024 Vonage
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
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;

/**
 * A client for talking to the Vonage Application API. The standard way to obtain an instance of
 * this class is to use {@link VonageClient#getApplicationClient()}.
 */
public class ApplicationClient {
    final RestEndpoint<ListApplicationRequest, ApplicationList> listApplications;
    final RestEndpoint<Application, Application> createApplication;
    final RestEndpoint<UUID, Application> getApplication;
    final RestEndpoint<Application, Application> updateApplication;
    final RestEndpoint<UUID, Void> deleteApplication;

    public ApplicationClient(HttpWrapper wrapper) {
        @SuppressWarnings("unchecked")
        final class Endpoint<T, R> extends DynamicEndpoint<T, R> {
            Endpoint(Function<T, String> pathGetter, HttpMethod method, R... type) {
                super(DynamicEndpoint.<T, R> builder(type)
                        .responseExceptionType(ApplicationResponseException.class)
                        .wrapper(wrapper).requestMethod(method)
                        .authMethod(TokenAuthMethod.class)
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
        getApplication = new Endpoint<>(UUID::toString, HttpMethod.GET);
        updateApplication = new Endpoint<>(Application::getId, HttpMethod.PUT);
        deleteApplication = new Endpoint<>(UUID::toString, HttpMethod.DELETE);
    }

    private Application validateApplication(Application request) {
        return Objects.requireNonNull(request, "Application request is required.");
    }

    private UUID validateApplicationId(String id) {
        return UUID.fromString(Objects.requireNonNull(id, "Application ID is required."));
    }

    /**
     * Create a new application.
     *
     * @param application The application properties for the application to be created with.
     *
     * @return The application which has been created.
     *
     * @throws ApplicationResponseException If there was an error processing the request.
     */
    public Application createApplication(Application application) throws ApplicationResponseException {
        return createApplication.execute(validateApplication(application));
    }

    /**
     * Update an existing application.
     *
     * @param application The application properties for the application to be updated with.
     *
     * @return The application which has been updated.
     *
     * @throws ApplicationResponseException If there was an error processing the request.
     */
    public Application updateApplication(Application application) throws ApplicationResponseException {
        return updateApplication.execute(validateApplication(application));
    }

    /**
     * Retrieve an application.
     *
     * @param applicationId The UUID of the application to retrieve as a string.
     *
     * @return The corresponding application.
     *
     * @throws ApplicationResponseException If there was an error processing the request.
     */
    public Application getApplication(String applicationId) throws ApplicationResponseException {
        return getApplication.execute(validateApplicationId(applicationId));
    }

    /**
     * Delete an application.
     *
     * @param applicationId The UUID of the application to delete as a string.
     *
     * @throws ApplicationResponseException If there was an error processing the request.
     */
    public void deleteApplication(String applicationId) throws ApplicationResponseException {
        deleteApplication.execute(validateApplicationId(applicationId));
    }

    /**
     * Lists the first 1000 available applications.
     *
     * @return The list of available applications.
     *
     * @throws ApplicationResponseException If there was an error processing the request.
     *
     * @since 7.7.0
     */
    public List<Application> listAllApplications() throws ApplicationResponseException {
        return listApplications(ListApplicationRequest.builder().pageSize(1000).build()).getApplications();
    }

    /**
     * Lists the first page of available applications.
     *
     * @return The ApplicationList HAL response.
     *
     * @throws ApplicationResponseException If there was an error processing the request.
     */
    public ApplicationList listApplications() throws ApplicationResponseException {
        return listApplications(ListApplicationRequest.builder().build());
    }

    /**
     * List the available applications.
     *
     * @param listApplicationRequest The page and number of applications per page to list.
     *
     * @return The ApplicationList HAL response.
     *
     * @throws ApplicationResponseException If there was an error processing the request.
     */
    public ApplicationList listApplications(ListApplicationRequest listApplicationRequest) throws ApplicationResponseException {
        return listApplications.execute(listApplicationRequest);
    }
}
