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
package com.vonage.client.verify2;

import com.vonage.client.AbstractClientTest;
import com.vonage.client.HttpWrapper;
import com.vonage.client.RestEndpoint;
import static com.vonage.client.TestUtils.testJsonableBaseObject;
import com.vonage.client.common.HttpMethod;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;
import java.net.URI;
import java.time.Instant;
import java.util.*;

public class Verify2ClientTest extends AbstractClientTest<Verify2Client> {
	static final UUID
			REQUEST_ID = UUID.fromString("90596ac8-e1f1-46a9-a80f-ebd55e2296ae"),
			TEMPLATE_ID = UUID.fromString("8f35a1a7-eb2f-4552-8fdf-fffdaee41bc9"),
			FRAGMENT_ID = UUID.fromString("c70f446e-997a-4313-a081-60a02a31dc19");
	static final Instant
			DATE_CREATED = Instant.parse("2021-08-30T20:12:15.17865735Z"),
			DATE_UPDATED = Instant.now();
	static final String CODE = "1234", LOCALE = "en-us",
			TEMPLATE_NAME = "my-template",
			FRAGMENT_TEXT = "Text content of the template. May contain 4 reserved variables:" +
					"`${code}`, `${brand}`, `${time-limit}` and `${time-limit-unit}`",
			VERIFICATION_RESPONSE = "{\"request_id\": \""+REQUEST_ID+"\"}",
			EMPTY_HAL_RESPONSE = "{\"_embedded\":{}}",
			TEMPLATE_RESPONSE = "{\n" +
					"   \"template_id\": \""+TEMPLATE_ID+"\",\n" +
					"   \"name\": \""+TEMPLATE_NAME+"\",\n" +
					"   \"is_default\": true,\n" +
					"   \"_links\": {\n" +
					"      \"self\": {\n" +
					"         \"href\": \"https://api.nexmo.com/v2/verify/templates/"+TEMPLATE_ID+"\"\n" +
					"      },\n" +
					"      \"fragments\": {\n" +
					"         \"href\": \"https://api.nexmo.com/v2/verify/templates/"+TEMPLATE_ID+"/template_fragments\"\n" +
					"      }\n" +
					"   }\n" +
					"}",
			FRAGMENT_RESPONSE = "{\n" +
					"   \"template_fragment_id\": \""+FRAGMENT_ID+"\",\n" +
					"   \"channel\": \"sms\",\n" +
					"   \"locale\": \""+LOCALE+"\",\n" +
					"   \"text\": \""+FRAGMENT_TEXT+"\",\n" +
					"   \"date_updated\": \""+DATE_UPDATED+"\",\n" +
					"   \"date_created\": \""+DATE_CREATED+"\",\n" +
					"   \"_links\": {\n" +
					"      \"self\": {\n" +
					"         \"href\": \"https://api.nexmo.com/v2/verify/templates/"+TEMPLATE_ID+"/template_fragments/"+FRAGMENT_ID+"\"\n" +
					"      },\n" +
					"      \"template\": {\n" +
					"         \"href\": \"https://api.nexmo.com/v2/verify/templates/"+TEMPLATE_ID+"\"\n" +
					"      }\n" +
					"   }\n" +
					"}";

	public Verify2ClientTest() {
		client = new Verify2Client(wrapper);
	}

	void assertEqualsSampleTemplate(Template parsed) {
		assertNotNull(parsed);
		assertEquals(TEMPLATE_ID, parsed.getId());
		assertEquals(TEMPLATE_NAME, parsed.getName());
		assertEquals(true, parsed.isDefault());
	}

	void assertEqualsSampleFragment(TemplateFragment parsed) {
		assertNotNull(parsed);
		assertEquals(FRAGMENT_ID, parsed.getFragmentId());
		assertEquals(FRAGMENT_TEXT, parsed.getText());
		assertEquals(LOCALE, parsed.getLocale().toLanguageTag().toLowerCase());
		assertEquals(FragmentChannel.SMS, parsed.getChannel());
		assertEquals(DATE_CREATED, parsed.getDateCreated());
		assertEquals(DATE_UPDATED, parsed.getDateUpdated());
	}

