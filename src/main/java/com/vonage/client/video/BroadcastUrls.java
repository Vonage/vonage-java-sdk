/*
 *   Copyright 2024 Vonage
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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.net.URI;
import java.util.List;

/**
 * Container for details about the HLS and RTMP broadcasts.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BroadcastUrls {
	@JsonProperty("hls") private URI hls;
	@JsonProperty("rtmp") private List<Rtmp> rtmp;

	/**
	 * If you specified an HLS endpoint, the object includes an HLS property, which is set to the URL for the
	 * HLS broadcast. Note this HLS broadcast URL points to an index file, an .M3U8-formatted playlist that contains
	 * a list of URLs to .ts media segment files (MPEG-2 transport stream files). While the URLs of both the playlist
	 * index file and media segment files are provided as soon as the HTTP response is returned, these URLs should not
	 * be accessed until 15-20 seconds later, after the initiation of the HLS broadcast, due to the delay between the
	 * HLS broadcast and the live streams in the Vonage video session. See <a
	 * href=https://developer.apple.com/library/ios/technotes/tn2288/_index.html>the Apple developer documentation</a>
	 * for more information about the playlist index file and media segment files for HLS.
	 *
	 * @return The HLS URL.
	 */
	public URI getHls() {
		return hls;
	}

	/**
	 * If you specified RTMP stream endpoints, this property will be non-null.
	 * This list includes information on each of the RTMP streams.
	 *
	 * @return Details of the RTMP streams.
	 */
	@JsonProperty("rtmp")
	public List<Rtmp> getRtmps() {
		return rtmp;
	}
}
