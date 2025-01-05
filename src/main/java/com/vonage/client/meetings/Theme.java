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
package com.vonage.client.meetings;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.Jsonable;
import com.vonage.client.JsonableBaseObject;
import java.net.URI;
import java.util.UUID;
import java.util.regex.Pattern;

public class Theme extends JsonableBaseObject {
	static final Pattern COLOR_PATTERN = Pattern.compile("(#[a-fA-F0-9]{6}|[a-fA-F0-9]{3})");

	@JsonIgnore private boolean update = false;
	private UUID themeId, applicationId;
	private ThemeDomain domain;
	private String themeName, mainColor, accountId, shortCompanyUrl,
			brandText, brandImageColored, brandImageWhite, brandedFavicon;
	private URI brandImageColoredUrl, brandImageWhiteUrl, brandedFaviconUrl;

	protected Theme() {
	}

	Theme(Builder builder) {
		if ((mainColor = builder.mainColor) != null && !COLOR_PATTERN.matcher(mainColor).matches()) {
			throw new IllegalArgumentException("Main color must be a valid hex pallet.");
		}

		if ((brandText = builder.brandText) != null) {
			if (brandText.length() > 200) {
				throw new IllegalArgumentException("Brand text cannot exceed 200 characters.");
			}
			else if (brandText.trim().isEmpty()) {
				throw new IllegalArgumentException("Brand text cannot be blank.");
			}
		}

		if ((themeName = builder.themeName) != null) {
			if (themeName.length() > 200) {
				throw new IllegalArgumentException("Theme name cannot exceed 200 characters.");
			}
			else if (themeName.trim().isEmpty()) {
				throw new IllegalArgumentException("Theme name cannot be blank.");
			}
		}

		if ((shortCompanyUrl = builder.shortCompanyUrl) != null) {
			if (shortCompanyUrl.length() > 128) {
				throw new IllegalArgumentException("Short company URL cannot exceed 128 characters.");
			}
			else if (shortCompanyUrl.trim().isEmpty()) {
				throw new IllegalArgumentException("Short company URL cannot be blank.");
			}
		}
	}

	@JsonIgnore
	void setThemeIdAndFlagUpdate(UUID themeId) {
		this.themeId = themeId;
		update = true;
	}

	/**
	 * Unique theme ID.
	 *
	 * @return The theme ID.
	 */
	@JsonProperty("theme_id")
	public UUID getThemeId() {
		return themeId;
	}

	/**
	 * Theme name.
	 *
	 * @return Name of the theme.
	 */
	@JsonProperty("theme_name")
	public String getThemeName() {
		return themeName;
	}

	/**
	 * The theme's application domain.
	 *
	 * @return The domain as an enum.
	 */
	@JsonProperty("domain")
	public ThemeDomain getDomain() {
		return domain;
	}

	/**
	 * Account ID.
	 *
	 * @return The account ID (API key).
	 */
	@JsonProperty("account_id")
	public String getAccountId() {
		return accountId;
	}

	/**
	 * Unique application ID.
	 *
	 * @return The application ID.
	 */
	@JsonProperty("application_id")
	public UUID getApplicationId() {
		return applicationId;
	}

	/**
	 * Main colour hex code.
	 *
	 * @return The main theme colour (hex value).
	 */
	@JsonProperty("main_color")
	public String getMainColor() {
		return mainColor;
	}

	/**
	 * Company name to include in the URL.
	 *
	 * @return The company's short URL.
	 */
	@JsonProperty("short_company_url")
	public String getShortCompanyUrl() {
		return shortCompanyUrl;
	}

	/**
	 * Brand text that will appear in the application.
	 *
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

	@Override
	public String toJson() {
		if (update) {
			UUID tempId = themeId;
			themeId = null;
			String json = "{\"update_details\":" + super.toJson() + "}";
			update = false;
			themeId = tempId;
			return json;
		}
		else {
			return super.toJson();
		}
	}

	/**
	 * Creates an instance of this class from a JSON payload.
	 *
	 * @param json The JSON string to parse.
	 * @return An instance of this class with the fields populated, if present.
	 */
	public static Theme fromJson(String json) {
		return Jsonable.fromJson(json);
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
		 * @param mainColor The colour's hex code, including the # character.
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
