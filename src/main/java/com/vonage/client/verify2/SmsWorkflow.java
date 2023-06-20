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
	 *
	 * @param appHash Android Application Hash Key for automatic code detection on a user's device.
	 */
	public SmsWorkflow(String to, String appHash) {
		super(Channel.SMS, to);
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
}
