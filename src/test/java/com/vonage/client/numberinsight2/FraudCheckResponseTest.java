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

import com.vonage.client.TestUtils;
import com.vonage.client.VonageResponseParseException;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import java.util.UUID;

public class FraudCheckResponseTest {
	
	@Test
	public void testFromJsonAllFields() {
		String requestId = "3f92ed75-e624-4503-abbd-a93d6b442571",
				number = "16197363066",
				carrier = "Google (Grand Central) - SVR",
				type = "Toll-Free",
				riskScore = "21",
				recommendation = "allow",
				label = "low",
				fraudStatus = "completed",
				simStatus = "failed",
				reason = "Mobile Network Operator Not Supported";
		boolean swapped = true;

		FraudCheckResponse response = FraudCheckResponse.fromJson("{\n" +
			"   \"request_id\": \""+requestId+"\",\n" +
			"   \"type\": \"phone\",\n" +
			"   \"phone\": {\n" +
			"      \"phone\": \""+number+"\",\n" +
			"      \"carrier\": \""+carrier+"\",\n" +
			"      \"type\": \""+type+"\"\n" +
			"   },\n" +
			"   \"fraud_score\": {\n" +
			"      \"risk_score\": \""+riskScore+"\",\n" +
			"      \"risk_recommendation\": \""+recommendation+"\",\n" +
			"      \"label\": \""+label+"\",\n" +
			"      \"status\": \""+fraudStatus+"\"\n" +
			"   },\n" +
			"   \"sim_swap\": {\n" +
			"      \"status\": \""+simStatus+"\",\n" +
			"      \"swapped\": "+swapped+",\n" +
			"      \"reason\": \""+reason+"\"\n" +
			"   }\n" +
			"}"
		);

		TestUtils.testJsonableBaseObject(response);
		assertEquals("phone", response.getType());
		assertEquals(UUID.fromString(requestId), response.getRequestId());
		assertEquals("phone", response.getType());
		Phone phone = response.getPhone();
		assertNotNull(phone);
		assertEquals(number, phone.getNumber());
		assertEquals(carrier, phone.getCarrier());
		assertEquals(PhoneType.TOLL_FREE, phone.getType());
		FraudScore fraudScore = response.getFraudScore();
		assertNotNull(fraudScore);
		assertEquals(Integer.valueOf(riskScore), fraudScore.getRiskScore());
		assertEquals(RiskRecommendation.ALLOW, fraudScore.getRiskRecommendation());
		assertEquals(FraudScoreStatus.COMPLETED, fraudScore.getStatus());
		SimSwap simSwap = response.getSimSwap();
		assertNotNull(simSwap);
		assertEquals(SimSwapStatus.FAILED, simSwap.getStatus());
		assertEquals(swapped, simSwap.getSwapped());
		assertEquals(reason, simSwap.getReason());
	}
	
	@Test
	public void testFromJsonInvalid() {
		assertThrows(VonageResponseParseException.class, () -> FraudCheckResponse.fromJson("{malformed]"));
	}

	@Test
	public void testFromJsonEmpty() {
		FraudCheckResponse response = FraudCheckResponse.fromJson("{}");
		assertNull(response.getType());
		assertNull(response.getRequestId());
		assertNull(response.getPhone());
		assertNull(response.getFraudScore());
		assertNull(response.getSimSwap());
	}

	@Test
	public void testEmptyFraudScoreAndSimSwap() {
		String phoneNumber = "11234567890";
		UUID requestId = UUID.randomUUID();
		String json = "{\"request_id\":\""+requestId+"\",\"phone\":{" +
				"\"phone\":\""+phoneNumber+"\",\"type\":\"PrePaid\"}," +
				"\"fraud_score\":{},\"sim_swap\":{}}";

		FraudCheckResponse parsed = FraudCheckResponse.fromJson(json);
		assertNotNull(parsed);
		Phone phone = parsed.getPhone();
		assertNotNull(phone);
		assertEquals(phoneNumber, phone.getNumber());
		assertNull(phone.getCarrier());
		assertEquals(PhoneType.PREPAID, phone.getType());
		FraudScore fraudScore = parsed.getFraudScore();
		assertNotNull(fraudScore);
		assertNull(fraudScore.getRiskScore());
		assertNull(fraudScore.getRiskRecommendation());
		assertNull(fraudScore.getLabel());
		assertNull(fraudScore.getStatus());
		SimSwap simSwap = parsed.getSimSwap();
		assertNotNull(simSwap);
		assertNull(simSwap.getSwapped());
		assertNull(simSwap.getReason());
		assertNull(simSwap.getStatus());
	}

	@Test
	public void testUnmappedPhoneTypeEnum() {
		String json = "{\"phone\":{\"type\":\"Banana\"}}";
		FraudCheckResponse parsed = FraudCheckResponse.fromJson(json);
		assertEquals(PhoneType.UNMAPPED, parsed.getPhone().getType());
		assertNull(PhoneType.fromString(null));
	}
}