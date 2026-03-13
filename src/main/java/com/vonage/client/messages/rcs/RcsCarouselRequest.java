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
import com.vonage.client.common.MessageType;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * {@link com.vonage.client.messages.Channel#RCS}, {@link MessageType#CAROUSEL} request.
 *
 * @since 9.6.0
 */
public final class RcsCarouselRequest extends RcsRequest {
	private List<RcsCard> carousel;

	RcsCarouselRequest(Builder builder) {
		super(builder, MessageType.CAROUSEL);
		this.carousel = Objects.requireNonNull(builder.carousel, "Carousel is required.");
		if (carousel.size() < 2) {
			throw new IllegalArgumentException("A carousel must contain at least 2 cards.");
		}
		if (carousel.size() > 10) {
			throw new IllegalArgumentException("A carousel can contain a maximum of 10 cards.");
		}
	}

	/**
	 * An array of card objects to include in the carousel.
	 * A carousel must contain between 2 and 10 cards.
	 *
	 * @return The list of RCS cards.
	 */
	@JsonProperty("carousel")
	public List<RcsCard> getCarousel() {
		return carousel;
	}

	/**
	 * Entry point for constructing an instance of this class.
	 *
	 * @return A new Builder.
	 */
	public static Builder builder() {
		return new Builder();
	}

	public static final class Builder extends RcsRequest.Builder<RcsCarouselRequest, Builder> {
		private List<RcsCard> carousel;

		Builder() {}

		/**
		 * (REQUIRED)
		 * An array of card objects to include in the carousel.
		 * A carousel must contain between 2 and 10 cards.
		 *
		 * @param carousel The list of RCS cards.
		 * @return This builder.
		 */
		public Builder carousel(List<RcsCard> carousel) {
			this.carousel = carousel;
			return this;
		}

		/**
		 * (REQUIRED)
		 * An array of card objects to include in the carousel.
		 * A carousel must contain between 2 and 10 cards.
		 *
		 * @param carousel The RCS cards as varargs.
		 * @return This builder.
		 */
		public Builder carousel(RcsCard... carousel) {
			return carousel(Arrays.asList(carousel));
		}

		@Override
		public RcsCarouselRequest build() {
			return new RcsCarouselRequest(this);
		}
	}
}
