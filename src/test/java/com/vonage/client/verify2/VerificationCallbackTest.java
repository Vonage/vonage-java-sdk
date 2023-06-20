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
package com.vonage.client.verify2;

import com.vonage.client.VonageResponseParseException;
import static org.junit.Assert.*;
import org.junit.Test;
import java.net.URI;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class VerificationCallbackTest {

	@Test
	public void testParseRegularEvent() {
		VerificationCallback webhook = VerificationCallback.fromJson("{\n" +
				"   \"request_id\": \"c11236f4-00bf-4b89-84ba-88b25df97315\",\n" +
				"   \"triggered_at\": \"2020-01-01T14:00:00.032Z\",\n" +
				"   \"type\": \"event\",\n" +
				"   \"channel\": \"whatsapp_interactive\",\n" +
				"   \"status\": \"completed\",\n" +
				"   \"finalized_at\": \"2020-01-01T15:00:00.001Z\",\n" +
				"   \"client_ref\": \"my-personal-ref\"\n" +
				"}"
		);
		assertEquals(UUID.fromString("c11236f4-00bf-4b89-84ba-88b25df97315"), webhook.getRequestId());
		assertEquals(Instant.ofEpochMilli(1577887200032L), webhook.getTriggeredAt());
		assertEquals(CallbackType.EVENT, webhook.getType());
		assertEquals(VerificationStatus.COMPLETED, webhook.getStatus());
		assertEquals(Instant.ofEpochMilli(1577890800001L), webhook.getFinalizedAt());
		assertEquals("my-personal-ref", webhook.getClientRef());
		assertNull(webhook.getChannelTimeout());
		assertNull(webhook.getSilentAuthUrl());
		assertNull(webhook.getWorkflows());
		assertNull(webhook.getSubmittedAt());
	}

	@Test
	public void testParseRequestUpdateMultipleWorkflows() {
		VerificationCallback webhook = VerificationCallback.fromJson("{\n" +
				"   \"request_id\": \"c11236f4-00bf-4b89-84ba-88b25df97325\",\n" +
				"   \"submitted_at\": \"2020-01-01T14:00:02.000Z\",\n" +
				"   \"status\": \"failed\",\n" +
				"   \"type\": \"summary\",\n" +
				"   \"channel_timeout\": 300,\n" +
				"   \"workflow\": [\n" +
				"      {\n" +
				"         \"channel\": \"sms\",\n" +
				"         \"initiated_at\": \"2020-01-01T14:00:00.000Z\",\n" +
				"         \"status\": \"expired\"\n" +
				"      },\n" +
				"      {\n" +
				"         \"channel\": \"whatsapp\",\n" +
				"         \"initiated_at\": \"2020-01-01T14:02:00.000Z\",\n" +
				"         \"status\": \"completed\"\n" +
				"      },\n" +
				"      {\n" +
				"         \"channel\": \"whatsapp_interactive\",\n" +
				"         \"initiated_at\": \"2020-01-01T15:05:00.000Z\",\n" +
				"         \"status\": \"user_rejected\"\n" +
				"      },\n" +
				"      {\n" +
				"         \"channel\": \"voice\",\n" +
				"         \"initiated_at\": \"2020-12-25T11:02:00.300Z\",\n" +
				"         \"status\": \"unused\"\n" +
				"      }\n" +
				"   ],\n" +
				"   \"price\": \"0.300000125\",\n" +
				"   \"client_ref\": \"my-personal-ref\"\n" +
				"}"
		);
		assertEquals(UUID.fromString("c11236f4-00bf-4b89-84ba-88b25df97325"), webhook.getRequestId());
		assertEquals(Instant.ofEpochMilli(1577887202000L), webhook.getSubmittedAt());
		assertEquals(VerificationStatus.FAILED, webhook.getStatus());
		assertEquals(CallbackType.SUMMARY, webhook.getType());
		assertEquals(300, webhook.getChannelTimeout().intValue());
		List<WorkflowStatus> workflows = webhook.getWorkflows();
		assertNotNull(workflows);
		assertEquals(4, workflows.size());
		WorkflowStatus workflowUnderEvaluation;

		workflowUnderEvaluation = workflows.get(0);
		assertNotNull(workflowUnderEvaluation);
		assertEquals(Channel.SMS, workflowUnderEvaluation.getChannel());
		assertEquals(Instant.ofEpochMilli(1577887200000L), workflowUnderEvaluation.getInitiatedAt());
		assertEquals(VerificationStatus.EXPIRED, workflowUnderEvaluation.getStatus());

		workflowUnderEvaluation = workflows.get(1);
		assertNotNull(workflowUnderEvaluation);
		assertEquals(Channel.WHATSAPP, workflowUnderEvaluation.getChannel());
		assertEquals(Instant.ofEpochMilli(1577887320000L), workflowUnderEvaluation.getInitiatedAt());
		assertEquals(VerificationStatus.COMPLETED, workflowUnderEvaluation.getStatus());

		workflowUnderEvaluation = workflows.get(2);
		assertNotNull(workflowUnderEvaluation);
		assertEquals(Channel.WHATSAPP_INTERACTIVE, workflowUnderEvaluation.getChannel());
		assertEquals(Instant.ofEpochMilli(1577891100000L), workflowUnderEvaluation.getInitiatedAt());
		assertEquals(VerificationStatus.USER_REJECTED, workflowUnderEvaluation.getStatus());

		workflowUnderEvaluation = workflows.get(3);
		assertNotNull(workflowUnderEvaluation);
		assertEquals(Channel.VOICE, workflowUnderEvaluation.getChannel());
		assertEquals(Instant.ofEpochMilli(1608894120300L), workflowUnderEvaluation.getInitiatedAt());
		assertEquals(VerificationStatus.UNUSED, workflowUnderEvaluation.getStatus());

		assertEquals("my-personal-ref", webhook.getClientRef());
		assertNull(webhook.getSilentAuthUrl());
		assertNull(webhook.getChannel());
		assertNull(webhook.getFinalizedAt());
		assertNull(webhook.getTriggeredAt());
	}

	@Test
	public void testParseSilentAuthUpdate() {
		VerificationCallback webhook = VerificationCallback.fromJson("{\n" +
				"   \"request_id\": \"c15236f4-00bf-4b89-84ba-88b25df97315\",\n" +
				"   \"triggered_at\": \"2023-03-22T11:48:59.717Z\",\n" +
				"   \"type\": \"event\",\n" +
				"   \"channel\": \"silent_auth\",\n" +
				"   \"status\": \"action_pending\",\n" +
				"   \"action\": {\n" +
				"       \"type\": \"check\",\n" +
				"       \"check_url\": \"https://eu.api.silent.auth/phone_check/v0.1/checks/request_id/redirect\"\n" +
				"    }\n" +
				"}"
		);
		assertEquals(UUID.fromString("c15236f4-00bf-4b89-84ba-88b25df97315"), webhook.getRequestId());
		assertEquals(Instant.ofEpochMilli(1679485739717L), webhook.getTriggeredAt());
		assertEquals(CallbackType.EVENT, webhook.getType());
		assertEquals(Channel.SILENT_AUTH, webhook.getChannel());
		assertEquals(VerificationStatus.ACTION_PENDING, webhook.getStatus());
		assertNotNull(webhook.action);
		assertEquals("check", webhook.action.type);
		assertEquals(
				URI.create("https://eu.api.silent.auth/phone_check/v0.1/checks/request_id/redirect"),
				webhook.getSilentAuthUrl()
		);
		assertNull(webhook.getFinalizedAt());
		assertNull(webhook.getClientRef());
		assertNull(webhook.getChannelTimeout());
		assertNull(webhook.getWorkflows());
		assertNull(webhook.getSubmittedAt());
	}

	@Test(expected = VonageResponseParseException.class)
	public void testFromJsonInvalid() {
		VerificationCallback.fromJson("{malformed]");
	}

	@Test
	public void testCallbackType() {
		assertNull(CallbackType.fromString("~Unknown-"));
		assertEquals("event", CallbackType.EVENT.toString());
	}

	@Test
	public void testVerificationStatus() {
		assertNull(VerificationStatus.fromString("~Unknown-"));
		assertEquals("action_pending", VerificationStatus.ACTION_PENDING.toString());
	}
}
