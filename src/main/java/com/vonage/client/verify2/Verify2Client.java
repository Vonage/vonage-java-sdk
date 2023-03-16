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

import com.vonage.client.HttpWrapper;
import java.util.Objects;
import java.util.UUID;

public class Verify2Client {
	final VerifyUserEndpoint verifyUser;
	final VerifyCodeEndpoint verifyRequest;

	/**
	 * Create a new Verify2Client.
	 *
	 * @param httpWrapper Http Wrapper used to create verification requests.
	 */
	public Verify2Client(HttpWrapper httpWrapper) {
		verifyUser = new VerifyUserEndpoint(httpWrapper);
		verifyRequest = new VerifyCodeEndpoint(httpWrapper);
	}

	/**
	 * Request a verification be sent to a user.
	 *
	 * @param request Properties of the verification request.
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
	public void validateVerificationCode(UUID requestId, String code) {
		Objects.requireNonNull(requestId, "Request ID is required.");
		Objects.requireNonNull(code, "Code is required.");
		verifyRequest.execute(new VerifyCodeRequestWrapper(requestId.toString(), code));
	}
}
