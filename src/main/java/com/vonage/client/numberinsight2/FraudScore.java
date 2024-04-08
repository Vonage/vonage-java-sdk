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
 * Represents the fraud score insight results in {@link FraudCheckResponse#getFraudScore()}.
 */
public class FraudScore extends JsonableBaseObject {
	private Integer riskScore;
	private RiskRecommendation riskRecommendation;
	private RiskLabel label;
	private FraudScoreStatus status;

	protected FraudScore() {}

	/**
	 * Score derived from evaluating fraud-related data associated with the phone number. This ranges from 0-100, with 0 meaning least risk and 100 meaning highest risk.
	 * 
	 * @return The risk score as an Integer between 0 and 100.
	 */
	@JsonProperty("risk_score")
	public Integer getRiskScore() {
		return riskScore;
	}

	/**
	 * Recommended action based on the risk score.
	 * 
	 * @return The recommendation as an enum.
	 */
	@JsonProperty("risk_recommendation")
	public RiskRecommendation getRiskRecommendation() {
		return riskRecommendation;
	}

	/**
	 * Mapping of risk score to a verbose description.
	 * 
	 * @return The risk label as an enum.
	 */
	@JsonProperty("label")
	public RiskLabel getLabel() {
		return label;
	}

	/**
	 * Status of the fraud score API call.
	 * 
	 * @return The insight status as an enum.
	 */
	@JsonProperty("status")
	public FraudScoreStatus getStatus() {
		return status;
	}
}
