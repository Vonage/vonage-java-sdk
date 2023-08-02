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
package com.vonage.client.users.channels;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Represents the "channels" field on {@link com.vonage.client.users.User}.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Channels {
	@JsonProperty("pstn") private List<Pstn> pstn;
	@JsonProperty("sip") private List<Sip> sip;
	@JsonProperty("vbc") private List<Vbc> vbc;
	@JsonProperty("websocket") private List<Websocket> websocket;
	@JsonProperty("sms") private List<Sms> sms;
	@JsonProperty("mms") private List<Mms> mms;
	@JsonProperty("whatsapp") private List<Whatsapp> whatsapp;
	@JsonProperty("viber") private List<Viber> viber;
	@JsonProperty("messenger") private List<Messenger> messenger;

	Channels() {

	}

	public List<Pstn> getPstn() {
		return pstn;
	}

	public List<Sip> getSip() {
		return sip;
	}

	public List<Vbc> getVbc() {
		return vbc;
	}

	public List<Websocket> getWebsocket() {
		return websocket;
	}

	public List<Sms> getSms() {
		return sms;
	}

	public List<Mms> getMms() {
		return mms;
	}

	public List<Whatsapp> getWhatsapp() {
		return whatsapp;
	}

	public List<Viber> getViber() {
		return viber;
	}

	public List<Messenger> getMessenger() {
		return messenger;
	}
}
