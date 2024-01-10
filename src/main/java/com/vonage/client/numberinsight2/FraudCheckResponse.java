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
package com.vonage.client.numberinsight2;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.Jsonable;
import java.util.UUID;

/**
 * Fraud check results as obtained from {@link NumberInsight2Client#fraudCheck(String, Insight, Insight...)}.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FraudCheckResponse implements Jsonable {
	private String type;
	private UUID requestId;
	private Phone phone;
	private FraudScore fraudScore;
	private SimSwap simSwap;

	protected FraudCheckResponse() {
	}

	/**
	 * The type of lookup used in the request. Currently always "phone".
	 *
	 * @return The lookup type as a string.
	 */
	@JsonProperty("type")
	protected String getType() {
		return type;
	}

	/**
	 * Unique ID for this request for reference.
	 * 
	 * @return The request reference UUID.
	 */
	@JsonProperty("request_id")
	public UUID getRequestId() {
		return requestId;
	}

	/**
	 * An object containing at least the phone number that was used in the fraud check. If {@linkplain Insight#FRAUD_SCORE} was also requested and successful, other phone information (carrier and type) will be returned.
	 * 
	 * @return Information about the phone number.
	 */
	@JsonProperty("phone")
	public Phone getPhone() {
		return phone;
	}

	/**
	 * Result of the fraud score insight operation. Only returned if {@linkplain Insight#FRAUD_SCORE} was requested.
	 * 
	 * @return The fraud score details, or {@code null} if not applicable.
	 */
	@JsonProperty("fraud_score")
	public FraudScore getFraudScore() {
		return fraudScore;
	}

	/**
	 * Result of the SIM swap insight operation. Only returned if {@linkplain Insight#SIM_SWAP} was requested.
	 * 
	 * @return The SIM swap details, or {@code null} if not applicable.
	 */
	@JsonProperty("sim_swap")
	public SimSwap getSimSwap() {
		return simSwap;
	}
	
	/**
	 * Creates an instance of this class from a JSON payload.
	 *
	 * @param json The JSON string to parse.
	 * @return An instance of this class with the fields populated, if present.
	 */
	public static FraudCheckResponse fromJson(String json) {
		return Jsonable.fromJson(json);
	}
}
