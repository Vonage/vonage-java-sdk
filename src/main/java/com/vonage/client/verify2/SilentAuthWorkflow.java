/*
 *   Copyright 2025 Vonage
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

import com.fasterxml.jackson.annotation.JsonProperty;
import java.net.URI;

/**
 * Defines properties for mobile network-based authentication. See the
 * <a href=https://developer.vonage.com/en/verify/verify-v2/guides/silent-authentication>Silent Auth guide</a>
 * for an overview of how this works.
 */
public final class SilentAuthWorkflow extends AbstractNumberWorkflow {
	@Deprecated
	private final Boolean sandbox;
	private final URI redirectUrl;

	SilentAuthWorkflow(Builder builder) {
		super(builder);
		sandbox = builder.sandbox;
		redirectUrl = builder.redirectUrl != null ? URI.create(builder.redirectUrl) : null;
	}

	/**
	 * Constructs a new Silent Auth verification workflow.
	 *
	 * @param to The number to registered to the device on the network to authenticate.
	 */
	public SilentAuthWorkflow(String to) {
		this(builder(to));
	}

	/**
	 * Constructs a new Silent Auth verification workflow.
	 *
	 * @param to The number to registered to the device on the network to authenticate.
	 * @param redirectUrl Optional final redirect added at the end of the check_url request/response lifecycle.
	 *                    Will contain the request_id and code as a URL fragment after the URL.
	 *
	 * @since 8.14.0
	 */
	public SilentAuthWorkflow(String to, String redirectUrl) {
		this(builder(to).redirectUrl(redirectUrl));
	}

	/**
	 * Constructs a new Silent Auth verification workflow.
	 *
	 * @param to The number to registered to the device on the network to authenticate.
	 * @param sandbox Whether the Vonage Sandbox should be used (for testing purposes).
	 *
	 * @since 7.10.0
	 * @deprecated The sandbox parameter is deprecated and will be removed in a future release.
	 */
	@Deprecated
	public SilentAuthWorkflow(String to, boolean sandbox) {
		this(builder(to).sandbox(sandbox));
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
	 * @deprecated The sandbox parameter is deprecated and will be removed in a future release.
	 */
	@Deprecated
	public SilentAuthWorkflow(String to, boolean sandbox, String redirectUrl) {
		this(builder(to).redirectUrl(redirectUrl).sandbox(sandbox));
	}

	/**
	 * Optional parameter if using the Vonage Sandbox to test Silent Auth integrations.
	 *
	 * @return Whether the Vonage Sandbox will be used, or {@code null} if not specified (the default).
	 *
	 * @since 7.10.0
	 * @deprecated The sandbox parameter is deprecated and will be removed in a future release.
	 */
	@Deprecated
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

	/**
	 * Entrypoint for constructing an instance of this class.
	 *
	 * @param to (REQUIRED) The number to registered to the device on the network to authenticate.
	 *
	 * @return A new Builder.
	 *
	 * @since 8.2.0
	 */
	public static Builder builder(String to) {
		return new Builder(to);
	}

	/**
	 * Builder for constructing a Silent Authentication workflow.
	 *
	 * @since 8.2.0
	 */
	public static final class Builder extends AbstractNumberWorkflow.Builder<SilentAuthWorkflow, Builder> {
		@Deprecated
		private Boolean sandbox;
		private String redirectUrl;

		private Builder(String to) {
			super(Channel.SILENT_AUTH, to);
		}

		/**
		 * (OPTIONAL) Whether the Vonage Sandbox should be used (for testing purposes).
		 *
		 * @param sandbox {@code true} to use the Vonage sandbox.
		 *
		 * @return This builder.
		 * @deprecated The sandbox parameter is deprecated and will be removed in a future release.
		 */
		@Deprecated
		public Builder sandbox(boolean sandbox) {
			this.sandbox = sandbox;
			return this;
		}

		/**
		 * (OPTIONAL) Final redirect after {@link VerificationResponse#getCheckUrl()}.
		 * See the documentation for integrations.
		 *
		 * @param redirectUrl The full redirect URL as a string.
		 *
		 * @return This builder.
		 */
		public Builder redirectUrl(String redirectUrl) {
			this.redirectUrl = redirectUrl;
			return this;
		}

		@Override
		public SilentAuthWorkflow build() {
			return new SilentAuthWorkflow(this);
		}
	}
}
