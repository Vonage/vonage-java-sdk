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
package com.vonage.client.users;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vonage.client.VonageResponseParseException;
import com.vonage.client.users.channels.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import java.net.URI;
import java.util.*;

public class UserTest {

	@Test
	public void testAllParamsEmptyChannels() throws Exception {
		String name = "Test_UserName",
				displayName = "Test DP",
				id = "USR-"+UUID.randomUUID(),
				imageUrl = "ftp:///path/to/photo.jpg";
		Map<String, Object> customData = new LinkedHashMap<>();
		customData.put("foo", "Bar");
		customData.put("Bar", 3);
		customData.put("baZ", false);
		customData.put("another_key", Arrays.asList("a value", 8.4, Collections.emptyMap()));

		User request = User.builder().channels()
				.name(name).displayName(displayName)
				.imageUrl(imageUrl).customData(customData).build();
		request.setId(id);

		String json = request.toJson();
		assertTrue(json.contains("\"id\":\""+id+"\""));
		assertTrue(json.contains("\"name\":\""+name+"\""));
		assertTrue(json.contains("\"display_name\":\""+displayName+"\""));
		assertTrue(json.contains("\"image_url\":\""+imageUrl+"\""));
		String customDataJson = new ObjectMapper().writeValueAsString(customData);
		assertTrue(json.contains("\"properties\":{\"custom_data\":"+customDataJson));
		assertTrue(json.contains("\"channels\":{}"));

		User parsed = User.fromJson(json);
		assertEquals(request, parsed);

		Set<User> userSet = new HashSet<>(2);
		assertTrue(userSet.add(parsed));
		assertFalse(userSet.add(request));
	}

	@Test
	public void testRequiredParams() {
		User user = User.builder().build();
		String json = user.toJson();
		assertEquals("{}", json);
		assertEquals(user, User.fromJson(json));
		assertNull(user.getChannels());
		assertNull(user.getImageUrl());
		assertNull(user.getCustomData());
		assertNull(user.getName());
		assertNull(user.getDisplayName());
		assertNull(user.getId());
	}

	@Test
	public void testEmptyChannels() {
		User emptyCollection = User.builder().channels(Collections.emptyList()).build();
		String expectedJson = "{\"channels\":{}}";
		assertEquals(expectedJson, emptyCollection.toJson());
		User emptyVarargs = User.builder().channels().build();
		assertEquals(emptyCollection, emptyVarargs);
		assertEquals(expectedJson, emptyVarargs.toJson());
		User nullChannels = User.builder().channels(null, null, null, null).build();
		assertEquals(emptyCollection, nullChannels);
		assertEquals(expectedJson, nullChannels.toJson());
	}

