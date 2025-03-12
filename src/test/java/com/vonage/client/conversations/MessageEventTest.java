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

import com.vonage.client.OrderedMap;
import static com.vonage.client.OrderedMap.entry;
import com.vonage.client.common.MessageType;
import static com.vonage.client.common.MessageType.*;
import static com.vonage.client.conversations.MessageEvent.builder;
import com.vonage.client.messages.whatsapp.Locale;
import com.vonage.client.messages.whatsapp.Policy;
import com.vonage.client.messages.whatsapp.Whatsapp;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import java.net.URI;
import java.util.List;
import java.util.Map;

public class MessageEventTest extends AbstractEventTest {
    final String text = "Hello world", url = "https://www.example.com/path/to/media.flv";

    MessageEvent testMessageEvent(MessageEvent.Builder builder, OrderedMap fields) {
        return testBaseEvent(EventType.MESSAGE, builder, fields);
    }

    @Test
    public void testMessageType() {
        assertNull(MessageType.fromString(null));
        assertEquals("text", TEXT.toString());
        assertEquals("image", IMAGE.toString());
        assertEquals("audio", AUDIO.toString());
        assertEquals("video", VIDEO.toString());
        assertEquals("file", FILE.toString());
        assertEquals("vcard", VCARD.toString());
        assertEquals("template", TEMPLATE.toString());
        assertEquals("custom", CUSTOM.toString());
        assertEquals("location", LOCATION.toString());
        assertEquals("sticker", STICKER.toString());
        assertEquals("unsupported", UNSUPPORTED.toString());
        assertEquals("reply", REPLY.toString());
        assertEquals("order", ORDER.toString());
        assertEquals("random", RANDOM.toString());
        assertEquals("button", BUTTON.toString());
        assertEquals("reaction", REACTION.toString());
        assertEquals("contact", CONTACT.toString());
    }

    @Test
    public void testUrlMediaEvents() {
        MessageType[] messageTypes = {IMAGE, AUDIO, VIDEO, FILE, VCARD};

        for (var messageType : messageTypes) {
            var event = testMessageEvent(
                    MessageEvent.builder(messageType).url(url),
                    new OrderedMap(
                            entry("message_type", messageType),
                            entry(messageType.toString(), entry("url", url))
                    )
            );
            assertEquals(messageType, event.getMessageType());
            assertEquals(URI.create(url), event.getUrl());
            assertNull(event.getText());
            assertNull(event.getLocation());

            assertThrows(IllegalStateException.class, () ->
                    applyBaseFields(builder(messageType).text(text)).build()
            );
        }
    }

    @Test
    public void testTextEvent() {
        var event = testMessageEvent(
                builder(TEXT).text(text),
                new OrderedMap(
                    entry("message_type", "text"),
                    entry("text", text)
                )
        );
        assertEquals(text, event.getText());
        assertNull(event.getUrl());
        assertNull(event.getLocation());
        assertNull(event.getCustom());
        assertNull(event.getTemplate());

        assertThrows(IllegalStateException.class, () ->
                applyBaseFields(builder(TEXT).url(url)).build()
        );
    }

    @Test
    public void testCustomEvent() {
        var payload = Map.of("foo", "bar", "baz", List.of(1, "qux", true), "zog", 3, "Map", Map.of("key", "value"));
        var event = testMessageEvent(
                builder(CUSTOM).custom(payload),
                new OrderedMap(
                        entry("message_type", "custom"),
                        entry("custom", payload)
                )
        );
        assertEquals(payload, event.getCustom());
        assertNull(event.getText());
        assertNull(event.getUrl());
        assertNull(event.getLocation());
        assertNull(event.getTemplate());

        assertThrows(IllegalStateException.class, () ->
                applyBaseFields(builder(CUSTOM).url(url)).build()
        );
    }

    @Test
    public void testTemplateEvent() {
        var template = new WhatsappTemplate(
                "Test template",
                List.of("param 1", "param 2"),
                new Whatsapp(Policy.DETERMINISTIC, Locale.DUTCH)
        );
        var event = testMessageEvent(
                builder(TEMPLATE).template(template),
                new OrderedMap(
                        entry("message_type", "template"),
                        entry("template", new OrderedMap(
                                entry("name", "Test template"),
                                entry("parameters", List.of("param 1", "param 2")),
                                entry("whatsapp", new OrderedMap(
                                        entry("policy", "deterministic"),
                                        entry("locale", "nl")
                                ))
                        ))
                )
        );
        assertEquals(template, event.getTemplate());
        assertNull(event.getText());
        assertNull(event.getUrl());
        assertNull(event.getLocation());
        assertNull(event.getCustom());

        assertThrows(IllegalStateException.class, () ->
                applyBaseFields(builder(TEMPLATE).url(url)).build()
        );
    }

    @Test
    public void testLocationEvent() {
        String name = "Vonage", address = "15 Bonhill St, London EC2A 4DN";
        double latitude = 51.52350767741431, longitude = -0.08532428836304748;
        var location = Location.builder()
                .latitude(latitude).longitude(longitude)
                .name(name).address(address).build();

        var event = testMessageEvent(
                builder(LOCATION).location(location),
                new OrderedMap(
                        entry("message_type", "location"),
                        entry("location", new OrderedMap(
                                entry("longitude", longitude),
                                entry("latitude", latitude),
                                entry("name", name),
                                entry("address", address)
                        ))
                )
        );

        assertNull(event.getUrl());
        assertNull(event.getText());
        assertNull(event.getCustom());
        assertNull(event.getTemplate());
        var parsedLocation = event.getLocation();
        assertNotNull(parsedLocation);
        assertEquals(location, parsedLocation);
        assertEquals(name, parsedLocation.getName());
        assertEquals(address, parsedLocation.getAddress());
        assertEquals(longitude, parsedLocation.getLongitude());
        assertEquals(latitude, parsedLocation.getLatitude());

        assertThrows(IllegalStateException.class, () ->
                applyBaseFields(builder(LOCATION).text(text)).build()
        );
        assertThrows(IllegalStateException.class, () ->
                applyBaseFields(builder(LOCATION).url(url)).build()
        );
        assertThrows(IllegalStateException.class, () ->
                applyBaseFields(builder(TEXT).location(location)).build()
        );
        assertThrows(IllegalStateException.class, () ->
                applyBaseFields(builder(LOCATION).custom(Map.of())).build()
        );
        assertThrows(IllegalStateException.class, () ->
                applyBaseFields(builder(LOCATION).template(new WhatsappTemplate())).build()
        );
    }
}
