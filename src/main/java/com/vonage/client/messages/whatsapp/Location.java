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
import com.vonage.client.JsonableBaseObject;

/**
 * Whatsapp Location message parameters.
 *
 * @since 7.2.0
 */
public final class Location extends JsonableBaseObject {
	private double latitude, longitude;
	private String name, address;

	Location() {}

	Location(WhatsappLocationRequest.Builder builder) {
		if (builder.latitude == null || builder.longitude == null) {
			throw new IllegalStateException("Both latitude and longitude are required.");
		}
		latitude = builder.latitude;
		longitude = builder.longitude;
		name = builder.name;
		address = builder.address;
	}

	@JsonProperty("lat")
	public double getLatitude() {
		return latitude;
	}

	@JsonProperty("long")
	public double getLongitude() {
		return longitude;
	}

	@JsonProperty("name")
	public String getName() {
		return name;
	}

	@JsonProperty("address")
	public String getAddress() {
		return address;
	}
}
