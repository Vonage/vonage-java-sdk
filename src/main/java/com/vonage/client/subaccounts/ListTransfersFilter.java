/*
 *   Copyright 2023 Vonage
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

import org.apache.http.client.methods.RequestBuilder;
import java.time.Instant;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

public class ListTransfersFilter {
	private final Instant startDate, endDate;
	private final Set<String> subaccounts;

	ListTransfersFilter(Builder builder) {
		startDate = builder.startDate != null ? builder.startDate : Instant.EPOCH;
		endDate = builder.endDate;
		subaccounts = builder.subaccounts;
	}
	
	RequestBuilder addParams(RequestBuilder request) {
        request.addParameter("start_date", startDate.toString());
		if (endDate != null) {
            request.addParameter("end_date", endDate.toString());
        }
		if (subaccounts != null) {
			for (String subaccount : subaccounts) {
				request.addParameter("subaccount", subaccount);
			}
        }
		return request;
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
	 * Subaccount API keys to filter by.
	 *
	 * @return The set of subaccount keys to include in the results, or null if unspecified (the default).
	 */
	public Set<String> getSubaccounts() {
		return subaccounts;
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
		private Set<String> subaccounts;
	
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
		 * (OPTIONAL) Subaccount IDs to include in the search. If you set this,
		 * all other subaccounts will be excluded from the search results.
		 *
		 * @param subaccounts The subaccount API keys to filter by.
		 *
		 * @return This builder.
		 */
		public Builder subaccounts(String... subaccounts) {
			this.subaccounts = new LinkedHashSet<>(Arrays.asList(subaccounts));
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
