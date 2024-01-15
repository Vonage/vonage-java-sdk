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

import com.vonage.client.TestUtils;
import com.vonage.client.VonageResponseParseException;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import java.net.URI;
import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class BroadcastTest {
	
	@Test
	public void testSerializeAndParseAllFields() {
		UUID id = UUID.randomUUID();
		UUID applicationId = UUID.randomUUID();
		String sessionId = "2_MX4xMDBfjE0Mzc2NzY1NDgwMTJ-TjMzfn4";
		String multiBroadcastTag = "Tag for multiple broadcasts";
		Long createdAt = Instant.now().getEpochSecond();
		Long updatedAt = Instant.now().plusSeconds(1).getEpochSecond();
		Integer maxDuration = 14500;
		Integer maxBitrate = 300000;
		Boolean hasAudio = true;
		Boolean hasVideo = true;
		Resolution resolution = Resolution.SD_LANDSCAPE;
		StreamMode streamMode = StreamMode.AUTO;
		BroadcastStatus status = BroadcastStatus.STOPPED;
		String stylesheet = "stream.instructor {position: absolute; width: 100%;  height:50%;}";
		StreamCompositionLayout layout = StreamCompositionLayout.builder(ScreenLayoutType.CUSTOM)
				.stylesheet(stylesheet).build();
		Hls hls = Hls.builder().dvr(false).lowLatency(false).build();
		URI hlsUrl = URI.create("https://example.com/path/to/playlist.m3u8");
		Rtmp rtmp1 = Rtmp.builder().serverUrl("rtmps://myfooserver/myfooapp")
				.streamName("mybarstream").id("bar").build();
		RtmpStatus rtmp1Status = RtmpStatus.CONNECTING;

		Broadcast request = Broadcast.builder(sessionId)
				.resolution(resolution).layout(layout).streamMode(streamMode)
				.maxBitrate(maxBitrate).hls(hls)
				.maxDuration(Duration.ofSeconds(maxDuration))
				.multiBroadcastTag(multiBroadcastTag)
				.addRtmpStream(rtmp1).build();

		String expectedRequestedJson = "{\"sessionId\":\""+sessionId+"\",\"streamMode\":\""+streamMode +
				"\",\"resolution\":\""+resolution+"\",\"layout\":{\"type\":\"custom\",\"stylesheet\":\"" +
				stylesheet+"\"},\"multiBroadcastTag\":\""+multiBroadcastTag + "\",\"maxDuration\":" +
				maxDuration+",\"maxBitrate\":"+maxBitrate+",\"outputs\":{" + "\"rtmp\":[{\"id\":\"" +
				rtmp1.getId()+"\",\"streamName\":\""+rtmp1.getStreamName()+"\",\"serverUrl\":\"" +
				rtmp1.getServerUrl()+"\"}],\"hls\":{\"dvr\":"+hls.dvr()+",\"lowLatency\":" +
				hls.lowLatency()+"}}}", requestJson = request.toJson();

		assertEquals(expectedRequestedJson, requestJson);

		String responseJson = "{\"id\":\""+id+"\"," +
				"\"sessionId\":\""+sessionId+"\"," +
				"\"multiBroadcastTag\":\""+multiBroadcastTag+"\"," +
				"\"applicationId\":\""+applicationId+"\"," +
				"\"createdAt\":\""+createdAt+"\"," +
				"\"updatedAt\":\""+updatedAt+"\"," +
				"\"maxDuration\":\""+maxDuration+"\"," +
				"\"maxBitrate\":\""+maxBitrate+"\"," +
				"\"status\":\""+status+"\"," +
				"\"hasAudio\":"+hasAudio+",\"hasVideo\":"+hasVideo +
				",\"streams\":[{}]," +
				"\"resolution\":\""+resolution+"\"," +
				"\"streamMode\":\""+streamMode+"\"," +
				"\"settings\":{\"hls\":{\"lowLatency\":"+hls.lowLatency()+",\"dvr\":"+hls.dvr()+"}}," +
				"\"broadcastUrls\":{\"hls\":\""+hlsUrl+"\",\"rtmp\":[{\"id\":\""+rtmp1.getId() +
				"\",\"serverUrl\":\""+rtmp1.getServerUrl()+"\",\"streamName\":\""+rtmp1.getStreamName() +
				"\",\"status\":\""+rtmp1Status+"\"}]}}";

		Broadcast response = Broadcast.fromJson(responseJson);

		assertEquals(id, response.getId());
		assertEquals(sessionId, response.getSessionId());
		assertEquals(multiBroadcastTag, response.getMultiBroadcastTag());
		assertEquals(applicationId, response.getApplicationId());
		assertEquals(createdAt, response.getCreatedAtMillis());
		assertEquals(updatedAt, response.getUpdatedAtMillis());
		assertEquals(maxDuration, response.getMaxDurationSeconds());
		assertEquals(maxBitrate, response.getMaxBitrate());
		assertEquals(hasAudio, response.hasAudio());
		assertEquals(hasVideo, response.hasVideo());
		assertEquals(resolution, response.getResolution());
		assertEquals(streamMode, response.getStreamMode());
		assertEquals(status, response.getStatus());
		BroadcastUrls responseBroadcastUrls = response.getBroadcastUrls();
		assertNotNull(responseBroadcastUrls);
		assertEquals(hlsUrl, responseBroadcastUrls.getHls());
		List<Rtmp> responseRtmps = responseBroadcastUrls.getRtmps();
		assertNotNull(responseRtmps);
		assertEquals(1, responseRtmps.size());
		Rtmp responseRtmp1 = responseRtmps.get(0);
		assertNotNull(responseRtmp1);
		assertEquals(rtmp1.getServerUrl(), responseRtmp1.getServerUrl());
		assertEquals(rtmp1.getStreamName(), responseRtmp1.getStreamName());
		assertEquals(rtmp1Status, responseRtmp1.getStatus());
		assertNull(response.getLayout());
		assertNotNull(response.getStreams());
		assertEquals(1, response.getStreams().size());
		VideoStream stream1 = response.getStreams().get(0);
		assertNotNull(stream1);
		assertNull(stream1.getStreamId());
		assertNull(stream1.hasAudio());
		assertNull(stream1.hasVideo());

		Broadcast parsedFromRequestJson = Broadcast.fromJson(requestJson);
		assertNotNull(parsedFromRequestJson);
		assertEquals(request.toString(), parsedFromRequestJson.toString());

		String expectedJsonFromRequest = "{" +
				"\"sessionId\":\"2_MX4xMDBfjE0Mzc2NzY1NDgwMTJ-TjMzfn4\"," +
				"\"streamMode\":\"auto\"," +
				"\"resolution\":\"640x480\"," +
				"\"layout\":{" +
					"\"type\":\"custom\"," +
					"\"stylesheet\":\"stream.instructor {position: absolute; width: 100%;  height:50%;}\"" +
				"},\"multiBroadcastTag\":\"Tag for multiple broadcasts\"," +
				"\"maxDuration\":14500," +
				"\"maxBitrate\":300000," +
				"\"outputs\":{" +
					"\"rtmp\":[{" +
						"\"id\":\"bar\"," +
						"\"streamName\":\"mybarstream\"," +
						"\"serverUrl\":\"rtmps://myfooserver/myfooapp\"" +
				"}],\"hls\":{" +
					"\"dvr\":false," +
					"\"lowLatency\":false" +
				"}}}",
				jsonFromRequest = parsedFromRequestJson.toJson();
		assertEquals(expectedJsonFromRequest, jsonFromRequest);

		Broadcast requestFromResponseJson = Broadcast.fromJson(responseJson);
		assertNotNull(requestFromResponseJson);
		String expectedRequestFromResponseJsonToJson = "{" +
				"\"id\":\""+id+"\"," +
				"\"applicationId\":\""+applicationId+"\"," +
				"\"sessionId\":\""+sessionId+"\"," +
				"\"streamMode\":\"auto\"," +
				"\"resolution\":\""+resolution+"\"," +
				"\"hasVideo\":"+hasVideo+"," +
				"\"hasAudio\":"+hasAudio+"," +
				"\"createdAt\":"+createdAt+"," +
				"\"streams\":[{}]," +
				"\"multiBroadcastTag\":\""+multiBroadcastTag+"\"," +
				"\"updatedAt\":"+updatedAt+"," +
				"\"maxDuration\":"+maxDuration+"," +
				"\"maxBitrate\":"+maxBitrate+"," +
				"\"status\":\""+status+"\"," +
				"\"broadcastUrls\":{\"hls\":\""+hlsUrl+"\"," +
				"\"rtmp\":[{\"id\":\""+rtmp1.getId()+"\",\"streamName\":\""+rtmp1.getStreamName()+"\"," +
				"\"serverUrl\":\""+rtmp1.getServerUrl()+"\",\"status\":\""+rtmp1Status+"\"}]}," +
				"\"settings\":{\"hls\":{\"dvr\":"+hls.dvr()+",\"lowLatency\":"+hls.lowLatency()+"}}}";
		String requestFromResponseJsonJson = requestFromResponseJson.toJson();
		assertEquals(expectedRequestFromResponseJsonToJson, requestFromResponseJsonJson);
	}

	@Test
	public void testSerializeMultipleRtmps() {
		Rtmp rtmp1 = Rtmp.builder().serverUrl("http://server/file").streamName("test1").build(),
				rtmp2 = Rtmp.builder().serverUrl("ftp://server2/another.ext").streamName("R2").build();
		Broadcast request = Broadcast.builder("SESSION_ID-321")
				.rtmpStreams(Collections.singleton(rtmp1)).addRtmpStream(rtmp2).build();

		String expectedJson = "{" +
				"\"sessionId\":\"SESSION_ID-321\"," +
				"\"outputs\":{\"rtmp\":[" +
				"{\"streamName\":\"test1\",\"serverUrl\":\"http://server/file\"}," +
				"{\"streamName\":\"R2\",\"serverUrl\":\"ftp://server2/another.ext\"}" +
				"]}}", actualJson = request.toJson();

		assertEquals(expectedJson, actualJson);
		assertNull(request.getSettings());
		assertNull(request.getId());
		assertNull(request.getMultiBroadcastTag());
		assertNull(request.getApplicationId());
		assertNull(request.getCreatedAt());
		assertNull(request.getUpdatedAt());
		assertNull(request.getMaxDuration());
		assertNull(request.getMaxBitrate());
		assertNull(request.hasAudio());
		assertNull(request.hasVideo());
		assertNull(request.getStatus());
		assertNull(request.getBroadcastUrls());
		assertNull(request.getHlsSettings());
		assertNull(request.getResolution());
		assertNull(request.getLayout());
		assertNull(request.getStreamMode());
	}

	@Test
	public void testConstructNoSessionId() {
		assertThrows(NullPointerException.class, () ->
				Broadcast.builder(null).hls(Hls.builder().lowLatency(false).dvr(false).build()).build()
		);
	}

	@Test
	public void testConstructNoRtmpsOrHls() {
		final String sessionId = "Sesh-1D";
		assertThrows(IllegalStateException.class, () ->
				Broadcast.builder(sessionId).build()
		);
		assertThrows(IllegalStateException.class, () ->
				Broadcast.builder(sessionId).rtmpStreams(Collections.emptyList()).build()
		);
		assertThrows(NullPointerException.class, () ->
				Broadcast.builder(sessionId).rtmpStreams(null).build()
		);
		assertThrows(NullPointerException.class, () ->
				Broadcast.builder(sessionId).rtmpStreams(Collections.singletonList(null)).build()
		);
		assertThrows(NullPointerException.class, () ->
				Broadcast.builder(sessionId).addRtmpStream(null).build()
		);
		assertThrows(IllegalStateException.class, () ->
				Broadcast.builder(sessionId).rtmpStreams(Collections.emptyList()).hls(null).build()
		);
	}

	@Test
	public void testRtmpConstructorValidation() {
		String serverUrl = "https://vonage.com", streamName = "Test Stream", id = "test_ID";
		assertThrows(IllegalArgumentException.class, () ->
				Rtmp.builder().id(id).build()
		);
		assertThrows(IllegalArgumentException.class, () ->
				Rtmp.builder().serverUrl(serverUrl).build()
		);
		assertThrows(IllegalArgumentException.class, () ->
				Rtmp.builder().serverUrl(serverUrl).streamName(streamName).id(" ").build()
		);
		assertThrows(NullPointerException.class, () ->
				Rtmp.builder().streamName(streamName).build()
		);
		assertThrows(IllegalArgumentException.class, () ->
				Rtmp.builder().streamName(streamName).serverUrl("%$").build()
		);
		assertThrows(IllegalArgumentException.class, () ->
				Rtmp.builder().serverUrl(serverUrl).streamName("  ").build()
		);

		Rtmp valid = Rtmp.builder().serverUrl(serverUrl).streamName(streamName).build();
		Broadcast request = Broadcast.builder("sessionID").rtmpStreams(Collections.singleton(valid)).build();
		String expectedJson = "{\"sessionId\":\"sessionID\",\"outputs\":{\"rtmp\":[{\"streamName\":" +
				"\"Test Stream\",\"serverUrl\":\"https://vonage.com\"}]}}",
				actualJson = request.toJson();
		assertEquals(expectedJson, actualJson);
	}

	@Test
	public void testHlsConstructorValidation() {
		assertThrows(IllegalArgumentException.class, () -> Hls.builder().dvr(true).lowLatency(true).build());
	}

	@Test
	public void testHlsSerialization() {
		String sessionId = "Test_seshID";

		Hls hls = Hls.builder().build();
		Broadcast request = Broadcast.builder(sessionId).hls(hls).build();
		String expectedJson = "{\"sessionId\":\"Test_seshID\",\"outputs\":{\"hls\":{}}}",
				actualJson = request.toJson();
		assertEquals(expectedJson, actualJson);

		hls = Hls.builder().dvr(true).build();
		request = Broadcast.builder(sessionId).hls(hls).build();
		expectedJson = "{\"sessionId\":\"Test_seshID\",\"outputs\":{\"hls\":{\"dvr\":true}}}";
		actualJson = request.toJson();
		assertEquals(expectedJson, actualJson);

		hls = Hls.builder().lowLatency(true).build();
		request = Broadcast.builder(sessionId).hls(hls).build();
		expectedJson = "{\"sessionId\":\"Test_seshID\",\"outputs\":{\"hls\":{\"lowLatency\":true}}}";
		actualJson = request.toJson();
		assertEquals(expectedJson, actualJson);

		hls = Hls.builder().lowLatency(false).dvr(false).build();
		request = Broadcast.builder(sessionId).hls(hls).build();
		expectedJson = "{\"sessionId\":\"Test_seshID\",\"outputs\":{\"hls\":{\"dvr\":false,\"lowLatency\":false}}}";
		actualJson = request.toJson();
		assertEquals(expectedJson, actualJson);
	}

	@Test
	public void testMaxDurationBoundaries() {
		String sessionId = "duration_sID";
		Rtmp rtmp = Rtmp.builder().serverUrl("server").streamName("name").build();
		Broadcast.Builder builder = Broadcast.builder(sessionId).addRtmpStream(rtmp);
		int min = 60, max = 36000;
		assertEquals(min, builder.maxDuration(min).build().getMaxDurationSeconds().intValue());
		assertEquals(max, builder.maxDuration(max).build().getMaxDurationSeconds().intValue());
		assertThrows(IllegalArgumentException.class, () -> builder.maxDuration(min - 1).build());
		assertThrows(IllegalArgumentException.class, () -> builder.maxDuration(max + 1).build());
	}

	@Test
	public void testFromJsonEmpty() {
		Broadcast response = Broadcast.fromJson("{}");
		TestUtils.testJsonableBaseObject(response);
		assertNull(response.getId());
		assertNull(response.getSessionId());
		assertNull(response.getMultiBroadcastTag());
		assertNull(response.getApplicationId());
		assertNull(response.getCreatedAt());
		assertNull(response.getUpdatedAt());
		assertNull(response.getMaxDuration());
		assertNull(response.getMaxBitrate());
		assertNull(response.hasAudio());
		assertNull(response.hasVideo());
		assertNull(response.getStatus());
		assertNull(response.getBroadcastUrls());
		assertNull(response.getHlsSettings());
		assertNull(response.getResolution());
		assertNull(response.getLayout());
		assertNull(response.getStreamMode());
	}

	@Test
	public void testFromJsonInvalid() {
		assertThrows(VonageResponseParseException.class, () -> Broadcast.fromJson("{malformed]"));
	}

	@Test
	public void testBroadcastStatusUnknownValue() {
		assertNull(BroadcastStatus.fromString("f00"));
	}

	@Test
	public void testRtmpStatusUnknownValue() {
		assertNull(RtmpStatus.fromString("f00"));
	}
}