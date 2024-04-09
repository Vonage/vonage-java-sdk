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


/**
 * Defines properties for sending a verification code to a user over WhatsApp
 * using an interaction prompt, where the user selects "Yes" or "No" to verify. See the
 * <a href=https://developer.vonage.com/en/verify/verify-v2/guides/using-whatsapp-interactive>
 * WhatsApp Interactive guide</a> for an overview of how this works.
 * <p>
 * You must have a WhatsApp Business Account configured to use the {@code from} field, which
 * is now a requirement for WhatsApp workflows.
 */
public final class WhatsappCodelessWorkflow extends AbstractWhatsappWorkflow {

	WhatsappCodelessWorkflow(Builder builder) {
		super(builder);
	}

	/**
	 * Constructs a new WhatsApp interactive verification workflow.
	 *
	 * @param to The number to send the verification prompt to, in E.164 format.
	 * @deprecated This no longer works and will be removed in a future release.
	 */
	@Deprecated
	public WhatsappCodelessWorkflow(String to) {
		this(to, null);
	}

	/**
	 * Constructs a new WhatsApp interactive verification workflow.
	 *
	 * @param to The number to send the verification prompt to, in E.164 format.
	 * @param from The WhatsApp Business Account number to send the message from, in E.164 format.
	 * @since 8.3.0
	 */
	public WhatsappCodelessWorkflow(String to, String from) {
		this(builder(to, from));
	}

	static Builder builder(String to, String from) {
		return new Builder(to, from);
	}

	static class Builder extends AbstractWhatsappWorkflow.Builder<WhatsappCodelessWorkflow, Builder> {

		private Builder(String to, String from) {
			super(Channel.WHATSAPP_INTERACTIVE, to, from);
		}

		@Override
		public WhatsappCodelessWorkflow build() {
			return new WhatsappCodelessWorkflow(this);
		}
	}
}
