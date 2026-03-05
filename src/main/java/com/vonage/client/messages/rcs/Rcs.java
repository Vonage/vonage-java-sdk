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
import com.vonage.client.JsonableBaseObject;

/**
 * RCS-specific parameters.
 *
 * @since 9.6.0
 */
public final class Rcs extends JsonableBaseObject {
	String category;
	CardOrientation cardOrientation;
	ImageAlignment imageAlignment;
	CardWidth cardWidth;

	Rcs() {}

	private Rcs(Builder builder) {
		this.category = builder.category;
		this.cardOrientation = builder.cardOrientation;
		this.imageAlignment = builder.imageAlignment;
		this.cardWidth = builder.cardWidth;
	}

	/**
	 * Creates an RCS options object with the specified category.
	 *
	 * @param category The RCS message category.
	 */
	public Rcs(String category) {
		this.category = category;
	}

	/**
	 * The RCS message category.
	 *
	 * @return The category as a string, or {@code null} if not set.
	 */
	@JsonProperty("category")
	public String getCategory() {
		return category;
	}

	/**
	 * The orientation of the rich card.
	 *
	 * @return The card orientation, or {@code null} if not set.
	 *
	 * @since 9.9.0
	 */
	@JsonProperty("card_orientation")
	public CardOrientation getCardOrientation() {
		return cardOrientation;
	}

	/**
	 * The alignment of the thumbnail image in the rich card.
	 * This property only applies when sending rich cards with a card_orientation of HORIZONTAL.
	 *
	 * @return The image alignment, or {@code null} if not set.
	 *
	 * @since 9.9.0
	 */
	@JsonProperty("image_alignment")
	public ImageAlignment getImageAlignment() {
		return imageAlignment;
	}

	/**
	 * The width of the rich cards displayed in the carousel.
	 *
	 * @return The card width, or {@code null} if not set.
	 *
	 * @since 9.9.0
	 */
	@JsonProperty("card_width")
	public CardWidth getCardWidth() {
		return cardWidth;
	}

	/**
	 * Entry point for constructing an instance of this class.
	 *
	 * @return A new Builder.
	 *
	 * @since 9.9.0
	 */
	public static Builder builder() {
		return new Builder();
	}

	/**
	 * Builder for constructing an Rcs object with fluent API.
	 *
	 * @since 9.9.0
	 */
	public static final class Builder {
		private String category;
		private CardOrientation cardOrientation;
		private ImageAlignment imageAlignment;
		private CardWidth cardWidth;

		Builder() {}

		/**
		 * (OPTIONAL)
		 * Sets the RCS message category.
		 *
		 * @param category The RCS category.
		 * @return This builder.
		 */
		public Builder category(String category) {
			this.category = category;
			return this;
		}

		/**
		 * (OPTIONAL)
		 * Sets the orientation of the rich card.
		 *
		 * @param cardOrientation The card orientation.
		 * @return This builder.
		 */
		public Builder cardOrientation(CardOrientation cardOrientation) {
			this.cardOrientation = cardOrientation;
			return this;
		}

		/**
		 * (OPTIONAL)
		 * Sets the alignment of the thumbnail image in the rich card.
		 * This property only applies when sending rich cards with a card_orientation of HORIZONTAL.
		 *
		 * @param imageAlignment The image alignment.
		 * @return This builder.
		 */
		public Builder imageAlignment(ImageAlignment imageAlignment) {
			this.imageAlignment = imageAlignment;
			return this;
		}

		/**
		 * (OPTIONAL)
		 * Sets the width of the rich cards displayed in the carousel.
		 *
		 * @param cardWidth The card width.
		 * @return This builder.
		 */
		public Builder cardWidth(CardWidth cardWidth) {
			this.cardWidth = cardWidth;
			return this;
		}

		/**
		 * Builds the Rcs object.
		 *
		 * @return A new Rcs instance.
		 */
		public Rcs build() {
			return new Rcs(this);
		}
	}
}
