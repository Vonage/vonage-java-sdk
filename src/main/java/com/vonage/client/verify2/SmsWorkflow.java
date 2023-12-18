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

/**
 * Defines workflow properties for sending a verification code to a user via SMS.
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public final class SmsWorkflow extends Workflow {
	final String appHash;

	/**
	 * Constructs a new SMS verification workflow.
	 *
	 * @param to The number to send the message to, in E.164 format.
	 */
	public SmsWorkflow(String to) {
		this(to, null);
	}

	/**
	 * Constructs a new SMS verification workflow.
	 *
	 * @param to The number to send the message to, in E.164 format.
	 * @param appHash Android Application Hash Key for automatic code detection on a user's device.
	 *
	 * @deprecated Use {@linkplain #SmsWorkflow(String, String, String)}.
	 */
	@Deprecated
	public SmsWorkflow(String to, String appHash) {
		this(to, null, appHash);
	}

	/**
	 * Constructs a new SMS verification workflow.
	 *
	 * @param to The number to send the message to, in E.164 format.
	 * @param from The number or sender ID to send the SMS from.
	 * @param appHash Android Application Hash Key for automatic code detection on a user's device.
	 *
	 * @since 8.1.0
	 */
	public SmsWorkflow(String to, String from, String appHash) {
		super(Channel.SMS, new E164(to).toString(), from);
		if ((this.appHash = appHash) != null && appHash.length() != 11) {
			throw new IllegalArgumentException("Android app hash must be 11 characters.");
		}
	}

	/**
	 * Android Application Hash Key for automatic code detection on a user's device.
	 *
	 * @return The Android application hash key (11 characters in length), or {@code null} if not set.
	 */
	@JsonProperty("app_hash")
	public String getAppHash() {
		return appHash;
	}

	/**
	 * The number or sender ID the message will be sent from.
	 *
	 * @return The sender phone number or sender ID, or {@code null} if unspecified.
	 *
	 * @since 8.1.0
	 */
	@JsonProperty("from")
	public String getFrom() {
		return from;
	}
}
