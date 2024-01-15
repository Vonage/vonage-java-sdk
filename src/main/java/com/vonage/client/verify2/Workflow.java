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
import com.vonage.client.JsonableBaseObject;
import java.util.Objects;

/**
 * Base workflow.
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class Workflow extends JsonableBaseObject {
	protected final Channel channel;
	protected String to, from;

	protected Workflow(Builder<?, ?> builder) {
		this(builder.channel, builder.to, builder.from);
	}

	protected Workflow(Channel channel, String to) {
		this(channel, to, null);
	}

	protected Workflow(Channel channel, String to, String from) {
		this.channel = Objects.requireNonNull(channel, "Verification channel is required.");
		this.to = validateTo(to);
		this.from = validateFrom(from);
	}

	protected String validateTo(String to) {
		if ((this.to = to) == null || to.trim().isEmpty()) {
			throw new IllegalArgumentException("Recipient is required.");
		}
		return to;
	}

	protected String validateFrom(String from) {
		if (from != null && from.trim().isEmpty()) {
			throw new IllegalArgumentException("Sender cannot be empty.");
		}
		return from;
	}

	/**
	 * The communication channel for this verification workflow.
	 *
	 * @return The channel as an enum.
	 */
	@JsonProperty("channel")
	public Channel getChannel() {
		return channel;
	}

	/**
	 * The phone number to contact with the message.
	 *
	 * @return The recipient's phone number, in E.164 format.
	 */
	@JsonProperty("to")
	public String getTo() {
		return to;
	}

	/**
	 * Builder class for an SMS workflow.
	 *
	 * @since 8.2.0
	 */
	@SuppressWarnings("unchecked")
	protected abstract static class Builder<W extends Workflow, B extends Builder<? extends W, ? extends B>>  {
		protected final Channel channel;
		protected String to, from;

		protected Builder(Channel channel) {
			this.channel = channel;
		}

		protected B to(String to) {
			this.to = to;
			return (B) this;
		}

		protected B from(String from) {
			this.from = from;
			return (B) this;
		}

		/**
		 * Builds the workflow.
		 *
		 * @return A new instance of the workflow with this builder's fields.
		 */
		public abstract W build();
	}
}
