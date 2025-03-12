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
package com.vonage.client.conversations;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a {@linkplain EventType#MEMBER_MEDIA} event.
 *
 * @since 8.19.0
 */
public final class MemberMediaEvent extends AbstractChannelEvent<MemberMediaEvent.Body> {
	MemberMediaEvent() {}

	/**
	 * The main body container for a MemberMediaEvent.
	 */
	static class Body extends AbstractChannelEvent.Body {
		@JsonProperty("audio") Boolean audio;
		@JsonProperty("media") MemberMedia media;
	}

	/**
	 * The {@code audio} field.
	 *
	 * @return Whether the event is audio, or {@code null} if unknown.
	 */
	@JsonIgnore
	public Boolean getAudio() {
		return body != null ? body.audio : null;
	}

	/**
	 * The {@code media} field.
	 *
	 * @return The media object, or {@code null} if absent.
	 */
	@JsonIgnore
	public MemberMedia getMedia() {
		return body != null ? body.media : null;
	}
}