	@Test
	public void testAllChannelsOneOfEach() {
		User user = User.builder().channels(
				new Sms("+49 170 1234568"),
				new Mms("+49 171 1234567"),
				new Pstn("49 1522 000000"),
				new Whatsapp("+447700900001"),
				new Viber("447700900002"),
				new Messenger("0123456789abc"),
				new Vbc(7890),
				new Sip("sip:1234568790@sip.example.org", "admin", "53cRe7"),
				new Websocket(
						"wss://socket.example.org/connect",
						Websocket.ContentType.AUDIO_L16_8K,
						Collections.singletonMap("retry", true)
				)
		).build();

		Channels channels = user.getChannels();
		assertNotNull(channels);

		List<Sms> sms = channels.getSms();
		assertNotNull(sms);
		assertEquals(1, sms.size());
		Sms sms0 = sms.get(0);
		assertTrue(sms.hashCode() != 0);
		assertEquals("491701234568", sms0.getNumber());

		List<Mms> mms = channels.getMms();
		assertNotNull(mms);
		assertEquals(1, mms.size());
		Mms mms0 = mms.get(0);
		assertTrue(mms0.hashCode() != 0);
		assertEquals("491711234567", mms0.getNumber());

		List<Pstn> pstn = channels.getPstn();
		assertNotNull(pstn);
		assertEquals(1, pstn.size());
		Pstn pstn0 = pstn.get(0);
		assertTrue(pstn0.hashCode() != 0);
		assertEquals("491522000000", pstn0.getNumber());

		List<Whatsapp> whatsapp = channels.getWhatsapp();
		assertNotNull(whatsapp);
		assertEquals(1, whatsapp.size());
		Whatsapp whatsapp0 = whatsapp.get(0);
		assertTrue(whatsapp.hashCode() != 0);
		assertEquals("447700900001", whatsapp0.getNumber());

		List<Viber> viber = channels.getViber();
		assertNotNull(viber);
		assertEquals(1, viber.size());
		Viber viber0 = viber.get(0);
		assertTrue(viber0.hashCode() != 0);
		assertEquals("447700900002", viber0.getNumber());

		List<Messenger> messenger = channels.getMessenger();
		assertNotNull(messenger);
		assertEquals(1, messenger.size());
		Messenger messenger0 = messenger.get(0);
		assertTrue(messenger0.hashCode() != 0);
		assertEquals("0123456789abc", messenger0.getId());

		List<Vbc> vbc = channels.getVbc();
		assertNotNull(vbc);
		assertEquals(1, messenger.size());
		Vbc vbc0 = vbc.get(0);
		assertTrue(vbc0.hashCode() != 0);
		assertEquals("7890", vbc0.getExtension());

		List<Sip> sip = channels.getSip();
		assertNotNull(sip);
		assertEquals(1, messenger.size());
		Sip sip0 = sip.get(0);
		assertTrue(sip0.hashCode() != 0);
		assertEquals(URI.create("sip:1234568790@sip.example.org"), sip0.getUri());
		assertEquals("admin", sip0.getUsername());
		assertEquals("53cRe7", sip0.getPassword());

		List<Websocket> websocket = channels.getWebsocket();
		assertNotNull(websocket);
		assertEquals(1, websocket.size());
		Websocket websocket0 = websocket.get(0);
		assertTrue(websocket0.hashCode() != 0);
		assertEquals(URI.create("wss://socket.example.org/connect"), websocket0.getUri());
		assertEquals(Websocket.ContentType.AUDIO_L16_8K, websocket0.getContentType());
		assertEquals(1, websocket0.getHeaders().size());

		String expectedJson = "{\"channels\":{" +
				"\"pstn\":[" +
				"{\"number\":\""+pstn0.getNumber()+"\"}" +
				"],\"sip\":[" +
				"{\"uri\":\""+sip0.getUri()+"\",\"username\":\""+sip0.getUsername() +
				"\",\"password\":\""+sip0.getPassword()+"\"}" +
				"],\"vbc\":[" +
				"{\"extension\":\""+ vbc0.getExtension()+"\"}" +
				"],\"websocket\":[" +
				"{\"uri\":\"wss://socket.example.org/connect\",\"content-type\":\"audio/l16;rate=8000\"," +
				"\"headers\":{\"retry\":true}}" +
				"],\"sms\":[" +
				"{\"number\":\""+sms0.getNumber()+"\"}" +
				"],\"mms\":[" +
				"{\"number\":\""+mms0.getNumber()+"\"}" +
				"],\"whatsapp\":[" +
				"{\"number\":\""+whatsapp0.getNumber()+"\"}" +
				"],\"viber\":[" +
				"{\"number\":\""+viber0.getNumber()+"\"}" +
				"],\"messenger\":[{\"id\":\""+messenger0.getId()+"\"}" +
				"]}}";

		String actualJson = user.toJson();
		assertEquals(expectedJson, actualJson);
		User parsedUser = User.fromJson(actualJson);
		assertEquals(user, parsedUser);
		assertEquals(user.hashCode(), parsedUser.hashCode());
		assertEquals(channels.hashCode(), parsedUser.getChannels().hashCode());
	}

