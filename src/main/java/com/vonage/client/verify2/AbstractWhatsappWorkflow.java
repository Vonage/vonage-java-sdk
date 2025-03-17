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

/**
 * Intermediate class for WhatsApp workflows.
 *
 * @since 8.3.0
 */
abstract class AbstractWhatsappWorkflow extends AbstractNumberWorkflow {

	protected AbstractWhatsappWorkflow(Builder<?, ?> builder) {
		super(builder);
	}

	/**
	 * The number to send the verification request from.
	 *
	 * @return The sender WABA number in E.164 format.
	 */
	@JsonProperty("from")
	public String getFrom() {
		return from;
	}

	protected abstract static class Builder<
			N extends AbstractWhatsappWorkflow,
			B extends AbstractWhatsappWorkflow.Builder<? extends N, ? extends B>
			> extends AbstractNumberWorkflow.Builder<N, B> {

		protected Builder(Channel channel, String to, String from) {
			super(channel, to);
			from(from);
		}
	}
}
