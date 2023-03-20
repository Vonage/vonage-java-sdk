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
import java.util.Objects;

/**
 * Base workflow.
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class Workflow {
	@JsonProperty("channel") protected Channel channel;
	@JsonProperty("to") protected String to;

	protected Workflow(Channel channel, String to) {
		this.channel = Objects.requireNonNull(channel, "Verification channel is required.");
		if ((this.to = to) == null || to.trim().isEmpty()) {
			throw new IllegalArgumentException("Recipient is required.");
		}
	}

	/**
	 * The phone number to contact with the message.
	 *
	 * @return The recipient's phone number, in E.164 format.
	 */
	public String getTo() {
		return to;
	}
}
