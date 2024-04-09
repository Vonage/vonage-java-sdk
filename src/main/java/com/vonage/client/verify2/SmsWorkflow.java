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
package com.vonage.client.verify2;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Defines workflow properties for sending a verification code to a user via SMS.
 */
public final class SmsWorkflow extends AbstractNumberWorkflow {
	final String appHash, contentId, entityId;

	SmsWorkflow(Builder builder) {
		super(builder);
		if ((this.appHash = builder.appHash) != null && appHash.length() != 11) {
			throw new IllegalArgumentException("Android app hash must be 11 characters.");
		}
		if ((this.contentId = builder.contentId) != null && (contentId.isEmpty() || contentId.length() > 20)) {
			throw new IllegalArgumentException("content_id must be between 1 and 20 characters long.");
		}
		if ((this.entityId = builder.entityId) != null && (entityId.isEmpty() || entityId.length() > 20)) {
			throw new IllegalArgumentException("entity_id must be between 1 and 20 characters long.");
		}
	}

	/**
	 * Constructs a new SMS verification workflow.
	 *
	 * @param to The number to send the message to, in E.164 format.
	 */
	public SmsWorkflow(String to) {
		this(builder(to));
	}

	/**
	 * Constructs a new SMS verification workflow.
	 *
	 * @param to The number to send the message to, in E.164 format.
	 * @param appHash Android Application Hash Key for automatic code detection on a user's device.
	 *
	 * @deprecated Use {@linkplain #builder(String)}.
	 */
	@Deprecated
	public SmsWorkflow(String to, String appHash) {
		this(builder(to).appHash(appHash));
	}

	/**
	 * Constructs a new SMS verification workflow.
	 *
	 * @param to The number to send the message to, in E.164 format.
	 * @param from The number or sender ID to send the SMS from.
	 * @param appHash Android Application Hash Key for automatic code detection on a user's device.
	 *
	 * @since 8.1.0
	 * @deprecated Use {@linkplain #builder(String)} instead.
	 */
	@Deprecated
	public SmsWorkflow(String to, String from, String appHash) {
		this(builder(to).from(from).appHash(appHash));
	}

	/**
	 * Android Application Hash Key for automatic code detection on a user's device.
	 *
	 * @return The Android application hash key (11 characters in length), or {@code null} if not set.
	 */
	@JsonProperty("app_hash")
	public String getAppHash() {
		return appHash;
	}

	/**
	 * The number or sender ID the message will be sent from.
	 *
	 * @return The sender phone number or sender ID, or {@code null} if unspecified.
	 *
	 * @since 8.1.0
	 */
	@JsonProperty("from")
	public String getFrom() {
		return from;
	}

	/**
	 * Optional value corresponding to a TemplateID for SMS delivery using Indian Carriers.
	 *
	 * @return The content ID, or {@code null} if unspecified.
	 *
	 * @since 8.2.0
	 */
	@JsonProperty("content_id")
	public String getContentId() {
		return contentId;
	}

	/**
	 * Optional PEID required for SMS delivery using Indian Carriers.
	 *
	 * @return The entity ID, or {@code null} if unspecified.
	 *
	 * @since 8.2.0
	 */
	@JsonProperty("entity_id")
	public String getEntityId() {
		return entityId;
	}

	/**
	 * Entrypoint for constructing an instance of this class.
	 *
	 * @param to (REQUIRED) The destination phone number in E.164 format.
	 *
	 * @return A new Builder.
	 *
	 * @since 8.2.0
	 */
	public static Builder builder(String to) {
		return new Builder(to);
	}

	/**
	 * Builder class for an SMS workflow.
	 *
	 * @since 8.2.0
	 */
	public static final class Builder extends AbstractNumberWorkflow.Builder<SmsWorkflow, Builder> {
		private String from, appHash, contentId, entityId;

		private Builder(String to) {
			super(Channel.SMS, to);
		}

		/**
		 * (OPTIONAL) The number or ID to send the SMS from.
		 *
		 * @param from The sender number in E.164 format, or an alphanumeric ID (50 characters max).
		 *
		 * @return This builder.
		 */
		@Override
		public Builder from(String from) {
			return super.from(from);
		}

		/**
		 * (OPTIONAL) Android Application Hash Key for automatic code detection on a user's device.
		 *
		 * @param appHash The 11 character Android app hash.
		 *
		 * @return This builder.
		 */
		public Builder appHash(String appHash) {
			this.appHash = appHash;
			return this;
		}

		/**
		 * (OPTIONAL) The TemplateID for SMS delivery using Indian Carriers.
		 *
		 * @param contentId The template ID (maximum 20 characters).
		 *
		 * @return This builder.
		 */
		public Builder contentId(String contentId) {
			this.contentId = contentId;
			return this;
		}

		/**
		 * (OPTIONAL) PEID field required for SMS delivery using Indian Carriers.
		 *
		 * @param entityId The PEID (maximum 20 characters).
		 *
		 * @return This builder.
		 */
		public Builder entityId(String entityId) {
			this.entityId = entityId;
			return this;
		}

		@Override
		public SmsWorkflow build() {
			return new SmsWorkflow(this);
		}
	}
}
