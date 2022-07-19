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
import com.vonage.client.messages.MessageType;

import java.util.List;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
public final class WhatsappTemplateRequest extends WhatsappRequest {
	Template template;
	Whatsapp whatsapp;

	WhatsappTemplateRequest(Builder builder) {
		super(builder, MessageType.TEMPLATE);
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
		List<?> parameters;
		Policy policy = Policy.DETERMINISTIC;
		String locale = "en_GB";

		Builder() {}

		/**
		 * (REQUIRED)
		 * The name of the template. For WhatsApp use your WhatsApp namespace (available via Facebook Business Manager),
		 * followed by a colon : and the name of the template to use.
		 *
		 * @param name The template name.
		 * @return This builder.
		 */
		public Builder name(String name) {
			this.name = name;
			return this;
		}

		/**
		 * (OPTIONAL)
		 * The parameters are an array of objects, with the first object being used for {{1}} in the template,
		 * with the second being {{2}} etc. You can find the full list of supported parameters on WhatsApp's
		 * <a href=https://developers.facebook.com/docs/whatsapp/on-premises/reference/messages#message-templates>
		 * messages parameters documentation</a>.
		 *
		 * @param parameters The serializable list of objects.
		 * @return This builder.
		 */
		public Builder parameters(List<?> parameters) {
			this.parameters = parameters;
			return this;
		}

		/**
		 * (REQUIRED)
		 * Policy for resolving what language template to use.
		 * Defaults to {@link Policy#DETERMINISTIC} if not set.
		 *
		 * @param policy The policy field.
		 * @return This builder.
		 */
		public Builder policy(Policy policy) {
			this.policy = policy;
			return this;
		}

		/**
		 * (REQUIRED)
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
		public WhatsappTemplateRequest build() {
			return new WhatsappTemplateRequest(this);
		}
	}
}
