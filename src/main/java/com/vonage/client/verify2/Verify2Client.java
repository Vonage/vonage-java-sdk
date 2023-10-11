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

import com.vonage.client.DynamicEndpoint;
import com.vonage.client.HttpWrapper;
import com.vonage.client.RestEndpoint;
import com.vonage.client.auth.JWTAuthMethod;
import com.vonage.client.auth.TokenAuthMethod;
import com.vonage.client.common.HttpMethod;
import java.net.URI;
import java.util.Objects;
import java.util.UUID;
import java.util.function.BiFunction;

public class Verify2Client {
	final boolean hasJwtAuthMethod;
	final RestEndpoint<VerificationRequest, VerificationResponse> verifyUser;
	final RestEndpoint<VerifyCodeRequestWrapper, Void> verifyRequest;
	final RestEndpoint<UUID, Void> cancel;
	final RestEndpoint<URI, SilentAuthResponse> silentAuthCheck;

	/**
	 * Create a new Verify2Client.
	 *
	 * @param wrapper Http Wrapper used to create verification requests.
	 */
	public Verify2Client(HttpWrapper wrapper) {
		hasJwtAuthMethod = wrapper.getAuthCollection().hasAuthMethod(JWTAuthMethod.class);

		@SuppressWarnings("unchecked")
		final class Endpoint<T, R> extends DynamicEndpoint<T, R> {
			Endpoint(BiFunction<String, T, String> pathGetter, HttpMethod method, R... type) {
				super(DynamicEndpoint.<T, R> builder(type)
						.responseExceptionType(VerifyResponseException.class)
						.wrapper(wrapper).requestMethod(method)
						.addAuthMethodIfTrue(method != HttpMethod.GET, JWTAuthMethod.class, TokenAuthMethod.class)
						.pathGetter((de, req) -> {
							String base = de.getHttpWrapper().getHttpConfig().getVersionedApiBaseUri("v2") + "/verify";
							return pathGetter.apply(base, req);
						})
				);
			}
		}

		verifyUser = new Endpoint<>((base, req) -> base, HttpMethod.POST);
		verifyRequest = new Endpoint<>((base, req) -> base + '/' + req.requestId, HttpMethod.POST);
		cancel = new Endpoint<>((base, req) -> base + '/' + req, HttpMethod.DELETE);
		silentAuthCheck = new Endpoint<>((base, req) -> req.toString(), HttpMethod.GET);
	}

	private UUID validateRequestId(UUID requestId) {
		return Objects.requireNonNull(requestId, "Request ID is required.");
	}

	/**
	 * Request a verification be sent to a user. This is the first step in the verification process.
	 *
	 * @param request Properties of the verification request. You must specify the brand name and at least one
	 * contact method (workflow). For example, to verify using Whatsapp and fall back to a voice call as backup
	 * to the same number with a 6-digit code and a 3-minute wait between attempts:
	 * <pre>
	 * {@code VerificationRequest.builder()
	 *      .brand("My Company")
	 *      .addWorkflow(new WhatsappWorkflow("447000000001"))
	 *      .addWorkflow(new VoiceWorkflow("447000000001"))
	 *      .codeLength(6)
	 *      .channelTimeout(180)
	 *      .build()}.
	 * </pre>
	 *
	 * @return The server's response, if successful.
	 *
	 * @throws VerifyResponseException If the request was unsuccessful. This could be for the following reasons:
	 * <ul>
	 *     <li><b>409</b>: Concurrent verifications to the same number are not allowed.</li>
	 *     <li><b>422</b>: The value of one or more parameters is invalid.</li>
	 *     <li><b>429</b>: Rate limit hit. Please wait and try again.</li>
	 * </ul>
	 */
	public VerificationResponse sendVerification(VerificationRequest request) {
		if (request.isCodeless() && !hasJwtAuthMethod) {
			throw new IllegalStateException(
				"Codeless verification requires an application ID to be set in order to use webhooks."
			);
		}
		return verifyUser.execute(Objects.requireNonNull(request));
	}

