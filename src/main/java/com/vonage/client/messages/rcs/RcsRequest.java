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
package com.vonage.client.messages.rcs;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.messages.Channel;
import com.vonage.client.messages.MessageRequest;
import com.vonage.client.common.MessageType;

/**
 * Base class for RCS messages.
 *
 * @since 8.11.0
 */
public abstract class RcsRequest extends MessageRequest {
	protected Rcs rcs;

	protected RcsRequest(Builder<?, ?> builder, MessageType messageType) {
		super(builder, Channel.RCS, messageType);
		this.rcs = builder.rcs;
	}

	@JsonProperty("ttl")
	public Integer getTtl() {
		return ttl;
	}

	@JsonProperty("rcs")
	public Rcs getRcs() {
		return rcs;
	}

	@SuppressWarnings("unchecked")
	protected abstract static class Builder<M extends RcsRequest, B extends Builder<? extends M, ? extends B>> extends MessageRequest.Builder<M, B> {
		protected Rcs rcs;

		/**
		 * (OPTIONAL)
		 * Sets RCS-specific parameters.
		 *
		 * @param rcs The RCS options object.
		 * @return This builder.
		 *
		 * @since 9.5.0
		 */
		public B rcs(Rcs rcs) {
			this.rcs = rcs;
			return (B) this;
		}

		/**
		 * (OPTIONAL)
		 * Sets the RCS message category.
		 *
		 * @param category The RCS category.
		 * @return This builder.
		 *
		 * @since 9.5.0
		 */
		public B rcsCategory(String category) {
			return rcs(new Rcs(category));
		}

		@Override
		protected B ttl(int ttl) {
			return super.ttl(ttl);
		}
	}
}
