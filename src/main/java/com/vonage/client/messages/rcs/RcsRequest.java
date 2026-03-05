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
		int min = 300, max = 2592000;
		if (ttl != null && (ttl < min || ttl > max)) {
			throw new IllegalArgumentException("TTL must be between "+min+" and "+max+" seconds.");
		}
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

		/**
		 * (OPTIONAL)
		 * Indicates if the recipient is trusted.
		 *
		 * @param trustedRecipient Whether the recipient is trusted (true or false).
		 * @return This builder.
		 *
		 * @since 9.8.0
		 */
		public B trustedRecipient(Boolean trustedRecipient) {
			if (rcs == null) {
				rcs = new Rcs();
			}
			rcs.trustedRecipient = trustedRecipient;
			return (B) this;
		}

		/**
		 * (OPTIONAL)
		 * Sets the orientation of the rich card.
		 *
		 * @param cardOrientation The card orientation.
		 * @return This builder.
		 *
		 * @since 9.9.0
		 */
		public B cardOrientation(CardOrientation cardOrientation) {
			if (rcs == null) {
				rcs = new Rcs();
			}
			rcs.cardOrientation = cardOrientation;
			return (B) this;
		}

		/**
		 * (OPTIONAL)
		 * Sets the alignment of the thumbnail image in the rich card.
		 * This property only applies when sending rich cards with a card_orientation of HORIZONTAL.
		 *
		 * @param imageAlignment The image alignment.
		 * @return This builder.
		 *
		 * @since 9.9.0
		 */
		public B imageAlignment(ImageAlignment imageAlignment) {
			if (rcs == null) {
				rcs = new Rcs();
			}
			rcs.imageAlignment = imageAlignment;
			return (B) this;
		}

		/**
		 * (OPTIONAL)
		 * Sets the width of the rich cards displayed in the carousel.
		 *
		 * @param cardWidth The card width.
		 * @return This builder.
		 *
		 * @since 9.9.0
		 */
		public B cardWidth(CardWidth cardWidth) {
			if (rcs == null) {
				rcs = new Rcs();
			}
			rcs.cardWidth = cardWidth;
			return (B) this;
		}

		/**
		 * (OPTIONAL)
		 * Sets the time-to-live for the RCS message.
		 *
		 * @param ttl The duration in seconds the delivery of a message will be attempted,
		 *            between 300 and 2592000 seconds.
		 * @return This builder.
		 */
		@Override
		public B ttl(int ttl) {
			return super.ttl(ttl);
		}
	}
}
