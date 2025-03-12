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
package com.vonage.client.conversations;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;

/**
 * Represents a {@linkplain EventType#RTC_STATUS} event.
 *
 * @since 8.19.0
 */
public final class RtcStatusEvent extends AbstractChannelEvent<RtcStatusEvent.Body> {
	RtcStatusEvent() {}

	static class Body extends AbstractChannelEvent.Body {
		@JsonProperty("duration") String duration;
		@JsonProperty("start_time") Instant startTime;
		@JsonProperty("end_time") Instant endTime;
		@JsonProperty("price_currency") String priceCurrency;
		@JsonProperty("price") String price;
		@JsonProperty("mos") Integer mos;
	}

	/**
	 * Returns the duration.
	 *
	 * @return the duration, or {@code null} if unknown.
	 */
	@JsonIgnore
	public String getDuration() {
	    return body != null ? body.duration : null;
	}

	/**
	 * Returns the start time.
	 *
	 * @return the start time, or {@code null} if unknown.
	 */
	@JsonIgnore
	public Instant getStartTime() {
	    return body != null ? body.startTime : null;
	}

	/**
	 * Returns the end time.
	 *
	 * @return the end time, or {@code null} if unknown.
	 */
	@JsonIgnore
	public Instant getEndTime() {
	    return body != null ? body.endTime : null;
	}

	/**
	 * Returns the price currency.
	 *
	 * @return the price currency, or {@code null} if unknown.
	 */
	@JsonIgnore
	public String getPriceCurrency() {
	    return body != null ? body.priceCurrency : null;
	}

	/**
	 * Returns the price.
	 *
	 * @return the price, or {@code null} if unknown.
	 */
	@JsonIgnore
	public String getPrice() {
	    return body != null ? body.price : null;
	}

	/**
	 * Returns the mean opinion score.
	 *
	 * @return the mean opinion score, or {@code null} if unknown.
	 */
	@JsonIgnore
	public Integer getMos() {
	    return body != null ? body.mos : null;
	}
}