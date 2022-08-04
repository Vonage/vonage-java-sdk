/*
 *   Copyright 2022 Vonage
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
package com.vonage.client.voice.ncco;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.vonage.client.voice.TextToSpeechLanguage;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Currency;

/**
 * An NCCO pay action which securely collects credit card information with DTMF input. Refer to
 * <a href=https://developer.vonage.com/voice/voice-api/ncco-reference#pay>the documentation</a> for details.
 *
 * @since 7.0.0
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PayAction implements Action {
	private static final String ACTION = "pay";

	private Collection<URI> eventUrl;
	private String currency;
	private Double amount;
	private Voice voice;
	private Collection<PaymentPrompt> prompts;

	PayAction() {}

	private PayAction(Builder builder) {
		if ((amount = builder.amount) == null) {
			throw new IllegalStateException("Payment amount is mandatory.");
		}
		if (builder.currency != null) {
			currency = builder.currency.getCurrencyCode().toLowerCase();
		}
		if (builder.language != null || builder.style != null) {
			voice = new Voice(builder.language, builder.style);
		}
		eventUrl = builder.eventUrl;
		prompts = builder.prompts;
	}

	@Override
	public String getAction() {
		return ACTION;
	}

	public Collection<URI> getEventUrl() {
		return eventUrl;
	}

	public String getCurrency() {
		return currency;
	}

	public double getAmount() {
		return amount;
	}

	public Voice getVoice() {
		return voice;
	}

	public Collection<PaymentPrompt> getPrompts() {
		return prompts;
	}

	/**
	 * Entry point for constructing a {@linkplain PayAction}.
	 *
	 * @return A new {@linkplain Builder} instance.
	 */
	public static Builder builder() {
		return new Builder();
	}

	@JsonInclude(value = JsonInclude.Include.NON_NULL)
	public static class Voice {
		private final TextToSpeechLanguage language;
		private final Integer style;

		public Voice(TextToSpeechLanguage language, Integer style) {
			this.language = language;
			this.style = style;
		}

		public TextToSpeechLanguage getLanguage() {
			return language;
		}

		public Integer getStyle() {
			return style;
		}
	}

	public static class Builder {
		private Collection<URI> eventUrl;
		private Collection<PaymentPrompt> prompts;
		private Currency currency;
		private Double amount;
		private TextToSpeechLanguage language;
		private Integer style;

		Builder() {}

		/**
		 * (OPTIONAL)
		 *
		 * @param eventUrl The URL to the webhook endpoint that is called asynchronously when payment is finished.
		 * @return This builder.
		 */
		public Builder eventUrl(URI eventUrl) {
			if (this.eventUrl == null) {
				this.eventUrl = new ArrayList<>(1);
			}
			this.eventUrl.add(eventUrl);
			return this;
		}

		/**
		 * (OPTIONAL)
		 *
		 * @param eventUrl The URL to the webhook endpoint that is called asynchronously when payment is finished.
		 * @return This builder.
		 */
		public Builder eventUrl(String eventUrl) {
			return eventUrl(URI.create(eventUrl));
		}

		/**
		 * (OPTIONAL)
		 *
		 * @param currency The currency. Default is <code>USD</code>, see the list of supported codes in
		 * <a href=https://developer.vonage.com/voice/voice-api/guides/payments#currency>the guide</a>.
		 *
		 * @return This builder.
		 */
		public Builder currency(Currency currency) {
			this.currency = currency;
			return this;
		}

		/**
		 * (OPTIONAL)
		 *
		 * @param currencyCode Currency code. Default value is <code>usd</code>, see the list of supported codes in
		 * <a href=https://developer.vonage.com/voice/voice-api/guides/payments#currency>the guide</a>.
		 *
		 * @return This builder.
		 */
		public Builder currency(String currencyCode) {
			return currency(Currency.getInstance(currencyCode.toUpperCase()));
		}

		/**
		 * (REQUIRED)
		 *
		 * @param amount Charge amount, must be positive.
		 * @return This builder.
		 */
		public Builder amount(double amount) {
			this.amount = amount;
			return this;
		}

		/**
		 * (OPTIONAL)
		 *
		 * @param language The language (BCP-47 format) for the prompts. Default is <code>en-US</code>.
		 * @return This builder.
		 */
		public Builder language(TextToSpeechLanguage language) {
			this.language = language;
			return this;
		}

		/**
		 * (OPTIONAL)
		 *
		 * @param style The vocal style (vocal range, tessitura and timbre). Default is <code>0</code>.
		 * Possible values are listed in the
		 * <a href=https://developer.vonage.com/voice/voice-api/guides/text-to-speech#supported-languages>
		 * Text-To-Speech guide</a>.
		 *
		 * @return This builder.
		 */
		public Builder style(int style) {
			this.style = style;
			return this;
		}

		/**
		 * (OPTIONAL)
		 *
		 * @param prompts <a href=https://developer.vonage.com/voice/voice-api/ncco-reference#prompts-text-settings>
		 * Prompt settings</a>.
		 *
		 * @return This builder.
		 */
		public Builder prompts(Collection<PaymentPrompt> prompts) {
			this.prompts = prompts;
			return this;
		}

		/**
		 * Builds the {@link PayAction} object with this builder's fields.
		 * @return A new {@link PayAction} instance.
		 */
		public PayAction build() {
			return new PayAction(this);
		}
	}
}
