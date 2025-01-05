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
package com.vonage.client.subaccounts;

import com.vonage.client.QueryParamsRequest;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.LinkedHashMap;
import java.util.Map;

public class ListTransfersFilter implements QueryParamsRequest {
	private final Instant startDate, endDate;
	private final String subaccount;

	ListTransfersFilter(Builder builder) {
		startDate = builder.startDate != null ? builder.startDate : Instant.EPOCH;
		endDate = builder.endDate;
		if ((subaccount = builder.subaccount) != null) {
			AbstractTransfer.validateAccountKey(builder.subaccount, "Subaccount");
		}
	}

	private static String formatTime(Instant timestamp) {
		return timestamp.truncatedTo(ChronoUnit.SECONDS).toString();
	}

	@Override
	public Map<String, String> makeParams() {
		Map<String, String> params = new LinkedHashMap<>(4);
		params.put("start_date", formatTime(startDate));
		if (endDate != null) {
			params.put("end_date", formatTime(endDate));
		}
		if (subaccount != null) {
			params.put("subaccount", subaccount);
		}
		return params;
	}

	/**
	 * Start of the retrieval period.
	 *
	 * @return The start date of the range or {@linkplain Instant#EPOCH} if unspecified (the default).
	 */
	public Instant getStartDate() {
		return startDate;
	}

	/**
	 * End of the retrieval period. If absent then all transfers until now are returned.
	 *
	 * @return The end date of the range or {@code null} if unspecified (the default).
	 */
	public Instant getEndDate() {
		return endDate;
	}

	/**
	 * Subaccount API key to filter by.
	 *
	 * @return The subaccount ID to filter by, or null if unspecified (the default).
	 */
	public String getSubaccount() {
		return subaccount;
	}

	/**
	 * Entry point for constructing an instance of this class.
	 * 
	 * @return A new Builder.
	 */
	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {
		private Instant startDate, endDate;
		private String subaccount;
	
		Builder() {}
	
		/**
		 * (OPTIONAL) Start of the retrieval period.
		 *
		 * @param startDate The start timestamp.
		 *
		 * @return This builder.
		 */
		public Builder startDate(Instant startDate) {
			this.startDate = startDate;
			return this;
		}

		/**
		 * (OPTIONAL) End of the retrieval period. If absent then all transfers until now is returned.
		 *
		 * @param endDate The end timestamp.
		 *
		 * @return This builder.
		 */
		public Builder endDate(Instant endDate) {
			this.endDate = endDate;
			return this;
		}

		/**
		 * (OPTIONAL) Subaccount ID to include in the search. If you set this,
		 * all other subaccounts will be excluded from the search results.
		 *
		 * @param subaccount The subaccount API key to filter by.
		 *
		 * @return This builder.
		 */
		public Builder subaccount(String subaccount) {
			this.subaccount = subaccount;
			return this;
		}
	
		/**
		 * Builds the {@linkplain ListTransfersFilter}.
		 *
		 * @return An instance of ListTransfersFilter, populated with all fields from this builder.
		 */
		public ListTransfersFilter build() {
			return new ListTransfersFilter(this);
		}
	}
}
