/*
 *   Copyright 2025 Vonage
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
package com.vonage.client.identityinsights;

import com.vonage.client.DynamicEndpoint;
import com.vonage.client.HttpWrapper;
import com.vonage.client.RestEndpoint;
import com.vonage.client.VonageClient;
import com.vonage.client.auth.JWTAuthMethod;
import com.vonage.client.camara.CamaraResponseException;
import com.vonage.client.common.HttpMethod;

/**
 * A client for communicating with the Vonage Identity Insights API.
 * The standard way to obtain an instance of this class is to use 
 * {@link VonageClient#getIdentityInsightsClient()}.
 * <p>
 * The Identity Insights API allows clients to request real-time information related to a phone number.
 * Users can retrieve any combination of different datasets, known as insights (e.g., number formatting,
 * SIM swap information), in a single API call.
 * </p>
 * <p>
 * Each insight is processed independently, and the response includes a structured result for each
 * insight along with a status code indicating success or the type of error encountered.
 * </p>
 *
 * @since 9.1.0
 */
public class IdentityInsightsClient {
    final RestEndpoint<IdentityInsightsRequest, IdentityInsightsResponse> getInsights;
    
    /**
     * Create a new IdentityInsightsClient.
     *
     * @param wrapper Http Wrapper used to create requests.
     */
    public IdentityInsightsClient(HttpWrapper wrapper) {
        @SuppressWarnings("unchecked")
        class Endpoint extends DynamicEndpoint<IdentityInsightsRequest, IdentityInsightsResponse> {
            Endpoint(IdentityInsightsResponse... type) {
                super(DynamicEndpoint.<IdentityInsightsRequest, IdentityInsightsResponse>builder(type)
                        .authMethod(JWTAuthMethod.class)
                        .responseExceptionType(CamaraResponseException.class)
                        .requestMethod(HttpMethod.POST)
                        .wrapper(wrapper)
                        .pathGetter((de, req) -> 
                            wrapper.getHttpConfig().getApiEuBaseUri() + "/identity-insights/v1/requests"
                        )
                );
            }
        }
        
        getInsights = new Endpoint();
    }
    
    /**
     * Retrieve multiple insights for a phone number in a single request.
     * <p>
     * Each insight is processed independently. The response will include a status code for each
     * requested insight, which can be:
     * </p>
     * <ul>
     *   <li><b>OK</b> - The insight was processed successfully.</li>
     *   <li><b>INVALID_NUMBER_FORMAT</b> - The phone number format is not valid.</li>
     *   <li><b>NO_COVERAGE</b> - The country or mobile network is not supported.</li>
     *   <li><b>NOT_FOUND</b> - The phone number could not be found for this insight.</li>
     *   <li><b>UNAUTHORIZED</b> - The request could not be authorized.</li>
     *   <li><b>INVALID_PURPOSE</b> - The purpose used is not valid or allowed.</li>
     *   <li><b>SUPPLIER_ERROR</b> - The supplier returned an error.</li>
     *   <li><b>INTERNAL_ERROR</b> - An internal error occurred.</li>
     * </ul>
     * <p>
     * Example usage:
     * </p>
     * <pre>{@code
     * IdentityInsightsRequest request = new IdentityInsightsRequest("+14155552671")
     *     .purpose("FraudPreventionAndDetection")
     *     .format()
     *     .simSwap(240)
     *     .currentCarrier();
     * 
     * IdentityInsightsResponse response = client.getInsights(request);
     * 
     * if (response.getInsights().getFormat() != null) {
     *     FormatInsightResponse format = response.getInsights().getFormat();
     *     if (format.getStatus().getCode() == InsightStatusCode.OK) {
     *         System.out.println("Country: " + format.getCountryName());
     *         System.out.println("Valid format: " + format.getIsFormatValid());
     *     }
     * }
     * }</pre>
     *
     * @param request The insights request specifying the phone number and desired insights.
     * @return The insights response containing results for each requested insight.
     * 
     * @throws CamaraResponseException If the request was unsuccessful. This could be for the following reasons:
     * <ul>
     *     <li><b>400</b> - Bad Request: Invalid request arguments or malformed JSON.</li>
     *     <li><b>401</b> - Unauthorized: Authentication credentials are missing, invalid, or expired.</li>
     *     <li><b>405</b> - Method Not Allowed: Request method not supported.</li>
     *     <li><b>415</b> - Unsupported Media Type: Invalid Content-Type header.</li>
     *     <li><b>422</b> - Unprocessable Entity: The request failed due to validation errors.</li>
     *     <li><b>429</b> - Too Many Requests: Rate limit exceeded.</li>
     *     <li><b>500</b> - Internal Server Error: An unexpected error occurred on the server.</li>
     *     <li><b>503</b> - Service Unavailable: Failed to invoke upstream service.</li>
     * </ul>
     */
    public IdentityInsightsResponse getInsights(IdentityInsightsRequest request) {
        return getInsights.execute(request);
    }
}
