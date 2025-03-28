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

	protected RcsRequest(Builder<?, ?> builder, MessageType messageType) {
		super(builder, Channel.RCS, messageType);
	}

	@JsonProperty("ttl")
	public Integer getTtl() {
		return ttl;
	}

	protected abstract static class Builder<M extends RcsRequest, B extends Builder<? extends M, ? extends B>> extends MessageRequest.Builder<M, B> {

		@Override
		protected B ttl(int ttl) {
			return super.ttl(ttl);
		}
	}
}
