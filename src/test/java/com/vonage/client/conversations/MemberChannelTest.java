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
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

public class MemberChannelTest {

    @Test
    public void testFromEmptyJson() {
        var channel = Jsonable.fromJson("{}", MemberChannel.class);
        assertNull(channel.getType());
        assertNull(channel.getFrom());
        assertNull(channel.getTo());
    }

    @Test
    public void testFromJsonTypeOnly() {
        var channel = Jsonable.fromJson("{\"type\":\"app\"}", MemberChannel.class);
        assertEquals(ChannelType.APP, channel.getType());
        assertNull(channel.getFrom());
        assertNull(channel.getTo());
    }

    @Test
    public void testInvalidFromNode() {
        var channel = Jsonable.fromJson("{\"from\":[]}", MemberChannel.class);
        assertNotNull(channel);
        assertNull(channel.getFrom());

        channel = Jsonable.fromJson("{\"to\":2}", MemberChannel.class);
        assertNotNull(channel);
        assertNull(channel.getTo());
    }
}
