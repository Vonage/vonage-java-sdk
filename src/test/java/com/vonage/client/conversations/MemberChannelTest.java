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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vonage.client.Jsonable;
import com.vonage.client.common.ChannelType;
import com.vonage.client.users.channels.Mms;
import com.vonage.client.users.channels.Pstn;
import com.vonage.client.users.channels.Sip;
import com.vonage.client.users.channels.Vbc;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemberChannelTest {
    final Map<String, ?> headers = Map.of(
            "Key", "Value",
            "Key2", 2,
            "k3", true,
            "k4", List.of("Aa", 'B', 0, false, Math.PI)
    );

    @Test
    public void testBuilderAllFields() {
        String id = "abc123";
        var from = new Mms("447900000001");
        var to = new Vbc(789);
        var channel = MemberChannel.builder()
                .id(id).from(from).to(to)
                .type(ChannelType.APP)
                .headers(headers)
                .build();

        assertEquals(id, channel.getId());
        assertEquals(from, channel.getFrom());
        assertEquals(to, channel.getTo());
        assertEquals(ChannelType.APP, channel.getType());
        assertEquals(headers, channel.getHeaders());
    }

    @Test
    public void testBuilderRequiredFields() {
        var channel = MemberChannel.builder().build();
        assertNull(channel.getId());
        assertNull(channel.getFrom());
        assertNull(channel.getTo());
        assertNull(channel.getType());
        assertNull(channel.getHeaders());
    }

    @Test
    public void testFromEmptyJson() {
        var channel = Jsonable.fromJson("{}", MemberChannel.class);
        assertNull(channel.getType());
        assertNull(channel.getFrom());
        assertNull(channel.getTo());
        assertNull(channel.getId());
        assertNull(channel.getHeaders());
    }

    @Test
    public void testFromJsonTypeOnly() {
        var channel = Jsonable.fromJson("{\"type\":\"app\"}", MemberChannel.class);
        assertEquals(ChannelType.APP, channel.getType());
        assertNull(channel.getFrom());
        assertNull(channel.getTo());
        assertNull(channel.getId());
        assertNull(channel.getHeaders());
    }

    @Test
    public void testFromJsonIdOnly() {
        String id = "abc123";
        var channel = Jsonable.fromJson("{\"id\":\""+id+"\"}", MemberChannel.class);
        assertEquals(id, channel.getId());
        assertNull(channel.getFrom());
        assertNull(channel.getTo());
        assertNull(channel.getType());
        assertNull(channel.getHeaders());
    }

    @Test
    public void testFromJsonFromAndToOnly() {
        var from = new Pstn("447900000001");
        var to = new Sip("sip:user@example.org");
        var channel = Jsonable.fromJson(
                "{\"from\":{\"type\":\"phone\",\"number\":\"447900000001\"}," +
                "\"to\":{\"type\":\"sip\",\"uri\":\"sip:user@example.org\"}}",
                MemberChannel.class
        );

        from.setTypeField();
        to.setTypeField();

        assertEquals(from, channel.getFrom());
        assertEquals(to, channel.getTo());
        assertNull(channel.getType());
        assertNull(channel.getId());
        assertNull(channel.getHeaders());
    }


    @Test
    public  void testFromJsonHeadersOnly() throws Exception {
        var headersJson = new ObjectMapper().writeValueAsString(headers);
        var channel = Jsonable.fromJson("{\"headers\":"+headersJson+"}", MemberChannel.class);
        assertEquals(new HashMap<>(headers).toString(), new HashMap<>(channel.getHeaders()).toString());
        assertNull(channel.getType());
        assertNull(channel.getFrom());
        assertNull(channel.getTo());
        assertNull(channel.getId());
    }

    @Test
    public void testInvalidFromNode() {
        var channel = Jsonable.fromJson("{\"from\":[]}", MemberChannel.class);
        assertNotNull(channel);
        assertNull(channel.getFrom());

        channel = Jsonable.fromJson("{\"to\":2}", MemberChannel.class);
        assertNotNull(channel);
        assertNull(channel.getTo());
        assertNull(channel.getType());
        assertNull(channel.getId());
        assertNull(channel.getHeaders());
    }
}
