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
	 */
	public VerificationResponse sendVerification(VerificationRequest request) {
		return verifyUser.execute(Objects.requireNonNull(request));
	}

	/**
	 * Check a supplied code against an existing verification request to see if it is valid.
	 *
	 * @param requestId ID of the verify request, obtained from {@link VerificationResponse#getRequestId()}.
	 * @param code The code supplied by the user.
	 */
	public void validateVerificationCode(UUID requestId, String code) {
		Objects.requireNonNull(requestId, "Request ID is required.");
		verifyRequest.execute(new VerifyCodeRequestWrapper(requestId.toString(), code));
	}
}
