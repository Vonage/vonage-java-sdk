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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Defines properties for sending a verification code to a user via e-mail.
 * <p>
 * Our email solution supports domain registration, if you plan to scale email verification to high
 * volumes with Verify v2, please contact Sales in order to get your account configured properly.
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public final class EmailWorkflow extends Workflow {

	/**
	 * Constructs a new e-mail verification workflow.
	 *
	 * @param to The email address to send the verification request to.
	 */
	public EmailWorkflow(String to) {
		this(to, null);
	}

	/**
	 * Constructs a new e-mail verification workflow with a custom sender address.
	 *
	 * @param to The email address to send the verification request to.
	 *
	 * @param from The e-mail address to send the verification request from.
	 * Note that you will need to get in touch with the Vonage sales team to enable use of the field.
	 */
	public EmailWorkflow(String to, String from) {
		super(Channel.EMAIL, to, from);
	}

	/**
	 * The e-mail address to send the verification request from, if configured.
	 *
	 * @return The sender e-mail address, or {@code null} if unset.
	 */
	@JsonProperty("from")
	public String getFrom() {
		return from;
	}

	/**
	 * The email address to send the verification request to.
	 *
	 * @return The recipient's e-mail address.
	 */
	@Override
	public String getTo() {
		return super.getTo();
	}
}
