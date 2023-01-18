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
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vonage.client.VonageUnexpectedException;
import java.io.IOException;
import java.net.URI;
import java.util.Objects;
import java.util.UUID;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Theme {
	private UUID themeId;
	private String themeName, mainColor;
	private ThemeDomain domain;
	private String accountId, applicationId;
	private String brandText, brandImageColored, brandImageWhite, brandedFavicon;
	private URI shortCompanyUrl, brandImageColoredUrl, brandImageWhiteUrl, brandedFaviconUrl;

	protected Theme() {
	}

	Theme(Builder builder) {
		brandText = Objects.requireNonNull(builder.brandText, "Brand text is required.");
		if (brandText.length() > 200) {
			throw new IllegalArgumentException("Brand text cannot exceed 200 characters.");
		}

		mainColor = Objects.requireNonNull(builder.mainColor, "Main color is required.");
		if (mainColor.length() != 7) {
			throw new IllegalArgumentException("Main color must be 7 characters.");
		}

		if ((themeName = builder.themeName) != null && themeName.length() > 200) {
			throw new IllegalArgumentException("Theme name cannot exceed 200 characters.");
		}

		if (builder.shortCompanyUrl != null) {
			if (builder.shortCompanyUrl.length() > 128) {
				throw new IllegalArgumentException("Short company URL cannot exceed 128 characters.");
			}
			shortCompanyUrl = URI.create(builder.shortCompanyUrl);
		}
	}

	/**
	 * @return The theme ID.
	 */
	@JsonProperty("theme_id")
	public UUID getThemeId() {
		return themeId;
	}

	/**
	 * @return Name of the theme.
	 */
	@JsonProperty("theme_name")
	public String getThemeName() {
		return themeName;
	}

	/**
	 * @return The domain.
	 */
	@JsonProperty("domain")
	public ThemeDomain getDomain() {
		return domain;
	}

	/**
	 * @return The account ID.
	 */
	@JsonProperty("account_id")
	public String getAccountId() {
		return accountId;
	}

	/**
	 * @return The application ID.
	 */
	@JsonProperty("application_id")
	public String getApplicationId() {
		return applicationId;
	}

	/**
	 * @return The main theme colour (hex value).
	 */
	@JsonProperty("main_color")
	public String getMainColor() {
		return mainColor;
	}

	/**
	 * @return The company's short URL.
	 */
	@JsonProperty("short_company_url")
	public URI getShortCompanyUrl() {
		return shortCompanyUrl;
	}

	/**
	 * @return The branding text.
	 */
	@JsonProperty("brand_text")
	public String getBrandText() {
		return brandText;
	}

	/**
	 * Coloured logo's key in storage system.
	 *
	 * @return The brand image in colour.
	 */
	@JsonProperty("brand_image_colored")
	public String getBrandImageColored() {
		return brandImageColored;
	}

	/**
	 * White logo's key in storage system.
	 *
	 * @return The brand image in white.
	 */
	@JsonProperty("brand_image_white")
	public String getBrandImageWhite() {
		return brandImageWhite;
	}

	/**
	 * Favicon's key in storage system.
	 *
	 * @return The brand favicon key.
	 */
	@JsonProperty("branded_favicon")
	public String getBrandedFavicon() {
		return brandedFavicon;
	}

	/**
	 * Coloured logo's link.
	 *
	 * @return The coloured brand image URL.
	 */
	@JsonProperty("brand_image_colored_url")
	public URI getBrandImageColoredUrl() {
		return brandImageColoredUrl;
	}

	/**
	 * White logo's link.
	 *
	 * @return The white brand image URL.
	 */
	@JsonProperty("brand_image_white_url")
	public URI getBrandImageWhiteUrl() {
		return brandImageWhiteUrl;
	}

	/**
	 * Favicon's link.
	 *
	 * @return The favicon URL.
	 */
	@JsonProperty("branded_favicon_url")
	public URI getBrandedFaviconUrl() {
		return brandedFaviconUrl;
	}
	
	/**
	 * Creates an instance of this class from a JSON payload.
	 *
	 * @param json The JSON string to parse.
	 * @return An instance of this class with the fields populated, if present.
	 */
	public static Theme fromJson(String json) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			return mapper.readValue(json, Theme.class);
		}
		catch (IOException ex) {
			throw new VonageUnexpectedException("Failed to produce Theme from json.", ex);
		}
	}
	
	/**
	 * Generates a JSON payload from this request.
	 *
	 * @return JSON representation of this Theme object.
	 */
	public String toJson() {
		try {
			ObjectMapper mapper = new ObjectMapper();
			return mapper.writeValueAsString(this);
		}
		catch (JsonProcessingException jpe) {
			throw new VonageUnexpectedException("Failed to produce JSON from "+getClass().getSimpleName()+" object.", jpe);
		}
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
		private String themeName, mainColor, brandText, shortCompanyUrl;
	
		Builder() {}
	
		/**
		 * (OPTIONAL) The name to give the theme (maximum 200 characters).
		 *
		 * @param themeName The theme name.
		 *
		 * @return This builder.
		 */
		public Builder themeName(String themeName) {
			this.themeName = themeName;
			return this;
		}

		/**
		 * (REQUIRED) The main theme colour code (hex value).
		 *
		 * @param mainColor The colour's hex code.
		 *
		 * @return This builder.
		 */
		public Builder mainColor(String mainColor) {
			this.mainColor = mainColor;
			return this;
		}

		/**
		 * (OPTIONAL) The short company URL. Must be a valid URI, cannot exceed 128 characters.
		 *
		 * @param shortCompanyUrl The URL as a String.
		 *
		 * @return This builder.
		 */
		public Builder shortCompanyUrl(String shortCompanyUrl) {
			this.shortCompanyUrl = shortCompanyUrl;
			return this;
		}

		/**
		 * (REQUIRED) The brand / logo text.
		 *
		 * @param brandText The text.
		 *
		 * @return This builder.
		 */
		public Builder brandText(String brandText) {
			this.brandText = brandText;
			return this;
		}

	
		/**
		 * Builds the {@linkplain Theme}.
		 *
		 * @return An instance of Theme, populated with all fields from this builder.
		 */
		public Theme build() {
			return new Theme(this);
		}
	}
}