	@Test
	public void testAllChannelsEmpty() {
		String json = "{\n" +
				"  \"channels\": {\n" +
				"    \"pstn\": [],\n" +
				"    \"sip\": [],\n" +
				"    \"vbc\": [],\n" +
				"    \"websocket\": [],\n" +
				"    \"sms\": [],\n" +
				"    \"mms\": [],\n" +
				"    \"whatsapp\": [],\n" +
				"    \"viber\": [],\n" +
				"    \"messenger\": []\n" +
				"  }\n" +
				"}";
		User parsed = User.fromJson(json);
		Channels channels = parsed.getChannels();
		assertNotNull(channels);
		assertTrue(channels.getPstn().isEmpty());
		assertTrue(channels.getSip().isEmpty());
		assertTrue(channels.getVbc().isEmpty());
		assertTrue(channels.getWebsocket().isEmpty());
		assertTrue(channels.getSms().isEmpty());
		assertTrue(channels.getMms().isEmpty());
		assertTrue(channels.getWhatsapp().isEmpty());
		assertTrue(channels.getViber().isEmpty());
		assertTrue(channels.getMessenger().isEmpty());
	}

	@Test
	public void testSipInvalidUri() {
		String[] validUris = {"sips:1234@api.example.org;transport=tls", "sip:a.b@127.0.0.1"};
		for (final String validUri : validUris) {
			Sip sip = new Sip(validUri);
			assertEquals(URI.create(validUri), sip.getUri());
			assertNull(sip.getUsername());
			assertNull(sip.getPassword());
			assertThrows(IllegalArgumentException.class, () -> new Sip(validUri, null, "pas5WD"));
			assertEquals(sip, new Sip(validUri, null, null));
		}
		assertThrows(IllegalArgumentException.class, () -> new Sip("http://sip.example.com/endpoint"));
		assertThrows(NullPointerException.class, () -> new Sip(null));
	}

	@Test
	public void testWebsocketInvalidUri() {
		String[] validUris = {"ws://domain.tld/path/to/sock", "wss://example.com"};
		for (final String validUri : validUris) {
			Websocket websocket = new Websocket(validUri);
			assertEquals(URI.create(validUri), websocket.getUri());
			assertNull(websocket.getHeaders());
			assertNull(websocket.getContentType());
			assertEquals(websocket, new Websocket(validUri, null, null));
		}
		assertThrows(IllegalArgumentException.class, () -> new Websocket("http://example.com/socket"));
		assertThrows(NullPointerException.class, () -> new Websocket(null));
	}

	@Test
	public void testUnknownContentType() {
		String id = "USR-" + UUID.randomUUID(),
			invalidContentType = "       {\"content-type\":\"application/meme;lolz=9001\"},",
			json = "{\"id\":\""+id+"\",\n" +
				"  \"channels\": {\n" +
				"    \"websocket\": [{}," + invalidContentType +
				"       {\"content-type\":\"audio/l16;rate=16000\"}," +
				"       {\"headers\":{}}," +
				"       {\"uri\":\"wss://example.org/wall\"}" +
				"    ]\n" +
				"  }\n" +
				"}";

		assertThrows(VonageResponseParseException.class, () -> User.fromJson(json));

		User user = User.fromJson(json.replace(invalidContentType, ""));
		assertEquals(id, user.getId());
		Channels channels = user.getChannels();
		assertNotNull(channels);
		List<Websocket> websockets = channels.getWebsocket();
		assertNotNull(websockets);
		assertEquals(4, websockets.size());

		Websocket websocket0 = websockets.get(0);
		assertNotNull(websocket0);
		assertNull(websocket0.getUri());
		assertNull(websocket0.getContentType());
		assertNull(websocket0.getHeaders());

		Websocket websocket1 = websockets.get(1);
		assertEquals(Websocket.ContentType.AUDIO_L16_16K, websocket1.getContentType());
		assertNull(websocket1.getUri());
		assertNull(websocket1.getHeaders());

		Websocket websocket2 = websockets.get(2);
		assertNotNull(websocket2.getHeaders());
		assertEquals(0, websocket2.getHeaders().size());
		assertNull(websocket2.getContentType());
		assertNull(websocket2.getUri());

		Websocket websocket3 = websockets.get(3);
		assertNotNull(websocket3.getUri());
		assertNull(websocket3.getContentType());
		assertNull(websocket3.getHeaders());
	}
}
