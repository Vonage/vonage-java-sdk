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
package com.vonage.client.meetings;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vonage.client.VonageResponseParseException;
import com.vonage.client.VonageUnexpectedException;
import javax.activation.MimeType;
import javax.activation.MimeTypeParseException;
import java.io.IOException;
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
	
	/**
	 * Creates an instance of this class from a JSON payload.
	 *
	 * @param json The JSON string to parse.
	 * @return An instance of this class with the fields populated, if present.
	 */
	public static LogoUploadsUrlResponse fromJson(String json) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			return mapper.readValue(json, LogoUploadsUrlResponse.class);
		}
		catch (IOException ex) {
			throw new VonageUnexpectedException("Failed to produce LogoUploadsUrlResponse from json.", ex);
		}
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Fields {
		private LogoType logoType;
		private MimeType contentType;
		private String key, bucket, policy, amzAlgorithm, amzCredential, amzDate, amzSecurityToken, amzSignature;

		protected Fields() {
		}

		/**
		 * MIME type of the content.
		 *
		 * @return The image type.
		 */
		public MimeType getContentType() {
			return contentType;
		}

		@JsonSetter("Content-Type")
		protected void setContentType(String contentType) {
			try {
				this.contentType = new MimeType(contentType);
			}
			catch (MimeTypeParseException ex) {
				throw new VonageResponseParseException("Invalid MIME type: "+contentType, ex);
			}
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
