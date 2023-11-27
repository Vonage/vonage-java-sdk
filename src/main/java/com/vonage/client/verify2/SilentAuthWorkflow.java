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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.common.E164;
import java.net.URI;

/**
 * Defines properties for mobile network-based authentication. See the
 * <a href=https://developer.vonage.com/en/verify/verify-v2/guides/silent-authentication>Silent Auth guide</a>
 * for an overview of how this works.
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public final class SilentAuthWorkflow extends Workflow {
	private Boolean sandbox;
	private URI redirectUrl;

	/**
	 * Constructs a new Silent Auth verification workflow.
	 *
	 * @param to The number to registered to the device on the network to authenticate.
	 */
	public SilentAuthWorkflow(String to) {
		super(Channel.SILENT_AUTH, new E164(to).toString());
	}

	/**
	 * Constructs a new Silent Auth verification workflow.
	 *
	 * @param to The number to registered to the device on the network to authenticate.
	 * @param sandbox Whether the Vonage Sandbox should be used (for testing purposes).
	 *
	 * @since 7.10.0
	 */
	public SilentAuthWorkflow(String to, boolean sandbox) {
		this(to);
		this.sandbox = sandbox;
	}

	/**
	 * Constructs a new Silent Auth verification workflow.
	 *
	 * @param to The number to registered to the device on the network to authenticate.
	 * @param sandbox Whether the Vonage Sandbox should be used (for testing purposes).
	 * @param redirectUrl Optional final redirect added at the end of the check_url request/response lifecycle.
	 *                    Will contain the request_id and code as a URL fragment after the URL.
	 *
	 * @since 8.0.0
	 */
	public SilentAuthWorkflow(String to, boolean sandbox, String redirectUrl) {
		this(to, sandbox);
		this.redirectUrl = URI.create(redirectUrl);
	}

	/**
	 * Optional parameter if using the Vonage Sandbox to test Silent Auth integrations.
	 *
	 * @return Whether the Vonage Sandbox will be used, or {@code null} if not specified (the default).
	 *
	 * @since 7.10.0
	 */
	@JsonProperty("sandbox")
	public Boolean getSandbox() {
		return sandbox;
	}

	/**
	 * Final redirect after {@link VerificationResponse#getCheckUrl()}. See the documentation for integrations.
	 *
	 * @return The optional {@code redirect_url}, or {@code null} if not set (the default).
	 * @since 8.0.0
	 */
	@JsonProperty("redirect_url")
	public URI getRedirectUrl() {
		return redirectUrl;
	}
}
