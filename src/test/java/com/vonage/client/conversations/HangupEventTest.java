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

import com.vonage.client.Jsonable;
import com.vonage.client.common.ChannelType;
import com.vonage.client.users.channels.Pstn;
import com.vonage.client.voice.CallDirection;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import java.net.URI;
import java.util.List;

public class HangupEventTest extends AbstractEventTest {

    @Test
    public void testParseEmptyEvents() {
        for (var tuple : List.of(
                new ParseEventTuple(SipHangupEvent.class, EventType.SIP_HANGUP, "sip:hangup"),
                new ParseEventTuple(RtcHangupEvent.class, EventType.RTC_HANGUP, "rtc:hangup")
        )) {
            var event = (AbstractHangupEvent) parseEvent(
                    tuple.eventTypeEnum(), tuple.clazz(), "{\"body\":{\"channel\":{}},\"type\":\""+tuple.eventTypeStr()+"\"}"
            );
            assertNotNull(event.getChannel());
            assertNull(event.getBandwidth());
            assertNull(event.getDirection());
            assertNull(event.getReason());
            assertNull(event.getQuality());
        }
    }

    @Test
    public void testParseFullEvents() {
        for (var tuple : List.of(
                new ParseEventTuple(SipHangupEvent.class, EventType.SIP_HANGUP, "sip:hangup"),
                new ParseEventTuple(RtcHangupEvent.class, EventType.RTC_HANGUP, "rtc:hangup")
        )) {
            String body = """
               {
                   "id": 27,
                   "type": \"""" +tuple.eventTypeStr()+"\","+"""
                   "from": "string",
                   "body": {
                      "direction": "inbound",
                      "quality": {
                          "mos_score": 4,
                          "quality_percentage": 98,
                          "jitter_min_var": 1,
                          "jitter_max_var": 5,
                          "jitter_loss_rate": 2,
                          "jitter_burst_rate": 3,
                          "flaw_total": 3,
                          "packet_cnt": 1000,
                          "packet_loss_perc": 6
                      },
                      "bandwidth": {
                         "byte_in": 1234,
                         "byte_out": 56789
                      },
                      "channel": {
                         "id": "string",
                         "type": "app",
                         "to": {
                            "type": "phone",
                            "number": "447700900000"
                         },
                         "from": {
                            "type": "phone",
                            "number": "447700900001"
                         },
                         "headers": {}
                      },
                      "reason": {
                         "text": "A good one",
                         "code": "ID10T",
                         "sip_code": "ZIP code?"
                      }
                   },
                   "timestamp": "2020-01-01T14:00:00.00Z",
                   "_embedded": {
                      "from_user": {
                         "id": "USR-82e028d9-5201-4f1e-8188-604b2d3471ec",
                         "name": "my_user_name",
                         "display_name": "My User Name",
                         "image_url": "https://example.com/image.png",
                         "custom_data": {}
                      },
                      "from_member": {
                         "id": "string"
                      }
                   },
                   "_links": {
                      "self": {
                         "href": "https://api.nexmo.com/v1/conversations/CON-d66d47de-5bcb-4300-94f0-0c9d4b948e9a/events/27"
                      }
                   }
               }
            """;
            var event = (AbstractHangupEvent) parseEvent(tuple.eventTypeEnum(), tuple.clazz(), body);

            assertEquals(27, event.getId());
            assertEquals(tuple.eventTypeStr(), event.getTypeName());
            assertEquals(tuple.eventTypeEnum(), event.getType());
            assertEquals(tuple.clazz(), event.getClass());
            assertEquals("string", event.getFrom());
            assertNotNull(event.getTimestamp());
            assertEquals("2020-01-01T14:00:00Z", event.getTimestamp().toString());

            assertEquals(CallDirection.INBOUND, event.getDirection());

            var quality = event.getQuality();
            assertNotNull(quality);
            assertEquals(4, quality.getMosScore());
            assertEquals(98, quality.getQualityPercentage());
            assertEquals(1, quality.getJitterMinVar());
            assertEquals(5, quality.getJitterMaxVar());
            assertEquals(2, quality.getJitterLossRate());
            assertEquals(3, quality.getJitterBurstRate());
            assertEquals(3, quality.getFlawTotal());
            assertEquals(1000, quality.getPacketCnt());
            assertEquals(6, quality.getPacketLossPerc());

            var bandwidth = event.getBandwidth();
            assertNotNull(bandwidth);
            assertEquals(1234, bandwidth.getByteIn());
            assertEquals(56789, bandwidth.getByteOut());

            var reason = event.getReason();
            assertNotNull(reason);
            assertEquals("A good one", reason.getText());
            assertEquals("ID10T", reason.getCode());
            assertEquals("ZIP code?", reason.getSipCode());

            var channel = event.getChannel();
            assertNotNull(channel);
            assertEquals("string", channel.getId());
            assertEquals(ChannelType.APP, channel.getType());
            assertNotNull(channel.getTo());
            assertEquals(ChannelType.PHONE, channel.getTo().getType());
            assertEquals("447700900000", ((Pstn) channel.getTo()).getNumber());
            assertNotNull(channel.getFrom());
            assertEquals(ChannelType.PHONE, channel.getFrom().getType());
            assertEquals("447700900001", ((Pstn) channel.getFrom()).getNumber());

            var fromUser = event.getFromUser();
            assertNotNull(fromUser);
            assertEquals("USR-82e028d9-5201-4f1e-8188-604b2d3471ec", fromUser.getId());
            assertEquals("my_user_name", fromUser.getName());
            assertEquals("My User Name", fromUser.getDisplayName());
            assertEquals(URI.create("https://example.com/image.png"), fromUser.getImageUrl());

            var fromMember = event.getFromMember();
            assertNotNull(fromMember);
            assertEquals("string", fromMember.getId());
        }
    }

    @Test
    public void testFromEmptyJson() {
        for (var json : List.of("{}", "{\"body\":{}}")) {
            for (var clazz : List.of(SipHangupEvent.class, RtcHangupEvent.class)) {
                var event = Jsonable.fromJson(json, clazz);
                assertNotNull(event);
                assertNull(event.getChannel());
                assertNull(event.getBandwidth());
                assertNull(event.getDirection());
                assertNull(event.getReason());
                assertNull(event.getQuality());
            }
        }
    }
}
