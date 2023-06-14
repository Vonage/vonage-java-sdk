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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.vonage.client.VonageResponseParseException;
import com.vonage.client.VonageUnexpectedException;
import java.io.IOException;
import java.util.Objects;

/**
 * Base class for number and balance / credit transfers.
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class AbstractTransfer {
	private String from, to, primaryAccountId;

	protected AbstractTransfer() {
	}

	protected AbstractTransfer(Builder<?, ?> builder) {
		from = validateAccountKey(builder.from, "From account");
		to = validateAccountKey(builder.to, "To account");
	}

	static String validateAccountKey(String key, String name) {
		String message = name + " API key is required.";
		if (Objects.requireNonNull(key, message).trim().isEmpty() || key.length() != 8) {
			throw new IllegalArgumentException("Invalid '"+name+"' API key.");
		}
		return key;
	}

	/**
	 * Primary account ID for this transaction.
	 *
	 * @return The primary account API key, or {@code null} if unknown.
	 */
	@JsonProperty("masterAccountId")
	public String getPrimaryAccountId() {
		return primaryAccountId;
	}

	/**
	 * Account to transfer from.
	 * 
	 * @return The transferer account ID.
	 */
	@JsonProperty("from")
	public String getFrom() {
		return from;
	}

	/**
	 * Account to transfer to.
	 * 
	 * @return The transferee account ID.
	 */
	@JsonProperty("to")
	public String getTo() {
		return to;
	}

	/**
	 * Generates a JSON payload from this request.
	 *
	 * @return JSON representation of this CreditTransfer object.
	 */
	public String toJson() {
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.registerModule(new JavaTimeModule());
			return mapper.writeValueAsString(this);
		}
		catch (JsonProcessingException jpe) {
			throw new VonageUnexpectedException("Failed to produce JSON from "+getClass().getSimpleName()+" object.", jpe);
		}
	}

	/**
	 * Updates (hydrates) this object's fields from additional JSON data.
	 *
	 * @param json The JSON payload.
	 */
	public void updateFromJson(String json) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.registerModule(new JavaTimeModule());
			mapper.readerForUpdating(this).readValue(json, getClass());
		}
		catch (IOException ex) {
			throw new VonageResponseParseException("Failed to update "+getClass().getSimpleName()+" from json.", ex);
		}
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName()+' '+toJson();
	}

	@SuppressWarnings("unchecked")
	protected abstract static class Builder<T extends AbstractTransfer, B extends Builder<? extends T, ? extends B>> {
		private String from, to;
	
		Builder() {}

		/**
		 * Account to transfer from.
		 *
		 * @param from The transferer account ID.
		 *
		 * @return This builder.
		 */
		public B from(String from) {
			this.from = from;
			return (B) this;
		}

		/**
		 * (REQUIRED) Account to transfer to.
		 *
		 * @param to The transferee account ID.
		 *
		 * @return This builder.
		 */
		public B to(String to) {
			this.to = to;
			return (B) this;
		}

		public abstract T build();
	}
}
