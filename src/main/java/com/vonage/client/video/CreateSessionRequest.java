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
package com.vonage.client.video;

import com.vonage.client.AbstractQueryParamsRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

/**
 * Defines the properties used to create a new video session.
 */
public class CreateSessionRequest extends AbstractQueryParamsRequest {
	private final InetAddress location;
	private final MediaMode mediaMode;
	private final ArchiveMode archiveMode;
	private final Boolean e2ee;

	private CreateSessionRequest(Builder builder) {
		location = builder.location;
		archiveMode = builder.archiveMode;
		mediaMode = builder.mediaMode;
		e2ee = builder.e2ee;
		if (archiveMode == ArchiveMode.ALWAYS && mediaMode != MediaMode.ROUTED) {
		    throw new IllegalStateException("A session with automatic archiving must also have the ROUTED media mode.");
		}
	}

	@Override
	public Map<String, String> makeParams() {
		Map<String, String> params = super.makeParams();
		if (location != null) {
			params.put("location", location.getHostAddress());
		}
		conditionalAdd("p2p.preference", mediaMode);
		conditionalAdd("archiveMode", archiveMode);
		conditionalAdd("e2ee", e2ee);
		return params;
	}

	/**
	 * The location hint IP address.
	 *
	 * @return The IP address, or {@code null} if unspecified.
	 */
	public InetAddress getLocation() {
		return location;
	}

	/**
	 * Defines whether the session will transmit streams using the Vonage Media Server or attempt
	 * to transmit streams directly between clients.
	 *
	 * @return The MediaMode as an enum, or {@code null} if unspecified.
	 */
	public MediaMode getMediaMode() {
		return mediaMode;
	}

	/**
	 * Defines whether the session will be automatically archived ({@code ArchiveMode.ALWAYS})
	 * or not ({@code ArchiveMode.MANUAL}).
	 *
	 * @return The ArchiveMode as an enum, or {@code null} if unspecified.
	 */
	public ArchiveMode getArchiveMode() {
		return archiveMode;
	}

	/**
	 * Defines whether the session will be end-to-end encrypted.
	 *
	 * @return {@code true} if end-to-end encryption is enabled, {@code null} if unspecified.
	 * @since 8.12.0
	 */
	public Boolean getE2ee() {
		return e2ee;
	}

	/**
	 * Instantiates a Builder, used to construct this object.
	 *
	 * @return A new {@linkplain Builder}.
	 */
	public static Builder builder() {
		return new Builder();
	}

	/**
	 * Builder for constructing {@linkplain CreateSessionRequest}.
	 */
	public static final class Builder {
		private InetAddress location;
		private MediaMode mediaMode;
		private ArchiveMode archiveMode;
		private Boolean e2ee;

		Builder() {}

		/**
		 * Call this method to set an IP address that the Vonage servers will use to
		 * situate the session in its global network. If you do not set a location hint,
		 * the Vonage servers will be based on the first client connecting to the session.
		 *
		 * @param location The IP address to serve as the location hint.
		 *
		 * @return This builder.
		 */
		public Builder location(InetAddress location) {
			this.location = location;
			return this;
		}

		/**
		 * Call this method to set an IP address that the Vonage servers will use to
		 * situate the session in its global network. If you do not set a location hint,
		 * the Vonage servers will be based on the first client connecting to the session.
		 *
		 * @param location The IP address to serve as the location hint.
		 *
		 * @return This builder.
		 */
		public Builder location(String location) {
			try {
				return location(InetAddress.getByName(location));
			}
			catch (UnknownHostException ex) {
				throw new IllegalArgumentException(ex);
			}
		}

		/**
		 * Call this method to determine whether the session will transmit streams using the
		 * Vonage Media Router ({@code MediaMode.ROUTED}) or not
		 * ({@code MediaMode.RELAYED}). By default, the {@code mediaMode} property
		 * is set to {@code MediaMode.RELAYED}.
		 *
		 * <p>
		 * With the {@code mediaMode} property set to {@code MediaMode.RELAYED}, the session
		 * will attempt to transmit streams directly between clients. If clients cannot connect due to
		 * firewall restrictions, the session uses the TURN server to relay audio-video streams.
		 *
		 * <p>
		 * The
		 * <a href="https://tokbox.com/developer/guides/create-session/#media-mode" target="_top">
		 * Media Router</a> provides the following benefits:
		 *
		 * <ul>
		 *   <li>The Media Router can decrease bandwidth usage in multiparty sessions.
		 *       (When the {@code mediaMode} property is set to {@code MediaMode.RELAYED},
		 *       each client must send a separate audio-video stream to each client subscribing to
		 *       it.)</li>
		 *   <li>The Media Router can improve the quality of the user experience through
		 *     <a href="https://tokbox.com/platform/fallback" target="_top">audio fallback and video
		 *     recovery</a>. With these features, if a client's connectivity degrades to a degree that
		 *     it does not support video for a stream it's subscribing to, the video is dropped on
		 *     that client (without affecting other clients), and the client receives audio only.
		 *     If the client's connectivity improves, the video returns.</li>
		 *   <li>The Media Router supports the
		 *     <a href="http://tokbox.com/developer/guides/archiving" target="_top">archiving</a>
		 *     feature, which lets you record, save, and retrieve video sessions.</li>
		 * </ul>
		 *
		 * @param mediaMode Set to a value defined in the {@link MediaMode} enum.
		 *
		 * @return This builder.
		 */
		public Builder mediaMode(MediaMode mediaMode) {
			this.mediaMode = mediaMode;
			return this;
		}

		/**
		 * Call this method to determine whether the session will be automatically archived ({@code ArchiveMode.ALWAYS})
		 * or not ({@code ArchiveMode.MANUAL}).
		 * Using an always archived session also requires the routed media mode ({@code MediaMode.ROUTED}).
		 *
		 * @param archiveMode The Archive mode.
		 *
		 * @return This builder.
		 */
		public Builder archiveMode(ArchiveMode archiveMode) {
			this.archiveMode = archiveMode;
			return this;
		}

		/**
		 * Call this method to enable end-to-end encryption for the session.
		 *
		 * @param e2ee Set to {@code true} to enable end-to-end encryption.
		 *
		 * @return This builder.
		 * @since 8.12.0
		 */
		public Builder e2ee(boolean e2ee) {
			this.e2ee = e2ee;
			return this;
		}

		/**
		 * Builds the CreateSessionRequest object.
		 *
		 * @return A new {@linkplain CreateSessionRequest} with this builder's properties.
		 */
		public CreateSessionRequest build() {
			return new CreateSessionRequest(this);
		}
	}
}