	/**
	 * Check a supplied code against an existing verification request. If the code is valid,
	 * this method will return normally. Otherwise, a {@link VerifyResponseException} will be
	 * thrown with the following status and reasons:
	 *
	 * <ul>
	 *      <li><b>400</b>: The provided code does not match the expected value.</li>
	 *      <li><b>404</b>: Request ID was not found or it has been verified already.</li>
	 *      <li><b>409</b>: The current workflow step does not support a code.</li>
	 *      <li><b>410</b>: An incorrect code has been provided too many times.</li>
	 *      <li><b>429</b>: Rate limit hit. Please wait and try again.</li>
	 * </ul>
	 *
	 * @param requestId ID of the verify request, obtained from {@link VerificationResponse#getRequestId()}.
	 * @param code The code supplied by the user.
	 *
	 * @throws VerifyResponseException If the code was invalid, or any other error.
	 */
	public void checkVerificationCode(UUID requestId, String code) {
		verifyRequest.execute(new VerifyCodeRequestWrapper(
				validateRequestId(requestId).toString(),
				Objects.requireNonNull(code, "Code is required.")
		));
	}

	/**
	 * Attempts to abort an active verification workflow.
	 * If successful (HTTP status 204), this method will return normally.
	 * Otherwise, an exception will be thrown indicating a 404 response.
	 *
	 * @param requestId ID of the verify request, obtained from {@link VerificationResponse#getRequestId()}.
	 *
	 * @throws VerifyResponseException If the request ID was not found or it has been verified already.
	 */
	public void cancelVerification(UUID requestId) {
		cancel.execute(validateRequestId(requestId));
	}

	/**
	 * Final step of Silent Authentication workflow. Once the {@linkplain #sendVerification(VerificationRequest)}
	 * has been called, pass the response to this method to complete the verification workflow. This method uses
	 * the {@linkplain #checkVerificationCode(UUID, String)}  under the hood with a code obtained from the API
	 * after following the `check_url` redirect. Refer to the
	 * <a href=https://developer.vonage.com/en/verify/guides/silent-authentication>
	 * Silent Authentication documentation</a> for more details.
	 *
	 * @param verifyResponse The VerificationResponse, as obtained from {@link #sendVerification(VerificationRequest)}.
	 *
	 * @throws VerifyResponseException If the Silent Authentication workflow failed due
	 * to a network error (409 HTTP status response).
	 *
	 * @since v7.10.0
	 */
	public void checkSilentAuth(VerificationResponse verifyResponse) {
		Objects.requireNonNull(verifyResponse, "Response object cannot be null.");
		URI checkUrl = verifyResponse.getCheckUrl();
		if (checkUrl == null) {
			throw new IllegalStateException("'check_url' is missing in the response.");
		}
		SilentAuthResponse response = silentAuthCheck.execute(checkUrl);
		checkVerificationCode(response.getRequestId(), response.getCode());
	}

	/*/**
	 * A fully declarative, automated utility method for performing Silent Authentication using
	 * a device's mobile network connection. If the authentication failed due to a network error,
	 * this method will return {@code false}. If a failure occurs for any other reason, a
	 * {@linkplain VerifyResponseException} will be thrown.
	 *
	 * @param number The device's SIM (phone) number in E.164 format.
	 *
	 * @return {@code true} if the authentication was successful.
	 *
	 * @throws VerifyResponseException If the workflow fails for any reason other than a 409 Network error.
	 *
	 * @since v7.10.0

	public boolean doSilentAuthWorkflow(String number) {
		VerificationRequest request = VerificationRequest.builder()
				.addWorkflow(new SilentAuthWorkflow(number))
				.brand("Vonage Java SDK").build();
		VerificationResponse response = sendVerification(request);
		try {
			checkSilentAuth(response);
			return true;
		}
		catch (VerifyResponseException ex) {
			return false;
		}
	}*/
}
