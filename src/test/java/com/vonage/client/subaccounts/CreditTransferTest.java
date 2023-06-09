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
package com.vonage.client.subaccounts;

import com.vonage.client.VonageUnexpectedException;
import static org.junit.Assert.*;
import org.junit.Test;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public class CreditTransferTest {
	
	@Test
	public void testSerializeAndParseAllParameters() {
		CreditTransfer request = CreditTransfer.builder().amount(BigDecimal.valueOf(123.45))
				.from("7c9738e6").to("ad6dc56f").reference("This gets added to the audit log").build();
		
		String json = request.toJson();
		assertTrue(json.contains("\"amount\":"+request.getAmount()));
		assertTrue(json.contains("\"from\":\""+request.getFrom()+"\""));
		assertTrue(json.contains("\"to\":\""+request.getTo()+"\""));
		assertTrue(json.contains("\"reference\":\""+request.getReference()+"\""));

		CreditTransfer response = CreditTransfer.fromJson(json);
		assertEquals(request.getAmount(), response.getAmount());
		assertEquals(request.getFrom(), response.getFrom());
		assertEquals(request.getTo(), response.getTo());
		assertEquals(request.getReference(), response.getReference());
	}
	
	@Test
	public void testFromJsonAllFields() {
		UUID creditTransferId = UUID.randomUUID();
		Instant createdAt = Instant.parse("2019-03-02T16:34:49Z");
		BigDecimal amount = BigDecimal.valueOf(453.12);
		String from = "7c9738e6";
		String to = "ad6dc56f";
		String reference = "This gets added to the audit log";
	
		CreditTransfer response = CreditTransfer.fromJson("{\n" +
				"\"credit_transfer_id\":\""+creditTransferId+"\",\n" +
				"\"created_at\":\""+createdAt+"\",\n" +
				"\"amount\":\""+amount+"\",\n" +
				"\"from\":\""+from+"\",\n" +
				"\"to\":\""+to+"\",\n" +
				"\"reference\":\""+reference+"\"\n" +
		"}");
		
		assertEquals(creditTransferId, response.getCreditTransferId());
		assertEquals(createdAt, response.getCreatedAt());
		assertEquals(amount, response.getAmount());
		assertEquals(from, response.getFrom());
		assertEquals(to, response.getTo());
		assertEquals(reference, response.getReference());
	}
	
	@Test(expected = VonageUnexpectedException.class)
	public void testFromJsonInvalid() {
		CreditTransfer.fromJson("{malformed]");
	}

	@Test
	public void testFromJsonEmpty() {
		CreditTransfer response = CreditTransfer.fromJson("{}");
		assertNull(response.getCreditTransferId());
		assertNull(response.getCreatedAt());
		assertNull(response.getAmount());
		assertNull(response.getFrom());
		assertNull(response.getTo());
		assertNull(response.getReference());
	}
}