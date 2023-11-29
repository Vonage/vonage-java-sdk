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

import com.vonage.client.ClientTest;
import com.vonage.client.HttpWrapper;
import com.vonage.client.RestEndpoint;
import com.vonage.client.common.HttpMethod;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class Verify2ClientTest extends ClientTest<Verify2Client> {
	static final UUID REQUEST_ID = UUID.randomUUID();
	static final String CODE = "1234";
	static final String VERIFICATION_RESPONSE = "{\"request_id\": \""+REQUEST_ID+"\"}";

	public Verify2ClientTest() {
		client = new Verify2Client(wrapper);
	}

	void assert429ResponseException(Executable invocation) throws Exception {
		String response = "{\n" +
				"   \"title\": \"Rate Limit Hit\",\n" +
				"   \"type\": \"https://www.developer.vonage.com/api-errors#throttled\",\n" +
				"   \"detail\": \"Please wait, then retry your request\",\n" +
				"   \"instance\": \"bf0ca0bf927b3b52e3cb03217e1a1ddf\"\n" +
				"}";
		assertApiResponseException(429, response, VerifyResponseException.class, invocation);
	}

	VerificationRequest newVerificationRequestWithAllParamsAndWorkflows() {
		String toNumber = "447100000009", fromNumber = "447900000001",
				toEmail = "email@domain.tld", fromEmail = "hello@example.com";
		List<Workflow> workflows = Arrays.asList(
				new SilentAuthWorkflow(toNumber, true),
				new SmsWorkflow(toNumber),
				new EmailWorkflow(toEmail, fromEmail),
				new VoiceWorkflow(toNumber),
				new WhatsappWorkflow(toNumber, fromNumber),
				new WhatsappCodelessWorkflow(toNumber)
		);
		return VerificationRequest.builder()
				.brand("Nexmo").fraudCheck(false)
				.code("ab2c3de5").codeLength(8)
				.channelTimeout(500)
				.locale("de-de")
				.clientRef("callback-ref0x1")
				.workflows(workflows).build();
	}

	void stubSuccessfulVerifyUserResponseAndRun(VerificationRequest request) throws Exception {
		stubResponse(202, VERIFICATION_RESPONSE);
		VerificationResponse response = client.sendVerification(request);
		assertNotNull(response);
		assertEquals(REQUEST_ID, response.getRequestId());
		assertNull(response.getCheckUrl());
	}

	@Test
	public void testVerifyUserSuccess() throws Exception {
		stubSuccessfulVerifyUserResponseAndRun(newVerificationRequestWithAllParamsAndWorkflows());
	}

	@Test
	public void testVerifyUser429() throws Exception {
		assert429ResponseException(() -> client.sendVerification(newVerificationRequestWithAllParamsAndWorkflows()));
	}

	@Test
	public void testVerifyUserEndpoint() throws Exception {
		new Verify2EndpointTestSpec<VerificationRequest, VerificationResponse>() {

			@Override
			protected RestEndpoint<VerificationRequest, VerificationResponse> endpoint() {
				return client.verifyUser;
			}

			@Override
			protected HttpMethod expectedHttpMethod() {
				return HttpMethod.POST;
			}

			@Override
			protected String expectedEndpointUri(VerificationRequest request) {
				return "/v2/verify";
			}

			@Override
			protected VerificationRequest sampleRequest() {
				return VerificationRequest.builder()
						.clientRef("my-personal-reference").locale("ar-XA")
						.addWorkflow(new SmsWorkflow("447700900001", "FA+9qCX9VSu"))
						.brand("ACME, Inc").codeLength(6).channelTimeout(320).build();
			}

			@Override
			protected String sampleRequestBodyString() {
				return "{\"locale\":\"ar-xa\",\"channel_timeout\":320,\"code_length\":6," +
						"\"brand\":\"ACME, Inc\",\"client_ref\":\"my-personal-reference\",\"workflow\":" +
						"[{\"channel\":\"sms\",\"to\":\"447700900001\",\"app_hash\":\"FA+9qCX9VSu\"}]}";
			}

			@Override
			public void runTests() throws Exception {
				super.runTests();
				testCodelessAndBasicAuth();
				testParseResponseFailureAllParams();
				testParseResponseFailureNoBody();
			}

			void testCodelessAndBasicAuth() throws Exception {
				VerificationRequest request = VerificationRequest.builder()
						.brand("Vonage").addWorkflow(new SilentAuthWorkflow("447700900001")).build();

				Verify2Client tempClient = new Verify2Client(new HttpWrapper());
				assertThrows(IllegalStateException.class, () -> tempClient.sendVerification(request));
				String expectedJson = "{\"brand\":\""+request.getBrand() +
						"\",\"workflow\":[{\"channel\":\"silent_auth\",\"to\":\"447700900001\"}]}";

				assertRequestUriAndBody(request, expectedJson);
			}

			void testParseResponseFailureNoBody() throws Exception {
				final int statusCode = 500;
				try {
					parseResponse("", statusCode);
					fail("Expected "+ VerifyResponseException.class.getName());
				}
				catch (VerifyResponseException mrx) {
					// The mock returns "OK"
					VerifyResponseException expected = VerifyResponseException.fromJson("{\"title\":\"OK\"}");
					expected.setStatusCode(statusCode);
					assertEquals(expected, mrx);
				}
			}

			void testParseResponseFailureAllParams() throws Exception {
				final int statusCode = 422;
				String title = "Conflict";
				URI type = URI.create("https://www.developer.vonage.com/api-errors/verify#conflict");
				String detail = "Concurrent verifications to the same number are not allowed.";
				String instance = "738f9313-418a-4259-9b0d-6670f06fa82d";
				UUID requestId = UUID.fromString("575a2054-aaaf-4405-994e-290be7b9a91f");
				String json = "{\n" +
						"   \"title\": \"" + title + "\",\n" +
						"   \"type\": \"" + type + "\",\n" +
						"   \"detail\": \"" + detail + "\",\n" +
						"   \"instance\": \"" + instance + "\",\n" +
						"   \"request_id\": \"" + requestId + "\"\n" +
						"}";

				try {
					parseResponse(json, statusCode);
					fail("Expected " + VerifyResponseException.class.getName());
				}
				catch (VerifyResponseException vrx) {
					VerifyResponseException expected = VerifyResponseException.fromJson(json);
					expected.setStatusCode(statusCode);
					assertEquals(expected, vrx);
					assertEquals(statusCode, vrx.getStatusCode());
					assertEquals(title, vrx.getTitle());
					assertEquals(type, vrx.getType());
					assertEquals(detail, vrx.getDetail());
					assertEquals(instance, vrx.getInstance());
					assertEquals(requestId, vrx.getRequestId());
				}
			}
		}
		.runTests();
	}

	@Test
	public void testVerifyCodeSuccess() throws Exception {
		stubResponseAndRun(204, () -> client.checkVerificationCode(REQUEST_ID, CODE));
	}

	@Test
	public void testVerifyCodeFailure() throws Exception {
		assert429ResponseException(() -> client.checkVerificationCode(REQUEST_ID, CODE));

		stubResponseAndAssertThrows(204, () ->
				client.checkVerificationCode(null, CODE),
				NullPointerException.class
		);
		stubResponseAndAssertThrows(204,
				() -> client.checkVerificationCode(REQUEST_ID, null),
				NullPointerException.class
		);
	}

	@Test
	public void testVerifyCodeEndpoint() throws Exception {
		new Verify2EndpointTestSpec<VerifyCodeRequestWrapper, Void>() {

			@Override
			protected RestEndpoint<VerifyCodeRequestWrapper, Void> endpoint() {
				return client.verifyRequest;
			}

			@Override
			protected HttpMethod expectedHttpMethod() {
				return HttpMethod.POST;
			}

			@Override
			protected String expectedEndpointUri(VerifyCodeRequestWrapper request) {
				return "/v2/verify/" + request.requestId;
			}

			@Override
			protected VerifyCodeRequestWrapper sampleRequest() {
				return new VerifyCodeRequestWrapper(UUID.randomUUID().toString(), "54321");
			}

			@Override
			protected String sampleRequestBodyString() {
				return "{\"code\":\"54321\"}";
			}

			@Override
			public void runTests() throws Exception {
				super.runTests();
				testParseResponseFailureNoBody();
				testParse422AllParams();
			}

			void testParse422AllParams() throws Exception {
				final int statusCode = 422;
				String title = "Conflict";
				URI type = URI.create("https://www.developer.vonage.com/api-errors/verify#conflict");
				String detail = "Concurrent verifications to the same number are not allowed.";
				String instance = "738f9313-418a-4259-9b0d-6670f06fa82d";
				UUID requestId = UUID.fromString("575a2054-aaaf-4405-994e-290be7b9a91f");
				String json = "{\n" +
						"   \"title\": \""+title+"\",\n" +
						"   \"type\": \""+type+"\",\n" +
						"   \"detail\": \""+detail+"\",\n" +
						"   \"instance\": \""+instance+"\",\n" +
						"   \"request_id\": \""+requestId+"\"\n" +
						"}";

				try {
					parseResponse(json, statusCode);
					fail("Expected "+ VerifyResponseException.class.getName());
				}
				catch (VerifyResponseException vrx) {
					VerifyResponseException expected = VerifyResponseException.fromJson(json);
					expected.setStatusCode(statusCode);
					assertEquals(expected, vrx);
					assertEquals(statusCode, vrx.getStatusCode());
					assertEquals(title, vrx.getTitle());
					assertEquals(type, vrx.getType());
					assertEquals(detail, vrx.getDetail());
					assertEquals(instance, vrx.getInstance());
					assertEquals(requestId, vrx.getRequestId());
				}
			}

			void testParseResponseFailureNoBody() throws Exception {
				final int statusCode = 500;
				try {
					parseResponse("", statusCode);
					fail("Expected "+ VerifyResponseException.class.getName());
				}
				catch (VerifyResponseException mrx) {
					// The mock returns "OK"
					VerifyResponseException expected = VerifyResponseException.fromJson("{\"title\":\"OK\"}");
					expected.setStatusCode(statusCode);
					assertEquals(expected, mrx);
				}
			}
		}
		.runTests();
	}

	@Test
	public void testCancelVerificationSuccess() throws Exception {
		stubResponseAndRun(204, () -> client.cancelVerification(REQUEST_ID));
	}

	@Test
	public void testCancelVerificationFailure() throws Exception {
		stubResponseAndAssertThrows(404, () ->
				client.cancelVerification(REQUEST_ID),
				VerifyResponseException.class
		);
	}

	@Test
	public void testCancelVerificationEndpoint() throws Exception {
		new Verify2EndpointTestSpec<UUID, Void>() {

			@Override
			protected RestEndpoint<UUID, Void> endpoint() {
				return client.cancel;
			}

			@Override
			protected HttpMethod expectedHttpMethod() {
				return HttpMethod.DELETE;
			}

			@Override
			protected String expectedEndpointUri(UUID request) {
				return "/v2/verify/" + request;
			}

			@Override
			protected UUID sampleRequest() {
				return REQUEST_ID;
			}

			@Override
			public void runTests() throws Exception {
				super.runTests();
				testParse404AllParams();
			}

			void testParse404AllParams() throws Exception {
				int statusCode = 404;
				String title = "Not Found",
						type = "https://developer.vonage.com/api-errors#not-found",
						detail = "Request "+sampleRequest()+" was not found or it has been verified already.",
						instance = "bf0ca0bf927b3b52e3cb03217e1a1ddf",
						json = "{\n" +
								"   \"title\": \""+title+"\",\n" +
								"   \"type\": \""+type+"\",\n" +
								"   \"detail\": \""+detail+"\",\n" +
								"   \"instance\": \""+instance+"\"\n" +
								"}";
				try {
					parseResponse(json, statusCode);
					fail("Expected "+ VerifyResponseException.class.getName());
				}
				catch (VerifyResponseException vrx) {
					VerifyResponseException expected = VerifyResponseException.fromJson(json);
					expected.setStatusCode(statusCode);
					assertEquals(expected, vrx);
					assertEquals(statusCode, vrx.getStatusCode());
					assertEquals(title, vrx.getTitle());
					assertEquals(URI.create(type), vrx.getType());
					assertEquals(detail, vrx.getDetail());
					assertEquals(instance, vrx.getInstance());
				}
			}
		}
		.runTests();
	}
}