	void assert429ResponseException(Executable invocation) throws Exception {
		String response = """
                {
                   "title": "Rate Limit Hit",
                   "type": "https://www.developer.vonage.com/api-errors#throttled",
                   "detail": "Please wait, then retry your request",
                   "instance": "bf0ca0bf927b3b52e3cb03217e1a1ddf"
                }""";
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
				new WhatsappCodelessWorkflow(toNumber, fromNumber)
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
						.addWorkflow(SmsWorkflow.builder("447700900001")
								.from("447900000002").appHash("FA+9qCX9VSu").build()
						)
						.brand("ACME, Inc").codeLength(6).channelTimeout(320).build();
			}

			@Override
			protected String sampleRequestBodyString() {
				return "{\"locale\":\"ar-xa\",\"channel_timeout\":320,\"code_length\":6," +
						"\"brand\":\"ACME, Inc\",\"client_ref\":\"my-personal-reference\"," +
						"\"workflow\":[{\"channel\":\"sms\",\"to\":\"447700900001\"," +
						"\"from\":\"447900000002\",\"app_hash\":\"FA+9qCX9VSu\"}]}";
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
	public void testVerifyCodeSuccessNoResponse() throws Exception {
		stubResponseAndRun(204, () -> client.checkVerificationCode(REQUEST_ID, CODE));
	}

	@Test
	public void testVerifyCodeSuccess() throws Exception {
		stubResponse(200, VERIFICATION_RESPONSE.replace("}", ",\"status\":\"completed\"}"));
		VerifyCodeResponse parsed = client.checkVerificationCode(REQUEST_ID, CODE);
		testJsonableBaseObject(parsed);
		assertEquals(REQUEST_ID, parsed.getRequestId());
		assertEquals(VerificationStatus.COMPLETED, parsed.getStatus());
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
		new Verify2EndpointTestSpec<VerifyCodeRequestWrapper, VerifyCodeResponse>() {

			@Override
			protected RestEndpoint<VerifyCodeRequestWrapper, VerifyCodeResponse> endpoint() {
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
		stubResponseAndAssertThrows(404,
				() -> client.cancelVerification(REQUEST_ID),
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

	@Test
	public void testNextWorkflowSuccess() throws Exception {
		stubResponseAndRun(200, () -> client.nextWorkflow(REQUEST_ID));
	}

	@Test
	public void testNextWorkflowFailure() throws Exception {
		stubResponseAndAssertThrows(409,
				() -> client.nextWorkflow(REQUEST_ID),
				VerifyResponseException.class
		);
	}

	@Test
	public void testNextWorkflowEndpoint() throws Exception {
		new Verify2EndpointTestSpec<UUID, Void>() {

			@Override
			protected RestEndpoint<UUID, Void> endpoint() {
				return client.nextWorkflow;
			}

			@Override
			protected HttpMethod expectedHttpMethod() {
				return HttpMethod.POST;
			}

			@Override
			protected String expectedEndpointUri(UUID request) {
				return "/v2/verify/"+request+"/next-workflow";
			}

			@Override
			protected UUID sampleRequest() {
				return REQUEST_ID;
			}
		}
		.runTests();
	}

	@Test
	public void testDeleteTemplateSuccess() throws Exception {
		stubResponseAndRun(204, () -> client.deleteTemplate(TEMPLATE_ID));
	}

	@Test
	public void testDeleteTemplateFailure() throws Exception {
		stubResponseAndAssertThrows(404,
				() -> client.deleteTemplate(TEMPLATE_ID),
				VerifyResponseException.class
		);
	}

	@Test
	public void testDeleteTemplateEndpoint() throws Exception {
		new Verify2EndpointTestSpec<UUID, Void>() {

			@Override
			protected RestEndpoint<UUID, Void> endpoint() {
				return client.deleteTemplate;
			}

			@Override
			protected HttpMethod expectedHttpMethod() {
				return HttpMethod.DELETE;
			}

			@Override
			protected String expectedEndpointUri(UUID request) {
				return "/v2/verify/templates/"+request;
			}

			@Override
			protected UUID sampleRequest() {
				return TEMPLATE_ID;
			}
		}
		.runTests();
	}

	@Test
	public void testDeleteTemplateFragmentSuccess() throws Exception {
		stubResponseAndRun(204, () -> client.deleteFragment(TEMPLATE_ID, FRAGMENT_ID));
	}

	@Test
	public void testDeleteTemplateFragmentFailure() throws Exception {
		stubResponseAndAssertThrows(404,
				() -> client.deleteFragment(TEMPLATE_ID, FRAGMENT_ID),
				VerifyResponseException.class
		);
	}

	@Test
	public void testDeleteTemplateFragmentEndpoint() throws Exception {
		new Verify2EndpointTestSpec<TemplateFragment, Void>() {

			@Override
			protected RestEndpoint<TemplateFragment, Void> endpoint() {
				return client.deleteFragment;
			}

			@Override
			protected HttpMethod expectedHttpMethod() {
				return HttpMethod.DELETE;
			}

			@Override
			protected String expectedEndpointUri(TemplateFragment request) {
				return "/v2/verify/templates/"+request.templateId+"/template_fragments/"+request.fragmentId;
			}

			@Override
			protected TemplateFragment sampleRequest() {
				var fragment = new TemplateFragment();
				fragment.templateId = TEMPLATE_ID;
				fragment.fragmentId = FRAGMENT_ID;
				return fragment;
			}
		}
		.runTests();
	}

	@Test
	public void testGetTemplateSuccess() throws Exception {
		stubResponse(200, TEMPLATE_RESPONSE);
		assertEqualsSampleTemplate(client.getTemplate(UUID.randomUUID()));
	}

	@Test
	public void testGetTemplateFailure() throws Exception {
		assertThrows(NullPointerException.class, () -> client.getTemplate(null));
		stubResponseAndAssertThrows(404,
				() -> client.getTemplate(TEMPLATE_ID),
				VerifyResponseException.class
		);
	}

	@Test
	public void testGetTemplateEndpoint() throws Exception {
		new Verify2EndpointTestSpec<UUID, Template>() {

			@Override
			protected RestEndpoint<UUID, Template> endpoint() {
				return client.getTemplate;
			}

			@Override
			protected HttpMethod expectedHttpMethod() {
				return HttpMethod.GET;
			}

			@Override
			protected String expectedEndpointUri(UUID request) {
				return "/v2/verify/templates/"+request;
			}

			@Override
			protected UUID sampleRequest() {
				return TEMPLATE_ID;
			}
		}
		.runTests();
	}

	@Test
	public void testGetTemplateFragmentSuccess() throws Exception {
		stubResponse(200, FRAGMENT_RESPONSE);
		assertEqualsSampleFragment(client.getFragment(TEMPLATE_ID, FRAGMENT_ID));
	}

	@Test
	public void testGetTemplateFragmentFailure() throws Exception {
		stubResponseAndAssertThrows(404,
				() -> client.getFragment(TEMPLATE_ID, FRAGMENT_ID),
				VerifyResponseException.class
		);
	}

	@Test
	public void testGetTemplateFragmentEndpoint() throws Exception {
		new Verify2EndpointTestSpec<TemplateFragment, TemplateFragment>() {

			@Override
			protected RestEndpoint<TemplateFragment, TemplateFragment> endpoint() {
				return client.getFragment;
			}

			@Override
			protected HttpMethod expectedHttpMethod() {
				return HttpMethod.GET;
			}

			@Override
			protected String expectedEndpointUri(TemplateFragment request) {
				return "/v2/verify/templates/"+request.templateId+"/template_fragments/"+request.fragmentId;
			}

			@Override
			protected TemplateFragment sampleRequest() {
				var fragment = new TemplateFragment();
				fragment.templateId = TEMPLATE_ID;
				fragment.fragmentId = FRAGMENT_ID;
				return fragment;
			}
		}
		.runTests();
	}

	@Test
	public void testCreateTemplateSuccess() throws Exception {
		stubResponse(201, TEMPLATE_RESPONSE);
		assertEqualsSampleTemplate(client.createTemplate("Custom_template-1"));
	}

	@Test
	public void testCreateTemplateFailure() throws Exception {
		assertThrows(NullPointerException.class, () -> client.createTemplate(null));
		assertThrows(IllegalArgumentException.class, () -> client.createTemplate("Invalid name"));
		stubResponseAndAssertThrows(409,
				() -> client.createTemplate(TEMPLATE_NAME),
				VerifyResponseException.class
		);
	}

	@Test
	public void testCreateTemplateEndpoint() throws Exception {
		new Verify2EndpointTestSpec<Template, Template>() {

			@Override
			protected RestEndpoint<Template, Template> endpoint() {
				return client.createTemplate;
			}

			@Override
			protected HttpMethod expectedHttpMethod() {
				return HttpMethod.POST;
			}

			@Override
			protected String expectedEndpointUri(Template request) {
				return "/v2/verify/templates";
			}

			@Override
			protected Template sampleRequest() {
				return new Template(TEMPLATE_NAME, null);
			}

			@Override
			protected String sampleRequestBodyString() {
				return "{\"name\":\""+TEMPLATE_NAME+"\"}";
			}
		}
		.runTests();
	}

	@Test
	public void testCreateTemplateFragmentSuccess() throws Exception {
		stubResponse(201, FRAGMENT_RESPONSE);
		assertEqualsSampleFragment(client.createFragment(
				TEMPLATE_ID,
				new TemplateFragment(FragmentChannel.SMS, "en_US", FRAGMENT_TEXT)
		));
	}

	@Test
	public void testCreateTemplateFragmentFailure() throws Exception {
		assertThrows(NullPointerException.class, () -> client.createFragment(TEMPLATE_ID, null));
		assertThrows(NullPointerException.class, () -> client.createFragment(null, new TemplateFragment(FRAGMENT_TEXT)));
		assertThrows(NullPointerException.class, () -> client.createFragment(TEMPLATE_ID, new TemplateFragment(null)));
		stubResponseAndAssertThrows(409, () ->
				client.createFragment(TEMPLATE_ID,
						new TemplateFragment(FragmentChannel.SMS, LOCALE, FRAGMENT_TEXT)
				),
				VerifyResponseException.class
		);
	}

	@Test
	public void testCreateTemplateFragmentEndpoint() throws Exception {
		new Verify2EndpointTestSpec<TemplateFragment, TemplateFragment>() {

			@Override
			protected RestEndpoint<TemplateFragment, TemplateFragment> endpoint() {
				return client.createFragment;
			}

			@Override
			protected HttpMethod expectedHttpMethod() {
				return HttpMethod.POST;
			}

			@Override
			protected String expectedEndpointUri(TemplateFragment request) {
				return "/v2/verify/templates/"+request.templateId+"/template_fragments";
			}

			@Override
			protected TemplateFragment sampleRequest() {
				var fragment = new TemplateFragment(FragmentChannel.SMS, LOCALE, FRAGMENT_TEXT);
				fragment.templateId = TEMPLATE_ID;
				return fragment;
			}

			@Override
			protected String sampleRequestBodyString() {
				return "{\"text\":\""+FRAGMENT_TEXT+"\",\"locale\":\""+LOCALE+"\",\"channel\":\"sms\"}";
			}
		}
		.runTests();
	}

	@Test
	public void testUpdateTemplateSuccess() throws Exception {
		stubResponse(200, TEMPLATE_RESPONSE);
		assertEqualsSampleTemplate(client.updateTemplate(TEMPLATE_ID, "2024_Custom-template_2", false));

		stubResponse(200, TEMPLATE_RESPONSE);
		assertEqualsSampleTemplate(client.updateTemplate(TEMPLATE_ID, null, true));

		stubResponse(200, TEMPLATE_RESPONSE);
		assertEqualsSampleTemplate(client.updateTemplate(TEMPLATE_ID, TEMPLATE_NAME, null));
	}

	@Test
	public void testUpdateTemplateFailure() throws Exception {
		assertThrows(NullPointerException.class, () -> client.updateTemplate(null, "New-name", false));
		assertThrows(IllegalArgumentException.class, () -> client.updateTemplate(TEMPLATE_ID, null, null));
		stubResponseAndAssertThrows(409, () ->
				client.updateTemplate(TEMPLATE_ID, "New_name", true),
				VerifyResponseException.class
		);
	}

	@Test
	public void testUpdateTemplateEndpoint() throws Exception {
		new Verify2EndpointTestSpec<Template, Template>() {

			@Override
			protected RestEndpoint<Template, Template> endpoint() {
				return client.updateTemplate;
			}

			@Override
			protected HttpMethod expectedHttpMethod() {
				return HttpMethod.PATCH;
			}

			@Override
			protected String expectedEndpointUri(Template request) {
				return "/v2/verify/templates/"+request.getId();
			}

			@Override
			protected Template sampleRequest() {
				return new Template(null, false);
			}

			@Override
			protected String sampleRequestBodyString() {
				return "{\"is_default\":false}";
			}
		}
		.runTests();
	}

	@Test
	public void testUpdateTemplateFragmentSuccess() throws Exception {
		stubResponse(200, FRAGMENT_RESPONSE);
		assertEqualsSampleFragment(client.updateFragment(TEMPLATE_ID, FRAGMENT_ID, "Your code is: {code}"));
	}

	@Test
	public void testUpdateTemplateFragmentFailure() throws Exception {
		assertThrows(NullPointerException.class, () -> client.updateFragment(TEMPLATE_ID, null, FRAGMENT_TEXT));
		assertThrows(NullPointerException.class, () -> client.updateFragment(null, FRAGMENT_ID, FRAGMENT_TEXT));
		assertThrows(NullPointerException.class, () -> client.updateFragment(TEMPLATE_ID, FRAGMENT_ID, null));
		stubResponseAndAssertThrows(409, () ->
				client.updateFragment(TEMPLATE_ID, FRAGMENT_ID, FRAGMENT_TEXT),
				VerifyResponseException.class
		);
	}

	@Test
	public void testUpdateTemplateFragmentEndpoint() throws Exception {
		new Verify2EndpointTestSpec<TemplateFragment, TemplateFragment>() {
			final String text = "Here is your verification code: {pin}. Valid for {exp} minutes.";

			@Override
			protected RestEndpoint<TemplateFragment, TemplateFragment> endpoint() {
				return client.updateFragment;
			}

			@Override
			protected HttpMethod expectedHttpMethod() {
				return HttpMethod.PATCH;
			}

			@Override
			protected String expectedEndpointUri(TemplateFragment request) {
				return "/v2/verify/templates/"+request.templateId+"/template_fragments/"+request.fragmentId;
			}

			@Override
			protected TemplateFragment sampleRequest() {
				var fragment = new TemplateFragment(text);
				fragment.templateId = TEMPLATE_ID;
				fragment.fragmentId = FRAGMENT_ID;
				return fragment;
			}

			@Override
			protected String sampleRequestBodyString() {
				return "{\"text\":\""+text+"\"}";
			}
		}
		.runTests();
	}

	@Test
	public void testListTemplatesSuccess() throws Exception {
		var baseUrl = "https://api.nexmo.com/v2/verify/templates?page=";
		var selfUrl = URI.create(baseUrl + "2&page_size=10");
		var firstUrl = URI.create(baseUrl + "1&page_size=5");
		var lastUrl = URI.create(baseUrl + "3&page_size=10");
		stubResponse(200, "{\n" +
				"   \"page_size\": 5,\n" +
				"   \"page\": 1,\n" +
				"   \"total_pages\": 3,\n" +
				"   \"total_items\": 25,\n" +
				"   \"_embedded\": {\n" +
				"      \"templates\": [\n" +
				"	     {\"template_id\":\""+FRAGMENT_ID+"\",\"name\":\"0_1\",\"is_default\":false},\n" +
				"        {},\n " + TEMPLATE_RESPONSE + "\n]\n},\n" +
				"    \"_links\": {\n" +
				"        \"self\": {\n" +
				"            \"href\": \""+selfUrl+"\"\n" +
				"        },\n" +
				"        \"first\": {\n" +
				"            \"href\": \""+firstUrl+"\"\n" +
				"        },\n" +
				"        \"last\": {\n" +
				"            \"href\": \""+lastUrl+"\"\n" +
				"        }\n" +
				"    }\n" +
				"}"
		);
		var response = client.listTemplates();
		assertNotNull(response);
		assertEquals(5, response.getPageSize());
		assertEquals(1, response.getPage());
		assertEquals(3, response.getTotalPages());
		assertEquals(25, response.getTotalItems());
		var templates = response.getTemplates();
		assertNotNull(templates);
		assertEquals(3, templates.size());
		var first = templates.getFirst();
		assertNotNull(first);
		assertEquals(FRAGMENT_ID, first.getId());
		assertEquals("0_1", first.getName());
		assertFalse(first.isDefault());
		var empty = templates.get(1);
		assertNotNull(empty);
		assertNull(empty.getId());
		assertNull(empty.getName());
		assertNull(empty.isDefault());
		assertEqualsSampleTemplate(response.getTemplates().getLast());
		var links = response.getLinks();
		assertNotNull(links);
		assertEquals(selfUrl, links.getSelfUrl());
		assertEquals(firstUrl, links.getFirstUrl());
		assertEquals(lastUrl, links.getLastUrl());

		stubResponse(200, EMPTY_HAL_RESPONSE);
		response = client.listTemplates(1, 1);
		assertNotNull(response);
		assertNull(response.getPageSize());
		assertNull(response.getPage());
		assertNull(response.getTotalPages());
		assertNull(response.getTotalItems());
		assertNull(response.getTemplates());
		assertNull(response.getLinks());
	}

	@Test
	public void testListTemplatesFailure() throws Exception {
		stubResponseAndAssertThrows(200, EMPTY_HAL_RESPONSE,
				() -> client.listTemplates(-1, 10),
				IllegalArgumentException.class
		);
		stubResponseAndAssertThrows(200, EMPTY_HAL_RESPONSE,
				() -> client.listTemplates(1, 0),
				IllegalArgumentException.class
		);
		assert429ResponseException(client::listTemplates);
	}

	@Test
	public void testListTemplatesEndpoint() throws Exception {
		new Verify2EndpointTestSpec<ListTemplatesRequest, ListTemplatesResponse>() {

			@Override
			protected RestEndpoint<ListTemplatesRequest, ListTemplatesResponse> endpoint() {
				return client.listTemplates;
			}

			@Override
			protected HttpMethod expectedHttpMethod() {
				return HttpMethod.GET;
			}

			@Override
			protected String expectedEndpointUri(ListTemplatesRequest request) {
				return "/v2/verify/templates";
			}

			@Override
			protected ListTemplatesRequest sampleRequest() {
				return new ListTemplatesRequest(1, 25, null);
			}

			@Override
			protected Map<String, String> sampleQueryParams() {
				return Map.of(
					"page", "1",
					"page_size", "25"
				);
			}
		}
		.runTests();
	}

	@Test
	public void testListTemplateFragmentsSuccess() throws Exception {
		var baseUrl = "https://api.nexmo.com/v2/verify/templates/" + TEMPLATE_ID + "/template_fragments?page=";
		var selfUrl = URI.create(baseUrl + "2&page_size=10");
		var firstUrl = URI.create(baseUrl + "1&page_size=5");
		var lastUrl = URI.create(baseUrl + "3&page_size=10");
		stubResponse(200, "{\n" +
				"   \"page_size\": 5,\n" +
				"   \"page\": 1,\n" +
				"   \"total_pages\": 3,\n" +
				"   \"total_items\": 25,\n" +
				"   \"_embedded\": {\n" +
				"      \"template_fragments\": [\n" +
				"	     {\"template_fragment_id\":\"" + TEMPLATE_ID + "\",\"channel\":\"voice\",\"locale\":\"ja-jp\"},\n" +
				"        {},\n " + FRAGMENT_RESPONSE + "\n]\n},\n" +
				"    \"_links\": {\n" +
				"        \"self\": {\n" +
				"            \"href\": \"" + selfUrl + "\"\n" +
				"        },\n" +
				"        \"first\": {\n" +
				"            \"href\": \"" + firstUrl + "\"\n" +
				"        },\n" +
				"        \"last\": {\n" +
				"            \"href\": \"" + lastUrl + "\"\n" +
				"        }\n" +
				"    }\n" +
				"}"
		);
		var response = client.listFragments(TEMPLATE_ID);
		assertNotNull(response);
		assertEquals(5, response.getPageSize());
		assertEquals(1, response.getPage());
		assertEquals(3, response.getTotalPages());
		assertEquals(25, response.getTotalItems());
		var fragments = response.getTemplateFragments();
		assertNotNull(fragments);
		assertEquals(3, fragments.size());
		var first = fragments.getFirst();
		assertNotNull(first);
		assertEquals(TEMPLATE_ID, first.getFragmentId());
		assertEquals(FragmentChannel.VOICE, first.getChannel());
		assertEquals(Locale.JAPAN, first.getLocale());
		var empty = fragments.get(1);
		assertNotNull(empty);
		assertNull(empty.getFragmentId());
		assertNull(empty.getChannel());
		assertNull(empty.getLocale());
		assertEqualsSampleFragment(response.getTemplateFragments().getLast());
		var links = response.getLinks();
		assertNotNull(links);
		assertEquals(selfUrl, links.getSelfUrl());
		assertEquals(firstUrl, links.getFirstUrl());
		assertEquals(lastUrl, links.getLastUrl());

		stubResponse(200, EMPTY_HAL_RESPONSE);
		response = client.listFragments(TEMPLATE_ID, 1, 1);
		assertNotNull(response);
		assertNull(response.getPageSize());
		assertNull(response.getPage());
		assertNull(response.getTotalPages());
		assertNull(response.getTotalItems());
		assertNull(response.getTemplateFragments());
		assertNull(response.getLinks());
	}
}
