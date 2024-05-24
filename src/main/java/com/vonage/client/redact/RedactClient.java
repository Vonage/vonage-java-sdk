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
package com.vonage.client.redact;

import com.vonage.client.*;
import com.vonage.client.auth.ApiKeyHeaderAuthMethod;
import com.vonage.client.common.HttpMethod;

/**
 * A client for talking to the Vonage Redact API. The standard way to obtain an instance of this class is to use {@link
 * VonageClient#getRedactClient()}.
 */
public class RedactClient {
    final RestEndpoint<RedactRequest, Void> redactTransaction;

    public RedactClient(HttpWrapper wrapper) {
        @SuppressWarnings("unchecked")
        final class Endpoint<T, R> extends DynamicEndpoint<T, R> {
            Endpoint(R... type) {
                super(DynamicEndpoint.<T, R> builder(type)
                        .wrapper(wrapper).requestMethod(HttpMethod.POST)
                        .responseExceptionType(VonageBadRequestException.class)
                        .authMethod(ApiKeyHeaderAuthMethod.class)
                        .pathGetter((de, req) -> de.getHttpWrapper().getHttpConfig()
                                .getVersionedApiBaseUri("v1") + "/redact/transaction"
                        )
                );
            }
        }
        redactTransaction = new Endpoint<>();
    }

    /**
     * Submit a request to the Redact API to redact a transaction.
     *
     * @param id      The transaction id to redact.
     * @param product The {@link RedactRequest.Product} which corresponds to the transaction.
     *
     * @throws VonageClientException        if there was a problem with the Vonage request or response objects.
     * @throws VonageResponseParseException if the response from the API could not be parsed.
     */
    public void redactTransaction(String id, RedactRequest.Product product) throws VonageResponseParseException, VonageClientException {
        redactTransaction(new RedactRequest(id, product));
    }

    /**
     * Submit a request to the Redact API to redact a transaction.
     *
     * @param id      The transaction id to redact.
     * @param product The {@link RedactRequest.Product} which corresponds to the transaction.
     * @param type    The {@link RedactRequest.Type} which is required if redacting SMS data.
     *
     * @throws VonageClientException        if there was a problem with the Vonage request or response objects.
     * @throws VonageResponseParseException if the response from the API could not be parsed.
     */
    public void redactTransaction(String id, RedactRequest.Product product, RedactRequest.Type type) throws VonageResponseParseException, VonageClientException {
        RedactRequest request = new RedactRequest(id, product);
        request.setType(type);
        redactTransaction(request);
    }

    /**
     * Submit a request to the Redact API to redact a transaction.
     *
     * @param redactRequest a {@link RedactRequest} object which contains the request parameters.
     *
     * @throws VonageClientException        if there was a problem with the Vonage request or response objects.
     * @throws VonageResponseParseException if the response from the API could not be parsed.
     */
    public void redactTransaction(RedactRequest redactRequest) throws VonageResponseParseException, VonageClientException {
        if (redactRequest.getId() == null || redactRequest.getProduct() == null) {
            throw new IllegalArgumentException("Redact transaction id and product are required.");
        }
        if (redactRequest.getProduct() == RedactRequest.Product.SMS && redactRequest.getType() == null) {
            throw new IllegalArgumentException("Redacting SMS requires a type.");
        }
        redactTransaction.execute(redactRequest);
    }
}
