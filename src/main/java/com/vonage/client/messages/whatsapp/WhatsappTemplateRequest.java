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
package com.vonage.client.messages.whatsapp;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.messages.internal.MessageType;

import java.util.List;
import java.util.Map;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
public final class WhatsappTemplateRequest extends WhatsappRequest {
	Template template;
	Whatsapp whatsapp;

	WhatsappTemplateRequest(Builder builder) {
		super(builder);
		template = new Template(builder.name, builder.parameters);
		whatsapp = new Whatsapp(builder.policy, builder.locale);
	}

	@JsonProperty("template")
	public Template getTemplate() {
		return template;
	}

	@JsonProperty("whatsapp")
	public Whatsapp getWhatsapp() {
		return whatsapp;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static final class Builder extends WhatsappRequest.Builder<WhatsappTemplateRequest, Builder> {
		String name;
		List<Map<String, ?>> parameters;
		Policy policy = Policy.DETERMINISTIC;
		String locale;

		Builder() {}

		public Builder name(String name) {
			this.name = name;
			return this;
		}

		public Builder parameters(List<Map<String, ?>> parameters) {
			this.parameters = parameters;
			return this;
		}

		public Builder policy(Policy policy) {
			this.policy = policy;
			return this;
		}

		/**
		 * The BCP 47 language of the template.
		 * 
		 * @param locale The BCP-47 locale.
		 * @return This builder.
		 */
		public Builder locale(String locale) {
			this.locale = locale;
			return this;
		}

		@Override
		protected MessageType getMessageType() {
			return MessageType.TEMPLATE;
		}

		@Override
		public WhatsappTemplateRequest build() {
			return new WhatsappTemplateRequest(this);
		}
	}
}
