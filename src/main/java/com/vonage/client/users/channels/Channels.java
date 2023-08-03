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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Represents the "channels" field on {@link com.vonage.client.users.User}.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class Channels {
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

	private <C extends Channel> List<C> initChannelList(List<C> channelList) {
		return channelList != null ? channelList : new ArrayList<>(2);
	}

	/**
	 * Creates a new Channels object and assigns the specified channels to the
	 * appropriate fields based on their type.
	 *
	 * @param channels The contact methods to associate with this user.
	 */
	public Channels(Collection<? extends Channel> channels) {
		if (channels == null || channels.isEmpty()) return;
		for (Channel channel : channels) {
			if (channel instanceof Pstn) {
				initChannelList(pstn).add((Pstn) channel);
			}
			else if (channel instanceof Sip) {
				initChannelList(sip).add((Sip) channel);
			}
			else if (channel instanceof Vbc) {
				initChannelList(vbc).add((Vbc) channel);
			}
			else if (channel instanceof Websocket) {
				initChannelList(websocket).add((Websocket) channel);
			}
			else if (channel instanceof Sms) {
				initChannelList(sms).add((Sms) channel);
			}
			else if (channel instanceof Mms) {
				initChannelList(mms).add((Mms) channel);
			}
			else if (channel instanceof Whatsapp) {
				initChannelList(whatsapp).add((Whatsapp) channel);
			}
			else if (channel instanceof Viber) {
				initChannelList(viber).add((Viber) channel);
			}
			else if (channel instanceof Messenger) {
				initChannelList(messenger).add((Messenger) channel);
			}
		}
	}

	/**
	 * PSTN channels.
	 *
	 * @return The list of PSTN channels, or {@code null} if not set.
	 */
	public List<Pstn> getPstn() {
		return pstn;
	}

	/**
	 * SIP channels.
	 *
	 * @return The list of SIP channels, or {@code null} if not set.
	 */
	public List<Sip> getSip() {
		return sip;
	}

	/**
	 * VBC channels.
	 *
	 * @return The list of VBC channels, or {@code null} if not set.
	 */
	public List<Vbc> getVbc() {
		return vbc;
	}

	/**
	 * Websocket channels.
	 *
	 * @return The list of Websocket channels, or {@code null} if not set.
	 */
	public List<Websocket> getWebsocket() {
		return websocket;
	}

	/**
	 * SMS channels.
	 *
	 * @return The list of SMS channels, or {@code null} if not set.
	 */
	public List<Sms> getSms() {
		return sms;
	}

	/**
	 * MMS channels.
	 *
	 * @return The list of MMS channels, or {@code null} if not set.
	 */
	public List<Mms> getMms() {
		return mms;
	}

	/**
	 * WhatsApp channels.
	 *
	 * @return The list of WhatsApp channels, or {@code null} if not set.
	 */
	public List<Whatsapp> getWhatsapp() {
		return whatsapp;
	}

	/**
	 * Viber channels.
	 *
	 * @return The list of Viber channels, or {@code null} if not set.
	 */
	public List<Viber> getViber() {
		return viber;
	}

	/**
	 * Messenger channels.
	 *
	 * @return The list of Messenger channels, or {@code null} if not set.
	 */
	public List<Messenger> getMessenger() {
		return messenger;
	}
}
