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
package com.vonage.client.meetings;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.net.URI;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LogoUploadsUrlResponse {
	private URI url;
	private Fields fields;

	protected LogoUploadsUrlResponse() {
	}

	/**
	 * Storage system URL.
	 *
	 * @return The URL.
	 */
	@JsonProperty("url")
	public URI getUrl() {
		return url;
	}

	/**
	 * Fields property has to be part of the POST request's body.
	 *
	 * @return The field properties.
	 */
	@JsonProperty("fields")
	public Fields getFields() {
		return fields;
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Fields {
		private LogoType logoType;
		private String contentType, key, bucket, policy,
				amzDate, amzAlgorithm, amzCredential, amzSecurityToken, amzSignature;

		protected Fields() {
		}

		/**
		 * MIME type of the content.
		 *
		 * @return The image type as a string.
		 */
		@JsonProperty("Content-Type")
		public String getContentType() {
			return contentType;
		}

		/**
		 * Logo's key in storage system.
		 *
		 * @return The logo key.
		 */
		@JsonProperty("key")
		public String getKey() {
			return key;
		}

		/**
		 * Logo's type.
		 *
		 * @return The logo type, as an enum.
		 */
		@JsonProperty("logoType")
		public LogoType getLogoType() {
			return logoType;
		}

		/**
		 * Bucket name to upload to.
		 *
		 * @return The bucket name.
		 */
		@JsonProperty("bucket")
		public String getBucket() {
			return bucket;
		}

		@JsonProperty("X-Amz-Algorithm")
		public String getAmzAlgorithm() {
			return amzAlgorithm;
		}

		@JsonProperty("X-Amz-Credential")
		public String getAmzCredential() {
			return amzCredential;
		}

		@JsonProperty("X-Amz-Date")
		public String getAmzDate() {
			return amzDate;
		}

		@JsonProperty("X-Amz-Security-Token")
		public String getAmzSecurityToken() {
			return amzSecurityToken;
		}

		@JsonProperty("Policy")
		public String getPolicy() {
			return policy;
		}

		@JsonProperty("X-Amz-Signature")
		public String getAmzSignature() {
			return amzSignature;
		}
	}
}
