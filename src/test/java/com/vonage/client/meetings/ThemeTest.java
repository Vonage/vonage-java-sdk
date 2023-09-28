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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.VonageResponseParseException;
import com.vonage.client.VonageUnexpectedException;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.Test;
import java.net.URI;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ThemeTest {

	@Test
	public void testSerializeAndParseCommonParameters() {
		String themeName = "Test theme 1";
		String mainColor = "#1e1e1f";
		String shortCompanyUrl = "t.co/VonageDev";
		String brandText = "Nexmo";
		Theme request = Theme.builder()
			.themeName(themeName)
			.mainColor(mainColor)
			.shortCompanyUrl(shortCompanyUrl)
			.brandText(brandText)
			.build();
		
		String json = request.toJson();
		assertTrue(json.contains("\"theme_name\":\""+themeName+"\""));
		assertTrue(json.contains("\"main_color\":\""+mainColor+"\""));
		assertTrue(json.contains("\"short_company_url\":\""+shortCompanyUrl+"\""));
		assertTrue(json.contains("\"brand_text\":\""+brandText+"\""));
	
		Theme response = Theme.fromJson(json);
		assertEquals(request.getThemeName(), response.getThemeName());
		assertEquals(request.getMainColor(), response.getMainColor());
		assertEquals(request.getShortCompanyUrl(), response.getShortCompanyUrl());
		assertEquals(request.getBrandText(), response.getBrandText());
	}

	@Test
	public void testFromJsonAllFields() {
		UUID themeId = UUID.randomUUID();
		String themeName = "Another name";
		ThemeDomain domain = ThemeDomain.VCP;
		String accountId = "account identifier";
		UUID applicationId = UUID.randomUUID();
		String mainColor = "#fff000";
		String shortCompanyUrl = "www.example.com";
		String brandText = "Test brand";
		String brandImageColored = "color_key";
		String brandImageWhite = "white_key";
		String brandedFavicon = "favicon_key";
		URI brandImageColoredUrl = URI.create("https://example.com/colour.jpg");
		URI brandImageWhiteUrl = URI.create("https://example.com/white.jpg");
		URI brandedFaviconUrl = URI.create("https://example.com/favicon.jpg");
	
		Theme response = Theme.fromJson("{\n" +
				"\"theme_id\":\""+themeId+"\",\n" +
				"\"theme_name\":\""+themeName+"\",\n" +
				"\"domain\":\""+domain+"\",\n" +
				"\"account_id\":\""+accountId+"\",\n" +
				"\"application_id\":\""+applicationId+"\",\n" +
				"\"main_color\":\""+mainColor+"\",\n" +
				"\"short_company_url\":\""+shortCompanyUrl+"\",\n" +
				"\"brand_text\":\""+brandText+"\",\n" +
				"\"brand_image_colored\":\""+brandImageColored+"\",\n" +
				"\"brand_image_white\":\""+brandImageWhite+"\",\n" +
				"\"branded_favicon\":\""+brandedFavicon+"\",\n" +
				"\"brand_image_colored_url\":\""+brandImageColoredUrl+"\",\n" +
				"\"brand_image_white_url\":\""+brandImageWhiteUrl+"\",\n" +
				"\"branded_favicon_url\":\""+brandedFaviconUrl+"\"\n" +
		"}");
		
		assertEquals(themeId, response.getThemeId());
		assertEquals(themeName, response.getThemeName());
		assertEquals(domain, response.getDomain());
		assertEquals(accountId, response.getAccountId());
		assertEquals(applicationId, response.getApplicationId());
		assertEquals(mainColor, response.getMainColor());
		assertEquals(shortCompanyUrl, response.getShortCompanyUrl());
		assertEquals(brandText, response.getBrandText());
		assertEquals(brandImageColored, response.getBrandImageColored());
		assertEquals(brandImageWhite, response.getBrandImageWhite());
		assertEquals(brandedFavicon, response.getBrandedFavicon());
		assertEquals(brandImageColoredUrl, response.getBrandImageColoredUrl());
		assertEquals(brandImageWhiteUrl, response.getBrandImageWhiteUrl());
		assertEquals(brandedFaviconUrl, response.getBrandedFaviconUrl());
	}

	@Test
	public void testUpdateFromJson() {
		Theme theme = Theme.builder().brandText("Initial text").mainColor("#a1b2c3").build();
		assertEquals("Initial text", theme.getBrandText());
		assertNull(theme.getThemeName());
		assertNull(theme.getThemeId());

		theme.updateFromJson("{\"theme_id\":\""+UUID.randomUUID()+"\"," +
				"\"brand_text\":\"Nexmo\",\"theme_name\":\"test Theme\"}"
		);
		assertNotNull(theme.getThemeId());
		assertEquals("#a1b2c3", theme.getMainColor());
		assertEquals("Nexmo", theme.getBrandText());
		assertNotNull(theme.getThemeName());

		assertThrows(VonageResponseParseException.class, () -> theme.updateFromJson("{malformed]"));
	}
	
	@Test(expected = VonageUnexpectedException.class)
	public void testFromJsonInvalid() {
		Theme.fromJson("{malformed]");
	}

	@Test
	public void testFromJsonEmpty() {
		Theme response = Theme.fromJson("{}");
		assertNull(response.getThemeId());
		assertNull(response.getThemeName());
		assertNull(response.getDomain());
		assertNull(response.getAccountId());
		assertNull(response.getApplicationId());
		assertNull(response.getMainColor());
		assertNull(response.getShortCompanyUrl());
		assertNull(response.getBrandText());
		assertNull(response.getBrandImageColored());
		assertNull(response.getBrandImageWhite());
		assertNull(response.getBrandedFavicon());
		assertNull(response.getBrandImageColoredUrl());
		assertNull(response.getBrandImageWhiteUrl());
		assertNull(response.getBrandedFaviconUrl());
	}

	@Test
	public void testInvalidDomain() {
		Theme parsed = Theme.fromJson("{\"domain\":\"Nexmo Business Meetings\"}");
		assertNull(parsed.getDomain());
	}

	@Test
	public void testSerializeEmpty() {
		String json = Theme.builder().build().toJson();
		assertEquals("{}", json);
	}

	@Test
	public void testBrandTextValidation() {
		String padding = IntStream.range(1, 200)
				.mapToObj(i -> Character.toString((char) (((char) i) % 100)))
				.collect(Collectors.joining());

		assertEquals(199, Theme.builder().brandText(padding).build().getBrandText().length());
		assertEquals(200, Theme.builder().brandText("0"+padding).build().getBrandText().length());
		assertThrows(IllegalArgumentException.class, () -> Theme.builder().brandText("ab"+padding).build());
		assertThrows(IllegalArgumentException.class, () -> Theme.builder().brandText(" ").build());
	}

	@Test
	public void testThemeNameValidation() {
		String padding = IntStream.range(1, 200)
				.mapToObj(i -> Character.toString((char) (((char) i) % 126)))
				.collect(Collectors.joining());

		assertEquals(199, Theme.builder().themeName(padding).build().getThemeName().length());
		assertEquals(200, Theme.builder().themeName("0"+padding).build().getThemeName().length());
		assertThrows(IllegalArgumentException.class, () -> Theme.builder().themeName("ab"+padding).build());
		assertThrows(IllegalArgumentException.class, () -> Theme.builder().themeName(" ").build());
	}

	@Test
	public void testShortCompanyUrlValidation() {
		String padding = IntStream.range(1, 128)
				.mapToObj(i -> Character.toString((char) (((char) i) % 126)))
				.collect(Collectors.joining());

		assertEquals(127, Theme.builder().shortCompanyUrl(padding).build().getShortCompanyUrl().length());
		assertEquals(128, Theme.builder().shortCompanyUrl("0"+padding).build().getShortCompanyUrl().length());
		assertThrows(IllegalArgumentException.class, () -> Theme.builder().shortCompanyUrl("ab"+padding).build());
		assertThrows(IllegalArgumentException.class, () -> Theme.builder().shortCompanyUrl(" ").build());
	}

	@Test
	public void testMainColorValidation() {
		assertThrows(IllegalArgumentException.class, () -> Theme.builder().mainColor("       ").build());
		assertThrows(IllegalArgumentException.class, () -> Theme.builder().mainColor("").build());
		assertThrows(IllegalArgumentException.class, () -> Theme.builder().mainColor("#######").build());
		assertThrows(IllegalArgumentException.class, () -> Theme.builder().mainColor("#gggggg").build());
	}

	@Test(expected = VonageUnexpectedException.class)
	public void triggerJsonProcessingException() {
		class SelfRefrencing extends Theme {
			@JsonProperty("self") final SelfRefrencing self = this;
		}
		new SelfRefrencing().toJson();
	}
}