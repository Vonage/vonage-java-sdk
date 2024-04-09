
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
package com.vonage.client.messages.whatsapp;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.messages.MessageType;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @since 7.2.0
 */
public final class WhatsappLocationRequest extends WhatsappRequest {
	final Map<String, Object> custom;

	WhatsappLocationRequest(Builder builder) {
		super(builder, MessageType.CUSTOM);
		custom = new LinkedHashMap<>(4);
		custom.put("type", "location");
		custom.put("location", new Location(builder));
	}

	@JsonProperty("custom")
	public Map<?, ?> getCustom() {
		return custom;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static final class Builder extends WhatsappRequest.Builder<WhatsappLocationRequest, Builder> {
		String name, address, text;
		Double latitude, longitude;

		Builder() {}

		/**
		 * (REQUIRED)
		 * Latitude of the location.
		 *
		 * @param latitude The latitude as a double.
		 * @return This builder.
		 */
		public Builder latitude(double latitude) {
			this.latitude = latitude;
			return this;
		}

		/**
		 * (REQUIRED)
		 * Longitude of the location.
		 *
		 * @param longitude The longitude as a double.
		 * @return This builder.
		 */
		public Builder longitude(double longitude) {
			this.longitude = longitude;
			return this;
		}

		/**
		 * (OPTIONAL)
		 * Name of the location.
		 *
		 * @param name The location name.
		 * @return This builder.
		 */
		public Builder name(String name) {
			this.name = name;
			return this;
		}

		/**
		 * (OPTIONAL)
		 * Address of the location. Only displayed if name is present.
		 *
		 * @param address The location address as a string.
		 * @return This builder.
		 */
		public Builder address(String address) {
			this.address = address;
			return this;
		}

		@Override
		public WhatsappLocationRequest build() {
			return new WhatsappLocationRequest(this);
		}
	}
}
