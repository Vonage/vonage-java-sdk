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
package com.vonage.client.messages.mms;

import com.vonage.client.messages.Channel;
import com.vonage.client.messages.MessageRequest;
import com.vonage.client.messages.MessageType;
import com.vonage.client.messages.internal.MessagePayload;

public abstract class MmsRequest extends MessageRequest {
	MessagePayload payload;

	protected MmsRequest(Builder<?, ?> builder, MessageType messageType) {
		super(builder, Channel.MMS, messageType);
	}

	@SuppressWarnings("unchecked")
	protected abstract static class Builder<M extends MmsRequest, B extends Builder<? extends M, ? extends B>> extends MessageRequest.Builder<M, B> {
		String url, caption;

		protected B url(String url) {
			this.url = url;
			return (B) this;
		}

		protected B caption(String caption) {
			this.caption = caption;
			return (B) this;
		}
	}
}
