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
package com.vonage.client.camara.simswap;

import com.vonage.client.DynamicEndpoint;
import com.vonage.client.HttpWrapper;
import com.vonage.client.RestEndpoint;
import com.vonage.client.VonageClient;
import com.vonage.client.auth.camara.BackendAuthRequest;
import com.vonage.client.auth.camara.NetworkAuthMethod;
import com.vonage.client.auth.camara.FraudPreventionDetectionScope;
import static com.vonage.client.auth.camara.FraudPreventionDetectionScope.CHECK_SIM_SWAP;
import static com.vonage.client.auth.camara.FraudPreventionDetectionScope.RETRIEVE_SIM_SWAP_DATE;
import com.vonage.client.camara.CamaraResponseException;
import com.vonage.client.camara.NetworkApiClient;
import com.vonage.client.common.HttpMethod;
import java.time.Instant;

/**
 * A client for communicating with the Vonage SIM Swap API. The standard way to obtain an instance
 * of this class is to use {@link VonageClient#getSimSwapClient()}.
 *
 * @deprecated This API will be removed in the next major release.
 */
@Deprecated
public class SimSwapClient extends NetworkApiClient {
    final RestEndpoint<SimSwapRequest, CheckSimSwapResponse> check;
    final RestEndpoint<SimSwapRequest, SimSwapDateResponse> retrieveDate;

    /**
     * Create a new SimSwapClient.
     *
     * @param wrapper Http Wrapper used to create requests.
     */
    public SimSwapClient(HttpWrapper wrapper) {
        super(wrapper);

        @SuppressWarnings("unchecked")
        class Endpoint<R> extends DynamicEndpoint<SimSwapRequest, R> {
            Endpoint(String path, FraudPreventionDetectionScope scope, R... type) {
                super(DynamicEndpoint.<SimSwapRequest, R> builder(type)
                        .authMethod(NetworkAuthMethod.class)
                        .responseExceptionType(CamaraResponseException.class)
                        .requestMethod(HttpMethod.POST).wrapper(wrapper).pathGetter((de, req) -> {
                            setNetworkAuth(new BackendAuthRequest(req.getPhoneNumber(), scope));
                            return getCamaraBaseUri() + "sim-swap/v040/" + path;
                        })
                );
            }
        }

        check = new Endpoint<>("check", CHECK_SIM_SWAP);
        retrieveDate = new Endpoint<>("retrieve-date", RETRIEVE_SIM_SWAP_DATE);
    }

    private boolean checkSimSwap(SimSwapRequest request) {
        return check.execute(request).getSwapped();
    }

    /**
     * Check if SIM swap has been performed within the last 240 hours for the given phone number.
     * Use {@linkplain #checkSimSwap(String, int)} to specify the time period.
     *
     * @param phoneNumber Subscriber number in E.164 format (starting with country code). Optionally prefixed with '+'.
     *
     * @return {@code true} if the SIM card has been swapped during the period within the provided age.
     *
     * @throws CamaraResponseException If the request was unsuccessful. This could be for the following reasons:
     * <ul>
     *     <li><b>400</b>: Invalid request arguments.</li>
     *     <li><b>401</b>: Request not authenticated due to missing, invalid, or expired credentials.</li>
     *     <li><b>403</b>: Client does not have sufficient permissions to perform this action.</li>
     *     <li><b>404</b>: SIM Swap can't be checked because the phone number is unknown.</li>
     *     <li><b>409</b>: Another request is created for the same MSISDN.</li>
     *     <li><b>502</b>: Bad gateway.</li>
     * </ul>
     */
    public boolean checkSimSwap(String phoneNumber) {
        return checkSimSwap(new SimSwapRequest(phoneNumber));
    }

    /**
     * Check if SIM swap has been performed during the specified past period for the given phone number.
     *
     * @param phoneNumber Subscriber number in E.164 format (starting with country code). Optionally prefixed with '+'.
     * @param maxAgeHours Period in hours to be checked for SIM swap. Must be between 1 and 2400.
     *
     * @return {@code true} if the SIM card has been swapped during the period within the provided age.
     *
     * @throws CamaraResponseException If the request was unsuccessful. This could be for the following reasons:
     * <ul>
     *     <li><b>400</b>: Invalid request arguments.</li>
     *     <li><b>401</b>: Request not authenticated due to missing, invalid, or expired credentials.</li>
     *     <li><b>403</b>: Client does not have sufficient permissions to perform this action.</li>
     *     <li><b>404</b>: SIM Swap can't be checked because the phone number is unknown.</li>
     *     <li><b>409</b>: Another request is created for the same MSISDN.</li>
     *     <li><b>502</b>: Bad gateway.</li>
     * </ul>
     */
    public boolean checkSimSwap(String phoneNumber, int maxAgeHours) {
        return checkSimSwap(new SimSwapRequest(phoneNumber, maxAgeHours));
    }

    /**
     * Get timestamp of last MSISDN to IMSI pairing change for a mobile user account.
     *
     * @param phoneNumber Subscriber number in E.164 format (starting with country code). Optionally prefixed with '+'.
     *
     * @return Time of the latest SIM swap performed, or {@code null} if unknown / not applicable.
     *
     * @throws CamaraResponseException If the request was unsuccessful. This could be for the following reasons:
	 * <ul>
     *     <li><b>400</b>: Invalid request arguments.</li>
     *     <li><b>401</b>: Request not authenticated due to missing, invalid, or expired credentials.</li>
     *     <li><b>403</b>: Client does not have sufficient permissions to perform this action.</li>
     *     <li><b>404</b>: SIM Swap can't be checked because the phone number is unknown.</li>
	 *     <li><b>409</b>: Another request is created for the same MSISDN.</li>
	 *     <li><b>502</b>: Bad gateway.</li>
	 * </ul>
     */
    public Instant retrieveSimSwapDate(String phoneNumber) {
        return retrieveDate.execute(new SimSwapRequest(phoneNumber)).getLatestSimChange();
    }
}
