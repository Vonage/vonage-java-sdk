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
package com.vonage.client.messages;

import java.net.URI;

class Payload {
	protected URI url;
	protected String caption;

	public Payload(String url) {
		this.url = URI.create(url);
	}

	public Payload(String url, String caption) {
		this(url);
		this.caption = caption;
	}

	public String getUrl() {
		return url.toString();
	}

	public String getCaption() {
		return caption;
	}
}
