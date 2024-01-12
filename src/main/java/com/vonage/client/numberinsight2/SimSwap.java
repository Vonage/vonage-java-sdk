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
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.JsonableBaseObject;

/**
 * Represents the sim swap insight results in {@link FraudCheckResponse#getSimSwap()}.
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SimSwap extends JsonableBaseObject {
	private SimSwapStatus status;
	private Boolean swapped;
	private String reason;

	protected SimSwap() {}

	/**
	 * Status of the sim swap call.
	 * 
	 * @return The insight status as an enum.
	 */
	@JsonProperty("status")
	public SimSwapStatus getStatus() {
		return status;
	}

	/**
	 * Whether the sim was swapped within the past week. Returned only if the sim swap check succeeds.
	 * 
	 * @return {@code true} if the sim was swapped in the last 7 days, or {@code null} if the check failed.
	 */
	@JsonProperty("swapped")
	public Boolean getSwapped() {
		return swapped;
	}

	/**
	 * Reason for a sim swap error response. Returned only if the sim swap check fails.
	 * 
	 * @return The error response description, or {@code null} if the call succeeded.
	 */
	@JsonProperty("reason")
	public String getReason() {
		return reason;
	}
}
